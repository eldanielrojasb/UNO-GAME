package org.example.eiscuno.model.player;

import org.example.eiscuno.model.card.Card;

public interface IPlayer {
    void addCard(Card card);

    Card getCard(int index);
}
