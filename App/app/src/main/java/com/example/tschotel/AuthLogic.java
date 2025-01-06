package com.example.tschotel;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthLogic {

    public static void registerUser(RegisterReqBody registerRequestBody) {
        DataInterface authApi = RetrofitClient.getInstance().create(DataInterface.class);

        Call<Void> call = authApi.register(registerRequestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("User Register", "User registered successfully");
                } else {
                    try {
                        Log.e("User Register", "Error: " + (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                    } catch (IOException e) {
                        Log.e("User Register", "Error reading errorBody: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("User Register", "Failed to register user: " + t.getMessage());
            }
        });
    }

    public static void loginUser(LoginReqBody loginRequestBody) {
        DataInterface authApi = RetrofitClient.getInstance().create(DataInterface.class);

        Call<LoginResBody> call = authApi.login(loginRequestBody);
        call.enqueue(new Callback<LoginResBody>() {
            @Override
            public void onResponse(Call<LoginResBody> call, Response<LoginResBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    saveToken(token);

                    getUserDetails();
                    Log.d("User Login", "Login successful. Token: " + token);
                } else {
                    try {
                        Log.e("User Login", "Error: " + (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                    } catch (IOException e) {
                        Log.e("User Login", "Error reading errorBody: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResBody> call, Throwable t) {
                Log.e("User Login", "Failed to login: " + t.getMessage());
            }
        });
    }

    public static void getUserDetails() {
        String token = getToken();
        Log.d("User Token", "Token: " + token);


        DataInterface authApi = RetrofitClient.getInstance().create(DataInterface.class);

        Call<CustomerInfoResBody> call = authApi.getCustomerDetails(token);
        call.enqueue(new Callback<CustomerInfoResBody>() {
            @Override
            public void onResponse(Call<CustomerInfoResBody> call, Response<CustomerInfoResBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CustomerInfoResBody customer = response.body();
                    Log.d("User CustomerDetails", "Customer fetched successfully: " + customer.getFirstName());
                    Log.d("User CustomerDetails", "First Name: " + customer.getFirstName());
                    Log.d("User CustomerDetails", "Last Name: " + customer.getLastName());
                    Log.d("User CustomerDetails", "Email: " + customer.getEmail());
                    Log.d("User CustomerDetails", "Phone Number: " + customer.getPhoneNumber());
                    Log.d("User CustomerDetails", "Customer ID: " + customer.getCustomerId());
                } else {
                    try {
                        Log.e("User CustomerDetails", "Error: " + (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                    } catch (IOException e) {
                        Log.e("User CustomerDetails", "Error reading errorBody: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerInfoResBody> call, Throwable t) {
                Log.e("User CustomerDetails", "Failed to fetch customer: " + t.getMessage());
            }
        });
    }

    public static void saveToken(String token) {
        Context context = ContextProvider.getContext(); // Get context from the singleton

        // Log the token before saving
        Log.d(TAG, "Saving token: " + token);

        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwt_token", token);
        editor.apply();

        // Log after saving
        Log.d(TAG, "Token saved successfully " + token);
    }

    // Get Token
    public static String getToken() {
        Context context = ContextProvider.getContext(); // Get context from the singleton
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Get the token from SharedPreferences
        String token = sharedPreferences.getString("jwt_token", null);

        // Log the token before returning
        if (token != null) {
            Log.d(TAG, "Retrieved token: " + token);
        } else {
            Log.d(TAG, "No token found in SharedPreferences.");
        }

        return token; // Return the token or null if not found
    }
}
