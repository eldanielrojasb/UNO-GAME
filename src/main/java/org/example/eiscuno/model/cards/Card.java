package org.example.eiscuno.model.cards;

import javafx.scene.image.Image;

/**
 * Abstract class Card
 *
 * This class represents a card and provides basic functionality for handling the card's image.
 */
public abstract class Card implements ICard {
    private Image cardImage;

    /**
     * Constructor for the Card class.
     *
     * @param url the URL of the card's image
     */
    public Card(String url) {
        this.cardImage = new Image(String.valueOf(getClass().getResource(url)));
    }

    /**
     * Gets the card's image.
     *
     * @return the card's image
     */
    public Image getCardImage() {
        return this.cardImage;
    }
}
