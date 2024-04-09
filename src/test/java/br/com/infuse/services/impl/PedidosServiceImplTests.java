package br.com.infuse.services.impl;

import br.com.infuse.InfuseApplication;
import br.com.infuse.dtos.ResponseDto;
import br.com.infuse.models.Pedido;
import br.com.infuse.repositories.PedidoRepository;
import br.com.infuse.services.PedidosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InfuseApplication.class)
class PedidosServiceImplTests {

    @MockBean(name="pedidoRepository")
    PedidoRepository pedidoRepository;
    PedidosService pedidosService;

    List<Pedido> pedidos;


    @BeforeEach
    public void setUp() {
        pedidos = Arrays.asList(
                Pedido.builder()
                        .id(1001L)
                        .nomeProduto("produto 1")
                        .valorUnitario(100.00)
                        .quantidade(1)
                        .build(),
                Pedido.builder()
                        .id(1002L)
                        .nomeProduto("produto 2")
                        .valorUnitario(200.00)
                        .quantidade(2)
                        .build()
        );

        pedidosService = new PedidosServiceImpl(pedidoRepository);
    }

    @Test
    public void pedidos_Success() {

        when( pedidoRepository.findAll()).thenReturn(pedidos);

        List<Pedido> result = pedidosService.pedidos();

        assertThat( result.size() ).isEqualTo(2);
        assertThat( result.containsAll(pedidos) ).isTrue();

    }

    @Test
    public void pedidosByNumeroPedido_Success() {

        when( pedidoRepository.findById(anyLong())).thenReturn(Optional.ofNullable(pedidos.get(0)));

        Pedido result = (Pedido) pedidosService.pedidosByNumeroPedido(1001L);

        assertThat( result ).isNotNull();
        assertThat( result.getId() ).isEqualTo(1001L);

    }

    @Test
    public void pedidosByNumeroPedido_NotFound() {

        when( pedidoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseDto result = (ResponseDto) pedidosService.pedidosByNumeroPedido(1L);

        assertThat( result ).isNotNull();
        assertThat( result ).isInstanceOf(ResponseDto.class);
        assertThat( result.getSuccess() ).isFalse();
        assertThat( result.getMessages() ).contains("Pedido não localizado");

    }

    @Test
    public void pedidosByDataCadastro_Success() {

        when( pedidoRepository.findAllByDataCadastro(any())).thenReturn(pedidos);

        List<Pedido> result = pedidosService.pedidosByDataCadastro(new Date());

        assertThat( result.size() ).isEqualTo(2);
        assertThat( result.containsAll(pedidos) ).isTrue();
    }

    @Test
    public void pedidosByDataCadastro_NotFound() {

        when( pedidoRepository.findAllByDataCadastro(any())).thenReturn(List.of());

        List<Pedido> result = pedidosService.pedidosByDataCadastro(new Date());

        assertThat( result.size() ).isEqualTo(0);

    }

}
