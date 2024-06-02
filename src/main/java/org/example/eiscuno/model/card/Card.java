package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card {
    private String url;
    private String nameCard;

    public Card(String url, String nameCard) {
        this.url = url;
        this.nameCard = nameCard;
    }

    public ImageView getCard() {
        ImageView card = new ImageView(new Image(String.valueOf(getClass().getResource(url))));
        card.setY(16);
        card.setFitHeight(90);
        card.setFitWidth(70);
        return card;
    }
}
