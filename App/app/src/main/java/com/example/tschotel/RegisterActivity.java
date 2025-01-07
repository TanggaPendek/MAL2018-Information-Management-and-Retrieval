package com.example.tschotel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity implements AuthLogic.AuthListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button registerMainButton;
        Button loginButton;

        registerMainButton = findViewById(R.id.registerSubmitButton);
        loginButton = findViewById(R.id.loginButton);

        EditText firstName = findViewById(R.id.firstNameField);
        EditText lastName = findViewById(R.id.lastNameField);
        EditText email = findViewById(R.id.emailField);
        EditText password = findViewById(R.id.passwordField);
        EditText phoneNumber = findViewById(R.id.phoneNumberField);

        registerMainButton.setOnClickListener(v -> {
            RegisterReqBody registerRequestBody = new RegisterReqBody();

            registerRequestBody.setFirstName(firstName.getText().toString());
            registerRequestBody.setLastName(lastName.getText().toString());
            registerRequestBody.setEmail(email.getText().toString());
            registerRequestBody.setPassword(password.getText().toString());
            registerRequestBody.setPhoneNumber(phoneNumber.getText().toString());

            AuthLogic.registerUser(registerRequestBody);

        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);

        });


    }

    @Override
    public void onLoginSuccess(String token) {

    }

    @Override
    public void onLoginFailure(String errorMessage) {

    }

    @Override
    public void onFetchCustomerDetailsSuccess(int customerId) {

    }

    @Override
    public void onFetchCustomerDetailsFailure(String errorMessage) {

    }

    @Override
    public void onRegisterSuccess() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterError() {

    }
}