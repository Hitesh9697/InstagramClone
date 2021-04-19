package com.example.instagramclone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagramclone.databinding.ActivitySignUpBinding;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle("Sign Up");

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Signing Up!");

        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = binding.editTextSignUpPassword.getText().toString();
                String confirmPassword = binding.editTextSignUpConfirmPassword.getText().toString();

                ParseUser user = new ParseUser();
                // Set the user's username and password, which can be obtained by a forms
                if (!binding.editTextSignUpUsername.getText().toString().equals("")
                        && !binding.editTextSignUpEmail.getText().toString().equals("")
                        && !binding.editTextSignUpPassword.getText().toString().equals("")){

                    user.setUsername(binding.editTextSignUpUsername.getText().toString());
                    user.setEmail(binding.editTextSignUpEmail.getText().toString());
                    if (password.equals(confirmPassword)) {
                        user.setPassword(binding.editTextSignUpPassword.getText().toString());
                        progressDialog.show();
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    ParseUser.logOut();
                                    progressDialog.dismiss();
                                    showAlert("Successful Sign Up!", "Welcome " + binding.editTextSignUpUsername.getText().toString() + "!");
                                    Toast.makeText(SignUpActivity.this, "Please login again!", Toast.LENGTH_SHORT).show();
                                } else {
                                    ParseUser.logOut();
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Password does not match. Try again!", Toast.LENGTH_LONG).show();
                        binding.editTextSignUpConfirmPassword.setText("");
                    }

                }
                if (binding.editTextSignUpUsername.getText().toString().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                }
                if (binding.editTextSignUpEmail.getText().toString().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                }
                if (binding.editTextSignUpPassword.getText().toString().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        // don't forget to change the line below with the names of your Activities
                        ParseUser.logOut();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(SignUpActivity.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}