package org.example.eiscuno.model.cards;

import javafx.scene.layout.GridPane;

/**
 * Interface ICard
 *
 * This interface defines the contract for printing cards in a {@link GridPane}.
 */
public interface ICard {

    /**
     * Prints a specific number of cards in the provided {@link GridPane}.
     *
     * @param gridPane the {@link GridPane} where the cards will be printed
     * @param numCards the number of cards to print
     */
    void printCards(GridPane gridPane, int numCards);
}

