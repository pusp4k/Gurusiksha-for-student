package com.androweb.voyage.CustomListAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.androweb.voyage.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.androweb.voyage.Helper.Constant.PLACES;
import static com.androweb.voyage.Helper.Constant.PLACE_IMG;


public class CustomListAdapter extends ListFragment {
    private static final ArrayList<HashMap<String, String>> data = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        for (int i = 0; i < PLACES.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("Place", PLACES[i]);
            map.put("Image", Integer.toString(PLACE_IMG[i]));

            data.add(map);
        }

        String[] from = {"Place", "Image"};
        int[] to = {R.id.txt_place_name, R.id.img_dest_image};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), data, R.layout.fragment_custom_list_view, from, to);
        setListAdapter(simpleAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        getListView().setOnItemClickListener((adapterView, view, position, id) -> Toast.makeText(getActivity(), data.get(position).get("Place"), Toast.LENGTH_LONG).show());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDividerHeight(0);
    }
}
