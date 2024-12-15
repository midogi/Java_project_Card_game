package GUI;

import GameClass.PlayerImpl;
import GameClass.makeIndianPokerGui;
import GameClass.ComputerPlayerImpl;
import java.io.*;
import java.net.Socket;
import java.util.Random;
import javax.swing.*;
import GameClass.makeIndianPokerGui;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class MainLobby extends JFrame {

    // 이미지 상대 경로 설정
    private static final String IMAGE_PATH = "/resources/images/Game_Title.jpg";
    private static final String RULES_FILE_PATH = "./src/GUI/Rules.txt"; // 규칙 파일 경로
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTextArea textArea;
    private static MainLobby instance;
    public MainLobby() {
        setTitle("Main Lobby");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(80, 30, 30)); // 배경색 설정

        // mainPanel과 cardLayout 초기화
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // mainPanel을 JFrame에 추가
        add(mainPanel, BorderLayout.CENTER);

        // 초기 UI 설정
        initializeUI();

        setVisible(true);
    }


    private void initializeUI() {
        // 기존의 UI 요소 추가 (배경, 버튼 등)
        loadBackgroundImage();
        addLobbyButtons();
        addUserInfo();
        addGameSelectionButtons();
    }


    private void loadBackgroundImage() {
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel, BorderLayout.NORTH);
      
        
    }
    

    private void addLobbyButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // 튜토리얼 버튼을 추가하고 액션 리스너 연결
        JButton tutorialButton = createButton("튜토리얼");
        tutorialButton.addActionListener(e -> showTutorialWindow()); // 새 창을 띄워서 규칙 보여줌

        buttonPanel.add(tutorialButton);
        add(buttonPanel, BorderLayout.NORTH);
    }

    private void addUserInfo() {
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBackground(new Color(80, 30, 30));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 사용자 정보 추가
        JLabel idLabel1 = new JLabel("2171250 나은민");
        idLabel1.setForeground(Color.YELLOW);
        idLabel1.setFont(new Font("SansSerif", Font.BOLD, 16));
        userInfoPanel.add(idLabel1);

        JLabel idLabel2 = new JLabel("2371105 윤동환");
        idLabel2.setForeground(Color.YELLOW);
        idLabel2.setFont(new Font("SansSerif", Font.BOLD, 16));
        userInfoPanel.add(idLabel2);

        JLabel idLabel3 = new JLabel("2271180 김동민");
        idLabel3.setForeground(Color.YELLOW);
        idLabel3.setFont(new Font("SansSerif", Font.BOLD, 16));
        userInfoPanel.add(idLabel3);

        add(userInfoPanel, BorderLayout.WEST);
    }

    private void addGameSelectionButtons() {
        JPanel gameSelectionPanel = new JPanel();
        gameSelectionPanel.setLayout(new GridLayout(3, 1, 10, 10));
        gameSelectionPanel.setBackground(new Color(80, 30, 30));
        gameSelectionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.ORANGE), "게임 선택", 0, 0, new Font("SansSerif", Font.BOLD, 18), Color.YELLOW));

        // 블랙잭 버튼
        JButton blackjackButton = createButton("블랙잭");
        blackjackButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                makeBlackJackGUI blackJackGame = new makeBlackJackGUI(this);
                blackJackGame.setVisible(true);
            });
        });
        gameSelectionPanel.add(blackjackButton);

        // 인디언 포커 버튼
        JButton indianPokerButton = createButton("인디언 포커");
        indianPokerButton.addActionListener(e -> {
            // "인디언 포커" 창을 새로 띄우기
            makeIndianPokerGui indianGame = new makeIndianPokerGui();
            // 인디언 포커 게임 창을 보이게 설정
            indianGame.setVisible(true);

            // Betting 버튼 동작 설정
            indianGame.getResultButton().addActionListener(ev -> handleBetting(indianGame, indianGame.getAiCardNum()));

            // Fold 버튼 동작 설정
            indianGame.getFoldButton().addActionListener(event -> {
                indianGame.meLoseChip(1);
                indianGame.aiWinnerChip(1);
                int playerCardValue = new Random().nextInt(10) + 1;
                indianGame.showCardImage(playerCardValue);
                indianGame.updateChips(indianGame.getMyChips(), indianGame.getAiChips());
                indianGame.updateChat("포기하셨습니다. 칩 1개를 잃습니다!\n");
            });

            // Continue 버튼 동작 설정
            indianGame.getContinueButton().addActionListener(event -> {
                indianGame.updateChat("새 라운드를 시작합니다.");
                int aiCardValue = new Random().nextInt(10) + 1;
                indianGame.settingAiCardNum(aiCardValue);
                indianGame.aiShowCardImage(aiCardValue);
                indianGame.cardReset();
                indianGame.updateChat("새로운 카드를 뽑았습니다.");

                if (indianGame.checkAiChip() || indianGame.checkPlayerChip()) {
                    int choice = JOptionPane.showConfirmDialog(null, "게임이 종료되었습니다. 다시 시작하시겠습니까?", "게임 종료", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        indianGame.resetGame();
                    } else {
                        System.exit(0);
                    }
                }
            });
        });


        gameSelectionPanel.add(indianPokerButton);

        // 원카드 버튼
        JButton oneCardButton = createButton("원카드");
        oneCardButton.addActionListener(e -> {
            PlayerImpl player1 = new PlayerImpl("플레이어");
            ComputerPlayerImpl computerPlayer = new ComputerPlayerImpl("컴퓨터");
            new OneCardGUI(player1, computerPlayer);
        });
        gameSelectionPanel.add(oneCardButton);

        add(gameSelectionPanel, BorderLayout.CENTER);
    }


    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        button.setBackground(new Color(100, 50, 50));
        button.setForeground(Color.YELLOW);
        button.setFocusPainted(false);
        return button;
    }

    // 튜토리얼 창 표시 메소드
    private void showTutorialWindow() {
        JFrame tutorialFrame = new JFrame("게임 규칙");
        tutorialFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tutorialFrame.setSize(500, 400);
        tutorialFrame.setLayout(new BorderLayout());
        tutorialFrame.getContentPane().setBackground(new Color(60, 30, 30));

        JTextArea tutorialArea = new JTextArea();
        tutorialArea.setEditable(false);
        tutorialArea.setLineWrap(true);
        tutorialArea.setWrapStyleWord(true);
        tutorialArea.setBackground(new Color(40, 20, 20));
        tutorialArea.setForeground(Color.WHITE);
        tutorialArea.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));

        // 규칙 파일 읽기
        try (BufferedReader br = new BufferedReader(new FileReader(RULES_FILE_PATH))) {
            String line;
            StringBuilder rulesText = new StringBuilder();
            while ((line = br.readLine()) != null) {
                rulesText.append(line).append("\n");
            }
            tutorialArea.setText(rulesText.toString());
        } catch (IOException e) {
            tutorialArea.setText("규칙 파일을 읽을 수 없습니다.");
        }

        JScrollPane scrollPane = new JScrollPane(tutorialArea);
        tutorialFrame.add(scrollPane, BorderLayout.CENTER);

        tutorialFrame.setVisible(true);
    }
    
    private void handleBetting(makeIndianPokerGui indianGame,int aiChoiceNum) {
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
                int aiChoice=aiChoiceNum;

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

    public static MainLobby getInstance(){
        if (instance == null){
            instance = new MainLobby();
        }
        return instance;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainLobby::new);
    }
}