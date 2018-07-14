package br.com.aee.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.aee.model.Modulo;
import br.com.aee.model.Usuario;
import br.com.aee.repository.UsuarioRepository;
import br.com.aee.util.CDIServiceLocator;

public class AppUserDetailsService implements UserDetailsService {
	
	@Override
	public UserDetails loadUserByUsername(String matricula) throws UsernameNotFoundException {
		UsuarioRepository repository = CDIServiceLocator.getBean(UsuarioRepository.class);
		Usuario usuario = repository.findByMatricula(matricula.toUpperCase());
		
		UsuarioSistema user = null;
		
		if (usuario != null) {
			user = new UsuarioSistema(usuario, getNivelDeAcesso(usuario));
		}
		
		return user;
	}
	
	
	private Collection<? extends GrantedAuthority> getNivelDeAcesso(Usuario usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(usuario.getPerfil().getNome().toUpperCase()));
		
		for (Modulo modulo : usuario.getPerfil().getModulos()) {
			authorities.add(new SimpleGrantedAuthority(modulo.getNome().toUpperCase()));
		}
		
		return authorities;
	}
}
