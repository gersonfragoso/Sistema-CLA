# 📌 Sistema CLA - Compartilhamento de Locais com Acessibilidade

O **Sistema CLA** é uma plataforma colaborativa que permite que usuários cadastrem, avaliem e consultem locais acessíveis para pessoas com deficiência ou mobilidade reduzida.

## 📖 Funcionalidades
- 📌 **Cadastro e Login de Usuários**
- 🏢 **Cadastro de Locais Acessíveis**
- ⭐ **Avaliação de Locais**
- 🔍 **Pesquisa e Filtros de Locais** ( ainda não disponivel)
- 🔧 **Administração de Critérios de Acessibilidade** ( ainda não disponivel)

---

## 🚀 Tecnologias Utilizadas
- **Linguagem:** Java 21 ☕
- **Framework:** Spring Boot 🌱
- **Banco de Dados:** PostgreSQL 🐘
- **Persistência:** JPA/Hibernate 📄
- **Gerenciamento de Dependências:** Maven 🛠️
- **Segurança:** Spring Security 🔒 (Futuro)

## 🛠️ Instalação e Configuração

### 🔧 **Pré-requisitos**
1. **Java 21** instalado ([Download](https://jdk.java.net/21/))
2. **PostgreSQL** instalado e rodando (Observação nesse momento estamos utilizando apenas o h2 pois o projeto esta em desenvolvimento)
3. **Maven** instalado

### Json para teste

- **Post**/usuarios 
- Observação: Nesse primeiro momentos as informações de endereço e telefone podem estar nulas.
```json
{
	"nome": "João",
	"sobrenome": "Silva",
	"cpf": "123.456.789-00",
	"email": "joao.silva@example.com",
	"senha": "senha123",
	"dataNascimento": "1990-05-10"
}
```

- **Post**/locais 
- statusAcessibilidade é um enum que possue ate o momento as seguintes opções: `ACESSIVEL`, `PARCIALMENTE_ACESSIVEL`, `INACESSIVEL`
```json
{
	"nome": "Restaurante Acessível",
	"endereco": "Rua das Flores, 123",
	"tipoLocal": "Restaurante",
	"statusAcessibilidade": "ACESSIVEL"
}
```

- **Post**/avaliar
```json
{
	"usuarioId": 1,
	"localId": 1,
	"nota": 5,
	"comentario": "Ótimo local, muito acessível!"
}
```

# 📋 Casos de Uso

## [UC01] Cadastrar Usuário
**Descrição:** Este caso de uso é responsável pelo cadastro das informações do usuário no sistema.  
**Ator:** Usuário  
**Pré-condições:** O usuário deve fornecer informações válidas.  
**Pós-condições:** Os dados do usuário devem estar armazenados no sistema.  

### Fluxo Principal [FP]
1. Este caso de uso se inicia quando o usuário seleciona a opção “Cadastro” na interface do sistema.
2. O sistema disponibiliza um formulário que contém campos para os dados do usuário (Nome, sobrenome, CPF, e-mail, data de nascimento, endereço e telefone).
3. O usuário informa os dados no formulário.
4. O sistema valida os dados informados ([FS01]).
5. O sistema armazena os dados informados e envia a mensagem “Dados do usuário inseridos com sucesso”.

### Fluxo Secundário de Exceção [FS01]
1. Caso o usuário forneça algum dado incorreto, o sistema invalida o dado informado.
2. O sistema executa novamente o passo 2 do [FP], disponibilizando apenas os campos de dados informados incorretamente.

---

## [UC02] Efetuar Login
**Descrição:** Este caso de uso é responsável por efetuar o login do usuário no sistema.  
**Ator:** Usuário  
**Pré-condições:** O usuário deve possuir login e senha válidos.  
**Pós-condições:** O usuário deve estar logado no sistema.  

### Fluxo Principal [FP]
1. Este caso de uso se inicia quando o usuário deseja efetuar login no sistema.
2. O sistema disponibiliza um formulário que contém campos para os dados do usuário: “login” e “senha”.
3. O usuário informa os dados solicitados no formulário.
4. O sistema valida os dados informados ([FS01]).
5. O sistema libera o acesso às funcionalidades de acordo com o perfil do usuário.

### Fluxo Secundário de Exceção [FS01]
1. Caso o usuário forneça algum dado incorreto, o sistema envia a mensagem “Login ou senha incorretos”.
2. O sistema executa novamente o passo 2 do [FP].

---

## [UC03] Avaliar Local
**Descrição:** Este caso de uso é responsável pelo cadastro das avaliações do usuário no sistema.  
**Ator:** Usuário  
**Pré-condições:** O usuário deve estar logado no sistema.  
**Pós-condições:** As avaliações do usuário devem estar armazenadas no sistema.  

### Fluxo Principal [FP]
1. O caso de uso se inicia quando o usuário deseja avaliar um local.
2. O sistema exibe um formulário contendo os seguintes campos: Título, Nota e Comentários.
3. O usuário preenche o formulário com as informações solicitadas.
4. O sistema valida os dados informados ([FS01]).
5. Se os dados forem válidos, o sistema registra a avaliação e exibe uma confirmação ao usuário.
6. O caso de uso é encerrado.

### Fluxo Secundário de Exceção [FS01]
1. Caso o usuário forneça um local não cadastrado, o sistema envia a seguinte mensagem “Local inválido”.
2. O sistema executa novamente o passo 2 do [FP].

# 📋 Casos de Uso do Usuario

![Image](https://github.com/user-attachments/assets/b7814809-31ad-45a1-a681-315396f2e601)

- Observação ainda faltar fazer o caso de uso do Usuario do tipo admin (`MODERADOR`) e do vistante (`VISITANTE`)


# 📋 Diagrama de classes

## 📋 Diagrama de classes v1

![Image](https://github.com/user-attachments/assets/05b57d59-9e25-400a-b2ea-236975fe461f)

Essa primeira versão contem apenas o esboço inicial do projeto.

## 📋 Diagrama de classes v2 

![Image](https://github.com/user-attachments/assets/86445c92-f54e-4c28-bd77-b61fd095dec5)

Essa segunda versão acabamos exagerando um pouco e criamos de maneira que não ficou bom de visualizar, por este motivo dá para notar bastante mudanças nos diagramas mais novos comparados a este.

## 📋 Diagrama de classes v3 simplificando

![Image](https://github.com/user-attachments/assets/ad9b329d-6a03-48a1-aed2-d1bab6f85332)

Nesta versão do diagrama realizamos a simplificação do diagrama anterior juntamente com a adição do design pattern FacadeSingletonController que será responsável por uma interface única para todos os controllers.

## 📋 Diagrama de classes v4 com DAO

![image](https://github.com/user-attachments/assets/af2625d6-da35-4740-909a-825ae911c19c)

Na versão 4 trocamos a utilização do JPA para fazer a persistência no banco pelo padrão DAO, e também mapeamos os design patterns que estão sendo utilizados no diagrama.

## 📋 Diagrama de classes v5 com Adapter
![Image](https://github.com/user-attachments/assets/68d80fab-157a-4893-85ea-2959ea46c6d0)

Por fim, na versão 5 adicionamos o design pattern Adapter para a comunicação externa da nossa aplicação com o Google Maps.

## 🏗️ Melhorias Futuras

- 🔐 **Autenticação e Autorização** (Spring Security + JWT)
- 🌎 **Integração com Google Maps API**
- 📊 **Relatórios administrativos** para moderadores
- 📱 **Interface Web** para facilitar a usabilidade
- ### 🛠️ Funções de Admin

- **Gerenciar usuários**: Criar, editar e excluir contas de usuários
- **Gerenciar locais acessíveis**: Adicionar, editar e remover locais
- **Visualizar relatórios**: Acessar relatórios detalhados sobre o uso do sistema
- **Moderadores**: Criar regras para o cadastro de locais de acessibilidade
