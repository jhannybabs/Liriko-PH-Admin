package com.example.lirikophadmin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class ArtistsPage extends AppCompatActivity {

    EditText imgUrlField, bandNameField, dateField, placeField, vipField, upperBoxField, lowerBoxField, regularField;
    Button addBtn;
    ImageView backBtn;
    private final ArtistsService artistsService = new ArtistsService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artists);

        imgUrlField = findViewById(R.id.imageUrlInp);
        bandNameField = findViewById(R.id.bandNameInp);
        dateField = findViewById(R.id.dateInp);
        placeField = findViewById(R.id.placeInp);
        vipField = findViewById(R.id.vipPriceInp);
        upperBoxField = findViewById(R.id.upperBoxPriceInp);
        lowerBoxField = findViewById(R.id.lowerBoxPriceInp);
        regularField = findViewById(R.id.regularPriceInp);
        addBtn = findViewById(R.id.addBtn);
        backBtn = findViewById(R.id.backBtn);

        addBtn.setOnClickListener(v -> createArtist());

        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void createArtist() {
        String imgUrl = imgUrlField.getText().toString().trim();
        String bandName = bandNameField.getText().toString().trim();
        String date = dateField.getText().toString().trim();
        String place = placeField.getText().toString().trim();
        int vip = Integer.parseInt(vipField.getText().toString().trim());
        int upper = Integer.parseInt(upperBoxField.getText().toString().trim());
        int lower = Integer.parseInt(lowerBoxField.getText().toString().trim());
        int regular = Integer.parseInt(regularField.getText().toString().trim());


        if (imgUrl.isEmpty() || bandName.isEmpty() || date.isEmpty() || place.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
        }

        List<SeatPrice> seatPrices = new ArrayList<>();
        seatPrices.add(new SeatPrice("VIP", vip));
        seatPrices.add(new SeatPrice("Upper Box", upper));
        seatPrices.add(new SeatPrice("Lower Box", lower));
        seatPrices.add(new SeatPrice("Regular", regular));

        String key = artistsService.generateArtistKey();
        Artists artists = new Artists(imgUrl, bandName, date, place, seatPrices);
        artists.setId(key);
        artistsService.createArtist(artists,this);
        clearInputs();
    }

    private void clearInputs() {
        imgUrlField.setText("");
        bandNameField.setText("");
        dateField.setText("");
        placeField.setText("");
        vipField.setText("");
        upperBoxField.setText("");
        lowerBoxField.setText("");
        regularField.setText("");
    }
}
