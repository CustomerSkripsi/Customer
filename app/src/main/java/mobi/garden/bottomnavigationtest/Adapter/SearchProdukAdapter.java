package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.DetailObatHome;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;

public class SearchProdukAdapter extends RecyclerView.Adapter<SearchProdukAdapter.SearchProdukViewHolder> {
    List<ModelPromo> mp;
    Context context;

    public SearchProdukAdapter(List<ModelPromo> mp, Context context) {
        this.mp = mp;
        this.context = context;
    }


    @NonNull
    @Override
    public SearchProdukAdapter.SearchProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_search_product,parent,false);
        return new SearchProdukAdapter.SearchProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProdukAdapter.SearchProdukViewHolder holder, int position) {
        final ModelPromo m = mp.get(position);
        holder.tvNama.setText(m.getPromoNameProduct());
        holder.tvNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,DetailObatHome.class);
                i.putExtra("ProductName",m.getPromoNameProduct());
                i.putExtra("ProductImage",m.getProductNameUrl());
                context.startActivity(i);
            }
        });

//        holder.llproduk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(context, DetailObatHome.class);
//                i.putExtra("ProductName",m.getPromoNameProduct());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mp.size();
    }

    public class SearchProdukViewHolder extends RecyclerView.ViewHolder{
        TextView tvNama;
        LinearLayout llproduk;
        public SearchProdukViewHolder(View itemView){
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvnama);
            llproduk = itemView.findViewById(R.id.llproduk);
        }
    }
}
