package fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ncshare.ncshare.R;

import common.SessionHandler;
import common.Utils;
import models.Organization;
import models.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNCFragment extends Fragment {

    private static final String TAG = "CreateNCFragment";
    private SessionHandler session;
    private EditText etName, etOrgName, etJobTitle, etContact, etEmail, etCourse;
    private Button btnCreate;
    private String name, orgName, jobTitle, contact, email, course;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Check if user is logged in
        session = new SessionHandler(getActivity().getApplicationContext());
        Utils.redirectToLogin(session, this.getContext());

        View view;
        if (Utils.isOrganization(session)) {
            // If user is from an organization
            view = inflater.inflate(R.layout.fragment_create_nc_org, container, false);
            etOrgName = (EditText) view.findViewById(R.id.etOrgName);
            etJobTitle = (EditText) view.findViewById(R.id.etJobTitle);
        } else {
            // If user is a student
            view = inflater.inflate(R.layout.fragment_create_nc_student, container, false);
            etCourse = (EditText) view.findViewById(R.id.etCourse);
        }
        etName = (EditText) view.findViewById(R.id.etName);
        etContact = (EditText) view.findViewById(R.id.etContact);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        btnCreate = (Button) view.findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString();
                contact = etContact.getText().toString();
                email = etEmail.getText().toString();
                if (validateCommonEntries()) {
                    if (Utils.isOrganization(session)) {
                        orgName = etOrgName.getText().toString();
                        jobTitle = etJobTitle.getText().toString();
                        if (orgName.isEmpty()) {
                            etOrgName.setError(view.getContext().getString(R.string.empty_org_name_error));
                            return;
                        }
                        if (jobTitle.isEmpty()){
                            etJobTitle.setError(view.getContext().getString(R.string.empty_job_title_error));
                            return;
                        }
                        //TODO: insert values into org table
                        Organization org = new Organization();
//                        Log.e(TAG, session.getUserDetails().getUid());
                        org.setContact(contact);
                        org.setEmail(email);
                        org.setJobTitle(jobTitle);
                        org.setName(name);
                        org.setOrganization(orgName);
//                        org.setOrgUserId(session.getUserDetails().getUid());
                        Call<Result> call = RetrofitClient
                                .getInstance()
                                .getOrganizationApi()
                                .addCard(org);
                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {
                                btnCreate.setEnabled(true);
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                if (response.body().getSuccess()){
                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_container, new HomeFragment())
                                            .commit();
                                }
                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {

                            }
                        });
                    } else {

                    }
                }
            }
        });

        return view;
    }

    private boolean validateCommonEntries() {
        int result = 0;
        if (Utils.validateContact(contact, etContact, this.getContext())) {
            result++;
        }
        if (Utils.validateEmail(email, etEmail, this.getContext())) {
            result++;
        }
        if (Utils.validateName(name, etName, this.getContext())) {
            result++;
        }
        if (result == 3) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Display Progress bar while Creating card
     */
    private void displayLoader() {
        btnCreate.setEnabled(false);
        pDialog = new ProgressDialog(this.getContext());
        pDialog.setMessage("Creating.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}
