package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;

import java.util.Random;
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
                    //hasPlayerPlayed = false;
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

            //Si colocó +4, escoge el color de la nueva carta, y repite turno
            if (hasPlayerPlayed && table.getCurrentCardOnTheTable().getValue().equals("+4") && table.getCurrentCardOnTheTable().getColor().equals("NON_COLOR")) {
                table.getCurrentCardOnTheTable().setColor(card.getColor());
                System.out.println("Ahora el color es: " + table.getCurrentCardOnTheTable().getColor());
                setHasPlayerPlayed(true); //repito turno
            }
            //Si colocó WILD, escoge el color de la nueva carta, y repite turno
            if (hasPlayerPlayed && table.getCurrentCardOnTheTable().getValue().equals("WILD") && table.getCurrentCardOnTheTable().getColor().equals("NON_COLOR")) {
                table.getCurrentCardOnTheTable().setColor(new String[]{"YELLOW", "GREEN", "RED", "BLUE"}[new Random().nextInt(4)]);
                System.out.println("Ahora el color es: " + table.getCurrentCardOnTheTable().getColor());
                setHasPlayerPlayed(false); //cede turno
            }


            if (validCard.get()) {

                //Si es un número juega y cede turno
                if (card.getValue().matches("[0-9]") && !table.getCurrentCardOnTheTable().getValue().equals("WILD")) {
                    table.addCardOnTheTable(card);
                    tableImageView.setImage(card.getImage());
                    machinePlayer.removeCard(index);
                    setHasPlayerPlayed(false); //pasa el turno
                }

                //Si es un +2, juega y repite turno
                if (card.getValue().equals("+2")){
                    table.addCardOnTheTable(card);
                    tableImageView.setImage(card.getImage());
                    machinePlayer.removeCard(index);
                    setHasPlayerPlayed(true); //repito turno
                }
                //Si es un +4 juega y repite turno
                if (card.getValue().equals("+4")){
                    table.addCardOnTheTable(card);
                    tableImageView.setImage(card.getImage());
                    machinePlayer.removeCard(index);
                    setHasPlayerPlayed(true); //repite turno
                }
                //Si es un cambia color, juega y repito turno
                if (card.getValue().equals("WILD")) {
                    table.addCardOnTheTable(card);
                    tableImageView.setImage(card.getImage());
                    machinePlayer.removeCard(index);
                    setHasPlayerPlayed(true); //repito turno
                }
                //Si es un SKIP juega y repite turno
                if (card.getValue().equals("SKIP") ||card.getValue().equals("RESERVE")){
                    table.addCardOnTheTable(card);
                    tableImageView.setImage(card.getImage());
                    machinePlayer.removeCard(index);
                    setHasPlayerPlayed(true); //repite turno
                }

                Platform.runLater(() -> {
                    if (machinePlayer.getCardsPlayer().size() < 4) {
                        gridPaneCardsMachine.getChildren().remove(0);
                    }
                });

            } else if (hasPlayerPlayed && table.getCurrentCardOnTheTable().getValue().equals("+4") && !table.getCurrentCardOnTheTable().getColor().equals("NON_COLOR")) {
                setHasPlayerPlayed(true);
            } else if (hasPlayerPlayed && table.getCurrentCardOnTheTable().getValue().equals("WILD") && !table.getCurrentCardOnTheTable().getColor().equals("NON_COLOR")) {
                validCard.set(true);//Sale del bucle
                setHasPlayerPlayed(false);//cede el turno
            } else {
                //Si no tiene, come.
                Card newCard = deck.takeCard();
                machinePlayer.addCard(newCard);
                validCard.set(true); //Sale del bucle, así solo come 1
                System.out.println("Comí una");
                System.out.println("tengo: " + machinePlayer.getCardsPlayer().size());
                setHasPlayerPlayed(false);
            }
            Platform.runLater(() -> {
                if((gridPaneCardsMachine.getChildren().size() == 5) && (machinePlayer.getCardsPlayer().size() > 3) ) {
                    ImageView cardBackImageView = new ImageView(EISCUnoEnum.CARD_UNO.getFilePath());
                    cardBackImageView.setFitWidth(90);
                    cardBackImageView.setFitHeight(100);
                    cardBackImageView.setPreserveRatio(true);
                    gridPaneCardsMachine.add(cardBackImageView, gridPaneCardsMachine.getChildren().size(), 0);
                    System.out.println("la baraja tiene: " +machinePlayer.getCardsPlayer().size());
                    System.out.println("ahora el pane tiene " + gridPaneCardsMachine.getChildren().size());
                }
            });
        }
    }

    public synchronized void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
        notify();
    }
}


