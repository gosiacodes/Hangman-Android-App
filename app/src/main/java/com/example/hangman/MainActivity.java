package com.example.hangman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    // declaring variables
    TextView txtWordToGuess;
    String wordToGuess;
    String wordDisplayedString;
    char [] wordDisplayedCharArray;
    ArrayList<String> myListOfWords;
    EditText editInput;
    TextView txtLettersTried;
    String lettersTried;
    final String MESSAGE_LETTERS_TRIED = "Letters tried: ";
    TextView txtTriesLeft;
    String triesLeft;
    final String WINNING_MESSAGE = "You won!";
    final String LOSING_MESSAGE = "You lost!";
    Animation rotateAnim;
    Animation scaleAnim;
    Animation scaleAndRotateAnim;
    TableRow tableRowTriesLeft;
    TableRow tableRowReset;
    TextView txtTriesLeftResult;
    String TRIES_LEFT;
    int intTries = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("lifecycle","onCreate invoked "+getApplicationContext());

        // initialize variables
        myListOfWords = new ArrayList<>();
        txtWordToGuess = findViewById(R.id.txtWordToGuess);
        editInput = findViewById(R.id.editInput);
        txtLettersTried = findViewById(R.id.txtLettersTried);
        txtTriesLeft = findViewById(R.id.txtTriesLeft);
        rotateAnim = AnimationUtils.loadAnimation(this,R.anim.rotate);
        scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale);
        scaleAndRotateAnim = AnimationUtils.loadAnimation(this, R.anim.scale_and_rotate);
        scaleAndRotateAnim.setFillAfter(true);
        tableRowTriesLeft = findViewById(R.id.tableRowTriesLeft);
        tableRowReset = findViewById(R.id.tableRowReset);
        txtTriesLeftResult = findViewById(R.id.txtTriesLeftResult);

        // traverse database file and populate array list
        InputStream myInputStream = null;
        Scanner input = null;
        String word = "";

        try {
            myInputStream = getAssets().open("database_file.txt");
            input = new Scanner(myInputStream);
            while(input.hasNext()){
                word = input.next();
                myListOfWords.add(word);
                //Toast.makeText(MainActivity.this, word, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(MainActivity.this,
                    e.getClass().getSimpleName() + ": " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        finally {
            // close scanner
            if (input != null) {
                input.close();
            }
            // close InputStream
            try {
                if (myInputStream != null) {
                    myInputStream.close();
                }
                } catch(IOException e){
                    Toast.makeText(MainActivity.this,
                            e.getClass().getSimpleName() + ": " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

        }

        initializeGame();

        // setup the text changed listener
        editInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if there is some letter on the input field
                if (s.length() != 0) {
                    checkIfLetterIsInWord(s.charAt(0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // void
    void initializeGame() {
        // WORD
        // shuffle arrayList and get first element and then remove it
        Collections.shuffle(myListOfWords);
        wordToGuess = myListOfWords.get(0);
        myListOfWords.remove(0);

        // initialize char array
        wordDisplayedCharArray = wordToGuess.toCharArray();

        // add underscores
        for (int i = 1; i < wordDisplayedCharArray.length - 1; i++){
            wordDisplayedCharArray[i] = '_';
        }

        // reveal all occurrences of first character
        revealLetterInWord(wordDisplayedCharArray[0]);

        // reveal all occurrences of last character
        revealLetterInWord(wordDisplayedCharArray[wordDisplayedCharArray.length - 1]);

        // initialize a string from this char array
        wordDisplayedString = String.valueOf(wordDisplayedCharArray);

        // display word
        displayWordOnScreen();

        // INPUT
        // clear input field
        editInput.setText("");

        // LETTERS TRIED
        // initialize string for letters tried with a space
        lettersTried = " ";

        // display on screen
        txtLettersTried.setText(MESSAGE_LETTERS_TRIED);

        // TRIES LEFT
        // initialize the string for tries left
        triesLeft = " X X X X X X X";
        txtTriesLeft.setText(triesLeft);
        TRIES_LEFT = triesLeft;
    }

    // void
    void revealLetterInWord(char letter) {
        int indexOfLetter = wordToGuess.indexOf(letter);

        // loop to check if index is positive or 0
        while (indexOfLetter >= 0) {
            wordDisplayedCharArray[indexOfLetter] = wordToGuess.charAt(indexOfLetter);
            indexOfLetter = wordToGuess.indexOf(letter, indexOfLetter + 1);
        }

        // update string
        wordDisplayedString = String.valueOf(wordDisplayedCharArray);

    }

    // void
    void displayWordOnScreen() {
        String formattedString ="";
        for (char character : wordDisplayedCharArray) {
            formattedString += character + " ";
        }
        txtWordToGuess.setText(formattedString);
    }

    // void
    void checkIfLetterIsInWord(char letter) {
        // if the letter was found in the word to guess
        if (wordToGuess.indexOf(letter) >= 0) {
            // if the letter was not displayed yet
            if (wordDisplayedString.indexOf(letter) < 0){
                // animate
                txtWordToGuess.startAnimation(scaleAnim);
                // replace underscores with this letter
                revealLetterInWord(letter);
                // update changes on the screen
                displayWordOnScreen();
                // check if the game is won
                if (!wordDisplayedString.contains("_")){
                    tableRowTriesLeft.startAnimation(scaleAndRotateAnim);
                    txtTriesLeft.setText(WINNING_MESSAGE);
                }
            }
        }
        // else if the letter was not found in the word to guess
        else {
            // decrease the number of tries left and show it on screen
            decreaseAndDisplayTriesLeft();
            // check if the game is lost
            if (triesLeft.isEmpty()){
                tableRowTriesLeft.startAnimation(scaleAndRotateAnim);
                txtTriesLeft.setText(LOSING_MESSAGE);
                txtWordToGuess.setText(wordToGuess);
            }
        }

        // display the letter that was tried
        if (lettersTried.indexOf(letter) < 0){
            lettersTried += letter + ", ";
            String messageToDisplay = MESSAGE_LETTERS_TRIED + lettersTried;
            txtLettersTried.setText(messageToDisplay);
        }
    }

    // void
    void decreaseAndDisplayTriesLeft() {
        // if there are still some tries left
        if (!triesLeft.isEmpty()) {
            // animate
            txtTriesLeft.startAnimation(scaleAnim);
            // take out the last two characters from the string
            triesLeft = triesLeft.substring(0, triesLeft.length() - 2);
            txtTriesLeft.setText(triesLeft);
            TRIES_LEFT = triesLeft;
            intTries = intTries - 1;
        }
    }

    // void for reset-button
    public void resetGame(View view) {
        // start animation
        tableRowReset.startAnimation(rotateAnim);

        // clear animation on table row
        tableRowTriesLeft.clearAnimation();

        intTries = 7;

        // setup a new game
        initializeGame();
    }

    // void for check how many tries left
    public void checkTriesLeft(View view) {
        Intent intent = new Intent(MainActivity.this,CheckTriesLeftActivity.class);
        intent.putExtra("Value1", TRIES_LEFT);
        intent.putExtra("Value3", intTries);
        startActivityForResult(intent, 2);
    }

    // void for check hangman-picture progress
    public void checkHangman(View view) {
        Intent intent = new Intent(MainActivity.this,HangingActivity.class);
        intent.putExtra("Value1", TRIES_LEFT);
        intent.putExtra("Value3", intTries);
        startActivityForResult(intent, 2);
    }

    // void for activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2)
        {
            String triesMessage = data.getStringExtra("Value2");
            txtTriesLeftResult.setText(triesMessage);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked "+getApplication());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked "+getApplication());

        SharedPreferences prefs = getSharedPreferences("MyPreference", MODE_PRIVATE);

        String savedWord =  prefs.getString("WORD", wordDisplayedString);
        String savedLetters = prefs.getString("LETTERS", lettersTried);
        String savedTriesLeft = prefs.getString("TRIES_LEFT", triesLeft);

        int savedTries = prefs.getInt("INT_TRIES", intTries);

        txtWordToGuess.setText(savedWord);
        txtLettersTried.setText(savedLetters);
        txtTriesLeft.setText(savedTriesLeft);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked "+getApplication());

        SharedPreferences.Editor editor = getSharedPreferences("MyPreference", MODE_PRIVATE).edit();

        editor.putString("WORD", wordDisplayedString);
        editor.putString("LETTERS", lettersTried);
        editor.putString("TRIES_LEFT", triesLeft);

        editor.putInt("INT_TRIES", intTries);

        editor.apply();
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop invoked "+getApplication());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked "+getApplication());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle","onDestroy invoked "+getApplication());
    }
}
