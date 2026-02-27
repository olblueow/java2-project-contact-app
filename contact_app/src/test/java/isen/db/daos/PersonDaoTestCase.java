package isen.db.daos;

import isen.db.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class PersonDaoTestCase {

	private final PersonDao personDao = new PersonDao();

	@BeforeEach
	public void initDatabase() throws Exception {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS person (\n" +
                        "    idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n" +
                        "    lastname VARCHAR(45) NOT NULL,  \n" +
                        "    firstname VARCHAR(45) NOT NULL,\n" +
                        "    nickname VARCHAR(45) NOT NULL,\n" +
                        "    phone_number VARCHAR(15) NULL,\n" +
                        "    address VARCHAR(200) NULL,\n" +
                        "    email_address VARCHAR(150) NULL,\n" +
                        "    birth_date DATE NULL);");
		stmt.executeUpdate("DELETE FROM person");
		stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='person'");
		stmt.executeUpdate("INSERT INTO person(idperson,firstname,lastname,nickname,phone_number,address,email_address,birth_date) VALUES (1,'James','Lemoine','Jamie','12345','41 Boulevard Vauban','james.lemoins@student.junia.com','1970-11-26 12:00:00.000')");
		stmt.executeUpdate("INSERT INTO person(idperson,firstname,lastname,nickname,phone_number,address,email_address,birth_date) VALUES (2,'Cesar','Dablemont','Salade','67890','3 Rue Norbert Segard','cesar.dablemont@student.junia.com','2000-6-12 12:00:00.000')");
		stmt.executeUpdate("INSERT INTO person(idperson,firstname,lastname,nickname,phone_number,address,email_address,birth_date) VALUES (3,'Olivier','Clavier','Touche','1234567890','16 Rue Colson','olivier.clavier@student.junia.com','2015-3-18 12:00:00.000')");
		stmt.close();
		connection.close();
	}

	@Test
	public void shouldListPeople() {
		// WHEN
		List<Person> people = personDao.listPeople();
		// THEN
		assertThat(people).hasSize(3);
        assertThat(people).extracting(Person::getId, Person::getFirstName, Person::getLastName, Person::getNickname, Person::getPhoneNumber, Person::getAddress, Person::getEmailAddress, Person::getBirthDate).containsOnly(
                tuple(1,"James","Lemoine","Jamie","12345","41 Boulevard Vauban","james.lemoins@student.junia.com",new Date(1970-1900,10,26).toLocalDate()),
                tuple(2,"Cesar","Dablemont","Salade","67890","3 Rue Norbert Segard","cesar.dablemont@student.junia.com",new Date(2000-1900,5,12).toLocalDate()),
                tuple(3,"Olivier","Clavier","Touche","1234567890","16 Rue Colson","olivier.clavier@student.junia.com",new Date(2015-1900,2,18).toLocalDate()));
	}
	
	@Test
	public void shouldGetPersonByName() {
		// WHEN
		Person person = personDao.getPersonByName("Cesar","Dablemont");
		// THEN
		assertThat(person.getId()).isEqualTo(2);
		assertThat(person.getFirstName()).isEqualTo("Cesar");
        assertThat(person.getLastName()).isEqualTo("Dablemont");
        assertThat(person.getNickname()).isEqualTo("Salade");
        assertThat(person.getPhoneNumber()).isEqualTo("67890");
        assertThat(person.getAddress()).isEqualTo("3 Rue Norbert Segard");
        assertThat(person.getEmailAddress()).isEqualTo("cesar.dablemont@student.junia.com");
        assertThat(person.getBirthDate()).isEqualTo(new Date(2000-1900,5,12).toLocalDate());
	}
	
	@Test
	public void shouldNotGetUnknownPerson() {
		// WHEN
		Person person = personDao.getPersonByName("Unknown","Unknown");
		// THEN
		assertThat(person).isNull();
	}
	
	@Test
	public void shouldAddPerson() throws Exception {
		// WHEN 
		PersonDao.addPerson("Clement","Brisson","Clem","0987654321","39 Boulevard Vauban", "clement.brisson@student.junia.com",new Date(2004-1900,6,19));
		// THEN
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE firstname='Clement' AND lastname = 'Brisson'");
		assertThat(resultSet.next()).isTrue();
		assertThat(resultSet.getInt("idperson")).isNotNull();
		assertThat(resultSet.getString("firstname")).isEqualTo("Clement");
        assertThat(resultSet.getString("lastname")).isEqualTo("Brisson");
        assertThat(resultSet.getString("nickname")).isEqualTo("Clem");
        assertThat(resultSet.getString("phone_number")).isEqualTo("0987654321");
        assertThat(resultSet.getString("address")).isEqualTo("39 Boulevard Vauban");
        assertThat(resultSet.getString("email_address")).isEqualTo("clement.brisson@student.junia.com");
        assertThat(resultSet.getDate("birth_date")).isEqualTo(new Date(2004-1900,6,19));
		assertThat(resultSet.next()).isFalse();
		resultSet.close();
		statement.close();
		connection.close();
	}

    @Test
    public void shouldRemovePerson() throws Exception {
        // GIVEN
        PersonDao.addPerson("Clement","Brisson","Clem","0987654321","39 Boulevard Vauban", "clement.brisson@student.junia.com",new Date(2004-1900,6,19));
        // THEN
        PersonDao.removePerson(4);
        //THEN
        Connection connection = DataSourceFactory.getDataSource().getConnection();
        Statement statement = connection.createStatement();
        Person person = personDao.getPersonByName("Clement","Brisson");
        assertThat(person).isNull();
    }
}
