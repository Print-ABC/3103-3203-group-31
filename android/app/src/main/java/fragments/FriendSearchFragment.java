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
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class FriendSearchFragment extends Fragment {

    private EditText etUsername;
    private TextView tvName, tvUsername;
    private Button btnAdd;
    private ImageButton btnSearch;
    private SessionHandler mSession;

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
        tvUsername = (TextView) view.findViewById(R.id.tvFUsername);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);
        mSession = new SessionHandler(this.getContext());

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<User> call = RetrofitClient
                        .getInstance()
                        .getUserApi()
                        .searchByUsername(mSession.getUserDetails().getToken(), etUsername.getText().toString());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        switch (response.code()) {
                            case 200:
                                try{
                                    tvName.setText(response.body().getName());
                                    tvUsername.setText("Username : " + response.body().getUsername());
                                    btnAdd.setVisibility(View.VISIBLE);
                                    btnAddOnClickListener(response.body().getUid(), response.body().getName(), response.body().getUsername());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 404:
                                disableForm("User not found");
                                break;
                            default:
                                disableForm("Search failed");
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<User> callU, Throwable t) {}
                });
            }
        });
        return view;
    }

    public void disableForm(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        tvName.setText(null);
        tvUsername.setText(null);
        btnAdd.setVisibility(View.INVISIBLE);
    }

    public void btnAddOnClickListener(final String f_uid, final String f_name, final String f_username) {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = mSession.getUserDetails();
                Call<FriendRequest> call = RetrofitClient
                        .getInstance()
                        .getFriendRequestApi()
                        .createRequest(user.getToken(), new FriendRequest(user.getUid(), user.getName(),
                                user.getUsername(), f_uid, f_name, f_username));
                call.enqueue(new Callback<FriendRequest>() {
                    @Override
                    public void onResponse(Call<FriendRequest> call, Response<FriendRequest> response) {
                        switch (response.code()) {
                            case 201:
                                Toast.makeText(getActivity(), "Friend request sent", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getActivity(), "Error. Please try again", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                    @Override
                    public void onFailure(Call<FriendRequest> call, Throwable t) {}
                });
            }
        });
    }

}
