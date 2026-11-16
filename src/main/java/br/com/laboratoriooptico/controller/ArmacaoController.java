package br.com.laboratoriooptico.controller;

import br.com.laboratoriooptico.facade.ArmacaoFacade;
import br.com.laboratoriooptico.model.Armacao;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ArmacaoController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ArmacaoFacade facade;

    private Armacao armacao;
    private List<Armacao> armacoes;
    private String pesquisa;

    @PostConstruct
    public void iniciar() {
        novo();
        atualizarLista();
    }

    public void salvar() {
        try {
            facade.salvar(armacao);
            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso", "Armação salva com sucesso!");
            novo();
            atualizarLista();
        } catch (Exception e) {
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Erro",
                    "Não foi possível salvar a armação. Verifique se o código já existe.");
        }
    }

    public void novo() {
        armacao = new Armacao();
        armacao.setAtivo(true);
    }

    public void editar(Armacao selecionada) {
        armacao = facade.buscarPorId(selecionada.getId());
    }

    public void excluir(Armacao selecionada) {
        try {
            facade.excluir(selecionada.getId());
            adicionarMensagem(FacesMessage.SEVERITY_INFO, "Sucesso", "Armação excluída com sucesso!");
            atualizarLista();
        } catch (Exception e) {
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Erro",
                    "Não foi possível excluir a armação. Verifique se ela está vinculada a algum pedido.");
        }
    }

    public void pesquisar() {
        if (pesquisa == null || pesquisa.trim().isEmpty()) {
            atualizarLista();
        } else {
            armacoes = facade.pesquisar(pesquisa.trim());
        }
    }

    public void atualizarLista() {
        armacoes = facade.listarTodos();
    }

    private void adicionarMensagem(FacesMessage.Severity tipo, String titulo, String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(tipo, titulo, detalhe));
    }

    public Armacao getArmacao() {
        return armacao;
    }

    public void setArmacao(Armacao armacao) {
        this.armacao = armacao;
    }

    public List<Armacao> getArmacoes() {
        return armacoes;
    }

    public String getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(String pesquisa) {
        this.pesquisa = pesquisa;
    }
}
