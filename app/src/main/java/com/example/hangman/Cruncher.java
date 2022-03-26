package com.example.hangman;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

// The "Game Engine" class
public class Cruncher {
    private GameActivity gameActivity; //For hideKeyboard()
    private ImageView imageView;
    private ArrayList<Integer> imagegesList;
    private TextView guessesRemainingTextView;
    private String correctWord;
    private StringBuilder wordSoFar;
    private ArrayList<String> poolOfWords;
    private int guessCount = 9;
    private int currentGuessCount;
    private int guessesRemaining;
    private ArrayList<Character> usedLetters;
    private static Random randomGenerator;
    private TextView correctWordTextView;
    private TextView currentGuessLetterTextView;
    private TextView winOrLosetextView;

    // Constructor
    public Cruncher(GameActivity gameActivity) {
        randomGenerator = new Random();
        this.gameActivity = gameActivity;
        setupImagesList();
        fillPoolOfWords();
        setCorrectWord(randomWord());
        fillWordSoFarWithQuestionMarks();
        resetGuesses();
    }

    private void resetGuesses() {
        setGuessesRemaining(guessCount);
        currentGuessCount = 0;
        if(getGuessesRemainingTextView() != null) {
            getGuessesRemainingTextView().setText("" + guessCount);
        }
        if(getImageView() != null) {
            setFirstImage();
        }
    }

    private void setupImagesList() {
        imagegesList = new ArrayList() {{add(R.drawable.empty); add(R.drawable.minus_two); add(R.drawable.minus_one);
            add(R.drawable.one); add(R.drawable.two);add(R.drawable.three); add(R.drawable.four); add(R.drawable.five);
            add(R.drawable.six); add(R.drawable.seven);}};
    }

    private void setFirstImage() {
        getImageView().setImageResource(imagegesList.get(0));
    }

    private void setNextImage() {
        getImageView().setImageResource(imagegesList.get(currentGuessCount));
    }

    private void fillPoolOfWords() {
        poolOfWords = new ArrayList() {{
            add("Bom"); add("Sax"); add("Kant"); add("Pudel"); add("Skräp"); add("Cykel"); add("Glas"); add("Bråte"); add("Yngel");
        }};
    }

    public String randomWord() {
        String correctWord = poolOfWords.get(randomGenerator.nextInt(poolOfWords.size() + 1));
        return correctWord;
    }

    private void fillWordSoFarWithQuestionMarks() {
        wordSoFar = new StringBuilder();
        for (int i= 0; i<correctWord.length(); i++) {
            wordSoFar.append("?");
        }
    }

    // Handle guesses from the user.
    public void recieveOneLetter(String letter, Context context) {
        if(letter.length() > 1) {
            Toast.makeText(context, "Please enter a single letter!", Toast.LENGTH_SHORT).show();
        } else {
            if(letter.length() > 0) {
                Log.d("Tag_10", "length() > 0 " + letter);
                char singleCharacter = letter.charAt(0);
                if (correctWord.contains(letter)) {
                    int length = correctWord.length();
                    for (int i = 0; i < length; i++) {
                        if (singleCharacter == correctWord.charAt(i)) {
                            setWordSoFarCharAtIndex(i, singleCharacter);
                        }
                    }
                    guessWasIndeedRight(singleCharacter, context);
                } else {
                    guessWasIndeedWrong(singleCharacter, context);
                }
            }
            getCorrectWordTextView().setText(wordSoFar.toString());
        }
        checkWinOrLose(context);
    }

    private void checkWinOrLose(Context context) {
        if(getWordSoFar().equals(correctWord)) {
            youWin(context);
        } else if(getGuessesRemaining() == 0) {
            youLose(context);
        }
    }

    private void youWin(Context context) {
        declareWinOrLose(true, context);
    }

    private void youLose(Context context) {
        declareWinOrLose(false, context);
    }

    private void declareWinOrLose(Boolean hasWon, Context context) {
        gameActivity.hideKeyboard();
        gameActivity.revealResultLayout();
        if(hasWon) {
            getWinOrLosetextView().setText("YOU WON!");
        } else {
            getWinOrLosetextView().setText("You lose");
        }
    }

    private void guessWasIndeedRight(char singleCharacter, Context context) {
        printSingleLetter(singleCharacter, R.color.green, context);
    }

    private void guessWasIndeedWrong(char singleCharacter, Context context) {
        if(letterHasAlreadyBeenUsed(singleCharacter)) {
            Toast.makeText(context, "" + singleCharacter + " has already been used", Toast.LENGTH_SHORT).show();
        } else {
            addLetterToUsedWords(singleCharacter);
        }
        currentGuessCount++;
        decreaseGuessesRemaining();
        setNextImage();
        getGuessesRemainingTextView().setText("" + getGuessesRemaining());
        printSingleLetter(singleCharacter, R.color.red, context);
    }

    private Boolean letterHasAlreadyBeenUsed(char letter) {
        if(getUsedLetters().contains(letter)) {
            return true;
        } else {
            return false;
        }
    }

    private void printSingleLetter(char letter, int color, Context context) {
        getCurrentGuessLetterTextView().setTextColor(context.getResources().getColor(color));  //  R.color.green
        getCurrentGuessLetterTextView().setText(String.valueOf(letter));
    }

    public void setWordSoFarCharAtIndex(int index, char character) {
        this.wordSoFar.replace(index, index + 1, String.valueOf(character));
    }

    private void addLetterToUsedWords(char singleCharacter) {
        getUsedLetters().add(singleCharacter);
    }


    //Setters and getters:

    public void setCorrectWord(String correctWord) {
        this.correctWord = correctWord;
    }

    public TextView getCorrectWordTextView() {
        return correctWordTextView;
    }

    public void setCorrectWordTextView(TextView correctWordTextView, Context context) {
        if(correctWordTextView != null) {
            this.correctWordTextView = correctWordTextView;
            this.correctWordTextView.setText(getWordSoFar());
            //If you want to see the correct answer right at the start, uncomment the next line
//            Toast.makeText(context, correctWord, Toast.LENGTH_LONG).show();
        }
    }

    public String getWordSoFar() {
        return wordSoFar.toString(); //?
    }

    public TextView getCurrentGuessLetterTextView() {
        return currentGuessLetterTextView;
    }

    public void setCurrentGuessLetterTextView(TextView currentGuessLetterTextView) {
        this.currentGuessLetterTextView = currentGuessLetterTextView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getGuessesRemainingTextView() {
        return guessesRemainingTextView;
    }

    public void setGuessesRemainingTextView(TextView guessesRemainingTextView) {
        this.guessesRemainingTextView = guessesRemainingTextView;
        resetGuesses();
    }

    public void decreaseGuessesRemaining() {
        guessesRemaining = (guessCount - currentGuessCount);
    }

    public void setGuessesRemaining(int count) {
        guessesRemaining = count;
    }

    public int getGuessesRemaining() {
        return guessesRemaining;
    }

    public ArrayList<Character> getUsedLetters() {
        if(usedLetters == null) {
            usedLetters = new ArrayList<>();
        }
        return usedLetters;
    }

    public TextView getWinOrLosetextView() {
        return winOrLosetextView;
    }

    public void setWinOrLosetextView(TextView winOrLosetextView) {
        this.winOrLosetextView = winOrLosetextView;
    }

    

}
