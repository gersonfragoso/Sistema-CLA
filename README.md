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

## Esta no documento excel no repo.

# 📋 Casos de Uso do Usuario

![Image](https://github.com/user-attachments/assets/b7814809-31ad-45a1-a681-315396f2e601)

- Observação ainda faltar fazer o caso de uso do Usuario do tipo admin (`MODERADOR`) e do vistante (`VISITANTE`)


# 📋 Diagrama de classes

![Image](https://github.com/user-attachments/assets/86445c92-f54e-4c28-bd77-b61fd095dec5)

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
