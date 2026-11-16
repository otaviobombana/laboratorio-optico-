package br.com.laboratoriooptico.model;

/**
 * Situação de um pedido de lentes dentro do laboratório.
 */
public enum StatusPedido {

    RECEBIDO("Recebido", "info"),
    EM_PRODUCAO("Em produção", "warning"),
    PRONTO("Pronto", "success"),
    ENTREGUE("Entregue", "secondary"),
    CANCELADO("Cancelado", "danger");

    private final String descricao;

    /** Cor da etiqueta (p:tag) do PrimeFaces. */
    private final String cor;

    StatusPedido(String descricao, String cor) {
        this.descricao = descricao;
        this.cor = cor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCor() {
        return cor;
    }
}
