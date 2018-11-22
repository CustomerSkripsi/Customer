package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import mobi.garden.bottomnavigationtest.Model.ModelKategori;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.PromoViewHolder> {
    List<ModelPromo> modelPromo;
    Context context;

    public PromoAdapter(List<ModelPromo> modelPromo, Context context) {
        this.modelPromo = modelPromo;
        this.context = context;
    }

    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_produk,parent,false);
        return new PromoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PromoViewHolder holder, int position) {
        final ModelPromo mp = modelPromo.get(position);
        //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
        holder.tvNamaProdukPromo.setText(mp.PromoNameProduct);
        holder.tvHargaCoret.setText("Rp. "+String.valueOf(mp.PriceProduct));
        //holder.tvharga.setText("Rp. "+String.valueOf(mp.PriceProduct));
//        if(mp.ProductNameUrl.contains("/")){
//            mp.ProductNameUrl = mp.ProductNameUrl.replace("\\/","");
//        }
        //Toast.makeText(context, ""+mp.ProductNameUrl, Toast.LENGTH_SHORT).show();
        if(mp.ProductNameUrl.equalsIgnoreCase("null")){
            Picasso.with(context).load("http://www.pharmanet.co.id/images/logo.png").into(holder.imgProduct);
        }else {
            Picasso.with(context).load(mp.ProductNameUrl).into(holder.imgProduct, new Callback() {
                @Override
                public void onSuccess() {}
                @Override
                public void onError() {
                    Picasso.with(context).load("http://www.pharmanet.co.id/images/logo.png").into(holder.imgProduct);
                }
            });
        }
       // Picasso.with(context).load("http://pharmaapp.pharmanet.co.id/public/data/images/product/zoom/0100175.jpg").into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return modelPromo.size();
    }

    public static class PromoViewHolder extends RecyclerView.ViewHolder{
        TextView tvNamaProdukPromo,tvHargaCoret, tvharga;
        ImageView imgProduct;
        public PromoViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNamaProdukPromo = itemView.findViewById(R.id.tvNamaProdukPromo);
            tvHargaCoret = itemView.findViewById(R.id.tvHargaCoret);
            tvharga = itemView.findViewById(R.id.tvHarga);
        }
    }
}
