# 网上书城项目开发文档

## 1. 系统架构设计

### 1.1 整体架构

本网上书城项目采用JSP+Servlet+MVC架构，使用Maven进行项目构建和依赖管理，MySQL作为数据库系统。整体架构分为以下几层：

1. **表示层（View）**：使用JSP页面实现用户界面，负责数据的展示和用户交互。
2. **控制层（Controller）**：使用Servlet处理用户请求，调用业务逻辑层的服务，并将结果返回给表示层。
3. **业务逻辑层（Service）**：实现核心业务逻辑，处理控制层传来的请求，并调用数据访问层获取数据。
4. **数据访问层（DAO）**：负责与数据库交互，执行数据的增删改查操作。
5. **实体层（Model）**：定义系统中的数据模型，对应数据库中的表结构。

![MVC架构图](https://www.plantuml.com/plantuml/png/TP31QiCm38RlUGgVk_G55wWDYHAYKnKJsQfRXPMOtPMTTYYTExzxUoFzzy_dvhsJcPcCcRmQFei9FQHgWYr1uGKGnEEDZ4lGAk_GWnY2Yc2MKbLgZQfH2_mHT4-ZGnQOLOVbDm_Wl6_WEMvs-Ry9VQpQTlRVYVm7)

### 1.2 技术架构

- **前端技术**：HTML、CSS、JavaScript、JSP、JSTL
- **后端技术**：Java、Servlet、JDBC
- **数据库**：MySQL
- **项目管理**：Maven
- **服务器**：Tomcat

### 1.3 项目结构

```
BookstoreWeb/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bookstore/
│   │   │           ├── controller/  # 控制器层
│   │   │           ├── dao/         # 数据访问层
│   │   │           ├── model/       # 实体模型层
│   │   │           ├── service/     # 业务逻辑层
│   │   │           └── util/        # 工具类
│   │   ├── resources/               # 配置文件
│   │   └── webapp/                  # Web资源
│   │       ├── WEB-INF/
│   │       │   ├── views/           # JSP视图
│   │       │   └── web.xml          # Web配置文件
│   │       ├── css/                 # 样式文件
│   │       ├── js/                  # JavaScript文件
│   │       └── images/              # 图片资源
│   └── test/                        # 测试代码
└── pom.xml                          # Maven配置文件
```

### 1.4 模块划分

1. **用户模块**：用户注册、登录、个人信息管理
2. **图书模块**：图书浏览、搜索、详情展示
3. **购物车模块**：添加商品、修改数量、删除商品、清空购物车
4. **订单模块**：创建订单、订单管理、订单状态更新
5. **评论模块**：添加评论、查看评论、评分管理
6. **后台管理模块**：用户管理、图书管理、订单管理

### 1.5 数据流程

1. 用户通过浏览器访问JSP页面
2. JSP页面发送请求到对应的Servlet控制器
3. Servlet控制器调用Service层处理业务逻辑
4. Service层调用DAO层访问数据库
5. DAO层执行SQL语句并返回结果
6. 结果层层返回，最终由JSP页面展示给用户

## 2. 数据库设计

### 2.1 ER图

（此处应有ER图，实际项目中可使用数据库设计工具生成）

### 2.2 数据库表结构

#### 2.2.1 用户表（users）

| 字段名 | 数据类型 | 约束 | 说明 |
|-------|---------|------|------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT | 用户ID |
| username | VARCHAR(50) | NOT NULL, UNIQUE | 用户名 |
| password | VARCHAR(100) | NOT NULL | 密码 |
| email | VARCHAR(100) | NOT NULL | 邮箱 |
| phone | VARCHAR(20) | | 电话 |
| address | VARCHAR(200) | | 地址 |
| register_time | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 注册时间 |
| role | INT | NOT NULL, DEFAULT 0 | 角色（0:普通用户, 1:管理员） |

#### 2.2.2 图书表（books）

| 字段名 | 数据类型 | 约束 | 说明 |
|-------|---------|------|------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT | 图书ID |
| title | VARCHAR(100) | NOT NULL | 书名 |
| author | VARCHAR(100) | NOT NULL | 作者 |
| publisher | VARCHAR(100) | | 出版社 |
| isbn | VARCHAR(20) | UNIQUE | ISBN编号 |
| price | DECIMAL(10,2) | NOT NULL | 价格 |
| stock | INT | NOT NULL, DEFAULT 0 | 库存 |
| category | VARCHAR(50) | | 分类 |
| description | TEXT | | 描述 |
| cover_image | VARCHAR(200) | | 封面图片URL |
| publish_date | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 出版日期 |

#### 2.2.3 订单表（orders）

| 字段名 | 数据类型 | 约束 | 说明 |
|-------|---------|------|------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT | 订单ID |
| user_id | INT | NOT NULL, FOREIGN KEY | 用户ID |
| total_amount | DECIMAL(10,2) | NOT NULL | 总金额 |
| shipping_address | VARCHAR(200) | NOT NULL | 收货地址 |
| status | VARCHAR(20) | NOT NULL | 订单状态 |
| order_time | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 下单时间 |
| payment_time | TIMESTAMP | | 付款时间 |
| shipping_time | TIMESTAMP | | 发货时间 |
| completion_time | TIMESTAMP | | 完成时间 |

#### 2.2.4 订单项表（order_items）

| 字段名 | 数据类型 | 约束 | 说明 |
|-------|---------|------|------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT | 订单项ID |
| order_id | INT | NOT NULL, FOREIGN KEY | 订单ID |
| book_id | INT | NOT NULL, FOREIGN KEY | 图书ID |
| quantity | INT | NOT NULL | 数量 |
| price | DECIMAL(10,2) | NOT NULL | 单价 |

#### 2.2.5 购物车表（cart_items）

| 字段名 | 数据类型 | 约束 | 说明 |
|-------|---------|------|------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT | 购物车项ID |
| user_id | INT | NOT NULL, FOREIGN KEY | 用户ID |
| book_id | INT | NOT NULL, FOREIGN KEY | 图书ID |
| quantity | INT | NOT NULL | 数量 |
| add_time | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 添加时间 |

#### 2.2.6 评论表（reviews）

| 字段名 | 数据类型 | 约束 | 说明 |
|-------|---------|------|------|
| id | INT | PRIMARY KEY, AUTO_INCREMENT | 评论ID |
| user_id | INT | NOT NULL, FOREIGN KEY | 用户ID |
| book_id | INT | NOT NULL, FOREIGN KEY | 图书ID |
| rating | INT | NOT NULL | 评分（1-5） |
| comment | TEXT | | 评论内容 |
| review_time | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 评论时间 |

### 2.3 数据库初始化SQL

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS bookstore DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bookstore;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(200),
    register_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role INT NOT NULL DEFAULT 0
);

-- 图书表
CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    publisher VARCHAR(100),
    isbn VARCHAR(20) UNIQUE,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    category VARCHAR(50),
    description TEXT,
    cover_image VARCHAR(200),
    publish_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    shipping_address VARCHAR(200) NOT NULL,
    status VARCHAR(20) NOT NULL,
    order_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_time TIMESTAMP NULL,
    shipping_time TIMESTAMP NULL,
    completion_time TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 订单项表
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- 购物车表
CREATE TABLE IF NOT EXISTS cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL,
    add_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- 评论表
CREATE TABLE IF NOT EXISTS reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    review_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- 插入管理员账户
INSERT INTO users (username, password, email, role) VALUES ('admin', 'admin123', 'admin@bookstore.com', 1);
```

## 3. API文档

### 3.1 用户模块API

#### 3.1.1 用户注册

- **URL**: `/user/register`
- **方法**: POST
- **参数**:
  - username: 用户名
  - password: 密码
  - email: 邮箱
- **返回**: 注册成功跳转到首页，失败返回错误信息

#### 3.1.2 用户登录

- **URL**: `/user/login`
- **方法**: POST
- **参数**:
  - username: 用户名
  - password: 密码
- **返回**: 登录成功跳转到首页，失败返回错误信息

#### 3.1.3 用户信息更新

- **URL**: `/user/update`
- **方法**: POST
- **参数**:
  - password: 密码
  - email: 邮箱
  - phone: 电话
  - address: 地址
- **返回**: 更新成功返回成功信息，失败返回错误信息

#### 3.1.4 用户登出

- **URL**: `/user/logout`
- **方法**: GET
- **参数**: 无
- **返回**: 登出后跳转到首页

### 3.2 图书模块API

#### 3.2.1 图书列表

- **URL**: `/book`
- **方法**: GET
- **参数**: 无
- **返回**: 所有图书列表

#### 3.2.2 图书搜索

- **URL**: `/book/search`
- **方法**: GET
- **参数**:
  - keyword: 搜索关键词
- **返回**: 符合条件的图书列表

#### 3.2.3 图书分类

- **URL**: `/book/category`
- **方法**: GET
- **参数**:
  - category: 图书分类
- **返回**: 该分类下的图书列表

#### 3.2.4 图书详情

- **URL**: `/book/{id}`
- **方法**: GET
- **参数**:
  - id: 图书ID
- **返回**: 图书详细信息和评论

#### 3.2.5 添加图书（管理员）

- **URL**: `/book/add`
- **方法**: POST
- **参数**:
  - title: 书名
  - author: 作者
  - publisher: 出版社
  - isbn: ISBN
  - price: 价格
  - stock: 库存
  - category: 分类
  - description: 描述
  - coverImage: 封面图片URL
- **返回**: 添加成功跳转到图书详情页，失败返回错误信息

### 3.3 购物车模块API

#### 3.3.1 查看购物车

- **URL**: `/cart`
- **方法**: GET
- **参数**: 无
- **返回**: 购物车内容

#### 3.3.2 添加到购物车

- **URL**: `/cart/add`
- **方法**: POST
- **参数**:
  - bookId: 图书ID
  - quantity: 数量
- **返回**: 添加成功跳转到购物车页面，失败返回错误信息

#### 3.3.3 更新购物车

- **URL**: `/cart/update`
- **方法**: POST
- **参数**:
  - cartItemId: 购物车项ID
  - quantity: 新数量
- **返回**: 更新成功跳转到购物车页面，失败返回错误信息

#### 3.3.4 删除购物车项

- **URL**: `/cart/remove`
- **方法**: POST
- **参数**:
  - cartItemId: 购物车项ID
- **返回**: 删除成功跳转到购物车页面，失败返回错误信息

#### 3.3.5 清空购物车

- **URL**: `/cart/clear`
- **方法**: GET
- **参数**: 无
- **返回**: 清空成功跳转到购物车页面，失败返回错误信息

### 3.4 订单模块API

#### 3.4.1 查看订单列表

- **URL**: `/order`
- **方法**: GET
- **参数**: 无
- **返回**: 用户的所有订单

#### 3.4.2 查看订单详情

- **URL**: `/order/{id}`
- **方法**: GET
- **参数**:
  - id: 订单ID
- **返回**: 订单详细信息和订单项

#### 3.4.3 创建订单

- **URL**: `/order/create`
- **方法**: POST
- **参数**:
  - shippingAddress: 收货地址
- **返回**: 创建成功跳转到订单详情页，失败返回错误信息

#### 3.4.4 取消订单

- **URL**: `/order/cancel`
- **方法**: POST
- **参数**:
  - orderId: 订单ID
- **返回**: 取消成功跳转到订单详情页，失败返回错误信息

#### 3.4.5 更新订单状态（管理员）

- **URL**: `/order/update-status`
- **方法**: POST
- **参数**:
  - orderId: 订单ID
  - status: 新状态
- **返回**: 更新成功跳转到订单详情页，失败返回错误信息

### 3.5 评论模块API

#### 3.5.1 添加评论

- **URL**: `/review/add`
- **方法**: POST
- **参数**:
  - bookId: 图书ID
  - rating: 评分
  - comment: 评论内容
- **返回**: 添加成功跳转到图书详情页，失败返回错误信息

#### 3.5.2 删除评论

- **URL**: `/review/delete`
- **方法**: POST
- **参数**:
  - reviewId: 评论ID
  - bookId: 图书ID
- **返回**: 删除成功跳转到图书详情页，失败返回错误信息

### 3.6 后台管理模块API

#### 3.6.1 管理员首页

- **URL**: `/admin`
- **方法**: GET
- **参数**: 无
- **返回**: 管理员首页

#### 3.6.2 订单管理

- **URL**: `/admin/orders`
- **方法**: GET
- **参数**: 无
- **返回**: 所有订单列表

#### 3.6.3 图书管理

- **URL**: `/admin/books`
- **方法**: GET
- **参数**: 无
- **返回**: 图书管理页面

#### 3.6.4 用户管理

- **URL**: `/admin/users`
- **方法**: GET
- **参数**: 无
- **返回**: 用户管理页面

## 4. 部署指南

### 4.1 环境要求

- JDK 11 或更高版本
- Maven 3.6.3 或更高版本
- MySQL 8.0 或更高版本
- Tomcat 9.0 或更高版本

### 4.2 部署步骤

1. **准备数据库**
   - 创建MySQL数据库并执行初始化SQL脚本
   - 配置数据库连接参数

2. **配置项目**
   - 修改`src/main/resources/database.properties`文件，设置数据库连接信息
   - 根据需要修改其他配置参数

3. **构建项目**
   ```bash
   cd BookstoreProject/BookstoreWeb
   mvn clean package
   ```

4. **部署到Tomcat**
   - 将生成的WAR文件（`target/BookstoreWeb.war`）复制到Tomcat的webapps目录
   - 启动Tomcat服务器
   - 访问`http://localhost:8080/BookstoreWeb`

### 4.3 常见问题解决

1. **数据库连接失败**
   - 检查数据库服务是否启动
   - 验证数据库连接参数是否正确
   - 确认数据库用户权限是否足够

2. **Tomcat部署问题**
   - 检查Tomcat日志查找错误信息
   - 确保WAR文件正确部署
   - 验证Tomcat版本兼容性

3. **项目构建失败**
   - 检查Maven配置和依赖
   - 确保JDK版本正确
   - 查看构建日志定位具体错误

## 5. 环境配置说明

### 5.1 开发环境

- **操作系统**: Windows 10/11 或 macOS 或 Linux
- **IDE**: Eclipse 2022-03 或 IntelliJ IDEA 2022.1 或更高版本
- **JDK**: 11.0.27
- **Maven**: 3.6.3
- **MySQL**: 8.0.28
- **Tomcat**: 9.0.65

### 5.2 项目依赖

项目使用Maven管理依赖，主要依赖包括：

- **javax.servlet-api**: 4.0.1
- **javax.servlet.jsp-api**: 2.3.3
- **jstl**: 1.2
- **mysql-connector-java**: 8.0.28
- **json**: 20220320
- **commons-dbcp**: 1.4
- **commons-fileupload**: 1.4
- **junit**: 4.13.2

详细依赖配置请参考`pom.xml`文件。

### 5.3 开发工具配置

#### 5.3.1 Eclipse配置

1. 安装Eclipse IDE for Enterprise Java and Web Developers
2. 安装Maven插件（如果未内置）
3. 配置JDK 11
4. 导入项目为Maven项目
5. 配置Tomcat服务器

#### 5.3.2 IntelliJ IDEA配置

1. 安装IntelliJ IDEA Ultimate版本
2. 配置JDK 11
3. 导入项目为Maven项目
4. 配置Tomcat服务器

### 5.4 数据库配置

1. 安装MySQL 8.0
2. 创建数据库和用户
3. 执行初始化SQL脚本
4. 配置项目的数据库连接参数

## 6. 总结

本网上书城项目采用JSP+Servlet+MVC架构，使用Maven进行构建，实现了用户注册登录、图书浏览搜索、购物车管理、订单处理、评论系统和后台管理等功能。项目结构清晰，代码规范，具有良好的可扩展性和可维护性。

通过本文档，开发人员可以了解系统架构、数据库设计、API接口和部署方法，便于后续的开发、维护和扩展。
