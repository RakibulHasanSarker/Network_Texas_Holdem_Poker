import java.math.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class Server {

    private HashMap<String, NetworkUtil> table;
    private Vector<Player> players;
    private Vector<Message> messages;

    private int id, roundEndingCount, numberOfPlayers, totalPlayers, BBidx;
    private int aliveTotalPlayers, roundCounter, playerCount, sixTimes;

    private boolean hasBetted, gameEnded, BBDone;
    private double amount;

    private Deck d;
    private Card c;

    private Server() {
        id = 0;
        totalPlayers = 0;
        roundCounter = 0;
        sixTimes = 0;
        gameEnded = false;

        table = new HashMap<>();
        players = new Vector<>();
        messages = new Vector<>();

        try {
            ServerSocket serverSocket = new ServerSocket(33333);
            while (!gameEnded) {
                Socket clientSocket = serverSocket.accept();
                serve(clientSocket);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serve(Socket clientSocket) {
        NetworkUtil nu = new NetworkUtil(clientSocket);
        nu.write(totalPlayers);

        if (totalPlayers == 0)
            numberOfPlayers = (Integer) nu.read();

        String clientName = (String) nu.read();

        table.put(clientName, nu);
        players.add(new Player(clientName));

        totalPlayers++;

        if (totalPlayers == numberOfPlayers)
            play();
    }

    private void play() {
        playerCount = totalPlayers;

        while (!gameEnded) {
            initialization();
            preFlop();
            flop();
            turn();
            river();
            showdown();
            roundCounter++;
        }
    }

    private void initialization() {
        BBDone = false;
        sixTimes = (roundCounter + 1) / 6;
        d = new Deck();

        setPot(0);

        aliveTotalPlayers = playerCount;

        setCallAmount(0);

        if (messages.size() == 0)
            for (int i = 0; i < totalPlayers; i++)
                messages.add(new Message());
        else
            for (int i = 0; i < totalPlayers; i++)
                messages.get(i).clear();

        for (int i = 0; i < totalPlayers; i++) {
            players.get(i).reset();

            for (int j = 0; j < 2; j++) {
                c = d.drawCard();
                players.get(i).addCard(c);
                players.get(i).addHoleCard(c);
            }
        }

        for (Player p : players)
            table.get(p.getName()).write(8);
    }

    private void setValidId() {
        id %= totalPlayers;

        while (players.get(id).getHasFolded() || players.get(id).getIsAllIn() || players.get(id).getIsLoser())
            id = (id + 1) % totalPlayers;
    }

    private void blind(int blindAmount) {
        amount = blindAmount * (1 << sixTimes);
        players.get(id).setGiven(amount);

        boolean flag = amount < players.get(id).getChips();
        players.get(id).spend(amount);

        if (!flag)
            aliveTotalPlayers--;

        setPot(players.get(id).getPot());

        if (blindAmount == 5)
            for (int i = 0; i < totalPlayers; i++) {
                if (i == id) {
                    if (flag)
                        messages.get(i).add("You gave " + amount + "$ as small blind");
                    else
                        messages.get(i).add("You have gone all-in due to small blind");
                }
                else {
                    if (flag)
                        messages.get(i).add(players.get(id).getName() + " gave " + amount + "$ as small blind");
                    else
                        messages.get(i).add(players.get(id).getName() + " have gone all-in due to small blind");
                }
            }

        else
            for (int i = 0; i < totalPlayers; i++) {
                if (i == id) {
                    if (flag)
                        messages.get(i).add("You gave " + amount + "$ as big blind");
                    else
                        messages.get(i).add("You have gone all-in due to big blind");
                }
                else {
                    if (flag)
                        messages.get(i).add(players.get(id).getName() + " gave " + amount + "$ as big blind");
                    else
                        messages.get(i).add(players.get(id).getName() + " have gone all-in due to big blind");
                }
            }

        if(blindAmount == 10) {
            for (Message m : messages)
                m.add("Pre-flop round has started");

            for (int i = 0; i < players.size(); i++)
                if (!players.get(i).getIsLoser()) {
                    table.get(players.get(i).getName()).write(0);
                    table.get(players.get(i).getName()).write(messages.get(i).getMessage());
                }
        }
    }

    private void runLoop() {
        while (true) {
            setValidId();
            setCurrent(id);
            reset();

            if (!hasBetted)
                menu1();
            else
                menu2();

            if (aliveTotalPlayers == roundEndingCount)
                return;

            id++;
        }
    }

    private void preFlop() {
        boolean isMenu1ForBB = true;

        hasBetted = true;
        roundEndingCount = 0;
        initGiven();

        id = roundCounter % totalPlayers;
        setValidId();

        setCurrent(id);
        setDealer((totalPlayers + id - 1) % totalPlayers);
        setCallAmount(10 * (1 << sixTimes));

        for (int amount = 5; amount <= 10; amount += 5) {
            setValidId();
            setCurrent(id);

            if (amount == 10)
                BBidx = id;

            blind(amount);
            id++;
        }

        if (aliveTotalPlayers <= 1)
            return;

        while (true) {
            setValidId();
            setCurrent(id);
            reset();

            if (id == BBidx && !BBDone)
                BBDone = true;

            if (!hasBetted || (id == BBidx && isMenu1ForBB)) {
                menu1();
                isMenu1ForBB = false;
            }

            else {
                table.get(players.get(id).getName()).write(2);
                int op = (Integer) table.get(players.get(id).getName()).read();

                if (op == 1) {
                    players.get(id).fold();
                    aliveTotalPlayers--;

                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getIsLoser())
                            continue;

                        if (id == i)
                            messages.get(i).add("You folded");
                        else
                            messages.get(i).add(players.get(id).getName() + " folded");

                        table.get(players.get(i).getName()).write(0);
                        table.get(players.get(i).getName()).write(messages.get(i).getMessage());
                    }
                }

                else if (op == 2) {
                    amount = players.get(0).getCallAmount() - players.get(id).getGiven();
                    players.get(id).spend(amount);

                    amount += players.get(id).getGiven();
                    players.get(id).setGiven(amount);

                    roundEndingCount++;

                    setPot(players.get(id).getPot());

                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getIsLoser())
                            continue;

                        if (i == id)
                            messages.get(i).add("You called");
                        else
                            messages.get(i).add(players.get(id).getName() + " called");

                        table.get(players.get(i).getName()).write(0);
                        table.get(players.get(i).getName()).write(messages.get(i).getMessage());
                    }
                }
                else if (op == 3) {

                    isMenu1ForBB = false;
                    amount = (Double) table.get(players.get(id).getName()).read();
                    setCallAmount(amount);

                    amount -= players.get(id).getGiven();
                    players.get(id).spend(amount);

                    amount += players.get(id).getGiven();
                    players.get(id).setGiven(amount);

                    roundEndingCount = 1;
                    setPot(players.get(id).getPot());

                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getIsLoser())
                            continue;

                        if (i == id)
                            messages.get(i).add("You raised to " + amount + "$");
                        else
                            messages.get(i).add(players.get(id).getName() + " raised to " + amount + "$");

                        table.get(players.get(i).getName()).write(0);
                        table.get(players.get(i).getName()).write(messages.get(i).getMessage());
                    }
                }
                else if (op == 4) {

                    isMenu1ForBB = false;
                    aliveTotalPlayers--;
                    if (players.get(id).getChips() > players.get(0).getCallAmount())
                        roundEndingCount = 0;

                    amount = players.get(id).getChips() + players.get(id).getGiven();
                    amount = Math.max(players.get(0).getCallAmount(), amount);
                    setCallAmount(amount);

                    players.get(id).spend(players.get(id).getChips());
                    setPot(players.get(id).getPot());

                    for (int i = 0; i < players.size(); i++) {
                        if (players.get(i).getIsLoser())
                            continue;

                        if (i == id)
                            messages.get(i).add("You have gone all-in");
                        else
                            messages.get(i).add(players.get(id).getName() + " has gone all-in");

                        table.get(players.get(i).getName()).write(0);
                        table.get(players.get(i).getName()).write(messages.get(i).getMessage());
                    }
                }
            }

            if (BBDone && aliveTotalPlayers == roundEndingCount)
                return;

            id++;
        }
    }


    private void flop() {
        incrementRoundId();
        initGiven();
        setCallAmount(5 * (1 << sixTimes));
        id = roundCounter % totalPlayers;

        hasBetted = false;
        roundEndingCount = 0;

        for (int i = 0; i < 3; i++) {
            c = d.drawCard();

            for (Player p : players)
                if (!p.getIsLoser()) {
                    p.addCard(c);
                    p.addCommunityCard(c);
                }
        }

        for (Message m : messages)
            m.add("Flop round has started");

        for (int i = 0; i < players.size(); i++)
            if (!players.get(i).getIsLoser()) {
                table.get(players.get(i).getName()).write(0);
                table.get(players.get(i).getName()).write(messages.get(i).getMessage());
            }

        if (aliveTotalPlayers <= 1)
            return;

        runLoop();
    }

    private void turn() {
        incrementRoundId();
        initGiven();
        setCallAmount(5 * (1 << sixTimes));
        id = roundCounter % totalPlayers;

        hasBetted = false;
        roundEndingCount = 0;

        c = d.drawCard();

        for (Player p : players)
            if (!p.getIsLoser()) {
                p.addCard(c);
                p.addCommunityCard(c);
            }

        for (Message m : messages)
            m.add("Turn round has started");

        for (int i = 0; i < players.size(); i++)
            if (!players.get(i).getIsLoser()) {
                table.get(players.get(i).getName()).write(0);
                table.get(players.get(i).getName()).write(messages.get(i).getMessage());
            }

        if (aliveTotalPlayers <= 1)
            return;

        runLoop();
    }

    private void river() {
        incrementRoundId();
        initGiven();
        setCallAmount(5 * (1 << sixTimes));
        id = roundCounter % totalPlayers;

        hasBetted = false;
        roundEndingCount = 0;

        c = d.drawCard();

        for (Player p : players)
            if (!p.getIsLoser()) {
                p.addCard(c);
                p.addCommunityCard(c);
            }

        for (Message m : messages)
            m.add("River round has started");

        for (int i = 0; i < players.size(); i++)
            if (!players.get(i).getIsLoser()) {
                table.get(players.get(i).getName()).write(0);
                table.get(players.get(i).getName()).write(messages.get(i).getMessage());
            }

        if (aliveTotalPlayers <= 1)
            return;

        runLoop();
    }

    private void showdown() {
        Vector<Player> tempPlayers = new Vector<>();

        for (Player p : players)
            if (!p.getHasFolded() && !p.getIsLoser())
                tempPlayers.add(p);

        Collections.sort(tempPlayers);

        double winnerCounter;
        for (winnerCounter = 1; (int) winnerCounter < tempPlayers.size(); winnerCounter += 1) {
            Hand bh1, bh2;
            bh1 = tempPlayers.get((int) winnerCounter - 1).bestHand();
            bh2 = tempPlayers.get((int) winnerCounter).bestHand();
            if (bh1.compareTo(bh2) != 0)
                break;
        }

        amount = players.get(0).getPot() / winnerCounter;

        for (int i = 0; i < (int) winnerCounter; i++)
            tempPlayers.get(i).setChips(tempPlayers.get(i).getChips() + amount);

        for (Player p : players)
            for (Player q : tempPlayers)
                if (p.equals(q))
                    p.setChips(q.getChips());

        for (Message m : messages)
            m.add("Showdown has started");

        for (int i = 0; i < players.size(); i++)
            for (int j = 0; j < players.size(); j++)
                if (!players.get(j).getIsLoser() && !players.get(j).getHasFolded()) {
                    if (i == j)
                        messages.get(i).add("You have " + players.get(j).showBestHand());
                    else
                        messages.get(i).add(players.get(j).getName() + " has " + players.get(j).showBestHand());
                }

        DecimalFormat df = new DecimalFormat("####.##");
        df.setRoundingMode(RoundingMode.CEILING);
        amount = Double.parseDouble(df.format(amount));

        for (int i = 0; i < players.size(); i++)
            for (int j = 0; j < (int) winnerCounter; j++) {
                if (tempPlayers.get(j).equals(players.get(i)))
                    messages.get(i).add("You won a pot of " + amount + "$");
                else
                    messages.get(i).add(tempPlayers.get(j).getName() + " won a pot of " + amount + "$");
            }

        setPot(0);
        incrementRoundId();

        for (int i = 0; i < messages.size(); i++) {
            if (players.get(i).getIsLoser())
                continue;

            table.get(players.get(i).getName()).write(0);
            table.get(players.get(i).getName()).write(messages.get(i).getMessage());
        }

        reset2();

        for (Player p : players)
            if (!p.getIsLoser() && !p.getHasFolded()) {
                table.get(p.getName()).write(7);
                block(p);
            }

        for (int i = 0; i < totalPlayers; i++)
            if (!players.get(i).getHasFolded() && players.get(i).getChips() == 0
                    && !players.get(i).getIsLoser()) {

                players.get(i).setLoser();
                playerCount--;

                messages.get(i).clear();
                messages.get(i).add("You lose the game\nBye");

                table.get(players.get(i).getName()).write(0);
                table.get(players.get(i).getName()).write(messages.get(i).getMessage());

                Vector<String> temp = new Vector<>();
                for (Player p : players)
                    temp.add(p.toString());

                table.get(players.get(i).getName()).write(4);
                table.get(players.get(i).getName()).write(temp);

                table.get(players.get(i).getName()).write(7);
                block(players.get(i));

                table.get(players.get(i).getName()).write(6);
                table.get(players.get(i).getName()).closeConnection();
            }

        Player p = null;
        int cnt = 0, idx = -1;

        for (int i = 0; i < players.size(); i++)
            if (!players.get(i).getIsLoser()) {
                p = players.get(i);
                idx = i;
                cnt++;
            }

        if (cnt == 1) {
            messages.get(idx).clear();
            messages.get(idx).add("You are the champion!");

            table.get(p.getName()).write(0);
            table.get(p.getName()).write(messages.get(idx).getMessage());

            reset2();

            table.get(p.getName()).write(7);
            block(p);

            table.get(p.getName()).write(6);
            table.get(p.getName()).closeConnection();
            gameEnded = true;
        }

        for (Player plr : players) {
            if (plr.getIsLoser())
                continue;

            table.get(plr.getName()).write(5);
            table.get(plr.getName()).write(2);
        }
    }

    private void menu1() {
        table.get(players.get(id).getName()).write(1);
        int op = (Integer) table.get(players.get(id).getName()).read();

        if (op == 1) {
            roundEndingCount++;

            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getIsLoser())
                    continue;

                if (i == id)
                    messages.get(i).add("You checked");
                else
                    messages.get(i).add(players.get(id).getName() + " checked");

                table.get(players.get(i).getName()).write(0);
                table.get(players.get(i).getName()).write(messages.get(i).getMessage());
            }
        }

        else if (op == 2) {
            amount = (Double) table.get(players.get(id).getName()).read();
            setCallAmount(amount);

            amount -= players.get(id).getGiven();
            players.get(id).spend(amount);

            amount += players.get(id).getGiven();
            players.get(id).setGiven(amount);

            hasBetted = true;
            roundEndingCount = 1;

            setPot(players.get(id).getPot());

            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getIsLoser())
                    continue;

                if (i == id)
                    messages.get(i).add("You raised to " + amount + "$");
                else
                    messages.get(i).add(players.get(id).getName() + " raised to " + amount + "$");

                table.get(players.get(i).getName()).write(0);
                table.get(players.get(i).getName()).write(messages.get(i).getMessage());
            }
        }

        else if (op == 3) {
            aliveTotalPlayers--;
            roundEndingCount = 0;
            hasBetted = true;
            setCallAmount(Math.max(players.get(0).getCallAmount(), players.get(id).getChips()));

            players.get(id).spend(players.get(id).getChips());
            setPot(players.get(id).getPot());

            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getIsLoser())
                    continue;

                if (i == id)
                    messages.get(i).add("You've gone all-in");
                else
                    messages.get(i).add(players.get(id).getName() + " has gone all-in");

                table.get(players.get(i).getName()).write(0);
                table.get(players.get(i).getName()).write(messages.get(i).getMessage());
            }
        }
    }

    private void menu2() {
        table.get(players.get(id).getName()).write(2);
        int op = (Integer) table.get(players.get(id).getName()).read();

        if (op == 1) {
            players.get(id).fold();
            aliveTotalPlayers--;

            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getIsLoser())
                    continue;

                if (id == i)
                    messages.get(i).add("You folded");
                else
                    messages.get(i).add(players.get(id).getName() + " folded");

                table.get(players.get(i).getName()).write(0);
                table.get(players.get(i).getName()).write(messages.get(i).getMessage());
            }
        }

        else if (op == 2) {
            amount = players.get(0).getCallAmount() - players.get(id).getGiven();
            players.get(id).spend(amount);

            amount += players.get(id).getGiven();
            players.get(id).setGiven(amount);

            roundEndingCount++;

            setPot(players.get(id).getPot());

            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getIsLoser())
                    continue;

                if (i == id)
                    messages.get(i).add("You called");
                else
                    messages.get(i).add(players.get(id).getName() + " called");

                table.get(players.get(i).getName()).write(0);
                table.get(players.get(i).getName()).write(messages.get(i).getMessage());
            }
        }
        else if (op == 3) {
            amount = (Double) table.get(players.get(id).getName()).read();
            setCallAmount(amount);

            amount -= players.get(id).getGiven();
            players.get(id).spend(amount);

            amount += players.get(id).getGiven();
            players.get(id).setGiven(amount);

            roundEndingCount = 1;
            setPot(players.get(id).getPot());

            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getIsLoser())
                    continue;

                if (i == id)
                    messages.get(i).add("You raised to " + amount + "$");
                else
                    messages.get(i).add(players.get(id).getName() + " raised to " + amount + "$");

                table.get(players.get(i).getName()).write(0);
                table.get(players.get(i).getName()).write(messages.get(i).getMessage());
            }
        }
        else if (op == 4) {
            aliveTotalPlayers--;
            amount = players.get(id).getChips() + players.get(id).getGiven();

            if (amount > players.get(0).getCallAmount())
                roundEndingCount = 0;

            amount = Math.max(players.get(0).getCallAmount(), amount);
            setCallAmount(amount);

            players.get(id).spend(players.get(id).getChips());
            setPot(players.get(id).getPot());

            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getIsLoser())
                    continue;

                if (i == id)
                    messages.get(i).add("You have gone all-in");
                else
                    messages.get(i).add(players.get(id).getName() + " has gone all-in");

                table.get(players.get(i).getName()).write(0);
                table.get(players.get(i).getName()).write(messages.get(i).getMessage());
            }
        }
    }

    private void initGiven() {
        for (Player p : players)
            p.setGiven(0);
    }

    private void incrementRoundId() {
        for (Player p : players) {
            table.get(p.getName()).write(5);
            table.get(p.getName()).write(1);
        }
    }

    private void setDealer(int i) {
        for (Player p : players)
            p.setDealer(false);

        players.get(i).setDealer(true);
    }

    private void setCurrent(int i) {
        for (Player p : players)
            p.setCurrent(false);

        players.get(i).setCurrent(true);
    }

    private void reset() {
        Vector<String> temp = new Vector<>();
        for (Player p : players)
            temp.add(p.toString());

        for (Player p : players) {
            if (p.getIsLoser())
                continue;

            table.get(p.getName()).write(3);
            table.get(p.getName()).write(temp);
        }
    }

    private void reset2() {
        Vector<String> temp = new Vector<>();
        for (Player p : players)
            temp.add(p.toString());

        for (Player p : players) {
            if (p.getIsLoser())
                continue;

            table.get(p.getName()).write(4);
            table.get(p.getName()).write(temp);
        }
    }

    private void setPot(double thisPot) {
        for (Player p : players)
            p.setPot(thisPot);
    }

    private void setCallAmount(double thisCallAmount) {
        for (Player p : players)
            p.setCallAmount(thisCallAmount);
    }

    private void block(Player p) {
        table.get(p.getName()).read();
    }

    public static void main(String args[]) {
        new Server();
    }
}