package t1_1;

import t1_1.Card;
import t1_1.Deck;
import t1_1.Player;

public class One_Card {
	private Deck deck;
	private Player player1;
	private Player player2;
	private Card topCard; //현재 바닥에 놓여있는 카드
	
	//생성자
	public OneCardGame(Player player1,Player player2) {
		this.player1=player1;
		this.player2=player2;
		this.deck=new Deck();
		startGame();
		
	}
	
	//게임 시작 함수
	public  void startGame() {
		for(Player player: players) {
			for(int i=0; i<5; i++) //초기 5장 카드 지급
				player.receiveCard(deck.drawCard());
		}
		topCard=deck.drawCard(); //게임 시작 할 때 바닥에 놓는 카드
		
	}
	
	//플레이어가 카드 내는 함수
	public boolean playTurn(Player player,int cardIndex) {
		
	}
	//카드가 현재 탑 카드와 일치하는지 검사
	private boolean canPlayCard(Card card) {
		return card.getSuit().equals(topCard.getsuit()) || card.getRank()==topCard.getRank();
	}
	//승자가 있는지  확인하는 함수
	public Player checkWinner() {
		for(Player player:players) {
			if(player.getCardCount()==0) {
				return player;
			}
		}
		return null;
	}
	
	//게임 실행하는 메서드
	public void playGame() {
		
	}
	
}
