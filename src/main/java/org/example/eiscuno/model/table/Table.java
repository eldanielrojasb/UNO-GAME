package org.example.eiscuno.model.table;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * Represents the table in the Uno game where cards are played.
 */
public class Table  {
    private ArrayList<Card> cardsTable;

    /**
     * Constructs a new Table object with no cards on it.
     */
    public Table() {
        this.cardsTable = new ArrayList<Card>();
    }

    /**
     * Adds a card to the table.
     *
     * @param card The card to be added to the table.
     */
    public Boolean addCardOnTheTable(Card card) {
        Card currentCardOnTheTable = this.cardsTable.get(this.cardsTable.size() - 1);

        if (currentCardOnTheTable.getColor().equals(card.getColor()) ||
                currentCardOnTheTable.getValue().equals(card.getValue())
        ) {
            this.cardsTable.add(card);
            return true;
        }

        return false;
    }

    public ArrayList<Card> getCardsTable() {
        return cardsTable;
    }

    public void setStartCard(Card card) {
        this.cardsTable.add(card);
    }
}
