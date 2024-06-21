package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController {

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView tableImageView;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;

    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;

    private Stage stage;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();
        printCardsHumanPlayer();
        printCardsMachinePlayer();

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer());
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView,this.gridPaneCardsMachine,this.deck);
        threadPlayMachine.start();
    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                //Si la carta es del mismo color o tiene un valor de valor
                if (card.getValue().equals("+4") || card.getValue().equals("SKIP") || card.getValue().equals("RESERVE") || card.getValue().equals("WILD") || card.getColor().equals(table.getCurrentCardOnTheTable().getColor()) || card.getValue().equals(table.getCurrentCardOnTheTable().getValue())) {
                    gameUno.playCard(card); //Juega la carta
                    tableImageView.setImage(card.getImage());
                    humanPlayer.removeCard((findPosCardsHumanPlayer(card)));
                    //Si un simple numero, lo pone y pasa turno, siempre y cuando lo que esta en la mesa no sea un cambia color
                    if (card.getValue().matches("[0-9]") && !table.getCurrentCardOnTheTable().getValue().equals("WILD")) {
                        threadPlayMachine.setHasPlayerPlayed(true); //pasa el turno
                    }

                    //Si la carta es un "+2", lo pone y no pasa turno, la maquina come 2
                    if (card.getValue().equals("+2")) {
                        for (int j = 0; j < 2; j++) {
                            machinePlayer.addCard(deck.takeCard());
                        }
                    }

                    //Si la carta es un "+4", lo pone y no pasa turno, la maquina come 4
                    if (card.getValue().equals("+4")) {
                        for (int j = 0; j < 4; j++) {
                            machinePlayer.addCard(deck.takeCard());
                        }
                    }

                }

                // Si la carta en la mesa es un cambia color, se le asigna el color que seleccione el usuario y cede el turno
                if (table.getCurrentCardOnTheTable().getValue().equals("WILD") && table.getCurrentCardOnTheTable().getColor().equals("NON_COLOR")) {
                    table.getCurrentCardOnTheTable().setColor(card.getColor());
                    threadPlayMachine.setHasPlayerPlayed(true); //pasa el turno
                }

                //Si la carta en la mesa es un +4, se selecciona el color de la carta seleccionada y se coloca la carta seleccionada
                if (table.getCurrentCardOnTheTable().getValue().equals("+4") && table.getCurrentCardOnTheTable().getColor().equals("NON_COLOR")) {
                    table.getCurrentCardOnTheTable().setColor(card.getColor());
                    System.out.println("Ahora el color es: " + table.getCurrentCardOnTheTable().getColor());
                    //gameUno.playCard(card);
                    //tableImageView.setImage(card.getImage());
                    //humanPlayer.removeCard((findPosCardsHumanPlayer(card)));
                    threadPlayMachine.setHasPlayerPlayed(true);
                }
                printCardsHumanPlayer();
            });

            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
    }
    private void printCardsMachinePlayer() {

        this.gridPaneCardsMachine.getChildren().clear();
        int numCardsMachinePlayer = 4;
        Card cardToPlay = this.machinePlayer.getCardsPlayer().get(0);

        gameUno.playCard(cardToPlay);

        tableImageView.setImage(cardToPlay.getImage());
        for (int i = 0; i < numCardsMachinePlayer; i++) {
            ImageView cardsMachine = new ImageView(EISCUnoEnum.CARD_UNO.getFilePath());
            cardsMachine.setFitWidth(90);
            cardsMachine.setFitHeight(100);
            cardsMachine.setPreserveRatio(true);
            this.gridPaneCardsMachine.add(cardsMachine, i, 0);
        }
    }
    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the action of taking a card and pass shift.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        Card newCard = deck.takeCard();
        humanPlayer.addCard(newCard);
        printCardsHumanPlayer();
        threadPlayMachine.setHasPlayerPlayed(true);
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        // Implement logic to handle Uno event here
    }

    @FXML
    void onHandleExit(ActionEvent event) {
        stage.close();
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}