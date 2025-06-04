# 网上书城项目环境配置与工具版本说明

## 1. 开发环境配置

### 1.1 必要软件

| 软件 | 版本 | 下载链接 |
|------|------|----------|
| JDK | 11.0.27 | https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html |
| Maven | 3.6.3 | https://maven.apache.org/download.cgi |
| MySQL | 8.0.28 | https://dev.mysql.com/downloads/mysql/ |
| Tomcat | 9.0.65 | https://tomcat.apache.org/download-90.cgi |

### 1.2 IDE推荐

| IDE | 版本 | 下载链接 |
|-----|------|----------|
| Eclipse | 2022-03 或更高 | https://www.eclipse.org/downloads/ |
| IntelliJ IDEA | 2022.1 或更高 | https://www.jetbrains.com/idea/download/ |

## 2. 环境安装步骤

### 2.1 JDK安装

1. 下载JDK 11.0.27安装包
2. 运行安装程序，按照向导完成安装
3. 配置环境变量：
   - JAVA_HOME: JDK安装目录
   - PATH: 添加%JAVA_HOME%\bin
4. 验证安装：打开命令行，输入`java -version`，应显示版本信息

### 2.2 Maven安装

1. 下载Maven 3.6.3二进制包
2. 解压到指定目录
3. 配置环境变量：
   - MAVEN_HOME: Maven解压目录
   - PATH: 添加%MAVEN_HOME%\bin
4. 验证安装：打开命令行，输入`mvn -version`，应显示版本信息

### 2.3 MySQL安装

1. 下载MySQL 8.0.28安装包
2. 运行安装程序，按照向导完成安装
3. 设置root密码并记住
4. 创建数据库和用户：
   ```sql
   CREATE DATABASE bookstore DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE USER 'bookstore'@'localhost' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON bookstore.* TO 'bookstore'@'localhost';
   FLUSH PRIVILEGES;
   ```
5. 导入初始化SQL脚本（见开发文档中的数据库初始化SQL）

### 2.4 Tomcat安装

1. 下载Tomcat 9.0.65二进制包
2. 解压到指定目录
3. 配置环境变量（可选）：
   - CATALINA_HOME: Tomcat解压目录
   - PATH: 添加%CATALINA_HOME%\bin
4. 启动Tomcat：
   - Windows: 运行`%CATALINA_HOME%\bin\startup.bat`
   - Linux/Mac: 运行`$CATALINA_HOME/bin/startup.sh`
5. 验证安装：访问`http://localhost:8080`，应显示Tomcat欢迎页面

## 3. 项目依赖详情

项目使用Maven管理依赖，主要依赖包括：

```xml
<dependencies>
  <!-- Servlet API -->
  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
  </dependency>
  
  <!-- JSP API -->
  <dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>javax.servlet.jsp-api</artifactId>
    <version>2.3.3</version>
    <scope>provided</scope>
  </dependency>
  
  <!-- JSTL -->
  <dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
  </dependency>
  
  <!-- MySQL Connector -->
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.28</version>
  </dependency>
  
  <!-- JSON Processing -->
  <dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20220320</version>
  </dependency>
  
  <!-- Apache Commons -->
  <dependency>
    <groupId>commons-dbcp</groupId>
    <artifactId>commons-dbcp</artifactId>
    <version>1.4</version>
  </dependency>
  
  <!-- File Upload -->
  <dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.4</version>
  </dependency>
  
  <!-- JUnit -->
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

## 4. 项目配置文件

### 4.1 数据库配置文件

文件位置：`src/main/resources/database.properties`

```properties
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/bookstore?useSSL=false&serverTimezone=UTC
username=bookstore
password=password
```

### 4.2 Web配置文件

文件位置：`src/main/webapp/WEB-INF/web.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
                             http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
  
  <display-name>BookstoreWeb</display-name>
  
  <welcome-file-list>
    <welcome-file>index.jsp</welcome-file>
  </welcome-file-list>
  
  <session-config>
    <session-timeout>30</session-timeout>
  </session-config>
  
</web-app>
```

## 5. 项目构建与部署

### 5.1 构建项目

使用Maven构建项目：

```bash
cd BookstoreProject/BookstoreWeb
mvn clean package
```

构建成功后，将在`target`目录下生成`BookstoreWeb.war`文件。

### 5.2 部署到Tomcat

1. 将`BookstoreWeb.war`文件复制到Tomcat的`webapps`目录
2. 启动Tomcat服务器
3. 访问`http://localhost:8080/BookstoreWeb`

### 5.3 开发模式部署

在IDE中配置Tomcat服务器，直接运行项目：

#### Eclipse:
1. 在Servers视图中添加Tomcat服务器
2. 右键项目，选择"Run As" > "Run on Server"
3. 选择配置好的Tomcat服务器，点击"Finish"

#### IntelliJ IDEA:
1. 点击"Run" > "Edit Configurations"
2. 添加Tomcat Server > Local
3. 配置Tomcat服务器路径
4. 在"Deployment"选项卡中添加项目构件
5. 点击"Apply"和"OK"
6. 点击"Run"按钮启动项目

## 6. 常见问题与解决方案

### 6.1 Maven依赖下载问题

**问题**: Maven无法下载依赖或下载速度慢

**解决方案**:
1. 配置国内Maven镜像，编辑`~/.m2/settings.xml`文件：
   ```xml
   <mirrors>
     <mirror>
       <id>aliyun</id>
       <name>Aliyun Maven Repository</name>
       <url>https://maven.aliyun.com/repository/public</url>
       <mirrorOf>central</mirrorOf>
     </mirror>
   </mirrors>
   ```
2. 检查网络连接
3. 尝试使用`mvn clean install -U`强制更新依赖

### 6.2 数据库连接问题

**问题**: 无法连接到MySQL数据库

**解决方案**:
1. 确认MySQL服务已启动
2. 验证数据库用户名和密码
3. 检查数据库连接URL是否正确
4. 确认数据库和表已创建
5. 检查MySQL用户权限

### 6.3 Tomcat部署问题

**问题**: 无法部署到Tomcat或访问应用

**解决方案**:
1. 检查Tomcat日志（`$CATALINA_HOME/logs/catalina.out`）
2. 确认WAR文件已正确部署
3. 验证应用上下文路径
4. 检查端口冲突（默认8080端口）
5. 确认Tomcat版本兼容性

## 7. 开发工具使用技巧

### 7.1 Eclipse技巧

1. 使用Maven插件管理依赖
2. 配置自动构建和热部署
3. 使用Servers视图管理Tomcat服务器
4. 安装JSP/HTML编辑器插件提高开发效率

### 7.2 IntelliJ IDEA技巧

1. 使用Maven视图管理依赖
2. 配置JRebel或Spring Boot DevTools实现热部署
3. 使用Database工具连接和管理MySQL数据库
4. 使用内置HTTP客户端测试API

## 8. 版本控制建议

1. 使用Git管理源代码
2. 创建合理的分支策略：
   - `master`/`main`: 稳定版本
   - `develop`: 开发版本
   - `feature/*`: 新功能开发
   - `bugfix/*`: 错误修复
3. 编写有意义的提交信息
4. 使用`.gitignore`排除不必要的文件：
   ```
   target/
   .idea/
   .settings/
   .classpath
   .project
   *.iml
   ```

## 9. 总结

本文档详细说明了网上书城项目的环境配置和工具版本要求，包括JDK、Maven、MySQL和Tomcat的安装步骤，项目依赖详情，配置文件说明，以及项目构建与部署方法。同时提供了常见问题的解决方案和开发工具使用技巧，帮助开发人员快速搭建开发环境并进行项目开发。
