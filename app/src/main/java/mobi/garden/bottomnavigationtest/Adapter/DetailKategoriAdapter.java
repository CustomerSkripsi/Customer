package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.DetailObatHome;
import mobi.garden.bottomnavigationtest.Model.ModelKategori;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;

public class DetailKategoriAdapter extends RecyclerView.Adapter<DetailKategoriAdapter.DetailKategoriViewHolder> {
    List<ModelKategori> modelKategoris;
    List<ModelPromo> detailKategoriList = new ArrayList<>();
    Context context;

    public DetailKategoriAdapter(List<ModelPromo> detailKategoriList, Context context) {
        this.detailKategoriList = detailKategoriList;
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
        final ModelPromo m = detailKategoriList.get(position);
        Picasso.with(context).load(m.getProductNameUrl()).into(holder.imgProduct, new Callback() {
            @Override
            public void onSuccess() { }
            @Override
            public void onError() {
                holder.imgProduct.setImageResource(R.drawable.nopicture);
            }
        });
        holder.tvNamaProdukPromo.setText(m.getPromoNameProduct());
        holder.tvHargaCoret.setText("Rp. "+String.valueOf(m.getProductPriceAfterDC()));
        holder.llproduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,DetailObatHome.class);
                i.putExtra("ProductName",m.getPromoNameProduct());
                Log.d("asdd", "onClick: "+m.getPromoNameProduct());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailKategoriList.size();
    }

    public static class DetailKategoriViewHolder extends RecyclerView.ViewHolder{
        TextView tvNamaProdukPromo,tvHargaCoret,tvHarga;
        ImageView imgProduct;
        LinearLayout llproduk;
        public DetailKategoriViewHolder(View itemView) {
            super(itemView);
            tvNamaProdukPromo = itemView.findViewById(R.id.tvNamaProdukPromo);
            tvHargaCoret = itemView.findViewById(R.id.tvHargaCoret);
            llproduk = itemView.findViewById(R.id.llproduk);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}
