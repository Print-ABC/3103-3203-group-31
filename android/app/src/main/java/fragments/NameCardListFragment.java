package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.ncshare.ncshare.R;

import java.util.List;

import common.SessionHandler;
import common.Utils;
import models.Organization;
import models.Session;
import models.CardList;
import models.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class NameCardListFragment extends Fragment {

        private static final String TAG = "NameCardListFragment";
        private Session session;
        private ListView lvViewCards;
        private TextView tvNoCards;
        private SearchView searchView;
        ArrayAdapter<String> adapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Check if user is logged in
            session = SessionHandler.getSession();
            Utils.redirectToLogin(this.getContext());
            View view = inflater.inflate(R.layout.fragment_name_card_list, container, false);

            lvViewCards = (ListView) view.findViewById(R.id.lvViewCards);
            tvNoCards = (TextView) view.findViewById(R.id.tvNoCards);

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
                    if(response.body().getSuccess()){
                        toggleListOn();
                        CardList cardInfo = response.body();
                        if (cardInfo != null) {
                            List<Student> studentCards = cardInfo.getStuCards();
                            List<Organization> orgCards = cardInfo.getOrgCards();
                        } else {
                            toggleListOff();
                        }
                    } else {
                        toggleListOff();
                    }
                }

                @Override
                public void onFailure(Call<CardList> call, Throwable t) {
                    toggleListOff();
                }
            });

            if(Utils.isOrganization(session)){

            }
            return view;
    }

    private void toggleListOn(){
        lvViewCards.setVisibility(View.VISIBLE);
        tvNoCards.setVisibility(View.GONE);
    }

    private void toggleListOff(){
        lvViewCards.setVisibility(View.GONE);
        tvNoCards.setVisibility(View.VISIBLE);
    }
}
