docker network create store-network
docker run -d --name mysql -p 3306:3306 --network=store-network -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_ROOT_HOST="%" -e MYSQL_DATABASE=store mysql:8.2.0

mysql -u root -psecret -D store -h 127.0.0.1
mysql> CREATE USER 'store'@'%' IDENTIFIED BY '<store-password>';
mysql> GRANT ALL PRIVILEGES ON store.* TO 'store'@'%'
mysql> quit

$ mysql -u store -pstore -D store -h 127.0.0.1 -P 13306
mysql> insert into roles(type) values('ROLE_USER');
mysql> insert into roles(type) values('ROLE_ADMIN');
mysql> quit

store-api.env (file at /root)
-------------
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/store?useSSL=false
SPRING_DATASOURCE_USERNAME=store
SPRING_DATASOURCE_PASSWORD=<store-password>

docker stop store-api
docker system prune -a -f
docker run -d --name store-api -p 8080:8080 --network=store-network --env-file=/root/store-api.env slivezey/store-api:latest

docker stop store-api
docker system prune -a -f
docker run -d --name store-website -p 4200:80 slivezey/store-website:latest
