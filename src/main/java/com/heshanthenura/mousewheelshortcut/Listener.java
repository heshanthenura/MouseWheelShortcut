package com.heshanthenura.mousewheelshortcut;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.awt.MouseInfo;
import java.awt.PointerInfo;

import static com.heshanthenura.mousewheelshortcut.MainController.count;

public class Listener {

    public static Logger infoLogger = Logger.getLogger("InfoLogger");
    public static Text countLbl;
    public static Stage stage;
    public static Arc upperHalf;
    private static boolean middleMouseButtonPressed = false;

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        new GlobalKeyMouseListener().startListening();

    }

    public static void setCountLbl(Text lbl){
        countLbl=lbl;
        infoLogger.info(String.valueOf(countLbl));
    }
    public static void setStage(Stage stg){
        stage=stg;
        infoLogger.info(String.valueOf(stage));
    }
    public static void setArc(Arc aH){
        upperHalf=aH;
    }

    static class GlobalKeyMouseListener implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener {


        public void startListening() {

            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);


            infoLogger.info("Starting Listner");

            try {
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException ex) {
                ex.printStackTrace();
                return;
            }

            GlobalScreen.addNativeKeyListener(this);
            GlobalScreen.addNativeMouseListener(this);
            GlobalScreen.addNativeMouseMotionListener(this);
        }

        public void stopListening() {
            GlobalScreen.removeNativeKeyListener(this);
            GlobalScreen.removeNativeMouseListener(this);
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void nativeKeyPressed(NativeKeyEvent e) {
            // Check if the pressed key is "E"
            if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase("E")) {
                infoLogger.info("Key 'E' Pressed");
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent e) {
            // Do something when a key is released
        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent e) {
            // Do something when a key is typed (pressed and released)
        }

        @Override
        public void nativeMouseClicked(NativeMouseEvent e) {

        }


        // Middle mouse button click listener
        @Override
        public void nativeMousePressed(NativeMouseEvent e) {

            if (e.getButton() == NativeMouseEvent.BUTTON3) {
                upperHalf.setFill(Color.rgb(0, 211, 35));

                middleMouseButtonPressed = true;
                Platform.runLater(() -> {
                    stage.setIconified(false);
                    stage.setAlwaysOnTop(true);
                    stage.requestFocus();
                });

                infoLogger.info("Middle Mouse Button Clicked");
                count+=1;
                infoLogger.info(String.valueOf(count));

                Platform.runLater(()->{
                countLbl.setText(String.valueOf(count));
                });

                PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                int mouseX = (int) pointerInfo.getLocation().getX();
                int mouseY = (int) pointerInfo.getLocation().getY();

                // Print the coordinates
                infoLogger.info("Mouse X: " + mouseX + ", Mouse Y: " + mouseY);

                Platform.runLater(() -> {
                    stage.setX(mouseX-(stage.getWidth()/2));
                    stage.setY(mouseY-(stage.getHeight()/2));
                });

                Platform.runLater(() -> {
                    stage.requestFocus();
                    stage.setAlwaysOnTop(false);
                });

            }

        }


        // Middle mouse button release listener
        @Override
        public void nativeMouseReleased(NativeMouseEvent e) {
            if (e.getButton() == NativeMouseEvent.BUTTON3) {
                middleMouseButtonPressed = false;
                infoLogger.info("Middle Mouse Button Released");
                upperHalf.setFill(Color.rgb(0, 211, 35));

                Platform.runLater(() -> stage.setIconified(true));

                if (isCursorOnUpperHalf()) {
                    // Print "Took screenshot" message
                    new Screenshot().takeAndSaveScreenshot();
                    infoLogger.info("Took screenshot");
                }
            }
        }

        private boolean isCursorOnUpperHalf() {
            if (upperHalf != null) {
                Point2D globalArcCenterLocation = upperHalf.localToScreen(upperHalf.getCenterX(), upperHalf.getCenterY());
                double centerX = globalArcCenterLocation.getX();
                double centerY = globalArcCenterLocation.getY();
                double radius = upperHalf.getRadiusX();

                // Get the current mouse position
                PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                double mouseX = pointerInfo.getLocation().getX();
                double mouseY = pointerInfo.getLocation().getY();

                // Calculate the distance between the mouse pointer and the center of the Arc
                double distance = Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));

                // Check if the mouse is inside the upper half of the semicircle and the distance is greater than 5 pixels
                return (mouseY <= centerY && distance > 5);
            }
            return false;
        }



        @Override
        public void nativeMouseMoved(NativeMouseEvent e) {

        }


        @Override
        public void nativeMouseDragged(NativeMouseEvent e) {
            if (middleMouseButtonPressed) {
                double mouseX = e.getX();
                double mouseY = e.getY();
                infoLogger.info("Mouse Moved while middle mouse button pressed: X=" + mouseX + ", Y=" + mouseY);

                if (upperHalf != null) {
                    Point2D globalArcCenterLocation = upperHalf.localToScreen(upperHalf.getCenterX(), upperHalf.getCenterY());
                    double centerX = globalArcCenterLocation.getX();
                    double centerY = globalArcCenterLocation.getY();
                    double radius = upperHalf.getRadiusX();

                    // Calculate the distance between the mouse pointer and the center of the Arc
                    double distance = Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));

                    // Check if the mouse is inside the upper half of the semicircle and the distance is greater than 5 pixels
                    if (mouseY <= centerY && distance > 20) {
                        Platform.runLater(() -> {
                            upperHalf.setFill(Color.RED);
                            infoLogger.info("Mouse is inside the upper half of the semicircle and distance is greater than 5px");
                        });
                    } else {
                        Platform.runLater(() -> {
                            upperHalf.setFill(Color.rgb(0, 211, 35));
                            infoLogger.info("Mouse is outside the upper half of the semicircle or distance is less than or equal to 5px");
                        });
                    }
                }
            }
        }



    }

}
