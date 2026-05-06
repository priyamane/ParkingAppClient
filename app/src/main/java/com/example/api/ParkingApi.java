package com.example.api;

import com.example.model.LoginRequest;
import com.example.model.LoginResponse;
import com.example.model.Tenant;
import com.example.model.Vehicle;
import java.util.List;
import com.example.model.ParkingDetailsResponse;
import com.example.model.TicketResponse;
import com.example.model.VehicleEntryRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ParkingApi {
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("api/parking/details/{ticketId}")
    Call<ParkingDetailsResponse> calculateFees(@Path("ticketId") String ticketId);

    @POST("api/parking/exit/{ticketId}")
    Call<ResponseBody> processExit(
            @Path("ticketId") String ticketId,
            @Query("amountPaid") double amountPaid,
            @Query("paymentMethod") String paymentMethod
    );
    @POST("api/parking/entry")
    Call<TicketResponse> registerEntry(@Body VehicleEntryRequest request);
    @POST("api/tenants")
    Call<Tenant> addTenant(@Body Tenant tenant);
    @GET("api/tenants")
    Call<List<Tenant>> getAllTenants();
    @PUT("api/tenants/{id}")
    Call<Tenant> updateTenantRate(@Path("id") Long id, @Body Tenant tenant);
    @POST("api/vehicles")
    Call<Vehicle> addVehicle(@Body Vehicle vehicle);

    // Matches GET /api/vehicles
    @GET("api/vehicles")
    Call<List<Vehicle>> getAllVehicles();
}