package GameClass;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class makeBlackJackGUI extends JPanel {
    private JLabel playerCardLabel;   // 플레이어 카드 레이블
    private JPanel playerCardPanel;    // 플레이어 카드 이미지를 담을 패널
    private JLabel dealerCardLabel;    // 딜러 카드 레이블
    private JPanel dealerCardPanel;     // 딜러 카드 이미지를 담을 패널
    private JSpinner betAmountSpinner;  // 베팅 금액 조정
    private JLabel playerChipsAmount;  // 플레이어 칩 개수
    private JButton hitButton;          // 히트 버튼
    private JButton standButton;        // 스탠드 버튼
    private JButton betButton;          // 베팅 버튼
    private JTextArea chatArea;         // 채팅 출력 영역
    private JTextField chatInputField;  // 채팅 입력 필드
    private JButton sendButton;          // 채팅 전송 버튼
    private int playerChips = 100;      // 플레이어 기본 칩 수
    private int betAmount = 0;          // 베팅 금액
    private List<String> playerCards;    // 플레이어 카드 목록
    private List<String> dealerCards;    // 딜러 카드 목록
    private Random random;

    public makeBlackJackGUI() {
        random = new Random();
        playerCards = new ArrayList<>();
        dealerCards = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(new Color(80, 30, 30)); // 전체 배경색 설정

        // 카드 패널 설정
        JPanel cardPanel = new JPanel(new GridLayout(1, 2));
        cardPanel.setBackground(new Color(80, 30, 30));

        // 플레이어 카드 레이블 및 패널 설정
        playerCardLabel = new JLabel("플레이어 카드", SwingConstants.CENTER);
        playerCardLabel.setForeground(Color.YELLOW);
        playerCardLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        playerCardPanel = new JPanel(new FlowLayout());
        playerCardPanel.setBackground(new Color(80, 30, 30));
        playerCardPanel.add(playerCardLabel); // 플레이어 카드 레이블 추가

        // 딜러 카드 레이블 및 패널 설정
        dealerCardLabel = new JLabel("딜러 카드", SwingConstants.CENTER);
        dealerCardLabel.setForeground(Color.YELLOW);
        dealerCardLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        dealerCardPanel = new JPanel(new FlowLayout());
        dealerCardPanel.setBackground(new Color(80, 30, 30));
        dealerCardPanel.add(dealerCardLabel); // 딜러 카드 레이블 추가

        // 카드 패널에 카드 추가
        cardPanel.add(dealerCardPanel);
        cardPanel.add(playerCardPanel); // 플레이어 카드 패널 추가

        // 하단 베팅 및 조작 버튼
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(new Color(80, 30, 30));
        JLabel bettingLabel = new JLabel("BETTING", SwingConstants.CENTER);
        bettingLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        bettingLabel.setForeground(Color.ORANGE);

        JPanel bettingControlPanel = new JPanel(new FlowLayout());
        bettingControlPanel.setBackground(new Color(80, 30, 30));

        betAmountSpinner = new JSpinner(new SpinnerNumberModel(0, 0, playerChips, 1));
        betAmountSpinner.setPreferredSize(new Dimension(70, 30));

        betButton = new JButton("Bet");
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");

        // 플레이어 칩 수 표시
        playerChipsAmount = new JLabel("보유 칩: " + playerChips, SwingConstants.CENTER);
        playerChipsAmount.setForeground(Color.YELLOW);
        playerChipsAmount.setFont(new Font("SansSerif", Font.BOLD, 16));

        // 버튼 클릭 이벤트 설정
        betButton.addActionListener(e -> {
            betAmount = (Integer) betAmountSpinner.getValue();
            if (betAmount > playerChips) {
                updateChat("베팅 금액이 보유 칩을 초과합니다.");
            } else {
                playerCards.clear(); // 이전 카드 초기화
                dealerCards.clear(); // 이전 카드 초기화
                initialCardDistribution(); // 카드 배분
                updateChat("베팅이 설정되었습니다: " + betAmount + "칩");
            }
        });

        hitButton.addActionListener(e -> {
            playerCards.add(drawCard()); // 새로운 카드 추가
            updateCardDisplay(); // 카드 표시 업데이트

            int playerScore = calculateScore(playerCards);
            updateChat("당신의 점수: " + playerScore); // 점수 출력
            if (playerScore > 21) {
                updateChat("버스트! 당신이 졌습니다.");
            }
        });

        standButton.addActionListener(e -> {
            if (dealerCards.isEmpty()) {
                System.err.println("딜러 카드가 없습니다.");
                return;
            }
            for (int i = 0; i < dealerCards.size(); i++) {
                JLabel cardImage = new JLabel(loadImage("src/main/resources/cards/" + dealerCards.get(i) + ".png", 100, 150));
                dealerCardPanel.add(cardImage); // 앞면 카드 추가
            }
            //updateDealerCardDisplay(); // 딜러 카드 표시 업데이트
            determineWinner();
        });

        bettingControlPanel.add(betAmountSpinner);
        bettingControlPanel.add(betButton);
        bettingControlPanel.add(hitButton);
        bettingControlPanel.add(standButton);

        controlPanel.add(bettingLabel, BorderLayout.NORTH);
        controlPanel.add(bettingControlPanel, BorderLayout.CENTER);
        controlPanel.add(playerChipsAmount, BorderLayout.SOUTH); // 플레이어 칩 수 추가

        // 하단 채팅창 패널
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(new Color(80, 30, 30));

        chatArea = new JTextArea(10, 30);
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(60, 30, 30));
        chatArea.setForeground(Color.WHITE);
        chatArea.setFont(new Font("돋움", Font.PLAIN, 12));

        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        chatInputField = new JTextField();
        sendButton = new JButton("Send");

        sendButton.addActionListener(e -> {
            String message = chatInputField.getText().trim();
            if (!message.isEmpty()) {
                chatArea.append("Me: " + message + "\n");
                chatInputField.setText("");
            }
        });

        chatInputField.addActionListener(e -> sendButton.doClick());

        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(chatInputField, BorderLayout.SOUTH);

        // 레이아웃 구성
        add(cardPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(chatPanel, BorderLayout.SOUTH);

        // 게임 시작 시 카드 배분
        initialCardDistribution();
    }

    // 게임 시작 시 카드 배분 메서드
    private void initialCardDistribution() {
        playerCards.add(drawCard());
        dealerCards.add(drawCard());
        playerCards.add(drawCard());
        dealerCards.add(drawCard());
        updateCardDisplay(); // 카드 표시 업데이트
    }

    private String drawCard() {
        // A를 포함한 카드 랜덤하게 생성
        int cardNumber = random.nextInt(13) + 1; // 1부터 13까지
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String suit = suits[random.nextInt(suits.length)];

        // A는 "A_of_Suit" 형식으로 반환
        if (cardNumber == 1) {
            return "A_of_" + suit; // A
        } else if (cardNumber > 10) {
            return (cardNumber == 11 ? "J_of_" : cardNumber == 12 ? "Q_of_" : "K_of_") + suit; // J, Q, K
        } else {
            return cardNumber + "_of_" + suit; // 2~10
        }
    }

    public ImageIcon loadImage(String path, int width, int height) {
        String absolutePath = Paths.get(path).toAbsolutePath().toString();
        File file = new File(absolutePath);
        if (!file.exists()) {
            System.err.println("이미지 경로를 찾을 수 없습니다: " + absolutePath);
        }
        ImageIcon icon = new ImageIcon(absolutePath);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    public void updateChat(String message) {
        chatArea.append(message + "\n");
    }

    private int calculateScore(List<String> cards) {
        int score = 0;
        int aceCount = 0; // A의 개수를 세기 위한 변수

        for (String card : cards) {
            String[] parts = card.split("_");
            String cardValue = parts[0]; // 카드의 값 부분 (예: "A", "2", ..., "10", "J", "Q", "K")

            // 카드 값 결정
            switch (cardValue) {
                case "A":
                    score += 11; // A는 일단 11로 계산
                    aceCount++;
                    break;
                case "J":
                case "Q":
                case "K":
                    score += 10; // J, Q, K는 10으로 간주
                    break;
                default:
                    // 숫자 카드 처리 (2~10)
                    try {
                        score += Integer.parseInt(cardValue); // 카드 값을 정수로 변환하여 점수에 추가
                    } catch (NumberFormatException e) {
                        System.err.println("잘못된 카드 형식: " + card);
                    }
            }
        }

        // 점수가 21을 초과하면 A의 값을 11에서 1로 조정
        while (score > 21 && aceCount > 0) {
            score -= 10; // A의 가치를 1로 조정
            aceCount--;
        }

        return score;
    }

    private void determineWinner() {
        int playerScore = calculateScore(playerCards);
        int dealerScore = calculateScore(dealerCards);

        // 점수 출력
        updateChat("플레이어 점수: " + playerScore);
        updateChat("딜러 점수: " + dealerScore);

        if (playerScore > 21) {
            updateChat("버스트! 당신이 졌습니다.");
        } else if (dealerScore > 21) {
            updateChat("딜러가 버스트! 당신의 승리입니다!");
            playerChips += betAmount; // 플레이어가 승리할 경우 칩 추가
        } else if (playerScore > dealerScore) {
            updateChat("당신의 승리! 칩을 추가합니다.");
            playerChips += betAmount; // 플레이어가 승리할 경우 칩 추가
        } else if (playerScore < dealerScore) {
            updateChat("딜러의 승리! 칩을 잃습니다.");
            playerChips -= betAmount; // 플레이어가 패배할 경우 칩 차감
        } else {
            updateChat("무승부입니다.");
        }

        // 플레이어 칩 수 업데이트
        playerChipsAmount.setText("보유 칩: " + playerChips);
    }

    private void updateCardDisplay() {
        playerCardPanel.removeAll(); // 기존 카드 이미지 제거

        // 플레이어 카드 이미지 표시
        for (String card : playerCards) {
            JLabel cardImage = new JLabel(loadImage("src/main/resources/cards/" + card + ".png", 100, 150));
            playerCardPanel.add(cardImage); // 패널에 카드 추가
        }

        playerCardPanel.revalidate(); // 레이아웃 갱신
        playerCardPanel.repaint(); // 패널 다시 그리기

        updateDealerCardDisplay(); // 딜러 카드 표시 업데이트
    }

    private void updateDealerCardDisplay() {
        dealerCardPanel.removeAll(); // 기존 카드 이미지 제거

        // 첫 번째 카드: 뒷면 이미지
        if (!dealerCards.isEmpty()) {
            JLabel cardBackImage = new JLabel(loadImage("src/main/resources/cards/card_back.png", 100, 150)); // 카드 뒷면 이미지
            dealerCardPanel.add(cardBackImage); // 뒷면 카드 추가
        }

        // 두 번째 카드 및 그 이후 카드: 앞면 이미지
        for (int i = 1; i < dealerCards.size(); i++) {
            JLabel cardImage = new JLabel(loadImage("src/main/resources/cards/" + dealerCards.get(i) + ".png", 100, 150));
            dealerCardPanel.add(cardImage); // 앞면 카드 추가
        }

        dealerCardPanel.revalidate(); // 레이아웃 갱신
        dealerCardPanel.repaint(); // 패널 다시 그리기
    }
}
