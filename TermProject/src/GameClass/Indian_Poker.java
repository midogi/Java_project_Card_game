package t1_1;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//Indian_Poker 클래스
public class Indian_Poker implements Card_game.Game{

	private Player player1; //첫 번째 플레이어
	private Player player2; //두 번째 플레이어
	private Deck deck; // 카드 덱

	
	
	
	//생성자
	public class Indian_Poker (Player player1, Player player2) {
		this.player1=player1;
		this.player2=player2;
		this.deck=new Deck();
		deck.shuffle();
	}
	@Override
	//게임시작하고 플레이어에게 카드 지급
	public void startGame() {
		
		for(Player player: players) {
			player.receiveCard(deck.drawCard());
		}
	}
	
	@Override
	public void playTurn(Player player) {
		//인디언포커 턴 진행 로직 구현
	}
	
	public Player determineWinner() {
		//게임의 승자 결정하는 로직 구현
		return null;
	}
	
	
	
	
	
}
