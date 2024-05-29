package org.example.eiscuno;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.eiscuno.view.GameUnoStage;

import java.io.IOException;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        GameUnoStage.getInstance();
    }
}
