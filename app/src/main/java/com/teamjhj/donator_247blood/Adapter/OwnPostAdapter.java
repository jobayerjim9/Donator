package com.teamjhj.donator_247blood.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamjhj.donator_247blood.DataModel.NonEmergencyInfo;
import com.teamjhj.donator_247blood.Fragment.BloodFeedFragment;
import com.teamjhj.donator_247blood.Fragment.BloodFeedRequestFragment;
import com.teamjhj.donator_247blood.Fragment.CommentBottomSheetFragment;
import com.teamjhj.donator_247blood.R;

import java.util.ArrayList;

public class OwnPostAdapter extends RecyclerView.Adapter<OwnPostAdapter.OwnPostViewHolder> {
    private Context ctx;
    private ArrayList<NonEmergencyInfo> nonEmergencyInfos;
    private FragmentManager fm;

    public OwnPostAdapter(Context ctx, ArrayList<NonEmergencyInfo> nonEmergencyInfos, FragmentManager fm) {
        this.ctx = ctx;
        this.nonEmergencyInfos = nonEmergencyInfos;
        this.fm = fm;
    }

    @NonNull
    @Override
    public OwnPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OwnPostViewHolder(LayoutInflater.from(ctx).inflate(R.layout.my_posts_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final OwnPostViewHolder holder, final int position) {

        holder.reasonMyPost.setText(nonEmergencyInfos.get(position).getReason());


        String placeHolder = nonEmergencyInfos.get(position).getDate() + "-" + nonEmergencyInfos.get(position).getMonth() + "-" + nonEmergencyInfos.get(position).getYear();
        holder.dateMyPost.setText(placeHolder);
        String blood1, blood2;
        if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("A+")) {
            blood1 = "A+";
            blood2 = "A Positive";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("A-")) {
            blood1 = "A-";
            blood2 = "A Negative";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("AB+")) {
            blood1 = "AB+";
            blood2 = "AB Positive";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("AB-")) {
            blood1 = "AB-";
            blood2 = "AB Negative";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("B+")) {
            blood1 = "B+";
            blood2 = "B Positive";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("B-")) {
            blood1 = "B-";
            blood2 = "B Negative";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("O+")) {
            blood1 = "O+";
            blood2 = "O Positive";
        } else if (nonEmergencyInfos.get(position).getSelectedBloodGroup().contains("O-")) {
            blood1 = "O-";
            blood2 = "O Negative";
        } else {
            blood1 = "";
            blood2 = "";
        }

        holder.bloodGroupLargeMyPost.setText(blood1);
        holder.bloodGroupSmallMyPost.setText(blood2);
        try {
            placeHolder = nonEmergencyInfos.get(position).getBags();

            holder.bagMyPost.setText(placeHolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.commentMyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = ((AppCompatActivity) ctx).getSupportFragmentManager();
                CommentBottomSheetFragment comments = new CommentBottomSheetFragment(nonEmergencyInfos.get(position));
                comments.show(manager, "CommentsOwn");
            }
        });
        holder.removeMyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ctx)
                        .setTitle("Remove Post?")
                        .setMessage("Are you sure you want to Remove this Post?")

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference post = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfos.get(position).getYear() + "").child(nonEmergencyInfos.get(position).getMonth() + "").child(nonEmergencyInfos.get(position).getDate() + "").child(nonEmergencyInfos.get(position).getKey());
                                post.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ctx, "Removed Successfully", Toast.LENGTH_LONG).show();
                                        BloodFeedFragment.getDataFromDatabase();
                                    }
                                });
                            }
                        })

                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        holder.my_post_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BloodFeedRequestFragment bloodFeedRequestFragment = new BloodFeedRequestFragment(nonEmergencyInfos.get(position));
                //bloodFeedRequestFragment.setCancelable(false);
                bloodFeedRequestFragment.show(fm, "BloodFeedRequest");
            }
        });


//        holder.nameOwn.setText(nonEmergencyInfos.get(position).getName());
//        holder.bloodOwn.setText(nonEmergencyInfos.get(position).getSelectedBloodGroup());
//        holder.mobileOwn.setText(nonEmergencyInfos.get(position).getPhone());
//        holder.locationOwn.setText(nonEmergencyInfos.get(position).getLocation());
//        String placeHolder = nonEmergencyInfos.get(position).getDate() + "-" + nonEmergencyInfos.get(position).getMonth() + "-" + nonEmergencyInfos.get(position).getYear();
//        holder.dateOwn.setText(placeHolder);
//        int hour = nonEmergencyInfos.get(position).getHour();
//        String amPm;
//        if (hour > 12) {
//            hour = hour - 12;
//            amPm = " PM";
//
//        } else {
//            amPm = " AM";
//        }
//        placeHolder = hour + " : " + nonEmergencyInfos.get(position).getMinute() + amPm;
//        holder.timeOwn.setText(placeHolder);
//        holder.reasonOwn.setText(nonEmergencyInfos.get(position).getReason());
//
//        holder.viewCommentsOwnPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager manager = ((AppCompatActivity)ctx).getSupportFragmentManager();
//                CommentBottomSheetFragment comments=new CommentBottomSheetFragment(nonEmergencyInfos.get(position));
//                comments.show(manager,"CommentsOwn");
//            }
//        });
//        holder.popupMenuOwnPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popupMenu=new PopupMenu(ctx,holder.popupMenuOwnPost);
//                popupMenu.inflate(R.menu.own_post_menu);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        if(menuItem.getItemId()==R.id.editProfile)
//                        {
//                            AppData.setEditPostInfo(nonEmergencyInfos.get(position));
//                            ctx.startActivity(new Intent(ctx, EditPostActivity.class));
//                        }
//                        else if(menuItem.getItemId()==R.id.removePost)
//                        {
//                            new AlertDialog.Builder(ctx)
//                                    .setTitle("Remove Post?")
//                                    .setMessage("Are you sure you want to Remove this Post?")
//
//                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            DatabaseReference post = FirebaseDatabase.getInstance().getReference("NonEmergencyRequests").child(nonEmergencyInfos.get(position).getYear() + "").child(nonEmergencyInfos.get(position).getMonth() + "").child(nonEmergencyInfos.get(position).getDate() + "").child(nonEmergencyInfos.get(position).getKey());
//                                            post.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    Toast.makeText(ctx,"Removed Successfully",Toast.LENGTH_LONG).show();
//                                                    BloodFeedFragment.getDataFromDatabase();
//                                                }
//                                            });
//                                        }
//                                    })
//
//                                    .setNegativeButton("No", null)
//                                    .setIcon(android.R.drawable.ic_dialog_alert)
//                                    .show();
//                        }
//                        return false;
//                    }
//                });
//                popupMenu.show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return nonEmergencyInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    class OwnPostViewHolder extends RecyclerView.ViewHolder {
        TextView reasonMyPost, dateMyPost, bagMyPost, bloodGroupLargeMyPost, bloodGroupSmallMyPost;
        ImageView commentMyPost, removeMyPost;
        CardView my_post_card;
        OwnPostViewHolder(@NonNull View itemView) {
            super(itemView);
            reasonMyPost = itemView.findViewById(R.id.reasonMyPost);
            dateMyPost = itemView.findViewById(R.id.dateMyPost);
            bagMyPost = itemView.findViewById(R.id.bagMyPost);
            bloodGroupLargeMyPost = itemView.findViewById(R.id.bloodGroupLargeMyPost);
            bloodGroupSmallMyPost = itemView.findViewById(R.id.bloodGroupSmallMyPost);
            commentMyPost = itemView.findViewById(R.id.commentMyPost);
            removeMyPost = itemView.findViewById(R.id.removeMyPost);
            my_post_card = itemView.findViewById(R.id.my_post_card);


        }
    }
}
