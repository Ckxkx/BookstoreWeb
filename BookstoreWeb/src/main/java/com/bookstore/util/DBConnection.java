package com.bookstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * 数据库连接工具类
 */
public class DBConnection {
    private static final String PROPERTIES_FILE = "database.properties";
    private static Properties properties = new Properties();
    
    static {
        try {
            // 加载配置文件
            InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                // 如果配置文件不存在，使用默认配置
                properties.setProperty("driver", "com.mysql.cj.jdbc.Driver");
                properties.setProperty("url", "jdbc:mysql://localhost:3306/bookstore?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
                properties.setProperty("username", "root");
                properties.setProperty("password", "root");
            }
            
            // 加载数据库驱动
            Class.forName(properties.getProperty("driver"));
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取数据库连接
     * @return 数据库连接对象
     * @throws SQLException 数据库连接异常
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            properties.getProperty("url"),
            properties.getProperty("username"),
            properties.getProperty("password")
        );
    }
    
    /**
     * 关闭数据库连接
     * @param connection 数据库连接对象
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
