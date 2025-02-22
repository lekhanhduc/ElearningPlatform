docker run -d -p 9411:9411 --name zipkin openzipkin/zipkin
docker run --name redis-container -p 6379:6379 -d redis