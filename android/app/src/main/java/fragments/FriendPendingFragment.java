package fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ncshare.ncshare.FriendsModel;
import com.ncshare.ncshare.R;
import com.ncshare.ncshare.SimpleDividerItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.SessionHandler;
import models.FriendRequest;
import models.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class FriendPendingFragment extends Fragment {
    private ArrayList<FriendsModel> mFriends;
    private RecyclerView mSFriendsRecyclerView;
    private FriendPendingFragment.FriendsAdapter mAdapter;
    private Session mSession;
    private List<FriendRequest> requests;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSession = SessionHandler.getSession();

        Call<List<FriendRequest>> call = RetrofitClient
                .getInstance()
                .getFriendRequestApi()
                .getByRecipientID(mSession.getUser().getUid());
        call.enqueue(new Callback<List<FriendRequest>>() {
            @Override
            public void onResponse(Call<List<FriendRequest>> call, Response<List<FriendRequest>> response) {
                requests = response.body();
            }
            @Override
            public void onFailure(Call<List<FriendRequest>> call, Throwable t) {}
        });

        mFriends = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        mSFriendsRecyclerView = (RecyclerView) view.findViewById(R.id.friends_recycler_view);
        mSFriendsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        mSFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        }, 1500);
        return view;
    }

    private void updateUI() {
        for (int i = 0; i < requests.size(); i++) {
            FriendsModel friends = new FriendsModel();
            friends.setName(requests.get(i).getRequester());
            friends.setUsername(requests.get(i).getRequester_username());
            mFriends.add(friends);
        }

        mAdapter = new FriendPendingFragment.FriendsAdapter(mFriends);
        mSFriendsRecyclerView.setAdapter(mAdapter);
    }

    private class FriendsHolder extends RecyclerView.ViewHolder {

        private FriendsModel mFriends;
        public TextView mNameTextView, mUsernameTextView;

        public FriendsHolder(View itemView) {
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.friends_name);
            mUsernameTextView = (TextView) itemView.findViewById(R.id.friends_username);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            mFriends.getName() + " clicked!", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        public void bindData(FriendsModel s) {
            mFriends = s;
            mNameTextView.setText(s.getName());
            mUsernameTextView.setText(s.getUsername());
        }
    }

    private class FriendsAdapter extends RecyclerView.Adapter<FriendPendingFragment.FriendsHolder> {
        private ArrayList<FriendsModel> mFriends;

        public FriendsAdapter(ArrayList<FriendsModel> Friends) {
            mFriends = Friends;
        }

        @Override
        public FriendPendingFragment.FriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.friendlist_requests_items, parent, false);
            return new FriendPendingFragment.FriendsHolder(view);

        }

        @Override
        public void onBindViewHolder(FriendPendingFragment.FriendsHolder holder, int position) {
            FriendsModel s = mFriends.get(position);
            holder.bindData(s);
        }

        @Override
        public int getItemCount() {
            return mFriends.size();
        }
    }
}
