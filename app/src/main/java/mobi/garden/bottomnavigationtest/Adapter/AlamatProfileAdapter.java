package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import mobi.garden.bottomnavigationtest.Activity.ProfileCustomerActivity;
import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.Model.Lacak;
import mobi.garden.bottomnavigationtest.R;

public class AlamatProfileAdapter extends RecyclerView.Adapter<AlamatProfileAdapter.AlamatProfileViewHolder> {

    List<User> mUserList;
    Context context;

    public AlamatProfileAdapter(List<User> userList, Context context){
        this.mUserList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlamatProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daftaralamat_list,parent, false);
        AlamatProfileViewHolder alamatProfileViewHolder = new AlamatProfileViewHolder(view);
        return alamatProfileViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlamatProfileViewHolder holder, int position) {
        final User user = mUserList.get(position);

        holder.tvRecipientName.setText(user.getRecipientName());
        holder.tvRecipientNumber.setText(user.getRecipientNumber() + "");
        holder.tvCustomerAddress.setText(user.getCustomerAddress());
        holder.tvCustomerCity.setText(user.getCustomerCity()+",");
        holder.tvCustomerPostalCode.setText(user.getCustomerPostalCode() + "");
        holder.tvCustomerProvince.setText(user.getCustomerProvince());

//        holder.TvUbah.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//
//        holder.TvHapus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
            return mUserList.size();

    }

    public class AlamatProfileViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipientName, tvRecipientNumber, tvCustomerAddress, tvCustomerPostalCode, tvCustomerCity, tvCustomerProvince, TvUbah, TvHapus;
        public AlamatProfileViewHolder(View itemView) {
            super(itemView);
            tvRecipientName = itemView.findViewById(R.id.tvNamaPenerima);
            tvRecipientNumber= itemView.findViewById(R.id.tvTeleponPenerima);
            tvCustomerAddress = itemView.findViewById(R.id.tvAlamatPenerima);
            tvCustomerCity = itemView.findViewById(R.id.tvKotaPenerima);
            tvCustomerPostalCode = itemView.findViewById(R.id.tvKodePosPenerima);
            tvCustomerProvince = itemView.findViewById(R.id.tvProvinsiPenerima);
            TvUbah = itemView.findViewById(R.id.tvUbahAlamat);
            TvHapus = itemView.findViewById(R.id.tvHapusAlamat);
        }
    }

}
