package GameClass;

import java.util.ArrayList;
import java.util.List;

public class PlayerImpl implements Card_game.Player {
    private final String name;
    private final List<CardImpl> hand;

    public PlayerImpl(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    @Override
    public void receiveCard(Card_game.Card card) {
        if (card instanceof CardImpl) {
            hand.add((CardImpl) card);
        } else {
            throw new IllegalArgumentException("Card must be of type CardImpl.");
        }
    }

    @Override
    public Card_game.Card playCard(Card_game.Card topCard) {
        for (CardImpl card : hand) {
            if (card.getSuit().equals(topCard.getSuit()) || card.getRank().equals(topCard.getRank())) {
                hand.remove(card);
                return card;
            }
        }
        return null; // No valid card to play
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<CardImpl> getHand() {
        return hand;
    }

    public void drawCards(Card_game.Deck deck, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            receiveCard(deck.drawCard());
        }
    }

    public boolean hasCards() {
        return !hand.isEmpty();
    }
}
