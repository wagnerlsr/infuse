package br.com.infuse.services.impl;

import br.com.infuse.dtos.ResponseDto;
import br.com.infuse.models.Pedido;
import br.com.infuse.repositories.PedidoRepository;
import br.com.infuse.services.PedidosService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PedidosServiceImpl implements PedidosService {

	private final PedidoRepository pedidoRepository;

	@Override
	public List<Pedido> pedidos() {
		return pedidoRepository.findAll();
	}

	@Override
	public Object pedidosByNumeroPedido(Long id) {
		Pedido pedido = pedidoRepository.findById(id).orElse(null);

		if (pedido == null)
			return ResponseDto.builder().success(false).messages(Collections.singletonList("Pedido não localizado")).build();
		else
			return pedido;
	}

	@Override
	public List<Pedido> pedidosByDataCadastro(Date data) {
		return pedidoRepository.findAllByDataCadastro(data);
	}

	@Override
	public ResponseDto gravarPedidosXml(MultipartFile file) {
		try {
			String xml = new String(file.getBytes(), StandardCharsets.UTF_8);

			XmlMapper xmlMapper = new XmlMapper();
			xmlMapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));

			List<Pedido> pedidos = xmlMapper.readValue(xml, new TypeReference<>() {});
			List<String> messages = new ArrayList<>();

			pedidos.forEach((Pedido pedido) -> gravaPedido(pedido, messages));

			return ResponseDto.builder().success(true).messages(messages).build();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseDto.builder().success(false).messages(Collections.singletonList(e.getMessage())).build();
		}
	}

	@Override
	public ResponseDto gravarPedidosJson(MultipartFile file) {
		try {
			ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));

			String json = new String(file.getBytes(), StandardCharsets.UTF_8);

			List<Pedido> pedidos = objectMapper.readValue(json, new TypeReference<>() {});
			List<String> messages = new ArrayList<>();

			pedidos.forEach((Pedido pedido) -> gravaPedido(pedido, messages));

			return ResponseDto.builder().success(true).messages(messages).build();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseDto.builder().success(false).messages(Collections.singletonList(e.getMessage())).build();
		}
	}

	private void gravaPedido(Pedido pedido, List<String> messages) {
		if (pedido.getId() == null) {
			messages.add(String.format("Número de pedido inválido [ %s ]", pedido));
		} else {
			if (pedidoRepository.findById(pedido.getId()).isPresent()) {
				messages.add(String.format("Número de pedido já cadastrado [ %d ]", pedido.getId()));
			} else {
				if (pedido.getNomeProduto() == null || pedido.getNomeProduto().isBlank()) {
					messages.add(String.format("Pedido não cadastrado [ %d ] [ Nome do produto é obrigatorio ]", pedido.getId()));
					return;
				}

				if (pedido.getValorUnitario() == null || pedido.getValorUnitario() <= 0) {
					messages.add(String.format("Pedido não cadastrado [ %d ] [ Valor unitário é obrigatorio ]", pedido.getId()));
					return;
				}

				if (pedido.getIdCliente() == null || pedido.getIdCliente() < 1 || pedido.getIdCliente() > 10) {
					messages.add(String.format("Pedido não cadastrado [ %d ] [ Cliente inválido ]", pedido.getId()));
					return;
				}

				try {
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					if (pedido.getDataCadastro() == null)
						pedido.setDataCadastro(formatter.parse(formatter.format(new Date())));
				} catch (Exception e) {}

				if (pedido.getQuantidade() == null || pedido.getQuantidade() <= 0) pedido.setQuantidade(1);

				pedido.setValorTotal(pedido.getQuantidade() * pedido.getValorUnitario());

				if (pedido.getQuantidade() >= 10)
					pedido.setValorTotal(pedido.getValorTotal() * 1.1);
				else if (pedido.getQuantidade() > 5)
					pedido.setValorTotal(pedido.getValorTotal() * 1.05);

				pedidoRepository.save(pedido);

				messages.add(String.format("Pedido [ %d ] cadastrado com sucesso", pedido.getId()));
			}

		}
	}
}
