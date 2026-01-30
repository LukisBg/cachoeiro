package com.padaria.cachoeiro.controller;

import com.padaria.cachoeiro.model.Cliente;
import com.padaria.cachoeiro.model.Produto;
import com.padaria.cachoeiro.model.Venda;
import com.padaria.cachoeiro.model.Anotado;
import com.padaria.cachoeiro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class NavegacaoController {

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private AnotadoRepository anotadoRepository;
    @Autowired
    private VendaRepository vendaRepository;

    @GetMapping("/")
    public String telaLogin() {
        return "index";
    }

@GetMapping("/dashboard")
public String telaDashboard(Model model) {
    var vendas = vendaRepository.findAll();
    
    // Soma robusta: trata nulos e lista vazia
    double total = (vendas == null) ? 0.0 : vendas.stream()
            .filter(v -> v != null)
            .mapToDouble(v -> v.getTotal() != null ? v.getTotal() : 0.0)
            .sum();

    model.addAttribute("ultimasVendas", vendas);
    model.addAttribute("totalVendas", total);
    model.addAttribute("produtosFalta", produtoRepository.count() > 0 ? 
        produtoRepository.findAll().stream().filter(p -> p.getEstoque() < 5).count() : 0);
    
    return "dashboard";
}

    // --- PRODUTOS ---
    @GetMapping("/produtos")
    public String telaProdutos(Model model) {
        model.addAttribute("produtos", produtoRepository.findAll());
        return "produtos";
    }

    @PostMapping("/produtos/salvar")
    public String salvarProduto(@ModelAttribute Produto produto) {
        try {
            if (produto.getDescricao() == null || produto.getDescricao().isBlank()) {
                produto.setDescricao(produto.getNome());
            }
            produtoRepository.save(produto);
            return "redirect:/produtos";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/produtos?erro";
        }
    }

    @GetMapping("/produtos/excluir/{id}")
    public String excluirProduto(@PathVariable Long id) {
        produtoRepository.deleteById(id);
        return "redirect:/produtos";
    }
    
    @GetMapping("/produtos/buscar")
public String buscarProdutos(@RequestParam("nome") String nome, Model model) {
    // Busca produtos que contenham o nome digitado (ignora maiúsculas/minúsculas)
    List<Produto> resultados = produtoRepository.findByNomeContainingIgnoreCase(nome);
    
    model.addAttribute("produtos", resultados);
    return "produtos"; 
}

    // --- CLIENTES ---
    @GetMapping("/clientes")
    public String telaClientes(Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("totalClientes", clienteRepository.count());
        return "clientes";
    }

    @PostMapping("/clientes/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente) {
        clienteRepository.save(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/excluir/{id}")
    public String excluirCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
        return "redirect:/clientes";
    }
    
@GetMapping("/clientes/buscar")
public String buscarClientes(@RequestParam(name = "nome", required = false) String termo, Model model) {
    
    System.out.println("VALOR RECEBIDO DO HTML: [" + termo + "]");

    if (termo == null || termo.trim().isEmpty()) {
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("totalClientes", clienteRepository.count());
    } else {
        List<Cliente> resultados = clienteRepository.findByNomeContainingIgnoreCaseOrCpfContaining(termo, termo);
        model.addAttribute("clientes", resultados);
        model.addAttribute("totalClientes", resultados.size());
    }
    return "clientes";
}

    // --- VENDAS ---
    @GetMapping("/vendas")
    public String telaVendas(Model model) {
        model.addAttribute("produtos", produtoRepository.findAll());
        model.addAttribute("clientes", clienteRepository.findAll()); 
        return "vendas";
    }

    @PostMapping("/api/vendas/finalizar")
    public String finalizarVenda(@RequestParam("formaPagamento") String forma,
                                 @RequestParam(value = "clienteId", required = false) Long clienteId,
                                 @RequestParam("totalVendaReal") Double totalVendaReal) {
        
        // 1. Salva a Venda Geral (Histórico de Caixa)
        Venda venda = new Venda();
        venda.setTotal(totalVendaReal);
        venda.setFormaPagamento(forma);
        vendaRepository.save(venda);

        // 2. Lógica específica para Fiado (ANOTADO)
        if ("ANOTADO".equals(forma) && clienteId != null) {
            Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
            
            if (cliente != null) {
                // Busca se já existe uma dívida para este cliente ou cria uma nova
                Anotado debito = anotadoRepository.findByNomeCliente(cliente.getNome())
                                                 .orElse(new Anotado());

                if (debito.getId() == null) {
                    debito.setNomeCliente(cliente.getNome());
                    debito.setValorDevido(0.0);
                    debito.setDataLancamento(java.time.LocalDate.now());
                }

                // Soma o valor da venda atual ao saldo devedor
                debito.setValorDevido(debito.getValorDevido() + totalVendaReal);
                debito.setStatus("PENDENTE");
                
                anotadoRepository.save(debito);
            }
        }
        return "redirect:/vendas?sucesso";
    }

    // --- ANOTADOS ---
    @GetMapping("/anotados")
    public String telaAnotados(Model model) {
    var lista = anotadoRepository.findAll();
    
    double totalGeral = (lista == null) ? 0.0 : lista.stream()
            .filter(a -> a != null)
            .mapToDouble(a -> a.getValorDevido() != null ? a.getValorDevido() : 0.0)
            .sum();

    model.addAttribute("listaAnotados", lista);
    model.addAttribute("totalGeral", totalGeral);
    model.addAttribute("totalDevedores", lista != null ? lista.size() : 0);
    return "anotados";
}
    
@PostMapping("/anotados/abater")
public String abaterDivida(@RequestParam("id") Long id, @RequestParam("valorPago") Double valorPago) {
    try {
        // Busca a dívida ou lança erro se não existir
        var anotado = anotadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dívida não encontrada"));
        
        // Se valorDevido for null, trata como 0.0
        double saldoAtual = (anotado.getValorDevido() != null) ? anotado.getValorDevido() : 0.0;
        
        // Faz a conta
        double novoSaldo = saldoAtual - valorPago;
        
        if (novoSaldo <= 0) {
            anotado.setValorDevido(0.0);
            anotado.setStatus("PAGO");
        } else {
            anotado.setValorDevido(novoSaldo);
            anotado.setStatus("PARCIAL");
        }
        
        anotadoRepository.save(anotado);
    } catch (Exception e) {
        
        System.err.println("ERRO CRÍTICO NO ABATIMENTO: " + e.getMessage());
        return "redirect:/anotados?erro";
    }
    return "redirect:/anotados?sucesso";
}

    @GetMapping("/anotados/buscar")
    public String buscarAnotados(@RequestParam(name = "nome", required = false) String nome, Model model) {
    if (nome == null || nome.trim().isEmpty()) {
        return "redirect:/anotados";
    }

    List<Anotado> resultados = anotadoRepository.findByNomeClienteContainingIgnoreCase(nome.trim());
    
    // Cálculo do total apenas para os resultados encontrados
    double totalGeral = resultados.stream()
            .mapToDouble(a -> a.getValorDevido() != null ? a.getValorDevido() : 0.0)
            .sum();

    model.addAttribute("listaAnotados", resultados);
    model.addAttribute("totalGeral", totalGeral);
    model.addAttribute("totalDevedores", resultados.size());
    
    return "anotados";
}
}