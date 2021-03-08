package com.androweb.voyage.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androweb.voyage.CustomListAdapter.CustomListAdapterEvent;
import com.androweb.voyage.R;
import com.androweb.voyage.utils.DataModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class FragmentSchedule extends Fragment {
    ArrayList<DataModel> recentDataModels = new ArrayList<>();
    ArrayList<DataModel> previousDataModels = new ArrayList<>();
    LinearLayout recentEvents;
    LinearLayout futureEvents;

    public static FragmentSchedule newInstance() {
        return new FragmentSchedule();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        TextView noDataFound = rootView.findViewById(R.id.txt_no_data_found);
        TextView priNoDataFound = rootView.findViewById(R.id.pri_txt_no_data_found);
        recentEvents = rootView.findViewById(R.id.ll_past_event);
        futureEvents = rootView.findViewById(R.id.ll_future_events);


        ListView recentListView = rootView.findViewById(R.id.event_details);
        ListView previousListView = rootView.findViewById(R.id.previous_event_details);
        boolean recentItems = getRecentItemDetails();
        if(recentItems) {
            recentListView.setAdapter(new CustomListAdapterEvent(recentDataModels, getActivity(), recentItems));
        } else {
            noDataFound.setVisibility(View.VISIBLE);
        }

        recentItems = getPreviousItemDetails();
        if(!recentItems) {
            previousListView.setAdapter(new CustomListAdapterEvent(previousDataModels, getActivity(), recentItems));
        } else {
            priNoDataFound.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    private boolean getPreviousItemDetails() {
        try {
            InputStream inputStream = Objects.requireNonNull(getContext()).getAssets().open("previous_schedule_events.xml");

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);

            Element element = document.getDocumentElement();
            element.normalize();

            NodeList nodeList = document.getElementsByTagName("event");

            if (nodeList != null) {

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element1 = (Element) node;

                        String stDt = ((String) getValue("startdate", element1));
                        String stMnt = ((String) getValue("startmnt", element1));
                        String srcName = ((String) getValue("origin", element1));
                        String destName = ((String) getValue("destination", element1));

                        previousDataModels.add(new DataModel(stDt, stMnt, srcName, destName));
                    }
                }
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean getRecentItemDetails() {
        try {
            InputStream inputStream = Objects.requireNonNull(getContext()).getAssets().open("schedule_events.xml");

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);

            Element element = document.getDocumentElement();
            element.normalize();

            NodeList nodeList = document.getElementsByTagName("event");

            if (nodeList != null) {

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element1 = (Element) node;

                        String stDt = ((String) getValue("startdate", element1));
                        String stMnt = ((String) getValue("startmnt", element1));

                        String endDt = ((String) getValue("endDate", element1));
                        String endMnt = ((String) getValue("endMnt", element1));

                        String eventTime = ((String) getValue("time", element1));
                        String amPm = ((String) getValue("ampm", element1));
                        String srcName = ((String) getValue("origin", element1));
                        String destName = ((String) getValue("destination", element1));

                        String status = ((String) getValue("status", element1));

                        recentDataModels.add(new DataModel(stDt, stMnt, endDt, endMnt
                                , eventTime, amPm, srcName, destName, status));

                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Object getValue(String dta, Element element) {
        NodeList nodeList = element.getElementsByTagName(dta).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
