package com.androweb.voyage.LocationService;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androweb.voyage.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.androweb.voyage.utils.Utils.bitmapDescriptorFromVector;

public class FetchLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {

    public static String PARAM_TITLE = "title";
    public static String PARAM_LATITUDE = "latitude";
    public static String PARAM_LONGITUDE = "longitude";
    public static String RESULT_ADDRESS = "address";
    public static String RESULT_POSTAL_CODE = "pin";
    public static String RESULT_LATITUDE = "latitude";
    public static String RESULT_LONGITUDE = "longitude";
    public static String RESULT_CITY = "city";
    public static String RESULT_COUNTRY = "country";

    private GoogleMap gMap;
    private GoogleMap.OnCameraIdleListener onCameraIdle;
    private ImageView ivLocationPin;
    private ImageButton btCurrentLocation;
    private TextView tvLocation;
    private Button btPickLocation;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private LatLng latLng;
    private String address;
    private String postalCode;
    private String cityName;
    private String country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_location);

        ivLocationPin = findViewById(R.id.ivLocationPin);
        btCurrentLocation = findViewById(R.id.btCurrentLocation);
        tvLocation = findViewById(R.id.tvLocation);
        btPickLocation = findViewById(R.id.btPickLocation);

        // Preventing user to get a invalid Address
        btPickLocation.setEnabled(false);

        String title = "";
        if(getIntent() != null) {
            title = getIntent().getStringExtra(PARAM_TITLE);
            latitude = getIntent().getDoubleExtra(PARAM_LATITUDE,0.0);
            longitude = getIntent().getDoubleExtra(PARAM_LONGITUDE,0.0);
        }

        latLng = new LatLng(latitude, longitude);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        btCurrentLocation.setOnClickListener(v -> {
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f));
        });

        btPickLocation.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(RESULT_ADDRESS,address);
            intent.putExtra(RESULT_POSTAL_CODE,postalCode);
            intent.putExtra(RESULT_LATITUDE, latitude);
            intent.putExtra(RESULT_LONGITUDE, longitude);
            intent.putExtra(RESULT_CITY, cityName);
            intent.putExtra(RESULT_COUNTRY,country);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng)
                .title("Your Location")
                .draggable(false);

        gMap.addMarker(markerOptions);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));

        gMap.setOnCameraMoveListener(this);
        gMap.setOnCameraIdleListener(this);

    }

    @Override
    public void onCameraMove() {
        gMap.clear();
        ivLocationPin.setVisibility(View.VISIBLE);

    }

    @Override
    public void onCameraIdle() {
        ivLocationPin.setVisibility(View.GONE);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(gMap.getCameraPosition().target)
                .icon(bitmapDescriptorFromVector(this, R.drawable.ic_location_pin));
        gMap.addMarker(markerOptions);

        // Fetch Address
        fetchedAddress(gMap.getCameraPosition().target);

    }

    private void fetchedAddress(LatLng target) {
        latitude = target.latitude;
        longitude = target.longitude;

        Geocoder geocoder = new Geocoder(FetchLocationActivity.this);

        try {
            List<Address> addressList = geocoder.getFromLocation(target.latitude, target.longitude, 1);
            if(addressList != null && addressList.size() > 0) {
                address = addressList.get(0).getAddressLine(0);
                postalCode = addressList.get(0).getPostalCode();
                cityName = addressList.get(0).getLocality();
                country = addressList.get(0).getCountryName();

                if (!address.isEmpty()) {
                    tvLocation.setText(address);
                    btPickLocation.setEnabled(true);
                } else {
                    tvLocation.setText(getResources().getString(R.string.location_err_msg));
                }
            }
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    public static void getLocation(Activity activity, int requestCode, String title, double latitude, double longitude) {
        Intent intent = new Intent(activity, FetchLocationActivity.class);
        intent.putExtra(PARAM_TITLE, title);
        intent.putExtra(PARAM_LATITUDE, latitude);
        intent.putExtra(PARAM_LONGITUDE, longitude);
        activity.startActivityForResult(intent, requestCode);
    }

}
