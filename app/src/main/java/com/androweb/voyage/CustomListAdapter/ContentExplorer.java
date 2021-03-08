package com.androweb.voyage.CustomListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androweb.voyage.R;
import com.androweb.voyage.utils.ViewHolder;

public class ContentExplorer extends ArrayAdapter<String> {

    private final String[] name;
    private final int[] img;
    Context mContext;
    private int lastPosition = -1;


    public ContentExplorer(Context context, String[] places, int[] placeImg) {
        super(context, R.layout.fragment_custom_list_view, places);
        this.mContext = context;
        this.name = places;
        this.img = placeImg;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        final View rootView;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fragment_custom_list_view, parent, false);
            viewHolder.contentImg = convertView.findViewById(R.id.img_dest_image);
            viewHolder.contentName = convertView.findViewById(R.id.txt_place_name);

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

        viewHolder.contentName.setText(name[position]);
        viewHolder.contentImg.setImageResource(img[position]);
        return convertView;

    }
}
