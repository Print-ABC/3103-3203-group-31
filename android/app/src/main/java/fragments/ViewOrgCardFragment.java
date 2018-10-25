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
import models.Organization;
import models.Session;
import models.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class ViewOrgCardFragment extends Fragment {

    private ArrayList<Organization> orgArrayList = new ArrayList<>();
    private RecyclerView rvViewOrgCard;
    private Session session;
    private ViewOrgCardFragment.OrgAdapter mAdapter;

    private ImageView ivNoEntries;
    private TextView tvNoEntries;


    public ViewOrgCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        session = SessionHandler.getSession();
        Utils.redirectToLogin(this.getContext());

        View view=inflater.inflate(R.layout.fragment_org_cards,container,false);

        ivNoEntries = (ImageView) view.findViewById(R.id.ivOrgNoEntries);
        tvNoEntries = (TextView) view.findViewById(R.id.tvOrgNoEntries);
        rvViewOrgCard = (RecyclerView) view.findViewById(R.id.view_org_card_recycler_view);
        rvViewOrgCard.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        rvViewOrgCard.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize card list array list
        CardList cardList = new CardList();
        cardList.setCards(session.getUser().getCards());


        // Retrieve user's username
        Call<CardList> call = RetrofitClient
                .getInstance()
                .getUserApi()
                .getCards(session.getUser().getToken(), cardList);
        call.enqueue(new Callback<CardList>() {
            @Override
            public void onResponse(Call<CardList> call, Response<CardList> response) {
                switch (response.code()){
                    case 200:
                        CardList cardInfo = response.body();

                        if (cardInfo != null) {
                            toggleListOn();

                            // Get card lists
                            List<Organization> orgCards = cardInfo.getOrgCards();
                            if (orgCards == null){
                                toggleListOff();
                                return;
                            }
                            updateUI((ArrayList<Organization>) orgCards);
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

        updateUI(orgArrayList);
        return view;
    }

    public void updateUI(ArrayList<Organization> orgArrayList){
        mAdapter = new ViewOrgCardFragment.OrgAdapter(orgArrayList);
        rvViewOrgCard.setAdapter(mAdapter);
        if (orgArrayList.isEmpty()) {
            toggleListOff();
        } else {
            toggleListOn();
        }
    }

    public class OrgCardViewHolder extends RecyclerView.ViewHolder {
        private Organization org;
        public TextView tvOrgRowName;
        public TextView tvOrgRowJobTitle;
        public TextView tvOrgRowOrganization;

        public OrgCardViewHolder(final View itemView) {
            super(itemView);

            tvOrgRowName = (TextView) itemView.findViewById(R.id.tvOrgRowName);
            tvOrgRowJobTitle = (TextView) itemView.findViewById(R.id.tvOrgRowJobTitle);
            tvOrgRowOrganization = (TextView) itemView.findViewById(R.id.tvOrgRowOrganization);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.setClickable(false);
                    Intent intent = new Intent(view.getContext(), ViewCardActivity.class);
                    intent.putExtra(Utils.CONTACT, org.getContact());
                    intent.putExtra(Utils.ORGANIZATION_NAME, org.getOrganization());
                    intent.putExtra(Utils.EMAIL, org.getEmail());
                    intent.putExtra(Utils.NAME, org.getName());
                    intent.putExtra(Utils.JOB_TITLE, org.getJobTitle());
                    intent.putExtra(Utils.USER_ROLE, Utils.ORGANIZATION_ROLE);
                    startActivity(intent);
                }
            });
        }

        public void bindData(Organization organization) {
            this.org = organization;
            this.tvOrgRowName.setText(organization.getName());
            this.tvOrgRowOrganization.setText(organization.getOrganization());
            this.tvOrgRowJobTitle.setText(organization.getJobTitle());
        }
    }

    private class OrgAdapter extends RecyclerView.Adapter<ViewOrgCardFragment.OrgCardViewHolder> {
        private ArrayList<Organization> organizations;

        public OrgAdapter(ArrayList<Organization> organizations) {
            this.organizations = organizations;
        }

        @Override
        public ViewOrgCardFragment.OrgCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.custom_org_row, parent, false);
            return new ViewOrgCardFragment.OrgCardViewHolder(view);

        }

        @Override
        public void onBindViewHolder(ViewOrgCardFragment.OrgCardViewHolder holder, int position) {
            Organization org = organizations.get(position);
            holder.bindData(org);
        }

        @Override
        public int getItemCount() {
            return organizations.size();
        }
    }

    private void toggleListOn(){
        ivNoEntries.setVisibility(View.GONE);
        tvNoEntries.setVisibility(View.GONE);
        rvViewOrgCard.setVisibility(View.VISIBLE);
    }

    private void toggleListOff(){
        ivNoEntries.setVisibility(View.VISIBLE);
        tvNoEntries.setVisibility(View.VISIBLE);
        rvViewOrgCard.setVisibility(View.GONE);
    }
}
