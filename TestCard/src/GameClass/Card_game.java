package GameClass;

import java.util.List;

public interface Card_game {

    // 1. Card 인터페이스
    public interface Card {
        String getSuit();  // 카드 무늬 반환
        String getRank();  // 카드 숫자 또는 순위 반환
    }

    // 2. Deck 인터페이스
    public interface Deck {
        void shuffle(); // 카드 덱 섞기
        Card drawCard(); // 덱에서 카드 한 장 뽑기
    }

    // 3. Player 인터페이스
    public interface Player {
        void receiveCard(Card card); // 플레이어가 카드 받는 함수
        Card playCard(Card topCard); // 플레이어 손에 있는 카드 중 주어진 카드와 일치하는 카드 플레이하는 함수
        String getName(); // 플레이어 이름 반환
        List<CardImpl> getHand(); // 플레이어가 가진 카드 목록 반환 (타입을 CardImpl로 변경)
    }

    // 4. Game 인터페이스
    public interface Game {
        void startGame(); // 게임 시작
        void playTurn(Player player); // 특정 플레이어 턴 진행
        Player determineWinner(); // 게임 승자 결정
    }
}