package org.example;

public class PedidoEstadoFaturado extends PedidoEstado {

    private PedidoEstadoFaturado() {};
    private static PedidoEstadoFaturado instance = new PedidoEstadoFaturado();
    public static PedidoEstadoFaturado getInstance() {
        return instance;
    }

    public String getEstado() {
        return "Faturado";
    }

    public boolean transportar(Pedido pedido) {
        pedido.setEstado(PedidoEstadoEmTransito.getInstance());
        return true;
    }

    public boolean entregar(Pedido pedido) {
        pedido.setEstado(PedidoEstadoEntregue.getInstance());
        return true;
    }

    public boolean cancelar(Pedido pedido) {
        pedido.setEstado(PedidoEstadoCancelado.getInstance());
        return true;
    }
}