import javafx.application.*;
import javafx.stage.*;
import java.util.*;

public class Client extends Application
{
    private Vector<Player> players;
    private int id;

    private Table table;
    private Menu1 m1 = new Menu1();
    private Menu2 m2 = new Menu2();

    @Override
    public void start(Stage primaryStage) throws Exception {
        NetworkUtil nu = new NetworkUtil("localhost", 33333);
        id = (Integer) nu.read();
        WelcomeWindow.show();

        if(id == 0) {
            PlayerCountWindow.show();
            nu.write(PlayerCountWindow.getOption());
        }

        NameWindow.show(id + 1);
        nu.write(NameWindow.getName());
        table = new Table(id);

        while (true) {

            switch ((Integer) nu.read()) {
                case 0: {
                    table.setMessage((Vector<String>)nu.read());
                    break;
                }
                case 1: {
                    m1.setPlayers(players);
                    m1.show(id);

                    int n = m1.getChoice();
                    nu.write(n);

                    if (n == 2)
                        nu.write(m1.getAmount());
                    break;
                }
                case 2: {
                    m2.setPlayers(players);
                    m2.show(id);

                    int n = m2.getChoice();
                    nu.write(n);

                    if (n == 3)
                        nu.write(m2.getAmount());
                    break;
                }
                case 3: {
                    players = Player.parse((Vector<String>) nu.read());

                    table.setPlayers(players);
                    table.show();
                    break;
                }
                case 4: {
                    players = Player.parse((Vector<String>) nu.read());

                    table.setPlayers(players);
                    table.showAll();
                    break;
                }
                case 5: {
                    int n = (Integer) nu.read();
                    if (n == 1)
                        table.incrementRoundId();
                    else
                        table.setRoundId();
                    break;
                }
                case 6: {
                    table.end();
                    nu.closeConnection();
                    return;
                }
                case 7: {
                    MessageBox.show("Press Enter to go next");

                    while (!MessageBox.getFlag());

                    nu.write(true);
                    break;
                }
                case 8:
                    table.reset();
                    break;
            }
        }
    }
    public static void main(String args[]) {
        launch();
    }
}