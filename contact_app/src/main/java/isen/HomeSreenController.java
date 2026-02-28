package isen;

import isen.db.entities.Person;
import isen.db.daos.PersonDao;

import java.sql.Date;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;

public class HomeSreenController implements Initializable {
  @FXML 
  private TableView<Person> personsTable;

  @FXML 
  private TableColumn<Person, String> personColumn;

  @FXML 
  private AnchorPane formPane;

  @FXML 
  private TextField lastnameField;

  @FXML 
  private TextField firstnameField;

  @FXML 
  private TextField nicknameField;

  @FXML 
  private TextField phone_numberField;

  @FXML 
  private TextField addressField;

  @FXML 
  private TextField email_addressField;

  @FXML 
  private DatePicker birth_dateField;

  @FXML
  private Person selectedPerson;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    personColumn.setCellValueFactory(cell -> new SimpleStringProperty(
        cell.getValue().getFirstName() + " " + cell.getValue().getLastName()));

    loadPersons();
    personsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
      showPerson(newSel);
    });
  }

  private void loadPersons() {
    personsTable.getItems().setAll(new PersonDao().listPeople());
  }

  private void showPerson(Person person) {
    selectedPerson = person;
    if (person == null) {
      clearForm();
      return;
    }
    firstnameField.setText(person.getFirstName());
    lastnameField.setText(person.getLastName());
    nicknameField.setText(person.getNickname());
    phone_numberField.setText(person.getPhoneNumber());
    addressField.setText(person.getAddress());
    email_addressField.setText(person.getEmailAddress());
    birth_dateField.setValue(person.getBirthDate());
  }

  private void clearForm() {
    firstnameField.clear();
    lastnameField.clear();
    nicknameField.clear();
    phone_numberField.clear();
    addressField.clear();
    email_addressField.clear();
    birth_dateField.setValue(null);
    selectedPerson = null;
  }

  @FXML
  private void handleNewButton() {
    clearForm();
    personsTable.getSelectionModel().clearSelection();
  }

  @FXML
  private void handleSaveButton() {
    if (firstnameField.getText().isBlank() || lastnameField.getText().isBlank()) {
      return;
    }

    Date birthDate = null;
    if (birth_dateField.getValue() != null) {
      birthDate = Date.valueOf(birth_dateField.getValue());
    }

    if (selectedPerson == null) {
      PersonDao.addPerson(
          firstnameField.getText(),
          lastnameField.getText(),
          nicknameField.getText(),
          phone_numberField.getText(),
          addressField.getText(),
          email_addressField.getText(),
          birthDate);
    } else {
      selectedPerson.setFirstName(firstnameField.getText());
      selectedPerson.setLastName(lastnameField.getText());
      selectedPerson.setNickname(nicknameField.getText());
      selectedPerson.setPhoneNumber(phone_numberField.getText());
      selectedPerson.setAddress(addressField.getText());
      selectedPerson.setEmailAddress(email_addressField.getText());
      selectedPerson.setBirthDate(birth_dateField.getValue());
      PersonDao.updatePerson(selectedPerson);
    }

    loadPersons();
    clearForm();
  }

  @FXML
  private void handleDeleteButton() {
    if (selectedPerson != null) {
      PersonDao.removePerson(selectedPerson.getId());
      loadPersons();
      clearForm();
    }
  }
}
