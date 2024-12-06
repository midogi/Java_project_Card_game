
package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class makeBlackJackGUI extends JFrame {
    private JPanel playerCardImagePanel;
    private JPanel dealerCardImagePanel;
    private JLabel playerCardLabel1;  // 플레이어 첫 번째 카드 이미지
    private JLabel playerCardLabel2;  // 플레이어 두 번째 카드 이미지
    private JLabel dealerCardLabel;    // 딜러 카드 이미지 (첫 번째 카드)
    private JLabel dealerHiddenCardLabel; // 딜러 숨겨진 카드 이미지 (뒷면)
    private JSpinner betAmountSpinner;  // 베팅 금액 조정
    private JLabel playerChipsAmount;  // 플레이어 칩 개수
    private JButton hitButton;  // 히트 버튼
    private JButton standButton;  // 스탠드 버튼
    private JButton betButton;  // 베팅 버튼
    private JTextArea chatArea;  // 채팅 출력 영역
    private JTextField chatInputField;  // 채팅 입력 필드
    private JButton sendButton;  // 채팅 전송 버튼
    private int playerChips = 100;  // 플레이어 기본 칩 수
    private int betAmount = 0;  // 베팅 금액
    private List<String> playerCards;  // 플레이어 카드 목록
    private List<String> dealerCards;  // 딜러 카드 목록
    private Random random;
    private MainLobby lobby;

/*
    public static void main(String[] args) {
        // JFrame 생성
        JFrame frame = new JFrame("Blackjack Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // makeBlackJackGUI 패널 추가
        makeBlackJackGUI gamePanel = new makeBlackJackGUI();
        frame.add(gamePanel);

        // 프레임 크기 설정
        frame.setSize(800, 600);
        frame.setVisible(true); // 프레임을 보이게 함
        frame.setLocationRelativeTo(null); // 화면 중앙에 위치
    }*/

    public makeBlackJackGUI(MainLobby lobby) {
        this.lobby = lobby;
        setTitle("블랙잭 게임");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(80, 30, 30)); // 전체 배경색 설정
        
        random = new Random();
        playerCards = new ArrayList<>();
        dealerCards = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(new Color(80, 30, 30)); // 전체 배경색 설정

        // 상단 카드 패널
        JPanel cardPanel = new JPanel(new GridLayout(1, 2));
        cardPanel.setBackground(new Color(80, 30, 30));

        // 플레이어 카드 텍스트
        JLabel playerCardTextLabel = new JLabel("플레이어 카드", SwingConstants.CENTER);
        playerCardTextLabel.setForeground(Color.YELLOW);
        playerCardTextLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        //플레이어 카드 이미지
        playerCardLabel1 = new JLabel();
        playerCardLabel2 = new JLabel();

        //플레이어 카드 이미지 패널
        playerCardImagePanel = new JPanel(new FlowLayout(  ));
        playerCardImagePanel.setBackground(new Color(80, 30, 30));
        playerCardImagePanel.add(playerCardLabel1);
        playerCardImagePanel.add(playerCardLabel2);

        //플레이어 카드 패널 구성
        JPanel playerCardPanel = new JPanel(new BorderLayout());
        playerCardPanel.setBackground(new Color(80, 30, 30));
        playerCardPanel.add(playerCardTextLabel, BorderLayout.NORTH); //텍스트 위쪽
        playerCardPanel.add(playerCardImagePanel, BorderLayout.CENTER); //이미지 아래쪽


        // 딜러 카드
        JLabel dealerCardTextLabel = new JLabel("딜러 카드", SwingConstants.CENTER);
        dealerCardTextLabel.setForeground(Color.YELLOW);
        dealerCardTextLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        //dealerHiddenCardLabel = new JLabel(loadImage("src/main/resources/cards/card_back.png", 100, 150), SwingConstants.CENTER); // 카드 뒷면 이미지

        //딜러 카드 이미지
        dealerCardLabel = new JLabel();
        dealerHiddenCardLabel = new JLabel();

        //딜러 카드 이미지 패널
        dealerCardImagePanel = new JPanel(new FlowLayout());
        dealerCardImagePanel.setBackground(new Color(80, 30, 30));
        dealerCardImagePanel.add(dealerHiddenCardLabel);
        dealerCardImagePanel.add(dealerCardLabel);

        // 딜러 카드 패널 구성
        JPanel dealerCardPanel = new JPanel(new BorderLayout());
        dealerCardPanel.setBackground(new Color(80, 30, 30));
        dealerCardPanel.add(dealerCardTextLabel, BorderLayout.NORTH); // 텍스트 위쪽
        dealerCardPanel.add(dealerCardImagePanel, BorderLayout.CENTER); // 이미지 아래쪽

        cardPanel.add(dealerCardPanel);
        cardPanel.add(playerCardPanel);

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

        // 버튼 클릭 이벤트
        betButton.addActionListener(e -> {
            betAmount = (Integer) betAmountSpinner.getValue();
            if (betAmount > playerChips) {
                updateChat("베팅 금액이 보유 칩을 초과합니다.");
            }
            if(betAmount > 15){
                updateChat("15개 이상을 베팅하셨으므로 게임을 묻고 다시 시작할 수 있습니다");

                int response = JOptionPane.showConfirmDialog(
                        null,
                        "15개 이상을 베팅하셨습니다. 게임을 묻고 다시 시작하시겠습니까?",
                        "게임 재시작",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if(response == JOptionPane.YES_OPTION){
                    playerCards.clear(); // 이전 카드 초기화
                    dealerCards.clear(); // 이전 카드 초기화
                    playerCards.add(drawCard());
                    dealerCards.add(drawCard());
                    playerCards.add(drawCard());
                    dealerCards.add(drawCard());
                    updateCardDisplay();
                    updateChat("베팅이 설정되었습니다: " + betAmount + "칩");
                }
                else if(response == JOptionPane.NO_OPTION){
                    updateChat("게임을 계속 진행합니다.");
                    updateChat("베팅이 설정되었습니다: " + betAmount + "칩");
                }
            }
            else{
                updateChat("베팅이 설정되었습니다: " + betAmount + "칩");
            }
        });

        hitButton.addActionListener(e -> {
            playerCards.add(drawCard()); //새로운 카드 추가
            betAmount += 10;
            updateChat("현재 베팅금액: " + betAmount);
            int dealerScore = dealerCalculateScore(dealerCards);
            if(dealerScore <17){
                dealerCards.add(drawCard());
            }
            updateCardDisplay(); //카드 표시 업데이트

            int playerScore = playerCalculateScore(playerCards);
            updateChat("당신의 점수: " + playerScore);//점수 출력
            if (playerScore > 21){
                dealerHiddenCardLabel.setIcon(loadImage("src/cards/" + dealerCards.get(0) + ".png", 100, 150));
                determineWinner();
                restartGame();
            }
            else if(dealerScore > 21){
                determineWinner();
                restartGame();
            }
        });

        standButton.addActionListener(e -> {
            if(dealerCards.isEmpty()){
                System.err.println("딜러 카드가 없습니다.");
                return;
            }
            dealerHiddenCardLabel.setIcon(loadImage("src/cards/" + dealerCards.get(0) + ".png", 100, 150));

            determineWinner();
            restartGame();
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
        chatArea.append(message+"\n");
    }

    public void restartGame(){
        int response = JOptionPane.showConfirmDialog(
                null,
                "게임이 종료되었습니다. 다시 시작하시겠습니까?",
                "게임 재시작",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if(response == JOptionPane.YES_OPTION){
            updateChat("게임을 다시 시작합니다");
            updateChat("카드를 다시 배분합니다");
            updateChat("현재 베팅금액 : " + betAmount);
            playerCards.clear();
            dealerCards.clear();
            playerCards.add(drawCard());
            dealerCards.add(drawCard());
            playerCards.add(drawCard());
            dealerCards.add(drawCard());
            updateCardDisplay();
        }
        else if(response == JOptionPane.NO_OPTION){
            updateChat("로비화면으로 돌아갑니다.");
            lobby.setVisible(true);
            dispose();

        }
    }
    private int playerCalculateScore(List<String> cards) {
        int score = 0;
        int aceCount = 0; // A의 개수를 세기 위한 변수

        for (String card : cards) {
            String[] parts = card.split("_");
            String cardValue = parts[0]; // 카드의 값 부분 (예: "A", "2", ..., "10", "J", "Q", "K")

            // 카드 값 결정
            switch (cardValue) {
                case "A":
                   // score += 11; // A는 일단 11로 계산
                    int aceValue = promptAceValue();
                    score += aceValue;
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

    private int dealerCalculateScore(List<String> cards) {
        int score = 0;
        int aceCount = 0;
        for (String card : cards) {
            String[] parts = card.split("_");
            String cardValue = parts[0];
            switch (cardValue) {
                case "A":
                    score += 11;
                    aceCount++;
                    break;
                case "J":
                case "Q":
                case "K":
                    score += 10;
                    break;
                default:
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
    private int promptAceValue() {
        Object[] options = {"1", "11"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "A카드를 뽑았습니다. 점수를 선택하세요",
                "Ace Value",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]
        );

        return choice == 0 ? 1 : 11;
    }

    private void determineWinner() {
        int playerScore = playerCalculateScore(playerCards);
        int dealerScore = dealerCalculateScore(dealerCards);

        // 점수 출력
        updateChat("플레이어 점수: " + playerScore);
        updateChat("딜러 점수: " + dealerScore);

        //승패가 결정된 경우는 betAmount = 0 으로 판돈을 초기화시킨다.
        if (playerScore > 21) {
            updateChat("버스트! 당신이 졌습니다.");
            playerChips -= betAmount;
            betAmount = 0;
        } else if (dealerScore > 21) {
            updateChat("딜러가 버스트! 당신의 승리입니다!");
            playerChips += betAmount; // 플레이어가 승리할 경우 칩 추가
            betAmount = 0;
        } else if (playerScore > dealerScore) {
            updateChat("당신의 승리! 칩을 추가합니다.");
            playerChips += betAmount; // 플레이어가 승리할 경우 칩 추가
            betAmount = 0;
        } else if (playerScore < dealerScore) {
            updateChat("딜러의 승리! 칩을 잃습니다.");
            playerChips -= betAmount; // 플레이어가 패배할 경우 칩 차감
            betAmount = 0;
        } else {
            updateChat("무승부입니다.");
            betAmount+=betAmount;
        }

        // 플레이어 칩 수 업데이트
        playerChipsAmount.setText("보유 칩: " + playerChips);
    }


    private void updateCardDisplay(){
        playerCardImagePanel.removeAll();
        dealerCardImagePanel.removeAll();
        for(int i=0;i<playerCards.size();i++){
            JLabel cardLabel = new JLabel();
            cardLabel.setIcon(loadImage("src/cards/" + playerCards.get(i) + ".png", 100, 150));
            playerCardImagePanel.add(cardLabel);
        }

        for(int i=0;i<dealerCards.size();i++){
            JLabel cardLabel2 = new JLabel();
            if(i==0) {
                dealerHiddenCardLabel.setIcon(loadImage("src/cards/card_back.png", 100, 150)); // 뒷면 카드
                dealerCardImagePanel.add(dealerHiddenCardLabel);
            }
            else{
                cardLabel2.setIcon(loadImage("src/cards/" + dealerCards.get(i) + ".png", 100, 150));
                dealerCardImagePanel.add(cardLabel2);
            }
        }
        playerCardImagePanel.revalidate();
        playerCardImagePanel.repaint();
        dealerCardImagePanel.revalidate();
        dealerCardImagePanel.repaint();
    }

}