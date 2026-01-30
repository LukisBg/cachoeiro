package com.padaria.cachoeiro.controller;

import com.padaria.cachoeiro.model.Usuario;
import com.padaria.cachoeiro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository repository;

    @PostMapping("/login")
    public String autenticar(@RequestParam String login, @RequestParam String senha, RedirectAttributes redirect) {
        // Busca o usuário que você inseriu via SQL
        Usuario user = repository.findByLogin(login).orElse(null);

        if (user != null && user.getSenha().equals(senha)) {
            // Se der certo, vai para o dashboard
            return "redirect:/dashboard";
        }
        
        // Se der errado, volta para o index com mensagem de erro
        redirect.addFlashAttribute("erro", "Usuário ou senha inválidos!");
        return "redirect:/";
    }
}