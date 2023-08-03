package com.heshanthenura.mousewheelshortcut;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;

import java.net.URL;
import java.security.Provider;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainController implements Initializable {

    public static Logger fxLogger = Logger.getLogger("FXLogger");


    public static int count=0;

    @FXML
    public Text CountLbl;

    @FXML
    private Arc upperHalf;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Service<Void> listenerService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {

                        Listener.setCountLbl(CountLbl);
                        Listener.setArc(upperHalf);
                        new Listener.GlobalKeyMouseListener().startListening();
                        return null;
                    }
                };
            }
        };
        listenerService.start();
    }
}