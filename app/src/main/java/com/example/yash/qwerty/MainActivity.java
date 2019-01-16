package com.example.yash.qwerty;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText password;
    EditText username;
    Button button;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password=findViewById(R.id.pass);
        username=findViewById(R.id.user);
        if(mAuth.getCurrentUser()!=null){
            login();
        }

    }

    public void goClicked(View view){
        mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("email").setValue(username.getText().toString());
                            login();
                        } else {
                            mAuth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString());
                            if(task.isSuccessful()) {

                                FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("email").setValue(username.getText().toString());
                                login();
                            }
                            else{

                            Toast.makeText(getApplicationContext(),"Login Failed,Try Again",Toast.LENGTH_SHORT).show();
                        }
                        }
                    }
                });

    }
    void login(){
        FirebaseDatabase.getInstance().getReference().child("users").child("uuid").child("email").setValue(username.getText().toString());

        Intent intent=new Intent(getApplicationContext(),SnapActivity.class);
        intent.putExtra("username",username.getText());
        Log.i("user",username.getText().toString());
        startActivity(intent);

    }
}
