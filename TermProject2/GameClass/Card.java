package GameClass;

import javax.swing.ImageIcon;
public class Card {
    private String suit; // 카드 무늬
    private String rank; // 카드 숫자
    private ImageIcon image;
    private int value; // 카드의 현재 값

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        this.image = new ImageIcon("src/main/resources/cards/" + rank + "_of_" + suit + ".png"); //이미지경로 설정
        this.value = determinegetValue(); //카드의 밸류를 초기화 시켜줘야함 이 코드가 있어야 점수가 보임
        // Player 클래스의 addCard()에 있는 calculateScore()와 같이 매우 중요함 이것 때문에 상당히 애를 먹었음
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public int determinegetValue() {
        if (rank.equals("A")) {
            return 11; // Ace는 11로 계산
        } else if (rank.equals("K") || rank.equals("Q") || rank.equals("J")) {
            return 10; // Face 카드들은 10으로 계산
        } else {
            return Integer.parseInt(rank); // 숫자 카드
        }
    }

    public void setAceValue(int newValue) {
        if(rank.equals("A")) {
            this.value = newValue;
        }
    }

    public int getValue(){
        return value;
    }
    public ImageIcon getImage() {
        return image; //카드 이미지 반환
    }
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

