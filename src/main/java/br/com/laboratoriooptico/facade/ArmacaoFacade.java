package br.com.laboratoriooptico.facade;

import br.com.laboratoriooptico.model.Armacao;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ArmacaoFacade {

    @PersistenceContext(unitName = "LaboratorioPU")
    private EntityManager em;

    public void salvar(Armacao armacao) {
        if (armacao.getId() == null) {
            em.persist(armacao);
        } else {
            em.merge(armacao);
        }
    }

    public Armacao buscarPorId(Long id) {
        return em.find(Armacao.class, id);
    }

    public List<Armacao> listarTodos() {
        return em.createQuery(
                "SELECT a FROM Armacao a ORDER BY a.marca, a.modelo",
                Armacao.class
        ).getResultList();
    }

    public List<Armacao> listarAtivas() {
        return em.createQuery(
                "SELECT a FROM Armacao a WHERE a.ativo = true ORDER BY a.marca, a.modelo",
                Armacao.class
        ).getResultList();
    }

    public List<Armacao> pesquisar(String pesquisa) {
        return em.createQuery(
                "SELECT a FROM Armacao a "
                + "WHERE LOWER(a.codigo) LIKE :pesquisa "
                + "OR LOWER(a.marca) LIKE :pesquisa "
                + "OR LOWER(a.modelo) LIKE :pesquisa "
                + "ORDER BY a.marca, a.modelo",
                Armacao.class
        )
        .setParameter("pesquisa", "%" + pesquisa.toLowerCase() + "%")
        .getResultList();
    }

    public void excluir(Long id) {
        Armacao armacao = buscarPorId(id);

        if (armacao != null) {
            em.remove(armacao);
        }
    }
}
