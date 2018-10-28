package fragments;

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

import com.ncshare.ncshare.R;
import com.ncshare.ncshare.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import common.SessionHandler;
import models.FriendRequest;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.RetrofitClient;

public class FriendPendingFragment extends Fragment {
    private ArrayList<FriendRequest> mFriendRequestList;
    private RecyclerView mSFriendsRecyclerView;
    private FriendPendingFragment.FriendsAdapter mAdapter;
    private SessionHandler mSession;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSession = new SessionHandler(this.getContext());
        mFriendRequestList = new ArrayList<>();
        mFriendRequestList.clear();

        user = mSession.getUserDetails();
        Call<List<FriendRequest>> call = RetrofitClient
                .getInstance()
                .getFriendRequestApi()
                .getByRecipientID(user.getToken(), user.getUid());
        call.enqueue(new Callback<List<FriendRequest>>() {
            @Override
            public void onResponse(Call<List<FriendRequest>> call, Response<List<FriendRequest>> response) {
                if (response.code() == 200)
                    mFriendRequestList.addAll(response.body());
                    Log.i("REQ SIZE", String.valueOf(mFriendRequestList.size()));
            }

            @Override
            public void onFailure(Call<List<FriendRequest>> call, Throwable t) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_pending, container, false);
        mSFriendsRecyclerView = (RecyclerView) view.findViewById(R.id.requests_recycler_view);
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

        private FriendRequest mFriendRequest;
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
                            mFriendRequest.getRequester() + " clicked!", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        public void bindData(FriendRequest req) {
            mFriendRequest = req;
            mNameTextView.setText(req.getRequester());
            mUsernameTextView.setText("Username : " + req.getRequester_username());
            btnConfirmOnClickListener(req);
            btnRejectOnClickListener(req);
        }

        public void btnConfirmOnClickListener(final FriendRequest req) {
            mConfirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    user = mSession.getUserDetails();
                    // Add requester to friend list
                    Call<FriendRequest> call = RetrofitClient
                            .getInstance()
                            .getFriendRequestApi()
                            .addFriend(user.getToken(), req.getRecipient_id(),
                                    req.getRequester_id() + "," + req.getRequester() + "," + req.getRequester_username());
                    call.enqueue(new Callback<FriendRequest>() {
                        @Override
                        public void onResponse(Call<FriendRequest> call, Response<FriendRequest> response) {
                        }

                        @Override
                        public void onFailure(Call<FriendRequest> call, Throwable t) {
                        }
                    });
                    // Add yourself to requester's friend list
                    Call<FriendRequest> call2 = RetrofitClient
                            .getInstance()
                            .getFriendRequestApi()
                            .addFriend(user.getToken(), req.getRequester_id(),
                                    req.getRecipient_id() + "," + req.getRecipient() + "," + req.getRecipient_username());
                    call2.enqueue(new Callback<FriendRequest>() {
                        @Override
                        public void onResponse(Call<FriendRequest> call, Response<FriendRequest> response) {
                            Toast.makeText(getActivity(), "Friend added", Toast.LENGTH_SHORT).show();
                            removeReqCall(req);
                        }

                        @Override
                        public void onFailure(Call<FriendRequest> call, Throwable t) {
                        }
                    });
                }
            });
        }

        public void btnRejectOnClickListener(final FriendRequest req) {
            mRejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeReqCall(req);
                    Toast.makeText(getActivity(), R.string.msg_delete_friend_request, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void removeReqCall(final FriendRequest req) {
            Call<FriendRequest> call = RetrofitClient
                    .getInstance()
                    .getFriendRequestApi()
                    .deleteRequest(user.getToken(), req.get_id());
            call.enqueue(new Callback<FriendRequest>() {
                @Override
                public void onResponse(Call<FriendRequest> call, Response<FriendRequest> response) {
                    switch (response.code()) {
                        case 200:
                            try {
                                mFriendRequestList.remove(req);
                                mAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            Toast.makeText(getActivity(), R.string.msg_friend_request_not_removed, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
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
