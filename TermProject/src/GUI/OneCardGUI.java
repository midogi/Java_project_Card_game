package GUI;

import GameClass.Card;
import GameClass.Deck;
import GameClass.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class OneCardGUI extends JFrame {
    private Deck deck;
    private List<Player> players;
    private Card topCard;
    private int currentPlayerIndex;
    private JLabel topCardLabel;
    private JTextArea playerInfoArea;

    // 생성자
    public OneCardGUI(Player player1, Player player2) {
        this.players = new ArrayList<>();
        this.players.add(player1);
        this.players.add(player2);
        this.deck = new Deck();
        this.currentPlayerIndex = 0;

        setTitle("One Card Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        startGame();
        initializeGUI();
    }

    // GUI 초기화
    private void initializeGUI() {
        // 상단 카드 영역
        JPanel topPanel = new JPanel();
        topCardLabel = new JLabel("Top Card: " + topCard);
        topPanel.add(topCardLabel);

        // 하단 플레이어 정보 및 버튼 영역
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        playerInfoArea = new JTextArea(10, 20);
        playerInfoArea.setEditable(false);
        updatePlayerInfo();

        JButton drawCardButton = new JButton("Draw Card");
        drawCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawCard(getCurrentPlayer());
                updatePlayerInfo();
                nextTurn();
            }
        });

        bottomPanel.add(new JScrollPane(playerInfoArea));
        bottomPanel.add(drawCardButton);

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 게임 시작 함수
    public void startGame() {
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {
                player.receiveCard(deck.drawCard());
            }
        }
        topCard = deck.drawCard();
    }

    // 현재 차례인 플레이어 반환
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // 차례를 다음 플레이어로 넘김
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        JOptionPane.showMessageDialog(this, "Next player's turn!");
    }

    // 카드 뽑기
    public void drawCard(Player player) {
        if (deck.isEmpty()) {
            reshuffleDeck();
        }
        player.receiveCard(deck.drawCard());
    }

    // 덱 재생성
    private void reshuffleDeck() {
        JOptionPane.showMessageDialog(this, "Reshuffling deck...");
    }

    // 플레이어 정보 업데이트
    private void updatePlayerInfo() {
        StringBuilder info = new StringBuilder();
        for (Player player : players) {
            info.append(player.getName()).append("'s cards: ").append(player.getHand()).append("\n");
        }
        playerInfoArea.setText(info.toString());
        topCardLabel.setText("Top Card: " + topCard);
    }
}
