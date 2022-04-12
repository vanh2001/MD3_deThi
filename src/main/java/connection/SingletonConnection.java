package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    public static final String URL = "jdbc:mysql://localhost:3306/quanLySanPham";
    public static final String USER = "root";
    public static final String PASSWORD = "12345678";

    private SingletonConnection() {
    }

    public static Connection connection;

    public static Connection getConnection(){
        if (connection == null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(
                        URL,
                        USER,
                        PASSWORD);
                System.out.println("Ket noi thanh cong");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
