docker run -d -p 9411:9411 --name zipkin openzipkin/zipkin
docker run --name redis-container -p 6379:6379 -d redis
docker pull bitnami/mongodb:8.0.5
docker run -d --name mongo-db -p 27017:27017 -e MONGODB_ROOT_USER=root -e MONGODB_ROOT_PASSWORD=123456 bitnami/mongodb:8.0.5
