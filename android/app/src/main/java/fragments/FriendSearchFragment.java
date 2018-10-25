package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ncshare.ncshare.R;

import common.SessionHandler;
import models.FriendRequest;
import models.Session;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class FriendSearchFragment extends Fragment {

    private EditText etUsername;
    private TextView tvName, tvEmail;
    private Button btnAdd;
    private ImageButton btnSearch;
    private Session mSession;

    public FriendSearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_search, container, false);
        etUsername = (EditText) view.findViewById(R.id.etSearchUid);
        tvName = (TextView) view.findViewById(R.id.tvFName);
        tvEmail = (TextView) view.findViewById(R.id.tvFEmail);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);
        mSession = SessionHandler.getSession();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<User> call = RetrofitClient
                        .getInstance()
                        .getUserApi()
                        .searchByUsername(etUsername.getText().toString());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        try{
                            tvName.setText(response.body().getName());
                            tvEmail.setText(response.body().getEmail());
                            btnAdd.setVisibility(View.VISIBLE);
                            btnAddOnClickListener(response.body().getUid(), response.body().getName(), response.body().getUsername());
                            //TODO need check if already added or not
                        } catch (NullPointerException ex) {
                            Toast.makeText(getActivity(), "User is not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> callU, Throwable t) {
                    }
                });
            }
        });
        return view;
    }

    public void btnAddOnClickListener(final String f_uid, final String f_name, final String f_username){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<FriendRequest> call = RetrofitClient
                        .getInstance()
                        .getFriendRequestApi()
                        .createRequest(new FriendRequest(mSession.getUser().getUid(), mSession.getUser().getName(),
                                mSession.getUser().getUsername(), f_uid, f_name, f_username));
                call.enqueue(new Callback<FriendRequest>() {
                    @Override
                    public void onResponse(Call<FriendRequest> call, Response<FriendRequest> response) {
                        Toast.makeText(getActivity(), "Friend request sent", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<FriendRequest> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error. Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
