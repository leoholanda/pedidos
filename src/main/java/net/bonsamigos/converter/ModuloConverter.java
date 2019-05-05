package net.bonsamigos.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import net.bonsamigos.model.Modulo;
import net.bonsamigos.repository.ModuloRepository;

@FacesConverter("moduloConverter")
public class ModuloConverter implements Converter {
	
	@Inject
	private ModuloRepository moduloRepository;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Modulo retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = moduloRepository.findBy(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Modulo modulo = (Modulo) value;
			return modulo.getId()== null ? null : modulo.getId().toString();
		}
		
		return "";
	}

}
