# 📚 在线书店系统

![版本](https://img.shields.io/badge/版本-1.0.0-blue.svg)
![许可证](https://img.shields.io/badge/许可证-MIT-green.svg)
![Java](https://img.shields.io/badge/Java-11+-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-lightgrey.svg)

一个功能完善的在线书店系统，提供图书浏览、购买、用户管理、订单处理等功能。

## ✨ 功能特点

- 📖 图书浏览与搜索
- 👤 用户注册与登录
- 🛒 购物车功能
- 📦 订单管理
- ⭐ 用户评论与评分系统
- 👑 管理员后台管理

## 🔧 技术栈

- **前端**：HTML, CSS, JavaScript
- **后端**：Java
- **数据库**：MySQL
- **Web容器**：Tomcat
- **构建工具**：Maven

## 📂 项目结构

```
BookstoreWeb/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bookstore/
│   │   │           ├── controller/  # 控制器层
│   │   │           ├── dao/         # 数据访问层
│   │   │           ├── model/       # 数据模型
│   │   │           ├── service/     # 服务层
│   │   │           └── util/        # 工具类
│   │   ├── resources/               # 配置文件
│   │   └── webapp/                  # Web资源
│   │       ├── WEB-INF/
│   │       │   └── views/           # JSP视图
│   │       ├── css/                 # 样式文件
│   │       └── js/                  # JavaScript文件
│   └── test/                        # 测试代码
├── pom.xml                          # Maven配置
└── README.md                        # 项目说明
```

## 🚀 安装与运行

### 前提条件

- JDK 8+
- MySQL 5.7+
- Maven 3.6+
- Tomcat 9+

### 安装步骤

1. **克隆仓库**
   ```bash
   git clone https://github.com/your-username/online-bookstore.git
   cd online-bookstore
   ```

2. **配置数据库**
   - 创建名为`bookstore`的数据库
   - 导入`sql/bookstore.sql`文件

3. **配置数据库连接**
   - 修改`src/main/resources/database.properties`文件中的数据库连接信息

4. **编译项目**
   ```bash
   mvn clean package
   ```

5. **部署到Tomcat**
   - 将生成的WAR文件部署到Tomcat的webapps目录

6. **访问应用**
   - 打开浏览器访问：http://localhost:8080/BookstoreWeb

## 📖 使用指南

详细的使用说明请参考 [使用文档](development_documentation.md)。

## 🔄 开发流程

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m '添加某功能'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建Pull Request

## 📝 待办事项

查看 [todo.md](todo.md) 了解计划中的功能和待完成的任务。

## 🔧 环境配置

详细的环境配置指南请参考 [environment_configuration.md](environment_configuration.md)。

## 📄 许可证

该项目采用 MIT 许可证 - 详情请参阅 [LICENSE](LICENSE) 文件。

## 👥 贡献者

- 开发者名字 - [GitHub](https://github.com/zhuhuichen)

## 📞 联系方式

如有任何问题或建议，请通过以下方式联系：

- 电子邮件：colors0874@gmail.com
- 项目地址：[GitHub](https://github.com/ckxkx/BookstoreWeb.git) 
