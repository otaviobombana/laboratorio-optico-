package br.com.laboratoriooptico.converter;

import br.com.laboratoriooptico.facade.LenteFacade;
import br.com.laboratoriooptico.model.Lente;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

/**
 * Converte Lente <-> id, para usar a entidade em p:selectOneMenu.
 */
@FacesConverter(forClass = Lente.class, managed = true)
public class LenteConverter implements Converter<Lente> {

    @Inject
    private LenteFacade facade;

    @Override
    public Lente getAsObject(FacesContext context, UIComponent component, String valor) {
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
    public String getAsString(FacesContext context, UIComponent component, Lente objeto) {
        if (objeto == null || objeto.getId() == null) {
            return "";
        }
        return objeto.getId().toString();
    }
}
