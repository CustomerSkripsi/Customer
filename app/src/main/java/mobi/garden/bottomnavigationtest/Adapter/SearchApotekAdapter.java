package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.SearchResultApotek;
import mobi.garden.bottomnavigationtest.Model.apotek;
import mobi.garden.bottomnavigationtest.R;

public class SearchApotekAdapter extends RecyclerView.Adapter<SearchApotekAdapter.SearchApotekViewHolder> {
    List<apotek> apoteks;
    Context context;

    public SearchApotekAdapter(List<apotek> apoteks, Context context) {
        this.apoteks = apoteks;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchApotekAdapter.SearchApotekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_search_apotek,parent,false);
        return new SearchApotekAdapter.SearchApotekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchApotekAdapter.SearchApotekViewHolder holder, int position) {
        final apotek ap = apoteks.get(position);
        holder.tvnamaapotek.setText(ap.getNama_apotek());
        holder.tvJamOperasional.setText(ap.getOutletOprOpen()+"-"+ap.getOutletOprClose());
        holder.llapotek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,SearchResultApotek.class);
                i.putExtra("ApotekName", ap.getNama_apotek());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apoteks.size();
    }

    public class SearchApotekViewHolder extends RecyclerView.ViewHolder{
        TextView tvnamaapotek, tvJamOperasional;
        LinearLayout llapotek;
        public SearchApotekViewHolder(View itemView){
            super(itemView);
            tvnamaapotek = itemView.findViewById(R.id.tvnamaapotek);
            tvJamOperasional = itemView.findViewById(R.id.tvJamOperasional);
            llapotek = itemView.findViewById(R.id.llApotek);
        }
    }
}
