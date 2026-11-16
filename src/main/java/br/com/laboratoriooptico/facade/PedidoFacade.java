package br.com.laboratoriooptico.facade;

import br.com.laboratoriooptico.model.Pedido;
import br.com.laboratoriooptico.model.StatusPedido;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PedidoFacade {

    @PersistenceContext(unitName = "LaboratorioPU")
    private EntityManager em;

    /** Salva o pedido junto com os itens (cascade). */
    public Pedido salvar(Pedido pedido) {
        if (pedido.getId() == null) {
            em.persist(pedido);
            return pedido;
        }
        return em.merge(pedido);
    }

    /** Busca o pedido já com os itens carregados, para editar na tela. */
    public Pedido buscarComItens(Long id) {
        List<Pedido> resultado = em.createQuery(
                "SELECT DISTINCT p FROM Pedido p "
                + "LEFT JOIN FETCH p.itens "
                + "WHERE p.id = :id",
                Pedido.class
        )
        .setParameter("id", id)
        .getResultList();

        return resultado.isEmpty() ? null : resultado.get(0);
    }

    /** Lista os pedidos (com itens, para mostrar total); status null = todos. */
    public List<Pedido> listar(StatusPedido status) {
        String jpql = "SELECT DISTINCT p FROM Pedido p "
                + "JOIN FETCH p.otica "
                + "LEFT JOIN FETCH p.itens "
                + (status != null ? "WHERE p.status = :status " : "")
                + "ORDER BY p.dataPedido DESC, p.id DESC";

        var query = em.createQuery(jpql, Pedido.class);
        if (status != null) {
            query.setParameter("status", status);
        }
        return query.getResultList();
    }

    public long contarPorStatus(StatusPedido status) {
        return em.createQuery(
                "SELECT COUNT(p) FROM Pedido p WHERE p.status = :status", Long.class)
                .setParameter("status", status)
                .getSingleResult();
    }

    public void excluir(Long id) {
        Pedido pedido = em.find(Pedido.class, id);

        if (pedido != null) {
            em.remove(pedido);
        }
    }
}
