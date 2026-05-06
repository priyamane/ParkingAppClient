package com.example.api;
import com.example.model.TokenManager;
// ^ Adjust package path if needed
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    // Check your CURRENT ngrok terminal output and use that URL.
// Example based on the previous screenshot:
    private static final String BASE_URL = "https://mirthful-mitch-neutrally.ngrok-free.dev";

// NOTE: Also ensure you use the correct protocol (http:// or https://)

    // Use an instance of ParkingApi that includes the interceptor
    private static ParkingApi parkingApiService;

    public static ParkingApi getParkingApi() {
        // Only initialize the service once
        if (parkingApiService == null) {
            parkingApiService = getRetrofitInstance().create(ParkingApi.class);
        }
        return parkingApiService;
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // 1. Define the Interceptor
            Interceptor authInterceptor = chain -> {
                Request originalRequest = chain.request();
                String token = TokenManager.getToken(); // Retrieve the stored token

                // 2. Add Authorization header only if a token is available
                Request.Builder builder = originalRequest.newBuilder();
                if (token != null && !token.isEmpty()) {
                    // Inject the JWT in the required Bearer format
                    builder.header("Authorization", "Bearer " + token);
                }

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            };

            // 3. Create OkHttpClient and add the Interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build();

            // 4. Build Retrofit using the configured OkHttpClient
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // Inject the OkHttpClient here
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    // Remove the redundant getParkingApiService() method
}