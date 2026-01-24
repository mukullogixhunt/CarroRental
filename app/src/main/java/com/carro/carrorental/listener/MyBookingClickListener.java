package com.carro.carrorental.listener;

import com.carro.carrorental.model.BookingListModel;

public interface MyBookingClickListener {
    void onBookingClick(BookingListModel bookingListModel, String type, String bookType);
}
