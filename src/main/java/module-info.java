module com.heshanthenura.mousewheelshortcut {
    requires javafx.controls;
    requires javafx.fxml;
    requires jnativehook;
    requires java.logging;
    requires java.desktop;


    opens com.heshanthenura.mousewheelshortcut to javafx.fxml;
    exports com.heshanthenura.mousewheelshortcut;
}