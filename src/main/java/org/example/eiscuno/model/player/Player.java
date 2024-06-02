package org.example.eiscuno.model.player;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

public class Player implements IPlayer {
    private ArrayList<Card> cardsPlayer;

    public Player(){};

    @Override
    public void addCard(Card card){
        cardsPlayer.add(card);
    }

    @Override
    public Card getCard(int index){
        return cardsPlayer.get(index);
    }
}
