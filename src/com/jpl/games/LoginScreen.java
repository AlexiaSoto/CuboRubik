package com.jpl.games;

import com.jpl.games.model.ParticipantModel;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginScreen extends Application {

    private ParticipantModel participantModel = new ParticipantModel();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inicio de sesión");
        primaryStage.setResizable(false);

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("/com/jpl/games/images/Cuborubik.jpg", 900, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Background background = new Background(backgroundImage);

        VBox layout = new VBox(20);
        layout.setBackground(background);
        layout.setAlignment(Pos.CENTER);

        TextField playerNameField = new TextField();
        playerNameField.setPromptText("Nombre del jugador");
        Button startButton = new Button("Ingresar");
        startButton.setOnAction(e -> {
            participantModel.setName(playerNameField.getText());
            openRubikWindow(primaryStage);
        });

        layout.getChildren().addAll(playerNameField, startButton);

        Scene scene = new Scene(layout, 900, 800);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
    private void openRubikWindow(Stage primaryStage) {
        RubikFX rubikFX = new RubikFX(participantModel);
        Stage stage = new Stage();
        rubikFX.start(stage);
        primaryStage.close(); // Cierra la ventana de inicio de sesión
    }
    public static void main(String[] args) {
        launch(args);
    }
}

