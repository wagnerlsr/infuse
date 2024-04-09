package br.com.infuse.services;

import br.com.infuse.dtos.ResponseDto;
import br.com.infuse.models.Pedido;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;


public interface PedidosService {

	ResponseDto gravarPedidosJson(MultipartFile file);
	ResponseDto gravarPedidosXml(MultipartFile file);
	List<Pedido> pedidos();
	List<Pedido> pedidosByDataCadastro(Date data);
	Object pedidosByNumeroPedido(Long id);

}
