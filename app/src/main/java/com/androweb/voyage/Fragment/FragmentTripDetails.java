package com.androweb.voyage.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androweb.voyage.R;

public class FragmentTripDetails extends Fragment {

    private TextView textSource;
    private TextView textDestination;
    private Button btnOneWay;
    private Button btnReturn;
    private Button btnOther;
    private LinearLayout returningLayout;


    public static FragmentTripDetails newInstance() {
        return new FragmentTripDetails();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trip_details, container, false);

        textSource = rootView.findViewById(R.id.txt_source);
        textDestination = rootView.findViewById(R.id.txt_dest);
        btnOneWay = rootView.findViewById(R.id.btn_one_way);
        btnReturn = rootView.findViewById(R.id.btn_return);
        btnOther = rootView.findViewById(R.id.btn_other);
        returningLayout = rootView.findViewById(R.id.returning_layout);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set one-way button selected
        btnOneWay.setSelected(true);

        setClickListener();
    }

    private void setClickListener() {
        btnOneWay.setOnClickListener(v -> {
            if(returningLayout.getVisibility() == View.VISIBLE) {
                returningLayout.setVisibility(View.INVISIBLE);

            }
            btnOneWay.setSelected(true);
            btnReturn.setSelected(false);
            btnOther.setSelected(false);
        });

        btnReturn.setOnClickListener(v -> {
            btnOneWay.setSelected(false);
            btnReturn.setSelected(true);
            btnOther.setSelected(false);

            if(returningLayout.getVisibility() == View.INVISIBLE) {
                returningLayout.setVisibility(View.VISIBLE);
            }

        });

        btnOther.setOnClickListener(v -> {
            btnOneWay.setSelected(false);
            btnReturn.setSelected(false);
            btnOther.setSelected(true);

            if(returningLayout.getVisibility() == View.INVISIBLE) {
                returningLayout.setVisibility(View.VISIBLE);
            }
        });
    }


}
