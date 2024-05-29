package org.example.eiscuno.model;

import javafx.scene.image.Image;

public class Card {
    private Image cardImage;

    public Card(String url) {
        this.cardImage = new Image(String.valueOf(getClass().getResource(url)));
    }

    public Image getCardImage() {
        return this.cardImage;
    }
}


