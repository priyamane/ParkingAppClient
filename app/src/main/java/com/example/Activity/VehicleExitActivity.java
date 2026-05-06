package com.example.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.api.RetrofitClient;
import com.example.model.ParkingDetailsResponse;
import com.example.parkingappclient.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleExitActivity extends AppCompatActivity {

    private EditText ticketIdInput;
    private Button fetchButton, exitButton, scanButton;
    private View detailsLayout;
    private TextView detailTicketId, detailVehicleNo, detailEntryTime, detailCharges, detailTenant, detailDuration, statusTextView;

    private double chargesDue = 0.0;
    private String currentTicketId = null;
    private ActivityResultLauncher<Intent> qrScannerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_exit);

        initViews();
        setupQrScanner();

        fetchButton.setOnClickListener(v -> lookupTicket(ticketIdInput.getText().toString().trim()));
        exitButton.setOnClickListener(v -> showPaymentOptionsDialog());

        scanButton.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setPrompt("Scan Ticket QR");
            integrator.setOrientationLocked(true);
            qrScannerLauncher.launch(integrator.createScanIntent());
        });
    }

    private void initViews() {
        ticketIdInput = findViewById(R.id.editTextTicketIdManual);
        fetchButton = findViewById(R.id.buttonFetchTicket);
        exitButton = findViewById(R.id.btn_process_exit);
        scanButton = findViewById(R.id.buttonScanQr);
        detailsLayout = findViewById(R.id.layoutTicketDetails);
        statusTextView = findViewById(R.id.textViewStatus);

        detailTicketId = findViewById(R.id.detailTicketId);
        detailVehicleNo = findViewById(R.id.detailVehicleNo);
        detailEntryTime = findViewById(R.id.detailEntryTime);
        detailTenant = findViewById(R.id.detailTenant);
        detailDuration = findViewById(R.id.detailDuration);
        detailCharges = findViewById(R.id.detailCharges);

        if (detailsLayout != null) detailsLayout.setVisibility(View.GONE);
    }

    private void setupQrScanner() {
        qrScannerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(result.getResultCode(), result.getData());
            if (scanResult != null && scanResult.getContents() != null) {
                String rawContent = scanResult.getContents();
                if (rawContent.length() >= 6) {
                    lookupTicket(rawContent.substring(0, 6)); // Fix for combined QR data
                } else {
                    Toast.makeText(this, "Invalid QR Format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void lookupTicket(String id) {
        statusTextView.setText("Searching for " + id + "...");
        statusTextView.setTextColor(Color.GRAY);

        RetrofitClient.getParkingApi().calculateFees(id).enqueue(new Callback<ParkingDetailsResponse>() {
            @Override
            public void onResponse(Call<ParkingDetailsResponse> call, Response<ParkingDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ParkingDetailsResponse data = response.body();
                    chargesDue = data.getTotalChargesDue();
                    currentTicketId = data.getTicketId();

                    detailTicketId.setText(currentTicketId);
                    detailVehicleNo.setText(data.getVehicleNumber());
                    detailEntryTime.setText(data.getEntryTime());
                    detailTenant.setText(data.getVisitingTenant());
                    detailDuration.setText(String.format("%.2f Hrs", data.getDurationHours()));
                    detailCharges.setText(String.format("₹ %.2f", chargesDue));

                    detailsLayout.setVisibility(View.VISIBLE);
                    statusTextView.setText("Ticket Found.");
                } else {
                    statusTextView.setText("Ticket ID " + id + " not found.");
                    detailsLayout.setVisibility(View.GONE);
                }
            }
            @Override public void onFailure(Call<ParkingDetailsResponse> call, Throwable t) {
                statusTextView.setText("Network Failure.");
                statusTextView.setTextColor(Color.RED);
            }
        });
    }

    private void showPaymentOptionsDialog() {
        if (currentTicketId == null) return;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_payment_selection);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTotal = dialog.findViewById(R.id.tvPaymentDueAmount);
        Button btnCash = dialog.findViewById(R.id.buttonCash);
        Button btnUPI = dialog.findViewById(R.id.buttonUpi);
        LinearLayout layoutSelection = dialog.findViewById(R.id.layoutPaymentSelection);
        LinearLayout layoutUpiQr = dialog.findViewById(R.id.layoutUpiQr);
        Button btnUpiFinal = dialog.findViewById(R.id.buttonUpiPaymentConfirm);

        if (tvTotal != null) tvTotal.setText(String.format("₹ %.2f", chargesDue));

        btnCash.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Cash Payment")
                    .setMessage("Received ₹" + String.format("%.2f", chargesDue) + "?")
                    .setPositiveButton("Yes", (d, i) -> { processFinalExit("CASH"); dialog.dismiss(); })
                    .setNegativeButton("No", null).show();
        });

        btnUPI.setOnClickListener(v -> {
            layoutSelection.setVisibility(View.GONE);
            layoutUpiQr.setVisibility(View.VISIBLE);
        });

        if (btnUpiFinal != null) {
            btnUpiFinal.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Confirm UPI Payment")
                        .setMessage("Payment of ₹" + String.format("%.2f", chargesDue) + " Successful?")
                        .setPositiveButton("Yes", (d, i) -> { processFinalExit("UPI"); dialog.dismiss(); })
                        .setNegativeButton("No", null).show();
            });
        }
        dialog.show();
    }

    private void processFinalExit(String method) {
        RetrofitClient.getParkingApi().processExit(currentTicketId, chargesDue, method).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 1. Clear UI and Reset
                    if (detailsLayout != null) detailsLayout.setVisibility(View.GONE);
                    ticketIdInput.setText("");

                    // 2. Success Feedback
                    statusTextView.setText("VEHICLE EXITED SUCCESSFULLY");
                    statusTextView.setTextColor(Color.parseColor("#2E7D32"));
                    Toast.makeText(VehicleExitActivity.this, "Exit Recorded!", Toast.LENGTH_SHORT).show();

                    // 3. Close with delay
                    new Handler().postDelayed(() -> finish(), 1500);
                } else {
                    // If error, show the error code to debug null columns
                    statusTextView.setText("SERVER ERROR: " + response.code());
                    statusTextView.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                statusTextView.setText("FAILED: " + t.getMessage());
                statusTextView.setTextColor(Color.RED);
            }
        });
    }
}