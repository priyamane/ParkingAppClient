package com.example.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Activity.MainActivity;
import com.example.parkingappclient.R;
import com.example.Activity.SecurityDashboardActivity;
import com.example.Activity.AdminDashboardActivity;
import com.example.api.RetrofitClient;
import com.example.model.LoginRequest;
import com.example.model.LoginResponse;
import com.example.model.TokenManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText usernameEditText;
    private EditText passwordEditText;
    private RadioGroup gateRadioGroup;
    private Button loginButton;
    private TextView errorMessageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEditText = findViewById(R.id.edit_text_username);
        passwordEditText = findViewById(R.id.edit_text_password);
        gateRadioGroup = findViewById(R.id.radioGroupLoginGate);
        loginButton = findViewById(R.id.button_login);
        errorMessageText = findViewById(R.id.error_message_text);

        if (gateRadioGroup != null) {
            gateRadioGroup.check(R.id.radioLoginGateG1);
        }

        if (errorMessageText != null) {
            errorMessageText.setVisibility(View.GONE);
        }

        if (loginButton != null) {
            loginButton.setOnClickListener(v -> attemptLogin());
        } else {
            Log.e(TAG, "FATAL: Login button (button_login) not found in XML.");
        }
    }

    private String getSelectedBranchId(int selectedId) {
        if (selectedId == R.id.radioLoginGateG1) {
            return "DI-Ambedkar-PG-I";
        } else if (selectedId == R.id.radioLoginGateG2) {
            return "Infinity-PG-G2";
        }
        return "";
    }

    private void attemptLogin() {
        if (usernameEditText == null || passwordEditText == null || gateRadioGroup == null) {
            showError("Application state error: UI elements missing.");
            return;
        }

        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        int selectedGateId = gateRadioGroup.getCheckedRadioButtonId();
        String branchId = getSelectedBranchId(selectedGateId);

        if (username.isEmpty() || password.isEmpty() || branchId.isEmpty()) {
            showError("Please enter username, password, and select a gate.");
            return;
        }

        LoginRequest loginRequest = new LoginRequest(username, password, branchId);

        showError(null);
        loginButton.setEnabled(false);

        RetrofitClient.getParkingApi().login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loginButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    handleLoginResponse(response.body(), branchId);
                } else {
                    String errorMsg = "Login Failed: Invalid credentials or server error. HTTP: " + response.code();
                    showError(errorMsg);
                    Log.e(TAG, "Login Error: " + response.code() + ". Message: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginButton.setEnabled(true);
                showError("Network Error: Could not connect to backend. Check your BASE_URL/network.");
                Log.e(TAG, "Network Failure: ", t);
            }
        });
   }
      private void handleLoginResponse(LoginResponse loginResponse, String assignedGate) {
        String role = loginResponse.getRole();
        String jwtToken = loginResponse.getJwtToken();
        if (role.contains("ADMIN") || role.contains("ACCOUNTANT")) {
            showError("Access Denied: Use the Web Portal.");
            return;
        }
        if (role.contains("ATTENDANT") || role.contains("GUARD")) {
            TokenManager.setToken(jwtToken);
            Intent intent = new Intent(LoginActivity.this, SecurityDashboardActivity.class);
            intent.putExtra("ASSIGNED_GATE", assignedGate);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            showError("Unauthorized role: " + role);
        }
    }
    private void showError(String message) {
        if (errorMessageText != null) {
            if (message == null || message.isEmpty()) {
                errorMessageText.setVisibility(View.GONE);
            } else {
                errorMessageText.setText(message);
                errorMessageText.setVisibility(View.VISIBLE);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Log.w(TAG, "Displaying Error: " + message);
            }
        } else
            {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, "CRITICAL ERROR: " + message, Toast.LENGTH_LONG).show();
            }}}}