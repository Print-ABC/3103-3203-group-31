package fragments;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ncshare.ncshare.FriendsModel;
import com.ncshare.ncshare.R;
import com.ncshare.ncshare.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import models.Relationship;
import models.Result;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.FriendService;
import services.RetrofitClient;

public class FriendListFragment extends Fragment {
    private String[] friendNames = {"Marie Curie","Thomas Edison","Albert Einstein","Michael Faraday","Galileo Galilei",
            "Stephen Hawking","Johannes Kepler","Issac Newton","Nikola Tesla"};

    private ArrayList<FriendsModel> mFriends;
    private RecyclerView mSFriendsRecyclerView;
    private FriendsAdapter mAdapter;
    public String user_friend_id = "";
    public String user_friends;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mFriends = new ArrayList<>();
        for(int i =0;i<friendNames.length;i++){
            FriendsModel friends = new FriendsModel();
            friends.setName(friendNames[i]);
            mFriends.add(friends);
        }
        String uid= "859f94b2cfba11e8a30b8c16456733d9";
        retrieveFUID(uid);
        super.onCreate(savedInstanceState);
    }

    public void retrieveFUID(String uid){
        // GET the user_friend_id of user by the uid
        Call<User> call = RetrofitClient
                .getInstance()
                .getUserApi()
                .retrieve(uid);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                response.body();
//                user_friend_id = response.body().getUidFriend();
                Log.i("UID FRIEND in Call", user_friend_id);

                //GET the friend_two_id of the user by the friend_one_id
                Call<Relationship> callR = RetrofitClient
                        .getInstance()
                        .getRelationshipApi()
                        .retrieveFriends(user_friend_id);
                callR.enqueue(new Callback<Relationship>() {
                    @Override
                    public void onResponse(Call<Relationship> callR, Response<Relationship> responseR) {
                        responseR.body();
                        user_friends = responseR.body().getFriendTwoId();
                        Log.i("UID FRIEND 2 ----------", user_friends);
                        if (Integer.parseInt(responseR.body().getFriendStatus())== 1) {
                            //GET the name of the friend by the user_friend_id
                            Call<User> callU = RetrofitClient
                                    .getInstance()
                                    .getUserApi()
                                    .retrieveFUID(user_friends);
                            callU.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> callU, Response<User> responseU) {
                                    responseU.body();
                                    String user_name = responseU.body().getName();
                                    Log.i("NAME ---------", user_name);
                                }

                                @Override
                                public void onFailure(Call<User> callU, Throwable t) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Relationship> callR, Throwable t) {
                    }
                });
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend__list, container, false);
        mSFriendsRecyclerView = (RecyclerView) view.findViewById(R.id.friends_recycler_view);
        mSFriendsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        mSFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI(){
        mAdapter = new FriendsAdapter(mFriends);
        mSFriendsRecyclerView.setAdapter(mAdapter);
    }

    private class FriendsHolder extends RecyclerView.ViewHolder{

        private FriendsModel mFriends;
        public TextView mNameTextView;
        public FriendsHolder(View itemView){
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.friends_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            mFriends.getName() + " clicked!", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
        public void bindData(FriendsModel s){
            mFriends = s;
            mNameTextView.setText(s.getName());
        }
    }
    private class FriendsAdapter extends RecyclerView.Adapter<FriendsHolder>{
        private ArrayList<FriendsModel> mFriends;
        public FriendsAdapter(ArrayList<FriendsModel> Friends){
            mFriends = Friends;
        }
        @Override
        public FriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.friendlist_items,parent,false);
            return new FriendsHolder(view);

        }
        @Override
        public void onBindViewHolder(FriendsHolder holder, int position) {
            FriendsModel s = mFriends.get(position);
            holder.bindData(s);
        }
        @Override
        public int getItemCount() {
            return mFriends.size();
        }
    }


}
