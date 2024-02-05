# Testes de API

![Modelo de domínio Desafio01](https://i.postimg.cc/Y9Bm3sqx/save.png)

# Sobre o projeto
Este é um projeto de filmes e avaliações de filmes. A visualização dos dados dos filmes é pública (não necessita login), porém as alterações de filmes (inserir, atualizar, deletar) são permitidas apenas para usuários ADMIN.
As avaliações de filmes podem ser registradas por qualquer usuário logado CLIENT ou ADMIN.
A entidade Score armazena uma nota de 0 a 5 (score) que cada usuário deu a cada filme. Sempre que um usuário registra uma nota, o sistema calcula a média das notas de todos usuários, e armazena essa nota média (score) na entidade Movie,
juntamente com a contagem de votos (count).  Veja vídeo explicativo.


# Tecnologias utilizadas
## Back end

- Java
- Spring Boot
- Mockito
- Maven
- JUnit
- MockMvc
- Rest Assured

# Autor

Lucas Danilo de Castro

https://www.linkedin.com/in/lucasdanilox/
