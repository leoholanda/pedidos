package br.com.aee.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.aee.model.Usuario;
import br.com.aee.repository.UsuarioRepository;
import br.com.aee.util.CDIServiceLocator;

@FacesConverter(forClass = Usuario.class)
public class UsuarioConverter implements Converter {

    //@Inject
    private UsuarioRepository usuarios;

    public UsuarioConverter() {
        this.usuarios = (UsuarioRepository) CDIServiceLocator.getBean(UsuarioRepository.class);
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Usuario retorno = null;

        if (value != null) {
            retorno = this.usuarios.findBy(new Long(value));
        }

        return retorno;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return ((Usuario) value).getId().toString();
        }
        return "";
    }

}
