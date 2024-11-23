package GameClass;

import javax.swing.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;
    private int score;
    private Socket socket; //소켓를 추가해봄

    public Player(String name, Socket socket) {
        this.name = name;
        this.hand = new ArrayList<>();
        //this.score = 0;
        this.socket = socket;
    }

    public void addCard(Card card) {
        hand.add(card);
        if (card.getRank().equals("A")) {
            // Ace 카드일 경우 사용자에게 값을 선택하도록 요청
            int choice = JOptionPane.showOptionDialog(null,
                    "Ace를 1 또는 11로 설정하시겠습니까?",
                    "Ace 선택",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[] {"11", "1"},
                    "11");
            card.setAceValue(choice == 0 ? 11 : 1); // 선택에 따라 Ace의 값을 설정
        }
        calculateScore(); //카드 추가 후 점수 계산 이 코드 꼭 필요함 안해주면 점수 계산 안됨
    }


    public void calculateScore() {
        int totalScore = 0;
        int aceCount = 0;

        //점수 계산
        for(Card card : hand) {
            totalScore += card.getValue();
            if(card.getRank().equals("A")) {
                aceCount++;
            }
        }
        //ace가 11로 계산된 경우 점수 조정
        while(totalScore > 21 && aceCount > 0){
            totalScore -= 10;
            aceCount--;
        }
        setScore(totalScore);
    }
    public Socket getSocket() {
        return socket;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void reset() {
        hand.clear();
        //score = 0;
    }
}

