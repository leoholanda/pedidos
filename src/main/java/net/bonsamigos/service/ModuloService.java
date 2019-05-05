package net.bonsamigos.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.bonsamigos.model.Modulo;
import net.bonsamigos.repository.ModuloRepository;

@Service
public class ModuloService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ModuloRepository moduloRepository;
	
	public List<Modulo> findAll() {
		return moduloRepository.findAllOrderByNome();
	}
}
