package br.com.laboratoriooptico.converter;

import br.com.laboratoriooptico.facade.ArmacaoFacade;
import br.com.laboratoriooptico.model.Armacao;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

/**
 * Converte Armacao <-> id, para usar a entidade em p:selectOneMenu.
 */
@FacesConverter(forClass = Armacao.class, managed = true)
public class ArmacaoConverter implements Converter<Armacao> {

    @Inject
    private ArmacaoFacade facade;

    @Override
    public Armacao getAsObject(FacesContext context, UIComponent component, String valor) {
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
    public String getAsString(FacesContext context, UIComponent component, Armacao objeto) {
        if (objeto == null || objeto.getId() == null) {
            return "";
        }
        return objeto.getId().toString();
    }
}
