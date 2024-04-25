# E-Commerce

Projeto desenvolvido para a disciplina de MAC0350 - Introdução ao Desenvolvimento de Sistemas de Software

Autores: Marcelo Nascimento e Victor Nascimento Ribeiro

# Descrição
Sistema web simples para gerenciamento e venda de produtos.

# Tecnologias
- Backend: Springboot, Docker e Mysql
- Frontend: Vue.js
  
# Funcionalidades
- Login com token e níveis de usuário
- Gerenciamento de usuários, produtos, pedidos e itens em estoque

## Requisitos
- OpenJDK 21
- Docker
- Docker Compose : 2.24.1
  - Esta versão é necessária, pois, depois da recente atualização do Docker Compose, a bibioteca Spring Boot Docker parou de funcionar.

# Execução
Ao abrir o projeto com o Intellij, no menu lateral do Gradle, ir em ecommerce->Tasks->application e clicar em bootRun. A aplicação Kotlin será executada em conjunto com uma imagem Docker do Mysql. 
- Endereço base do site: localhost:8080
- Endereço base da API: localhost:8080/api/


## Diagrama de classes
![alt text](https://lucid.app/publicSegments/view/05d03078-9dc9-49f0-bfbc-af45759a2506/image.png)
