package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

import java.util.concurrent.atomic.AtomicBoolean;


public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private Deck deck;

    private GridPane gridPaneCardsMachine;
    private volatile boolean hasPlayerPlayed;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView,GridPane gridPaneCardsMachine, Deck deck) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.gridPaneCardsMachine = gridPaneCardsMachine;
        this.deck = deck;
        this.hasPlayerPlayed = false;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (this) {
                    while (!hasPlayerPlayed) {
                        wait(); // Esperar hasta que el jugador haya jugado
                    }
                    // Sección crítica: haPlayerPlayed es true
                    Thread.sleep(2000); // Esperar 2 segundos antes de jugar la carta

                    // Lógica para colocar la carta
                    putCardOnTheTable();

                    // Reiniciar la bandera para que el hilo espere de nuevo
                    hasPlayerPlayed = false;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restablecer la bandera de interrupción
            }
        }
    }

    private void putCardOnTheTable() {
        Card card = null;
        AtomicBoolean validCard = new AtomicBoolean(false);


        while (!validCard.get() && machinePlayer.getCardsPlayer().size() > 0) {
            int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
            card = machinePlayer.getCard(index);
            //es valida si cumple con ser +4, WILD, o el color o el número
            validCard.set(card.getValue().equals("+4") || card.getValue().equals("WILD") || card.getColor().equals(table.getCurrentCardOnTheTable().getColor()) || card.getValue().equals(table.getCurrentCardOnTheTable().getValue()));

            // falta colocar la lógica de los poderes aquí

            // En caso de ser +4
            // En caso de ser +2
            // En caso de ser WILD
            // En caso de ser SKIP
            // En caso de ser RESERVE

            if (validCard.get()) {
                table.addCardOnTheTable(card);
                tableImageView.setImage(card.getImage());
                machinePlayer.removeCard(index);
                Platform.runLater(() -> {
                    if (!gridPaneCardsMachine.getChildren().isEmpty()) {
                        gridPaneCardsMachine.getChildren().remove(0);
                    }
                });

            } else {
                //Si no tiene, come.
                Card newCard = deck.takeCard();
                machinePlayer.addCard(newCard);
                validCard.set(true); //Sale del bucle, así solo come 1
                System.out.println("Comí una");
                Platform.runLater(() -> {
                    ImageView cardBackImageView = new ImageView(EISCUnoEnum.CARD_UNO.getFilePath());
                    cardBackImageView.setFitWidth(90);
                    cardBackImageView.setFitHeight(100);
                    cardBackImageView.setPreserveRatio(true);
                    gridPaneCardsMachine.add(cardBackImageView, gridPaneCardsMachine.getChildren().size(), 0);
                });
            }
        }
    }

    public synchronized void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
        notify();
    }
}