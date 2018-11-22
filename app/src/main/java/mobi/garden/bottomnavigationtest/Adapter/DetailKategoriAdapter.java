package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mobi.garden.bottomnavigationtest.Model.ModelKategori;
import mobi.garden.bottomnavigationtest.R;

public class DetailKategoriAdapter extends RecyclerView.Adapter<DetailKategoriAdapter.DetailKategoriViewHolder> {
    List<ModelKategori> modelKategoris;
    Context context;

    public DetailKategoriAdapter(List<ModelKategori> modelKategoris, Context context) {
        this.modelKategoris = modelKategoris;
        this.context = context;
    }

    @Override
    public DetailKategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_produk,parent,false);
        return new DetailKategoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailKategoriViewHolder holder, int position) {
        final ModelKategori m = modelKategoris.get(position);
        //Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
        holder.tvNamaProdukPromo.setText(m.CategoryName);
    }

    @Override
    public int getItemCount() {
        return modelKategoris.size();
    }

    public static class DetailKategoriViewHolder extends RecyclerView.ViewHolder{
        TextView tvNamaProdukPromo;
        public DetailKategoriViewHolder(View itemView) {
            super(itemView);
            tvNamaProdukPromo = itemView.findViewById(R.id.tvNamaProdukPromo);
        }
    }
}
