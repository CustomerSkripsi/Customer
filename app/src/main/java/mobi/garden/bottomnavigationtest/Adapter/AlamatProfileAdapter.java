package mobi.garden.bottomnavigationtest.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import mobi.garden.bottomnavigationtest.Activity.AlamatProfile;
import mobi.garden.bottomnavigationtest.Activity.ProfileCustomerActivity;
import mobi.garden.bottomnavigationtest.Activity.UbahAlamatActivity;
import mobi.garden.bottomnavigationtest.LoginRegister.Register;
import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.Lacak;
import mobi.garden.bottomnavigationtest.R;

public class AlamatProfileAdapter extends RecyclerView.Adapter<AlamatProfileAdapter.AlamatProfileViewHolder> {

    List<User> mUserList;
    Context context;
    private UserLocalStore userLocal;
    private static User currUser;
    String customerID;
    Dialog dialog;
    User user;
    TextView tvYa,tvTidak;

    public AlamatProfileAdapter(List<User> userList, Context context, String customerID){
        this.mUserList = userList;
        this.context = context;
        this.customerID = customerID;
        userLocal = new UserLocalStore(context);
        currUser = userLocal.getLoggedInUser();
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
        user = mUserList.get(position);

        holder.tvRecipientName.setText(user.getRecipientName());
        holder.tvRecipientNumber.setText(user.getRecipientNumber() + "");
        holder.tvCustomerAddress.setText(user.getCustomerAddress());
        holder.tvCustomerCity.setText(user.getCustomerCity()+",");
        holder.tvCustomerPostalCode.setText(user.getCustomerPostalCode() + "");
        holder.tvCustomerProvince.setText(user.getCustomerProvince());

        holder.TvUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UbahAlamatActivity.class);

                i.putExtra("LocationID", user.getCustomerLocationID());
                i.putExtra("NamaPenerima", user.getRecipientName());
                i.putExtra("TeleponPenerima", user.getRecipientNumber());
                i.putExtra("AlamatPenerima", user.getCustomerAddress());
                i.putExtra("KotaPenerima", user.getCustomerCity());
                i.putExtra("KodeposPenerima", user.getCustomerPostalCode());
                i.putExtra("ProvinsiPenerima", user.getCustomerProvince());

                v.getContext().startActivity(i);
            }
        });

        holder.TvHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mydialog(context);
            }
        });
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

    public void delete(String customerLocationID){
        //Toast.makeText(this,"masuk",Toast.LENGTH_SHORT).show();
        String url = "http://pharmanet.apodoc.id/delete_alamat_customer.php";
        JSONObject objdelete= new JSONObject();

        try{
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("CustomerID", currUser.getUserID());
            objDetail.put("CustomerLocationID", customerLocationID);
            arrData.put(objDetail);
            objdelete.put("data", arrData);
            Log.d("delete", String.valueOf(objdelete));
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest= new JsonObjectRequest(Request.Method.POST, url, objdelete, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //  etxt1.setText(response.getString("status"));

                    Log.d("response",response.toString());
                    if (response.getString("status").equals("OK")) {
                        Toast.makeText(context, "berhasil", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "gagal", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage()+"", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("error_response_register",error.getMessage());
                    }

                }

        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void Mydialog(Context context){

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_yakinatautidak);


        tvYa = dialog.findViewById(R.id.tvya);
        tvTidak = dialog.findViewById(R.id.tvtidak);

        tvYa.setOnClickListener((View v) -> {
            delete(user.getCustomerLocationID());
            AlamatProfile.getAlamat();
            dialog.dismiss();
        });

        tvTidak.setOnClickListener(v -> dialog.cancel());
        dialog.show();
    }
}
