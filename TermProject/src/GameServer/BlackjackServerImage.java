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
                player = new Player(name);
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
                            out.println("당신이 뽑은 카드: " + card); //카드정보 클라이언트로 전송
                        } else {
                            out.println("더 이상 카드가 없습니다.");
                        }
                    } else if ("stand".equalsIgnoreCase(command)) {
                        out.println("게임을 종료합니다. 최종 점수: " + player.getScore());
                        break;
                    }

                    // 점수 확인
                    if (player.getScore() > 21) {
                        out.println("버스트! 점수가 21을 초과했습니다.");
                        break;
                    }
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
                for (int j = 0; j < 2; j++) {
                    Card card = deck.drawCard();
                    player.addCard(card);
                    //broadcast(player.getName() + "의 카드: " + card);
                    System.out.println(player.getName() + "의 카드: " + card);
                }
            }
            broadcast("게임이 시작되었습니다!");
        }

        private void broadcast(String message) {
            for (Player player : players) {
                // 각 플레이어에게 메시지 전송
                // 이 부분은 클라이언트와의 연결이 필요하므로 추가적인 수정이 필요
            }
        }
    }
}
