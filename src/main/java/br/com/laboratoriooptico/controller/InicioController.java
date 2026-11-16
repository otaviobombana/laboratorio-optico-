package br.com.laboratoriooptico.controller;

import br.com.laboratoriooptico.facade.PedidoFacade;
import br.com.laboratoriooptico.model.StatusPedido;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.EnumMap;
import java.util.Map;

/**
 * Painel inicial: quantidade de pedidos em cada status.
 */
@Named
@RequestScoped
public class InicioController {

    @EJB
    private PedidoFacade pedidoFacade;

    private final Map<StatusPedido, Long> totais = new EnumMap<>(StatusPedido.class);

    @PostConstruct
    public void iniciar() {
        for (StatusPedido status : StatusPedido.values()) {
            totais.put(status, pedidoFacade.contarPorStatus(status));
        }
    }

    public StatusPedido[] getStatusPossiveis() {
        return StatusPedido.values();
    }

    public long total(StatusPedido status) {
        return totais.getOrDefault(status, 0L);
    }
}
