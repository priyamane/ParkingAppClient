package com.example.Activity; // <-- CORRECTED PACKAGE NAME

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.util.List;
import com.example.api.ParkingApi;
import com.example.api.RetrofitClient;
import com.example.model.Tenant;
import com.example.model.Vehicle;
import com.example.parkingappclient.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class   MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView apiDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiDataTextView = findViewById(R.id.api_data_textview);
        if (apiDataTextView == null) {
            Log.e(TAG, "Error: TextView with ID api_data_textview not found in layout.");
            return;
        }
        updateTestTenantRate();
        fetchAllTenants();
        addTestVehicle();
        fetchAllVehicles();
    }
    private void updateTestTenantRate() {
        final Long TENANT_ID_TO_UPDATE = 1L;
        Tenant updatedTenantData = new Tenant();
        updatedTenantData.setBaseRate(6.50);
        updatedTenantData.setHourlyRate(1.50);
        ParkingApi apiService = RetrofitClient.getParkingApi();
        Call<Tenant> call = apiService.updateTenantRate(TENANT_ID_TO_UPDATE, updatedTenantData);

        call.enqueue(new Callback<Tenant>() {
            @Override
            public void onResponse(Call<Tenant> call, Response<Tenant> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Tenant updatedTenant = response.body();
                    Log.d(TAG, "Tenant Updated Successfully. ID: " + updatedTenant.getTenantId() +
                            ", New Rate: " + updatedTenant.getBaseRate());
                } else {
                    Log.e(TAG, "PUT Tenant failed. HTTP Code: " + response.code() + " (Requires Auth/Role).");
                }
            }
            @Override
            public void onFailure(Call<Tenant> call, Throwable t) {
                Log.e(TAG, "PUT Tenant network failed: " + t.getMessage());
            }
        });
    }

    private void fetchAllTenants() {
        ParkingApi apiService = RetrofitClient.getParkingApi();
        Call<List<Tenant>> call = apiService.getAllTenants();
        call.enqueue(new Callback<List<Tenant>>() {
            @Override
            public void onResponse(Call<List<Tenant>> call, Response<List<Tenant>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Tenant> tenants = response.body();
                    Log.d(TAG, "Successfully fetched " + tenants.size() + " tenants.");

                    StringBuilder sb = new StringBuilder();
                    sb.append("--- Tenants (").append(tenants.size()).append(") ---\n");
                    for (Tenant tenant : tenants) {
                        sb.append("Name: ").append(tenant.getTenantName())
                                .append(", Base Rate: ").append(tenant.getBaseRate()).append("\n");
                    }
                    if (apiDataTextView != null) {
                        apiDataTextView.setText(sb.toString());
                    }
                } else {
                    Log.e(TAG, "GET Tenants Response failed. Code: " + response.code() + " (Requires Auth/Role).");
                    if (apiDataTextView != null) {
                        apiDataTextView.setText("Error loading tenants. Code: " + response.code());
                    }}}
            @Override
            public void onFailure(Call<List<Tenant>> call, Throwable t) {
                Log.e(TAG, "GET Tenants API Call Failed: " + t.getMessage());
                if (apiDataTextView != null) {
                    apiDataTextView.setText("Network error fetching tenants: " + t.getMessage());
                }
                t.printStackTrace();
            }
        });
    }
    private void addTestVehicle() {
        Vehicle newVehicle = new Vehicle();
        newVehicle.setVehicleNumber("MH-01-AB-1235");
        newVehicle.setVehicleType("Car");
        newVehicle.setTenantName("Default Tenant");
        newVehicle.setEntryTime("2025-11-17T13:00:00");

        ParkingApi apiService = RetrofitClient.getParkingApi();
        Call<Vehicle> call = apiService.addVehicle(newVehicle);

        call.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Vehicle added successfully. ID: " + response.body().getId());
                } else {
                    Log.e(TAG, "POST Vehicle failed. HTTP Code: " + response.code() + " (Requires Auth/Role).");
                }
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                Log.e(TAG, "POST Vehicle network failed: " + t.getMessage());
            }
        });
    }

    private void fetchAllVehicles() {
        ParkingApi apiService = RetrofitClient.getParkingApi();
        Call<List<Vehicle>> call = apiService.getAllVehicles();
        call.enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Vehicle> vehicles = response.body();
                    Log.d(TAG, "Successfully fetched " + vehicles.size() + " vehicles.");

                    CharSequence currentText = apiDataTextView != null ? apiDataTextView.getText() : "";
                    StringBuilder sb = new StringBuilder(currentText);

                    sb.append("\n--- Vehicles (").append(vehicles.size()).append(") ---\n");

                    for (Vehicle vehicle : vehicles) {
                        sb.append("ID: ").append(vehicle.getId())
                                .append(", Number: ").append(vehicle.getVehicleNumber()).append("\n");
                    }
                    if (apiDataTextView != null) {
                        apiDataTextView.setText(sb.toString());
                    }

                } else {
                    Log.e(TAG, "GET Vehicles Response failed. Code: " + response.code() + " (Requires Auth/Role).");
                    if (apiDataTextView != null) {
                        apiDataTextView.append("\nError loading vehicles. Code: " + response.code());
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
                Log.e(TAG, "GET Vehicles API Call Failed: " + t.getMessage());
                if (apiDataTextView != null) {
                    apiDataTextView.append("\nNetwork error fetching vehicles: " + t.getMessage());
                }
                t.printStackTrace();
            }
        });
    }
}