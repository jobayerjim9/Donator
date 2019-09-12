package com.teamjhj.donator_247blood.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.teamjhj.donator_247blood.DataModel.NearbyPlaceData;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;

public class NearbyPlaceAdapter extends RecyclerView.Adapter<NearbyPlaceAdapter.NearbyPlaceViewHolder> {
    private Context context;
    private ArrayList<NearbyPlaceData> nearbyPlaceData;

    public NearbyPlaceAdapter(Context context, ArrayList<NearbyPlaceData> nearbyPlaceData) {
        this.context = context;
        this.nearbyPlaceData = nearbyPlaceData;
    }

    @NonNull
    @Override
    public NearbyPlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NearbyPlaceViewHolder(LayoutInflater.from(context).inflate(R.layout.nearby_place_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyPlaceViewHolder holder, int position) {

        holder.nearbyPlaceName.setText(nearbyPlaceData.get(position).getName());
        holder.nearbyPlaceVincity.setText(nearbyPlaceData.get(position).getVicinity());
//        Log.d("NearbyImage",nearbyPlaceData.get(position).getPhotos().get(0).getPhotoReference() );
        GoogleMap thisMap = holder.mapCurrent;
        if(thisMap != null) {
            LatLng location=new LatLng(nearbyPlaceData.get(position).getGeometry().getLocation().getLat(),nearbyPlaceData.get(position).getGeometry().getLocation().getLng());
            thisMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f));
            thisMap.addMarker(new MarkerOptions().position(location));
            // Set the map type back to normal.
            thisMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }
    }

    @Override
    public int getItemCount() {
        return nearbyPlaceData.size();
    }

    class NearbyPlaceViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback
    {
        MapView nearbyMapView;
        GoogleMap mapCurrent;
        TextView nearbyPlaceName,nearbyPlaceVincity;
        public NearbyPlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            nearbyPlaceName=itemView.findViewById(R.id.nearbyPlaceName);
            nearbyPlaceVincity=itemView.findViewById(R.id.nearbyPlaceVincity);
            nearbyMapView =(MapView) itemView.findViewById(R.id.nearbyMapView);
            if (nearbyMapView != null) {
                // Initialise the MapView
                nearbyMapView.onCreate(null);
                nearbyMapView.onResume();
                // Set the map ready callback to receive the GoogleMap object
                nearbyMapView.getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            mapCurrent = googleMap;
            notifyDataSetChanged();
        }
    }



    @Override
    public void onViewRecycled(@NonNull NearbyPlaceViewHolder holder) {
        if (holder.mapCurrent != null)
        {
            holder.mapCurrent.clear();
            holder.mapCurrent.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }
}
