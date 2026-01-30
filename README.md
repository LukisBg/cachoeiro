🍞 Padaria Cachoeiro - Sistema de Gestão Web
Este projeto é um sistema de gerenciamento desenvolvido para a Padaria Cachoeiro, focado na transição de processos manuais para uma plataforma digital. Ele permite o controle de estoque, gerenciamento de clientes, usuários e a operação de vendas, com foco especial no controle de vendas a prazo ("Fiado").

🚀 Funcionalidades Principais
Dashboard Inteligente: Resumo de vendas e alertas automáticos de estoque baixo.

Gestão de Estoque (CRUD): Cadastro, edição e exclusão de produtos com busca dinâmica.

Controle de Clientes: Base de dados de clientes com busca por CPF ou Nome.

Módulo de Usuários: Gerenciamento de acessos com níveis diferenciados (Administrador e Funcionário).

Sistema de Vendas: Registro de vendas à vista e "Anotadas".

Gestão Financeira (Anotados): Controle de dívidas por cliente, permitindo abatimentos parciais ou totais e atualização automática de status (Pendente, Parcial, Pago).

🛠️ Tecnologias Utilizadas
Linguagem: Java 17

Framework: Spring Boot 3

Template Engine: Thymeleaf (Renderização dinâmica do Front-end)

Persistência: Spring Data JPA / Hibernate

Banco de Dados: MySQL

Segurança: Autenticação customizada via LoginController

Gestão de Projeto: Jira (Bugtracking) e Git (Versionamento)

📦 Como Rodar o Projeto
1. Pré-requisitos
Java JDK 17 ou superior.

MySQL Server instalado e rodando.

Maven (geralmente embutido no NetBeans/IntelliJ).

2. Configuração do Banco de Dados
Crie um banco de dados no MySQL chamado padaria_db (ou o nome definido no seu properties) e execute o script SQL disponível na pasta /sql do projeto.

3. Configuração do application.properties
Ajuste as credenciais de acesso ao banco no arquivo src/main/resources/application.properties:

Properties
spring.datasource.url=jdbc:mysql://localhost:3306/nome_do_seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
4. Execução
Importe o projeto no NetBeans como um "Maven Project".

Aguarde o download das dependências.

Clique com o botão direito no projeto e selecione Run.

Acesse no navegador: http://localhost:8081 (ou a porta configurada).

📋 Documentação e Testes
O projeto conta com um Plano de Testes Manuais e um Relatório de Bugtracking (gerado via Jira), que documentam as falhas identificadas e corrigidas durante o desenvolvimento, garantindo a estabilidade da versão final.

✒️ Autor
Ivan Lucas Ferreira Borges - Desenvolvedor
