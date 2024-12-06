// CardImpl.java
package GameClass;

public class CardImpl implements Card_game.Card {
    private final String suit;
    private final String rank;

    public CardImpl(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public String getSuit() {
        return suit;
    }

    @Override
    public String getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}