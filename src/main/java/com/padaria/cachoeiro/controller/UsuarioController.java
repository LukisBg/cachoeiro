package com.padaria.cachoeiro.controller;

import com.padaria.cachoeiro.model.Usuario;
import com.padaria.cachoeiro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", repository.findAll());
        return "usuarios";
    }

    @PostMapping("/salvar")
    public String salvar(Usuario usuario) {
        repository.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/usuarios";
    }

    @GetMapping("/buscar") // O caminho completo será /usuarios/buscar
    public String buscarUsuarios(@RequestParam(name = "termo", required = false) String termo, Model model) {
    
    if (termo == null || termo.trim().isEmpty()) {
        return "redirect:/usuarios";
    }

    System.out.println("Pesquisando usuário por: " + termo);

    List<Usuario> resultados = repository.findByNomeContainingIgnoreCaseOrLoginContainingIgnoreCase(termo, termo);
    model.addAttribute("usuarios", resultados);
    
    return "usuarios";
}
}