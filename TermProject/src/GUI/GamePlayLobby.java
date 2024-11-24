package GUI;

import GameClass.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePlayLobby extends JFrame {
    public GamePlayLobby() {
        setTitle("Game Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLayout(new GridLayout(3, 1, 10, 10));

        // Blackjack 버튼
        JButton blackjackButton = new JButton("Blackjack");
        blackjackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Blackjack 실행 준비 중!");
            }
        });

        // IndianPoker 버튼
        JButton indianPokerButton = new JButton("IndianPoker");
        indianPokerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "IndianPoker 실행 준비 중!");
            }
        });

        // OneCard 버튼
        JButton oneCardButton = new JButton("OneCard");
        oneCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player player1 = new Player("Player 1");
                Player player2 = new Player("Player 2");
                new OneCardGUI(player1, player2); // OneCardGUI 실행
                dispose(); // GamePlayLobby 창 닫기
            }
        });

        add(blackjackButton);
        add(indianPokerButton);
        add(oneCardButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        new GamePlayLobby();
    }
}
