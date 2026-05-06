package com.example.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.model.TicketResponse;
import com.example.model.VehicleEntryRequest;
import com.example.api.RetrofitClient;
import com.example.parkingappclient.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleEntryActivity extends AppCompatActivity {

    private RadioGroup vehicleTypeRadioGroup;
    private EditText vehicleNoEditText, mobileNoEditText;
    private Spinner tenantSpinner;
    private Button entryButton;
    private String assignedGate = "G1";
    private String selectedVehicleType = "2-WHEELER"; // Default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_entry);

        // Receive Gate from Login
        assignedGate = getIntent().getStringExtra("ASSIGNED_GATE");

        vehicleTypeRadioGroup = findViewById(R.id.radioGroupVehicleType);
        vehicleNoEditText = findViewById(R.id.editTextVehicleNo);
        mobileNoEditText = findViewById(R.id.editTextMobileNo);
        tenantSpinner = findViewById(R.id.spinnerTenant);
        entryButton = findViewById(R.id.buttonEntry);

        // Fix: Vehicle Selection
        vehicleTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioTwoWheeler) {
                selectedVehicleType = "2-WHEELER";
            } else if (checkedId == R.id.radioFourWheeler) {
                selectedVehicleType = "4-WHEELER";
            }
        });

        // Fix: Populate Dropdown
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tenant_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tenantSpinner.setAdapter(adapter);

        entryButton.setOnClickListener(v -> attemptVehicleEntry());
    }

    private void attemptVehicleEntry() {
        String vehicleNo = vehicleNoEditText.getText().toString().trim();
        if (vehicleNo.isEmpty()) {
            vehicleNoEditText.setError("Required");
            return;
        }

        VehicleEntryRequest request = new VehicleEntryRequest(
                vehicleNo, mobileNoEditText.getText().toString().trim(),
                assignedGate, selectedVehicleType,
                tenantSpinner.getSelectedItem().toString(),
                determineRate(tenantSpinner.getSelectedItem().toString()));

        RetrofitClient.getParkingApi().registerEntry(request).enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    // Pass the Entry Time to the Ticket Screen
                    handleSuccessfulEntry(response.body(), vehicleNo);
                }
            }
            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                Toast.makeText(VehicleEntryActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String determineRate(String tenant) {
        if (tenant.contains("Pearl")) return "RATE_PEARL_75_75";
        if (tenant.contains("SuzyQ")) return "RATE_SUZYQ_50_50";
        return "RATE_STANDARD_DEFAULT";
    }
    private void handleSuccessfulEntry(TicketResponse response, String vNo) {
        Intent intent = new Intent(this, TicketDisplayActivity.class);
        intent.putExtra("TICKET_ID", response.getTicketId());
        intent.putExtra("ENTRY_TIME", response.getEntryTime()); // Captured time
        intent.putExtra("VEHICLE_NO", vNo);
        intent.putExtra("TENANT_NAME", tenantSpinner.getSelectedItem().toString());
        intent.putExtra("QR_PAYLOAD", response.getQrPayload());
        startActivity(intent);
        finish();
    }
}