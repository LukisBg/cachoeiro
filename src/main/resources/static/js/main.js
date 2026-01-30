let carrinho = [];

function openModal(id) {
    if (id === 'modalPagamento') {
        const elTotalVenda = document.getElementById('valorTotalVenda');
        const elInputHidden = document.getElementById('inputTotalVendaHidden');
        const elTotalModal = document.getElementById('totalModal');

        if (elTotalVenda) {
            const totalTexto = elTotalVenda.innerText;
            // Limpa TUDO: tira R$, tira o ponto e troca a vírgula decimal por ponto
            const totalLimpo = totalTexto
                .replace('R$ ', '')
                .replace(/\./g, '')
                .replace(',', '.');   

            if (elInputHidden) elInputHidden.value = totalLimpo;
            if (elTotalModal) elTotalModal.innerText = totalTexto;
        }
    }
    document.getElementById(id).style.display = 'block';
}

function closeModal(id) {
    const modal = document.getElementById(id);
    if (modal) {
        modal.style.display = 'none';
        limparFormulario(modal); // Limpa ao fechar para evitar erros no próximo clique
    }
}

// Fecha ao clicar fora da janela (na parte escura)
window.onclick = function(event) {
    if (event.target.className === 'modal') {
        event.target.style.display = 'none';
        limparFormulario(event.target);
    }
}

// Limpar o formulário
function limparFormulario(modal) {
    const form = modal.querySelector('form');
    if (form) {
        form.reset(); // Limpa os textos
        
        const inputId = document.getElementById('hiddenIdInput');
        if (inputId) {
            inputId.value = ''; 
        }
    }
    
    // Reseta o título original
    const titulo = modal.querySelector('h3');
    if (titulo) {
        if (modal.id === 'modalCliente') titulo.innerText = '👤 Novo Cliente';
    }
}

// --- LÓGICA DE EDIÇÃO DE CLIENTES ---

function preencherEdicao(botao) {
    // Pega os dados que guardamos no botão
    const id = botao.getAttribute('data-id');
    const nome = botao.getAttribute('data-nome');
    const cpf = botao.getAttribute('data-cpf');
    const tel = botao.getAttribute('data-telefone');

    // Preenche o modal de edição
    document.getElementById('editId').value = id;
    document.getElementById('editNome').value = nome;
    document.getElementById('editCpf').value = cpf;
    document.getElementById('editTel').value = tel;
    
    openModal('modalEditarCliente');
}

// --- LÓGICA DO CARRINHO DE VENDAS ---

function adicionarAoCarrinho() {
    const inputProd = document.getElementById('inputProduto');
    const inputQtd = document.getElementById('inputQtd');
    const datalist = document.getElementById('listaProdutos');
    
    let preco = 0;
    let opcaoSelecionada;
    
    if (datalist && inputProd) {
        opcaoSelecionada = Array.from(datalist.options).find(opt => opt.value === inputProd.value);
    }
    
    if (opcaoSelecionada) {
        preco = parseFloat(opcaoSelecionada.getAttribute('data-preco'));
        const item = {
            nome: inputProd.value,
            quantidade: parseInt(inputQtd.value),
            precoUnitario: preco,
            total: preco * parseInt(inputQtd.value)
        };

        carrinho.push(item);
        renderizarCarrinho();
        
        // Limpa campos após adicionar
        inputProd.value = '';
        inputQtd.value = '1';
        inputProd.focus();
    } else {
        alert("Selecione um produto válido da lista!");
    }
}

function renderizarCarrinho() {
    const corpoTabela = document.querySelector('#tabelaCarrinho tbody');
    if (!corpoTabela) return;

    corpoTabela.innerHTML = '';
    let totalGeral = 0;

    carrinho.forEach((item, index) => {
        totalGeral += item.total;
        corpoTabela.innerHTML += `
            <tr>
                <td>${item.nome}</td>
                <td>${item.quantidade}</td>
                <td>R$ ${item.precoUnitario.toFixed(2)}</td>
                <td>R$ ${item.total.toFixed(2)}</td>
                <td style="text-align: center;">
                    <button class="btn-icon" onclick="removerDoCarrinho(${index})">🗑️</button>
                </td>
            </tr>
        `;
    });

    const elTotalVenda = document.getElementById('valorTotalVenda');
    const elTotalModal = document.getElementById('totalModal');
    const textoTotal = `R$ ${totalGeral.toFixed(2).replace('.', ',')}`;
    
    if (elTotalVenda) elTotalVenda.innerText = textoTotal;
    if (elTotalModal) elTotalModal.innerText = textoTotal;
}

function removerDoCarrinho(index) {
    carrinho.splice(index, 1);
    renderizarCarrinho();
}

function abrirModalFinalizar() {
    if(carrinho.length === 0) {
        alert("O carrinho está vazio!");
        return;
    }
    const modal = document.getElementById('modalFinalizar');
    if(modal) modal.style.display = 'block';
}

function enviarVendaParaJava() {
    if(carrinho.length === 0) return;

    const jsonCarrinho = JSON.stringify(carrinho);
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/vendas/salvar';

    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'dadosVenda';
    input.value = jsonCarrinho;

    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
}

function preencherEdicaoProduto(elemento) {
    const id = elemento.getAttribute('data-id');
    const nome = elemento.getAttribute('data-nome');
    const preco = elemento.getAttribute('data-preco');
    const estoque = elemento.getAttribute('data-estoque');

    document.getElementById('editIdProd').value = id;
    document.getElementById('editNomeProd').value = nome;
    document.getElementById('editPrecoProd').value = preco;
    document.getElementById('editEstoqueProd').value = estoque;
    
    document.getElementById('editDescProd').value = nome; // Evita erro de descrição nula no Java
    
    openModal('modalEditarProduto');
}

function toggleCliente(valor) {
    const divCliente = document.getElementById('campoSeletorCliente');
    if (valor === 'ANOTADO') {
        divCliente.style.display = 'block';
    } else {
        divCliente.style.display = 'none';
    }
}

function verificarFormaPagamento(valor) {
    const divCliente = document.getElementById('seletorClienteAnotado');
    if (valor === 'ANOTADO') {
        divCliente.style.display = 'block';
    } else {
        divCliente.style.display = 'none';
    }
}

function prepararAbatimento(botao) {
    // Pega os dados direto do botão que foi clicado
    const id = botao.getAttribute('data-id');
    const nome = botao.getAttribute('data-nome');
    const valor = parseFloat(botao.getAttribute('data-valor'));

    document.getElementById('abaterId').value = id;
    document.getElementById('abaterNome').innerText = nome;
    document.getElementById('abaterValorAtual').innerText = 'R$ ' + valor.toLocaleString('pt-BR', {minimumFractionDigits: 2});
    
    openModal('modalAbater');
}