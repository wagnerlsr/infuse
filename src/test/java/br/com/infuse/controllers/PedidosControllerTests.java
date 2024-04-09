package br.com.infuse.controllers;

import br.com.infuse.dtos.ResponseDto;
import br.com.infuse.models.Pedido;
import br.com.infuse.services.PedidosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(PedidosController.class)
class PedidosControllerTests {

    @MockBean(name="pedidosService")
    PedidosService pedidosService;

    PedidosController pedidosController;

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

        pedidosController = new PedidosController(pedidosService);
    }

    @Test
    public void pedidos_Success() throws Exception {

        BDDMockito.given( pedidosService.pedidos()).willReturn(pedidos);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/pedidos");

        MockMvcBuilders.standaloneSetup(pedidosController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$").isArray())
                .andExpect( jsonPath("$").value(hasSize(2)))
                .andExpect( jsonPath("$[0].numero_pedido", equalTo(1001)));
    }

    @Test
    public void pedidosByNumeroPedido_Success() throws Exception {

        BDDMockito.given( pedidosService.pedidosByNumeroPedido(any())).willReturn(pedidos.get(0));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/pedidos/1001");

        MockMvcBuilders.standaloneSetup(pedidosController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.numero_pedido", equalTo(1001)));
    }

    @Test
    public void pedidosByNumeroPedido_NotFound() throws Exception {

        BDDMockito.given( pedidosService.pedidosByNumeroPedido(any())).willReturn(ResponseDto.builder().success(false).messages(Collections.singletonList("Pedido não localizado")).build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/pedidos/1");

        MockMvcBuilders.standaloneSetup(pedidosController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$.success", equalTo(false)))
                .andExpect( jsonPath("$.messages[0]", equalTo("Pedido não localizado")));
    }

    @Test
    public void pedidosByDataCadastro_Success() throws Exception {

        BDDMockito.given( pedidosService.pedidosByDataCadastro(any())).willReturn(pedidos);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/pedidos/cadastro/20240401");

        MockMvcBuilders.standaloneSetup(pedidosController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$").isArray())
                .andExpect( jsonPath("$").value(hasSize(2)))
                .andExpect( jsonPath("$[1].numero_pedido", equalTo(1002)));
    }

    @Test
    public void pedidosByDataCadastro_NotFound() throws Exception {

        BDDMockito.given( pedidosService.pedidosByDataCadastro(any())).willReturn(List.of());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/pedidos/cadastro/20240405");

        MockMvcBuilders.standaloneSetup(pedidosController).build().perform(request)
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( jsonPath("$").isArray())
                .andExpect( jsonPath("$").value(hasSize(0)));
    }

}
