package org.example.eiscuno.model.game;

import javafx.application.Platform;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.observer.Observer;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameUnoTest {

    private GameUno game;
    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;

    @BeforeAll
    public static void setUpClass() {

        Platform.startup(() -> {

        });
    }

    @AfterAll
    public static void tearDownClass() {

        Platform.exit();
    }

    @BeforeEach
    public void setUp() {
        humanPlayer = new Player("Human");
        machinePlayer = new Player("Machine");
        deck = new Deck(); // Use a real Deck instead of a mock
        table = new Table();
        game = new GameUno(humanPlayer, machinePlayer, deck, table);
    }

    @Test
    public void testPlayCardAddsToTable() {
        Card card = deck.takeCard();
        game.playCard(card);
        assertEquals(card, table.getCurrentCardOnTheTable());
    }

    @Test
    public void testObserversNotifiedWhenCardPlayed() {
        TestObserver observer = new TestObserver();
        game.addObserver(observer);
        Card card = deck.takeCard();
        game.playCard(card);
        assertTrue(observer.isUpdated());
    }


    private static class TestObserver implements Observer {
        private boolean updated = false;

        @Override
        public void update() {
            updated = true;
        }

        public boolean isUpdated() {
            return updated;
        }
    }
}

