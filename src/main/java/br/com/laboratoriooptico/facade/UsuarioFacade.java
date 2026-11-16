package br.com.laboratoriooptico.facade;

import br.com.laboratoriooptico.config.Senha;
import br.com.laboratoriooptico.model.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UsuarioFacade {

    @PersistenceContext(unitName = "LaboratorioPU")
    private EntityManager em;

    public void salvar(Usuario usuario) {
        if (usuario.getId() == null) {
            em.persist(usuario);
        } else {
            em.merge(usuario);
        }
    }

    public long contar() {
        return em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class)
                .getSingleResult();
    }

    /**
     * Devolve o usuário se login e senha conferem e ele está ativo; senão, null.
     */
    public Usuario autenticar(String login, String senha) {
        List<Usuario> encontrados = em.createQuery(
                "SELECT u FROM Usuario u "
                + "WHERE u.login = :login AND u.senhaHash = :senha AND u.ativo = true",
                Usuario.class
        )
        .setParameter("login", login.trim())
        .setParameter("senha", Senha.hash(senha))
        .getResultList();

        return encontrados.isEmpty() ? null : encontrados.get(0);
    }
}
