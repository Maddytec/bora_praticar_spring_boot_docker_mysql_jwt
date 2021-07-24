package br.com.maddytec.cliente.security.resource;

import br.com.maddytec.cliente.security.entities.Usuario;
import br.com.maddytec.cliente.security.request.UserRequest;
import br.com.maddytec.cliente.security.request.UsuarioJwtRequest;
import br.com.maddytec.cliente.security.response.UserResponse;
import br.com.maddytec.cliente.security.response.UsuarioJwtResponse;
import br.com.maddytec.cliente.security.service.UsuarioService;
import br.com.maddytec.cliente.security.service.impl.UserDetailsServiceImpl;
import br.com.maddytec.cliente.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Log4j2
@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UsuarioResource {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public UserResponse save(@RequestBody @Valid UserRequest userRequest){

        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(userRequest, usuario);
        usuario.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        usuarioService.save(usuario);
        log.info("Usuario cadastrado com sucesso: {}", usuario );

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(usuario, userResponse);

        return userResponse;
    }

    @PostMapping("/autenticacao")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioJwtResponse getAutenticacao(@RequestBody UsuarioJwtRequest usuarioJwtRequest){
        try{
            UserDetails userDetails = userDetailsServiceImpl.autenticar(usuarioJwtRequest);
            String token = jwtUtil.obterToken(usuarioJwtRequest);
            return  UsuarioJwtResponse.builder()
                    .email(userDetails.getUsername())
                    .token(token)
                    .build();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }



}
