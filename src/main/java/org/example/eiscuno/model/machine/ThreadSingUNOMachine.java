package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;
    private volatile boolean unoCalled;
    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer){
        this.cardsPlayer = cardsPlayer;
        this.unoCalled = false;  // Inicialmente está en true

    }

    @Override
    public void run(){
        while (true){
            try {
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasOneCardTheHumanPlayer();
        }
    }

    private void hasOneCardTheHumanPlayer(){
        if(cardsPlayer.size() == 1){
            System.out.println("UNO");
            setUnoCalled(true);
        }
    }
    public synchronized boolean isUnoCalled() {
        return unoCalled;
    }

    public synchronized void setUnoCalled(boolean unoCalled) {
        this.unoCalled = unoCalled;
    }
}
