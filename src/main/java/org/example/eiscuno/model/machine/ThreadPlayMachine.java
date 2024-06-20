package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import javafx.scene.layout.GridPane;


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
        boolean validCard = false;

        while (!validCard && machinePlayer.getCardsPlayer().size() > 0) {
            int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
            card = machinePlayer.getCard(index);

            validCard = card.getValue().equals("+4") || card.getValue().equals("WILD") || card.getColor().equals(table.getCurrentCardOnTheTable().getColor()) || card.getValue().equals(table.getCurrentCardOnTheTable().getValue());

            if (validCard) {
                table.addCardOnTheTable(card);
                tableImageView.setImage(card.getImage());
                machinePlayer.removeCard(index);
                Platform.runLater(() -> {
                    if (!gridPaneCardsMachine.getChildren().isEmpty()) {
                        gridPaneCardsMachine.getChildren().remove(0);
                    }
                });

            } else {

                Card newCard = deck.takeCard();
                machinePlayer.addCard(newCard);
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