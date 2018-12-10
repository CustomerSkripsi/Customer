package mobi.garden.bottomnavigationtest.Adapter;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.apotek;
import mobi.garden.bottomnavigationtest.Model.session_obat;
import mobi.garden.bottomnavigationtest.R;

/**
 * Created by ASUS on 5/8/2018.
 */

public class apotek_adapter extends RecyclerView.Adapter<apotek_adapter.apotekViewHolder> {

    List<apotek> apoteklist;
    Context context;
    session_obat session;

    String CustomerID;
    UserLocalStore userLocalStore;
    DecimalFormat df;

    Activity activity;

    double userLong;
    double userlat;

    public apotek_adapter(Context c, List<apotek> apoteklist,double longitude,double latitude) {
        this.apoteklist = apoteklist;
        this.context = c;
        session = new session_obat(c);
        userLong = longitude;
        userlat = latitude;
    }

    public apotek_adapter(Context c, List<apotek> apoteklist, Activity activity) {
        this.apoteklist = apoteklist;
        this.context = c;
        session = new session_obat(c);
        this.activity = activity;
    }

    @Override
    public apotek_adapter.apotekViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.cv_apotek, viewGroup, false);

        return new apotek_adapter.apotekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(apotek_adapter.apotekViewHolder holder, int position) {
        final apotek pr = apoteklist.get(position);
        userLocalStore = new UserLocalStore(context);
        User currUser = userLocalStore.getLoggedInUser();
        CustomerID = currUser.getUserID();

        df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);

//
        Location loc1 = new Location("");
        loc1.setLatitude(userlat);
        loc1.setLongitude(userLong);

        Location loc2 = new Location("");
        loc2.setLatitude(pr.latitude);
        loc2.setLongitude(pr.longitude);

        double distanceInMeters = loc1.distanceTo(loc2)/1000;

        Log.d("Jarak", distanceInMeters+"");

        holder.tv_nama_apotek.setText(pr.getNama_apotek());
        holder.tv_harga_obat_apotek.setText(df.format(pr.getHarga())+"");
        holder.tv_stok_obat_apotek.setText(String.valueOf(pr.getStok()));
        if(userLong != 0 && userlat !=0)
        {
            holder.tv_jarak.setText(String.format("%.1f",distanceInMeters)+" KM");
        }
        else
        {
            holder.tv_jarak.setText("0 KM");
        }

        holder.RatObat.setRating(pr.getRating());
        holder.RatObat.setEnabled(false);
        holder.tv_jam_open.setText(pr.getOutletOprOpen());
        holder.tv_jam_close.setText(pr.getOutletOprClose());


    }

    @Override
    public int getItemCount() {
        return apoteklist.size();
    }

    public static class apotekViewHolder extends RecyclerView.ViewHolder {
        RatingBar RatObat;

        TextView tv_nama_apotek,tv_harga_obat_apotek,tv_stok_obat_apotek,tv_jarak, tv_jam_open, tv_jam_close;
        LinearLayout containerApotek;

        public apotekViewHolder(View v) {
            super(v);
            RatObat =v.findViewById(R.id.ratingBar);
            containerApotek=v.findViewById(R.id.containerApotek);
            tv_nama_apotek= (TextView) v.findViewById(R.id.tv_nama_apotek);
            tv_harga_obat_apotek=(TextView) v.findViewById(R.id.tv_harga_obat_apotek);
            tv_stok_obat_apotek=(TextView) v.findViewById(R.id.tv_stok_obat_apotek);
            tv_jarak= v.findViewById(R.id.tv_jarak);
            tv_jam_open = v.findViewById(R.id.tv_jam_open);
            tv_jam_close = v.findViewById(R.id.tv_jam_close);

        }
    }

}
