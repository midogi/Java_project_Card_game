package GameClass;

import java.util.*;

public class One_Card implements Card_game.Game {
    private List<PlayerImpl> players;
    private DeckImpl deck;
    private Stack<CardImpl> discardPile;
    private PlayerImpl currentPlayer;
    private boolean forcePlayAnyCard;
    private int accumulatedDraw;

    public One_Card(List<PlayerImpl> players) {
        this.players = players;
        this.deck = new DeckImpl();
        this.discardPile = new Stack<>();
        this.forcePlayAnyCard = false;
        this.accumulatedDraw = 0;
    }

    @Override
    public void startGame() {
        deck.shuffle();

        for (PlayerImpl player : players) {
            for (int i = 0; i < 7; i++) {
                player.receiveCard(deck.drawCard());
            }
        }

        // 첫 카드 설정 - 조커 카드가 아닌 카드를 뽑을 때까지 반복
        CardImpl firstCard;
        do {
            firstCard = deck.drawCard();
        } while (firstCard.getRank().equalsIgnoreCase("Color") || firstCard.getRank().equalsIgnoreCase("Black & White"));

        discardPile.push(firstCard);
        currentPlayer = players.get(0);

        System.out.println("게임 시작! 첫 카드: " + discardPile.peek());
        System.out.println("첫 번째 플레이어: " + currentPlayer.getName());
    }

    @Override
    public void playTurn(Card_game.Player player) {
        PlayerImpl current = (PlayerImpl) player;
        System.out.println(current.getName() + "의 턴입니다.");
    }

    @Override
    public PlayerImpl determineWinner() {
        for (PlayerImpl player : players) {
            if (player.getHand().isEmpty()) {
                return player;
            }
        }
        return null;
    }

    // 카드 플레이 메소드
    public void playCard(PlayerImpl player, CardImpl cardToPlay) {
        if (canPlayCard(cardToPlay)) {
            player.getHand().remove(cardToPlay);
            discardPile.push(cardToPlay);

            // 일반 카드를 내는 경우 누적된 드로우 처리
            if (accumulatedDraw > 0 && !isSpecialCard(cardToPlay)) {
                System.out.println(player.getName() + "가 누적된 " + accumulatedDraw + "장의 카드를 뽑습니다.");
                drawCards(player, accumulatedDraw);
                accumulatedDraw = 0;
            }

            handleSpecialCard(cardToPlay);
            System.out.println(player.getName() + "가 " + cardToPlay + " 카드를 냈습니다.");
        } else {
            throw new IllegalArgumentException("유효하지 않은 카드 선택입니다!");
        }
    }

    // 카드 플레이 가능 여부 확인
    public boolean canPlayCard(CardImpl card) {
        // 조커 카드는 언제든지 낼 수 있음
        if (card.getRank().equalsIgnoreCase("Color") || card.getRank().equalsIgnoreCase("Black & White")) {
            return true;
        }
        if (forcePlayAnyCard) {
            return true;
        }
        CardImpl topCard = discardPile.peek();
        return card.getSuit().equals(topCard.getSuit()) || card.getRank().equals(topCard.getRank());
    }

    // 특수 카드 처리
    private void handleSpecialCard(CardImpl card) {
        forcePlayAnyCard = false;
        switch (card.getRank()) {
            case "2":
                System.out.println("다음 플레이어는 2장의 카드를 뽑아야 합니다!");
                accumulatedDraw += 2;
                break;
            case "A":
                System.out.println("다음 플레이어는 3장의 카드를 뽑아야 합니다!");
                accumulatedDraw += 3;
                break;
            case "Color":
            case "Black & White":
                System.out.println("다음 플레이어는 5장의 카드를 뽑아야 합니다!");
                accumulatedDraw += 5;
                forcePlayAnyCard = true;
                break;
            default:
                if (accumulatedDraw > 0) {
                    accumulatedDraw = 0;
                }
                break;
        }
    }

    private boolean isSpecialCard(CardImpl card) {
        return card.getRank().equals("2") || card.getRank().equals("A") ||
                card.getRank().equalsIgnoreCase("Color") || card.getRank().equalsIgnoreCase("Black & White");
    }

    // 카드 뽑기 메소드
    public void drawCard(PlayerImpl player) {
        if (accumulatedDraw > 0) {
            System.out.println(player.getName() + "가 " + accumulatedDraw + "장의 카드를 뽑습니다.");
            drawCards(player, accumulatedDraw);
            accumulatedDraw = 0;
        } else {
            if (deck.isEmpty()) {
                reshuffleDiscardPile();
            }
            if (!deck.isEmpty()) {
                player.receiveCard(deck.drawCard());
            } else {
                System.out.println("뽑을 수 있는 카드가 없습니다.");
            }
        }
    }

    private void drawCards(PlayerImpl player, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            if (deck.isEmpty()) {
                reshuffleDiscardPile();
            }
            if (!deck.isEmpty()) {
                player.receiveCard(deck.drawCard());
            } else {
                System.out.println("뽑을 수 있는 카드가 없습니다.");
                break;
            }
        }
    }

    // 턴 넘김 메소드
    public void nextTurn() {
        PlayerImpl winner = determineWinner();
        if (winner != null) {
            System.out.println(winner.getName() + "가 승리하였습니다!");
            return; // 승자가 나왔으므로 게임을 종료
        }
        int currentIndex = players.indexOf(currentPlayer);
        currentPlayer = players.get((currentIndex + 1) % players.size());
    }

    // 현재 플레이어 가져오기
    public PlayerImpl getCurrentPlayer() {
        return currentPlayer;
    }

    // 탑 카드 가져오기
    public CardImpl getTopCard() {
        return discardPile.peek();
    }

    // 누적 드로우 수 가져오기
    public int getAccumulatedDraw() {
        return accumulatedDraw;
    }

    // 버린 카드 더미 다시 섞기
    private void reshuffleDiscardPile() {
        System.out.println("버린 카드 더미를 다시 섞습니다...");
        CardImpl topCard = discardPile.pop();
        List<CardImpl> reshuffledCards = new ArrayList<>(discardPile);
        Collections.shuffle(reshuffledCards);
        discardPile.clear();
        discardPile.push(topCard);
        deck.addCards(reshuffledCards);
    }
}