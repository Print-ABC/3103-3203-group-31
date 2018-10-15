package fragments;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ncshare.ncshare.R;

import org.w3c.dom.Text;

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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<User> call = RetrofitClient
                        .getInstance()
                        .getUserApi()
                        .retrieveUsername(etUsername.getText().toString());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        response.body();
                        String user_name = response.body().getName();
                        String user_email = response.body().getUserEmail();
                        tvName.setText(user_name);
                        tvEmail.setText(user_email);
                        btnAdd.setVisibility(View.VISIBLE);
                        //TODO need check if already added or not
                    }

                    @Override
                    public void onFailure(Call<User> callU, Throwable t) {
                    }
                });
            }
        });
        return view;
    }

}
