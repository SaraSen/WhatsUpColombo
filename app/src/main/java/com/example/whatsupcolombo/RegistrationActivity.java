package com.example.whatsupcolombo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText Useranme;
    private EditText Email;
    private EditText Password;
    private EditText ConfirmPassword;
    private Button Register;
    private TextView GoBack;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setUpUI();

        //creating an object from firebase class
        firebaseAuth = FirebaseAuth.getInstance();

        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    String User_email = Email.getText().toString().trim();//remove whitespaces user entered by mistake
                    String User_password = Password.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(User_email,User_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                //inside a loop so context may not be recognized
                                Toast.makeText(RegistrationActivity.this,"you have successfully registers",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                            }else {
                                Toast.makeText(RegistrationActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }


    private void setUpUI() {
        //assign mentrioned variables to respective xml ida
        Useranme = (EditText) findViewById(R.id.etUserName);
        Email = (EditText) findViewById(R.id.etEmail);
        Password = (EditText) findViewById(R.id.etPassword);
        ConfirmPassword = (EditText) findViewById(R.id.etConfPassword);
        Register = (Button) findViewById(R.id.btnRegister);
        GoBack = (TextView) findViewById(R.id.tvGoBack);
    }

    private Boolean validate() {

         Boolean Result = false;

         //check if content is empty if it is display message
        if (Useranme.getText().toString().isEmpty() || Password.getText().toString().isEmpty() || Email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter the required details", Toast.LENGTH_SHORT).show();

            //if passwords != confpassword display message
            if (!(Password.getText().toString().equalsIgnoreCase(ConfirmPassword.getText().toString()))) {
                Toast.makeText(this, "Password and Confirm psswords should match", Toast.LENGTH_LONG).show();
            }
        } else {
            Result = true;
        }
        return Result;


    }
}
