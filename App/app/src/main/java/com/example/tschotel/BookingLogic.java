package com.example.tschotel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingLogic {

    public interface BookingCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public static void GetData(int customerId) {
        DataInterface bookingApi = RetrofitClient.getInstance().create(DataInterface.class);

        Call<List<BookingInfoResBody>> call = bookingApi.getBookingDetails();
        call.enqueue(new Callback<List<BookingInfoResBody>>() {
            @Override
            public void onResponse(Call<List<BookingInfoResBody>> call, Response<List<BookingInfoResBody>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BookingInfoResBody> bookingList = response.body();

                    // Filter the list by customerId
                    List<BookingInfoResBody> filteredList = new ArrayList<>();
                    for (BookingInfoResBody booking : bookingList) {
                        if (booking.getCustomerId() == customerId) {
                            filteredList.add(booking);
                        }
                    }

                    // Save filtered booking list to SharedPreferences
                    saveBookingList(filteredList);
                    // Log the filtered results (optional)
                    for (BookingInfoResBody booking : filteredList) {
                        Log.d("FilteredBookingDetails", "ID: " + booking.getBookingId() +
                                ", Customer: " + booking.getCustomerName() +
                                ", Email: " + booking.getCustomerEmail() +
                                ", Room: " + booking.getRoomName() +
                                ", Hotel: " + booking.getHotelName() +
                                ", Price: $" + booking.getRoomPrice() +
                                ", Days: " + booking.getAmountDay() +
                                ", Date: " + booking.getBookedDate() +
                                ", Status: " + booking.getBookingStatus());
                    }

                    if (filteredList.isEmpty()) {
                        Log.d("FilteredBookingDetails", "No bookings found for customerId: " + customerId);
                    }
                } else {
                    try {
                        Log.e("BookingDetails", "Error: " + (response.errorBody() != null ? response.errorBody().string() : "Unknown error"));
                    } catch (IOException e) {
                        Log.e("BookingDetails", "Error reading errorBody: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BookingInfoResBody>> call, Throwable t) {
                Log.e("BookingDetails", "Failed to fetch data: " + t.getMessage());
            }
        });
    }




    public static void AddBooking(AddBookingReqBody addBookingReqBody, BookingAdapter bookingAdapter, BookingCallback callback) {
        DataInterface bookingApi = RetrofitClient.getInstance().create(DataInterface.class);

        // Make the API call
        Call<BookingInfoResBody> call = bookingApi.addBooking(addBookingReqBody);
        call.enqueue(new Callback<BookingInfoResBody>() {
            @Override
            public void onResponse(Call<BookingInfoResBody> call, Response<BookingInfoResBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BookingInfoResBody booking = response.body();
                    Log.d("AddBooking", "Booking added successfully: " + booking);
                    GetData(AuthLogic.getCustomerId());
                    bookingAdapter.notifyDataSetChanged();

                    if (callback != null) {
                        callback.onSuccess(); // Notify on success
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("AddBooking", "Error: " + errorMessage);
                        if (callback != null) {
                            callback.onFailure(errorMessage); // Notify on failure
                        }
                    } catch (IOException e) {
                        Log.e("AddBooking", "Error reading errorBody: " + e.getMessage());
                        if (callback != null) {
                            callback.onFailure(e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BookingInfoResBody> call, Throwable t) {
                Log.e("AddBooking", "Failed to add data: " + t.getMessage());
                if (callback != null) {
                    callback.onFailure(t.getMessage()); // Notify on failure
                }
            }
        });
    }




    public static void DeleteBooking(int BookingId, BookingCallback callback) {
        DataInterface bookingApi = RetrofitClient.getInstance().create(DataInterface.class);

        Call<Void> call = bookingApi.deleteBooking(BookingId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("DeleteBooking", "Booking deleted successfully");
                    GetData(AuthLogic.getCustomerId());

                    if (callback != null) {
                        callback.onSuccess(); // Notify on success
                    }
                } else {
                    Log.e("DeleteBooking", "Error: " + response.errorBody());
                    if (callback != null) {
                        callback.onFailure(response.errorBody().toString()); // Notify on failure
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("DeleteBooking", "Failed to delete data: " + t.getMessage());
                if (callback != null) {
                    callback.onFailure(t.getMessage()); // Notify on failure
                }
            }
        });
    }


    public static void UpdateBooking(int bookingId, UpdateBookingReqBody updateBookingReqBody, BookingAdapter bookingAdapter, BookingCallback callback) {
        DataInterface bookingApi = RetrofitClient.getInstance().create(DataInterface.class);

       /* UpdateBookingReqBody updateBookingReqBody = new UpdateBookingReqBody();
        updateBookingReqBody.setRoomId(2);
        updateBookingReqBody.setBookedDate("2024-11-11");
        updateBookingReqBody.setAmountDay(10);
        updateBookingReqBody.setStatusId(2);*/

        Call<UpdateBookingResBody> call = bookingApi.updateBooking(bookingId, updateBookingReqBody);

        call.enqueue(new Callback<UpdateBookingResBody>() {
            @Override
            public void onResponse(Call<UpdateBookingResBody> call, Response<UpdateBookingResBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateBookingResBody updatedBooking = response.body();
                    Log.d("UpdateBooking", "Booking updated successfully: " + updatedBooking);
                    GetData(AuthLogic.getCustomerId());

                    if (callback != null) {
                        callback.onSuccess(); // Notify on success
                    }
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("UpdateBooking", "Error: " + errorMessage);
                        if (callback != null) {
                            callback.onFailure(errorMessage); // Notify on failure
                        }
                    } catch (IOException e) {
                        Log.e("UpdateBooking", "Error reading errorBody: " + e.getMessage());
                        if (callback != null) {
                            callback.onFailure(e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateBookingResBody> call, Throwable t) {
                Log.e("UpdateBooking", "Failed to update data: " + t.getMessage());
                if (callback != null) {
                    callback.onFailure(t.getMessage()); // Notify on failure
                }
            }
        });
    }


    // Save booking info
    public static void saveBookingList(List<BookingInfoResBody> bookingList) {
        Context context = ContextProvider.getContext(); // Get context from singleton
        SharedPreferences sharedPreferences = context.getSharedPreferences("BookingPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the list to JSON
        Gson gson = new Gson();
        String json = gson.toJson(bookingList);
        editor.putString("booking_list", json);
        editor.apply();

        Log.d("BookingInfo", "Booking list saved: " + bookingList);
    }



    // Retrieve booking info
    public static BookingInfoResBody getBookingInfo() {
        Context context = ContextProvider.getContext(); // Get context from singleton

        SharedPreferences sharedPreferences = context.getSharedPreferences("BookingPrefs", Context.MODE_PRIVATE);
        String bookingJson = sharedPreferences.getString("booking_list", null);

        if (bookingJson != null) {
            // Convert the JSON string back to a BookingInfoResBody object
            Gson gson = new Gson();
            BookingInfoResBody booking = gson.fromJson(bookingJson, BookingInfoResBody.class);

            Log.d("BookingInfo", "Retrieved booking info: " + booking);

            return booking;
        } else {
            Log.d("BookingInfo", "No booking info found");
            return null; // No booking data found
        }
    }


}
