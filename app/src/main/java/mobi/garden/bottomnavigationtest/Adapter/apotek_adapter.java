package mobi.garden.bottomnavigationtest.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import mobi.garden.bottomnavigationtest.Activity.CartApotekActivity;
import mobi.garden.bottomnavigationtest.Activity.SearchApotek;
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

    public apotek_adapter(Context c, List<apotek> apoteklist) {
        this.apoteklist = apoteklist;
        this.context = c;
        session = new session_obat(c);
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

//        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        double longitude = location.getLongitude();
//        double latitude = location.getLatitude();
//
//        Location loc1 = new Location("");
//        loc1.setLatitude(latitude);
//        loc1.setLongitude(longitude);
//
//        Location loc2 = new Location("");
//        loc2.setLatitude(pr.latitude);
//        loc2.setLongitude(pr.longitude);
//
//        double distanceInMeters = loc1.distanceTo(loc2)/1000;
//
//        Log.d("Jarak", distanceInMeters+"");

//        android.support.v7.widget.Toolbar dToolbar = findViewById(R.id.toolbar4);
//        dToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
//        dToolbar.setTitle(kategoriname);

        holder.tv_nama_apotek.setText(pr.getNama_apotek());
        holder.tv_harga_obat_apotek.setText(df.format(pr.getHarga())+"");
        holder.tv_stok_obat_apotek.setText(String.valueOf(pr.getStok()));
        holder.tv_jarak.setText(100+"");
        holder.RatObat.setRating(pr.getRating());
        holder.RatObat.setEnabled(false);
        holder.tv_jam_open.setText(pr.getOutletOprOpen());
        holder.tv_jam_close.setText(pr.getOutletOprClose());
        holder.containerApotek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,CartApotekActivity.class);
                i.putExtra("ApotekName", pr.getNama_apotek());
                Log.d( "ssssssss",pr.getNama_apotek());
                context.startActivity(i);
            }
        });


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
            //containerApotek = v.findViewById(R.id.containerApotek);

        }
    }

}
