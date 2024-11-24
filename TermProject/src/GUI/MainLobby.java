package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainLobby extends JFrame {

    // 이미지 절대 경로 설정
    private static final String IMAGE_PATH = "D:\\ydh\\CARDGAME\\Java_project_Card_game\\TermProject\\src\\resources\\images\\Game_Title.jpg";

    public MainLobby() {
        setTitle("Main Lobby");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // 이미지 로드
        JLabel backgroundLabel = new JLabel(new ImageIcon(IMAGE_PATH));
        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // 배경 투명
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 20));

        JButton playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GamePlayLobby(); // 게임 로비 화면으로 전환
                dispose(); // 현재 창 닫기
            }
        });

        JButton leaderboardButton = new JButton("LeaderBoards");
        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "리더보드 화면은 구현 예정입니다.");
            }
        });

        buttonPanel.add(playButton);
        buttonPanel.add(leaderboardButton);

        // 버튼 패널을 하단에 추가
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainLobby();
    }
}
