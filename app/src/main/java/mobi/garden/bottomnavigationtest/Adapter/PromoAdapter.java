package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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

import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.DetailObatHome;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.R;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.PromoViewHolder> {
    List<ModelPromo> modelPromo;
    Context context;
    String tempurl;
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

        holder.tvNamaProdukPromo.setText(mp.getPromoNameProduct());
        holder.tvHargaCoret.setText("Rp. "+String.valueOf(mp.getPriceProduct()));
        holder.tvharga.setText("Rp. "+String.valueOf(mp.getProductPriceAfterDC()));
//        if(mp.getProductPriceAfterDC()==0){
//            holder.tvharga.setText();
//        }
        if(mp.getPriceProduct() != mp.getProductPriceAfterDC()){
            holder.tvHargaCoret.setPaintFlags(holder.tvHargaCoret.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
        }
//        if(mp.getProductNameUrl().equalsIgnoreCase("null")){
//            Picasso.with(context).load("http://www.pharmanet.co.id/images/logo.png").into(holder.imgProduct);
//        }else {
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

        holder.llproduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,DetailObatHome.class);
                i.putExtra("ProductName",mp.getPromoNameProduct());
                Log.d("namaputexxtrny", ""+mp.getPromoNameProduct());
                context.startActivity(i);
            }
        });

//        }

       // Picasso.with(context).load("http://pharmaapp.pharmanet.co.id/public/data/images/product/zoom/0100175.jpg").into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return modelPromo.size();
    }

    public static class PromoViewHolder extends RecyclerView.ViewHolder{
        TextView tvNamaProdukPromo,tvHargaCoret, tvharga;
        ImageView imgProduct;
        LinearLayout llproduk;
        public PromoViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNamaProdukPromo = itemView.findViewById(R.id.tvNamaProdukPromo);
            tvHargaCoret = itemView.findViewById(R.id.tvHargaCoret);
            tvharga = itemView.findViewById(R.id.tvHarga);
            llproduk = itemView.findViewById(R.id.llproduk);
        }
    }
}
