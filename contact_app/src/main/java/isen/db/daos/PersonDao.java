package isen.db.daos;

import isen.db.entities.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDao {
    public List<Person> listPeople() {
        List<Person> collectionPerson = new ArrayList<Person>();
        var dataSource = DataSourceFactory.getDataSource();

        try (Connection connection = dataSource.getConnection()) { // Connect to database

            try (Statement statement = connection.createStatement()) { // Create a statement

                String query = "SELECT * FROM person";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) { // Transform ResultSet into Persons in a collection
                    collectionPerson.add(new Person(resultSet.getInt("idperson"), resultSet.getString("firstname"),resultSet.getString("lastname"),resultSet.getInt("age")));
                }

                statement.close();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }

            connection.close();
            return collectionPerson;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Person getPerson(String firstName,String lastName) {
        Person requestedPerson = null;
        var dataSource = DataSourceFactory.getDataSource();
        try (Connection connection = dataSource.getConnection()) { // Connect to database
            try (Statement statement = connection.createStatement()) { // Create a statement
                String query = "SELECT * FROM person WHERE firstname = ? AND lastname = ?";
                try (PreparedStatement PreparedStatement = connection.prepareStatement(query)) { // Preapare a statement
                    PreparedStatement.setString(1, firstName);
                    PreparedStatement.setString(2, lastName);
                    ResultSet resultSet = PreparedStatement.executeQuery();
                    if (resultSet.next()) { // If there is something in the resultSet, return it
                        requestedPerson = new Person(resultSet.getInt("idperson"), resultSet.getString("firstname"),resultSet.getString("lastname"),resultSet.getInt("age"));
                    }

                    return requestedPerson;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addPerson(String firstName, String lastName, Integer age) {
        var dataSource = DataSourceFactory.getDataSource();
        try (Connection connection = dataSource.getConnection()) { // Connect to database
            try (Statement statement = connection.createStatement()) { // Create a statement

                String query = "INSERT INTO person(firstname, lastname,age) VALUES(?,?,?)";
                try (PreparedStatement PreparedStatement = connection.prepareStatement(query)) { // Prepare the statement
                    PreparedStatement.setString(1, firstName);
                    PreparedStatement.setString(2, lastName);
                    PreparedStatement.setInt(3, age);
                    PreparedStatement.executeUpdate();
                }
                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                statement.close();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
            connection.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
