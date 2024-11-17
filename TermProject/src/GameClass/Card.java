package GameClass;

public class Card {
    private String suit; // 카드 무늬
    private String rank; // 카드 숫자

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public int getValue() {
        if (rank.equals("A")) {
            return 11; // Ace는 11로 계산
        } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
            return 10; // Face 카드들은 10으로 계산
        } else {
            return Integer.parseInt(rank); // 숫자 카드
        }
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
