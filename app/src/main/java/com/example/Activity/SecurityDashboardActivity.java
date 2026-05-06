package com.example.Activity;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import com.example.parkingappclient.R;

public class SecurityDashboardActivity extends AppCompatActivity {

    private MaterialCardView entryCard;
    private MaterialCardView exitCard;
    private String assignedGate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_dashboard);

        entryCard = findViewById(R.id.cardVehicleEntry);
        exitCard = findViewById(R.id.cardVehicleExit);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();

        assignedGate = getIntent().getStringExtra("ASSIGNED_GATE");
        if (assignedGate == null || assignedGate.isEmpty()) {
            assignedGate = "G1"; // Default fallback
        }
        entryCard = findViewById(R.id.cardVehicleEntry);
        exitCard = findViewById(R.id.cardVehicleExit);
        if (entryCard != null) {
            setupCoolTouchAnimation(entryCard);
            entryCard.setOnClickListener(v -> {
                Intent intent = new Intent(SecurityDashboardActivity.this, VehicleEntryActivity.class);
                intent.putExtra("ASSIGNED_GATE", assignedGate);
                startActivity(intent);
            });
        }
        if (exitCard != null) {
            setupCoolTouchAnimation(exitCard);
            exitCard.setOnClickListener(v -> {
                Intent intent = new Intent(SecurityDashboardActivity.this, VehicleExitActivity.class);
                startActivity(intent);
            });
        }
    }

    /**
     * Adds a "cool" tactile scale animation when the staff touches the card.
     */
    private void setupCoolTouchAnimation(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();
                    break;
            }
            return false; // Allows onClick to still fire
        });
    }
}