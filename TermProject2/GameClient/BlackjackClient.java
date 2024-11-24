package GameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class BlackjackClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private JTextArea textArea;
    private String playerName;
    private JPanel playerCardPanel; // 플레이어 카드를 표시할 패널
    private JPanel dealerCardPanel; // 딜러 카드를 표시할 패널
    private JLabel playerScoreLabel; // 플레이어 점수 레이블

    public BlackjackClient() {
        JFrame frame = new JFrame("블랙잭 게임");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400); // 창 크기 조정

        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // 플레이어 카드 패널 추가 (OverlayLayout 사용)
        playerCardPanel = new JPanel();
        playerCardPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.add(playerCardPanel, BorderLayout.EAST);

        // 딜러 카드 패널 추가 (OverlayLayout 사용)
        dealerCardPanel = new JPanel();
        dealerCardPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        frame.add(dealerCardPanel, BorderLayout.WEST);

        // 점수 레이블 추가 (플레이어 점수를 우측 상단에 배치)
        playerScoreLabel = new JLabel("플레이어 점수: 0");

        JPanel scorePanel = new JPanel(); // 점수를 위한 패널
        scorePanel.setLayout(new BorderLayout());
        scorePanel.add(playerScoreLabel, BorderLayout.NORTH);
        frame.add(scorePanel, BorderLayout.NORTH); // 점수 패널을 프레임의 북쪽에 추가

        JPanel panel = new JPanel();
        JButton hitButton = new JButton("Hit");
        JButton standButton = new JButton("Stand");
        JButton exitButton = new JButton("Exit");

        panel.add(hitButton);
        panel.add(standButton);
        panel.add(exitButton);
        frame.add(panel, BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("hit");
            }
        });

        standButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage("stand");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.setVisible(true);
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // 플레이어 이름 입력
            playerName = JOptionPane.showInputDialog("서버에 연결할 이름을 입력하세요:");
            out.println(playerName);

            // 서버로부터 메시지를 수신하는 스레드 시작
            new Thread(new IncomingReader()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        out.println(message);
    }

    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    textArea.append(message + "\n");
                    if (message.contains("의 카드:")) { // 카드 정보 수신
                        String[] parts = message.split("의 카드: ");
                        if (parts.length > 1) {
                            String[] cardInfos = parts[1].split(", "); // 카드 정보 분리
                            if (parts[0].equals("딜러")) {
                                // 딜러 카드 표시, 첫 번째 카드는 뒷면
                                displayDealerCardImage("뒷면", false); // 뒷면 카드
                                for (int i = 1; i < cardInfos.length; i++) {
                                    displayDealerCardImage(cardInfos[i].trim(), true); // 두 번째 카드
                                }
                            } else {
                                for (String cardInfo : cardInfos) {
                                    displayPlayerCardImage(cardInfo.trim()); // 플레이어 카드 이미지 표시
                                }
                            }
                        }
                    } else if (message.startsWith("당신이 뽑은 카드:")) { // 플레이어의 카드 정보 수신
                        String[] parts = message.split(": ");
                        if (parts.length > 1) {
                            String cardInfo = parts[1];
                            displayPlayerCardImage(cardInfo);
                        }
                    } else if (message.startsWith("딜러의 카드 공개:")) { // 딜러 카드 공개 수신
                        String[] parts = message.split(": ");
                        if (parts.length > 1) {
                            String dealerCardsInfo = parts[1];
                            String[] dealerCardInfos = dealerCardsInfo.split(", ");
                            for (String dealerCardInfo : dealerCardInfos) {
                                displayDealerCardImage(dealerCardInfo.trim(), true); // 공개된 딜러 카드 이미지 표시
                            }
                        }
                    } else if (message.startsWith("점수:")) { // 점수 정보 수신
                        String[] parts = message.split(": ");
                        if (parts.length > 2) {
                            if (parts[1].equals("플레이어")) {
                                playerScoreLabel.setText("플레이어 점수: " + parts[2]);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayPlayerCardImage(String cardInfo) {
        displayCardImage(cardInfo, playerCardPanel);
    }

    private void displayDealerCardImage(String cardInfo, boolean isFaceUp) {
        // 카드 정보에서 랭크와 무늬를 분리
        String imagePath;
        if (isFaceUp) {
            String[] cardParts = cardInfo.split(" of ");
            if (cardParts.length == 2) {
                String rank = cardParts[0];
                String suit = cardParts[1].replace(".", ""); // 무늬에서 . 제거함
                imagePath = "src/main/resources/cards/" + rank + "_of_" + suit + ".png";
            } else {
                return; // 카드 정보가 잘못된 경우
            }
        } else {
            // 뒷면 카드
            imagePath = "src/main/resources/cards/card_back.png"; // 카드 뒷면 이미지 경로
        }

        // 이미지 크기 조정
        ImageIcon cardImage = new ImageIcon(imagePath);
        Image img = cardImage.getImage();
        Image scaledImgImage = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        cardImage = new ImageIcon(scaledImgImage);

        JLabel cardLabel = new JLabel(cardImage);
        dealerCardPanel.add(cardLabel); // 카드를 패널에 추가
        dealerCardPanel.revalidate(); // 패널 재조정
        dealerCardPanel.repaint(); // 패널 다시 그리기
    }

    private void displayCardImage(String cardInfo, JPanel panel) {
        // 카드 정보에서 랭크와 무늬를 분리
        String[] cardParts = cardInfo.split(" of ");
        if (cardParts.length == 2) {
            String rank = cardParts[0];
            String suit = cardParts[1].replace(".", ""); // 무늬에서 . 제거함
            String imagePath = "src/main/resources/cards/" + rank + "_of_" + suit + ".png";

            // 이미지 크기 조정
            ImageIcon cardImage = new ImageIcon(imagePath);
            Image img = cardImage.getImage();
            Image scaledImgImage = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            cardImage = new ImageIcon(scaledImgImage);

            JLabel cardLabel = new JLabel(cardImage);
            panel.add(cardLabel); // 카드를 패널에 추가
            panel.revalidate(); // 패널 재조정
            panel.repaint(); // 패널 다시 그리기
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BlackjackClient::new);
    }
}
