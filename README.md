# Rest API by Spring boot 2 + JWT

Crie autenticação de APIs REST simples por JWT e gerencie o usuário no banco de dados usando,
- JAVA 11
- MYSQL
- Maven
- Spring boot 2
  - spring-boot-starter-web
  - spring-boot-starter-security
  - spring-boot-starter-data-jpa
- Outras Bibliotecas
  - mysql-connector-java
  - jjwt - json web token
  - jakarta.xml.bind-api - in JAVA 11 `java.xml.bind (JAXB)` pode ser removido.


# APIs
### /register
API para cadastrar usuário. Uma vez registrado, use a API `/authenticate` para criar o token JWT.

Exemplo request:
```
curl -X POST 'http://localhost:8080/register' -H 'Content-Type: application/json' --data-raw '{
    "username": "winitonc",
    "password": "password"
}'
```

Exemplo response:
```
{
    "id": 1,
    "username": "gmontinny"
}
```


### /authenticate
API para autenticar o usuário. Esta API retorna o token JWT, então, use o token na API `/hello`.

Exemplo request:
```
curl -X POST 'http://localhost:8080/authenticate' -H 'Content-Type: application/json' --data-raw '{
    "username": "gmontinny",
    "password": "password"
}'
```
Exemplo response:
```
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3aW5pdG9uYyIsImV4cCI6MTY2ODI4MDIxOCwiaWF0IjoxNjY4MjYyMjE4fQ.2AHZDeUIr17AYNnXF5tld0zsK4-sVIFxhpLUap6UynwdsuFtphaypQt0y60ykQUx4NtYq0wZI-JrTQ2bRPs6wA"
}
```

### /hello
API para testar authenication com bareer JWT token.

Exemplo request with 401 response:
```
curl -X GET 'http://localhost:8080/hello'
```

Exemplo request with 200 response:
```
curl -X GET 'http://localhost:8080/hello' -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3aW5pdG9uYyIsImV4cCI6MTY2ODI4MDIxOCwiaWF0IjoxNjY4MjYyMjE4fQ.2AHZDeUIr17AYNnXF5tld0zsK4-sVIFxhpLUap6UynwdsuFtphaypQt0y60ykQUx4NtYq0wZI-JrTQ2bRPs6wA'
```