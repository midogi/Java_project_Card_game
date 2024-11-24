package GameServer;

import GameClass.Card;
import GameClass.Deck;
import GameClass.Player;

import java.io.*;
import java.net.*;
import java.util.*;

public class BlackjackServer {
    private static final int PORT = 12345;
    private static List<Player> players = new ArrayList<>();
    private static Deck deck = new Deck();
    private static boolean gameStarted = false;
    private static int dealerScore = 0; // 딜러의 점수 변수

    public static void main(String[] args) {
        System.out.println("블랙잭 서버 시작...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private Player player;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // 플레이어 이름 받기
                out.println("이름을 입력하세요:");
                String name = in.readLine();
                player = new Player(name, socket);
                players.add(player);
                System.out.println(name + "이(가) 연결되었습니다.");

                // 게임 시작
                if (!gameStarted) {
                    gameStarted = true;
                    startGame();
                }

                // 게임 루프
                while (true) {
                    out.println("현재 점수: " + player.getScore());
                    out.println("카드를 추가하려면 'hit', 종료하려면 'stand'를 입력하세요:");
                    String command = in.readLine();

                    if ("hit".equalsIgnoreCase(command)) {
                        Card card = deck.drawCard();
                        if (card != null) {
                            player.addCard(card);
                            out.println("당신이 뽑은 카드: " + card);
                            out.println("점수: 플레이어: " + player.getScore()); // 플레이어 점수 전송

                            // 딜러 점수 확인 및 카드 추가
                            if (dealerScore < 17) {
                                Card dealerCard = deck.drawCard();
                                if (dealerCard != null) {
                                    // 딜러의 카드 추가 로직
                                    // 예시: dealerCards.append(dealerCard).append(", ");
                                    // dealerScore = calculateDealerScore(dealerCards);
                                    // broadcast("딜러의 새로운 카드: " + dealerCard);
                                    // broadcast("점수: 딜러: " + dealerScore);

                                    // 딜러 카드 추가 및 점수 계산
                                    dealerScore += getCardValue(dealerCard);
                                    broadcast("딜러의 카드가 추가되었습니다: " + dealerCard);
                                    broadcast("점수: 딜러: " + dealerScore);
                                }
                            }
                            if(player.getScore() > 21){
                                out.println("게임을 종료합니다.");
                                out.println(player.getName() + "이 버스트당했습니다. 딜러가 승리했습니다.!!");
                                break;
                            }
                            else if(dealerScore > 21){
                                out.println("게임을 종료합니다.");
                                out.println("딜러가 버스트당했습니다." + player.getName() + "이 승리했습니다!!");
                            }
                        } else {
                            out.println("더 이상 카드가 없습니다.");
                        }
                    } else if ("stand".equalsIgnoreCase(command)) {
                        out.println("게임을 종료합니다");
                        out.println(player.getName() + "의 점수 : " + player.getScore());
                        broadcast("딜러의 카드 공개: " + dealerScore);
                        broadcast("점수 : 딜러: " + dealerScore);
                        out.println("딜러의 점수 : " + dealerScore);
                        if(player.getScore()<=21 && player.getScore()>dealerScore){
                            out.println(player.getName() + "이 승리하였습니다!!!");
                        }
                        else if(dealerScore <= 21 && player.getScore()<dealerScore){
                            out.println("딜러가 승리하였습니다!!!");
                        }
                        break;
                    }

                    // 점수 확인
                    /*
                    if (player.getScore() > 21) {
                        out.println("버스트! 점수가 21을 초과했습니다.");
                        break;
                    }*/
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                players.remove(player);
                System.out.println(player.getName() + "이(가) 연결을 종료했습니다.");
            }
        }

        private void startGame() {
            for (Player player : players) {
                StringBuilder playerCards = new StringBuilder();
                for (int j = 0; j < 2; j++) {
                    Card card = deck.drawCard();
                    player.addCard(card);
                    playerCards.append(card).append(", "); // 카드 정보를 누적
                }
                broadcast(player.getName() + "의 카드: " + playerCards.toString());
                broadcast("점수: 플레이어: " + player.getScore()); // 플레이어 점수 전송
                System.out.println(player.getName() + "의 카드: " + playerCards);
            }

            // 딜러 카드 처리
            StringBuilder dealerCards = new StringBuilder();
            Card dealerFirstCard = deck.drawCard(); // 첫 번째 카드 (뒷면으로 표시)
            Card dealerSecondCard = deck.drawCard(); // 두 번째 카드 (앞면으로 표시)

            dealerCards.append(dealerFirstCard).append(", ").append(dealerSecondCard); // 카드 정보를 누적
            broadcast("딜러의 카드: [뒷면], " + dealerSecondCard); // 첫 번째 카드는 뒷면으로 보여줌
            dealerScore = calculateDealerScore(dealerCards); // 딜러 점수 계산
            broadcast("점수: 딜러: " + dealerScore); // 딜러 점수 전송
            broadcast("게임이 시작되었습니다!");
        }


        private void broadcast(String message) {
            for (Player player : players) {
                try {
                    // 각 플레이어에게 메시지 전송
                    PrintWriter playerOut = new PrintWriter(player.getSocket().getOutputStream(), true);
                    playerOut.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private int calculateDealerScore(StringBuilder dealerCards) {
            String[] cardInfos = dealerCards.toString().split(", ");
            int totalScore = 0;
            int aceCount = 0;

            for (String cardInfo : cardInfos) {
                String[] cardParts = cardInfo.split(" of ");
                String rank = cardParts[0];

                switch (rank) {
                    case "A":
                        totalScore += 11; // A는 일단 11로 계산
                        aceCount++;
                        break;
                    case "K":
                    case "Q":
                    case "J":
                        totalScore += 10;
                        break;
                    default:
                        totalScore += Integer.parseInt(rank); // 숫자 카드
                        break;
                }
            }

            // A의 점수를 조정 (11에서 1로)
            while (totalScore > 21 && aceCount > 0) {
                totalScore -= 10; // A의 점수를 1로 조정
                aceCount--;
            }

            return totalScore; // 최종 점수 반환
        }

        private int getCardValue(Card card) {
            String rank = card.getRank();
            switch (rank) {
                case "A":
                    return 11; // 기본적으로 A는 11로 계산
                case "K":
                case "Q":
                case "J":
                    return 10;
                default:
                    return Integer.parseInt(rank); // 숫자 카드
            }
        }
    }
}
