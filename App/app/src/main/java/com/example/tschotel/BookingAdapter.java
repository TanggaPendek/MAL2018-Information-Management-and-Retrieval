package com.example.tschotel;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context; // Add a context field
    private List<BookingInfoResBody> bookingList;

    public BookingAdapter(Context context, List<BookingInfoResBody> bookingList) {
        this.context = context; // Save context
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingInfoResBody booking = bookingList.get(position);

        holder.tvBookingId.setText("Booking ID: " + booking.getBookingId());
        holder.tvCustomerName.setText("Customer Name: " + booking.getCustomerName());
        holder.tvRoomName.setText("Room Name: " + booking.getRoomName());
        holder.tvBookingDate.setText("Booking Date: " + booking.getBookedDate());
        holder.tvAmountDay.setText("Amount of Days: " + booking.getAmountDay());
        holder.tvBookingStatus.setText("Status: " + booking.getBookingStatus());

        String checkoutTime = calculateCheckoutTime(booking.getBookedDate(), booking.getAmountDay());
        holder.tvCheckoutTime.setText("Checkout Time: " + checkoutTime);

        double totalPrice = booking.getRoomPrice() * booking.getAmountDay();
        holder.tvCalculatedPrice.setText("Total Price: $" + totalPrice);

        holder.btnDelete.setOnClickListener(v -> {
            int bookingId = booking.getBookingId();
            BookingLogic.DeleteBooking(bookingId, new BookingLogic.BookingCallback() {
                @Override
                public void onSuccess() {
                    BookingLogic.GetData(AuthLogic.getCustomerId());
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
            removeItem(position); // Manually remove the item
        });

        Dialog editDialog = new Dialog(context);
        editDialog.setContentView(R.layout.dialog_booking_form2);

        Button confirmButton = editDialog.findViewById(R.id.confirmButton);
        Spinner roomSpinner = editDialog.findViewById(R.id.roomSpinner);
        CalendarView calendarView = editDialog.findViewById(R.id.calendarView);
        EditText amountStayField = editDialog.findViewById(R.id.amountStayField);

        holder.btnEdit.setOnClickListener(v -> {
            editDialog.show();
            String[] roomTypes = {"Family Room 1", "Family Room 2", "Family Room 3", "Suite 1", "Suite 2", "Standard Deluxe Room 1", "Standard Deluxe Room 2", "Standard Deluxe Room 3"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, roomTypes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roomSpinner.setAdapter(adapter);
        });

        confirmButton.setOnClickListener(v -> {

            long calendarDateInMillis = calendarView.getDate();
            LocalDate calendarDate = Instant.ofEpochMilli(calendarDateInMillis)
                    .atZone(ZoneId.of("Europe/London"))  // Make sure it's in UK time
                    .toLocalDate();

            LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/London"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayDate = localDateTime.format(formatter);
            String formattedDate = calendarDate.format(formatter);
            int amountStay = Integer.parseInt(amountStayField.getText().toString());

            UpdateBookingReqBody updateBookingReqBody = new UpdateBookingReqBody();
            updateBookingReqBody.setRoomId(roomSpinner.getSelectedItemPosition() + 1);
            updateBookingReqBody.setBookedDate(formattedDate);
            updateBookingReqBody.setAmountDay(amountStay);
            updateBookingReqBody.setStatusId(1);

            BookingLogic.UpdateBooking(booking.getBookingId(), updateBookingReqBody ,this, new BookingLogic.BookingCallback() {
                @Override
                public void onSuccess() {
                    BookingLogic.GetData(AuthLogic.getCustomerId());
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });



            editDialog.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    // Method to calculate checkout time
    private String calculateCheckoutTime(String bookingDate, int days) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(bookingDate));
            calendar.add(Calendar.DAY_OF_YEAR, days);
            return dateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Invalid date";
    }

    // Add this method to update dataset
    public void updateList(List<BookingInfoResBody> newBookingList) {
        this.bookingList = newBookingList;
        notifyDataSetChanged(); // Refresh the entire list
    }

    // Add this method to remove an item
    public void removeItem(int position) {
        bookingList.remove(position);
        notifyItemRemoved(position); // Notify RecyclerView of item removal
        notifyItemRangeChanged(position, bookingList.size()); // Update the affected range
    }



    public static class BookingViewHolder extends RecyclerView.ViewHolder {

        TextView tvBookingId;
        TextView tvCustomerName;
        TextView tvRoomName;
        TextView tvBookingDate;
        TextView tvAmountDay;
        TextView tvBookingStatus;
        TextView tvCheckoutTime;
        TextView tvCalculatedPrice;
        Button btnDelete;
        Button btnEdit;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tvBookingId);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvBookingDate = itemView.findViewById(R.id.tvBookingDate);
            tvAmountDay = itemView.findViewById(R.id.tvAmountDay);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
            tvCheckoutTime = itemView.findViewById(R.id.tvCheckoutTime);
            tvCalculatedPrice = itemView.findViewById(R.id.tvCalculatedPrice);
            btnDelete = itemView.findViewById(R.id.deleteButton);
            btnEdit = itemView.findViewById(R.id.editButton);
        }
    }

    public interface OnItemClickListener {
        void onDeleteClick(int bookingId);
    }
}
