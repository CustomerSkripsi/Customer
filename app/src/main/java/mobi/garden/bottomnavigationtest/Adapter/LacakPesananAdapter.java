package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mobi.garden.bottomnavigationtest.Activity.LacakPesananDetail;
import mobi.garden.bottomnavigationtest.Model.Lacak;
import mobi.garden.bottomnavigationtest.R;

public class LacakPesananAdapter extends RecyclerView.Adapter<LacakPesananAdapter.LacakPesananViewHolder> {

    private ArrayList<Lacak> mLacakList;

    public LacakPesananAdapter(ArrayList<Lacak> LacakList){
        mLacakList = LacakList;
    }


    @NonNull
    @Override
    public LacakPesananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list,parent, false);
        LacakPesananViewHolder lacakPesananViewHolder = new LacakPesananViewHolder(view);
        return lacakPesananViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LacakPesananAdapter.LacakPesananViewHolder holder, int position) {
        final Lacak currentItem = mLacakList.get(position);

        holder.orderID.setText(currentItem.getOrderID());
        holder.tanggalTransaksi.setText(currentItem.getTanggal());
        holder.outletName.setText(currentItem.getOutletName());
        holder.statusOrder.setText(currentItem.getStatusOrder());

        if (currentItem.getStatusOrder().equals("Accepted"))
        {
            holder.statusOrder.setTextColor(Color.parseColor("#228B22"));
        }
        else if(currentItem.getStatusOrder().equals("Received"))
        {
            holder.statusOrder.setTextColor(Color.parseColor("#228B22"));
        }
        else if(currentItem.getStatusOrder().equals("Proses"))
        {
            holder.statusOrder.setTextColor(Color.parseColor("#fff202"));
        }
        else if(currentItem.getStatusOrder().equals("Rejected"))
        {
            holder.statusOrder.setTextColor(Color.parseColor("#ff0800"));
        }

        holder.transaksiDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), LacakPesananDetail.class);

                i.putExtra("OrderID", currentItem.getOrderID());
                i.putExtra("Tanggal", currentItem.getTanggal());
                i.putExtra("OutletName", currentItem.getOutletName());
                i.putExtra("StatusOrder", currentItem.getStatusOrder());
                // i.putExtra("StatusOrderID", currentItem.getStatusOrderId());


                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLacakList.size();
    }

    public class LacakPesananViewHolder extends RecyclerView.ViewHolder{
        TextView orderID, tanggalTransaksi, outletName, statusOrder;
        ImageView transaksiDetail;

        public LacakPesananViewHolder(View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.tv_OrderID);
            tanggalTransaksi = itemView.findViewById(R.id.tv_tanggalTransaksi);
            outletName = itemView.findViewById(R.id.tv_namaApotek);
            statusOrder = itemView.findViewById(R.id.tv_StatusOrder);
            transaksiDetail = itemView.findViewById(R.id.iv_transaksiDetail);
        }
    }
}
