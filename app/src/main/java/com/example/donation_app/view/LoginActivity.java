package com.example.donation_app.view;


import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;
import com.example.donation_app.R;
import com.example.donation_app.contract.AuthContract;
import com.example.donation_app.presenter.AuthPresenter;

public class LoginActivity extends ComponentActivity implements AuthContract.View {

    private EditText emailInput, passwordInput;
    private AuthPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        presenter = new AuthPresenter(this);

        findViewById(R.id.loginButton).setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                presenter.login(email, password);
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.goToRegister).setOnClickListener(v->
                startActivity(new Intent(this, RegisterActivity.class)));

    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, DonorDashboardActivity.class)); // or NGODashboardActivity based on role
        finish();
    }

    @Override
    public void onLoginFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterSuccess() { }

    @Override
    public void onRegisterFailure(String message) { }

}
