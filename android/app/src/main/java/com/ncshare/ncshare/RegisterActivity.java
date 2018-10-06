package com.ncshare.ncshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class RegisterActivity extends AppCompatActivity {

    EditText e_userId, e_email, e_password, e_name, e_contact, e_organization;
    String userId, email, password, name, contact, organization, role;
    MaterialBetterSpinner s_role;
    Button btnRegister;
    String[] spinnerRole = {"Student", "Organization"};
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, spinnerRole);
        s_role = findViewById(R.id.spinner_role);
        s_role.setAdapter(arrayAdapter);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = e_name.getText().toString();
                email = e_email.getText().toString();
                password = e_password.getText().toString();
                name = e_name.getText().toString();
                contact = e_contact.getText().toString();
                role = s_role.getText().toString();
                organization = e_organization.getText().toString();
                Log.i("--- INFO ---", userId + ", " + email + ", " + password + ", " + name + ", " + contact + ", " + organization + ", " + role);

                //if success, go login page
                //else stay register page
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    public void registerUser(View view){
        userId = e_name.getText().toString();
        email = e_email.getText().toString();
        password = e_password.getText().toString();
        name = e_name.getText().toString();
        contact = e_contact.getText().toString();
        role = s_role.getText().toString();
        organization = e_organization.getText().toString();

        //Create and run a background task
        String method = "register";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, userId, email, password, name, contact, organization, role);
    }

}
