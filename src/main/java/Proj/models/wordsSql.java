package Proj.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class wordsSql {

	private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
	
	public static String getWord(String word){
		return word;
	}    
	
	public static int getInt(int number){
		return number;
	}    
    
	public void addWord(String word, int test_id) throws Exception{
		try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/memory?" + "user=root&password=Tulving67");

            statement = connect.createStatement();
            preparedStatement = connect.prepareStatement("insert into  memory.words values (?, ?)");

            preparedStatement.setInt(1, test_id);
            preparedStatement.setString(2, word);
            preparedStatement.executeUpdate();

		} catch (Exception e) {
            throw e;
		} finally {
            close();
    	}
	
	}
	
	public int addTest(int userid) throws Exception{
		try {
			int returnedId =0;
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/memory?" + "user=root&password=Tulving67");

            statement = connect.createStatement();
            preparedStatement = connect.prepareStatement("insert into  memory.tests values (default, ?, ?)");

            preparedStatement.setInt(1, 0);
            preparedStatement.setInt(2, (userid));
            preparedStatement.executeUpdate();
            //definitely works up to here
            
            preparedStatement = connect.prepareStatement("select MAX(test_id) from memory.tests where userid = ?");
            preparedStatement.setInt(1, userid);
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
	
	public void addWord(int testid, String word) throws Exception{
		try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/memory?" + "user=root&password=Tulving67");

            statement = connect.createStatement();
            preparedStatement = connect.prepareStatement("insert into  memory.words values (?, ?)");

            preparedStatement.setInt(1, testid);
            preparedStatement.setString(2, word);
            preparedStatement.executeUpdate();

		} catch (Exception e) {
            throw e;
		} finally {
            close();
    	}
	
	}
	
	public ArrayList<Integer> selTestIds(int userId) throws Exception{
		try {
			ArrayList<Integer> testsList = new ArrayList<Integer>();
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/memory?" + "user=root&password=Tulving67");

            statement = connect.createStatement();
            preparedStatement = connect.prepareStatement("select test_id from memory.tests where userid = ?");

            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()){
            	testsList.add(resultSet.getInt(1));;
            }
            
            return testsList;

		} catch (Exception e) {
            throw e;
		} finally {
            close();
    	}
	}
	
	public ArrayList<String> selTestWords(int testId) throws Exception{
		try {
			ArrayList<String> wordsList = new ArrayList<String>();
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/memory?" + "user=root&password=Tulving67");

            statement = connect.createStatement();
            preparedStatement = connect.prepareStatement("select word from memory.words where test_id = ?");

            preparedStatement.setInt(1, testId);
            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()){
            	wordsList.add(resultSet.getString(1));;
            }
            
            return wordsList;

		} catch (Exception e) {
            throw e;
		} finally {
            close();
    	}
	}
	
	public double grade(String userAnswer, ArrayList<String> answerKey, ArrayList<String> userAnswersList){
		
		double score = 0.0;
		
		if (userAnswersList.contains(userAnswer)){
			return 0.0;
		}
		
		if (answerKey.contains(userAnswer)){
			score = score + 1.0;
		}
		
		return score;
	}
	
	public void saveScore(double newScore, int testid) throws Exception{
		try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/memory?" + "user=root&password=Tulving67");

            statement = connect.createStatement();
            preparedStatement = connect.prepareStatement("insert into  memory.scores values (default, ?, ?)");

            preparedStatement.setDouble(1, newScore);
            preparedStatement.setInt(2, testid);
            preparedStatement.executeUpdate();

		} catch (Exception e) {
            throw e;
		} finally {
            close();
    	}
	}
	
	public ArrayList<Double> showAvg(int testid) throws Exception{
		try {
			ArrayList<Double> scoreList = new ArrayList<Double>();
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/memory?" + "user=root&password=Tulving67");

            statement = connect.createStatement();
            preparedStatement = connect.prepareStatement("select score from memory.scores where test_id = ?");

            preparedStatement.setInt(1, testid);
            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()){
            	scoreList.add(resultSet.getDouble(1));;
            }
            
            return scoreList;
   
            //for (int i = 0; i < scoreList.size(); i++)  {
             //   avg = avg + scoreList.get(i); 
            //}  
            //return avg/scoreList.size();

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
