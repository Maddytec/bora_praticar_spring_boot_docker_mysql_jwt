package br.com.maddytec.cliente.security.utils;

import br.com.maddytec.cliente.security.request.UsuarioJwtRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Date gerarDataExpiracao() {

        return new Date(System.currentTimeMillis() + expiration * 60000);
    }

    public String obterToken(UsuarioJwtRequest usuarioJwtRequest) {
        return Jwts.builder()
                .setSubject(usuarioJwtRequest.getEmail())
                .setExpiration(gerarDataExpiracao())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Claims getClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsuario(String token) throws ExpiredJwtException {
        Claims claims = getClaims(token);
        return  claims.getSubject();
    }

    public Date getDataExpiracao(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }

    private boolean isTokenExpirado(String token) {
        final Date dataExpiracao = getDataExpiracao(token);
        return dataExpiracao.before(new Date());
    }

    public boolean isTokenValido(String token){
        return !isTokenExpirado(token);
    }


}
