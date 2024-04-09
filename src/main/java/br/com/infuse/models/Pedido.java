package br.com.infuse.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pedido")
public class Pedido {

	@Id
	@Column(name = "numero_pedido")
	@JsonProperty("numero_pedido")
	private Long id;

	@Column(name = "data_cadastro")
	@JsonProperty("data_cadastro")
	private Date dataCadastro;

	@Column(name = "nome_produto")
	@JsonProperty("nome_produto")
	private String nomeProduto;

	@Column(name = "valor_unitario")
	@JsonProperty("valor_unitario")
	private Double valorUnitario;

	@Column(name = "quantidade")
	@JsonProperty("quantidade")
	private Integer quantidade;

	@Column(name = "id_cliente")
	@JsonProperty("id_cliente")
	private Integer idCliente;

	@Column(name = "valor_total")
	@JsonIgnore
	private Double valorTotal;

}
