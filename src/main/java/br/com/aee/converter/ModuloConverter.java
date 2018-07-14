package br.com.aee.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.aee.model.Modulo;
import br.com.aee.repository.ModuloRepository;


@FacesConverter("moduloConverter")
public class ModuloConverter implements Converter {

	@Inject
	private ModuloRepository moduloRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Modulo retorno = null;

		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = moduloRepository.findById(id);
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Modulo equipamento = (Modulo) value;
			return equipamento.getId() == null ? null : equipamento.getId().toString();
		}

		return "";
	}

}