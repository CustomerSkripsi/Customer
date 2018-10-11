package mobi.garden.bottomnavigationtest.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.ApotekActivity;
import mobi.garden.bottomnavigationtest.Activity.CartApotekActivity;
import mobi.garden.bottomnavigationtest.LoginRegister.Login;
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

    Button button_lanjut, button_Batal;
    Dialog myDialog;

    public apotek_adapter(Context c, List<apotek> apoteklist) {
        this.apoteklist = apoteklist;
        this.context = c;
        session = new session_obat(c);
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
        userLocalStore  = new UserLocalStore(context);
        User currUser = userLocalStore.getLoggedInUser();
        CustomerID = currUser.getUserID();

        df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);

        holder.tv_nama_apotek.setText(pr.getNama_apotek());
        holder.tv_harga_obat_apotek.setText(df.format(pr.getHarga())+"");
        holder.tv_stok_obat_apotek.setText(String.valueOf(pr.getStok()));
        holder.tv_keterangan_apotek.setText("Min. "+df.format(Integer.parseInt(pr.getKeterangan())));

        holder.btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userLocalStore.getUserLoggedIn()){
                    Intent i = new Intent(context,CartApotekActivity.class);
                    i.putExtra(CartApotekActivity.OUTLET_ID, pr.id_apotek);
                    i.putExtra(CartApotekActivity.CATEGORY_ID, session.getUserDetails().get(session_obat.CATEGORYID));
                    i.putExtra(CartApotekActivity.PRODUCT_ID, session.getUserDetails().get(session_obat.ID));
                    i.putExtra(CartApotekActivity.OUTLET_NAME, pr.nama_apotek);

                    add(session.getUserDetails().get(session_obat.ID),session.getUserDetails().get(session_obat.NAMA)
                            , pr.getHarga(),1, Integer.parseInt(CustomerID), pr.id_apotek);


                    context.startActivity(i);
                }
                else {
                    Intent i = new Intent(context,Login.class);
                    context.startActivity(i);
                }

//                outletid[0] = pr.id_apotek;
//
//                if(outletid[0] != pr.id_apotek){
//                    myDialog = new Dialog(context);
//                    myDialog.setContentView(R.layout.dialog_layout2);
//
//                    button_lanjut = (Button) myDialog.findViewById(R.id.button_lanjut);
//                    button_Batal = (Button) myDialog.findViewById(R.id.button_batal);
//
//                    button_Batal.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            myDialog.cancel();
//                        }
//                    });
//
//                    button_lanjut.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent i=new Intent(context,CartApotekActivity.class);
//                            i.putExtra(CartApotekActivity.OUTLET_ID, pr.id_apotek);
//                            i.putExtra(CartApotekActivity.CATEGORY_ID, session.getUserDetails().get(session_obat.CATEGORYID));
//                            i.putExtra(CartApotekActivity.PRODUCT_ID, session.getUserDetails().get(session_obat.ID));
//                            i.putExtra(CartApotekActivity.OUTLET_NAME, pr.nama_apotek);
//
//                            add(session.getUserDetails().get(session_obat.ID),session.getUserDetails().get(session_obat.NAMA)
//                                    , pr.getHarga(),1, Integer.parseInt(CustomerID), pr.id_apotek);
//
//
//                            context.startActivity(i);
//                        }
//                    });
//                    myDialog.show();
//
//                    outletid[0] = pr.id_apotek;
//                }
//                else{
//                    Intent i=new Intent(context,CartApotekActivity.class);
//                    i.putExtra(CartApotekActivity.OUTLET_ID, pr.id_apotek);
//                    i.putExtra(CartApotekActivity.CATEGORY_ID, session.getUserDetails().get(session_obat.CATEGORYID));
//                    i.putExtra(CartApotekActivity.PRODUCT_ID, session.getUserDetails().get(session_obat.ID));
//                    i.putExtra(CartApotekActivity.OUTLET_NAME, pr.nama_apotek);
//
//                    add(session.getUserDetails().get(session_obat.ID),session.getUserDetails().get(session_obat.NAMA)
//                            , pr.getHarga(),1, Integer.parseInt(CustomerID), pr.id_apotek);
//
//
//                    context.startActivity(i);
//
//                    outletid[0] = pr.id_apotek;
//                }

            }
        });

        holder.containerApotek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ApotekActivity.class);
                intent.putExtra(ApotekActivity.OUTLET_ID,pr.id_apotek);
                context.startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apoteklist.size();
    }

    public static class apotekViewHolder extends RecyclerView.ViewHolder {

        TextView tv_nama_apotek,tv_harga_obat_apotek,tv_stok_obat_apotek,tv_keterangan_apotek;
        LinearLayout containerApotek;
        Button btn_beli;

        public apotekViewHolder(View v) {
            super(v);

            containerApotek=v.findViewById(R.id.containerApotek);
            tv_nama_apotek= (TextView) v.findViewById(R.id.tv_nama_apotek);
            tv_harga_obat_apotek=(TextView) v.findViewById(R.id.tv_harga_obat_apotek);
            tv_stok_obat_apotek=(TextView) v.findViewById(R.id.tv_stok_obat_apotek);
            tv_keterangan_apotek= (TextView) v.findViewById(R.id.tv_keterangan_apotek);
            btn_beli = (Button)v.findViewById(R.id.btn_beli);
        }
    }
    public void add(String product_id, String product_name, int product_price, int qty, int CustomerID , String outletID) {
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductName", product_name);
            objDetail.put("Price", product_price);
            objDetail.put("Qty", qty);
            objDetail.put("CustomerID", CustomerID);
            objDetail.put("UpdatedBy",CustomerID);
            objDetail.put("ProductID", product_id);
            objDetail.put("OutletID", outletID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://pharmanet.apodoc.id/addCartCustomer_apotek.php", objAdd,
                new Response.Listener<JSONObject>() {
                    @Override

                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("OK")) {
                                CartApotekActivity.initiateBelowAdapter();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
