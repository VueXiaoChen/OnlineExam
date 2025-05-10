package com.example.onlineexam.filter;

import java.sql.*;

public class JdbcIdTest {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/xc_onlineexam?tinyInt1isBit=false&characterEncoding=UTF8&autoReconnect=true&serverTimezone=Asia/Shanghai&allowMultiQueries=true&returnGeneratedKeys=true";
        String sql = "INSERT INTO comment (vid, content,parent_id,to_user_id,love,bad,is_top,is_deleted,create_time) VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(url, "root", "123456");
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, 3);
            stmt.setString(2, "测试");
            stmt.setInt(3, 1);
            stmt.setInt(4, 1);
            stmt.setInt(5, 1);
            stmt.setInt(6, 1);
            stmt.setInt(7, 1);
            stmt.setInt(8, 1);
            stmt.setDate(9, new Date(1799999999));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("Generated ID: " + rs.getInt(1));  // 必须打印非零值
            }
        }
    }
}
