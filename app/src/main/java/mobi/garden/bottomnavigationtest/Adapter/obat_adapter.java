package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.DetailObatHome;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.Model.session_obat;
import mobi.garden.bottomnavigationtest.R;

/**
 * Created by ASUS on 5/7/2018.
 */

public class obat_adapter extends RecyclerView.Adapter<obat_adapter.obatViewHolder> {

    List<obat> obatlist;
    Context context;


    public obat_adapter(Context c, List<obat> obatlist) {
        this.obatlist = obatlist;
        this.context = c;
    }

    @Override
    public obat_adapter.obatViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.cv_obat, viewGroup, false);

        return new obat_adapter.obatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(obat_adapter.obatViewHolder holder, int position) {
        final session_obat session ;
        session=new session_obat(context);
        session.logOutUser();
        final obat pr = obatlist.get(position);
        Picasso.with(context).load(pr.productPhoto).into(holder.iv_picture_obat);
        holder.tv_nama_obat.setText(pr.productName);

        holder.btn_detail_obat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.createLoginSession(pr.getProductID(),pr.getProductName(),pr.getProductPhoto(),pr.getProductDescription(),pr.getIndikasi(),pr.getKandungan(),pr.getDosis(),pr.getCarapakai(),pr.getKemasan(),pr.getGolongan(),pr.getResepYN(),pr.getKontraindikasi(),pr.getCarasimpan(),pr.getPrincipal(),pr.categoryID);
                Intent i=new Intent(context,DetailObatHome.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return obatlist.size();
    }

    public static class obatViewHolder extends RecyclerView.ViewHolder {

        int i=0;
        ImageView iv_picture_obat;
        LinearLayout ll_cv_obat;
        TextView tv_nama_obat;
        Button btn_detail_obat;

        public obatViewHolder(View v) {
            super(v);

            iv_picture_obat=(ImageView) v.findViewById(R.id.iv_picture_obat);
            ll_cv_obat = (LinearLayout) v.findViewById(R.id.ll_cv_obat);
            tv_nama_obat= (TextView) v.findViewById(R.id.tv_nama_obat);
            btn_detail_obat=(Button)v.findViewById(R.id.btn_detail_obat);
        }
    }
}
