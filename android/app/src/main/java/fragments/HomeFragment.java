package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ncshare.ncshare.R;

import activities.MainActivity;
import common.SessionHandler;
import common.Utils;
import models.Session;

public class HomeFragment extends Fragment {

    private Session session;
    private Button btnExchangeNc, btnCreateNc, btnFriendList, btnViewCards;
    BottomNavigationItemView nameCardNavigationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Check if user is logged in
        session = SessionHandler.getSession();
        Utils.redirectToLogin(this.getContext());

        // Initialize views
        btnCreateNc = (Button)view.findViewById(R.id.btnCreateNc);

        btnCreateNc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateNCFragment()).commit();
            }
        });



        return view;
    }
}
