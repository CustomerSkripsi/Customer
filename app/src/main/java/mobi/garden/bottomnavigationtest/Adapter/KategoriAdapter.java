package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.DetailKategori;
import mobi.garden.bottomnavigationtest.Model.ModelKategori;
import mobi.garden.bottomnavigationtest.R;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.kategoriViewHolder> {
    List<ModelKategori> modelKategoris;
    Context context;

    public KategoriAdapter(List<ModelKategori> modelKategoris, Context context) {
        this.modelKategoris = modelKategoris;
        this.context = context;
    }

    public static class kategoriViewHolder extends RecyclerView.ViewHolder {
        android.widget.Button btnFeedback;

        public kategoriViewHolder(View itemView) {
            super(itemView);
            btnFeedback = itemView.findViewById(R.id.btn_feedback);
        }
    }

    @Override
    public KategoriAdapter.kategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_kategori, parent, false);
        return new kategoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(kategoriViewHolder holder, int position) {
        final ModelKategori m = modelKategoris.get(position);
        holder.btnFeedback.setText(m.CategoryName);
        holder.btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailKategori.class);
                intent.putExtra("CategoryName", m.getCategoryName());
                context.startActivity(intent);
                //Toast.makeText(context, "->"+m.getCategoryName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelKategoris.size();
    }


}
