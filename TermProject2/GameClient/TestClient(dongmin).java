package GameClient;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import javax.swing.*;
import GameClass.makeIndianPokerGui;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TestClient extends JFrame {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private JTextArea chatArea;
    private JTextField chatInput;
    private String clientName;
    private CardLayout cardLayout; // 화면 전환을 위한 레이아웃
    private JPanel mainPanel; // CardLayout이 적용된 메인 패널

    public TestClient() throws IOException {
        renderUI(); // UI 생성
        initClient(); // 클라이언트 초기화
    }

    private void initClient() throws IOException {
        socket = new Socket("127.0.0.1", 30000);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        clientName = JOptionPane.showInputDialog(this, "사용자 이름을 입력하세요:");
        if (clientName == null || clientName.trim().isEmpty()) {
            clientName = "Unknown";
        }

        out.writeUTF(clientName); // 서버에 이름 전달
        startReceiver(); // 수신 스레드 시작
    }

    private void sendMessage(String message) {
        try {
            if (message.trim().isEmpty()) return;
            out.writeUTF(message); // 메시지 서버로 전송
            chatArea.append("[나] " + message + "\n");
            chatInput.setText(""); // 입력 창 비우기
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReceiver() {
        Thread receiver = new Thread(() -> {
            try {
                while (true) {
                    String msg = in.readUTF(); // 서버에서 메시지 수신
                    System.out.println("서버 메시지: " + msg);

                    if ("게임 시작".equals(msg)) {
                        SwingUtilities.invokeLater(() -> {
                            // 게임 화면을 한 번만 추가하도록 수정
                            makeIndianPokerGui indianGame = new makeIndianPokerGui();
                            // 생성된 인디언 포커 게임 화면을 mainPanel에 추가
                            mainPanel.add(indianGame, "인디언 포커");
                            // "인디언 포커" 화면으로 전환
                            cardLayout.show(mainPanel, "인디언 포커");
                        });
                    } else {
                        chatArea.append(msg + "\n"); // 일반 채팅 메시지 처리
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "서버 연결이 끊어졌습니다.", "연결 끊김", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
        receiver.start();
    }

    private void showGameScreen() {
        cardLayout.show(mainPanel, "게임 화면"); // "게임 화면"으로 전환
    }

    private void renderUI() {
        setTitle("클라이언트 채팅");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 채팅 화면
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatArea = new JTextArea(10, 40);
        chatArea.setEditable(false);
        JScrollPane chatScroll = new JScrollPane(chatArea);

        chatInput = new JTextField(30);
        JButton sendButton = new JButton("전송");

        chatInput.addActionListener((ActionEvent e) -> sendMessage(chatInput.getText()));
        sendButton.addActionListener((ActionEvent e) -> sendMessage(chatInput.getText()));

        JPanel gameButtonPanel = new JPanel();
        gameButtonPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton blackjackButton = new JButton("블랙잭");
        JButton indianPokerButton = new JButton("인디언 포커");
        JButton oneCardButton = new JButton("원카드");

        blackjackButton.addActionListener(e -> sendMessage("매칭 요청: 블랙잭"));
       
        indianPokerButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                // 인디언 포커 GUI 화면을 생성
                makeIndianPokerGui indianGame = new makeIndianPokerGui();
                mainPanel.add(indianGame, "인디언 포커");

                // "인디언 포커" 화면으로 전환
                cardLayout.show(mainPanel, "인디언 포커");
                chatArea.append("게임 시작!\n");
                
                // Betting 버튼 동작 설정
                indianGame.getResultButton().addActionListener(ev -> handleBetting(indianGame));
                //Give 버튼 동작 설정
                indianGame.getFoldButton().addActionListener(event -> {
                    // 1. 내 칩 1개 차감하고, AI 칩 1개 증가
                    indianGame.meLoseChip(1);
                    indianGame.aiWinnerChip(1);

                    // 2. 내 카드 공개 (카드 이미지 변경)
                    int playerCardValue = new Random().nextInt(10) + 1; // 내 카드 값
                    indianGame.showCardImage(playerCardValue);

                    // 칩 정보 업데이트
                    indianGame.updateChips(indianGame.getMyChips(), indianGame.getAiChips());
                    indianGame.updateChat("포기하셨습니다. 칩 1개를 잃습니다!\n");
                });
                
                indianGame.getContinueButton().addActionListener(event ->{
                	
                	//1. ai카드 다시 초기화
                	int aiCardValue = new Random().nextInt(10)+1;
                	indianGame.aiShowCardImage(aiCardValue);
                	
                	//2. 내 카드 초기화
                	indianGame.cardReset();
                	
                });
                
                
            });
        });
        
        oneCardButton.addActionListener(e -> sendMessage("매칭 요청: 원카드"));

        gameButtonPanel.add(blackjackButton);
        gameButtonPanel.add(indianPokerButton);
        gameButtonPanel.add(oneCardButton);

        JPanel inputPanel = new JPanel();
        inputPanel.add(chatInput);
        inputPanel.add(sendButton);

        chatPanel.add(chatScroll, BorderLayout.NORTH);
        chatPanel.add(gameButtonPanel, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        mainPanel.add(chatPanel, "채팅 화면");

        // 게임 화면
        JPanel gamePanel = new JPanel(new BorderLayout());
        JLabel gameLabel = new JLabel("Game Start!", SwingConstants.CENTER);
        gameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JButton backButton = new JButton("돌아가기");

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "채팅 화면"));

        gamePanel.add(gameLabel, BorderLayout.CENTER);
        gamePanel.add(backButton, BorderLayout.SOUTH);

        mainPanel.add(gamePanel, "게임 화면");

        getContentPane().add(mainPanel);
        cardLayout.show(mainPanel, "채팅 화면");

        setVisible(true);
    }

    
    private void handleBetting(makeIndianPokerGui indianGame) {
        new Thread(() -> {
            try {
                // AI 배팅 로직
                int aiBetAmount = new Random().nextInt(5) + 1;  // AI가 1~5개 칩을 배팅
                indianGame.updateChat("AI는 " + aiBetAmount + "개 베팅했습니다.");

                // 3초 카운트다운
                for (int i = 3; i > 0; i--) {
                    indianGame.updateChat(i + "초 남았습니다!");
                    Thread.sleep(1000); // 1초 대기
                }

                // 공개
                indianGame.updateChat("공개!");

                // 내가 선택한 카드 값 생성
                int meChoice = new Random().nextInt(10) + 1;
                indianGame.updateChat("내가 선택한 카드: " + meChoice);

                // 내 카드 이미지
                indianGame.showCardImage(meChoice);

                // AI가 선택한 카드 값 생성
                int aiChoice = new Random().nextInt(10) + 1;

                // 카드 비교 후 승패 결정
                if (meChoice > aiChoice) {
                    indianGame.updateChat("내가 승리했습니다!");
                    indianGame.meWinnerChip(aiBetAmount);  // 상대의 배팅 금액만큼 내 칩에 추가
                    indianGame.aiLoseChip(aiBetAmount);   // AI는 배팅한 칩만큼 잃음
                } else if (meChoice < aiChoice) {
                    indianGame.updateChat("AI가 승리했습니다!");
                    indianGame.aiWinnerChip(indianGame.getBetAmount());  // 내가 배팅한 칩만큼 AI 칩에 추가
                    indianGame.meLoseChip(indianGame.getBetAmount());   // 나는 내가 배팅한 칩만큼 잃음
                } else {
                    // 무승부 처리
                    indianGame.updateChat("무승부입니다! 베팅 금액이 돌려집니다.");
                    indianGame.meWinnerChip(indianGame.getBetAmount());  // 내가 배팅한 금액 돌려받음
                    indianGame.aiWinnerChip(aiBetAmount);  // AI가 배팅한 금액 돌려받음
                }

                // 칩 정보 업데이트
                indianGame.updateChips(indianGame.getMyChips(), indianGame.getAiChips());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start(); // 새로운 스레드 시작
    }




    public static void main(String[] args) {
        try {
            new TestClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
