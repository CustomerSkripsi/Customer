package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;

public class PromoSelengkapnyaAdapter extends RecyclerView.Adapter<PromoSelengkapnyaAdapter.PromoSelengkapnyaViewHolder>{
    List<ModelPromo> modelPromo;
    Context context;
    String tempurl;

    public PromoSelengkapnyaAdapter(List<ModelPromo> modelPromo, Context context) {
        this.modelPromo = modelPromo;
        this.context = context;
    }

    @NonNull
    @Override
    public PromoSelengkapnyaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_promo_selengkapnya,parent,false);
        return new PromoSelengkapnyaAdapter.PromoSelengkapnyaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoSelengkapnyaViewHolder holder, int position) {
        final ModelPromo mp = modelPromo.get(position);

        holder.tvNamaProdukPromo.setText(mp.getPromoNameProduct());
        holder.tvHargaCoret.setText("Rp. "+String.valueOf(mp.getPriceProduct()));
        holder.tvharga.setText("Rp. "+String.valueOf(mp.getProductPriceAfterDC()));

        if(mp.getPriceProduct() != mp.getProductPriceAfterDC()){
            holder.tvHargaCoret.setPaintFlags(holder.tvHargaCoret.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
        }

        tempurl = mp.getProductNameUrl();
        Log.d("onBindViewHolder: ",tempurl);
        if(tempurl.contains(" ")){
            tempurl = tempurl.replace(" ","%20");
        }
        Picasso.with(context).load(tempurl).into(holder.imgProduct, new Callback() {
            @Override
            public void onSuccess() {
                Picasso.with(context).load(tempurl).into(holder.imgProduct);
            }
            @Override
            public void onError() {
                holder.imgProduct.setImageResource(R.drawable.nopicture);
                //Picasso.with(context).load("http://www.pharmanet.co.id/images/logo.png").into(holder.imgProduct);
            }
        });

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelPromo.size();
    }

    public static class PromoSelengkapnyaViewHolder extends RecyclerView.ViewHolder{
        TextView tvNamaProdukPromo,tvHargaCoret, tvharga;
        ImageView imgProduct;
        LinearLayout llproduk;
        Button btnAdd;
        public PromoSelengkapnyaViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNamaProdukPromo = itemView.findViewById(R.id.tvNamaProdukPromo);
            tvHargaCoret = itemView.findViewById(R.id.tvHargaCoret);
            tvharga = itemView.findViewById(R.id.tvHarga);
            btnAdd = itemView.findViewById(R.id.btn_add_obat);
            llproduk = itemView.findViewById(R.id.llproduk);
        }
    }
}
