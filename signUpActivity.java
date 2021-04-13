package com.example.account;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;


public class signUpActivity extends AppCompatActivity {
    private static final String TAG = "signUpActivity";
    private Uri ImageUri;
    private String pathUri;
    private File tempFile;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseStorage mStorage; //이미지 저장하는 storage
    private DatabaseReference mPostReference;
    private WebView webView;
    private TextView txt_address;
    private Handler handler;






    public signUpActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();
        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signUpButton:
                    signUp();
                    break;
            }
        }
    };

    String id;
    String password;
    String name;
    Long birth;
    String gender;








    private void signUp() {
        id = ((EditText)findViewById(R.id.userId)).getText().toString();
        password = ((EditText)findViewById(R.id.userPw)).getText().toString();
        name = ((EditText)findViewById(R.id.name)).getText().toString();
        birth = Long.valueOf(((EditText)findViewById(R.id.birth)).getText().toString());
        gender = ((EditText)findViewById(R.id.gender)).getText().toString();


        mAuth.createUserWithEmailAndPassword(id, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            final String uid = task.getResult().getUser().getUid();
                            UserModel userModel = new UserModel();

                            userModel.name = name;
                            userModel.birth = birth;
                            userModel.gender = gender;

                            mDatabase.getReference().child("users").child(uid).setValue(userModel);

                            Log.d(TAG, "회원가입이 완료되었습니다.");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //UI
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "회원가입에 실패하였습니다.", task.getException());
                            //UI
                        }
                    }
                });

    }

}