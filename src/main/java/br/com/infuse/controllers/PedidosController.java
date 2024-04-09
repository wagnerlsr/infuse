package br.com.infuse.controllers;

import br.com.infuse.dtos.ResponseDto;
import br.com.infuse.models.Pedido;
import br.com.infuse.services.PedidosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
public class PedidosController {
	
	private final PedidosService pedidosService;


	@PostMapping(value = "/pedidos/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDto> pedidosJson(@RequestParam("file") MultipartFile file) {
		return ResponseEntity.status(HttpStatus.OK).body(pedidosService.gravarPedidosJson(file));
	}

	@PostMapping(value = "/pedidos/xml", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseDto> pedidosXml(@RequestParam("file") MultipartFile file) {
		return ResponseEntity.status(HttpStatus.OK).body(pedidosService.gravarPedidosXml(file));
	}

	@GetMapping(value = "/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Pedido>> pedidos() {
		return ResponseEntity.status(HttpStatus.OK).body(pedidosService.pedidos());
	}

	@GetMapping(value = "/pedidos/{idPedido}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> pedidosByNumeroPedido(@PathVariable("idPedido") Long idPedido) {
		return ResponseEntity.status(HttpStatus.OK).body(pedidosService.pedidosByNumeroPedido(idPedido));
	}

	@GetMapping(value = "/pedidos/cadastro/{dataCadastro}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> pedidosByDataCadastro(@PathVariable("dataCadastro") String dataCadastro) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			Date data =	formatter.parse(dataCadastro);

			return ResponseEntity.status(HttpStatus.OK).body(pedidosService.pedidosByDataCadastro(data));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					ResponseDto.builder().success(false).messages(Collections.singletonList("Data invalida")).build());
		}
	}

}
