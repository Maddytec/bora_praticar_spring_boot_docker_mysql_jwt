package br.com.maddytec.cliente.security.service;

import br.com.maddytec.cliente.security.entities.Usuario;

import java.util.Optional;

public interface UsuarioService {

    Optional<Usuario> findByEmail(String email);

    Usuario save(Usuario usuario);
}
