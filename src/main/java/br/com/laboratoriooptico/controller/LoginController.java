package br.com.laboratoriooptico.controller;

import br.com.laboratoriooptico.facade.UsuarioFacade;
import br.com.laboratoriooptico.model.Usuario;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Nome do atributo de sessão que o filtro verifica. */
    public static final String USUARIO_LOGADO = "usuarioLogado";

    @EJB
    private UsuarioFacade usuarioFacade;

    private String login;
    private String senha;

    public String entrar() {
        Usuario usuario = usuarioFacade.autenticar(login, senha);
        senha = null;

        if (usuario == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Acesso negado", "Usuário ou senha inválidos."));
            return null;
        }

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getSessionMap().put(USUARIO_LOGADO, usuario);
        return "/admin/index.xhtml?faces-redirect=true";
    }

    public String sair() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public Usuario getUsuarioLogado() {
        return (Usuario) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get(USUARIO_LOGADO);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
