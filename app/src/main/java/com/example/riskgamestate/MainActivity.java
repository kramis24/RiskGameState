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
     * MainActivity
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

    @Override
    public void onClick(View view) {

        // switches to a content view with the output log
        setContentView(R.layout.output_log);
        EditText outputText = findViewById(R.id.outputText);


        // runs tests
        outputText.append("Starting tests.\n");
        RiskGameState firstInstance = new RiskGameState();
        ArrayList<Territory> territories = firstInstance.getT();
        firstInstance.nextTurn();
        firstInstance.nextTurn();
        firstInstance.nextTurn();
        RiskGameState secondInstance = new RiskGameState(firstInstance);
        outputText.setText(firstInstance.toString());
        outputText.append(secondInstance.toString());
    }
}