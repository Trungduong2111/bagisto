spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root  # hoặc ecommerce_user
    password: your_mysql_password  # thay bằng password thật
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  jpa:
    hibernate:
      ddl-auto: update  # tạo tables tự động
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true
    defer-datasource-initialization: true
    
  sql:
    init:
      mode: always
      data-locations: classpath:data.sql  # MySQL-compatible data

logging:
  level:
    com.ecommerce.backend: DEBUG
    org.hibernate.SQL: DEBUG