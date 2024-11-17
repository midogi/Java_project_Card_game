package GameClass;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;
    private int score; 

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    public void addCard(Card card) {
        hand.add(card);
        score += card.getValue();
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getScore() {
        return score;
    }
    //이름 반환
    public String getName() {
        return name;
    }
    
    public void receiveCard(Card card) {
    	hand.add(card);
    }

    public void reset() {
        hand.clear();
        score = 0;
    }
    
    public boolean isBust() {
    	return getScore()>21;
    }
}
