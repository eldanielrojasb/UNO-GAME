package org.example.eiscuno.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.example.eiscuno.model.Card;
import org.example.eiscuno.model.EISCUnoEnum;

public class GameUnoController {

    @FXML
    private Button buttonDeck;

    @FXML
    private Button buttonUno;

    @FXML
    private GridPane cardsMachine;

    @FXML
    private GridPane cardsPlayer;

    @FXML
    private ImageView imageTable;

    @FXML
    public void initialize(){
        printDeckofCards();
        printUno();
    }

    private void printDeckofCards(){
        buttonDeck.setBackground(
                new Background(
                        new BackgroundImage(
                                new Card(EISCUnoEnum.DECK_OF_CARDS.getFilePath()).getCardImage(),
                                BackgroundRepeat.REPEAT,
                                BackgroundRepeat.REPEAT,
                                BackgroundPosition.DEFAULT,
                                new BackgroundSize(
                                100,
                                149,
                                false,
                                false,
                                false,
                                false
                        ))));
    }

    private void printUno(){
        buttonUno.setBackground(
                new Background(
                        new BackgroundImage(
                                new Card(EISCUnoEnum.BUTTON_UNO.getFilePath()).getCardImage(),
                                BackgroundRepeat.REPEAT,
                                BackgroundRepeat.REPEAT,
                                BackgroundPosition.DEFAULT,
                                new BackgroundSize(
                                        100,
                                        50,
                                        false,
                                        false,
                                        false,
                                        false
                                ))));
    }
}
