package GameClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class One_Card {
    private Deck deck;
    private List<Card> discardPile; // 버린 카드 더미
    private Player player1;
    private Player player2;
    private Card topCard; // 현재 바닥에 놓인 카드
    private Player currentPlayer; // 현재 플레이어

    public One_Card(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.deck = new Deck();
        this.discardPile = new ArrayList<>();
        startGame();
    }

    // 게임 시작 함수
    public void startGame() {
        // 각 플레이어에게 카드 7장씩 지급
        for (int i = 0; i < 7; i++) {
            player1.receiveCard(deck.drawCard());
            player2.receiveCard(deck.drawCard());
        }

        // 버린 카드 더미의 초기 카드 설정
        topCard = deck.drawCard();
        discardPile.add(topCard);

        // Player1부터 시작
        currentPlayer = player1;
    }

    // 플레이어가 카드 내는 함수
    public boolean playTurn(Player player, int cardIndex) {
        if (player != currentPlayer) {
            System.out.println("It's not your turn!");
            return false;
        }

        Card chosenCard = player.getHand().get(cardIndex);

        // 낼 수 있는 카드인지 확인
        if (canPlayCard(chosenCard)) {
            player.getHand().remove(cardIndex);
            discardPile.add(chosenCard);
            topCard = chosenCard;
            applySpecialCardEffect(chosenCard); // 특수 카드 효과 적용

            // 턴 종료 및 다음 플레이어로 변경
            currentPlayer = (currentPlayer == player1) ? player2 : player1;
            return true;
        } else {
            System.out.println("You cannot play this card!");
            return false;
        }
    }

    // 특수 카드 효과 적용
    private void applySpecialCardEffect(Card card) {
        String rank = card.getRank();
        if (rank.equals("2")) {
            currentPlayer.receiveCard(deck.drawCard());
            currentPlayer.receiveCard(deck.drawCard());
        } else if (rank.equals("A")) {
            currentPlayer.receiveCard(deck.drawCard());
            currentPlayer.receiveCard(deck.drawCard());
            currentPlayer.receiveCard(deck.drawCard());
        } else if (rank.equals("Joker")) {
            for (int i = 0; i < 5; i++) {
                currentPlayer.receiveCard(deck.drawCard());
            }
        }
    }

    // 카드가 현재 탑 카드와 일치하는지 검사
    private boolean canPlayCard(Card card) {
        return card.getSuit().equals(topCard.getSuit()) || card.getRank().equals(topCard.getRank());
    }

    // 승자가 있는지 확인하는 함수
    public Player checkWinner() {
        if (player1.getHand().isEmpty()) {
            return player1;
        } else if (player2.getHand().isEmpty()) {
            return player2;
        }
        return null;
    }

    // 뒷면 카드 뭉치 재생성
    private void reshuffleDeck() {
        if (deck.isEmpty()) {
            Card lastCard = discardPile.remove(discardPile.size() - 1);
            deck.addCards(discardPile);
            discardPile.clear();
            discardPile.add(lastCard);
            deck.shuffle();
        }
    }

    // 카드 뽑기
    public void drawCard(Player player) {
        if (deck.isEmpty()) {
            reshuffleDeck();
        }
        player.receiveCard(deck.drawCard());
    }
}
