package br.com.laboratoriooptico.controller;

import br.com.laboratoriooptico.facade.ArmacaoFacade;
import br.com.laboratoriooptico.facade.LenteFacade;
import br.com.laboratoriooptico.facade.OticaFacade;
import br.com.laboratoriooptico.facade.PedidoFacade;
import br.com.laboratoriooptico.model.Armacao;
import br.com.laboratoriooptico.model.ItemPedido;
import br.com.laboratoriooptico.model.Lente;
import br.com.laboratoriooptico.model.Otica;
import br.com.laboratoriooptico.model.Pedido;
import br.com.laboratoriooptico.model.StatusPedido;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Funcionalidade principal: pedido de lentes com vários itens.
 */
@Named
@ViewScoped
public class PedidoController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PedidoFacade pedidoFacade;

    @EJB
    private OticaFacade oticaFacade;

    @EJB
    private LenteFacade lenteFacade;

    @EJB
    private ArmacaoFacade armacaoFacade;

    private Pedido pedido;
    private List<Pedido> pedidos;
    private StatusPedido filtroStatus;

    // Campos do item que está sendo adicionado
    private Lente lenteItem;
    private Armacao armacaoItem;
    private Integer quantidadeItem = 1;

    private List<Otica> oticas;
    private List<Lente> lentes;
    private List<Armacao> armacoes;

    @PostConstruct
    public void iniciar() {
        oticas = oticaFacade.listarAtivas();
        lentes = lenteFacade.listarAtivas();
        armacoes = armacaoFacade.listarAtivas();
        novo();
        atualizarLista();
    }

    public void novo() {
        pedido = new Pedido();
        pedido.setDataPedido(LocalDate.now());
        pedido.setStatus(StatusPedido.RECEBIDO);
        limparItem();
    }

    public void adicionarItem() {
        if (lenteItem == null) {
            adicionarMensagem(FacesMessage.SEVERITY_WARN, "Atenção", "Escolha a lente do item.");
            return;
        }
        if (quantidadeItem == null || quantidadeItem < 1) {
            adicionarMensagem(FacesMessage.SEVERITY_WARN, "Atenção", "A quantidade deve ser pelo menos 1.");
            return;
        }

        BigDecimal valor = lenteItem.getValor();
        if (armacaoItem != null) {
            valor = valor.add(armacaoItem.getValor());
        }

        ItemPedido item = new ItemPedido();
        item.setLente(lenteItem);
        item.setArmacao(armacaoItem);
        item.setQuantidade(quantidadeItem);
        item.setValorUnitario(valor);
        pedido.adicionarItem(item);

        limparItem();
    }

    public void removerItem(ItemPedido item) {
        pedido.removerItem(item);
    }

    public void salvar() {
        if (pedido.getItens().isEmpty()) {
            adicionarMensagem(FacesMessage.SEVERITY_WARN, "Atenção",
                    "Adicione pelo menos um item ao pedido.");
            return;
        }
        if (pedido.getDataPrevisao() != null
                && pedido.getDataPrevisao().isBefore(pedido.getDataPedido())) {
            adicionarMensagem(FacesMessage.SEVERITY_WARN, "Atenção",
                    "A previsão de entrega não pode ser antes da data do pedido.");
            return;
        }

        try {
            pedidoFacade.salvar(pedido);
            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso", "Pedido salvo com sucesso!");
            novo();
            atualizarLista();
        } catch (Exception e) {
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Erro",
                    "Não foi possível salvar o pedido: " + e.getMessage());
        }
    }

    public void editar(Pedido selecionado) {
        pedido = pedidoFacade.buscarComItens(selecionado.getId());
        limparItem();
        if (pedido == null) {
            adicionarMensagem(FacesMessage.SEVERITY_WARN, "Atenção", "Pedido não encontrado.");
            novo();
        }
    }

    public void excluir(Pedido selecionado) {
        try {
            pedidoFacade.excluir(selecionado.getId());
            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso", "Pedido excluído com sucesso!");
            if (selecionado.getId().equals(pedido.getId())) {
                novo();
            }
            atualizarLista();
        } catch (Exception e) {
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Erro", "Não foi possível excluir o pedido.");
        }
    }

    public void atualizarLista() {
        pedidos = pedidoFacade.listar(filtroStatus);
    }

    private void limparItem() {
        lenteItem = null;
        armacaoItem = null;
        quantidadeItem = 1;
    }

    private void adicionarMensagem(FacesMessage.Severity tipo, String titulo, String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(tipo, titulo, detalhe));
    }

    public StatusPedido[] getStatusPossiveis() {
        return StatusPedido.values();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public StatusPedido getFiltroStatus() {
        return filtroStatus;
    }

    public void setFiltroStatus(StatusPedido filtroStatus) {
        this.filtroStatus = filtroStatus;
    }

    public Lente getLenteItem() {
        return lenteItem;
    }

    public void setLenteItem(Lente lenteItem) {
        this.lenteItem = lenteItem;
    }

    public Armacao getArmacaoItem() {
        return armacaoItem;
    }

    public void setArmacaoItem(Armacao armacaoItem) {
        this.armacaoItem = armacaoItem;
    }

    public Integer getQuantidadeItem() {
        return quantidadeItem;
    }

    public void setQuantidadeItem(Integer quantidadeItem) {
        this.quantidadeItem = quantidadeItem;
    }

    public List<Otica> getOticas() {
        return oticas;
    }

    public List<Lente> getLentes() {
        return lentes;
    }

    public List<Armacao> getArmacoes() {
        return armacoes;
    }
}
