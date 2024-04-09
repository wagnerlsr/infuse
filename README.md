# Infuse

### Vaga Programador Java
##

Arquivos contidos na pasta _assets_ do projeto:

* **INFUSE.postman_collection.json** - _Collection com as chamadas da API._
* **pedidos.json** - _Arquivo com pedidos no format JSON._
* **pedidos.xml** - _Arquivo com pedidos no formato XML._
* **script_pedidos.sql** - _Script para criação da tabela de pedidos._

###
Considerações sobre o projeto:

* O arquivo _**docker-compose.yml**_ contem a configuração do banco MYSQL, basta executa-lo antes de executar o projeto.
* A configuração de JPA do Datasource no arquivo _**application.properties**_ esta configurada para criação automatica das entidades no banco de dados (_spring.jpa.hibernate.ddl-auto=update_).
* Caso queiram fazer a criação da tabela manualmente mudar a configuração para (_spring.jpa.hibernate.ddl-auto=none_).

###
AWS:

* Instalei o projeto na cloud para que possam testar com mais facilidade:
  * http://54.160.42.69:3000/infuse/v1/pedidos

