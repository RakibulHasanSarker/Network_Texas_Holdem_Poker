import java.util.*;

public class Player implements Comparable<Player> {
    private Vector<Card> cards, hole, community;
    private String name;

    private double chips, pot, callAmount, given;
    private boolean hasFolded, isAllIn, isLoser;
    private boolean isDealer, isCurrent;

    double getGiven() {
        return given;
    }

    void setGiven(double given) {
        this.given = given;
    }

    boolean getIsCurrent() {
        return isCurrent;
    }

    void setCurrent(boolean current) {
        isCurrent = current;
    }

    boolean getIsDealer() {
        return isDealer;
    }

    void setDealer(boolean dealer) {
        isDealer = dealer;
    }

    double getCallAmount() {
        return callAmount;
    }

    void setCallAmount(double callAmount) {
        this.callAmount = callAmount;
    }

    double getPot() {
        return pot;
    }

    void setPot(double pot) {
        this.pot = pot;
    }

    boolean getIsLoser() {
        return isLoser;
    }

    void setLoser() {
        isLoser = true;
    }

    double getChips() {
        return chips;
    }

    void setChips(double chips) {
        this.chips = chips;
    }

    Vector<Card> getHole() {
        return hole;
    }

    Vector<Card> getCommunity() {
        return community;
    }

    boolean getHasFolded() {
        return hasFolded;
    }

    boolean getIsAllIn() {
        return isAllIn;
    }

    void fold() {
        hasFolded = true;
    }

    public String getName() {
        return name;
    }

    public Player(String name) {
        this.name = name;

        cards = new Vector<>();
        hole = new Vector<>();
        community = new Vector<>();

        chips = 1000; pot = 0; callAmount = 0; given = 0;
        hasFolded = false; isAllIn = false; isLoser = false;
        isDealer = false; isCurrent = false;
    }

    void spend(double amount) {
        if (amount >= chips) {
            isAllIn = true;
            pot += chips;
            chips = 0;
            return;
        }
        pot += amount;
        chips -= amount;
    }

    void reset() {
        community = new Vector<>();
        hole = new Vector<>();
        cards = new Vector<>();
        hasFolded = false;
        isAllIn = false;
    }

    void addCard(Card card) {
        cards.add(card);
        Collections.sort(cards);
    }

    void addHoleCard(Card card) {
        hole.add(card);
    }

    void addCommunityCard(Card card) {
        this.community.add(card);
    }

    Hand bestHand() {
        Vector<Hand> hands = new Vector<>();
        int maxHandValue = 0;

        for (int i = 0; i < 7; i++) 
            for (int j = i + 1; j < 7; j++) {
                Vector<Card> v = new Vector<>();
                
                for (int k = 0; k < 7; k++) 
                    if (k != i && k != j)
                        v.add(cards.get(k));
                
                maxHandValue = Math.max(maxHandValue, new Hand(v).bestHandType().getValue());
            }

        for (int i = 0; i < 7; i++) 
            for (int j = i + 1; j < 7; j++) {
                Vector<Card> v = new Vector<>();
                
                for (int k = 0; k < 7; k++) 
                    if (k != i && k != j)
                        v.add(cards.get(k));
                
                if (new Hand(v).bestHandType().getValue() == maxHandValue)
                    hands.add(new Hand(v));
            }

        Collections.sort(hands);
        return hands.get(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Player))
            return false;

        return name.equals(((Player) obj).getName());
    }

    String showBestHand() {
        return bestHand().bestHandType().getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Player o) {
        Hand h1 = bestHand(), h2 = o.bestHand();
        return h1.compareTo(h2);
    }

    @Override
    public String toString() {
        StringBuffer temp = new StringBuffer("");
        int i;

        if (cards.size() > 0)
            temp.append(cards.get(0).toString());
        else
            temp.append("null");

        for (i = 1; i < cards.size(); i++)
            temp.append("," + cards.get(i).toString());

        for (; i < 7; i++)
            temp.append(",null");

        for (i = 0; i < hole.size(); i++)
            temp.append("," + hole.get(i).toString());

        for (; i < 2; i++)
            temp.append(",null");

        for (i = 0; i < community.size(); i++)
            temp.append("," + community.get(i).toString());

        for (; i < 5; i++)
            temp.append(",null");

        temp.append("," + name);
        temp.append("," + chips);
        temp.append("," + pot);
        temp.append("," + callAmount);
        temp.append("," + hasFolded);
        temp.append("," + isAllIn);
        temp.append("," + isLoser);
        temp.append("," + isDealer);
        temp.append("," + isCurrent);
        temp.append("," + given);

        return temp.toString();
    }

    static Vector<Player> parse(Vector<String> temp)
    {
        Vector<Player> players = new Vector<>();

        for(String s : temp)
        {
            String[] strings = s.split(",");

            Vector<Card> cards, hole, community;
            cards = new Vector<>();
            hole = new Vector<>();
            community = new Vector<>();

            for(int i = 0; i < 7; i++)
            {
                if(strings[i].equalsIgnoreCase("null"))
                    break;

                String[] fewCards = strings[i].split("-");
                cards.add(new Card(Integer.parseInt(fewCards[0]), fewCards[1]));
            }

            for(int i = 7; i < 9; i++)
            {
                if(strings[i].equalsIgnoreCase("null"))
                    break;

                String[] fewCards = strings[i].split("-");
                hole.add(new Card(Integer.parseInt(fewCards[0]), fewCards[1]));
            }

            for(int i = 9; i < 14; i++)
            {
                if(strings[i].equalsIgnoreCase("null"))
                    break;

                String[] fewCards = strings[i].split("-");
                community.add(new Card(Integer.parseInt(fewCards[0]), fewCards[1]));
            }

            String name = strings[14];
            double chips = Double.parseDouble(strings[15]);
            double pot = Double.parseDouble(strings[16]);
            double callAmount = Double.parseDouble(strings[17]);
            boolean hasFolded = Boolean.parseBoolean(strings[18]);
            boolean isAllIn = Boolean.parseBoolean(strings[19]);
            boolean isLoser = Boolean.parseBoolean(strings[20]);
            boolean isDealer = Boolean.parseBoolean(strings[21]);
            boolean isCurrent = Boolean.parseBoolean(strings[22]);
            double given = Double.parseDouble(strings[23]);

            Player p = new Player(name);

            p.cards = cards;
            p.hole = hole;
            p.community = community;
            p.name = name;
            p.chips = chips;
            p.pot = pot;
            p.callAmount = callAmount;
            p.hasFolded = hasFolded;
            p.isAllIn = isAllIn;
            p.isLoser = isLoser;
            p.isDealer = isDealer;
            p.isCurrent = isCurrent;
            p.given = given;

            players.add(p);
        }

        return players;
    }
}
