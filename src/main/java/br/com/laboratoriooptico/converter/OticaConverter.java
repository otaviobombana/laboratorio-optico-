package br.com.laboratoriooptico.converter;

import br.com.laboratoriooptico.facade.OticaFacade;
import br.com.laboratoriooptico.model.Otica;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

/**
 * Converte Otica <-> id, para usar a entidade em p:selectOneMenu.
 */
@FacesConverter(forClass = Otica.class, managed = true)
public class OticaConverter implements Converter<Otica> {

    @Inject
    private OticaFacade facade;

    @Override
    public Otica getAsObject(FacesContext context, UIComponent component, String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return facade.buscarPorId(Long.valueOf(valor));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Otica objeto) {
        if (objeto == null || objeto.getId() == null) {
            return "";
        }
        return objeto.getId().toString();
    }
}
