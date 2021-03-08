package com.androweb.voyage.CustomListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androweb.voyage.R;
import com.androweb.voyage.utils.DataModel;
import com.androweb.voyage.utils.ViewHolder;

import java.util.ArrayList;


public class CustomListAdapterEvent extends ArrayAdapter<DataModel> {
    final ArrayList<DataModel> dataSet;
    Context mContext;
    boolean recentEvents;
    private int lastPosition = -1;


    public CustomListAdapterEvent(ArrayList<DataModel> dataModels, Context context, boolean recentItems) {
        super(context, R.layout.fragment_last_week_events, dataModels);
        this.dataSet = dataModels;
        this.mContext = context;
        this.recentEvents = recentItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        final View rootView;

        if (recentEvents) {

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.fragment_last_week_events, parent, false);
                viewHolder.events = convertView.findViewById(R.id.events);
                viewHolder.returnDate = convertView.findViewById(R.id.ll_return_date);
                viewHolder.startDate = convertView.findViewById(R.id.txt_event_dt_start);
                viewHolder.startMnt = convertView.findViewById(R.id.txt_event_mnt_start);
                viewHolder.endDate = convertView.findViewById(R.id.txt_event_dt_fin);
                viewHolder.endMnt = convertView.findViewById(R.id.txt_event_mnt_fin);
                viewHolder.eventTime = convertView.findViewById(R.id.txt_event_time);
                viewHolder.amPm = convertView.findViewById(R.id.txt_event_time_am_pm);
                viewHolder.srcName = convertView.findViewById(R.id.txt_event_srcName);
                viewHolder.destName = convertView.findViewById(R.id.txt_event_destName);
                viewHolder.okStatus = convertView.findViewById(R.id.txt_status_completed);
                viewHolder.cancelStatus = convertView.findViewById(R.id.txt_status_canceled);

                rootView = convertView;
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                rootView = convertView;
            }

            Animation animation = AnimationUtils.loadAnimation(mContext, (position >
                    lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            rootView.startAnimation(animation);
            lastPosition = position;

            viewHolder.startDate.setText(dataModel.getStartDate());
            viewHolder.startMnt.setText(dataModel.getStartMnt());

            String endDt = dataModel.getEndDate();
            String endMt = dataModel.getEndMnt();

            if (endDt.equals("00") && endMt.equals("AAA")) {
                viewHolder.returnDate.setVisibility(View.GONE);
            } else {
                viewHolder.returnDate.setVisibility(View.VISIBLE);
                viewHolder.endDate.setText(endDt);
                viewHolder.endMnt.setText(endMt);
            }

            viewHolder.eventTime.setText(dataModel.getEventTime());
            viewHolder.amPm.setText(dataModel.getAmPm());
            viewHolder.srcName.setText(dataModel.getSrcName());
            viewHolder.destName.setText(dataModel.getDestName());

            String stat = dataModel.getStatus();
            if (stat.equalsIgnoreCase("0")) {
                viewHolder.okStatus.setText(R.string.completed);
                viewHolder.okStatus.setVisibility(View.VISIBLE);
                viewHolder.cancelStatus.setVisibility(View.GONE);
            } else {
                viewHolder.cancelStatus.setText(R.string.canceled);
                viewHolder.cancelStatus.setVisibility(View.VISIBLE);
                viewHolder.okStatus.setVisibility(View.GONE);
            }
        } else {
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.fragment_previous_events, parent, false);

                viewHolder.preScrName = convertView.findViewById(R.id.pre_event_src);
                viewHolder.preDesName = convertView.findViewById(R.id.pre_event_des);
                viewHolder.preStartDate = convertView.findViewById(R.id.pre_event_dt_start);
                viewHolder.preStartMnt = convertView.findViewById(R.id.pre_event_mnt_start);

                rootView = convertView;
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                rootView = convertView;
            }

            Animation animation = AnimationUtils.loadAnimation(mContext, (position >
                    lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            rootView.startAnimation(animation);
            lastPosition = position;

            viewHolder.preScrName.setText(dataModel.getSrcName());
            viewHolder.preDesName.setText(dataModel.getDestName());
            viewHolder.preStartDate.setText(dataModel.getStartDate());
            viewHolder.preStartMnt.setText(dataModel.getStartMnt());

        }

        return convertView;
    }

}
