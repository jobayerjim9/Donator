package com.teamjhj.donator_247blood.Fragment;

import android.app.Dialog;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.Adapter.CommentsAdapter;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.DataModel.CommentsData;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.DataModel.NotificationData;
import com.teamjhj.donator_247blood.DataModel.NotificationSender;
import com.teamjhj.donator_247blood.DataModel.UserProfile;
import com.teamjhj.donator_247blood.R;
import com.teamjhj.donator_247blood.RestApi.NotificationAPI;
import com.teamjhj.donator_247blood.RestApi.ApiInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentBottomSheetFragment extends BottomSheetDialogFragment {
    NonEmergencyInfo nonEmergencyInfo;
    DatabaseReference databaseReference;
    private BottomSheetBehavior mBehavior;
    private ArrayList<CommentsData> commentsDataList;
    private RecyclerView commentsRecycler;
    private CommentsAdapter commentsAdapter;
    private EditText commentsText;
    private TextView no_comment_label;
    private LottieAnimationView no_comment;
    private AppBarLayout app_bar_layout;
    public CommentBottomSheetFragment(NonEmergencyInfo nonEmergencyInfo) {
        this.nonEmergencyInfo = nonEmergencyInfo;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.cooment_bottom_sheet, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        app_bar_layout=view.findViewById(R.id.app_bar_layout);
        hideView(app_bar_layout);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    showView(app_bar_layout, getActionBarSize());

                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    hideView(app_bar_layout);
                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
//        LinearLayout linearLayout = view.findViewById(R.id.root);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
//        params.height = getScreenHeight();
//        linearLayout.setLayoutParams(params);
//
        databaseReference = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfo.getYear() + "").child(nonEmergencyInfo.getMonth() + "").child(nonEmergencyInfo.getDate() + "").child(nonEmergencyInfo.getKey()).child("comments");
        commentsText = view.findViewById(R.id.commentsText);
        no_comment_label = view.findViewById(R.id.no_comment_label);
        no_comment = view.findViewById(R.id.no_comment);
        commentsText = view.findViewById(R.id.commentsText);
        commentsDataList = new ArrayList<>();
        commentsRecycler = view.findViewById(R.id.commentsRecycler);
        commentsRecycler.setHasFixedSize(true);
        commentsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsAdapter = new CommentsAdapter(getContext(), commentsDataList);
        commentsRecycler.setAdapter(commentsAdapter);
        ImageView sendButtonComments = view.findViewById(R.id.sendButtonComments);
        sendButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComments();
            }
        });
        ImageButton backButtonComments = view.findViewById(R.id.backButtonComments);
        backButtonComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        getCommentsData();

        return dialog;
    }

    private void postComments() {
        String name, message;
        Date date = Calendar.getInstance().getTime();
        name = AppData.getUserProfile().getName();
        message = commentsText.getText().toString();

        if (!message.isEmpty()) {
            commentsText.setText("");

            CommentsData postData = new CommentsData(name, message, FirebaseAuth.getInstance().getUid(), date);

            databaseReference.push().setValue(postData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Comments Posted Successfully", Toast.LENGTH_LONG).show();
                        sendNotification();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Please Write Your Comment First", Toast.LENGTH_LONG).show();
        }

    }

    private void getCommentsData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentsDataList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    CommentsData commentsData = dataSnapshot1.getValue(CommentsData.class);

                    Log.d("Comments", commentsData.getName());
                    commentsDataList.add(commentsData);
                    commentsAdapter.notifyDataSetChanged();
                }
                if (commentsDataList.isEmpty()) {
                    no_comment.setVisibility(View.VISIBLE);
                    no_comment_label.setVisibility(View.VISIBLE);
                } else {
                    no_comment.setVisibility(View.GONE);
                    no_comment_label.setVisibility(View.GONE);
                }
                commentsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void sendNotification() {
        try {
            ApiInterface apiInterface = NotificationAPI.getClient().create(ApiInterface.class);
            DatabaseReference profile = FirebaseDatabase.getInstance().getReference("UserProfile").child(nonEmergencyInfo.getUid());
            profile.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfile postOwner = dataSnapshot.getValue(UserProfile.class);
                    if (postOwner != null && !postOwner.getUid().equals(FirebaseAuth.getInstance().getUid())) {
                        try {
                            String tempToken = AppData.getUserProfile().getToken();
                            Log.e("MyToken", tempToken);
                            String notificationMessage = AppData.getUserProfile().getName() + " Commented On Your Blood Feed Post!";
                            NotificationData notificationData = new NotificationData(notificationMessage, "Blood Feed!!", "Comments");
                            Date date = Calendar.getInstance().getTime();
                            notificationData.setDate(date);
                            NotificationSender notificationSender = new NotificationSender(postOwner.getToken(), notificationData);
                            //NotificationSender notificationSender = new NotificationSender(tempToken, notificationData);
                            DatabaseReference notification = FirebaseDatabase.getInstance().getReference("Notifications").child(postOwner.getUid());
                            notificationData.setYear(nonEmergencyInfo.getYear());
                            notificationData.setMonth(nonEmergencyInfo.getMonth());
                            notificationData.setDay(nonEmergencyInfo.getDate());
                            notificationData.setPostKey(nonEmergencyInfo.getKey());
                            notification.push().setValue(notificationData);
                            Call<ResponseBody> bodyCall = apiInterface.sendNotification(notificationSender);

                            bodyCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Log.e("Response Code", response.code() + "");
                                    Log.e("Error MEssage", response.message());
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

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
