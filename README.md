Sistema simples para cadastramento de apostas. 

# apostas-backend

Feito com:
+ Java Spring Rest retornando JSon.
+ Spring Security com tag/directiva para Autorização e OAuth.
+ JPA e Spring Data.
+ Migration com FlyWay.

O retorno JSON terá sempre o mesmo padrão:

{
	"dado":null,
	"lista":null,
	"mensagens":[
		{"chave":"mensagem","valor":"texto da mensagem"}
	]
}


# apostas-frontend

Feito com:
+ AngularJS.
+ Bootstrap.

Configurar o ubuntu:

+ sudo apt-get install npm
+ sudo apt-get install nodejs-legacy

Instalar dependencias transientes:

+ sudo npm install -g node
+ sudo npm install -g grunt-cli

Na raiz do projeto:

+ Instalar dependencias do projeto: npm install
+ Rodar o servidor node: grunt
+ Acessar: http://localhost:9000/

Proxy com NPM:
+ npm config set proxy http://proxy.company.com:8080
+ npm config set https-proxy http://proxy.company.com:8080
