package org.example;

public class PedidoEstadoEmTransito extends PedidoEstado {

    private PedidoEstadoEmTransito() {};
    private static PedidoEstadoEmTransito instance = new PedidoEstadoEmTransito();
    public static PedidoEstadoEmTransito getInstance() {
        return instance;
    }

    public String getEstado() {
        return "Em Transito";
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