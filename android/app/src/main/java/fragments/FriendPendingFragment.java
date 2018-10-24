package fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ncshare.ncshare.R;
import com.ncshare.ncshare.SimpleDividerItemDecoration;

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
    private ArrayList<FriendRequest> mFriendRequestList;
    private RecyclerView mSFriendsRecyclerView;
    private FriendPendingFragment.FriendsAdapter mAdapter;
    private Session mSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSession = SessionHandler.getSession();
        mFriendRequestList = new ArrayList<>();
        mFriendRequestList.clear();

        Call<List<FriendRequest>> call = RetrofitClient
                .getInstance()
                .getFriendRequestApi()
                .getByRecipientID(mSession.getUser().getUid());
        call.enqueue(new Callback<List<FriendRequest>>() {
            @Override
            public void onResponse(Call<List<FriendRequest>> call, Response<List<FriendRequest>> response) {
                mFriendRequestList.addAll(response.body());
            }
            @Override
            public void onFailure(Call<List<FriendRequest>> call, Throwable t) {}
        });
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
        mAdapter = new FriendPendingFragment.FriendsAdapter(mFriendRequestList);
        mSFriendsRecyclerView.setAdapter(mAdapter);
    }

    private class FriendsHolder extends RecyclerView.ViewHolder {

        private FriendRequest mFriendRequests;
        public TextView mNameTextView, mUsernameTextView;
        public ImageButton mConfirmBtn, mRejectBtn;

        public FriendsHolder(View itemView) {
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.friends_name);
            mUsernameTextView = (TextView) itemView.findViewById(R.id.friends_username);
            mConfirmBtn = (ImageButton) itemView.findViewById(R.id.btnCfm);
            mRejectBtn = (ImageButton) itemView.findViewById(R.id.btnRej);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            mFriendRequests.getRequester() + " clicked!", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        public void bindData(FriendRequest s) {
            mFriendRequests = s;
            mNameTextView.setText(s.getRequester());
            mUsernameTextView.setText("Username : " + s.getRequester_username());
            btnRejectOnClickListener(s, s.get_id());
        }

        public void btnConfirmOnClickListener(FriendRequest request){
            mConfirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                }
            });
        }

        public void btnRejectOnClickListener(final FriendRequest request, final String id){
            mRejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<FriendRequest> call = RetrofitClient
                            .getInstance()
                            .getFriendRequestApi()
                            .deleteRequest(id);
                    call.enqueue(new Callback<FriendRequest>() {
                        @Override
                        public void onResponse(Call<FriendRequest> call, Response<FriendRequest> response) {
                            Toast.makeText(getActivity(), "Friend request deleted", Toast.LENGTH_SHORT).show();
                            mFriendRequestList.remove(request);
                            mAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onFailure(Call call, Throwable t) {}
                    });
                }
            });
        }
    }

    private class FriendsAdapter extends RecyclerView.Adapter<FriendPendingFragment.FriendsHolder> {
        private ArrayList<FriendRequest> mFriendRequests;

        public FriendsAdapter(ArrayList<FriendRequest> requests) {
            mFriendRequests = requests;
        }

        @Override
        public FriendPendingFragment.FriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.friendlist_requests_items, parent, false);
            return new FriendPendingFragment.FriendsHolder(view);
        }

        @Override
        public void onBindViewHolder(FriendPendingFragment.FriendsHolder holder, int position) {
            FriendRequest s = mFriendRequests.get(position);
            holder.bindData(s);
        }

        @Override
        public int getItemCount() {
            return mFriendRequests.size();
        }
    }
}
