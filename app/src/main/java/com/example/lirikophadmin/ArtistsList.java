package com.example.lirikophadmin;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

public class ArtistsList extends Fragment {

    Button addNewBtn;
    private ListView artistsListView;
    private final List<Artists> artistsList = new ArrayList<>();
    private ArrayAdapter<Artists> adapter;
    private final ArtistsService artistsService = new ArtistsService();

    public ArtistsList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artists_list, container, false);

        artistsListView = view.findViewById(R.id.artistsListView);
        adapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, artistsList);
        artistsListView.setAdapter(adapter);
        artistsListView.setOnItemClickListener((parent, view1, position, id) -> showEditDialog(artistsList.get(position)));

        addNewBtn = view.findViewById(R.id.addNewBtn);

        addNewBtn.setOnClickListener(v -> {
            Intent add = new Intent(getActivity(), ArtistsPage.class);
            startActivity(add);
        });

        loadArtists();

        return view;
    }

    private void loadArtists() {
        artistsService.getAllArtists(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                artistsList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Artists artist = data.getValue(Artists.class);
                    if (artist != null) {
                        artist.setId(data.getKey());
                        artistsList.add(artist);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load artists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog(Artists artists) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.artist_dialog, null);
        builder.setView(dialogView);

        EditText editImgUrl = dialogView.findViewById(R.id.editImgUrl);
        EditText editBandName = dialogView.findViewById(R.id.editBandName);
        EditText editDate = dialogView.findViewById(R.id.editDate);
        EditText editPlace = dialogView.findViewById(R.id.editPlace);
        EditText editVipPrice = dialogView.findViewById(R.id.editVipPrice);
        EditText editUpperBoxPrice = dialogView.findViewById(R.id.editUpperBoxPrice);
        EditText editLowerBoxPrice = dialogView.findViewById(R.id.editLowerBoxPrice);
        EditText editRegularPrice = dialogView.findViewById(R.id.editRegularPrice);
        Button updateBtn = dialogView.findViewById(R.id.updateBtn);
        Button deleteBtn = dialogView.findViewById(R.id.deleteBtn);
        Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);

        editImgUrl.setText(artists.getImageUrl());
        editBandName.setText(artists.getBandName());
        editDate.setText(artists.getDate());
        editPlace.setText(artists.getPlace());

        if (artists.getPrice() != null) {
            for (SeatPrice sp : artists.getPrice()) {
                switch (sp.getType()) {
                    case "VIP":
                        editVipPrice.setText(String.valueOf(sp.getPrice()));
                        break;
                    case "Upper Box":
                        editUpperBoxPrice.setText(String.valueOf(sp.getPrice()));
                        break;
                    case "Lower Box":
                        editLowerBoxPrice.setText(String.valueOf(sp.getPrice()));
                        break;
                    case "Regular":
                        editRegularPrice.setText(String.valueOf(sp.getPrice()));
                        break;
                }
            }
        }

        AlertDialog dialog = builder.create();

        updateBtn.setOnClickListener(v -> {
            try {
                String newImgUrl = editImgUrl.getText().toString().trim();
                String newBandName = editBandName.getText().toString().trim();
                String newDate = editDate.getText().toString().trim();
                String newPlace = editPlace.getText().toString().trim();

                int newVipPrice = Integer.parseInt(editVipPrice.getText().toString().trim());
                int newUpperBoxPrice = Integer.parseInt(editUpperBoxPrice.getText().toString().trim());
                int newLowerBoxPrice = Integer.parseInt(editLowerBoxPrice.getText().toString().trim());
                int newRegularPrice = Integer.parseInt(editRegularPrice.getText().toString().trim());

                List<SeatPrice> newPrices = new ArrayList<>();
                newPrices.add(new SeatPrice("VIP", newVipPrice));
                newPrices.add(new SeatPrice("Upper Box", newUpperBoxPrice));
                newPrices.add(new SeatPrice("Lower Box", newLowerBoxPrice));
                newPrices.add(new SeatPrice("Regular", newRegularPrice));

                Map<String, Object> updates = new HashMap<>();
                updates.put("imageUrl", newImgUrl);
                updates.put("bandName", newBandName);
                updates.put("date", newDate);
                updates.put("place", newPlace);
                updates.put("price", newPrices);

                artistsService.updateArtist(artists.getId(), updates, getContext());
                Toast.makeText(getContext(), "Artist updated!", Toast.LENGTH_SHORT).show();
                loadArtists();

                dialog.dismiss();

            } catch (Exception e) {
                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(v -> {
            artistsService.deleteArtist(artists.getId(), getContext());
            dialog.dismiss();
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
