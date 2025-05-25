package com.example.lirikophadmin;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArtistsService {

    private final DatabaseReference artistsRef;

    public  ArtistsService() {
        artistsRef = FirebaseDatabase.getInstance().getReference("artists");
    }

    public void createArtist(Artists artists, Context context) {
        artistsRef.child(artists.getId()).setValue(artists)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(context, "Artist Added!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Failed to add Artist!", Toast.LENGTH_SHORT).show());
    }

    public void updateArtist(String artistKey, Map<String, Object> updates, Context context) {
        artistsRef.child(artistKey).updateChildren(updates)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(context, "Artist Updated!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Artist Update Failed!", Toast.LENGTH_SHORT).show());
    }

    public void deleteArtist(String artistKey, Context context) {
        artistsRef.child(artistKey).removeValue()
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(context, "Artist Deleted!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Artist Delete Failed!", Toast.LENGTH_SHORT).show());
    }

    public void getArtist(OnArtistsLoadListener listener){
        artistsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Artists> artists = new ArrayList<>();
                for (DataSnapshot artistSnapshot : snapshot.getChildren()) {
                    Artists artist = artistSnapshot.getValue(Artists.class);
                    if (artist != null) {
                        artist.setId(artistSnapshot.getKey());
                        artists.add(artist);
                    }
                }
                listener.onLoaded(artists);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure(error.toException());
            }
        });
    }

    public void getAllArtists(ValueEventListener listener) {
        artistsRef.addValueEventListener(listener);
    }

    public String generateArtistKey() {
        return artistsRef.push().getKey();
    }

    public interface OnArtistsLoadListener {
        void onLoaded(List<Artists> artists);
        void onFailure(Exception e);
    }
}
