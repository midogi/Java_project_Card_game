// DeckImpl.java
package GameClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckImpl implements Card_game.Deck {
    private final List<Card_game.Card> cards;

    public DeckImpl() {
        cards = new ArrayList<>();
        String[] suits = {"Hearts", "Spades", "Clubs", "Diamonds"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new CardImpl(suit, rank));
            }
        }

        // Add Jokers
        cards.add(new CardImpl("Joker", "Color"));
        cards.add(new CardImpl("Joker", "Black & White"));
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public CardImpl drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No more cards in the deck.");
        }
        return (CardImpl) cards.remove(cards.size() - 1);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void addCards(List<CardImpl> reshuffledCards) {
        cards.addAll(reshuffledCards);
    }

    public int remainingCards() {
        return cards.size();
    }
}