package com.example.lirikophadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.*;

public class Home extends Fragment {
    public Home() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        TextView seeArtistsBtn = view.findViewById(R.id.seeArtistsBtn);
        seeArtistsBtn.setOnClickListener(v -> {
            BottomNavigationView nav = getActivity().findViewById(R.id.bottomNavigation);
            nav.setSelectedItemId(R.id.nav_artists);
        });

        ListView recentArtistsList = view.findViewById(R.id.recentArtists);
        ListView recentTixList = view.findViewById(R.id.recentTix);  // new ListView for recent bookings

        ArtistsService artistsService = new ArtistsService();
        artistsService.getArtist(new ArtistsService.OnArtistsLoadListener() {
            @Override
            public void onLoaded(List<Artists> artists) {
                int limit = Math.min(5, artists.size());
                List<Artists> recent = artists.subList(artists.size() - limit, artists.size());

                List<String> names = new ArrayList<>();
                for (Artists artist : recent) {
                    names.add(artist.getBandName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, names);
                recentArtistsList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Failed to load recent artists", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");
        bookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> tickets = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    BookingModel booking = data.getValue(BookingModel.class);
                    if (booking != null) {
                        String item = booking.bandName + " - " + booking.ticketType + " - " + booking.total;
                        tickets.add(item);
                    }
                }

                int limit = Math.min(5, tickets.size());
                List<String> recent = tickets.subList(tickets.size() - limit, tickets.size());

                ArrayAdapter<String> ticketAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, recent);
                recentTixList.setAdapter(ticketAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load bookings", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}