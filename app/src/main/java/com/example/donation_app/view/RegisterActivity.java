package com.example.donation_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import com.example.donation_app.R;
import com.example.donation_app.presenter.AuthPresenter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput;
    private RadioGroup roleGroup;
    private CheckBox volunteerCheckBox;
    private AuthPresenter presenter;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        roleGroup = findViewById(R.id.roleGroup);
        volunteerCheckBox = findViewById(R.id.volunteerCheckBox);
        signupButton = findViewById(R.id.registerButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                int selectedRoleId = roleGroup.getCheckedRadioButtonId();
                String role = selectedRoleId == R.id.radioDonor ? "donor" : "ngo";
                boolean isVolunteer = volunteerCheckBox.isChecked();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    HelperClass helperClass = new HelperClass(name, email, role, password, isVolunteer);
                    reference.child(name).setValue(helperClass);
                    Toast.makeText(RegisterActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
