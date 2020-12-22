# [B2W Starwars API](https://planetsapib2w.herokuapp.com/swagger-ui.html#/)
---
 ## Índice

- [Introdução](#introduçao)
- [Modelo](#modelo)
- [Funcionalidades e Respostas](#funcionalidades-e-respostas)
  - Busca paginada de planeta e/ou pelo nome
  - Busca do planeta pelo id
  - Criação do planeta
  - Remoção do planeta
- [Executando o projeto localmente](#executando-o-projeto-localmente) 
- [Tecnologias utilizadas](#tecnologias-utilizadas)

## Introdução
A B2W Starwars API tem a intenção de disponibilizar aos seus usuários funcionalidades acerca dos planetas do universo Starwars, tais como a busca, remoção e inserção dos mesmos.

A API é construída em cima do estilo arquitetural REST, disponibilizando acesso aos seus recursos através do protocolo HTTP, utilizando os métodos de tal protocolo para a comunicação e expondo endpoints bem definidos.

Além dessa documentação, esta API está documentada pelo Swagger, sendo acessada a partir do link: https://planetsapib2w.herokuapp.com/swagger-ui.html#/

Os dados dos planetas já efetuados na aplicação foram obtidos através da API https://swapi.dev/

R.I.P swapi.io

## Modelo
Conforme informado na introdução, a aplicação se comunica através de métodos HTTP, além de disponibilizarmos nossos recursos através de JSON. Segue abaixo os objetos utilizados:

### Planet
Recurso da representação do planeta

Atributo | Tipo | Descrição
------------ | ------------- | -------------
Id | String | Identificador do planeta
Name | String | Nome do planeta
Climate | String | Clima do planeta
Terrain | String | Terrenos do planeta
TimesOnMovies | Integer | Nº de aparições em filmes

### ApiError
Recurso da representação de erro na aplicação

Atributo | Tipo | Descrição
------------ | ------------- | -------------
UserMessage | String | Mensagem amigável de erro ao usuário
DeveloperMessage | String | Mensagem contendo a exception para análise do desenvolvedor

### Page<T>
Recurso do Spring Boot para efetuar a paginação do recurso solicitado, segue abaixo os principais atributos

Atributo | Tipo | Descrição
------------ | ------------- | -------------
Content | Array | Recurso(s) solicitados 
Empty | Boolean | Retorna se os recursos solicitados vieram vazios ou não
First | Boolean | Retorna se é a primeira página
Last | Boolean | Retorna se é a última página
Number | Integer | Número da página solicitada
NumberOfElements | Integer | Número de recursos na página
Size | Integer | Número máx. de recursos retornados por página
TotalElements | Integer | Número total de recursos
TotalPages | Integer | Número total de páginas

## Funcionalidades e Respostas
Nessa sessão será demonstrado cada funcionalidade e as respectivas respostas do servidor

### Busca paginada de planeta
Método responsável pela busca dos planetas de forma paginada

- **Endpoint**

    https://planetsapib2w.herokuapp.com/api/planets
- **Método HTTP**

    GET
- **Query Parameters**

    Parâmetros |Tipo| Descrição | Valor Default | Obrigatório 
    ------------ | ------------- | ------------- | ------------- | ------------- 
    name |String|Se informado, irá efetuar a busca pelo nome do planeta|✖| ✖
    size |Integer|Número de planetas desejados por página|5| ✖
    page |Integer|Número da página desejada|0| ✖
    >**Importante:** 
    - O atributo *name* não é case sensitive, e pode ser informado apenas parte do nome do planeta. Ex: Informar apenas a letra *a* irá trazer todos os planetas que contenham tal letra
    - **Atenção:** a paginação começa com o índice 0. Ex.: Caso deseje a página 2, informe o número 1 no parâmetro
    
- **Exemplos de Requisições**

    Busca pela primeira página trazendo cinco planetas por página
    
    `curl -X GET "https://planetsapib2w.herokuapp.com/api/planets?page=0&size=5" -H "accept: application/json"`

    Busca pelo planeta Tatooine
    
    `curl -X GET "https://planetsapib2w.herokuapp.com/api/planets?name=Tatooine" -H "accept: application/json"`
    
    Busca por planetas com a letra a trazendo 20 resultados por página
    
    `curl -X GET "https://planetsapib2w.herokuapp.com/api/planets?name=a&size=20" -H "accept: application/json"`
    
- **Respostas**

    **HTTP 200**: Planeta(s) encontrados
    
```json
{
   "content":[
      {
         "id":"5f31ddac7cc70566188aa13b",
         "name":"Tatooine",
         "climate":"arid",
         "terrain":"desert",
         "timesOnMovies":5
      },
      {
         "id":"5f31ddac7cc70566188aa13c",
         "name":"Alderaan",
         "climate":"temperate",
         "terrain":"grasslands, mountains",
         "timesOnMovies":2
      },
      {
         "id":"5f31ddac7cc70566188aa13d",
         "name":"Yavin IV",
         "climate":"temperate, tropical",
         "terrain":"jungle, rainforests",
         "timesOnMovies":1
      },
      {
         "id":"5f31ddac7cc70566188aa13e",
         "name":"Hoth",
         "climate":"frozen",
         "terrain":"tundra, ice caves, mountain ranges",
         "timesOnMovies":1
      },
      {
         "id":"5f31ddac7cc70566188aa13f",
         "name":"Dagobah",
         "climate":"murky",
         "terrain":"swamp, jungles",
         "timesOnMovies":3
      }
   ],
   "pageable":{
      "sort":{
         "sorted":false,
         "unsorted":true,
         "empty":true
      },
      "pageNumber":0,
      "pageSize":5,
      "offset":0,
      "paged":true,
      "unpaged":false
   },
   "last":false,
   "totalElements":60,
   "totalPages":12,
   "first":true,
   "sort":{
      "sorted":false,
      "unsorted":true,
      "empty":true
   },
   "numberOfElements":5,
   "size":5,
   "number":0,
   "empty":false
}
```

**HTTP 404**: Planeta não encontrado
```json
{
   "userMessage":"The resource was not found by your filters",
   "developerMessage":"PlanetServiceImpl.findByName : Not found on database"
}
```

### Busca do planeta pelo id e/ou pelo nome
Método responsável pelo acesso ao planeta pelo id

- **Endpoint**

    https://planetsapib2w.herokuapp.com/api/planets/{id}
- **Método HTTP**

    GET
- **Path Parameters**

    Parâmetros |Tipo| Descrição | Obrigatório 
    ------------ | ------------- | ------------- | ------------- 
    ID |String|ID do planeta|✓ 
    
- **Exemplos de Requisições**

    Busca pelo id do planeta
    
    `curl -X GET "https://planetsapib2w.herokuapp.com/api/planets/5f31ddac7cc70566188aa13b" -H "accept: application/json"`
    
- **Respostas**

**HTTP 200**: Planeta encontrados
    
```json
{
   "id":"5f31ddac7cc70566188aa13b",
   "name":"Tatooine",
   "climate":"arid",
   "terrain":"desert",
   "timesOnMovies":5
}
```

**HTTP 404**: Planeta não encontrado
    
```json
{
   "userMessage":"The resource was not found by your filters",
   "developerMessage":"PlanetServiceImpl.findById : Not found on database"
}
```

### Criação do planeta
Método responsável pela criação do planeta

- **Endpoint**

    https://planetsapib2w.herokuapp.com/api/planets
- **Método HTTP**

    POST

- **Exemplos de Requisições**

    Criação do planeta
    Atributos do objeto |Tipo| Descrição | Obrigatório 
    ------------ | ------------- | ------------- | ------------- 
    ID |String|Se informado será ignorado|✖
    Name |String|Nome do planeta|✓ 
    Climate |String|Clima do planeta|✓ 
    Terrain |String|Terreno do planeta|✓ 
    TimesOnMovies |Integer|Quantidade de vezes vista nos filmes|✖
    >**Atenção:** o atributo timesOnMovies tem como valor default o 0

```json
{
   "name":"Planet",
   "climate":"Climate",
   "terrain":"Terrain",
   "timesOnMovies":15
}
```
    
- **Respostas**

    **HTTP 201**: Planeta criado
    
    Será informado a localização do recurso no Location, dentro dos headers da requisição

    **HTTP 400**: Planeta não criado devido atributos obrigatórios nulos
    
```json
[
   {
      "userMessage":"Planet: The attribute name is required",
      "developerMessage":"Validation failed for argument [0] in public org.springframework.http.ResponseEntity<io.b2w.starwars.model.Planet> io.b2w.starwars.controller.PlanetController.create(io.b2w.starwars.model.Planet) throws java.net.URISyntaxException: [Field error in object 'planet' on field 'name': rejected value [null]; codes [NotBlank.planet.name,NotBlank.name,NotBlank.java.lang.String,NotBlank]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [planet.name,name]; arguments []; default message [name]]; default message [The attribute name is required]] "
   }
]
```

### Remoção do planeta

Método responsável pela exclusão do planeta

- **Endpoint**

    https://planetsapib2w.herokuapp.com/api/planets/{id}
- **Método HTTP**

    DELETE
- **Path Parameters**

    Parâmetros |Tipo| Descrição | Obrigatório 
    ------------ | ------------- | ------------- | ------------- 
    ID |String|ID do planeta|✓ 

- **Exemplos de Requisições**

    Exclusão do planeta
    `curl -X DELETE "https://planetsapib2w.herokuapp.com/api/planets/{id}" -H "accept: application/json"`
    
- **Respostas**

    **HTTP 204**: Planeta deletado com sucesso

    **HTTP 404**: Planeta não encontrado
    
```json
{
   "userMessage":"The resource was not found by your filters",
   "developerMessage":"PlanetServiceImpl.findById : Not found on database"
}
```

## Executando o projeto localmente
Para executar o projeto localmente parte-se do pressuposto que você tenha o Java 8 instalado em sua máquina.

Com isso, o Spring Boot nos facilita  bastante, basta baixar esse projeto em .ZIP, ou cloná-lo para a pasta de sua preferência.

Assim que cloná-lo, abra o terminal do seu sistema na pasta principal (/b2w-starwars-api) e digite um dos comandos abaixo de sua preferência:

Para executar o projeto no Windows:
`mvnw.cmd spring-boot:run`

Para executar o projeto no Linux:
`./mvnw spring-boot:run`

>Atenção: Certifique-se que para rodar o projeto a porta :8080 esteja livre

Para executar os testes no Windows:
`mvnw.cmd test`

Para executar os testes no Linux:
`./mvnw test`

>Fique tranquilo! Não é necessário configurar nenhum banco de dados, a aplicação já está configurada com o banco de dados na nuvem, basta rodar e usar.

## Tecnologias utilizadas
Foram utilizados os seguintes frameworks e/ou ferramentas:

- Java 8
- Spring Boot 4
- Spring Data MongoDB
- MongoDB Atlas
- JUnit
- Maven
- Heroku
- Lombok
    
