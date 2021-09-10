import com.github.javafaker.Faker;


import java.sql.*;

public class SchoolDatabase {
    static int id;
    static  int unique(int ids){
        id +=ids;
        return id;
    }

    public static void main(String[] args)
    {


        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate("drop table if exists personaldetails");
            statement.executeUpdate("create table personaldetails(id INT PRIMARY KEY, firstName string, lastName String,email String, streetAddress String)");
            String excuteInsert = "INSERT INTO personaldetails(id,firstName,lastName,email,streetAddress) VALUES (?,?,?,?,?)";


            for (int i=0;i < 100_000;i++) {
                Faker faker = new Faker();

                int id = unique(1);
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String email = faker.internet().emailAddress();
                String streetAddress = faker.address().streetAddress();
                PreparedStatement preparedStmt = connection.prepareStatement(excuteInsert);

                //set parameters in the statement
                preparedStmt.setInt(1, id);
                preparedStmt.setString(2, firstName);
                preparedStmt.setString(3, lastName);
                preparedStmt.setString(4, email);
                preparedStmt.setString(5, streetAddress);

                //execute statement
                preparedStmt.executeUpdate();



                ResultSet rs = statement.executeQuery("SELECT * from personaldetails");
                while (rs.next()) {
                    // read the result set

                    System.out.println("name = " + rs.getString("firstName"));
                    System.out.println("email = " + rs.getString("email"));
                    System.out.println("id = " + rs.getInt("id"));
                }
            }


        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }






    }






