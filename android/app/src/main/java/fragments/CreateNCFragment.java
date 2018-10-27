package fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import models.Student;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

/**
 * Fragment for creating student/organization name cards
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
        session = new SessionHandler(this.getContext());
        Utils.redirectToLogin(this.getContext());

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
                displayLoader();
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
                        if (jobTitle.isEmpty()) {
                            etJobTitle.setError(view.getContext().getString(R.string.empty_job_title_error));
                            return;
                        }

                        User user = session.getUserDetails();
                        Organization org = new Organization();
                        org.setContact(contact);
                        org.setEmail(email);
                        org.setJobTitle(jobTitle);
                        org.setName(name);
                        org.setOrganization(orgName);
                        org.setUid(user.getUid());
                        Call<Result> call = RetrofitClient
                                .getInstance()
                                .getOrganizationApi()
                                .addCard(user.getToken(), org);
                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {
                                pDialog.dismiss();
                                btnCreate.setEnabled(true);
                                switch (response.code()) {
                                    case 201:
                                        Toast.makeText(getActivity(), R.string.msg_name_card_created, Toast.LENGTH_SHORT).show();
                                        session.setCardId(response.body().getCardId());
                                        getActivity().getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragment_container, new HomeFragment())
                                                .commit();
                                        break;
                                    case 406:
                                        Toast.makeText(getActivity(), R.string.error_nc_exists, Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getActivity(), R.string.error_nc_create_fail, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                pDialog.dismiss();
                                btnCreate.setEnabled(true);
                            }
                        });
                    } else {
                        course = etCourse.getText().toString();
                        if (course.isEmpty()) {
                            etCourse.setError(getString(R.string.invalid_course_input));
                            return;
                        }

                        User user = session.getUserDetails();
                        Student student = new Student();
                        student.setCourse(course);
                        student.setContact(contact);
                        student.setEmail(email);
                        student.setName(name);
                        student.setUid(user.getUid());
                        Call<Result> call = RetrofitClient
                                .getInstance()
                                .getStudentApi()
                                .addCard(user.getToken(), student);
                        call.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {
                                pDialog.dismiss();
                                btnCreate.setEnabled(true);
                                switch (response.code()) {
                                    case 201:
                                        Toast.makeText(getActivity(), R.string.msg_name_card_created, Toast.LENGTH_SHORT).show();
                                        session.setCardId(response.body().getCardId());
                                        getActivity().getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragment_container, new HomeFragment())
                                                .commit();
                                        break;
                                    case 406:
                                        Toast.makeText(getActivity(), R.string.error_nc_exists, Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getActivity(), R.string.error_nc_create_fail, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                pDialog.dismiss();
                                btnCreate.setEnabled(true);
                            }
                        });
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
        pDialog.setMessage(getString(R.string.dialog_creating_nc));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}
