package com.example.riskgamestate;
/**
 * MainActivity
 * Sets layouts and runs tests.
 *
 * @author Phi Nguyen, Dylan Kramis, Charlie Benning
 * @version 10/6/2021
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * onCreate
     * Runs upon startup, sets up the button used to tests.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializes layout elements
        Button testButton = findViewById(R.id.runTest);
        TextView testView = findViewById(R.id.testView);
        testButton.setOnClickListener(this);

    }

    /**
     * onClick
     * Runs tests when the button is clicked.
     *
     * @param view button clicked
     */
    @Override
    public void onClick(View view) {

        // switches to a content view with the output log
        setContentView(R.layout.output_log);
        EditText outputText = findViewById(R.id.outputText);


        // runs tests
        outputText.append("Starting tests.\n");

        // initializes game state
        outputText.append("Initializing game state.\n");
        RiskGameState firstInstance = new RiskGameState();

        // retrieves territories
        outputText.append("Getting territories.\n");
        ArrayList<Territory> territories = firstInstance.getT();

        // gives player 1 all of north america for testing purposes
        for (int i = 0; i < 9; i++) {
            firstInstance.getT().get(i).setOwner(1);
        }

        // gives player 2 all of south america for testing purposes
        for (int i = 9; i < 13; i++) {
            firstInstance.getT().get(i).setOwner(2);
        }

        // prints initial game state
        outputText.append(firstInstance.toString());

        // deploys 5 troops to alaska
        outputText.append("Deploying 5 troops to Alaska.\n");
        firstInstance.deploy(firstInstance.getT().get(0), 5);
        firstInstance.nextTurn();

        // prints game state
        outputText.append(firstInstance.toString());

        // copying game state
        outputText.append("Copying game state.\n");
        RiskGameState secondInstance = new RiskGameState(firstInstance);

        // attacks venezuela from central america
        outputText.append("Attacking Venezuela from Central America.\n");
        firstInstance.attack(firstInstance.getT().get(8), firstInstance.getT().get(9));
        firstInstance.nextTurn();

        // prints game state
        outputText.append(firstInstance.toString());

        // fortifies central america from eastern united states with 7 troops
        firstInstance.fortify(firstInstance.getT().get(7),firstInstance.getT().get(8),7 );

        // prints game states
        outputText.append(firstInstance.toString());
        outputText.append(secondInstance.toString());
    }
}