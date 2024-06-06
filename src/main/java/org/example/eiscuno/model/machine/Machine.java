package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

public class Machine
        //implements Runnable
        extends Thread
    {
    private ArrayList<Card> cardsPlayer;

    public Machine (ArrayList<Card> cardsPlayer){
        this.cardsPlayer = cardsPlayer;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep((long) (Math.random() * 3000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasOneCardTheHumanPlayer();
        }
    }

    private void hasOneCardTheHumanPlayer(){
        if(cardsPlayer.size() == 1){
            System.out.println("UNO");
        }
    }
}
