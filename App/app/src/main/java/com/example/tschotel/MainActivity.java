package com.example.tschotel;

import static com.example.tschotel.AuthLogic.getCustomerId;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);


        ContextProvider.init(this);
/*
        RegisterReqBody registerRequestBody = new RegisterReqBody();
        registerRequestBody.setFirstName("Jonathan");
        registerRequestBody.setLastName("Brass");
        registerRequestBody.setEmail("Jonathan.b@example.com");
        registerRequestBody.setPhoneNumber("1234567890");
        registerRequestBody.setPassword("password123");

        LoginReqBody loginRequestBody = new LoginReqBody();
        loginRequestBody.setEmail("Jonathan.b@example.com");
        loginRequestBody.setPassword("password123");

        AuthLogic.loginUser(loginRequestBody);

        BookingLogic.AddBooking();*/
    }


}