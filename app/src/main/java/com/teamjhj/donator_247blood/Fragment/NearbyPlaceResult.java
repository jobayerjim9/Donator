package com.teamjhj.donator_247blood.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamjhj.donator_247blood.Adapter.NearbyPlaceAdapter;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;

public class NearbyPlaceResult extends DialogFragment {
    private Context context;
    public static NearbyPlaceResult newInstance()
    {
        return new NearbyPlaceResult();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.NearbyTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nearby_place_result_dialog, container, false);

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
}
