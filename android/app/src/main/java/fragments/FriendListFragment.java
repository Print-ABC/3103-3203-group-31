package fragments;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ncshare.ncshare.FriendsModel;
import com.ncshare.ncshare.R;
import com.ncshare.ncshare.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import common.SessionHandler;
import common.Utils;
import models.Organization;
import models.Session;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class FriendListFragment extends Fragment {
    private List<String> friend = SessionHandler.getSessionUserObj().getFriendship();
    private List<String> cards = SessionHandler.getSessionUserObj().getCards();
    private ArrayList<FriendsModel> mFriends;
    private ArrayList<Organization> mCards;
    private RecyclerView mSFriendsRecyclerView, mSNCRecyclerView;
    private FriendListFragment.FriendsAdapter mAdapter;
    private FriendListFragment.NCAdapter mNCAdapter;
    private String friendUID, OrgCardID, myCardId; //Selected friend's uid, the selected card's id, and my card Id.
    private Session session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if user is logged in
        session = SessionHandler.getSession();
        Utils.redirectToLogin(this.getContext());
        myCardId = session.getUser().getCardId();
        Log.i("MyCardID", myCardId);
        mFriends = new ArrayList<>();
        for(int i = 0; i < friend.size(); i++){
            String str[] = friend.get(i).split(",");
            FriendsModel friends = new FriendsModel();
            friends.setUID(str[0]);
            friends.setName(str[1]);
            friends.setUsername(str[2]);
            mFriends.add(friends);
        }
        mCards = new ArrayList<>();
        for(int i = 0; i < cards.size(); i++){
            String cardId = cards.get(i);
            Log.i("CARD LIST --------", cardId);
            if (!cardId.equals(myCardId)){
                Log.i("Compare CardID", "NOPE!");
                //CALL to get card details by card ID
                Call<Organization> call = RetrofitClient
                        .getInstance()
                        .getOrganizationApi()
                        .getcardinfo(cardId);
                call.enqueue(new Callback<Organization>() {
                    @Override
                    public void onResponse(Call<Organization> call, Response<Organization> response) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if(response.body().getSuccess()) {
                            Log.i("CALL RESPONSE ---------", response.body().getName());
                            Organization cards = new Organization();
                            cards.setCardId(response.body().getCardId());
                            cards.setName(response.body().getName());
                            cards.setOrganization(response.body().getOrganization());
                            mCards.add(cards);
                        }
                        else{
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Organization> call, Throwable t) {
                        Log.i("onFailure",t.getMessage());
                    }
                });
                Log.i("CARDS", mCards.toString());
            }
            else{
                Log.i("Compare CardID", "it's my card id!");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        mSFriendsRecyclerView = (RecyclerView) view.findViewById(R.id.friends_recycler_view);
        mSFriendsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        mSFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    // This is the pop up dialog that display namecards of the user
    // for them to send to selected friend except their own's
    public void open_dialog(View v){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        View row = getActivity().getLayoutInflater().inflate(R.layout.alert_dialog_row_items, null);
        alertDialog.setView(row);
        mSNCRecyclerView = (RecyclerView) row.findViewById(R.id.lvNamecards);
        mSNCRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        mSNCRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUICard();
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void updateUI(){
        mAdapter = new FriendListFragment.FriendsAdapter(mFriends);
        mSFriendsRecyclerView.setAdapter(mAdapter);
    }

    private class FriendsHolder extends RecyclerView.ViewHolder{

        private FriendsModel mFriends;
        public ImageButton btnSend, btnDelete;
        public TextView mNameTextView, mUsernameTextView;

        public FriendsHolder(View itemView){
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.friends_name);
            mUsernameTextView = (TextView) itemView.findViewById(R.id.friends_username);
            btnSend = (ImageButton) itemView.findViewById(R.id.btnSend);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendUID = mFriends.getUID();
                    Log.i("Want to send to", friendUID);
                    Toast.makeText(getActivity(), "You're sending to " +
                            mFriends.getName(), Toast.LENGTH_SHORT)
                            .show();
                    open_dialog(v);
                }
            });
        }
        public void bindData(FriendsModel s){
            mFriends = s;
            mNameTextView.setText(s.getName());
            mUsernameTextView.setText("Username : " + s.getUsername());
        }
    }
    private class FriendsAdapter extends RecyclerView.Adapter<FriendListFragment.FriendsHolder>{
        private ArrayList<FriendsModel> mFriends;
        public FriendsAdapter(ArrayList<FriendsModel> Friends){
            mFriends = Friends;
        }
        @Override
        public FriendListFragment.FriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.friendlist_items,parent,false);
            return new FriendListFragment.FriendsHolder(view);

        }
        @Override
        public void onBindViewHolder(FriendListFragment.FriendsHolder holder, int position) {
            FriendsModel s = mFriends.get(position);
            holder.bindData(s);
        }
        @Override
        public int getItemCount() {
            return mFriends.size();
        }
    }

    private void updateUICard(){
        mNCAdapter = new FriendListFragment.NCAdapter(mCards);
        mSNCRecyclerView.setAdapter(mNCAdapter);
        mNCAdapter.notifyDataSetChanged();
    }

    private class NCHolder extends RecyclerView.ViewHolder{

        private Organization mCards;
        public TextView mNameTextView, mOrgTextView;

        public NCHolder(View itemView){
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.tvCardname);
            mOrgTextView = (TextView) itemView.findViewById(R.id.tvCardOrg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //get the cardID from the clicked item
                    OrgCardID = mCards.getCardId();

                    //Update the receiver user's cards array
                    Call<User> callA = RetrofitClient
                            .getInstance()
                            .getUserApi()
                            .checkForCard(friendUID, OrgCardID);
                    callA.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> callA, Response<User> responseA) {
                            Toast.makeText(getContext(), responseA.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if(responseA.body().getSuccess()){
                                Log.i("onResponseeeee ------","Card SEnt to Friend!");
                            } else {
                                Log.i("onResponseeeee --------","Card NOT sent to friend!");
                            }
                        }
                        @Override
                        public void onFailure(Call<User> callA, Throwable t) {
                            Log.i("onFailure","ERROR!");
                        }
                    });
                }
            });
        }
        public void bindData(Organization s){
            mCards = s;
            mNameTextView.setText(s.getName());
            mOrgTextView.setText(s.getOrganization());
        }
    }
    private class NCAdapter extends RecyclerView.Adapter<FriendListFragment.NCHolder>{
        private ArrayList<Organization> mCards;
        public NCAdapter(ArrayList<Organization> Cards){
            mCards = Cards;
        }
        @Override
        public FriendListFragment.NCHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.friend_card_list,parent,false);
            return new FriendListFragment.NCHolder(view);

        }
        @Override
        public void onBindViewHolder(FriendListFragment.NCHolder holder, int position) {
            Organization s = mCards.get(position);
            holder.bindData(s);
        }
        @Override
        public int getItemCount() {
            Log.i("ITEM count ------", String.valueOf(mCards.size()));
            return mCards.size();
        }
    }
}
