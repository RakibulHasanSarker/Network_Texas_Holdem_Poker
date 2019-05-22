public class Card implements Comparable<Card> {
    private String suit;
    private int rank;

    public Card(int rank, String suit) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public int compareTo(Card o) {
        return o.rank - rank;
    }

    @Override
    public String toString() {
        String temp = "";
        temp += rank;
        temp += "-";
        temp += suit;
        return temp;
    }

    public String CardToImage() {
        String s = "";

        if (suit.equalsIgnoreCase("Diamonds"))
            s += "d";
        else if (suit.equalsIgnoreCase("Clubs"))
            s += "c";
        else if (suit.equalsIgnoreCase("Spades"))
            s += "s";
        else
            s += "h";

        s += Integer.toString(rank);
        s += ".bmp";

        return s;
    }
}
