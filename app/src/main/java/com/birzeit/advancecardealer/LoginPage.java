package com.birzeit.advancecardealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    LoginDBHelper db;
    EditText emailLogin, passwordLogin;
    CheckBox rememberMeLogin;
    Button loginButton, signupButton;
    GradientDrawable errorDrawable = new GradientDrawable();
    GradientDrawable normalDrawable = new GradientDrawable();
    static String emailStr;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        db = new LoginDBHelper(this);
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        rememberMeLogin = findViewById(R.id.rememberMeLogin);

        loginButton = findViewById(R.id.button_login);
        signupButton = findViewById(R.id.button_signup);

        errorDrawable.setShape(GradientDrawable.RECTANGLE);
        errorDrawable.setStroke(8, Color.parseColor("#FFE80000")); // Stroke width and color
        errorDrawable.setCornerRadius(50);  // Corner radius

        normalDrawable.setShape(GradientDrawable.RECTANGLE);
        normalDrawable.setStroke(8, Color.parseColor("#111414")); // Stroke width and color
        normalDrawable.setCornerRadius(50);  // Corner radius

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String rememberedEmail = settings.getString("rememberedEmail", "");
        emailLogin.setText(rememberedEmail);

        loginButton.setOnClickListener(view -> {
            String email = emailLogin.getText().toString().trim();
            String password = passwordLogin.getText().toString().trim();

            if(db.checkUser(email, password) && !db.checkAdmin(email)){
                if(rememberMeLogin.isChecked()){
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("rememberedEmail", email);
                    editor.apply();
                }
                else{
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("rememberedEmail", "");
                    editor.apply();
                }
                emailStr = email;
                emailLogin.setBackground(normalDrawable);
                passwordLogin.setBackground(normalDrawable);
                Toast.makeText(LoginPage.this, "Login successful", Toast.LENGTH_SHORT).show();
                Intent HomeCustomerintent = new Intent(LoginPage.this, NavigationCustomer.class);
                HomeCustomerintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(HomeCustomerintent);
                //admin
            }else if((email.equals("admin") && password.equals("admin")) || db.checkAdmin(email)){
                emailStr = email;
                emailLogin.setBackground(normalDrawable);
                passwordLogin.setBackground(normalDrawable);
                Toast.makeText(LoginPage.this, "Login Admin successful", Toast.LENGTH_SHORT).show();
                Intent HomeAdminintent = new Intent(LoginPage.this, NavigationAdmin.class);
                HomeAdminintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(HomeAdminintent);
            }else {
                emailLogin.setBackground(errorDrawable);
                passwordLogin.setBackground(errorDrawable);
                Toast.makeText(LoginPage.this, "Login failed", Toast.LENGTH_SHORT).show();
            }

        });

        signupButton.setOnClickListener(view -> {
            Intent Signupintent = new Intent(LoginPage.this, SignupPage.class);
            startActivity(Signupintent);
        });

    }
}