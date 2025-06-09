package br.com.javafxsecurekey.model.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ToggleSwitch extends StackPane {
    private final BooleanProperty switchedOn = new SimpleBooleanProperty(false);
    private final Label background = new Label();
    private final Circle trigger = new Circle(10);

    public ToggleSwitch() {
        background.setMinSize(40, 20);
        background.setMaxSize(40, 20);
        background.setStyle("-fx-background-color: gray; -fx-background-radius: 10;");
        setPadding(new Insets(5));
        getChildren().addAll(background, trigger);
        setMinSize(50, 30);

        trigger.setTranslateX(-10);
        trigger.setFill(Color.WHITE);
        trigger.setStroke(Color.LIGHTGRAY);

        switchedOn.addListener((obs, oldState, newState) -> {
            if (newState) {
                background.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 10;");
                trigger.setTranslateX(10);
            } else {
                background.setStyle("-fx-background-color: gray; -fx-background-radius: 10;");
                trigger.setTranslateX(-10);
            }
        });

        setOnMouseClicked(event -> switchedOn.set(!switchedOn.get()));
    }

    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }

    public boolean isSwitchedOn() {
        return switchedOn.get();
    }

    public void setSwitchedOn(boolean value) {
        switchedOn.set(value);
    }
}
