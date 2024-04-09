-- pedidos.pedido definition

CREATE TABLE `pedido` (
  `numero_pedido` bigint NOT NULL,
  `data_cadastro` datetime(6) DEFAULT NULL,
  `id_cliente` int DEFAULT NULL,
  `nome_produto` varchar(255) DEFAULT NULL,
  `quantidade` int DEFAULT NULL,
  `valor_total` double DEFAULT NULL,
  `valor_unitario` double DEFAULT NULL,
  PRIMARY KEY (`numero_pedido`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
