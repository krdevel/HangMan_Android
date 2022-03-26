package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
   A simple Hangman program for Android
 */

// The main/launcher class
public class GameActivity extends AppCompatActivity {
    private Cruncher cruncher;
    private ImageView imageView;
    private TextView guessesRemainingTextView;
    private TextView correctWordTextView;
    private TextView currentGuessLetterTextView;
    private EditText enterLetterTextview;
    private TextView winOrLosetextView;
    private LinearLayout guessLayout;
    private LinearLayout resultLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        startGame();
    }

    public void startGame() {
        setUp();
    }

    private void setUp() {
        imageView = findViewById(R.id.image_view);
        guessesRemainingTextView = findViewById(R.id.guesses_remaining_textView);
        correctWordTextView = findViewById(R.id.correct_word_textView);
        enterLetterTextview = findViewById(R.id.enter_letter_textview);
        currentGuessLetterTextView = findViewById(R.id.current_guess_textView);
        currentGuessLetterTextView.setText("");
        winOrLosetextView = findViewById(R.id.win_or_loose_textView);
        guessLayout = findViewById(R.id.guess_container);
        resultLayout = findViewById(R.id.result_container);
        revealGuessLayout();
        cruncher = new Cruncher(this);
        cruncher.setImageView(imageView);
        cruncher.setGuessesRemainingTextView(guessesRemainingTextView);
        cruncher.setCorrectWordTextView(correctWordTextView, this);
        cruncher.setCurrentGuessLetterTextView(currentGuessLetterTextView);
        cruncher.setWinOrLosetextView(winOrLosetextView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int itemID = item.getItemId();
        switch (itemID) {
            case R.id.about_menu_item: {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void guessButtonMethod(View view) {
        String guessedLetter = enterLetterTextview.getText().toString();
        cruncher.recieveOneLetter(guessedLetter, this);
        enterLetterTextview.setText("");
        enterLetterTextview.requestFocus();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void revealGuessLayout() {
        guessLayout.setVisibility(View.VISIBLE);
        resultLayout.setVisibility(View.GONE);
    }

    public void revealResultLayout() {
        guessLayout.setVisibility(View.GONE);
        resultLayout.setVisibility(View.VISIBLE);
    }

    public void playAgainButtonMethod(View view) {
        startGame();
    }

    public void quitButtonMethod(View view) {
        finish();
//      finishAffinity(); // Close all activites
//      System.exit(0);  // Releasing resources
    }

}