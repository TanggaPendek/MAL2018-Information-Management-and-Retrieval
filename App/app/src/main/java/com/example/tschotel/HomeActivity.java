package com.example.tschotel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements BookingAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<BookingInfoResBody> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int customerid = AuthLogic.getCustomerId();

        // Fetch the data
        BookingLogic.GetData(customerid);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initially, get the saved booking list from SharedPreferences
        bookingList = getBookingList();

        // Set the adapter to the RecyclerView
        bookingAdapter = new BookingAdapter(this,bookingList);
        recyclerView.setAdapter(bookingAdapter);

        Collections.sort(bookingList, new Comparator<BookingInfoResBody>() {
            @Override
            public int compare(BookingInfoResBody booking1, BookingInfoResBody booking2) {
                // Compare the IDs in descending order
                return Integer.compare(booking2.getBookingId(), booking1.getBookingId()); // Latest ID first
            }
        });

        // You might need to notify the adapter that data has been updated, once it's done fetching
        bookingAdapter.notifyDataSetChanged(); // Refresh the RecyclerView if data is updated

        FloatingActionButton refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> {
            BookingLogic.GetData(AuthLogic.getCustomerId());
            updateBookingList(bookingList);
            restartActivity();
        });

        Dialog addNewBookingDialog = new Dialog(this);
        addNewBookingDialog.setContentView(R.layout.dialog_booking_form);

        Spinner roomSpinner = addNewBookingDialog.findViewById(R.id.roomSpinner);
        CalendarView calendarView = addNewBookingDialog.findViewById(R.id.calendarView);
        EditText amountStayField = addNewBookingDialog.findViewById(R.id.amountStayField);



        FloatingActionButton addNewButton = findViewById(R.id.addNewButton);
        addNewButton.setOnClickListener(v -> {
            int customerId;
            int roomId;
            String dateBooking;
            String bookedDate;
            int amountDay;
            int statusId;


            addNewBookingDialog.show();



            String[] roomTypes = {"Family Room 1", "Family Room 2", "Family Room 3", "Suite 1", "Suite 2", "Standard Deluxe Room 1", "Standard Deluxe Room 2", "Standard Deluxe Room 3"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomTypes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roomSpinner.setAdapter(adapter);
        });

        addNewBookingDialog.findViewById(R.id.confirmButton).setOnClickListener(v -> {
            long calendarDateInMillis = calendarView.getDate();
            LocalDate calendarDate = Instant.ofEpochMilli(calendarDateInMillis)
                    .atZone(ZoneId.of("Europe/London"))  // Make sure it's in UK time
                    .toLocalDate();

            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/London"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayDate = localDateTime.format(formatter);
            String formattedDate = calendarDate.format(formatter);
            int amountStay = Integer.parseInt(amountStayField.getText().toString());



            String selectedRoom = roomSpinner.getSelectedItem().toString();

            Log.d("BookingInfo", "Customer ID: " + customerid);
            Log.d("BookingInfo", "Room ID: " + (roomSpinner.getSelectedItemPosition() + 1));
            Log.d("BookingInfo", "Booking Date: " + formattedDate);
            Log.d("BookingInfo", "Booked Date: " + todayDate);
            Log.d("BookingInfo", "Amount Stay: " + amountStay);
            Log.d("BookingInfo", "Status ID: 1");

            AddBookingReqBody addBookingReqBody = new AddBookingReqBody();
            addBookingReqBody.setCustomerId(customerid);
            addBookingReqBody.setRoomId(roomSpinner.getSelectedItemPosition() + 1);
            addBookingReqBody.setDateBooking(formattedDate);
            addBookingReqBody.setBookedDate(todayDate);
            addBookingReqBody.setAmountDay(amountStay);
            addBookingReqBody.setStatusId(1);

            BookingLogic.AddBooking(addBookingReqBody, bookingAdapter, new BookingLogic.BookingCallback() {
                @Override
                public void onSuccess() {
                    Log.d("logging","added");
                    BookingLogic.GetData(AuthLogic.getCustomerId());
                    updateBookingList(bookingList);
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
            addNewBookingDialog.dismiss();
            bookingAdapter.notifyDataSetChanged();
        });
    }

    private void updateBookingList(List<BookingInfoResBody> BookingList) {
        bookingList.clear();

        // Initially, get the saved booking list from SharedPreferences
        bookingList = getBookingList();
        Log.d("Booking List", String.valueOf(bookingList));

        // Set the adapter to the RecyclerView
        bookingAdapter = new BookingAdapter(this,bookingList);
        recyclerView.setAdapter(bookingAdapter);

        Collections.sort(bookingList, new Comparator<BookingInfoResBody>() {
            @Override
            public int compare(BookingInfoResBody booking1, BookingInfoResBody booking2) {
                // Compare the IDs in descending order
                return Integer.compare(booking2.getBookingId(), booking1.getBookingId()); // Latest ID first
            }
        });
    }

    @Override
    public void onDeleteClick(int bookingId) {

        deleteBooking(bookingId);
    }

    private void deleteBooking(int bookingId) {

        BookingLogic.DeleteBooking(bookingId, new BookingLogic.BookingCallback() {
            @Override
            public void onSuccess() {
                BookingLogic.GetData(AuthLogic.getCustomerId());
                updateBookingList(bookingList);
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        Log.d("DeleteBooking", "Booking ID to delete: " + bookingId);

        // You can also update the RecyclerView after deleting the item
        // For example, if you're deleting from a list, you can remove the item and call notifyDataSetChanged()
        bookingList.removeIf(booking -> booking.getBookingId() == bookingId);
        bookingAdapter.notifyDataSetChanged(); // Notify adapter to update the RecyclerView
    }

    public static List<BookingInfoResBody> getBookingList() {
        Context context = ContextProvider.getContext(); // Get context from singleton

        SharedPreferences sharedPreferences = context.getSharedPreferences("BookingPrefs", Context.MODE_PRIVATE);
        String bookingListJson = sharedPreferences.getString("booking_list", null);

        if (bookingListJson != null) {
            // Convert the JSON string back to a List of BookingInfoResBody objects
            Gson gson = new Gson();
            Type listType = new TypeToken<List<BookingInfoResBody>>() {}.getType();
            List<BookingInfoResBody> bookingList = gson.fromJson(bookingListJson, listType);

            Log.d("BookingInfo", "Retrieved booking list: " + bookingList);

            return bookingList;
        } else {
            Log.d("BookingInfo", "No booking list found");
            return new ArrayList<>(); // Return empty list if no data is found
        }
    }

    public void restartActivity() {
        finish();
        startActivity(getIntent());
        overridePendingTransition(0, 0); // No animation during restart
    }

}
