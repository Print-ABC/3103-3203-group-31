package fragments;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
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
import models.FriendRequest;
import models.Organization;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class FriendListFragment extends Fragment {
    private List<String> friend;
    private List<String> cards;
    private ArrayList<FriendsModel> mFriends;
    private ArrayList<Organization> mCards;
    private RecyclerView mSFriendsRecyclerView, mSNCRecyclerView;
    private FriendListFragment.FriendsAdapter mAdapter;
    private FriendListFragment.NCAdapter mNCAdapter;
    private String friendUID, friendName, friendUname; //Selected friend's uid, name, and username
    private String OrgCardID, myCardId, myUID; //The selected card's id, my card Id and my UID.
    private String friendship, friendship1; //Concats
    private SessionHandler session;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if user is logged in
        session = new SessionHandler(this.getContext());
        user = session.getUserDetails();
        friend = user.getFriendship();
        cards = user.getCards();

        Utils.redirectToLogin(this.getContext());

        myUID = user.getUid();

        friendship1 = myUID + "," + user.getName() + ","+ user.getUsername();

        mFriends = new ArrayList<>();
        mFriends.clear();
        mCards = new ArrayList<>();
        mCards.clear();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        session = new SessionHandler(this.getContext());
        user = session.getUserDetails();
        friend = user.getFriendship();
        cards = user.getCards();
        String userCardId = user.getCardId();

        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        if (userCardId != null) {
            myCardId = user.getCardId();
        }
        else if (!((user.getCardId()).equals("none"))){
            myCardId = user.getCardId();
        }
        else{
            myCardId="";
        }
        mFriends.clear();
        for(int i = 0; i < friend.size(); i++){
            String str[] = friend.get(i).split(",");
            FriendsModel friends = new FriendsModel();
            friends.setUID(str[0]);
            friends.setName(str[1]);
            friends.setUsername(str[2]);
            mFriends.add(friends);
        }
        mCards.clear();
        for(int i = 0; i < cards.size(); i++){
            String cardId = cards.get(i);
            if (!cardId.equals(myCardId) || myCardId.equals("")){
                //CALL to get card details by card ID
                Call<Organization> call = RetrofitClient
                        .getInstance()
                        .getOrganizationApi()
                        .getcardinfo(user.getToken(),cardId);
                call.enqueue(new Callback<Organization>() {
                    @Override
                    public void onResponse(Call<Organization> call, Response<Organization> response) {
                        switch (response.code()) {
                            case 200:
                                Toast.makeText(getActivity(), "Retrieving cards from collections", Toast.LENGTH_SHORT).show();
                                Organization cards = new Organization();
                                cards.setCardId(response.body().getCardId());
                                cards.setName(response.body().getName());
                                cards.setOrganization(response.body().getOrganization());
                                mCards.add(cards);
                                break;

                            case 500:
                                Toast.makeText(getActivity(), "Error retrieving", Toast.LENGTH_SHORT).show();
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onFailure(Call<Organization> call, Throwable t) {
                    }
                });
            }
            else{
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        }, 1500);
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
        mAdapter.notifyDataSetChanged();
    }

    private class FriendsHolder extends RecyclerView.ViewHolder{

        private FriendsModel mFriendsModel;
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
                    friendUID = mFriendsModel.getUID();
                    Toast.makeText(getActivity(), "You're sending to " +
                            mFriendsModel.getName(), Toast.LENGTH_SHORT)
                            .show();
                    open_dialog(v);
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendUID = mFriendsModel.getUID();
                    friendName = mFriendsModel.getName();
                    friendUname = mFriendsModel.getUsername();
                    friendship = friendUID + "," + friendName + "," + friendUname;
                    Toast.makeText(getActivity(), "You're deleting " + mFriendsModel.getName(), Toast.LENGTH_SHORT).show();

                    //Deleting friend from user's list
                    Call<FriendRequest> callC = RetrofitClient
                            .getInstance()
                            .getFriendRequestApi()
                            .deleteFriend(user.getToken(), myUID, friendship);
                    callC.enqueue(new Callback<FriendRequest>() {
                        @Override
                        public void onResponse(Call<FriendRequest> callC, Response<FriendRequest> responseC) {
                            switch (responseC.code()) {
                                case 200:
                                    Toast.makeText(getContext(), "Friend deleted from list", Toast.LENGTH_SHORT).show();
                                    //Deleting user from friend's list
                                    Call<FriendRequest> callB = RetrofitClient
                                            .getInstance()
                                            .getFriendRequestApi()
                                            .deleteFriend(user.getToken(), friendUID, friendship1);
                                    callB.enqueue(new Callback<FriendRequest>() {
                                        @Override
                                        public void onResponse(Call<FriendRequest> callB, Response<FriendRequest> responseB) {
                                            switch (responseB.code()) {
                                                case 200:
                                                    Toast.makeText(getContext(), "Removing friends", Toast.LENGTH_SHORT).show();
                                                    mFriends.remove(mFriendsModel);
                                                    updateUI();
                                                    break;
                                                case 500:
                                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                                    break;
                                                default:
                                                    Toast.makeText(getActivity(), "Failed to delete friend", Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<FriendRequest> callB, Throwable t) {
                                        }
                                    });
                                    break;
                                default:
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                        @Override
                        public void onFailure(Call<FriendRequest> callC, Throwable t) {
                        }
                    });
                }
            });
        }

        public void bindData(FriendsModel s){
            mFriendsModel = s;
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
                            .checkForCard(user.getToken(),friendUID, OrgCardID);
                    callA.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> callA, Response<User> responseA) {
                            switch (responseA.code()) {
                                case 200:
                                    Toast.makeText(getContext(), "Card Sent to Friend!", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getContext(), "Card NOT sent to friend!", Toast.LENGTH_SHORT).show();
                                    break;
                                case 406:
                                    Toast.makeText(getContext(), "Exist in friend's collection", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "Failed to send card", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                        @Override
                        public void onFailure(Call<User> callA, Throwable t) {
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
            return mCards.size();
        }
    }
}
