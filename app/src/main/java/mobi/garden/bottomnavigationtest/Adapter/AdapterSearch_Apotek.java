package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import mobi.garden.bottomnavigationtest.Activity.ApotekActivity;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Model.Model_Apotek;

import java.util.ArrayList;
import java.util.List;

public class AdapterSearch_Apotek extends RecyclerView.Adapter<AdapterSearch_Apotek.SearchApotekViewHolder> {

    private List<Model_Apotek> modelApotekList = new ArrayList<>();
    Context context;
    private int selectedPos = RecyclerView.NO_POSITION;

    public AdapterSearch_Apotek(Context context) {
        this.context = context;
    }

    public List<Model_Apotek> getModelSearchList() {
        return modelApotekList;
    }

    public void setModelApotekList(List<Model_Apotek> modelApotekList) {
        this.modelApotekList = modelApotekList;
    }

    @NonNull
    @Override
    public SearchApotekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_search, parent
                , false);
        return new SearchApotekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchApotekViewHolder holder, int position) {
        Model_Apotek modelSearch = modelApotekList.get(position);

        holder.tvModelName.setText(modelSearch.OutletName);
        holder.itemView.setBackgroundColor(selectedPos == position ? Color.LTGRAY : Color.TRANSPARENT);

        holder.tvModelName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ApotekActivity.class);
                intent.putExtra("outletname", modelSearch.OutletName + "");
                Toast.makeText(context, "You've Just Press : " + modelSearch.OutletID, Toast.LENGTH_SHORT).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("OutletID", modelSearch.OutletID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelApotekList.size();
    }

    public class SearchApotekViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvModelName;

        public SearchApotekViewHolder(View itemView) {
            super(itemView);
            tvModelName = itemView.findViewById(R.id.listSearch);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) {
                Model_Apotek onClick = modelApotekList.get(pos);
                Toast.makeText(v.getContext(), "you clicked " + onClick.OutletName, Toast.LENGTH_SHORT).show();
                notifyItemChanged(selectedPos);
                selectedPos = getLayoutPosition();
                notifyItemChanged(selectedPos);
            }
        }
    }
}







