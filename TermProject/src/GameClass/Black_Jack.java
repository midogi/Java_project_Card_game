package GameClass;

import GameClass.Deck;
import GameClass.Player;

public class Black_Jack {
	private Deck deck;
	private Player dealer;
	private Player player1;  
	
	//생성자
	public BlackjackGame(Player player1) {
		this.deck=new Deck();
		this.dealer=new Player("Dealer");
		this.player1=player1;
	}
	
	//게임 시작 함수
	public void startGame() {
		//플레이어에게  카드 2장 지급
		player1.receiveCard(deck.drawCard());
		player1.receiveCard(deck.drawCard());
		
		//딜러에게도 카드 2장 지급
		dealer.receiveCard(deck.drawCard());
		dealer.receiveCard(deck.drawCard());
	}
	//플레이어 턴 진행  함수 (ex: 카드 추가로 받을지 결정)
	public void playerTurn(Player player) {
		
	}
	//딜러의 턴 진행 함수
	public void dealerTurn() {
		
	}
	
	//승자 결정 함수
	public void determinWinner() {
		int dealerScore=dealer.getScore();
		
		if(player1.isBust()) {
			System.out.println(player1.getName()+" 패배했습니다.");
		}
		else if(dealer.isBust()||player1.getScore()>dealerScore) {
			System.out.println(player1.getName()+" 승리했습니다");
		}
		else if(player1.getScore()==dealerScore) {
			System.out.println(player1.getName()+" 무승부입니다");
		}
		else {
			System.out.println(player1.getName()+" 패배했습니다.");
		}
		
	}
	
	//게임 시작 함수
	public void playGame() {
	}
	
}
