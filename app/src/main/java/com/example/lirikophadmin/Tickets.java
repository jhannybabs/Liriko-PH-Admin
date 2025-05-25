package com.example.lirikophadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tickets extends Fragment {
    private ListView ticketsListView;
    private List<String> ticketItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private DatabaseReference bookingsRef;

    public Tickets() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tickets_list, container, false);

        ticketsListView = view.findViewById(R.id.ticketsListView);
        adapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, ticketItems);
        ticketsListView.setAdapter(adapter);

        bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");

        fetchBookings();

        return view;
    }

    private void fetchBookings() {
        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ticketItems.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    BookingModel booking = data.getValue(BookingModel.class);
                    if (booking != null) {
                        String item = booking.bandName + "\nSeat: " + booking.ticketType +
                                "\nPaid via: " + booking.payment +
                                "\nTotal: " + booking.total;
                        ticketItems.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to load tickets", Toast.LENGTH_SHORT).show();
            }
        });
    }
}