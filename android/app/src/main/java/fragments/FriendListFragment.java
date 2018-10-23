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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ncshare.ncshare.FriendsModel;
import com.ncshare.ncshare.R;
import com.ncshare.ncshare.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment {
    private String[] friendNames = {"Marie Curie","Thomas Edison","Albert Einstein","Michael Faraday","Galileo Galilei",
            "Stephen Hawking","Johannes Kepler","Issac Newton","Nikola Tesla","Albert Einstein","Michael Faraday","Galileo Galilei",
            "Stephen Hawking","Johannes Kepler","Issac Newton","Nikola Tesla"};

    private String[] schools = {"SIT","TP","NYP","SIT","SMU",
            "RP","NTU","SIT","SIT","NYP","SIT","SMU",
            "RP","NTU","SIT","SIT"};
    private ArrayList<FriendsModel> mFriends;
    private RecyclerView mSFriendsRecyclerView;
    private FriendListFragment.FriendsAdapter mAdapter;
    private ListView lvNC;

    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriends = new ArrayList<>();
        for(int i =0;i<friendNames.length;i++){
            FriendsModel friends = new FriendsModel();
            friends.setName(friendNames[i]);
            friends.setSchool(schools[i]);
            mFriends.add(friends);
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
        lvNC = (ListView)row.findViewById(R.id.lvNamecards);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, friendNames);
        lvNC.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        alertDialog.setView(row);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
        lvNC.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                String s = lvNC.getItemAtPosition(position).toString();
                Toast.makeText(getContext().getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUI(){
        mAdapter = new FriendListFragment.FriendsAdapter(mFriends);
        mSFriendsRecyclerView.setAdapter(mAdapter);
    }

    private class FriendsHolder extends RecyclerView.ViewHolder{

        private FriendsModel mFriends;
        public TextView mNameTextView, mSchoolTextView;
        public ImageButton btnSend, btnDelete;

        public FriendsHolder(View itemView){
            super(itemView);

            mNameTextView = (TextView) itemView.findViewById(R.id.friends_name);
            mSchoolTextView = (TextView) itemView.findViewById(R.id.friends_school);
            btnSend = (ImageButton) itemView.findViewById(R.id.btnSend);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);
/*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            mFriends.getName() + " clicked!", Toast.LENGTH_SHORT)
                            .show();
                }
            });
*/
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            mFriends.getName() + " this is clicked!", Toast.LENGTH_SHORT)
                            .show();
                    open_dialog(v);
                }
            });
        }
        public void bindData(FriendsModel s){
            mFriends = s;
            mNameTextView.setText(s.getName());
            mSchoolTextView.setText(s.getSchool());
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
}
