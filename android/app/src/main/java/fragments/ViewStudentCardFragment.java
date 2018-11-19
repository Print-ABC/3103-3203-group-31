package fragments;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ncshare.ncshare.R;
import com.ncshare.ncshare.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import activities.ViewCardActivity;
import common.SessionHandler;
import common.Utils;
import models.CardList;
import models.Request;
import models.Student;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;


public class ViewStudentCardFragment extends Fragment {

    private ArrayList<Student> students = new ArrayList<>();
    private RecyclerView rvViewStudentCard;
    private SessionHandler session;
    private ViewStudentCardFragment.StudentAdapter mAdapter;

    private ImageView ivNoEntries;
    private TextView tvNoEntries;


    public ViewStudentCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        session = new SessionHandler(this.getContext());
        Utils.redirectToLogin(this.getContext());

        View view=inflater.inflate(R.layout.fragment_student_cards,container,false);

        ivNoEntries = (ImageView) view.findViewById(R.id.ivStuNoEntries);
        tvNoEntries = (TextView) view.findViewById(R.id.tvStuNoEntries);
        rvViewStudentCard = (RecyclerView) view.findViewById(R.id.view_student_card_recycler_view);
        rvViewStudentCard.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        rvViewStudentCard.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize card list array list
        Request req = new Request();
        User user = session.getUserDetails();
        req.setUid(user.getUid());

        // Instead of get cards from session, get from db and update UI + session
        // Now cards array is the most updated
        Call<CardList> call = RetrofitClient
                .getInstance()
                .getUserApi()
                .getCards(user.getToken(), req);
        call.enqueue(new Callback<CardList>() {
            @Override
            public void onResponse(Call<CardList> call, Response<CardList> response) {
                switch (response.code()){
                    case 200:
                        CardList cardInfo = response.body();

                        if (cardInfo != null) {
                            toggleListOn();

                            // Get card lists
                            List<Student> studentCards = cardInfo.getStuCards();

                            // Set retrieved card list to session
                            session.setCardList((ArrayList<String>) cardInfo.getCards());

                            if (studentCards == null){
                                toggleListOff();
                                return;
                            }
                            updateUI((ArrayList<Student>) studentCards);

                        } else {
                            toggleListOff();
                        }
                        break;
                    default:
                        toggleListOff();
                        break;
                }
            }

            @Override
            public void onFailure(Call<CardList> call, Throwable t) {
                toggleListOff();
            }
        });

        updateUI(students);

        return view;
    }

    public void updateUI(ArrayList<Student> students){
        mAdapter = new ViewStudentCardFragment.StudentAdapter(students);
        rvViewStudentCard.setAdapter(mAdapter);
        if (students.isEmpty()) {
            toggleListOff();
        } else {
            toggleListOn();
        }
    }

    public class StudentCardViewHolder extends RecyclerView.ViewHolder {
        private Student student;
        public TextView tvStudentRowName;
        public TextView tvStudentRowCourse;
        public TextView tvStudentRowSchool;

        public StudentCardViewHolder(final View itemView) {
            super(itemView);

            tvStudentRowName = (TextView) itemView.findViewById(R.id.tvStudentRowName);
            tvStudentRowCourse = (TextView) itemView.findViewById(R.id.tvStudentRowCourse);
            tvStudentRowSchool = (TextView) itemView.findViewById(R.id.tvStudentRowSchool);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.setClickable(false);
                    Toast.makeText(getActivity(),
                            student.getName() + " clicked!", Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(view.getContext(), ViewCardActivity.class);
                    intent.putExtra(Utils.CONTACT, student.getContact());
                    intent.putExtra(Utils.COURSE, student.getCourse());
                    intent.putExtra(Utils.EMAIL, student.getEmail());
                    intent.putExtra(Utils.NAME, student.getName());
                    intent.putExtra(Utils.USER_ROLE, Utils.STUDENT_ROLE);
                    startActivity(intent);
                }
            });
        }

        public void bindData(Student student) {
            this.student = student;
            this.tvStudentRowName.setText(student.getName());
            this.tvStudentRowCourse.setText(student.getCourse());
            this.tvStudentRowSchool.setText(R.string.sit_name);
        }
    }

    private class StudentAdapter extends RecyclerView.Adapter<ViewStudentCardFragment.StudentCardViewHolder> {
        private ArrayList<Student> students;

        public StudentAdapter(ArrayList<Student> students) {
            this.students = students;
        }

        @Override
        public ViewStudentCardFragment.StudentCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.custom_student_row, parent, false);
            return new ViewStudentCardFragment.StudentCardViewHolder(view);

        }

        @Override
        public void onBindViewHolder(ViewStudentCardFragment.StudentCardViewHolder holder, int position) {
            Student s = students.get(position);
            holder.bindData(s);
        }

        @Override
        public int getItemCount() {
            return students.size();
        }
    }

    private void toggleListOn(){
        ivNoEntries.setVisibility(View.GONE);
        tvNoEntries.setVisibility(View.GONE);
        rvViewStudentCard.setVisibility(View.VISIBLE);
    }

    private void toggleListOff(){
        ivNoEntries.setVisibility(View.VISIBLE);
        tvNoEntries.setVisibility(View.VISIBLE);
        rvViewStudentCard.setVisibility(View.GONE);
    }
}
