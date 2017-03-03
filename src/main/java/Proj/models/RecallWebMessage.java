package Proj.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class RecallWebMessage {
	
	private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    

	public static String getMessage(String name){
		return "Hello, " + name + "!";
	}
	
	public void register(String name, String password) throws Exception{
		try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/memory?" + "user=root&password=Tulving67");
            statement = connect.createStatement();
            preparedStatement = connect.prepareStatement("insert into  memory.admins values (default, ?, ?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();

		} catch (Exception e) {
            throw e;
		} finally {
            close();
    	}
	
	}
	
	public int saveId(String name, String password) throws Exception{
		try {
			int returnedId = 0;
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/memory?" + "user=root&password=Tulving67");

            statement = connect.createStatement();
            preparedStatement = connect.prepareStatement("select userid from memory.admins where username = ? and password = ?");
            
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
            	returnedId = resultSet.getInt(1);
            }
            
             return returnedId;
             
		} catch (Exception e) {
            throw e;
		} finally {
            close();
    	}
	}
	
	public Boolean authenticate(String name, String password) throws Exception{
		try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/memory?" + "user=root&password=Tulving67");

            statement = connect.createStatement();
            preparedStatement = connect.prepareStatement("select userid from memory.admins where username = ? and password = ?");
            // ("userid, username, password");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()){
            	return true;
            }
            else{
            	return false;
            }

		} catch (Exception e) {
            throw e;
		} finally {
            close();
    	}
	
	}
    
    public void close() {
        try {
                if (resultSet != null) {
                        resultSet.close();
                }

                if (statement != null) {
                        statement.close();
                }

                if (connect != null) {
                        connect.close();
                }
        } catch (Exception e) {

        }
    }
	
}
