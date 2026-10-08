package org.example;

public abstract class PedidoEstado {

    public abstract String getEstado();

    public boolean aguardarPagamento(Pedido pedido) {
        return false;
    }

    public boolean faturar(Pedido pedido) {
        return false;
    }

    public boolean transportar(Pedido pedido) {
        return false;
    }

    public boolean entregar(Pedido pedido) {
        return false;
    }

    public boolean cancelar(Pedido pedido) {
        return false;
    }

}