package GUI;

import GameClass.One_Card;
import GameClass.CardImpl;
import GameClass.PlayerImpl;
import GameClass.ComputerPlayerImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class OneCardGUI extends JFrame {
    private One_Card game;
    private List<PlayerImpl> players;
    private JLabel topCardTextLabel;
    private JLabel topCardImageLabel;
    private JLabel accumulatedDrawLabel;
    private JTextArea gameLogArea;
    private JPanel playerPanel;
    private JPanel computerPanel;
    private JScrollPane playerScrollPane;
    private JScrollPane computerScrollPane;
    private static final String CARD_BACK_IMAGE_PATH = "/resources/images/cards/Card-back.png";
    private static final int CARD_WIDTH = 60;  // 카드 너비를 줄임
    private static final int CARD_HEIGHT = 90; // 카드 높이를 줄임

    // 생성자
    public OneCardGUI(PlayerImpl player, ComputerPlayerImpl computerPlayer) {
        this.players = new ArrayList<>();
        this.players.add(player);
        this.players.add(computerPlayer);
        this.game = new One_Card(players);

        setTitle("원카드 게임");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(80, 30, 30)); // 전체 배경색 설정

        game.startGame();
        initializeGUI();
        updateTopCard(); // 초기 탑 카드 정보 업데이트
    }

    // GUI 초기화
    private void initializeGUI() {
        // 플레이어 카드 패널 (하단에 위치)
        playerPanel = new JPanel(new GridLayout(1, 0, 10, 10));
        playerPanel.setBackground(new Color(80, 30, 30));
        playerScrollPane = new JScrollPane(playerPanel);
        playerScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        playerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        playerScrollPane.setPreferredSize(new Dimension(750, 120));

        // 컴퓨터 카드 패널 (상단에 위치)
        computerPanel = new JPanel(new GridLayout(1, 0, 10, 10));
        computerPanel.setBackground(new Color(80, 30, 30));
        computerScrollPane = new JScrollPane(computerPanel);
        computerScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        computerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        computerScrollPane.setPreferredSize(new Dimension(750, 120));

        // 카드 패널 컨테이너
        JPanel cardPanelsContainer = new JPanel(new GridLayout(2, 1, 10, 10));
        cardPanelsContainer.setBackground(new Color(80, 30, 30));
        cardPanelsContainer.add(createPlayerCardsPanel(computerScrollPane, "컴퓨터 카드"));
        cardPanelsContainer.add(createPlayerCardsPanel(playerScrollPane, "플레이어 카드"));

        // 상단 카드 영역 (탑 카드 정보)
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(80, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0); // 여백 설정

        // 탑 카드 텍스트 레이블
        topCardTextLabel = new JLabel("탑 카드: ", SwingConstants.CENTER);
        topCardTextLabel.setForeground(Color.ORANGE);
        topCardTextLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(topCardTextLabel, gbc);

        // 탑 카드 이미지 레이블
        gbc.gridy = 1;
        topCardImageLabel = new JLabel();
        topPanel.add(topCardImageLabel, gbc);

        // 누적 드로우 정보 레이블
        gbc.gridy = 2;
        accumulatedDrawLabel = new JLabel("누적 드로우: 0", SwingConstants.CENTER);
        accumulatedDrawLabel.setForeground(Color.YELLOW);
        accumulatedDrawLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(accumulatedDrawLabel, gbc);

        updatePlayerCardImages();

        // 하단 게임 로그 영역 및 버튼 영역
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(80, 30, 30));

        // 게임 로그를 표시하는 텍스트 영역
        gameLogArea = new JTextArea(8, 40);
        gameLogArea.setEditable(false);
        gameLogArea.setLineWrap(true);
        gameLogArea.setWrapStyleWord(true);
        gameLogArea.setBackground(new Color(40, 20, 20));
        gameLogArea.setForeground(Color.LIGHT_GRAY);
        gameLogArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane logScrollPane = new JScrollPane(gameLogArea);

        bottomPanel.add(logScrollPane, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(80, 30, 30));

        JButton drawCardButton = new JButton("카드 뽑기");
        drawCardButton.setPreferredSize(new Dimension(100, 30));
        drawCardButton.setBackground(new Color(100, 50, 50));
        drawCardButton.setForeground(Color.YELLOW);
        drawCardButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        drawCardButton.addActionListener(e -> {
            int drawCount = game.getAccumulatedDraw() > 0 ? game.getAccumulatedDraw() : 1;
            game.drawCard(game.getCurrentPlayer());
            appendLog(game.getCurrentPlayer().getName() + "가 " + drawCount + "장의 카드를 뽑았습니다.");
            updateTopCard(); // 뽑은 후 누적 드로우 수 업데이트
            updatePlayerCardImages();
            nextTurn();
        });

        buttonPanel.add(drawCardButton);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.CENTER);
        add(cardPanelsContainer, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createPlayerCardsPanel(JComponent component, String labelText) {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(new Color(80, 30, 30));
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.YELLOW);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        containerPanel.add(label, BorderLayout.NORTH);
        containerPanel.add(component, BorderLayout.CENTER);
        return containerPanel;
    }

    // 카드 이미지를 로드하는 함수
    private ImageIcon loadCardImage(CardImpl card) {
        String imagePath = "/resources/images/cards/" + card.getRank() + "_of_" + card.getSuit() + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image scaledImage = icon.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    // 카드 뒷면 이미지를 로드하는 함수
    private ImageIcon loadCardBackImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource(CARD_BACK_IMAGE_PATH));
        Image scaledImage = icon.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void updatePlayerCardImages() {
        playerPanel.removeAll();
        computerPanel.removeAll();

        // 플레이어의 카드 이미지를 패널에 추가
        for (CardImpl card : players.get(0).getHand()) {
            JLabel cardLabel = new JLabel(loadCardImage(card));
            cardLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (game.canPlayCard(card)) {
                        game.playCard(players.get(0), card);
                        appendLog(players.get(0).getName() + "가 " + card + " 카드를 냈습니다.");
                        updateTopCard(); // 탑 카드 정보 업데이트
                        updatePlayerCardImages();
                        nextTurn();
                    } else {
                        JOptionPane.showMessageDialog(OneCardGUI.this, "잘못된 카드 선택입니다!", "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            playerPanel.add(cardLabel);
        }

        // 컴퓨터의 카드 개수만큼 카드 뒷면 이미지를 추가
        int computerCardCount = players.get(1).getHand().size();
        for (int i = 0; i < computerCardCount; i++) {
            JLabel cardBackLabel = new JLabel(loadCardBackImage());
            computerPanel.add(cardBackLabel);
        }

        playerPanel.revalidate();
        computerPanel.revalidate();
        playerPanel.repaint();
        computerPanel.repaint();
    }

    // 차례를 다음 플레이어로 넘김
    private void nextTurn() {
        PlayerImpl winner = game.determineWinner();
        if (winner != null) {
            JOptionPane.showMessageDialog(this, winner.getName() + "가 승리하였습니다!", "게임 종료", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // 승자가 나왔으므로 프로그램을 종료
            return;
        }

        game.nextTurn();
        updatePlayerCardImages();

        // 컴퓨터의 차례일 경우 자동으로 턴 수행
        if (game.getCurrentPlayer() instanceof ComputerPlayerImpl) {
            ComputerPlayerImpl computerPlayer = (ComputerPlayerImpl) game.getCurrentPlayer();
            appendLog(computerPlayer.getName() + "의 턴입니다.");

            if (game.getAccumulatedDraw() > 0) {
                int drawCount = game.getAccumulatedDraw();  // 누적된 드로우 수를 저장한 후 드로우
                game.drawCard(computerPlayer);
                appendLog(computerPlayer.getName() + "가 " + drawCount + "장의 카드를 뽑았습니다.");
                updateTopCard();  // 컴퓨터가 카드를 뽑은 후 탑 카드 정보 업데이트
                updatePlayerCardImages();
            } else {
                CardImpl cardToPlay = computerPlayer.playCard(game.getTopCard());
                if (cardToPlay != null) {
                    game.playCard(computerPlayer, cardToPlay);
                    appendLog(computerPlayer.getName() + "가 " + cardToPlay + " 카드를 냈습니다.");
                    updateTopCard();  // 컴퓨터가 카드를 낸 후 탑 카드 정보 업데이트
                    updatePlayerCardImages();
                } else {
                    game.drawCard(computerPlayer);
                    appendLog(computerPlayer.getName() + "가 1장의 카드를 뽑았습니다.");
                }
            }
            nextTurn();  // 컴퓨터의 턴이 끝나면 다음 턴으로 이동
        } else {
            JOptionPane.showMessageDialog(this, "당신의 차례입니다!");
        }
    }

    // 로그를 추가하는 메소드
    private void appendLog(String message) {
        gameLogArea.append(message + "\n");
        gameLogArea.setCaretPosition(gameLogArea.getDocument().getLength());
    }

    // 탑 카드 정보 업데이트
    private void updateTopCard() {
        CardImpl topCard = game.getTopCard();
        topCardImageLabel.setIcon(loadCardImage(topCard));
        topCardTextLabel.setText("탑 카드: " + topCard);
        accumulatedDrawLabel.setText("누적 드로우: " + game.getAccumulatedDraw());
    }
}