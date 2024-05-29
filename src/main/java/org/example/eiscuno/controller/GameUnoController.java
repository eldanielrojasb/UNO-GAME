package org.example.eiscuno.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.EISCUnoEnum;
import org.example.eiscuno.model.cards.CardsMachine;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController {

    @FXML
    private Button buttonDeck;

    @FXML
    private Button buttonUno;

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView imageTable;

    private CardsMachine cardsMachine;

    private int numCardsMachine;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize(){
        printCardsMachine();
    }

    /**
     * Prints cards for the machine player.
     */
    private void printCardsMachine(){
        numCardsMachine = 4;
        cardsMachine = new CardsMachine(EISCUnoEnum.CARD_UNO.getFilePath());
        cardsMachine.printCards(gridPaneCardsMachine, numCardsMachine);
    }
}
