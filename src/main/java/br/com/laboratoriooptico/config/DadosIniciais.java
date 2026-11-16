package br.com.laboratoriooptico.config;

import br.com.laboratoriooptico.facade.UsuarioFacade;
import br.com.laboratoriooptico.model.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

/**
 * Na primeira execução (tabela de usuários vazia) cria o administrador:
 * login "admin", senha "admin123".
 */
@Singleton
@Startup
public class DadosIniciais {

    @EJB
    private UsuarioFacade usuarioFacade;

    @PostConstruct
    public void iniciar() {
        if (usuarioFacade.contar() == 0) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setLogin("admin");
            admin.setSenhaHash(Senha.hash("admin123"));
            admin.setAtivo(true);
            usuarioFacade.salvar(admin);
        }
    }
}
