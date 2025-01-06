package com.example.tschotel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DataInterface {
    @GET("/getBookingDetails/")
    Call<List<BookingInfoResBody>> getBookingDetails();

    @POST("insertBooking")
    Call<BookingInfoResBody> addBooking(@Body AddBookingReqBody addBooking);

    // Delete a booking by ID
    @DELETE("deleteBooking/{bookingId}")
    Call<Void> deleteBooking(@Path("bookingId") int bookingId);

    // Update booking status (for example, marking as complete)
    @PUT("updateBooking/{bookingId}")
    Call<UpdateBookingResBody> updateBooking(@Path("bookingId") int bookingId, @Body  UpdateBookingReqBody updateBooking);



    @POST("register")
    Call<Void> register(@Body RegisterReqBody registerReqBody);

    // Login endpoint
    @POST("login")
    Call<LoginResBody> login(@Body LoginReqBody loginReqBody);

    // Fetch customer details endpoint
    @GET("customer")
    Call<CustomerInfoResBody> getCustomerDetails(@Header("Authorization") String token);
}
