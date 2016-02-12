package com.awesome.awesomefifteenpuzzle;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;


/**
 * <h1>An Awesome Fifteen Puzzle</h1>
 * The android interface of the 15-puzzle by team Awesome2.0 . The user can play the game by
 * pressing the different tiles, he can also ask for a shuffle tha will display a shuffled
 * puzzle computed by the webService. Finally the user could also ask for the puzzle to be solved
 * online.
 *
 * @author Team Awesome 2.0
 * @version 1.0
 * @since 12/2/2016
 */
public class FifteenPuzzle extends Activity implements View.OnClickListener {

    //The difference between the positions od neighboring tiles
    final int X_DIFF = 264;
    final int Y_DIFF = 144;

    //Array of all game tiles
    Button[] yourBtns = new Button[16];
    int[] buttonID = {R.id.block1, R.id.block2, R.id.block3, R.id.block4
            , R.id.block5, R.id.block6, R.id.block7, R.id.block8, R.id.block9, R.id.block10
            , R.id.block11, R.id.block12, R.id.block13, R.id.block14, R.id.block15, R.id.block0};

    int webService = 0; // 1 for shuffle & 2 for solve

    Board board = Board.getInstance();
    WebServiceConnection web_service = WebServiceConnection.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifteen_puzzle);

        board.setActivity(FifteenPuzzle.this);

        for (int i = 0; i < 16; i++) {
            yourBtns[i] = (Button) findViewById(buttonID[i]);
            yourBtns[i].setOnClickListener(this);
        }

        //Used for the internet connection
        final ConnectivityManager connMan = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Button yourBtn;
        yourBtn = (Button) findViewById(R.id.shuffle);
        yourBtn.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        webService = 1;
                        System.out.println(web_service.shuffleWeb(connMan));
                    }
                });

        yourBtn = (Button) findViewById(R.id.solve);
        yourBtn.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        webService = 2;
                        System.out.println(web_service.solveWeb(connMan));
                    }
                });
    }

    public void onClick(View v) {
        String text_inButton;
        //Identifies the button pressed by the text written on it
        text_inButton = (String) ((TextView) v).getText();

        //If no text then it is the blank tile
        if (text_inButton.equals("")) System.out.println("Space pressed");
        else if (text_inButton.equals("solve") || text_inButton.equals("shuffle")) return;
        else {
            //gets the number of the button
            int number = Integer.parseInt(text_inButton);
            System.out.println("Block " + number + " pressed");
            moveTile(number);
        }
    }

    /**
     * This method takes a list of tiles and move them by calling {@link #moveTile(int)}
     * for each element
     * @param moves integer array of values of tiles to be moved
     */
    public void multipleMoves(int[] moves){
        for(int i = 0; i < moves.length; i++) {
            moveTile(moves[i]);
            //I was trying to put some delay between each move
            //findViewById(R.id.main_layout).invalidate();
           // SystemClock.sleep(1000);
        }
    }

    /**
     * moveTile calls the {@link Board#move(int)} from the board and change the display accordingly
     * @param number value of the tile to be moved
     */
    public void moveTile(int number) {
        char movement = board.move(number);
        switch (movement) {
            case 'd':
                yourBtns[number - 1].animate().translationYBy(Y_DIFF);
                yourBtns[15].animate().translationYBy(-Y_DIFF);
                System.out.println("Block moved");
                break;
            case 'u':
                yourBtns[number - 1].animate().translationYBy(-Y_DIFF);
                yourBtns[15].animate().translationYBy(Y_DIFF);
                System.out.println("Block moved");
                break;
            case 'r':
                yourBtns[number - 1].animate().translationXBy(X_DIFF);
                yourBtns[15].animate().translationXBy(-X_DIFF);
                System.out.println("Block moved");
                break;
            case 'l':
                yourBtns[number - 1].animate().translationXBy(-X_DIFF);
                yourBtns[15].animate().translationXBy(X_DIFF);
                System.out.println("Block moved");
                break;
            case 'f':
                System.out.println("Block not beside the blank");
                break;
        }
    }

    /**
     * updateDisplay by offsets between current state and goal state of the puzzle uses the
     * {@link Board#find(int)} method in the {@link Board}
     *
     * @param displacements 2d array of number of movements of each tile from the current state to
     *                      the new state
     * @param temp goal state of the puzzle
     */
    public void updateDisplay(int[][] displacements, int[] temp) {
        int current_tile;
        int current_pos;
        for (int i = 0; i < 16; i++) {
            current_tile = temp[i];
            current_pos = board.find(current_tile);
            if (current_tile == 0) {
                yourBtns[15].animate().translationXBy(displacements[current_pos][0] * X_DIFF);
                yourBtns[15].animate().translationYBy(displacements[current_pos][1] * Y_DIFF);
            } else {
                yourBtns[current_tile - 1].animate().translationXBy(displacements[current_pos][0] * X_DIFF);
                yourBtns[current_tile - 1].animate().translationYBy(displacements[current_pos][1] * Y_DIFF);
            }
        }
    }
}
