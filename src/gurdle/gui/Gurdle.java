package gurdle.gui;

import gurdle.CharChoice;
import gurdle.Model;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import util.Observer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.HashMap;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * The graphical user interface to the Wordle game model in
 * {@link Model}.
 *
 * @author Aniruddha Roy
 * ar7475
 */
public class Gurdle extends Application
        implements Observer< Model, String > {

    private GridPane grid = new GridPane();
    private Model model;
    private GridPane keyboard;
    private HashMap<Character, Button> keyLabelMap;
    private Label left;
    private Label top;

    @Override public void init() {

        System.out.println( "TODO - init code here" );
        this.left = new Label("#guesses: ");
        this.model = new Model();
        this.model.addObserver( this );
        this.grid = new GridPane();
        this.keyboard = new GridPane();
        this.keyLabelMap = new HashMap<>();
        this.top = new Label("Make a guess!");
        model.newGame();
    }

    /**
     * This method is called when the JavaFX application starts, and is used to initialize the main window
     * and set up the user interface.
     *
     * @param mainStage The main stage (window) of the JavaFX application.
     */
    @Override
    public void start( Stage mainStage ) {

        BorderPane bPane = new BorderPane();

        GridPane topGrid = new GridPane();
        topGrid.setHgap(50);
        topGrid.setAlignment(Pos.CENTER);


        Label right = new Label("");

        topGrid.add(left, 0, 0);
        topGrid.add(top, 1, 0);
        topGrid.add(right, 2, 0);

        GridPane.setHalignment(left, HPos.LEFT);
        GridPane.setHalignment(top, HPos.CENTER);
        GridPane.setHalignment(right, HPos.RIGHT);

        bPane.setTop(topGrid);


        //center
        for (int r = 0; r < 5; r++){
            for (int c = 0; c < 6; c++){

                TextField wordInput = new TextField();
                wordInput.setStyle( """
                            -fx-padding: 10;
                            -fx-border-style: solid inside;
                            -fx-border-width: 2;
                            -fx-border-insets: 5;
                            -fx-border-radius: 2;
                            -fx-border-color: black;
                """);


                grid.add(wordInput,r,c);
            }

        }
        grid.setAlignment(Pos.CENTER);
        bPane.setCenter(grid);

        //bottom
        BorderPane bPane2 = new BorderPane();
        bPane.setBottom(bPane2);


        HBox hbox2 = new HBox();
        Button newGame = new Button("NEW GAME");
        Button cheat = new Button("CHEAT");
        hbox2.getChildren().addAll(newGame, cheat);
        hbox2.setAlignment(Pos.CENTER);
        bPane2.setBottom(hbox2);

        newGame.setOnAction((event) ->  {model.newGame();
            });

        cheat.setOnAction((actionEvent) -> {
            String secretWord = model.secret();
            right.setText("secret: " + secretWord);
        });
        bPane2.setPadding(new Insets(10)); // 10 pixels of padding around the grid


        Button enter = new Button("ENTER");
        enter.setOnAction((event) -> { model.confirmGuess();});

        bPane2.setRight(enter);

        // Define the QWERTY keyboard layout as a two-dimensional array
        char[][] qwertyLayout = {
                {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'},
                {'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'},
                {'Z', 'X', 'C', 'V', 'B', 'N', 'M'}
        };

        for (int row = 0; row < qwertyLayout.length; row++) {
            // Loop through each letter in this row
            for (int col = 0; col < qwertyLayout[row].length; col++) {
                // Create a new button for the letter
                Button button = new Button(String.valueOf(qwertyLayout[row][col]));

                int finalRow = row;
                int finalCol = col;
                button.setOnAction(event -> model.enterNewGuessChar(qwertyLayout[finalRow][finalCol]));


                keyboard.add(button, col, row);
                button.setPrefSize(40, 40);
                keyLabelMap.put(qwertyLayout[row][col], button);

            }
        }

        bPane2.setCenter(keyboard);

        Scene scene = new Scene(bPane);

        mainStage.setScene( scene );

        mainStage.show();
    }


    /**
     * Updates the model with the given message, triggering any necessary
     * changes in the internal state of the model.
     *
     * @param model The model to be updated.
     * @param message The message containing information to update the model.
     */
    @Override
    public void update( Model model, String message ) {
        this.top.setText(message);

        this.left.setText("#guesses: "+ model.numAttempts());
        System.out.println("""
                TODO
                Here is where the model is queried
                and the view is updated.""");

        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 5; c++) {

                TextField ch = new TextField(model.get(r, c).toString());
                ch.setAlignment(Pos.CENTER);
                for (Button pinapple : keyLabelMap.values()){
                    pinapple.setBackground(new Background(new BackgroundFill(
                            null, null, null)));
                }

                grid.add(ch, c, r);

                if (model.get(r, c).getStatus() == CharChoice.Status.WRONG_POS) {
                    ch.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, null, null)));
                    if (model.usedLetter(model.get(r, c).getChar())){
                    keyLabelMap.get(model.get(r, c).getChar()).setBackground(new Background(new BackgroundFill(
                            Color.BURLYWOOD, null, null)));}

                }
                else if (model.get(r, c).getStatus() == CharChoice.Status.RIGHT_POS) {
                    ch.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
                    if (model.usedLetter(model.get(r, c).getChar())){
                        keyLabelMap.get(model.get(r, c).getChar()).setBackground(new Background(new BackgroundFill(
                                Color.LIGHTGREEN, null, null)));}

                }
                else if (model.get(r, c).getStatus() == CharChoice.Status.WRONG) {
                    ch.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
                    if (model.usedLetter(model.get(r, c).getChar())){
                        keyLabelMap.get(model.get(r, c).getChar()).setBackground(new Background(new BackgroundFill(
                                Color.LIGHTGRAY, null, null)));}
                }
                else if (model.get(r, c).getStatus() == CharChoice.Status.EMPTY) {
                    ch.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

                }
            }
        }


    }
    /**
     * The main entry point of the Java program.
     * This method is automatically called when the Java program is executed.
     * It serves as the starting point of the program's execution.
     *
     * @param args An array of String arguments passed to the program.
     *             These arguments can be accessed within the method body
     *             to perform program logic.
     */
    public static void main( String[] args ) {
        if ( args.length > 1 ) {
            System.err.println( "Usage: java Gurdle [1st-secret-word]" );
        }
        Application.launch( args );
    }
}