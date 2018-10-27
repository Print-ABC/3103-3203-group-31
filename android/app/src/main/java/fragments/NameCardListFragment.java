package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ncshare.ncshare.R;

import java.util.ArrayList;
import java.util.List;

import common.SessionHandler;
import common.Utils;

public class NameCardListFragment extends Fragment {

    private static final String TAG = "NameCardListFragment";
    private SessionHandler session;
    private TabLayout tabs;
    private ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //TODO: uncomment
        // Check if user is logged in
        session = new SessionHandler(this.getContext());
        Utils.redirectToLogin(this.getContext());

        View view = inflater.inflate(R.layout.fragment_name_card_list, container, false);

        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) view.findViewById(R.id.view_pager_nc_list);

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new ViewStudentCardFragment(), "Student");
        adapter.addFragment(new ViewOrgCardFragment(), "Organization");
        viewPager.setAdapter(adapter);

        // Set Tabs inside Toolbar
        tabs = (TabLayout) view.findViewById(R.id.tab_nc_list);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}