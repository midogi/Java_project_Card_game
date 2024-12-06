package GameClass;

import java.util.List;

public class ComputerPlayerImpl extends PlayerImpl {

    public ComputerPlayerImpl(String name) {
        super(name);
    }

    @Override
    public CardImpl playCard(Card_game.Card topCard) {
        // 가능한 카드를 선택합니다. 우선적으로 낼 수 있는 카드를 선택합니다.
        for (CardImpl card : getHand()) {
            if (card.getSuit().equals(topCard.getSuit()) || card.getRank().equals(topCard.getRank()) || 
                card.getRank().equalsIgnoreCase("Color") || card.getRank().equalsIgnoreCase("Black & White")) {
                getHand().remove(card);
                return card;
            }
        }
        // 낼 카드가 없으면 null 반환 (드로우 해야 함)
        return null;
    }

    // 컴퓨터의 턴을 자동으로 수행하는 메소드
    public void takeTurn(One_Card game) {
        System.out.println(getName() + "의 턴입니다.");
        CardImpl topCard = game.getTopCard();
        CardImpl cardToPlay = playCard(topCard);

        if (cardToPlay != null) {
            // 낼 수 있는 카드가 있을 경우 카드 플레이
            System.out.println("[console]" + getName() + "가 " + cardToPlay + " 카드를 냅니다.");
            game.playCard(this, cardToPlay);
        } else {
            // 낼 카드가 없으면 카드 뽑기
            System.out.println("[console]" + getName() + "는 카드를 뽑습니다.");
            game.drawCard(this);
        }
    }
}
