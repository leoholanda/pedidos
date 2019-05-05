package net.bonsamigos.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import net.bonsamigos.model.Perfil;
import net.bonsamigos.service.PerfilService;

@FacesConverter(forClass = Perfil.class)
public class PerfilConverter implements Converter {

	@Inject
	private PerfilService perfilService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Perfil retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = perfilService.findBy(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Perfil perfil = (Perfil) value;
			return perfil.getId()== null ? null : perfil.getId().toString();
		}
		
		return "";
	}
}
