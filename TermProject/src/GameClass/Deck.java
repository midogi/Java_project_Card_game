package GameClass;

import java.util.ArrayList;
import java.util.Collections; //리스트를 섞는 용도
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards); // 카드 섞기
    }
    
    // 카드 덱이 비어있는지 확인하는 메서드
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    // 카드 섞기
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // 덱에서 한 장의 카드 뽑는 함수
    public Card drawCard() {
        if (cards.isEmpty()) {
            return null; // 카드가 없을 경우 
        }
        return cards.remove(0); // 덱에서 첫 번째 카드 반환하고 제거
    }

    // 버린 카드들을 다시 덱에 추가하는 메서드
    public void addCards(List<Card> newCards) {
        cards.addAll(newCards);
    }
}
