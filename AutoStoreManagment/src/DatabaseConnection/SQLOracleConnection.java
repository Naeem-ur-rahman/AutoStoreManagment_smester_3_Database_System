/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DatabaseConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Naeem ur Rahman
 */
public class SQLOracleConnection {
    
 private Connection con;
    
    public  Connection databaseConnection() {
    
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url      = "jdbc:oracle:thin:@localhost:1521:xe";
            String username = "system";
            String password = "123";
            con = DriverManager.getConnection(url,username,password);
            System.out.println("Connecton Successfully........................! ");
            
        }catch(ClassNotFoundException | SQLException e){
            System.out.println(e);
        }
        return con;
    }
}
