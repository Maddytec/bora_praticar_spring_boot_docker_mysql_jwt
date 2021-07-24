package br.com.maddytec.cliente.security.service.impl;

import br.com.maddytec.cliente.security.entities.Usuario;
import br.com.maddytec.cliente.security.repository.UsuarioRepository;
import br.com.maddytec.cliente.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Optional<Usuario> findByEmail(String email) {

        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuario save(Usuario usuario) {

        return usuarioRepository.save(usuario);
    }
}
