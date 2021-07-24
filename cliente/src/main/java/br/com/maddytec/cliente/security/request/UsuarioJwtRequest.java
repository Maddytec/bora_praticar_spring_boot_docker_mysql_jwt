package br.com.maddytec.cliente.security.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioJwtRequest {

    private String email;
    private String password;




}