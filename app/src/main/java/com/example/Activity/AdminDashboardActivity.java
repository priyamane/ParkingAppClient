package com.example.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.parkingappclient.R;
public class AdminDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        setTitle("Admin Dashboard - Full Access");
    }
}