package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PedidoTest {

    Pedido pedido;
    Cliente cliente;

    @BeforeEach
    public void setUp() {
        pedido = new Pedido();
        pedido.setNome("Pedido 1");
        cliente = new Cliente("Cliente 1");
    }

    // ==========================================
    // TESTES DE NOTIFICAÇÃO (OBSERVER)
    // ==========================================

    @Test
    void deveNotificarUmCliente() {
        cliente.acompanhar(pedido);
        pedido.faturar();
        assertEquals("Cliente 1, status do seu pedido mudou: Pedido{nome='Pedido 1', estado='Faturado'}", cliente.getUltimaNotificacao());
    }

    @Test
    void deveNotificarClientes() {
        Cliente cliente1 = new Cliente("Cliente 1");
        Cliente cliente2 = new Cliente("Cliente 2");
        cliente1.acompanhar(pedido);
        cliente2.acompanhar(pedido);
        pedido.faturar();
        assertEquals("Cliente 1, status do seu pedido mudou: Pedido{nome='Pedido 1', estado='Faturado'}", cliente1.getUltimaNotificacao());
        assertEquals("Cliente 2, status do seu pedido mudou: Pedido{nome='Pedido 1', estado='Faturado'}", cliente2.getUltimaNotificacao());
    }

    @Test
    void naoDeveNotificarCliente() {
        pedido.faturar();
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    void deveNotificarClientePedidoA() {
        Pedido pedidoA = new Pedido();
        pedidoA.setNome("Pedido A");
        Pedido pedidoB = new Pedido();
        pedidoB.setNome("Pedido B");
        Cliente cliente1 = new Cliente("Cliente 1");
        Cliente cliente2 = new Cliente("Cliente 2");

        cliente1.acompanhar(pedidoA);
        cliente2.acompanhar(pedidoB);

        pedidoA.faturar();

        assertEquals("Cliente 1, status do seu pedido mudou: Pedido{nome='Pedido A', estado='Faturado'}", cliente1.getUltimaNotificacao());
        assertEquals(null, cliente2.getUltimaNotificacao());
    }

    // ==========================================
    // TESTES DE TRANSIÇÃO E NOTIFICAÇÃO (STATE + OBSERVER)
    // ==========================================

    // Pedido Aguardando Pagamento

    @Test
    public void naoDeveAguardarPagamentoPedidoAguardandoPagamento() {
        pedido.setEstado(PedidoEstadoAguardandoPagamento.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.aguardarPagamento());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void deveFaturarPedidoAguardandoPagamento() {
        pedido.setEstado(PedidoEstadoAguardandoPagamento.getInstance());
        cliente.acompanhar(pedido);
        assertTrue(pedido.faturar());
        assertEquals(PedidoEstadoFaturado.getInstance(), pedido.getEstado());
        assertEquals("Cliente 1, status do seu pedido mudou: Pedido{nome='Pedido 1', estado='Faturado'}", cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveTransportarPedidoAguardandoPagamento() {
        pedido.setEstado(PedidoEstadoAguardandoPagamento.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.transportar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveEntregarPedidoAguardandoPagamento() {
        pedido.setEstado(PedidoEstadoAguardandoPagamento.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.entregar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void deveCancelarPedidoAguardandoPagamento() {
        pedido.setEstado(PedidoEstadoAguardandoPagamento.getInstance());
        cliente.acompanhar(pedido);
        assertTrue(pedido.cancelar());
        assertEquals(PedidoEstadoCancelado.getInstance(), pedido.getEstado());
        assertEquals("Cliente 1, status do seu pedido mudou: Pedido{nome='Pedido 1', estado='Cancelado'}", cliente.getUltimaNotificacao());
    }

    // Pedido Faturado

    @Test
    public void naoDeveAguardarPagamentoPedidoFaturado() {
        pedido.setEstado(PedidoEstadoFaturado.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.aguardarPagamento());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveFaturarPedidoFaturado() {
        pedido.setEstado(PedidoEstadoFaturado.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.faturar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void deveTransportarPedidoFaturado() {
        pedido.setEstado(PedidoEstadoFaturado.getInstance());
        cliente.acompanhar(pedido);
        assertTrue(pedido.transportar());
        assertEquals(PedidoEstadoEmTransito.getInstance(), pedido.getEstado());
        assertEquals("Cliente 1, status do seu pedido mudou: Pedido{nome='Pedido 1', estado='Em Transito'}", cliente.getUltimaNotificacao());
    }

    @Test
    public void deveEntregarPedidoFaturado() {
        pedido.setEstado(PedidoEstadoFaturado.getInstance());
        cliente.acompanhar(pedido);
        assertTrue(pedido.entregar());
        assertEquals(PedidoEstadoEntregue.getInstance(), pedido.getEstado());
        assertEquals("Cliente 1, status do seu pedido mudou: Pedido{nome='Pedido 1', estado='Entregue'}", cliente.getUltimaNotificacao());
    }

    @Test
    public void deveCancelarPedidoFaturado() {
        pedido.setEstado(PedidoEstadoFaturado.getInstance());
        cliente.acompanhar(pedido);
        assertTrue(pedido.cancelar());
        assertEquals(PedidoEstadoCancelado.getInstance(), pedido.getEstado());
        assertEquals("Cliente 1, status do seu pedido mudou: Pedido{nome='Pedido 1', estado='Cancelado'}", cliente.getUltimaNotificacao());
    }

    // Pedido Em Trânsito

    @Test
    public void naoDeveAguardarPagamentoPedidoEmTransito() {
        pedido.setEstado(PedidoEstadoEmTransito.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.aguardarPagamento());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveFaturarPedidoEmTransito() {
        pedido.setEstado(PedidoEstadoEmTransito.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.faturar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveTransportarPedidoEmTransito() {
        pedido.setEstado(PedidoEstadoEmTransito.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.transportar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void deveEntregarPedidoEmTransito() {
        pedido.setEstado(PedidoEstadoEmTransito.getInstance());
        cliente.acompanhar(pedido);
        assertTrue(pedido.entregar());
        assertEquals(PedidoEstadoEntregue.getInstance(), pedido.getEstado());
        assertEquals("Cliente 1, status do seu pedido mudou: Pedido{nome='Pedido 1', estado='Entregue'}", cliente.getUltimaNotificacao());
    }

    @Test
    public void deveCancelarPedidoEmTransito() {
        pedido.setEstado(PedidoEstadoEmTransito.getInstance());
        cliente.acompanhar(pedido);
        assertTrue(pedido.cancelar());
        assertEquals(PedidoEstadoCancelado.getInstance(), pedido.getEstado());
        assertEquals("Cliente 1, status do seu pedido mudou: Pedido{nome='Pedido 1', estado='Cancelado'}", cliente.getUltimaNotificacao());
    }

    // Pedido Entregue

    @Test
    public void naoDeveAguardarPagamentoPedidoEntregue() {
        pedido.setEstado(PedidoEstadoEntregue.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.aguardarPagamento());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveFaturarPedidoEntregue() {
        pedido.setEstado(PedidoEstadoEntregue.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.faturar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveTransportarPedidoEntregue() {
        pedido.setEstado(PedidoEstadoEntregue.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.transportar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveEntregarPedidoEntregue() {
        pedido.setEstado(PedidoEstadoEntregue.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.entregar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveCancelarPedidoEntregue() {
        pedido.setEstado(PedidoEstadoEntregue.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.cancelar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    // Pedido Cancelado

    @Test
    public void naoDeveAguardarPagamentoPedidoCancelado() {
        pedido.setEstado(PedidoEstadoCancelado.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.aguardarPagamento());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveFaturarPedidoCancelado() {
        pedido.setEstado(PedidoEstadoCancelado.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.faturar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveTransportarPedidoCancelado() {
        pedido.setEstado(PedidoEstadoCancelado.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.transportar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveEntregarPedidoCancelado() {
        pedido.setEstado(PedidoEstadoCancelado.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.entregar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }

    @Test
    public void naoDeveCancelarPedidoCancelado() {
        pedido.setEstado(PedidoEstadoCancelado.getInstance());
        cliente.acompanhar(pedido);
        assertFalse(pedido.cancelar());
        assertEquals(null, cliente.getUltimaNotificacao());
    }
}