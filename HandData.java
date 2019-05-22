import java.util.*;

public class HandData {
    private int totalSuits;
    private Vector<Integer> rankCounts = new Vector<>(Collections.nCopies(17, 0));

    HandData(Vector<Card> cards) {
        HashSet<String> suits = new HashSet<>();

        for (Card card : cards) {
            int index = card.getRank();

            suits.add(card.getSuit());
            rankCounts.set(index, rankCounts.get(index) + 1);
        }

        rankCounts.set(1, rankCounts.get(14));

        int best, secondBest;
        best = secondBest = 0;

        for (int i = 2; i < 15; i++) {
            if (rankCounts.get(i) >= best) {
                secondBest = best;
                best = rankCounts.get(i);
            }
            else if (rankCounts.get(i) > secondBest) {
                secondBest = rankCounts.get(i);
            }
        }

        rankCounts.set(15, best);
        rankCounts.set(16, secondBest);
        totalSuits = suits.size();
    }

    public int getTotalSuits() {
        return totalSuits;
    }

    public Vector<Integer> getRankCounts() {
        return rankCounts;
    }
}