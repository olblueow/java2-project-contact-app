package isen.db.daos;

import isen.db.entities.Person;

import java.sql.*;
import java.time.LocalDate;
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
                    collectionPerson.add(new Person(
                            resultSet.getInt("idperson"),
                            resultSet.getString("firstname"),
                            resultSet.getString("lastname"),
                            resultSet.getString("nickname"),
                            resultSet.getString("phone_number"),
                            resultSet.getString("address"),
                            resultSet.getString("email_address"),
                            resultSet.getDate("birth_date").toLocalDate()));
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

    public Person getPersonByName(String firstName,String lastName) {
        Person requestedPerson = null;
        var dataSource = DataSourceFactory.getDataSource();
        try (Connection connection = dataSource.getConnection()) { // Connect to database
            try (Statement statement = connection.createStatement()) { // Create a statement
                String query = "SELECT * FROM person WHERE firstname = ? AND lastname = ?";
                try (PreparedStatement PreparedStatement = connection.prepareStatement(query)) { // Prepare a statement
                    PreparedStatement.setString(1, firstName);
                    PreparedStatement.setString(2, lastName);
                    ResultSet resultSet = PreparedStatement.executeQuery();
                    if (resultSet.next()) { // If there is something in the resultSet, return it
                        requestedPerson = new Person(resultSet.getInt("idperson"),
                                resultSet.getString("firstname"),
                                resultSet.getString("lastname"),
                                resultSet.getString("nickname"),
                                resultSet.getString("phone_number"),
                                resultSet.getString("address"),
                                resultSet.getString("email_address"),
                                resultSet.getDate("birth_date").toLocalDate());
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

    public static void addPerson(String firstName, String lastName, String nickname, String phoneNumber, String address, String emailAddress, Date birthDate) {
        var dataSource = DataSourceFactory.getDataSource();
        try (Connection connection = dataSource.getConnection()) { // Connect to database
            try (Statement statement = connection.createStatement()) { // Create a statement

                String query = "INSERT INTO person(firstname, lastname, nickname, phone_number, address, email_address, birth_date) VALUES(?,?,?,?,?,?,?)";
                try (PreparedStatement PreparedStatement = connection.prepareStatement(query)) { // Prepare the statement
                    PreparedStatement.setString(1, firstName);
                    PreparedStatement.setString(2, lastName);
                    PreparedStatement.setString(3, nickname);
                    PreparedStatement.setString(4,phoneNumber);
                    PreparedStatement.setString(5,address);
                    PreparedStatement.setString(6,emailAddress);
                    PreparedStatement.setDate(7,birthDate);
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

    public static void removePerson(Integer id) {
        var dataSource = DataSourceFactory.getDataSource();
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                String query = "DELETE FROM person WHERE idperson=?";
                try (PreparedStatement PreparedStatement = connection.prepareStatement(query)) {
                    PreparedStatement.setInt(1,id);
                    PreparedStatement.executeUpdate();
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
