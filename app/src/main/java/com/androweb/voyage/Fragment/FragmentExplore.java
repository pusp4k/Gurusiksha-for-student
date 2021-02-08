package com.androweb.voyage.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androweb.voyage.R;
import com.androweb.voyage.databinding.FragmentExplorerBinding;

public class FragmentExplore extends Fragment
{

    public static final String TAG = FragmentExplore.class.getSimpleName();
    private FragmentExplorerBinding binding;

    private Context context;

    public static FragmentExplore newInstance(Context mContext) {


        FragmentExplore mFragmentExplore = new FragmentExplore();
        mFragmentExplore.context = mContext;

        return mFragmentExplore;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explorer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
