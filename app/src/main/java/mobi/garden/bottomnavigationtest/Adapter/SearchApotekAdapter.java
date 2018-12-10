package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import mobi.garden.bottomnavigationtest.Model.apotek;
import mobi.garden.bottomnavigationtest.R;

public class SearchApotekAdapter extends RecyclerView.Adapter<SearchApotekAdapter.SearchApotekViewHolder> {
    List<apotek> apoteks;
    Context context;
    double userLong;
    double userlat;

    public SearchApotekAdapter(List<apotek> apoteks, Context context, double userLong, double userlat) {
        this.apoteks = apoteks;
        this.context = context;
        this.userLong = userLong;
        this.userlat = userlat;
    }


    public SearchApotekAdapter(List<apotek> apoteks, Context context) {
        this.apoteks = apoteks;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchApotekAdapter.SearchApotekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cv_search_apotek,parent,false);
        return new SearchApotekAdapter.SearchApotekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchApotekAdapter.SearchApotekViewHolder holder, int position) {
        final apotek ap = apoteks.get(position);
        holder.tvnamaapotek.setText(ap.getNama_apotek());
        holder.tvJamOperasional.setText(ap.getOutletOprOpen()+"-"+ap.getOutletOprClose());
        holder.rbApotek.setRating((float)ap.getRatingbar());
        //holder.rbApotek.setRating((float)3.7888);
//        holder.llapotek.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Intent i = new Intent(context, )
//                //i.putExtra("ProductName",);
//            }
//        });


        Location loc1 = new Location("");
        loc1.setLatitude(userlat);
        loc1.setLongitude(userLong);

        Location loc2 = new Location("");
        loc2.setLatitude(ap.latitude);
        loc2.setLongitude(ap.longitude);
        double distanceInMeters = loc1.distanceTo(loc2)/1000;
        Log.d("Jarak", distanceInMeters+"");
        if(userLong != 0 && userlat !=0)
        {
            holder.tvJarak.setText(String.format("%.1f",distanceInMeters)+" KM");
        }
        else
        {
            holder.tvJarak.setText("0 KM");
        }

    }

    @Override
    public int getItemCount() {
        return apoteks.size();
    }

    public class SearchApotekViewHolder extends RecyclerView.ViewHolder{
        TextView tvnamaapotek, tvJamOperasional,tvJarak;
        LinearLayout llapotek;
        RatingBar rbApotek;
        public SearchApotekViewHolder(View itemView){
            super(itemView);
            tvnamaapotek = itemView.findViewById(R.id.tvnamaapotek);
            tvJamOperasional = itemView.findViewById(R.id.tvJamOperasional);
            tvJarak = itemView.findViewById(R.id.tvJarak);
            llapotek = itemView.findViewById(R.id.llApotek);
            rbApotek = itemView.findViewById(R.id.rbApotek);
        }
    }
}
