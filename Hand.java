import java.util.*;

public class Hand implements Comparable<Hand> {
    private Vector<Card> cards;

    public Hand(Vector<Card> cards) {
        this.cards = cards;
    }

    public int areConsecutive2(Vector<Integer> v) {
        for (int i = 14; i >= 5; i--) {
            if (v.get(i) > 0) {
                boolean flag = true;

                for (int j = i - 1; j >= i - 4; j--)
                    if (v.get(j) <= 0)
                        flag = false;

                if (flag)
                    return i;
            }
        }
        return -1;
    }

    public boolean areConsecutive(Vector<Integer> v) {
        return areConsecutive2(v) != -1;
    }

    public HandType bestHandType() {
        HandData data = new HandData(cards);
        Vector<Integer> v = data.getRankCounts();

        if (data.getTotalSuits() == 1 && v.get(14) == 1 && v.get(13) == 1 &&
                v.get(12) == 1 && v.get(11) == 1 && v.get(10) == 1)

            return new HandType("Royal Flush", 9);

        else if (data.getTotalSuits() == 1 && areConsecutive(v))
            return new HandType("Straight Flush", 8);

        else if (v.get(15) == 4)
            return new HandType("Four-Of-A-Kind", 7);

        else if (v.get(15) == 3 && v.get(16) == 2)
            return new HandType("Full House", 6);

        else if (data.getTotalSuits() == 1)
            return new HandType("Flush", 5);

        else if (areConsecutive(v))
            return new HandType("Straight", 4);

        else if (v.get(15) == 3)
            return new HandType("Three-Of-A-Kind", 3);

        else if (v.get(15) == 2 && v.get(16) == 2)
            return new HandType("Two Pair", 2);

        else if (v.get(15) == 2)
            return new HandType("One Pair", 1);

        else
            return new HandType("High Card", 0);
    }

    @Override
    public int compareTo(Hand o) {
        int ou = o.bestHandType().getValue(), u = bestHandType().getValue();
        String s = o.bestHandType().getType();

        if (ou != u)
            return ou - u;

        int a = areConsecutive2(new HandData(cards).getRankCounts());
        int b = areConsecutive2(new HandData(o.cards).getRankCounts());

        if (a == 5 && b > 5)
            return 1;

        else if (a > 5 && b == 5)
            return -1;

        else if (s.equals("Royal Flush") || s.equals("Straight Flush") ||
                s.equals("Flush") || s.equals("Straight") || s.equals("High Card")) {

            for (int i = 0; i < o.cards.size(); i++)
                if (cards.get(i).compareTo(o.cards.get(i)) != 0)
                    return cards.get(i).compareTo(o.cards.get(i));
        }
        else if (s.equals("Four-Of-A-Kind")) {
            Vector<Card> o1 = fourOfAKind(new HandData(cards).getRankCounts());
            Vector<Card> o2 = fourOfAKind(new HandData(o.cards).getRankCounts());

            for (int i = 0; i < o1.size(); i++)
                if (o1.get(i).compareTo(o2.get(i)) != 0)
                    return o1.get(i).compareTo(o2.get(i));
        }
        else if (s.equals("Full House")) {
            Vector<Card> o1 = fullHouse(new HandData(cards).getRankCounts());
            Vector<Card> o2 = fullHouse(new HandData(o.cards).getRankCounts());

            for (int i = 0; i < o1.size(); i++)
                if (o1.get(i).compareTo(o2.get(i)) != 0)
                    return o1.get(i).compareTo(o2.get(i));
        }
        else if (s.equals("Three-Of-A-Kind")) {
            Vector<Card> o1 = threeOfAKind(new HandData(cards).getRankCounts());
            Vector<Card> o2 = threeOfAKind(new HandData(o.cards).getRankCounts());

            for (int i = 0; i < o1.size(); i++)
                if (o1.get(i).compareTo(o2.get(i)) != 0)
                    return o1.get(i).compareTo(o2.get(i));
        }
        else if (s.equals("Two Pair")) {
            Vector<Card> o1 = twoPair(new HandData(cards).getRankCounts());
            Vector<Card> o2 = twoPair(new HandData(o.cards).getRankCounts());

            for (int i = 0; i < o1.size(); i++)
                if (o1.get(i).compareTo(o2.get(i)) != 0)
                    return o1.get(i).compareTo(o2.get(i));
        }
        else if (s.equals("One Pair")) {
            Vector<Card> o1 = onePair(new HandData(cards).getRankCounts());
            Vector<Card> o2 = onePair(new HandData(o.cards).getRankCounts());

            for (int i = 0; i < o1.size(); i++)
                if (o1.get(i).compareTo(o2.get(i)) != 0)
                    return o1.get(i).compareTo(o2.get(i));
        }
        return 0;
    }

    private Vector<Card> fourOfAKind(Vector<Integer> count) {
        Vector<Card> notSoMany = new Vector<>();
        for (int i = 2; i <= 14; i++)
            if (count.get(i) == 4) {
                notSoMany.add(new Card(i, "Clubs"));
                break;
            }

        for (int i = 2; i <= 14; i++)
            if (count.get(i) == 1) {
                notSoMany.add(new Card(i, "Clubs"));
                break;
            }

        return notSoMany;
    }

    private Vector<Card> fullHouse(Vector<Integer> count) {
        Vector<Card> notSoMany = new Vector<>();
        for (int i = 2; i <= 14; i++)
            if (count.get(i) == 3) {
                notSoMany.add(new Card(i, "Clubs"));
                break;
            }

        for (int i = 2; i <= 14; i++)
            if (count.get(i) == 2) {
                notSoMany.add(new Card(i, "Clubs"));
                break;
            }

        return notSoMany;
    }

    private Vector<Card> threeOfAKind(Vector<Integer> count) {
        Vector<Card> notSoMany = new Vector<>();
        for (int i = 2; i <= 14; i++)
            if (count.get(i) == 3) {
                notSoMany.add(new Card(i, "Clubs"));
                break;
            }

        for (int i = 14; i >= 2; i--)
            if (count.get(i) == 1) {
                notSoMany.add(new Card(i, "Clubs"));
            }

        return notSoMany;
    }

    private Vector<Card> twoPair(Vector<Integer> count) {
        Vector<Card> notSoMany = new Vector<>();
        for (int i = 14; i >= 2; i--)
            if (count.get(i) == 2) {
                notSoMany.add(new Card(i, "Clubs"));
            }

        for (int i = 2; i <= 14; i++)
            if (count.get(i) == 1) {
                notSoMany.add(new Card(i, "Clubs"));
                break;
            }

        return notSoMany;
    }

    private Vector<Card> onePair(Vector<Integer> count) {
        Vector<Card> notSoMany = new Vector<>();
        for (int i = 2; i <= 14; i++)
            if (count.get(i) == 2) {
                notSoMany.add(new Card(i, "Clubs"));
                break;
            }

        for (int i = 14; i >= 2; i--)
            if (count.get(i) == 1) {
                notSoMany.add(new Card(i, "Clubs"));
            }

        return notSoMany;
    }
}
