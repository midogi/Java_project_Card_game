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
    private JPanel cardPanel; //카드 이미지를 표시할 패널

    public BlackjackClient() {
        JFrame frame = new JFrame("블랙잭 게임");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        //카드 패널 추가
        cardPanel = new JPanel();
        cardPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        frame.add(cardPanel, BorderLayout.EAST);

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
                    if(message.startsWith("당신이 뽑은 카드:")){ //플레이어의 카드정보를 받아옴
                        String [] parts = message.split(": ");
                        if(parts.length > 1){
                            String cardInfo = parts[1];
                            displayCardImage(cardInfo);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayCardImage(String cardInfo) {
        //카드 정보에서 랭크와 무늬를 분리
        String[] cardParts = cardInfo.split(" of ");
        if(cardParts.length == 2){
            String rank = cardParts[0];
            String suit = cardParts[1].replace(".", ""); //무늬에서 . 제거함
            String imagePath = "src/main/resources/cards/"+rank+"_of_"+suit+".png";

            //이미지 크기 조정. 처음에 실행해 봤는데 카드가 너무 크게 나와서 글씨를 가림
            ImageIcon cardImage = new ImageIcon(imagePath); //ImageIcon은 java swing의 클래스임
            Image img = cardImage.getImage();
            Image scaledImgImage = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            cardImage = new ImageIcon(scaledImgImage);

            JLabel cardLabel = new JLabel(cardImage);
            cardPanel.add(cardLabel); //카드를 패널에 추가
            cardPanel.revalidate(); //패널 재조정
            //카드 이미지를 추가할 때, 기존의 카드 이미지를 포함한 레이아웃이 새 카드 이미지에 맞게 조정해줌
            cardPanel.repaint(); //패널 다시 그리기
            //카드 이미지 추가 후, 패널의 내용이 변경되었으므로 변경된 내용을 사용자에게 보여주기 위해
            //Swing 컴포넌트를 화면에 다시 그림
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BlackjackClient::new);
    }
}
