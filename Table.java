import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.shape.*;
import java.math.*;
import java.text.*;
import java.util.*;

public class Table {
    private int id;
    private Stage stg;
    private Vector<Player> players = new Vector<>();
    private Vector<ImageView> images;
    private DecimalFormat df = new DecimalFormat("####.##");

    private int roundId;
    private String s = "back.bmp";

    private final Vector<String> roundNames =
            new Vector<>(Arrays.asList("Pre-Flop Round", "Flop Round", "Turn Round", "River Round", "Showdown"));
    private AnchorPane pane;
    private Scene scene;

    private Vector<Text> txtNames;
    private Vector<Text> txtChips;
    private Vector<Text> message;
    private Vector<Text> message2;
    Text roundName;
    Text txtPot;

    public void setPlayers(Vector<Player> players) {
        this.players = players;
    }

    public void end() {
        stg.close();
    }

    public void incrementRoundId() {
        roundId++;
    }

    public void setRoundId() {
        for (Text t : message2)
            t.setText("");

        roundId = 0;
    }

    public void reset() {
        images = new Vector<>();
        txtNames = new Vector<>();
        txtChips = new Vector<>();

        roundName = new Text(roundNames.get(roundId));
        txtPot = new Text("Pot: 0$");

        Rectangle background = new Rectangle(1280, 650);
        background.setFill(Color.GREEN);

        Rectangle r = new Rectangle(768, 390);
        r.setFill(Color.DARKGREEN);
        r.setArcWidth(390);
        r.setArcHeight(390);

        Rectangle m = new Rectangle(330, 200);
        m.setFill(Color.BISQUE);

        Rectangle n = new Rectangle(330, 200);
        n.setFill(Color.BISQUE);

        for (int i = 0; i < 17; i++)
            images.add(new ImageView("back.bmp"));

        for (int i = 0; i < 6; i++)
            txtChips.add(new Text());

        for (int i = 0; i < 6; i++)
            txtNames.add(new Text());

        setImages(0, s, 351, 450);
        setImages(1, s, 401, 450);
        setTxtNames(0, "1", 351, 550);
        setTxtChips(0, "Chips: 0$", 351, 580);

        setImages(2, s, 170, 290);
        setImages(3, s, 220, 290);
        setTxtNames(1, "2", 10, 310);
        setTxtChips(1, "Chips: 0$", 10, 340);

        setImages(4, s, 351, 130);
        setImages(5, s, 401, 130);
        setTxtNames(2, "2", 351, 80);
        setTxtChips(2, "Chips: 0$", 351, 110);

        setImages(6, s, 629, 130);
        setImages(7, s, 679, 130);
        setTxtNames(3, "3", 629, 80);
        setTxtChips(3, "Chips: 0$", 629, 110);

        setImages(8, s, 810, 290);
        setImages(9, s, 860, 290);
        setTxtNames(4, "4", 930, 310);
        setTxtChips(4, "Chips: 0$", 930, 340);

        setImages(10, s, 629, 450);
        setImages(11, s, 679, 450);
        setTxtNames(5, "4", 629, 550);
        setTxtChips(5, "Chips: 0$", 629, 580);

        setImages(12, s, 515, 290);
        setImages(13, s, 465, 290);
        setImages(14, s, 415, 290);
        setImages(15, s, 565, 290);
        setImages(16, s, 615, 290);

        roundName.setFont(Font.font("Century Schoolbook", 35));
        roundName.setX(420);
        roundName.setY(40);

        txtPot.setFont(Font.font("Century Schoolbook", 30));
        txtPot.setX(470);
        txtPot.setY(250);

        StackPane holder = new StackPane();
        holder.getChildren().addAll(background, r, m, n);
        StackPane.setAlignment(r, Pos.TOP_LEFT);

        StackPane.setMargin(background, new Insets(0));
        StackPane.setMargin(r, new Insets(130, 0, 0, 156));
        StackPane.setMargin(m, new Insets(410, 0, 0, 900));
        StackPane.setMargin(n, new Insets(-400, 0, 0, 900));

        pane = new AnchorPane(holder);

        for (int i = 0; i < images.size(); i++)
            pane.getChildren().add(images.get(i));

        for (int i = 0; i < txtNames.size(); i++)
            pane.getChildren().add(txtNames.get(i));

        for (int i = 0; i < txtChips.size(); i++)
            pane.getChildren().add(txtChips.get(i));

        for (int i = 0; i < 7; i++)
            pane.getChildren().add(message.get(i));

        for (int i = 0; i < 7; i++)
            pane.getChildren().add(message2.get(i));

        pane.getChildren().addAll(roundName, txtPot);
    }

    public Table(int id) {
        message = new Vector<>();
        message2 = new Vector<>();
        stg = new Stage();

        for (int i = 0; i < 7; i++) {
            message.add(new Text());
            message.get(i).setFont(Font.font("Century Schoolbook", 20));
            message.get(i).setX(940);
            message.get(i).setY(460 + 30 * i);
        }

        for (int i = 0; i < 7; i++) {
            message2.add(new Text());
            message2.get(i).setFont(Font.font("Century Schoolbook", 20));
            message2.get(i).setX(940);
            message2.get(i).setY(50 + 30 * i);
        }

        this.id = id;
        roundId = 0;
        df.setRoundingMode(RoundingMode.CEILING);
        reset();
    }

    void setImages(int id, String s, int x, int y) {
        images.get(id).setImage(new Image(s));
        images.get(id).setFitWidth(50);
        images.get(id).setPreserveRatio(true);
        images.get(id).setX(x);
        images.get(id).setY(y);
    }

    void setTxtNames(int id, String s, int x, int y) {
        txtNames.get(id).setFont(Font.font("Century Schoolbook", 20));
        txtNames.get(id).setText(s);
        txtNames.get(id).setX(x);
        txtNames.get(id).setY(y);
    }

    void setTxtChips(int id, String s, int x, int y) {
        txtChips.get(id).setFont(Font.font("Century Schoolbook", 20));
        txtChips.get(id).setText(s);
        txtChips.get(id).setX(x);
        txtChips.get(id).setY(y);
    }

    public void setMessage(Vector<String> vs) {
        for (Text t : message)
            t.setText("");

        for (Text t : message2)
            t.setText("");

        int i;
        for (i = 0; i < vs.size() && i < 6; i++)
            message.get(i).setText(vs.get(i));

        if (i < vs.size() && i >= 6) {

            for (int j = 0; i + j < vs.size() && j < 6; j++)
                message2.get(j).setText(vs.get(i + j));
        }
    }

    public void show() {
        String tempStr = df.format(players.get(0).getPot());
        roundName.setText(roundNames.get(roundId));
        txtPot.setText("Pot: " + Double.parseDouble(tempStr) + "$");

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getIsLoser()) {
                setImages(2 * i, "back.bmp", 1280, 650);
                setImages(2 * i + 1, "back.bmp", 1280, 650);
                setTxtChips(i, "", 1280, 650);
                setTxtNames(i, "", 1280, 650);
            }
            else if (players.get(i).getHasFolded()) {
                setImages(2 * i, "back.bmp", 1280, 650);
                setImages(2 * i + 1, "back.bmp", 1280, 650);
            }
            else if (i == id) {
                Vector<Card> temp = players.get(i).getHole();
                images.get(2 * i).setImage(new Image(temp.get(0).CardToImage()));
                images.get(2 * i + 1).setImage(new Image(temp.get(1).CardToImage()));
            }

            if (players.get(i).getIsCurrent()) {
                txtNames.get(i).setFill(Color.BISQUE);
                txtChips.get(i).setFill(Color.BISQUE);
                txtNames.get(i).setFont(Font.font("Cooper Black", 20));
                txtChips.get(i).setFont(Font.font("Cooper Black", 20));
            }

            else {
                txtNames.get(i).setFill(Color.BLACK);
                txtChips.get(i).setFill(Color.BLACK);
                txtNames.get(i).setFont(Font.font("Century Schoolbook", 20));
                txtChips.get(i).setFont(Font.font("Century Schoolbook", 20));
            }

            tempStr = df.format(players.get(i).getChips());
            txtChips.get(i).setText("Chips: " + Double.parseDouble(tempStr) + "$");

            tempStr = players.get(i).getName() + (players.get(i).getIsDealer() ? " (D)" : "");
            txtNames.get(i).setText(tempStr);
        }

        for (int i = players.size(); i < 6; i++) {
            setImages(2 * i, "back.bmp", 1280, 650);
            setImages(2 * i + 1, "back.bmp", 1280, 650);
            setTxtChips(i, "", 1280, 650);
            setTxtNames(i, "", 1280, 650);
        }

        Vector<Card> tempCommunity = players.get(id).getCommunity();

        for (int i = 0; i < tempCommunity.size(); i++)
            images.get(12 + i).setImage(new Image(tempCommunity.get(i).CardToImage()));

        if (pane.getScene() != null)
            pane.getScene().setRoot(new HBox());

        scene = new Scene(pane, 1260, 630);

        stg.close();
        stg = new Stage();

        stg.setScene(scene);
        stg.setTitle(players.get(id).getName());
        stg.setResizable(false);
        stg.show();
    }

    public void showAll() {
        String tempStr = df.format(players.get(0).getPot());
        roundName.setText(roundNames.get(roundId));
        txtPot.setText("Pot: " + Double.parseDouble(tempStr) + "$");

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getIsLoser()) {
                setImages(2 * i, "back.bmp", 1280, 650);
                setImages(2 * i + 1, "back.bmp", 1280, 650);
                setTxtChips(i, "", 1280, 650);
                setTxtNames(i, "", 1280, 650);
            }
            else if (players.get(i).getHasFolded()) {
                setImages(2 * i, "back.bmp", 1280, 650);
                setImages(2 * i + 1, "back.bmp", 1280, 650);
            }
            else {
                Vector<Card> temp = players.get(i).getHole();
                images.get(2 * i).setImage(new Image(temp.get(0).CardToImage()));
                images.get(2 * i + 1).setImage(new Image(temp.get(1).CardToImage()));

                tempStr = df.format(players.get(i).getChips());
                txtChips.get(i).setText("Chips: " + Double.parseDouble(tempStr) + "$");

                tempStr = players.get(i).getName() + (players.get(i).getIsDealer() ? " (D)" : "");
                txtNames.get(i).setText(tempStr);
            }

            txtNames.get(i).setFill(Color.BLACK);
            txtChips.get(i).setFill(Color.BLACK);
            txtNames.get(i).setFont(Font.font("Century Schoolbook", 20));
            txtChips.get(i).setFont(Font.font("Century Schoolbook", 20));
        }

        for (int i = players.size(); i < 6; i++) {
            setImages(2 * i, "back.bmp", 1280, 650);
            setImages(2 * i + 1, "back.bmp", 1280, 650);
            setTxtChips(i, "", 1280, 650);
            setTxtNames(i, "", 1280, 650);
        }

        Vector<Card> tempCommunity = players.get(id).getCommunity();

        for (int i = 0; i < tempCommunity.size(); i++)
            images.get(12 + i).setImage(new Image(tempCommunity.get(i).CardToImage()));

        if (pane.getScene() != null)
            pane.getScene().setRoot(new HBox());

        scene = new Scene(pane, 1260, 630);

        stg.close();
        stg = new Stage();

        stg.setScene(scene);
        stg.setTitle(players.get(id).getName());
        stg.setResizable(false);
        stg.show();
    }
}
