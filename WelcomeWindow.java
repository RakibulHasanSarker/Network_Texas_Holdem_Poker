import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.image.*;

public class WelcomeWindow
{
    public static void show()
    {
        Stage stg = new Stage();
        ImageView wp = new ImageView("Wallpaper.png");
        wp.setFitHeight(650);
        wp.setPreserveRatio(true);

        Button btn = new Button("Play Game");
        btn.setFont(Font.font("Century Schoolbook", 30));
        btn.setMinWidth(230);
        btn.setMinHeight(70);
        btn.setOnAction(e -> stg.close());
        btn.setDefaultButton(true);

        Text txt = new Text("Network Texas Hold'em");
        txt.setFont(Font.font("Century Schoolbook", 50));
        txt.setFill(Color.BISQUE);
        txt.setX(300);
        txt.setY(100);

        AnchorPane pane = new AnchorPane();
        pane.getChildren().addAll(wp, btn, txt);
        AnchorPane.setTopAnchor(btn, 572.0);
        AnchorPane.setLeftAnchor(btn, 5.0);

        Scene sc = new Scene(pane, 1145, 640);
        stg.setTitle("Poker Game");
        stg.setResizable(false);
        stg.setScene(sc);
        stg.showAndWait();
    }
}
