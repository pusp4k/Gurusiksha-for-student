package com.androweb.voyage.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androweb.voyage.CustomListAdapter.ContentExplorer;
import com.androweb.voyage.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.androweb.voyage.Fragment.FragmentHome.getLocation;
import static com.androweb.voyage.Fragment.FragmentHome.getPlaceName;
import static com.androweb.voyage.Helper.Constant.PLACES;
import static com.androweb.voyage.Helper.Constant.PLACE_IMG;


public class FragmentExplorer extends Fragment {

    public static final String TAG = FragmentExplorer.class.getSimpleName();
    private ListView contentExplore;

    public static FragmentExplorer newInstance() {
        return new FragmentExplorer();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explorer, container, false);
        contentExplore = rootView.findViewById(R.id.custom_list);
        ContentExplorer explorer = new ContentExplorer(getActivity(), PLACES, PLACE_IMG);
        contentExplore.setAdapter(explorer);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contentExplore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Location currentLocation = getLocation(requireContext());
                LatLng currentLatLng;
                String srcName;
                if (currentLocation != null) {
                    currentLatLng = new LatLng(currentLocation.getLatitude(),
                            currentLocation.getLongitude());
                } else {
                    currentLatLng = null;
                }

                if (currentLatLng != null) {
                    srcName = getPlaceName(currentLatLng, requireContext());
                } else {
                    srcName = "Invalid";
                }

                List<String> list = Arrays.asList(PLACES);
                String desName = list.get(position);

                LatLng destLatLng = getLatLng(desName);

                if(!srcName.isEmpty() && !desName.isEmpty() && currentLatLng != null && destLatLng != null) {
                    openTripDetailsFragment(srcName,desName,currentLatLng,destLatLng);
                } else {
                    Snackbar.make(requireView(), "Some thing went wrong",Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void openTripDetailsFragment(String srcName, String desName, LatLng currentLatLng, LatLng destLatLng) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fragmentManager).beginTransaction();
        FragmentTripDetails fragmentTripDetails = FragmentTripDetails.newInstance(srcName, desName, currentLatLng, destLatLng);
        ft.replace(R.id.content_frame, fragmentTripDetails);
        ft.commit();
    }

    private LatLng getLatLng(String desName) {
        try {

            Geocoder geocoder = new Geocoder(requireContext());
            List<Address> addresses = geocoder.getFromLocationName(desName, 5);

            LatLng latLng;
            if (addresses.size() > 0) {

                for (Address address : addresses) {
                    if (address.hasLatitude() && address.hasLongitude()) {
                        latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        return latLng;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
