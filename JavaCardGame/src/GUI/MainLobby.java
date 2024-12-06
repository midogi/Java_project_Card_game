package GUI;

import GameClass.PlayerImpl;
import GameClass.ComputerPlayerImpl;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainLobby extends JFrame {

    // 이미지 상대 경로 설정
    private static final String IMAGE_PATH = "/resources/images/Game_Title.jpg";
    private static final String RULES_FILE_PATH = "./src/GUI/Rules.txt"; // 규칙 파일 경로

    public MainLobby() {
        setTitle("Main Lobby");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(80, 30, 30)); // 배경색 설정

        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        loadBackgroundImage();
        addLobbyButtons();
        addUserInfo();
        addGameSelectionButtons();
    }

    private void loadBackgroundImage() {
        JLabel backgroundLabel = new JLabel(new ImageIcon(getClass().getResource(IMAGE_PATH)));
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
        blackjackButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "블랙잭 실행 준비 중!"));
        gameSelectionPanel.add(blackjackButton);

        // 인디언 포커 버튼
        JButton indianPokerButton = createButton("인디언 포커");
        indianPokerButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "인디언 포커 실행 준비 중!"));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainLobby::new);
    }
}
