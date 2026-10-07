# Sistema de Gestão de Infrações de Trânsito

Aplicação full-stack desenvolvida para gerenciamento de condutores, veículos e infrações de trânsito.

O sistema permite cadastrar condutores e veículos, configurar tipos de infração, registrar ocorrências e atualizar automaticamente a pontuação da CNH do condutor.

Este projeto foi desenvolvido com foco em estudo e portfólio, aplicando conceitos de desenvolvimento web full-stack, APIs REST, regras de negócio, persistência de dados, validações e testes unitários.

---

## Visão geral

O sistema possui as seguintes áreas:

- Dashboard
- Condutores
- Veículos
- Tipos de Infração
- Infrações

A aplicação foi dividida em frontend e backend.

O frontend foi desenvolvido com React e TypeScript e consome uma API REST desenvolvida em Java com Spring Boot.

O banco de dados utilizado é o SQL Server.

---

## Funcionalidades

### Condutores

- Cadastro de condutores
- Edição de condutores
- Consulta de condutores cadastrados
- Controle da pontuação da CNH
- Validação de CPF duplicado
- Validação de número de CNH duplicado

### Veículos

- Cadastro de veículos
- Edição de veículos
- Exclusão de veículos
- Associação de veículo a um condutor
- Bloqueio da exclusão quando existem infrações vinculadas

### Tipos de Infração

- Cadastro de tipos de infração
- Definição de:
  - código
  - descrição
  - gravidade
  - quantidade de pontos
  - valor da multa
- Edição de tipos de infração
- Exclusão de tipos de infração
- Proteção contra exclusão quando existem infrações vinculadas
- Proteção contra alteração da pontuação de tipos já utilizados

### Infrações

- Registro de infrações
- Associação entre:
  - condutor
  - veículo
  - tipo de infração
  - data e hora
- Edição de infrações
- Exclusão de infrações
- Consulta por condutor
- Consulta por veículo
- Atualização automática dos pontos da CNH
- Validação de vínculo entre veículo e condutor

---

## Regras de negócio

Ao registrar uma infração, os pontos definidos no tipo de infração são adicionados automaticamente à CNH do condutor.

Ao editar uma infração, a pontuação anterior é removida e a nova pontuação é aplicada.

Ao excluir uma infração, os respectivos pontos são removidos da CNH.

O backend também valida se o veículo informado realmente pertence ao condutor selecionado.

Além disso:

- veículos com infrações vinculadas não podem ser excluídos;
- tipos de infração utilizados em infrações não podem ser excluídos;
- a pontuação de um tipo de infração já utilizado não pode ser alterada;
- CPF e número de CNH não podem ser duplicados.

Exemplo:

```text
Condutor: João
Pontuação inicial: 0

Infração grave: +5 pontos

Pontuação da CNH: 5

Infração alterada para gravíssima: 7 pontos

Pontuação da CNH: 7

Infração excluída

Pontuação da CNH: 0

```
### Tecnologias utilizadas
## Backend
- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Jakarta Validation
- Maven
- SQL Server
- Springdoc OpenAPI / Swagger
## Frontend
- React
- TypeScript
- React Router
- Axios
- Vite
- CSS
## Testes
- JUnit 5
- Mockito

### Estrutura do projeto
Estrutura simplificada do backend:
src/main/java/com/transito/sistema

├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
└── service

Estrutura simplificada do frontend:

frontend/src

├── components
├── pages
├── services
├── App.tsx
└── main.tsx

### Endpoints principais

## Condutores
GET    /condutores
GET    /condutores/{id}
POST   /condutores
PUT    /condutores/{id}

## Veículos
GET    /veiculos
POST   /veiculos
PUT    /veiculos/{id}
DELETE /veiculos/{id}

## Tipos de Infração
GET    /tipos-infracao
POST   /tipos-infracao
PUT    /tipos-infracao/{id}
DELETE /tipos-infracao/{id}

## Infrações
GET    /infracoes
GET    /infracoes/{id}
GET    /infracoes/condutor/{condutorId}
GET    /infracoes/veiculo/{veiculoId}
POST   /infracoes
PUT    /infracoes/{id}
DELETE /infracoes/{id}

## Documentação da API
Acessada pelo Swagger em: http://localhost:8080/swagger-ui/index.html

### Como executar o projeto
## Pré-requisitos
Antes de iniciar, é necessário possuir:
- Java 21
- Node.js
- npm
- SQL Server

### Configuração do banco de dados
Crie um banco SQL Server chamado: SistemaInfracoes
CREATE DATABASE SistemaInfracoes;

O projeto possui o arquivo: src/main/resources/application-example.properties
Crie uma cópia dele com o nome: application.properties

## Exemplo: 
spring.application.name=sistema-infracoes

spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=SistemaInfracoes;encrypt=true;trustServerCertificate=true
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

### Executando o backend
Na raiz do projeto:
## Windows / PowerShell
.\mvnw.cmd spring-boot:run
## O backend ficará disponível em: http://localhost:8080

## Executando o frontend
Abra outro terminal e entre na pasta: cd frontend
## Instale as dependências: npm install e execute: npm run dev
O Vite normalmente disponibilizará a aplicação em: http://localhost:5173

### Fluxo básico para testar o sistema
1. Cadastrar um condutor

2. Cadastrar um veículo e associá-lo ao condutor

3. Cadastrar um tipo de infração

4. Registrar uma infração

5. Voltar para a tela de condutores e verificar
   se a pontuação da CNH foi atualizada

6. Editar a infração e verificar
   se os pontos foram recalculados

7. Excluir a infração e verificar
   se os pontos foram removidos

### Testes
Os testes do backend utilizam JUnit 5 e Mockito.
Para executar: .\mvnw.cmd test

Os testes cobrem regras como:
- cadastro e consulta de condutores;
- bloqueio de CPF duplicado;
- bloqueio de CNH duplicada;
- adição de pontos ao registrar infração;
- recálculo da pontuação ao editar infração;
- remoção de pontos ao excluir infração;
- validação de veículo pertencente ao condutor;
- bloqueio de exclusão de veículos vinculados;
- bloqueio de exclusão de tipos de infração vinculados.

### Para verificar o build do frontend: 
cd frontend
npm run build
npm run dev

### Tratamento de erros
A API possui tratamento global de exceções.
Alguns dos status utilizados são:
400 Bad Request

## Para dados inválidos ou regras como veículo não pertencente ao condutor.
404 Not Found

## Para recursos inexistentes.
409 Conflict

## Para conflitos de regra de negócio, como tentativa de excluir um veículo que possui infrações vinculadas.
As respostas de erro seguem um formato semelhante a:
{
  "message": "Mensagem explicando o erro."
}

### Objetivo do projeto
Este projeto foi desenvolvido com o objetivo de praticar e demonstrar conhecimentos em:
- Java
- Spring Boot
- React
- TypeScript
- APIs REST
- Programação orientada a objetos
- Arquitetura em camadas
- DTOs
- Spring Data JPA
- Relacionamentos entre entidades
- Regras de negócio
- Validação de dados
- Tratamento global de exceções
- Integração frontend e backend
- SQL Server
- Testes unitários com JUnit e Mockito
- Git e GitHub

### Possíveis melhorias futuras
Algumas funcionalidades que poderiam ser adicionadas futuramente:
- autenticação de usuários;
- busca e filtros;
- paginação;
- histórico detalhado por condutor;
- dashboard com mais indicadores;
- deploy do frontend e backend;
- configuração do banco em ambiente cloud;
- interface responsiva mais completa.
Essas funcionalidades não fazem parte do escopo atual do projeto

### Autor
João Vitor Rath
Estudante de Ciência da Computação.
Projeto desenvolvido para fins de estudo e portfólio.
