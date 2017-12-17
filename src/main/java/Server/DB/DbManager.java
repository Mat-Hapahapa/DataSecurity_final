package Server.DB;

import java.sql.*;

public class DbManager {

    public void test(){
        Connection connection;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sensorData");

            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM sensor");

            while (rs.next()){
                System.out.println(rs.getString("sensorID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
