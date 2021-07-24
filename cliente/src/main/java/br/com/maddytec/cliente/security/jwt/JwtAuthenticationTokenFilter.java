package br.com.maddytec.cliente.security.jwt;

import br.com.maddytec.cliente.security.service.impl.UserDetailsServiceImpl;
import br.com.maddytec.cliente.security.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	private static final String AUTH_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	private UserDetailsServiceImpl userDetailsServiceImpl;
	private JwtUtil jwtUtil;

	public JwtAuthenticationTokenFilter(UserDetailsServiceImpl userDetailsServiceImpl, JwtUtil jwtUtil) {
		this.userDetailsServiceImpl = userDetailsServiceImpl;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String usuario = null;
		String token = request.getHeader(AUTH_HEADER);

		if (token != null && token.startsWith(BEARER_PREFIX)) {
			token = token.substring(7);
			usuario = jwtUtil.getUsuario(token);
		}

		if (usuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(usuario);

			if (jwtUtil.isTokenValido(token) && userDetails != null) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);

	}
}