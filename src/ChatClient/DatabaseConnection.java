package ChatClient;

import java.sql.*;

public class DatabaseConnection {
	private Connection connection = null;
	private Statement statement = null;

	public void AddUser(ClientSide user, ClientSide friend)
	{
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			connection = DriverManager.getConnection("jdbc:sqlite:db/rvp_db.sqlite");
			statement = connection.createStatement();
			statement.executeUpdate("insert or replace into ClientSide values('" + user.getClientName() + "','" + friend.getClientName() + "')");
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
		}
		finally
		{
			if(connection != null)
			{
				try
				{
					connection.close();
				}
				catch(SQLException sqle)
				{
					System.err.println(sqle.getMessage());
				}
			}
		}
	}

	public void RemoveUser(ClientSide user)
	{
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try
		{
			connection = DriverManager.getConnection("jdbc:sqlite:db/rvp_db.sqlite");
			statement = connection.createStatement();
			statement.executeUpdate("delete from ClientSide where clientID='" + user.getClientName() + "'");
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
		}
		finally
		{
			if(connection != null)
			{
				try
				{
					connection.close();
				}
				catch(SQLException sqle)
				{
					System.err.println(sqle.getMessage());
				}
			}
		}
	}

	public void ShowAllFriendships()
	{
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			connection = DriverManager.getConnection("jdbc:sqlite:db/rvp_db.sqlite");
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from ClientSide");
			while(resultSet.next())
			{
				System.out.println("ClientID = " + resultSet.getString("clientID"));
				System.out.println("FriendID = " + resultSet.getString("friendID"));
			}
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
		}
		finally
		{
			if(connection != null)
			{
				try
				{
					connection.close();
				}
				catch(SQLException sqle)
				{
					System.err.println(sqle.getMessage());
				}
			}
		}
	}
}
