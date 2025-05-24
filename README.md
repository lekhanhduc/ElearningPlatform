# E-Learning System

Chào mừng bạn đến với hệ thống **E-Learning**! Đây là dự án xây dựng nền tảng học trực tuyến với nhiều tính năng như quản lý khóa học, tài liệu học tập (sách điện tử), theo dõi tiến độ học, và phân tích hành vi người dùng.

## Mục Lục
1. [Giới thiệu](#giới-thiệu)
2. [Cài đặt](#cài-đặt)
3. [Cấu trúc dự án](#cấu-trúc-dự-án)
4. [Các tính năng](#các-tính-năng)
5. [Sơ đồ hệ thống](#sơ-đồ-hệ-thống)
6. [Hướng dẫn sử dụng](#hướng-dẫn-sử-dụng)
7. [Liên hệ](#liên-hệ)

## Giới thiệu

Hệ thống **E-Learning** là một nền tảng học trực tuyến cung cấp các khóa học, tài liệu học tập (dưới dạng sách điện tử), và công cụ hỗ trợ học tập. Dự án này sử dụng các công nghệ:
- **BACKEND**: Spring Boot, Kafka, RabbitMQ, Redis, Elasticsearch, Kibana
- **FRONTEND**: Angular
- **DATABASE**: MySQL, Postgres, MongoDB
- **CACHE**: Redis
- **LOGGING**: ELK
- **MONITORING**: Prometheus, Grafana
- **DISTRIBUTED TRACING**: Zipkin

## Cài đặt

### Yêu cầu
- Java 21
- Maven 3.9.8
- Docker
- Spring Boot 3.x

### Các bước cài đặt
1. Clone repository
    - `git clone https://github.com/lekhanhduc/ElearningPlatform`
    - branch: master

2. Chạy các dịch vụ
    - Sử dụng Docker Compose để khởi động các dịch vụ Kafka, RabbitMQ, Zipkin, MongoDB, MySQL, Elasticsearch, Kibana:
      ```bash
      docker-compose up -d

3. Cấu trúc dự án
spring-microservices-21.01.2025/
├── ApiGateway/
├── BookService/         
├── CartService/          
├── ConfigServer/
├── CourseService/        
├── EnrollmentService/    
├── EurekaServer/
├── EventBusService/
├── FileService/
├── IdentityService/
├── NotificationService/
├── OrderService/         
├── PaymentService/
├── PostService/
├── ProfileService/
├── ProgressService/      
├── ReviewService/
├── SearchService/
├── docker-compose.yml
├── initdb.sql
└── README.md

