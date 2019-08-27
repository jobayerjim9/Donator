package com.teamjhj.donator_247blood.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamjhj.donator_247blood.DataModel.AppData;
import com.teamjhj.donator_247blood.Fragment.PhoneNumberPopupDialog;
import com.teamjhj.donator_247blood.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {
    private final int OTHER_SIGN_IN = 1;
    ProgressDialog progressDialog;
    TextInputLayout mobileNumber, verificationCode;
    Button signInButton, sendAgainButton, submitButton;
    String mobileNumberValue, verificationCodeValue, mVerificationId;
    PhoneAuthProvider phoneAuthProvider;
    TextView againText;
    ImageButton facebookLoginButton;
    SignInButton googleSignIn;
    LottieAnimationView loading_sign_in;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeAutoRetrievalTimeOut(String s) {
            submitButton.setVisibility(View.GONE);
            againText.setVisibility(View.VISIBLE);
            sendAgainButton.setVisibility(View.VISIBLE);
            loading_sign_in.setVisibility(View.GONE);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();


            if (code != null) {
                verificationCodeValue = code;
                verificationCode.getEditText().setText(code);
                verifyVerificationCode(code);
            } else {
                // submitButton.setVisibility(View.INVISIBLE);
                againText.setVisibility(View.VISIBLE);
                sendAgainButton.setVisibility(View.VISIBLE);
                loading_sign_in.setVisibility(View.GONE);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            submitButton.setVisibility(View.GONE);
            againText.setVisibility(View.VISIBLE);
            sendAgainButton.setVisibility(View.VISIBLE);
            loading_sign_in.setVisibility(View.GONE);
            e.printStackTrace();
            Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initializeView();


    }

    private void initializeView() {
        mAuth = FirebaseAuth.getInstance();
        againText = findViewById(R.id.againText);
        mobileNumber = findViewById(R.id.mobileNumber);
        verificationCode = findViewById(R.id.verificationCode);
        loading_sign_in = findViewById(R.id.loading_sign_in);
        loading_sign_in.setVisibility(View.GONE);
        signInButton = findViewById(R.id.signInButton);
        sendAgainButton = findViewById(R.id.sendAgainButton);
        facebookLoginButton = findViewById(R.id.facebookLoginButton);
        googleSignIn = findViewById(R.id.googleSignIn);
        submitButton = findViewById(R.id.submitButton);

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookSignIn();
            }
        });
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmailSignIn();
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                signInButtonAction();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = verificationCode.getEditText().getText().toString();
                if (otp.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Please Enter Otp", Toast.LENGTH_LONG).show();
                } else {
                    verifyVerificationCode(otp);
                }
            }
        });
        sendAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                againText.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);
                sendAgainButton.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);
                mobileNumber.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);
                verificationCode.setVisibility(View.GONE);
                facebookLoginButton.setVisibility(View.VISIBLE);
                googleSignIn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void gmailSignIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                OTHER_SIGN_IN
        );
    }

    private void facebookSignIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                OTHER_SIGN_IN

        );
    }

    private void signInButtonAction() {

        mobileNumberValue = Objects.requireNonNull(mobileNumber.getEditText()).getText().toString().trim();
        System.out.println("MOBILE 1 " + mobileNumberValue);
        if (mobileNumberValue.isEmpty()) {
            mobileNumber.setErrorEnabled(true);
            mobileNumber.setError("Please Enter Your Mobile Number");
        } else if (mobileNumberValue.contains("+")) {
            mobileNumber.setErrorEnabled(true);
            mobileNumber.setError("Please Enter Number Only. Country Code Doesn't Required!");
        } else if (mobileNumberValue.length() != 11) {
            mobileNumber.setErrorEnabled(true);
            mobileNumber.setError("Please Enter Valid Phone Number(11 Digit)");
        } else {
            submitButton.setVisibility(View.VISIBLE);

            mobileNumberValue = "+88" + mobileNumberValue;
            AppData.setMobileNumber(mobileNumberValue);
            loading_sign_in.setVisibility(View.VISIBLE);
            facebookLoginButton.setVisibility(View.GONE);
            googleSignIn.setVisibility(View.GONE);
            new SignInAction().execute();
        }

    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance();
        phoneAuthProvider.verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void verifyVerificationCode(String otp) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            validate();

                            //verification successful we will start the profile activity


                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Toast.makeText(SignInActivity.this, message, Toast.LENGTH_LONG);
                        }
                    }
                });
    }

    private void validate() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        final DatabaseReference mobileNumber = databaseReference;
        Toast.makeText(this, "Please Wait! Verifying Your Number", Toast.LENGTH_LONG).show();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                } else {
                    AppData.setMobileNumber(mobileNumberValue);
                    startActivity(new Intent(SignInActivity.this, SignUpActivity.class));

                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == OTHER_SIGN_IN) {
                final IdpResponse response = IdpResponse.fromResultIntent(data);

                if (resultCode == RESULT_OK) {
                    // Successfully signed in

                    loginWIthOtherMethod(response);
                    // ...
                } else {
                    assert response != null;
                    Toast.makeText(SignInActivity.this, Objects.requireNonNull(response.getError()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loginWIthOtherMethod(final IdpResponse response) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    finish();
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                } else {
                    assert response != null;
                    if (response.getPhoneNumber() != null) {

                        AppData.setMobileNumber(response.getPhoneNumber());
                        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                    } else {
                        Toast.makeText(SignInActivity.this, "Mobile Number Not Found On Your Facebook Account!", Toast.LENGTH_LONG).show();
                        DialogFragment phoneNumberPopupDialog = new PhoneNumberPopupDialog();
                        phoneNumberPopupDialog.show(getSupportFragmentManager(), "phoneNumberPopupDialog");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.teamjhj.donator_247blood",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class SignInAction extends AsyncTask {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SignInActivity.this);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            sendVerificationCode(mobileNumberValue);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mobileNumber.setVisibility(View.GONE);
                    signInButton.setVisibility(View.GONE);
                    verificationCode.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
