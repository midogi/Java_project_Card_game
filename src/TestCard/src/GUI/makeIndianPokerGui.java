package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.Random;

public class makeIndianPokerGui extends JPanel {
    private JLabel playerCard;  // ai카드
    private JLabel opponentCard;  // 내카드
    private JSpinner betAmountSpinner;  // betAmountSpinner를 클래스 필드로 선언
    private JLabel opponentName;  // 상대 이름
    private JLabel playerName;  // 플레이어 이름
    private JLabel opponentChipsLabel;  // 상대 칩 이미지
    private JLabel playerChipsLabel;  // 플레이어 칩 이미지
    private JLabel opponentChipsText;  // 상대 "보유한 칩" 텍스트
    private JLabel playerChipsText;  // 플레이어 "보유한 칩" 텍스트
    private JLabel opponentChipsAmount;  // 상대 칩 개수
    private JLabel playerChipsAmount;  // 플레이어 칩 개수
    private JButton allInButton;  // All In 버튼
    private JButton foldButton;  // Fold 버튼
    private JButton betButton;  // Bet 버튼
    private JTextArea chatArea;  // 채팅 출력 영역
    private JTextField chatInputField;  // 채팅 입력 필드
    private JButton sendButton;  // Send 버튼
    private JButton resultButton; //결과 확인 버튼
    private int myChips=20; //내 기본 칩 갯수
    private int aiChips=20; // ai 기본 칩 갯수
    private int betAmount = 0; //베팅 금액 저장하는 변수
    private JButton continueButton;  // "계속하기" 버튼 추가
    public int aiCardNum; //ai 카드 번호
    

    public makeIndianPokerGui() {
        setLayout(new BorderLayout());
        setBackground(new Color(80, 30, 30)); // 전체 배경색 설정
       
        // 상단 카드와 사용자 이름
        JPanel cardPanel = new JPanel(new GridLayout(1, 2));
        cardPanel.setBackground(new Color(80, 30, 30)); // 상단 패널 배경색
        
        aiCardNum = new Random().nextInt(10) + 1; 
      
        
        // 내 카드 패널 
        JPanel opponentCardPanel = new JPanel(new BorderLayout());
        opponentCardPanel.setBackground(new Color(80, 30, 30)); 
        opponentCard = new JLabel(loadImage("images/Indian_Poker_Verso.png", 150, 130)); 
        opponentName = new JLabel("Me", SwingConstants.CENTER);
        opponentName.setForeground(Color.YELLOW);
        opponentName.setFont(new Font("Arial", Font.BOLD, 16));
        
        
        // 상대 카드 아래 칩 정보 추가
        JPanel opponentInfoPanel = new JPanel(new BorderLayout());
        opponentInfoPanel.setBackground(new Color(80, 30, 30)); 

        // 칩 이미지
        opponentChipsLabel = new JLabel(loadImage("images/Indian_Poker_Chip.png", 90, 70));

        // "보유한 칩" 텍스트
        opponentChipsText = new JLabel("보유한 칩", SwingConstants.CENTER);
        opponentChipsText.setForeground(Color.WHITE);

        // 칩 개수 표시 (x40)
        opponentChipsAmount = new JLabel("x"+myChips, SwingConstants.CENTER);
        opponentChipsAmount.setForeground(Color.WHITE);  // 숫자 색상 설정
        opponentChipsAmount.setFont(new Font("Arial", Font.BOLD, 14)); // 숫자 크기 설정

        // 칩 이미지와 숫자 배치
        JPanel chipsPanel = new JPanel(new BorderLayout());
        chipsPanel.setBackground(new Color(80, 30, 30));
        chipsPanel.add(opponentChipsLabel, BorderLayout.CENTER);
        chipsPanel.add(opponentChipsAmount, BorderLayout.SOUTH); // x40 숫자 추가

        // 모든 요소 배치
        opponentInfoPanel.add(opponentChipsText, BorderLayout.NORTH);
        opponentInfoPanel.add(chipsPanel, BorderLayout.CENTER); // 칩과 숫자를 배치

        opponentCardPanel.add(opponentCard, BorderLayout.CENTER);
        opponentCardPanel.add(opponentName, BorderLayout.NORTH);
        opponentCardPanel.add(opponentInfoPanel, BorderLayout.SOUTH);

        // 상대 플레이어 카드 패널 (적 카드)
        JPanel playerCardPanel = new JPanel(new BorderLayout());
        playerCardPanel.setBackground(new Color(80, 30, 30)); 
        playerCard = new JLabel(loadImage("images/Indian_Poker_"+aiCardNum+".png", 150, 130)); 
        playerName = new JLabel("Enemy", SwingConstants.CENTER);
        playerName.setForeground(Color.YELLOW);
        playerName.setFont(new Font("Arial", Font.BOLD, 16));

        // 적 카드 아래 칩 정보 추가
        JPanel playerInfoPanel = new JPanel(new BorderLayout());
        playerInfoPanel.setBackground(new Color(80, 30, 30));

        // 칩 이미지
        playerChipsLabel = new JLabel(loadImage("images/Indian_Poker_Chip.png", 90, 70));

        // "보유한 칩" 텍스트
        playerChipsText = new JLabel("보유한 칩", SwingConstants.CENTER);
        playerChipsText.setForeground(Color.WHITE);

        // 칩 개수 표시 (x40)
        playerChipsAmount = new JLabel("x"+aiChips, SwingConstants.CENTER);
        playerChipsAmount.setForeground(Color.WHITE);  // 숫자 색상 설정
        playerChipsAmount.setFont(new Font("Arial", Font.BOLD, 14)); // 숫자 크기 설정

        // 칩 이미지와 숫자 배치
        JPanel playerChipsPanel = new JPanel(new BorderLayout());
        playerChipsPanel.setBackground(new Color(80, 30, 30));
        playerChipsPanel.add(playerChipsLabel, BorderLayout.CENTER);
        playerChipsPanel.add(playerChipsAmount, BorderLayout.SOUTH); // x40 숫자 추가

        // 모든 요소 배치
        playerInfoPanel.add(playerChipsText, BorderLayout.NORTH);
        playerInfoPanel.add(playerChipsPanel, BorderLayout.CENTER); // 칩과 숫자 배치

        playerCardPanel.add(playerCard, BorderLayout.CENTER);
        playerCardPanel.add(playerName, BorderLayout.NORTH);
        playerCardPanel.add(playerInfoPanel, BorderLayout.SOUTH);

        // 카드 패널에 두 플레이어 카드 추가
        cardPanel.add(playerCardPanel);
        cardPanel.add(opponentCardPanel);

        // 하단 베팅 컨트롤
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(new Color(80, 30, 30));
        JLabel bettingLabel = new JLabel("BETTING", SwingConstants.CENTER);
        bettingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bettingLabel.setForeground(Color.ORANGE);

        JPanel bettingControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bettingControlPanel.setBackground(new Color(80, 30, 30));

        // 배팅 금액 조정용 JSpinner (위아래 화살표)
        SpinnerNumberModel betAmountModel = new SpinnerNumberModel(0, 0, 1000, 1);  // 최소 0, 최대 1000, 1씩 증가
        betAmountSpinner = new JSpinner(betAmountModel); // betAmountSpinner는 클래스 필드로 선언됨
        betAmountSpinner.setEditor(new JSpinner.NumberEditor(betAmountSpinner, "#"));  // 숫자 포맷 설정
        betAmountSpinner.setPreferredSize(new Dimension(70, 30));  // 크기만 설정

        // 배팅 버튼 크기 설정 (변경하지 않음)
        allInButton = new JButton("All in");
        allInButton.setPreferredSize(new Dimension(100, 30));  // 버튼 크기

        foldButton = new JButton("Give Up");
        foldButton.setPreferredSize(new Dimension(100, 30));

        betButton = new JButton("Betting");
        betButton.setPreferredSize(new Dimension(100, 30));
        
        // Betting 버튼 클릭 이벤트
        betButton.addActionListener(e -> {
        	 betAmount = (Integer) betAmountSpinner.getValue(); // JSpinner에서 값을 가져옴
        	 updateChat("배팅 금액: " + betAmount + "칩으로 설정되었습니다.");
        });
        
        // All in 버튼 클릭 이벤트
        allInButton.addActionListener(e -> {
           
            betAmountSpinner.setValue(myChips);
        });

        resultButton = new JButton("결과 확인");
        resultButton.setPreferredSize(new Dimension(120, 30));
        
       
        // 결과 확인 버튼 클릭 이벤트
        resultButton.addActionListener(e -> {   	 
            chatArea.append("결과를 확인합니다\n");
        });
        
        // "계속하기" 버튼 추가
        continueButton = new JButton("계속하기");
        continueButton.setPreferredSize(new Dimension(120, 30));

      
        // 패널에 배팅 금액과 버튼들 추가
        bettingControlPanel.add(allInButton);
        bettingControlPanel.add(betAmountSpinner);
        bettingControlPanel.add(foldButton);
        bettingControlPanel.add(betButton);
        bettingControlPanel.add(resultButton);  // 결과 확인 버튼 추가
        bettingControlPanel.add(continueButton);  // "계속하기" 버튼 추가
        
        // 베팅 컨트롤을 포함한 패널 추가
        controlPanel.add(bettingLabel, BorderLayout.NORTH);
        controlPanel.add(bettingControlPanel, BorderLayout.CENTER);

        // 레이아웃 구성
        add(cardPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        
        // 하단 채팅창 패널
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(new Color(80, 30, 30));  // 배경색 설정

        // 채팅 출력 영역 (JTextArea)
        
        chatArea = new JTextArea(10, 30);  // 10행 30열
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(60, 30, 30));  // 배경색 설정
        chatArea.setForeground(Color.WHITE);
        chatArea.setFont(new Font("돋움", Font.PLAIN, 12));
        

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        
        // 채팅 입력 영역 (텍스트 필드와 버튼)
        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInputPanel.setBackground(new Color(80, 30, 30));

        chatInputField = new JTextField();
        chatInputField.setPreferredSize(new Dimension(0, 30));

        sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(80, 30));

        // Send 버튼 클릭 시 이벤트
        sendButton.addActionListener(e -> {
            String message = chatInputField.getText().trim();
            if (!message.isEmpty()) {
                chatArea.append("Me: " + message + "\n");
                chatInputField.setText("");
            }
        });

        // 엔터 키로도 전송되도록 설정
        chatInputField.addActionListener(e -> sendButton.doClick());

        // 채팅 입력 패널에 텍스트 필드와 버튼 추가
        chatInputPanel.add(chatInputField, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST);

        // 채팅창 패널에 출력 영역과 입력 패널 추가
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);

        // 기존 레이아웃에 채팅창 추가
        add(chatPanel, BorderLayout.SOUTH);
    }

    public void updatePlayerCard(String cardName) {
        opponentCard.setIcon(loadImage("images/Indian_Poker_" + cardName + ".png", 150, 130));
    }
    
    public void showCardImage(int number) {
        String aiCardImagePath = "images/Indian_Poker_" + number + ".png";
        opponentCard.setIcon(loadImage(aiCardImagePath, 150, 130));
    }
    
    
    public void aiShowCardImage(int number) {
        String aiCardImagePath = "images/Indian_Poker_" + number + ".png";
        playerCard.setIcon(loadImage(aiCardImagePath, 150, 130));
    }
    
    
    public JSpinner getBetAmountSpinner() {
        return betAmountSpinner;
    }
    
    public void cardReset() {
        String aiCardImagePath = ("images/Indian_Poker_Verso.png");
    	opponentCard.setIcon(loadImage(aiCardImagePath,150,130));
    }
    
    public JLabel getplayerCard() {
        return playerCard;  // playerCard JLabel 반환
    }
    
    public JButton getBettingButton() {
    	return betButton;
    }
    
    public JButton getResultButton() {
    	return resultButton;
    }
    
    public JButton getFoldButton() {
    	return foldButton;
    }
    
    public int getAiCardNum() {
    	return aiCardNum;
    }
    
 // 내가 이긴 경우 - 상대가 배팅한 금액만큼 내 칩에 추가
    public void meWinnerChip(int winChips) {
        myChips += winChips;  // 내가 이기면 상대가 배팅한 금액만큼 내 칩에 추가
        updateChips(myChips, aiChips);  // UI 업데이트
    }

    // AI가 이긴 경우 - 내가 배팅한 금액만큼 AI 칩에 추가
    public void aiWinnerChip(int winChips) {
        aiChips += winChips;  // AI가 이기면 내가 배팅한 금액만큼 AI 칩에 추가
     
    }

    // 내가 지면 - 내가 배팅한 금액만큼 내 칩에서 차감
    public void meLoseChip(int loseChips) {
        myChips -= loseChips;  // 내가 지면 내가 배팅한 금액만큼 내 칩에서 차감
    }

    // AI가 지면 - AI가 배팅한 금액만큼 AI 칩에서 차감
    public void aiLoseChip(int loseChips) {
        aiChips -= loseChips;  // AI가 지면 AI가 배팅한 금액만큼 AI 칩에서 차감
       
    }
    public int getBetAmount() {
    	return betAmount;
    }
    
    public int getMyChips() {
    	return myChips;
    }
    
    public int getAiChips() {
    	return aiChips;
    }
    
    
    public void settingAiCardNum(int num) {
    	this.aiCardNum=num;
    }
    
    public JButton getContinueButton() {
    	return continueButton;
    }
    
    public boolean checkAiChip() {
        return this.aiChips <= 0; // AI 칩이 0 이하일 때 종료
    }

    public boolean checkPlayerChip() {
        return this.myChips <= 0; // 플레이어 칩이 0 이하일 때 종료
    }
    
    
    public void updateChips(int playerChips, int aiChips) {
        playerChipsAmount.setText("x" + aiChips);  
        this.myChips = playerChips;  
        
        opponentChipsAmount.setText("x" + playerChips);  
        this.aiChips = aiChips;  
    }
    
    public boolean checkGameOver() {  // 'boolean'으로 수정
        if (myChips == 0 || aiChips == 0) {
            int choice = JOptionPane.showConfirmDialog(null, "게임이 종료되었습니다. 다시 시작하시겠습니까?", "게임 종료", JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
                return true;  // 반환값 'true'
            } else {
                System.exit(0);
                return false;  // 반환값 'false'
            }
        }
        return false;  // 기본 반환값 추가
    }
    
   
    
    public void resetGame() {
    	myChips=20;
    	aiChips=20;
    	updateChips(myChips,aiChips);
    	
    	//카드 초기화
    	cardReset();
    	
    	//배팅 금액 초기화
    	betAmount=0;
    	betAmountSpinner.setValue(betAmount);
    	
    	//채팅창 초기화
    	chatArea.setText("");
    	
    	//게임 상태 체크
    	checkGameOver();
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Indian Poker GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new makeIndianPokerGui());
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }
}
