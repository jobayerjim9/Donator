package com.teamjhj.donator_247blood.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.teamjhj.donator_247blood.Activity.ChatActivity;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;

public class ViewProfileBottomSheet extends BottomSheetDialogFragment {
    private String uid;
    private BottomSheetBehavior mBehavior;
    private Context context;
    public ViewProfileBottomSheet(String uid) {
        this.uid = uid;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.view_profile_bottom_sheet, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        ImageView view_profile_image=view.findViewById(R.id.view_profile_image);
        TextView nameViewProfile=view.findViewById(R.id.nameViewProfile);
        TextView bloodGroupProfile=view.findViewById(R.id.bloodGroupProfile);
        TextView lastDonationDateProfile=view.findViewById(R.id.lastDonationDateProfile);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("UserProfilePicture").child(uid);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).placeholder(R.drawable.com_facebook_profile_picture_blank_square).into(view_profile_image);
            }
        });
        ImageView backButtonViewProfile=view.findViewById(R.id.backButtonViewProfile);
        backButtonViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        ImageView sendMessageViewProfile=view.findViewById(R.id.sendMessageViewProfile);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("UserProfile").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                if(userProfile!=null)
                {
                    sendMessageViewProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(context, ChatActivity.class);
                            i.putExtra("Name",userProfile.getName());
                            i.putExtra("uid", uid);
                            startActivity(i);
                        }
                    });
                    String placeHolder;
                    nameViewProfile.setText(userProfile.getName());
                    bloodGroupProfile.setText(userProfile.getBloodGroup());
                    if (userProfile.getLastDonationYear()==-1)
                    {
                        placeHolder="Not Donated Yet!";
                    }
                    else
                    {
                        placeHolder=userProfile.getLastDonationDate()+"-"+userProfile.getLastDonationMonth()+"-"+userProfile.getLastDonationYear();

                    }

                    lastDonationDateProfile.setText(placeHolder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        return dialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);
    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        return size;
    }
}
