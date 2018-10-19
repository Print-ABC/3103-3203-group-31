package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ncshare.ncshare.R;

import common.Session;
import common.Utils;

public class HomeFragment extends Fragment {

    private Session session;
    private Button btnExchangeNc, btnCreateNc, btnFriendList, btnViewCards;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Check if user is logged in
        session = Session.getSession();
        Utils.redirectToLogin(session, this.getContext());

        // Initialize views
        btnCreateNc = (Button)view.findViewById(R.id.btnCreateNc);
        btnExchangeNc = (Button)view.findViewById(R.id.btnExchageNc);
        btnFriendList = (Button)view.findViewById(R.id.btnFriendList);
        btnViewCards = (Button) view.findViewById(R.id.btnViewCards);

        // Show/hide friend list button depending on user's role
        if (Utils.isOrganization(session)){
            btnFriendList.setVisibility(View.GONE);
        } else {
            btnFriendList.setVisibility(View.VISIBLE);
        }

        btnCreateNc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateNCFragment()).commit();
            }
        });

        btnViewCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ViewCardsFragment()).commit();
            }
        });



        return view;
    }
}
