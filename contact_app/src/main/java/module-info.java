module isen {
    requires javafx.controls;
    requires javafx.fxml;

    opens isen to javafx.fxml;
    exports isen;
}
