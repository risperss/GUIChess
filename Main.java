import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
    Gaetano Rispoli
    ICS3U Culminating Project
    Chess. No modifications to the rules.
*/
public class Main extends Application {
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chess");
        primaryStage.setResizable(false);

        /*
        was gonna do a main menu, but javafx made sure it was not meant to be. the way that it looks
        now is good enough for me. If you want to play cpu vs cpu or human vs human or even play as black
        you have to do it the good old fashioned way and do it in the code. maybe something i add in the summer...
        who knows

        also, the formatting is weird becase I accidentally formatted a document using the auto formatter, and 
        then just applied it to all so that they would all look the same
        */
        Player whitePlayer = new HumanPlayer(ChessColor.WHITE);
        Player blackPlayer = new CPUPlayer(ChessColor.BLACK);

        Game game = new Game(whitePlayer, blackPlayer);

        Scene scene = new Scene(game.getBoard().getGraphicBoard(), 400.0, 400.0);

        Thread gameThread = new Thread(game);

        gameThread.start();

        primaryStage.setScene(scene);
        primaryStage.show();  
    }
}