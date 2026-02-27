module isen {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens isen to javafx.fxml;
    exports isen;
}
