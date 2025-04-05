# WatchAlert

WatchAlert是一个基于Spring Boot的股票价格监控系统。

## 功能特性

- 股票信息的增删改查
- 股票价格更新
- 异常处理
- RESTful API

## 技术栈

- Spring Boot 2.7.0
- Spring Data JPA
- MySQL
- Lombok
- Maven

## 快速开始

### 环境要求

- JDK 11
- Maven 3.6+
- MySQL 8.0+

### 配置

1. 创建MySQL数据库:
```sql
CREATE DATABASE watchalert;
```

2. 修改`application.yml`中的数据库配置:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/watchalert?useSSL=false&serverTimezone=UTC
    username: your_username
    password: your_password
```

### 构建和运行

1. 克隆项目:
```bash
git clone https://github.com/yourusername/watchalert.git
cd watchalert
```

2. 构建项目:
```bash
mvn clean package
```

3. 运行项目:
```bash
java -jar target/watchalert-1.0.0.jar
```

## API文档

### 股票管理

#### 创建股票
- POST /api/v1/stocks
- 请求体: Stock对象

#### 更新股票
- PUT /api/v1/stocks/{id}
- 请求体: Stock对象

#### 删除股票
- DELETE /api/v1/stocks/{id}

#### 获取股票
- GET /api/v1/stocks/{id}

#### 获取所有股票
- GET /api/v1/stocks

#### 根据股票代码获取股票
- GET /api/v1/stocks/code/{code}

#### 根据股票名称获取股票
- GET /api/v1/stocks/name/{name}

#### 更新股票价格
- PUT /api/v1/stocks/{id}/price?price={price}

## 测试

运行单元测试:
```bash
mvn test
```

## 许可证

MIT License 