package br.com.laboratoriooptico.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Pedido de lentes feito por uma ótica ao laboratório.
 * Um pedido tem vários itens (relacionamento one-to-many).
 */
@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "otica_id", nullable = false)
    private Otica otica;

    @Column(nullable = false, length = 150)
    private String paciente;

    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @Column(name = "data_previsao")
    private LocalDate dataPrevisao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status = StatusPedido.RECEBIDO;

    @Column(length = 500)
    private String observacao;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
    }

    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        itens.add(item);
    }

    public void removerItem(ItemPedido item) {
        itens.remove(item);
        item.setPedido(null);
    }

    public BigDecimal getValorTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedido item : itens) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public int getQuantidadeTotal() {
        int total = 0;
        for (ItemPedido item : itens) {
            total += item.getQuantidade();
        }
        return total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Otica getOtica() {
        return otica;
    }

    public void setOtica(Otica otica) {
        this.otica = otica;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public LocalDate getDataPrevisao() {
        return dataPrevisao;
    }

    public void setDataPrevisao(LocalDate dataPrevisao) {
        this.dataPrevisao = dataPrevisao;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }
}
