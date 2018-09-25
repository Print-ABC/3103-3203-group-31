package com.ncshare.ncshare;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    EditText e_userId, e_email, e_password, e_name, e_contact, e_organization, e_role;
    String userId, email, password, name, contact, organization, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        e_userId = findViewById(R.id.editTextUserId);
        e_email = findViewById(R.id.editTextEmail);
        e_password = findViewById(R.id.editTextPassword);
        e_name = findViewById(R.id.editTextName);
        e_contact = findViewById(R.id.editTextContact);
        e_organization = findViewById(R.id.editTextOrganization);
        e_role = findViewById(R.id.editTextRole);
    }

    public void registerUser(View view){
        userId = e_name.getText().toString();
        email = e_email.getText().toString();
        password = e_password.getText().toString();
        name = e_name.getText().toString();
        contact = e_contact.getText().toString();
        organization = e_organization.getText().toString();
        role = e_role.getText().toString();

        //Create and run a background task
        String method = "register";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, userId, email, password, name, contact, organization, role);
    }

}
