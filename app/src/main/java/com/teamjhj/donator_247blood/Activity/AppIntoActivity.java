package com.teamjhj.donator_247blood.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.google.firebase.auth.FirebaseAuth;
import com.teamjhj.donator_247blood.R;

public class AppIntoActivity extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SliderPage sliderPage=new SliderPage();
        sliderPage.setImageDrawable(R.drawable.logo_white);
        sliderPage.setBgColor(Color.parseColor("#EF5350"));
        sliderPage.setTitle("Welcome To Donator");
        sliderPage.setDescription("Let's Take A Tour!");
        addSlide(AppIntroFragment.newInstance(sliderPage));


        SliderPage sliderPage2=new SliderPage();
        sliderPage2.setImageDrawable(R.drawable.app_intro_1);
        sliderPage2.setBgColor(Color.parseColor("#EF5350"));
        sliderPage2.setTitle("Emergency Search!");
        sliderPage2.setDescription("When You Need Blood Urgently Use Emergency Search By Providing These Information!");
        addSlide(AppIntroFragment.newInstance(sliderPage2));

        SliderPage sliderPage3=new SliderPage();
        sliderPage3.setImageDrawable(R.drawable.app_intro_2);
        sliderPage3.setBgColor(Color.parseColor("#EF5350"));
        sliderPage3.setTitle("You WIll Get Nearest Donors!");
        sliderPage3.setDescription("You Can Place Request By Swiping! Donor Will Get Request Notification");
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        SliderPage sliderPage31=new SliderPage();
        sliderPage31.setImageDrawable(R.drawable.app_intro_31);
        sliderPage31.setBgColor(Color.parseColor("#EF5350"));
        sliderPage31.setTitle("View Donor Profile!");
        sliderPage31.setDescription("You Can View Donor Profile By Click On Profile Picture!");
        addSlide(AppIntroFragment.newInstance(sliderPage31));

        SliderPage sliderPage32=new SliderPage();
        sliderPage32.setImageDrawable(R.drawable.app_intro_32);
        sliderPage32.setBgColor(Color.parseColor("#EF5350"));
        sliderPage32.setTitle("View Your Emergency Requests!");
        sliderPage32.setDescription("You Can See The Donors Who Accepted Your Request! You Can Also Press 'Blood Received' To The Specific Person");
        addSlide(AppIntroFragment.newInstance(sliderPage32));




        SliderPage sliderPage5=new SliderPage();
        sliderPage5.setImageDrawable(R.drawable.app_intro_4);
        sliderPage5.setBgColor(Color.parseColor("#EF5350"));
        sliderPage5.setTitle("Non Emergency Search!");
        sliderPage5.setDescription("When You Need Blood Tomorrow Or Later! Use Non Emergency Search By Providing These Information! Everyone Will See Your Post On Blood Feed");
        addSlide(AppIntroFragment.newInstance(sliderPage5));

        SliderPage sliderPage6=new SliderPage();
        sliderPage6.setImageDrawable(R.drawable.app_intro_5);
        sliderPage6.setBgColor(Color.parseColor("#EF5350"));
        sliderPage6.setTitle("View Your Blood Feed Post");
        sliderPage6.setDescription("You Can See Opt In Donors On Your Post By Tapping The Post! You Also Can Confirm 'Blood Received' Like Emergency Search");
        addSlide(AppIntroFragment.newInstance(sliderPage6));

        SliderPage sliderPage7=new SliderPage();
        sliderPage7.setImageDrawable(R.drawable.app_intro_6);
        sliderPage7.setBgColor(Color.parseColor("#EF5350"));
        sliderPage7.setTitle("Blood Feed!");
        sliderPage7.setDescription("You Can Help Someone By Donating Blood From Blood Feed! You Can Contact With The Requester By Opt In/Calling/Commenting/In App Messaging");
        addSlide(AppIntroFragment.newInstance(sliderPage7));

        SliderPage sliderPage71=new SliderPage();
        sliderPage71.setImageDrawable(R.drawable.app_intro_13);
        sliderPage71.setBgColor(Color.parseColor("#EF5350"));
        sliderPage71.setTitle("Share To The World!");
        sliderPage71.setDescription("You Can Share Help Post Anywhere!");
        addSlide(AppIntroFragment.newInstance(sliderPage71));

        SliderPage sliderPage8=new SliderPage();
        sliderPage8.setImageDrawable(R.drawable.app_intro_7);
        sliderPage8.setBgColor(Color.parseColor("#EF5350"));
        sliderPage8.setTitle("All App Menu");
        sliderPage8.setDescription("Navigate All Menu From Menu Tab!");
        addSlide(AppIntroFragment.newInstance(sliderPage8));

        SliderPage sliderPage9=new SliderPage();
        sliderPage9.setImageDrawable(R.drawable.app_intro_8);
        sliderPage9.setBgColor(Color.parseColor("#EF5350"));
        sliderPage9.setTitle("In App Messaging!");
        sliderPage9.setDescription("Contact Donor Through In App Messaging!");
        addSlide(AppIntroFragment.newInstance(sliderPage9));

        SliderPage sliderPage10=new SliderPage();
        sliderPage10.setImageDrawable(R.drawable.app_intro_9);
        sliderPage10.setBgColor(Color.parseColor("#EF5350"));
        sliderPage10.setTitle("Notifications!");
        sliderPage10.setDescription("Your All Notification Will Took This Place!");
        addSlide(AppIntroFragment.newInstance(sliderPage10));

        SliderPage sliderPage11=new SliderPage();
        sliderPage11.setImageDrawable(R.drawable.app_intro_10);
        sliderPage11.setBgColor(Color.parseColor("#EF5350"));
        sliderPage11.setTitle("Donate Blood!");
        sliderPage11.setDescription("You Can Accept Request If You Are Available To Donate! You Will Not Get Any Notification Until Your 90 Days Over!");
        addSlide(AppIntroFragment.newInstance(sliderPage11));

        SliderPage sliderPage12=new SliderPage();
        sliderPage12.setImageDrawable(R.drawable.app_intro_11);
        sliderPage12.setBgColor(Color.parseColor("#EF5350"));
        sliderPage12.setTitle("Update Your History");
        sliderPage12.setDescription("If Someone Confirm You As A Donor!\nYou Will Get A Popup Notification! Accepting The Request Will Update Your Last Donation Date!");
        addSlide(AppIntroFragment.newInstance(sliderPage12));

        SliderPage sliderPage14=new SliderPage();
        sliderPage14.setImageDrawable(R.drawable.app_intro_12);
        sliderPage14.setBgColor(Color.parseColor("#EF5350"));
        sliderPage14.setTitle("Nearby Places!");
        sliderPage14.setDescription("You Can Find Nearby Places: Hospital, Blood Banks, Clinics, Police Stations, Pharmacy, NGOs");
        addSlide(AppIntroFragment.newInstance(sliderPage14));


        SliderPage sliderPage13=new SliderPage();
        sliderPage13.setImageDrawable(R.drawable.logo_white);
        sliderPage13.setBgColor(Color.parseColor("#EF5350"));
        sliderPage13.setTitle("Congratulation!");
        sliderPage13.setDescription("Hope You Will Appreciate Us By Giving Us 5* On Google Play!");
        addSlide(AppIntroFragment.newInstance(sliderPage13));






        setSeparatorColor(Color.parseColor("#ffffff"));

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            finish();
        }
        else {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            finish();
        }
        else {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            finish();
        }
        else {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }
}
