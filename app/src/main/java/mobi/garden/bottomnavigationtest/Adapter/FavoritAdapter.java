package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.DetailObatHome;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;

public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.FavoritViewHolder> {
    List<ModelPromo> modelPromo;
    Context context;

    public FavoritAdapter(List<ModelPromo> modelPromo, Context context) {
        this.modelPromo = modelPromo;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoritAdapter.FavoritViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_produk,parent,false);
        return new FavoritViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritAdapter.FavoritViewHolder holder, int position) {
        final ModelPromo mp = modelPromo.get(position);
        holder.tvNamaProdukPromo.setText(mp.getPromoNameProduct());
        holder.tvHargaCoret.setText("Rp. "+String.valueOf(mp.getPriceProduct()));

        if(mp.getPriceProduct() != mp.getProductPriceAfterDC()){
            holder.tvHargaCoret.setPaintFlags(holder.tvHargaCoret.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
        }
//        if(mp.getProductNameUrl().equalsIgnoreCase("null")){
//            Picasso.with(context).load("http://www.pharmanet.co.id/images/logo.png").into(holder.imgProduct);
//        }else {
            Picasso.with(context).load(mp.getProductNameUrl()).into(holder.imgProduct, new Callback() {
                @Override
                public void onSuccess() {}
                @Override
                public void onError() {
                    holder.imgProduct.setImageResource(R.drawable.nopicture);
                    //Picasso.with(context).load("http://www.pharmanet.co.id/images/logo.png").into(holder.imgProduct);
                }
            });
        holder.llproduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,DetailObatHome.class);
                i.putExtra("ProductName",mp.getPromoNameProduct());
                context.startActivity(i);
            }
        });
//        }
    }

    @Override
    public int getItemCount() {
        return modelPromo.size();
    }

    public static class FavoritViewHolder extends RecyclerView.ViewHolder{
        TextView tvNamaProdukPromo,tvHargaCoret, tvharga;
        ImageView imgProduct;
        LinearLayout llproduk,ll_favorite;
        public FavoritViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNamaProdukPromo = itemView.findViewById(R.id.tvNamaProdukPromo);
            tvHargaCoret = itemView.findViewById(R.id.tvHargaCoret);
            tvharga = itemView.findViewById(R.id.tvHarga);
            llproduk = itemView.findViewById(R.id.llproduk);
            ll_favorite = itemView.findViewById(R.id.ll_favorite);
        }
    }
}
