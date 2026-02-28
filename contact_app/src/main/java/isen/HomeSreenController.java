package isen;

import isen.db.entities.Person;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class HomeSreenController {
  @FXML private TableView<Person> personsTable;
  @FXML private TableColumn<Person, String> personColumn;
  @FXML private AnchorPane formPane;
  @FXML private TextField lastnameField;
  @FXML private TextField firstnameField;
  @FXML private TextField nicknameField;
  @FXML private TextField phone_numberField;
  @FXML private TextField addressField;
  @FXML private TextField email_addressField;
  @FXML private DatePicker birth_dateField;

  @FXML
  private void handleNewButton() {}

  @FXML
  private void handleSaveButton() {}

  @FXML
  private void handleDeleteButton() {}
}
