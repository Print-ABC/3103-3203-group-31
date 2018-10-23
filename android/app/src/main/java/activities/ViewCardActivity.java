package activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ncshare.ncshare.R;

import common.SessionHandler;
import common.Utils;
import models.Session;

public class ViewCardActivity extends AppCompatActivity {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = SessionHandler.getSession();
        Utils.redirectToLogin(this);

        super.onCreate(savedInstanceState);

        int role = getIntent().getIntExtra(Utils.USER_ROLE,3);
        if (role == Utils.STUDENT_ROLE) {
            setContentView(R.layout.activity_view_student_card);
            TextView tvCourse = (TextView) findViewById(R.id.tvOrganization);
            TextView tvStudentName = (TextView) findViewById(R.id.tvName);
            TextView tvStudentEmail = (TextView) findViewById(R.id.tvOrgEmail);
            TextView tvStudentContact = (TextView) findViewById(R.id.tvOrgContact);

            tvCourse.setText(getIntent().getStringExtra(Utils.COURSE));
            tvStudentName.setText(getIntent().getStringExtra(Utils.NAME));
            tvStudentEmail.setText(getIntent().getStringExtra(Utils.EMAIL));
            tvStudentContact.setText(getIntent().getStringExtra(Utils.CONTACT));
        } else if (role == Utils.ORGANIZATION_ROLE){
            setContentView(R.layout.activity_view_organization_card);
            TextView tvJobTitle = (TextView) findViewById(R.id.tvOrgJobTitle);
            TextView tvOrgName = (TextView) findViewById(R.id.tvOrganization);
            TextView tvOrgEmail = (TextView) findViewById(R.id.tvOrgEmail);
            TextView tvOrgContact = (TextView) findViewById(R.id.tvOrgContact);
            TextView tvName = (TextView) findViewById(R.id.tvName);

            tvJobTitle.setText(getIntent().getStringExtra(Utils.JOB_TITLE));
            tvOrgName.setText(getIntent().getStringExtra(Utils.ORGANIZATION_NAME));
            tvOrgEmail.setText(getIntent().getStringExtra(Utils.EMAIL));
            tvOrgContact.setText(getIntent().getStringExtra(Utils.CONTACT));
            tvName.setText(getIntent().getStringExtra(Utils.NAME));
        }

    }
}
