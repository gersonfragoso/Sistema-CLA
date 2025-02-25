# Sistema CLA 

## Descrição
O Sistema CLA tem como objetivo facilitar o compartilhamento de informações sobre locais acessíveis, ajudando pessoas com deficiência ou mobilidade reduzida a encontrar espaços que atendam às suas necessidades. A plataforma permitirá que usuários cadastrem, avaliem e consultem locais acessíveis, promovendo maior inclusão e acessibilidade urbana.

## Diagramas 
### Diagrama de Classes: (Aqui vai o diagrama de classe)

## Tecnologias Utilizadas
- **Linguagem**: Java
- **Framework**: Spring Boot
- **Persistência**: H2 (banco de dados em memória)
- **Arquitetura**: Hexagonal (Ports and Adapters)
- **Dependências**: Spring Data JPA, Lombok (opcional), Jakarta Persistence

## Estrutura do Projeto
O projeto segue a arquitetura hexagonal, dividindo-se em camadas para separar a lógica do domínio, os adaptadores (entrada e saída) e a infraestrutura. Abaixo está a estrutura de pacotes observada:

### 1. `adapter`
- **Inbound (entrada)**: Camada responsável por receber requisições externas, como APIs REST via `UsuarioController`.
- **Outbound (saída)**: Camada responsável por implementar a interação com recursos externos, como o banco de dados H2.
	 - * Entities : Estão as classes responsaveis por criar as entidades no banco.
	 - * Repositories: Aqui esta a interface JPA que vai ser pela a persistencia dos dados no banco.

### 2. `application`
- **Service**: Contém a lógica de negócios, implementada em `UsuarioService`, que atua como o "core" do domínio, interagindo com os ports.

### 3. `domain`
- **Model**: Define os modelos de domínio puro (`Usuario`, `Endereco`, `Telefone`), livres de dependências externas.
- **Repository**: Define o port `UsuarioRepository`, uma interface que expõe as operações de CRUD para o domínio, sem detalhes de implementação.

### 4. `infrastructure`
- **Exceptions**: Contém exceções personalizadas, como `DuplicateUserException`, para tratamento de erros específicos.
- **Utils**: Inclui utilitários, como `UsuarioMapper`, para mapeamento entre entidades e modelos de domínio.

## Funcionalidades
Ate o momento do projeto criamos apenas a base. Então as funcionalidades que estão aqui são as que foram pedidas para a entrega da tarefa atual.
- **Criação de Usuários**: Permite registrar novos usuários com informações como CPF, nome, telefone, endereço e status de bloqueio.
- **Busca de Usuários**: Recupera informações de um usuário por ID ou lista todos os usuários.
- **Bloqueio/Desbloqueio de Usuários**: Altera o status de bloqueio de um usuário.

## Pré-requisitos
- **JDK**: 17 ou superior
- **Maven**: 3.6 ou superior
- **IDE**: IntelliJ IDEA, Eclipse ou outra compatível com Java
- **H2**: Já incluído como dependência no projeto (não requer instalação separada)

### Json para teste de cadastro

- **Post**/usuarios 
```json
{
  "nome": "teste32",
  "sobrenome": "Silva",
  "cpf": "123.456.756-10",
  "dataNascimento": "1990-05-15",
  "endereco": {
    "rua": "Rua das Flores",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567",
    "numeroCasa": "123"
  },
  "telefone": {
    "ddd": "11",
    "numeroTelefone": "9276155321"
  },
  "bloqueado": false
}
```
