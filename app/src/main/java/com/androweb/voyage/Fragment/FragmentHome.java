package com.androweb.voyage.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androweb.voyage.CustomDialog.CustomProgressDialog;
import com.androweb.voyage.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.androweb.voyage.utils.Utils.bitmapDescriptorFromVector;

public class FragmentHome extends Fragment implements OnMapReadyCallback {
    private final static String TAG = FragmentHome.class.getSimpleName();
    private final static int LOCATION_ERROR_DIALOG = 101;
    public static String PARAM_TITLE = "title";
    public static String PARAM_LATITUDE = "latitude";
    public static String PARAM_LONGITUDE = "longitude";
    public static String RESULT_ADDRESS = "address";
    public static String RESULT_POSTAL_CODE = "pin";
    public static String RESULT_LATITUDE = "latitude";
    public static String RESULT_LONGITUDE = "longitude";
    public static String RESULT_CITY = "city";
    public static String RESULT_COUNTRY = "country";
    private final ArrayList<LatLng> markerPoints = new ArrayList<>();
    // Polyline object
    protected LatLng sourceLatLng;
    protected LatLng destLatLng;
    private GoogleMap gMap;
    private LocationManager locationManager;
    private Location location;
    private TextView textSource;
    private TextView textDestination;
    private ImageButton goNext;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private LatLng latLng;
    private String address;
    private String postalCode;
    private String cityName;
    private String country;
    private String placeName;
    private Marker destMarker;
    private Marker sourceMarker;
    private int height, width;
    private View mapView;
    private CustomProgressDialog customDialog;

    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.container);

        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }

        mapView = supportMapFragment.getView();
        mapView.post(() -> {
            height = mapView.getMeasuredHeight();
            width = mapView.getMeasuredWidth();
        });

        textSource = rootView.findViewById(R.id.txt_source);
        textDestination = rootView.findViewById(R.id.txt_destination);
        goNext = rootView.findViewById(R.id.goNext);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        customDialog = new CustomProgressDialog(requireActivity());
        customDialog.showProgress();

        goNext.setOnClickListener(this::validationCheck);
    }

    private void validationCheck(View v) {
        String strSrc = textSource.getText().toString();
        String strDes = textDestination.getText().toString();

        boolean sourceEmpty = strSrc.equals("");
        boolean destEmpty = strDes.equals("");

        if (sourceEmpty && destEmpty) {
            textSource.setText(R.string.origin_error_msg);
            textDestination.setText(R.string.destination_error_msg);
        } else if (sourceEmpty) {
            textSource.setText(R.string.origin_error_msg);
        } else if (destEmpty) {
            textDestination.setText(R.string.destination_error_msg);
        } else {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            FragmentTripDetails fragmentTripDetails = FragmentTripDetails.newInstance();
            ft.replace(R.id.content_frame, fragmentTripDetails);
            ft.commit();
        }

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        customDialog.processFinished();
        gMap = googleMap;
        gMap.setMyLocationEnabled(true);
        location = getLocation();

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            return;
        }

        sourceLatLng = new LatLng(latitude, longitude);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(sourceLatLng).zoom(12).build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        sourceMarker = gMap.addMarker(new MarkerOptions()
                .position(sourceLatLng)
                .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_source_pin)));

        placeName = getPlaceName(sourceLatLng);

        if (placeName != null) {
            textSource.setText(placeName);
        } else {
            textSource.setText(R.string.location_error_msg);
        }

        markerPoints.add(sourceLatLng);
        setMapListener(gMap);

    }


    private void setMapListener(GoogleMap gMap) {
        gMap.setOnMapClickListener(latLng -> {

            // Remove the previous Destination
            if (markerPoints.size() == 2) {
                markerPoints.remove(1);

                if (destMarker != null) {
                    destMarker.remove();
                }
            }

            destLatLng = latLng;
            // Adding new Item to array list
            markerPoints.add(latLng);

            // Add Marker Points
            destMarker = gMap.addMarker(new MarkerOptions()
                    .position(destLatLng)
                    .icon(bitmapDescriptorFromVector(requireContext(), R.drawable.ic_destination_pin)));

            String destPlaceName = getPlaceName(destLatLng);
            if (destPlaceName != null) {
                textDestination.setText(destPlaceName);
            } else {
                textDestination.setText(R.string.location_error_msg);
            }
            // Check and validate Source and Destination were captured
            if (markerPoints.size() >= 2) {

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(sourceMarker.getPosition());
                builder.include(destMarker.getPosition());

                LatLngBounds bounds = builder.build();
                int padding = (int) (width * 0.10);

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                gMap.animateCamera(cameraUpdate);
            }
        });
    }

    private String getPlaceName(LatLng latLng) {
        Geocoder geocoder = new Geocoder(requireContext());

        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                address = addressList.get(0).getAddressLine(0);
                postalCode = addressList.get(0).getPostalCode();
                cityName = addressList.get(0).getLocality();
                country = addressList.get(0).getCountryName();

                if (cityName != null) {
                    return cityName;
                } else {
                    return "";
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // Build the alert dialog
                showAlertDialog();
            } else {
                // GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            return location;
                        }
                    }
                } else
                // First get location from Network Provider
                {
                    if (location == null) {
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                return location;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    private void showAlertDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogInterface = inflater.inflate(R.layout.dialog_custom, null);
        builder.setView(dialogInterface);

        TextView dialogTitle = dialogInterface.findViewById(R.id.dialogTitle);
        TextView dialogMessage = dialogInterface.findViewById(R.id.dialogMessage);
        Button dialogPositive = dialogInterface.findViewById(R.id.dialogPositive);
        Button dialogNegative = dialogInterface.findViewById(R.id.dialogNegative);

        dialogTitle.setText(R.string.location_error_title);
        dialogMessage.setText(R.string.location_error_message);

        androidx.appcompat.app.AlertDialog alert = builder.create();
        alert.setCancelable(false);

        dialogPositive.setOnClickListener(view -> {
            // Show location settings when the user acknowledges the alert dialog
            alert.dismiss();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

        });
        dialogNegative.setOnClickListener(v -> alert.dismiss());

        alert.show();
    }

}
