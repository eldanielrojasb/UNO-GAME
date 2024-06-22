package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable{
    private ArrayList<Card> cardsPlayer;
    public static long startTimeM;
    public static long startTime;
    public static long endTimeM;
    public static long durationM;



    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer){
        this.cardsPlayer = cardsPlayer;
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
        if(cardsPlayer.size() == 1 ){

            long startTimeM = System.nanoTime();
            long startTime = System.nanoTime();
            System.out.println("UNO");
            long endTimeM = System.nanoTime();
            long durationM = endTimeM - startTimeM;
        }
    }
}
