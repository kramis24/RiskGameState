package com.example.riskgamestate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * @author Phi Nguyen, Dylan Kramis, Charlie Benning
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button testButton = findViewById(R.id.runTest);
        TextView testView = findViewById(R.id.testView);

     }

    @Override
    public void onClick(View view) {
        RiskGameState firstInstance = new RiskGameState();
        RiskGameState secondInstance = new RiskGameState(firstInstance);
    }
}