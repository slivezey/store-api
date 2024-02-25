docker build -t slivezey/store-api:latest
docker image push slivezey/store-api:latest

docker network create store-network
docker run -d --name mysql -p 13306:3306 --network=store-network -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_ROOT_HOST="%" -e MYSQL_DATABASE=store mysql:8.2.0

mysql -u root -psecret -D store -h 127.0.0.1 -P 13306
GRANT ALL PRIVILEGES ON store.* TO 'store'@'%'
CREATE USER 'store'@'%' IDENTIFIED BY 'password';

docker run -d -p 8082:8080 --name store-api --network=store-network --env-file=store-api.env slivezey/store-api

mysql -u store -pstore -D store -h 127.0.0.1 -P 13306
mysql> insert into roles(type) values('ROLE_USER');
mysql> insert into roles(type) values('ROLE_ADMIN');

store-api.env
-------------
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/store?useSSL=false
SPRING_DATASOURCE_USERNAME=store
SPRING_DATASOURCE_PASSWORD=store