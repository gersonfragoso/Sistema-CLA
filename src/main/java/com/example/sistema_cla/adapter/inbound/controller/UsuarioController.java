package com.example.sistema_cla.adapter.inbound.controller;


import com.example.sistema_cla.domain.model.Usuario;
import com.example.sistema_cla.application.service.UsuarioService;
import com.example.sistema_cla.infrastructure.exceptions.DuplicateUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.criarUsuario(usuario);
            return ResponseEntity.ok(novoUsuario);
        } catch (DuplicateUserException e) {
            return ResponseEntity.badRequest().body(null); // Ou uma mensagem customizada
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> usuarios = usuarioService.listarAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}/bloquear")
    public ResponseEntity<Void> bloquearUsuario(@PathVariable Long id) {
        usuarioService.bloquearUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
