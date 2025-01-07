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

    public interface AuthListener {
        void onLoginSuccess(String token);
        void onLoginFailure(String errorMessage);
        void onFetchCustomerDetailsSuccess(int customerId);
        void onFetchCustomerDetailsFailure(String errorMessage);
        void onRegisterSuccess();
        void onRegisterError();
    }

    private static AuthListener authListener;

    // Register user logic
    public static void registerUser(RegisterReqBody registerRequestBody) {
        DataInterface authApi = RetrofitClient.getInstance().create(DataInterface.class);

        Call<Void> call = authApi.register(registerRequestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("User Register", "User registered successfully");
                    authListener.onRegisterSuccess();
                } else {
                    try {
                        Log.e("User Register", "Error: " + (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                    } catch (IOException e) {
                        Log.e("User Register", "Error reading errorBody: " + e.getMessage());
                        authListener.onRegisterError();
                    }
                    if (authListener != null) {
                        authListener.onRegisterError();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("User Register", "Failed to register user: " + t.getMessage());
                if (authListener != null) {
                    authListener.onRegisterError();
                }
            }
        });
    }

    // Login user logic with callback
    public static void loginUser(LoginReqBody loginRequestBody, AuthListener listener) {
        authListener = listener; // Set listener for callback

        DataInterface authApi = RetrofitClient.getInstance().create(DataInterface.class);

        Call<LoginResBody> call = authApi.login(loginRequestBody);
        call.enqueue(new Callback<LoginResBody>() {
            @Override
            public void onResponse(Call<LoginResBody> call, Response<LoginResBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    saveToken(token);
                    getUserDetails(token);

                    Log.d("User Login", "Login successful. Token: " + token);
                    authListener.onLoginSuccess(token);
                } else {
                    try {
                        Log.e("User Login", "Error: " + (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                    } catch (IOException e) {
                        Log.e("User Login", "Error reading errorBody: " + e.getMessage());
                    }
                    if (authListener != null) {
                        authListener.onLoginFailure("Login failed: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResBody> call, Throwable t) {
                Log.e("User Login", "Failed to login: " + t.getMessage());
                if (authListener != null) {
                    authListener.onLoginFailure(t.getMessage());
                }
            }
        });
    }

    // Get user details logic
    private static void getUserDetails(String token) {
        DataInterface authApi = RetrofitClient.getInstance().create(DataInterface.class);

        Call<CustomerInfoResBody> call = authApi.getCustomerDetails(token);
        call.enqueue(new Callback<CustomerInfoResBody>() {
            @Override
            public void onResponse(Call<CustomerInfoResBody> call, Response<CustomerInfoResBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CustomerInfoResBody customer = response.body();
                    saveCustomerId(customer.getCustomerId());

                    Log.d("User CustomerDetails", "Customer fetched successfully: " + customer.getFirstName());
                    Log.d("User CustomerDetails", "First Name: " + customer.getFirstName());

                    // Notify listener that user details were fetched successfully
                    if (authListener != null) {
                        authListener.onFetchCustomerDetailsSuccess(customer.getCustomerId());
                    }
                } else {
                    try {
                        Log.e("User CustomerDetails", "Error: " + (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                    } catch (IOException e) {
                        Log.e("User CustomerDetails", "Error reading errorBody: " + e.getMessage());
                    }

                    // Notify failure
                    if (authListener != null) {
                        authListener.onFetchCustomerDetailsFailure("Failed to fetch customer details");
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerInfoResBody> call, Throwable t) {
                Log.e("User CustomerDetails", "Failed to fetch customer: " + t.getMessage());
                if (authListener != null) {
                    authListener.onFetchCustomerDetailsFailure("Error fetching customer details: " + t.getMessage());
                }
            }
        });
    }

    // Save token
    public static void saveToken(String token) {
        Context context = ContextProvider.getContext(); // Get context from singleton

        Log.d(TAG, "Saving token: " + token);

        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwt_token", token);
        editor.apply();

        Log.d(TAG, "Token saved successfully " + token);
    }

    // Retrieve token
    public static String getToken() {
        Context context = ContextProvider.getContext(); // Get context from singleton
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("jwt_token", null);

        if (token != null) {
            Log.d(TAG, "Retrieved token: " + token);
        } else {
            Log.d(TAG, "No token found in SharedPreferences.");
        }

        return token;
    }

    // Save customer ID
    public static void saveCustomerId(int customerId) {
        Context context = ContextProvider.getContext(); // Get context from singleton

        Log.d("CustomerID", "Saving customerId: " + customerId);

        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("customer_id", customerId);
        editor.apply();

        Log.d("CustomerID", "CustomerId saved successfully: " + customerId);
    }

    // Retrieve customer ID
    public static int getCustomerId() {
        Context context = ContextProvider.getContext(); // Get context from singleton

        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int customerId = sharedPreferences.getInt("customer_id", -1);

        Log.d("CustomerID", "Retrieved customerId: " + customerId);

        return customerId;
    }
}
