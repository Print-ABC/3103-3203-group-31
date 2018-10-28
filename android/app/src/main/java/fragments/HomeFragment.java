package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ncshare.ncshare.R;

import common.SessionHandler;
import common.Utils;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Check if user is logged in
        SessionHandler session = new SessionHandler(this.getContext());
        Utils.redirectToLogin(this.getContext());

        return view;
    }
}
