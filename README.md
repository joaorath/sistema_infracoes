# Sistema de Gestão de Infrações de Trânsito

Aplicação full-stack desenvolvida para gerenciamento de condutores, veículos e infrações de trânsito.

O sistema permite cadastrar condutores e veículos, configurar tipos de infração, registrar ocorrências e atualizar automaticamente a pontuação da CNH do condutor.

Este projeto foi desenvolvido com foco em estudo e portfólio, aplicando conceitos de desenvolvimento web full-stack, APIs REST, regras de negócio, persistência de dados e testes unitários.

---

## Visão geral

O sistema possui as seguintes áreas:

- Dashboard
- Condutores
- Veículos
- Tipos de Infração
- Infrações

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

---

## Regras de negócio

Ao registrar uma infração, os pontos definidos no tipo de infração são adicionados automaticamente à CNH do condutor.

Ao editar uma infração, a pontuação anterior é removida e a nova pontuação é aplicada.

Ao excluir uma infração, os respectivos pontos são removidos da CNH.

O backend também valida se o veículo informado realmente pertence ao condutor selecionado.

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