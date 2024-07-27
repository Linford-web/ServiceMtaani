package com.example.smtaani;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smtaani.models.userModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class loginRegister extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    ProgressBar progressbar;
    ImageView passwordIcon;
    boolean valid = true;
    TextView fPassword, registerTxt, loginTxt;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    boolean passwordShowing = false;
    LinearLayout loginLayout, registerLayout;



    EditText name, reg_email, reg_password, number, conPassword;
    Button registerBtn;
    CheckBox bossCheck;
    ProgressBar progressbar2;
    ImageView passwordrIcon, conPasswordIcon;
    CountryCodePicker countryCodePicker;
    boolean conPasswordShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_register);


        fPassword = findViewById(R.id.forgot_password);
        fAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        progressbar = findViewById(R.id.progressbar);
        passwordIcon = findViewById(R.id.passwordIcon);
        registerTxt = findViewById(R.id.registerTxt);
        fStore = FirebaseFirestore.getInstance();
        loginLayout = findViewById(R.id.loginLayout);
        registerLayout = findViewById(R.id.registerLayout);


        name = findViewById(R.id.user_name);
        reg_email = findViewById(R.id.user_email);
        reg_password = findViewById(R.id.user_password);
        conPassword = findViewById(R.id.confirmPassword);
        registerBtn = findViewById(R.id.registerBtn);
        bossCheck = findViewById(R.id.bossCheck);
        number = findViewById(R.id.phone);
        progressbar2 = findViewById(R.id.progressbar2);
        passwordrIcon = findViewById(R.id.passwordrIcon);
        conPasswordIcon = findViewById(R.id.conPasswordIcon);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        loginTxt = findViewById(R.id.loginTxt);

        // country code picker
        countryCodePicker.registerCarrierNumberEditText(number);

        passwordrIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing){
                    reg_password.setTransformationMethod(null);
                    passwordrIcon.setImageResource(R.drawable.visibility_off_icon);
                    passwordShowing = false;
                    // move cursor to the end of the password field
                    reg_password.setSelection(reg_password.getText().length());
                }
                else {
                    reg_password.setTransformationMethod(new PasswordTransformationMethod());
                    passwordrIcon.setImageResource(R.drawable.visibility_on_icon);
                    passwordShowing = true;
                    // move cursor to the end of the password field
                    reg_password.setSelection(reg_password.getText().length());
                }
                // move cursor to the end of the password field
                reg_password.setSelection(password.getText().length());
            }
        });
        conPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conPasswordShowing){
                    conPassword.setTransformationMethod(null);
                    conPasswordIcon.setImageResource(R.drawable.visibility_off_icon);
                    conPasswordShowing = false;
                    // move cursor to the end of the password field
                    conPassword.setSelection(conPassword.getText().length());
                }
                else {
                    conPassword.setTransformationMethod(new PasswordTransformationMethod());
                    conPasswordIcon.setImageResource(R.drawable.visibility_on_icon);
                    conPasswordShowing = true;
                    // move cursor to the end of the password field
                    conPassword.setSelection(conPassword.getText().length());
                }
                // move cursor to the end of the password field
                conPassword.setSelection(conPassword.getText().length());
            }
        });

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(name);
                checkField(reg_email);
                checkField(number);
                checkField(reg_password);
                checkField(conPassword);

                // check if password and confirm password is the same
                if (!(reg_password.getText().toString().equals(conPassword.getText().toString()))){
                    Toast.makeText(loginRegister.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    valid = true;
                }
                if (!countryCodePicker.isValidFullNumber()){
                    number.setError("Invalid Phone Number");
                    return;
                }

                if (valid){

                    progressbar2.setVisibility(View.VISIBLE);
                    // start the user registration process
                    fAuth.createUserWithEmailAndPassword(reg_email.getText().toString(),reg_password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    FirebaseUser user =fAuth.getCurrentUser();

                                    Toast.makeText(loginRegister.this, "Account Created", Toast.LENGTH_SHORT).show();

                                    // specify Access Level
                                    String userType;
                                    if (bossCheck.isChecked()) {
                                        userType = "corporate";
                                    } else {
                                        userType = "normal";
                                    }

                                    userModel users = new userModel("", user.getUid(), name.getText().toString(), reg_email.getText().toString(), number.getText().toString(), userType);
                                    fStore.collection("Users").document(user.getUid()).set(users);

                                    if (fAuth.getCurrentUser() != null) {
                                        startDashboardActivity();
                                    } else {
                                        loginLayout.setVisibility(View.VISIBLE);
                                        registerLayout.setVisibility(View.GONE);
                                    }


                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(loginRegister.this, "Failed to Create Account!", Toast.LENGTH_SHORT).show();
                                    progressbar2.setVisibility(View.GONE);
                                }
                            });

                }

            }
        });

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passwordShowing){
                    password.setTransformationMethod(null);
                    passwordIcon.setImageResource(R.drawable.visibility_off_icon);
                    passwordShowing = false;
                }
                else {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    passwordIcon.setImageResource(R.drawable.visibility_on_icon);
                    passwordShowing = true;
                }
                // move cursor to the end of the password field
                password.setSelection(password.getText().length());
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressbar.setVisibility(View.VISIBLE);
                checkField(email);
                checkField(password);
                if (valid){
                    fAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    checkUserAccessLevel(Objects.requireNonNull(authResult.getUser()).getUid());
                                    startDashboardActivity();
                                    progressbar.setVisibility(View.GONE);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(loginRegister.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });

        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginLayout.setVisibility(View.GONE);
                registerLayout.setVisibility(View.VISIBLE);
            }
        });

        // Deals with the reset password logic
        fPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your Email to Receive Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(loginRegister.this, "Reset Link sent to your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(loginRegister.this, "Error! Resent Link Not Sent."+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });

    }

    private void startDashboardActivity() {
        DocumentReference df = fStore.collection("Users")
                .document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userType = documentSnapshot.getString("userType");
                if (userType != null) {
                    if (userType.equals("corporate") || userType.equals("normal")) {
                        // User is Corporate or Musician
                        startActivity(new Intent(getApplicationContext(), MainDashboard.class));
                        finish();
                    }  else {
                        // Handle other user types if needed
                        Log.d("TAG", "User is neither Corporate nor Musician");
                    }
                } else {
                    // Handle the case where userType is null if needed
                    Log.d("TAG", "userType is null");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), loginRegister.class));
                finish();
            }
        });
    }

    private void checkUserAccessLevel(String uid) {

        DocumentReference df = fStore.collection("Users").document(uid);

        // Extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                // Identify User Access Level
                String userType = documentSnapshot.getString("userType");
                if (userType != null) {
                    if (userType.equals("Corporate") || userType.equals("Musician")) {
                        // User is Corporate or Musician
                        startActivity(new Intent(getApplicationContext(), MainDashboard.class));
                        finish();
                        Toast.makeText(loginRegister.this, "Welcome", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle other user types if needed
                        Log.d("TAG", "User is neither Corporate nor Musician");
                    }
                } else {
                    // Handle the case where userType is null if needed
                    Log.d("TAG", "userType is null");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Failed to get document: " + e.getMessage());
            }
        });
    }

    private void checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }

    }
}