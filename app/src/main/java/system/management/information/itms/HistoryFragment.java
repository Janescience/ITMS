package system.management.information.itms;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class HistoryFragment extends Fragment {

    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_history, container, false);

        mBlogList = (RecyclerView) rootview.findViewById(R.id.blog_list);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        return rootview;
    }


    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<History, HistoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<History, HistoryViewHolder>(

                History.class,
                R.layout.history_row,
                HistoryViewHolder.class,
                mDatabase

        ) {
            protected void populateViewHolder(HistoryViewHolder viewHolder, History model, int position) {
                viewHolder.setHeader(model.getHeader());
                viewHolder.setDetail(model.getDetail());
                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());
                viewHolder.setDate(model.getDate());
            }

        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        View mView;


        public HistoryViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setHeader(String header) {
            TextView post_header = (TextView) mView.findViewById(R.id.post_header);
            post_header.setText(header);
        }

        public void setDetail(String detail) {
            TextView post_details = (TextView) mView.findViewById(R.id.post_details);
            post_details.setText(detail);
        }

        public void setImage(Context ctx, String photos) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(photos).fit().centerCrop().into(post_image);
        }

        public void setDate(String date){
            TextView post_date = (TextView) mView.findViewById(R.id.post_date);
            post_date.setText(date);
        }
    }
}


