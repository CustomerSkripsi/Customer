package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.HomeActivity;
import mobi.garden.bottomnavigationtest.Model.Rating;
import mobi.garden.bottomnavigationtest.R;

public class RatingAdapter  extends RecyclerView.Adapter<RatingAdapter.buttonViewHolder> {
    List<Rating> RatingList;
    Context context;
    EditText mFeedback;
    boolean click = true;
    int lastSelectedPosition = -1;

    public RatingAdapter(List<Rating> ratingList, Context context, EditText mFeedback) {
        this.RatingList = ratingList;
        this.context = context;
        this.mFeedback = mFeedback;
    }

    @NonNull
    @Override
    public RatingAdapter.buttonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_rating,parent,false);
        return new buttonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.buttonViewHolder holder, int position) {
        final Rating butt = RatingList.get(position);
        holder.btnFeedback.setText(butt.mFeedbackOption);
        if (!butt.isChecked){
            holder.btnFeedback.setBackgroundResource(R.drawable.btn_choise);
            holder.btnFeedback.setTextColor(ContextCompat.getColor(context, R.color.green));
        }else{
            holder.btnFeedback.setBackgroundResource(R.drawable.btn_beli_clicked);
            holder.btnFeedback.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        holder.btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lastSelectedPosition >= 0){
                    RatingList.get(lastSelectedPosition).setChecked(false);
                }
                butt.setChecked(!butt.isChecked);
                Log.d("Current_button", butt.isChecked+"");
                Log.d("Current_button", lastSelectedPosition+"");
                try {
                    Log.d("Last_Button",RatingList.get(lastSelectedPosition).isChecked+"");
                }catch (Exception e){
                    Log.d("error",e+"");
                }
                lastSelectedPosition = holder.getAdapterPosition();
                HomeActivity.number = butt.mFeedbackOptionID;
                notifyDataSetChanged();
            }
        });
    }

    public void clear() {
        final int size = RatingList.size();
        RatingList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return RatingList.size();
    }

    public static class buttonViewHolder extends RecyclerView.ViewHolder{
        android.widget.Button btnFeedback;
        public buttonViewHolder(View itemview){
            super(itemview);
            btnFeedback = itemview.findViewById(R.id.btn_feedback);
        }
    }

}
