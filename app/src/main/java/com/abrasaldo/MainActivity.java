package com.abrasaldo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout_name,
                            textInputLayout_amount,
                            textInputLayout_guess1,
                            textInputLayout_guess2,
                            textInputLayout_guess3,
                            textInputLayout_guess4,
                            textInputLayout_guess5,
                            textInputLayout_guess6;
    private Vector<TextInputLayout> textInputLayouts = new Vector<>();
    private TextView textView_title, textView_lotto, textView_winners, textView_message;
    private Vector<Integer> guesses = new Vector<>();
    private Button button_go, button_close;
    private Random random = new Random();

    private DecimalFormat formatter = new DecimalFormat("#,###,##0.00");
    private int otherWinners = 0;
    private double jackpotPrize = 50000000.00;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputLayout_name = findViewById(R.id.textLayout_name);
        textInputLayout_amount = findViewById(R.id.textLayout_amount);

        textView_title = findViewById(R.id.textView_title);
        textView_lotto = findViewById(R.id.textView_generated);
        textView_winners = findViewById(R.id.textView_winners);
        textView_message = findViewById(R.id.textView_message);

        textInputLayout_guess1 = findViewById(R.id.textLayout_guess1);
        textInputLayout_guess2 = findViewById(R.id.textLayout_guess2);
        textInputLayout_guess3 = findViewById(R.id.textLayout_guess3);
        textInputLayout_guess4 = findViewById(R.id.textLayout_guess4);
        textInputLayout_guess5 = findViewById(R.id.textLayout_guess5);
        textInputLayout_guess6 = findViewById(R.id.textLayout_guess6);

        textInputLayouts.add(textInputLayout_guess1);
        textInputLayouts.add(textInputLayout_guess2);
        textInputLayouts.add(textInputLayout_guess3);
        textInputLayouts.add(textInputLayout_guess4);
        textInputLayouts.add(textInputLayout_guess5);
        textInputLayouts.add(textInputLayout_guess6);

        textView_winners.setVisibility(View.INVISIBLE);
        textView_message.setVisibility(View.INVISIBLE);
        textView_lotto.setVisibility(View.INVISIBLE);

        button_go = findViewById(R.id.button_go);
        button_close = findViewById(R.id.button_close);

        button_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean proceed = true;
                if(!validateName()) {
                    proceed = false;
                }
                if(!validateGuess()) {
                    proceed = false;
                }
                if(!validateAmount()) {
                    proceed = false;
                }
                if(!guessNotRepeating()) {
                    proceed = false;
                }
                if(proceed) {
                    checkWinner();
                }
            }
        });
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
    private boolean validateName() {
        if(textInputLayout_name.getEditText().getText().toString().isEmpty()) {
            textInputLayout_name.setError("This field can't be empty");
            return false;
        }
        else {
            textInputLayout_name.setError(null);
            return true;
        }
    }
    private boolean validateAmount() {
        if(textInputLayout_amount.getEditText().getText().toString().isEmpty()) {
            textInputLayout_amount.setError("This field can't be empty");
            return false;
        }
        else {
            textInputLayout_amount.setError(null);
            if(!textInputLayout_amount.getEditText().getText().toString().equals("20")) {
                textInputLayout_amount.setError("You must enter ₱20.00");
                return false;
            }
            else {
                return true;
            }
        }
    }
    private boolean validateGuess() {
        boolean valid = true;
        guesses.clear();
        for(int i = 0; i < textInputLayouts.size(); i++) {
            if(textInputLayouts.get(i).getEditText().getText().toString().isEmpty()) {
                textInputLayouts.get(i).setError("This field can't be empty");
                valid = false;
            }
            else {
                textInputLayouts.get(i).setError(null);
                int numberGuess = Integer.parseInt(textInputLayouts.get(i).getEditText().getText().toString());
                if(numberGuess > 58 || numberGuess == 0) {
                    textInputLayouts.get(i).setError("Must be 1 to 58 only");
                    valid = false;
                }
                else {
                    guesses.add(numberGuess);
                }
            }
        }
        return valid;
    }
    private boolean guessNotRepeating() {
        boolean valid = true;
        for(int i = 0; i < guesses.size(); i++) {
            for(int j = i + 1; j < guesses.size(); j++) {
                if(guesses.get(i) == guesses.get(j)) {
                    for(int l = 0; l < textInputLayouts.size(); l++) {
                        if(textInputLayouts.get(l).getEditText().getText().toString().equals(Integer.toString(guesses.get(j)))) {
                            textInputLayouts.get(l).setError("Number mustn't repeat");
                            valid = false;
                        }
                    }
                }
            }
        }
        return valid;
    }
    private Vector<Integer> lottoGeneratedNumbers() {
        Vector<Integer> lotto = new Vector<>();
        String lottoGeneratedNumber = "Result\n";
        int numberOfResults = 0;
        while(numberOfResults < 6) {
            int number = random.nextInt(58) + 1;

            if(!lotto.contains(number)) {
                lotto.add(number);
                lottoGeneratedNumber += "[" + number + "] ";
                numberOfResults++;
            }
        }
        textView_winners.setVisibility(View.VISIBLE);
        textView_message.setVisibility(View.VISIBLE);
        textView_lotto.setVisibility(View.VISIBLE);

        textView_lotto.setText(lottoGeneratedNumber);
        return lotto;
    }
    private void checkWinner() {
        int pairCount = 0;
        jackpotPrize += 20;
        String message = "";
        double totalAmountWon = 0;
        int winner = 0;

        Vector<Integer> lotto = lottoGeneratedNumbers();
        Collections.sort(lotto);
        Collections.sort(guesses);

        for(int i = 0; i < 6; i++) {
            if(lotto.get(i) == guesses.get(i)) {
                pairCount++;
            }
        }
        otherWinners = random.nextInt(5);
        switch (pairCount) {
            case 6:
                totalAmountWon = jackpotPrize / otherWinners;
                jackpotPrize = jackpotPrize - totalAmountWon;
                message = "Congrats! You have won ₱" + formatter.format(totalAmountWon);
                break;
            case 5:
                totalAmountWon = (jackpotPrize / 2) / otherWinners;
                jackpotPrize = jackpotPrize - totalAmountWon;
                message = "Congrats! You have won ₱" + formatter.format(totalAmountWon);
                winner = 1;
                break;
            case 4:
                totalAmountWon = (jackpotPrize / 5) / otherWinners;
                jackpotPrize = jackpotPrize - totalAmountWon;
                message = "Congrats! You have won ₱" + formatter.format(totalAmountWon);
                winner = 1;
                break;
            case 3:
                totalAmountWon = 5000.00;
                jackpotPrize = jackpotPrize - totalAmountWon;
                message = "Congrats! You have won ₱" + formatter.format(totalAmountWon);
                winner = 1;
                break;
            case 2:
                message = "Congrats! You have won! You can play again 5 lotto panels";
                winner = 1;
                break;
            default:
                message = "Sorry, you did not win. :(";
        }
        textView_winners.setText("Total Winners: " + (otherWinners + winner));
        textView_message.setText(message);
    }
}