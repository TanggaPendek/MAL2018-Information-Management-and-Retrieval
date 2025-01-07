package com.example.tschotel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity  implements AuthLogic.AuthListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        EditText email = findViewById(R.id.emailField);
        EditText password = findViewById(R.id.passwordField);



        Button loginMainButton;
        Button regiterButton;

        loginMainButton = findViewById(R.id.loginSubmitButton);
        regiterButton = findViewById(R.id.regiterButton);

        loginMainButton.setOnClickListener(v -> {
            LoginReqBody loginRequestBody = new LoginReqBody();
            loginRequestBody.setEmail(email.getText().toString());
            loginRequestBody.setPassword(password.getText().toString());

            AuthLogic.loginUser(loginRequestBody,this);
        });

        regiterButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);

        });
    }

    @Override
    public void onLoginSuccess(String token) {
        // Handle login success, navigate to the next activity
        Log.d("Login", "Login successful, token: " + token);
        Log.d("logged","login");

        // Now that login is successful, go to another activity
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailure(String errorMessage) {
        // Handle login failure
        Log.e("Login", "Login failed: " + errorMessage);
    }

    @Override
    public void onFetchCustomerDetailsSuccess(int customerId) {
        // Handle customer details fetch success
        Log.d("CustomerDetails", "Customer ID fetched: " + customerId);
    }

    @Override
    public void onFetchCustomerDetailsFailure(String errorMessage) {
        // Handle failure to fetch customer details
        Log.e("CustomerDetails", "Failed to fetch customer details: " + errorMessage);
    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterError() {

    }


}