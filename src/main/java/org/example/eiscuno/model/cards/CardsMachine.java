package org.example.eiscuno.model.cards;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Class CardsMachine
 *
 * This class represents a card machine and provides functionality to print multiple cards in a {@link GridPane}.
 */
public class CardsMachine extends Card {

    /**
     * Constructor for the CardsMachine class.
     *
     * @param url the URL of the card's image
     */
    public CardsMachine(String url) {
        super(url);
    }

    /**
     * Prints a specific number of cards in the provided {@link GridPane}.
     *
     * @param gridPane the {@link GridPane} where the cards will be printed
     * @param numCards the number of cards to print
     */
    @Override
    public void printCards(GridPane gridPane, int numCards) {
        int i = 1;

        while (i < numCards + 1) {
            ImageView card = new ImageView(getCardImage());
            card.setFitHeight(100);
            card.setFitWidth(80);
            gridPane.add(card, i, 0);
            i++;
        }
    }
}
