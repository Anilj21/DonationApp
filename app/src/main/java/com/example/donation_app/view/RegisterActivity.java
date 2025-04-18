package com.example.donation_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;
import com.example.donation_app.R;
import com.example.donation_app.contract.AuthContract;
import com.example.donation_app.presenter.AuthPresenter;

public class RegisterActivity extends ComponentActivity implements AuthContract.View {

    private EditText nameInput, emailInput, passwordInput;
    private RadioGroup roleGroup;
    private CheckBox volunteerCheckBox;
    private AuthPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        roleGroup = findViewById(R.id.roleGroup);
        volunteerCheckBox = findViewById(R.id.volunteerCheckBox);
        presenter = new AuthPresenter(this);

        findViewById(R.id.registerButton).setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            int selectedRoleId = roleGroup.getCheckedRadioButtonId();
            String role = selectedRoleId == R.id.radioDonor ? "donor" : "ngo";
            boolean isVolunteer = volunteerCheckBox.isChecked();

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                presenter.register(name, email, password, role, isVolunteer);
            } else {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRegisterSuccess() {
        Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onRegisterFailure(String message) {
        Toast.makeText(this, "Registration Failed: " + message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLoginSuccess() {} // Not used here
    @Override
    public void onLoginFailure(String message) {} // Not used here
}
