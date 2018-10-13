package fragments;

import android.os.Bundle;
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

import java.util.ArrayList;

public class FriendsFragment extends Fragment {
    private String[] friendNames = {"Marie Curie","Thomas Edison","Albert Einstein","Michael Faraday","Galileo Galilei",
            "Stephen Hawking","Johannes Kepler","Issac Newton","Nikola Tesla"};

    private ArrayList<FriendsModel> mFriends;
    private RecyclerView mSFriendsRecyclerView;
    private FriendsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mFriends = new ArrayList<>();
        for(int i =0;i<friendNames.length;i++){
            FriendsModel friends = new FriendsModel();
            friends.setName(friendNames[i]);
            mFriends.add(friends);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
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

