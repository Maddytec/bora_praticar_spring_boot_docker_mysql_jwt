package br.com.maddytec.cliente.security.service.impl;

import br.com.maddytec.cliente.security.entities.Usuario;
import br.com.maddytec.cliente.security.enums.PerfilEnum;
import br.com.maddytec.cliente.security.repository.UsuarioRepository;
import br.com.maddytec.cliente.security.request.UsuarioJwtRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado: " + username));

        return User
                .builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(mapToGrantedAuthorities(usuario.getPerfil()))
                .build();
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(PerfilEnum perfilEnum) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(perfilEnum.toString()));
        return authorities;
    }

    public UserDetails autenticar(UsuarioJwtRequest usuarioJwtRequest) throws Exception {
        UserDetails userDetails =  loadUserByUsername(usuarioJwtRequest.getEmail());
        boolean senhaValida = passwordEncoder.matches(usuarioJwtRequest.getPassword(), userDetails.getPassword());

        if(senhaValida){
            return userDetails;
        }
        throw new Exception("Senha Invalida.");
    }

}
