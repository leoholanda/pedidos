package net.bonsamigos.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import net.bonsamigos.model.Modulo;
import net.bonsamigos.model.Usuario;
import net.bonsamigos.service.UsuarioService;
import net.bonsamigos.util.CDIServiceLocator;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
		UsuarioService service = CDIServiceLocator.getBean(UsuarioService.class);
		Usuario usuario = service.loadUsuarioAutorizado(cpf);

		UsuarioSistema user = null;

		if (usuario != null) {
			user = new UsuarioSistema(usuario, getNivelDeAcesso(usuario));
		} else {
			throw new UsernameNotFoundException("Usuário não encontrado!");
		}
		return user;
	}
	
//	public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
//		UsuarioService service = CDIServiceLocator.getBean(UsuarioService.class);
//		Usuario usuario = service.loadUsuarioAutorizado(cpf);
//
//		UsuarioSistema user = null;
//
//		if (usuario != null) {
//			user = new UsuarioSistema(usuario, getNivelDeAcesso(usuario));
//		} else {
//			throw new UsernameNotFoundException("Usuário não encontrado.");
//		}
//		return user;
//	}

	private Collection<? extends GrantedAuthority> getNivelDeAcesso(Usuario usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(usuario.getPerfil().getNome().toUpperCase()));

		for (Modulo modulo : usuario.getPerfil().getModulos()) {
			authorities.add(new SimpleGrantedAuthority(modulo.getNome().toUpperCase()));
		}

		return authorities;
	}
}
