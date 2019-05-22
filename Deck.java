import java.util.*;

public class Deck {
    private Stack<Card> cards = new Stack<>();
    private String[] suits = {"Diamonds", "Clubs", "Hearts", "Spades"};

    public Deck() {
        for (String s : suits)
            for (int i = 2; i <= 14; i++)
                cards.add(new Card(i, s));

        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.pop();
    }
}
