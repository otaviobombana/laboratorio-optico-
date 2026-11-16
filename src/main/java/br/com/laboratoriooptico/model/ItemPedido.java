package br.com.laboratoriooptico.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Item de um pedido: uma lente (e, se o laboratório montar, a armação)
 * com quantidade e valor unitário no momento do pedido.
 */
@Entity
@Table(name = "item_pedido")
public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lente_id", nullable = false)
    private Lente lente;

    @ManyToOne
    @JoinColumn(name = "armacao_id")
    private Armacao armacao;

    @Column(nullable = false)
    private int quantidade = 1;

    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    public ItemPedido() {
    }

    public BigDecimal getSubtotal() {
        if (valorUnitario == null) {
            return BigDecimal.ZERO;
        }
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Lente getLente() {
        return lente;
    }

    public void setLente(Lente lente) {
        this.lente = lente;
    }

    public Armacao getArmacao() {
        return armacao;
    }

    public void setArmacao(Armacao armacao) {
        this.armacao = armacao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}
