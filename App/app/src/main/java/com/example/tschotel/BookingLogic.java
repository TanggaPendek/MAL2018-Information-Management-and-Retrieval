package com.example.tschotel;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingLogic {

    public static void GetData(){

        DataInterface bookingApi = RetrofitClient.getInstance().create(DataInterface.class);

        Call<List<BookingInfoResBody>> call = bookingApi.getBookingDetails();
        call.enqueue(new Callback<List<BookingInfoResBody>>() {
            @Override
            public void onResponse(Call<List<BookingInfoResBody>> call, Response<List<BookingInfoResBody>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BookingInfoResBody> bookingList = response.body();
                    for (BookingInfoResBody booking : bookingList) {
                        Log.d("BookingDetails", "ID: " + booking.getBookingId() +
                                ", Customer: " + booking.getCustomerName() +
                                ", Email: " + booking.getCustomerEmail() +
                                ", Room: " + booking.getRoomName() +
                                ", Hotel: " + booking.getHotelName() +
                                ", Price: $" + booking.getRoomPrice() +
                                ", Days: " + booking.getAmountDay() +
                                ", Date: " + booking.getBookedDate() +
                                ", Status: " + booking.getBookingStatus());
                    }
                } else {
                    Log.e("BookingDetails", "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<BookingInfoResBody>> call, Throwable t) {
                Log.e("BookingDetails", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    public static void AddBooking() {
        DataInterface bookingApi = RetrofitClient.getInstance().create(DataInterface.class);

        // Prepare the request body
        AddBookingReqBody addBookingReqBody = new AddBookingReqBody();
        addBookingReqBody.setCustomerId(1);
        addBookingReqBody.setRoomId(1);
        addBookingReqBody.setDateBooking("2023-11-11");
        addBookingReqBody.setBookedDate("2023-11-11");
        addBookingReqBody.setAmountDay(2);
        addBookingReqBody.setStatusId(1);

        // Make the API call
        Call<BookingInfoResBody> call = bookingApi.addBooking(addBookingReqBody); // Changed to BookingInfoResBody
        call.enqueue(new Callback<BookingInfoResBody>() { // Changed to BookingInfoResBody
            @Override
            public void onResponse(Call<BookingInfoResBody> call, Response<BookingInfoResBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookingInfoResBody booking = response.body();
                    // Log the success
                    Log.d("AddBooking", "Booking added successfully: " + booking);
                } else {
                    // Handle error response
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("AddBooking", "Error: " + errorMessage);
                    } catch (IOException e) {
                        Log.e("AddBooking", "Error reading errorBody: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BookingInfoResBody> call, Throwable t) {
                // Log the failure
                Log.e("AddBooking", "Failed to add data: " + t.getMessage());
            }
        });
    }



    public static void DeleteBooking() {
        DataInterface bookingApi = RetrofitClient.getInstance().create(DataInterface.class);

        int bookingIdToDelete = 49;
        Call<Void> call = bookingApi.deleteBooking(bookingIdToDelete);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                Log.d("DeleteBooking", "Booking deleted successfully");
            } else {
                    Log.e("DeleteBooking", "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("DeleteBooking", "Failed to delete data: " + t.getMessage());
            }
        });
    }

    public static void UpdateBooking(int bookingId) {
        // Create Retrofit API instance
        DataInterface bookingApi = RetrofitClient.getInstance().create(DataInterface.class);

        // Create dummy data for UpdateBookingReqBody
        UpdateBookingReqBody updateBookingReqBody = new UpdateBookingReqBody();
        updateBookingReqBody.setRoomId(2);  // Dummy room ID
        updateBookingReqBody.setBookedDate("2024-11-11");  // Dummy booked date
        updateBookingReqBody.setAmountDay(10);  // Dummy amount of days
        updateBookingReqBody.setStatusId(2);  // Dummy status ID

        // Make the API call
        Call<UpdateBookingResBody> call = bookingApi.updateBooking(bookingId, updateBookingReqBody);

        // Execute the call asynchronously
        call.enqueue(new Callback<UpdateBookingResBody>() {
            @Override
            public void onResponse(Call<UpdateBookingResBody> call, Response<UpdateBookingResBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Log success and process the response body
                    UpdateBookingResBody updatedBooking = response.body();
                    Log.d("UpdateBooking", "Booking updated successfully: " + updatedBooking);
                } else {
                    // Log error response
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("UpdateBooking", "Error: " + errorMessage);
                    } catch (IOException e) {
                        Log.e("UpdateBooking", "Error reading errorBody: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateBookingResBody> call, Throwable t) {
                // Log failure
                Log.e("UpdateBooking", "Failed to update data: " + t.getMessage());
            }
        });
    }


}
