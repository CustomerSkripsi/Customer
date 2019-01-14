package mobi.garden.bottomnavigationtest.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.HashMap;

import mobi.garden.bottomnavigationtest.Adapter.FirebaseRecyclerAdapter;
import mobi.garden.bottomnavigationtest.Model.ChatModel;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class InboxActivity extends AppCompatActivity {
    private static String TAG = "FirebaseUI.chat";
    private Query mInboxRef;
    private RecyclerView mInbox;
    private Firebase mRef,mRef2;

    private SessionManagement session;
    private HashMap<String, String> login;
    private static String memberID;
    public static FirebaseRecyclerAdapter<ChatModel, InboxHolder> mRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        Firebase.setAndroidContext(this);

        if(getIntent().getExtras()!=null){
            for(String key : getIntent().getExtras().keySet()){
                String value = getIntent().getExtras().getString(key);
                Log.d("TAG", "KEY : " + key + "Value : " + value);
            }
        }

        session = new SessionManagement(getApplicationContext());
        login = session.getMemberDetails();
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);
        mInbox = findViewById(R.id.rvInbox);

        mRef = new Firebase("https://pharmanetb2b-development.firebaseio.com/Customer/PHARMACHAT_adminb2c_ChatWith_"+memberID);
        mInboxRef = mRef.limitToLast(256);


        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(false);
        mInbox.setHasFixedSize(false);
        mInbox.setLayoutManager(manager);

        mRecycleViewAdapter = new FirebaseRecyclerAdapter<ChatModel,
                InboxHolder>(ChatModel.class, R.layout.cv_inbox, InboxHolder.class, mInboxRef){

            @Override
            protected void populateViewHolder(InboxHolder viewHolder, ChatModel model, int position) {
                Log.d("test", model.getName());
                viewHolder.setTitle(model.getName());
                viewHolder.setDate(model.getFormattedTime());
                viewHolder.setText(model.getMessage());
            }
        };

        mRecycleViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mRecycleViewAdapter.getItemCount();

                int lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mInbox.scrollToPosition(positionStart);
                }
                mInbox.smoothScrollToPosition(mRecycleViewAdapter.getItemCount());
            }
        });

        mInbox.setLayoutManager(manager);
        mInbox.setAdapter(mRecycleViewAdapter);

        Toolbar mToolbar = findViewById(R.id.tbConfirmation);
        mToolbar.setTitle("Inbox");
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static class InboxHolder extends RecyclerView.ViewHolder {
        View mView;
        public InboxHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title){
            TextView textView = mView.findViewById(R.id.titleInbox);
            textView.setText(title);
        }

        public void setDate(String date){
            TextView textView = mView.findViewById(R.id.dateInbox);
            textView.setText(date);
        }

        public void setText(String text){
            TextView textView = mView.findViewById(R.id.msginbox);
            textView.setText(text);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
