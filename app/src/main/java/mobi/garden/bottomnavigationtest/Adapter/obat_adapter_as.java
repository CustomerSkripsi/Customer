package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.CartApotekActivity;
import mobi.garden.bottomnavigationtest.CONFIG;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class obat_adapter_as extends RecyclerView.Adapter<obat_adapter_as.obatViewHolder> {

    List<obat> obatlist;
    List<obat> cartlist;
    Context context;//String CustomerID;
    static DecimalFormat df;
    public static String add_url = "http://pharmanet.apodoc.id/customer/addCartCustomer.php";

    //login
    SessionManagement session;
    HashMap<String, String> login;
    public static String CustomerID,memberID, userName;

//    UserLocalStore userLocalStore;
//    public obat_adapter_as(Context c, List<obat> obatlist, List<obat> cartlist) {
//        this.obatlist = obatlist;
//        this.context = c;
//        this.cartlist = cartlist;
//    }

    public obat_adapter_as( List<obat> obatlist , Context context) {
        this.obatlist = obatlist;
        this.context = context;
        this.memberID = CustomerID;
        session = new SessionManagement(context);
    }
    public void setProductList(List<obat> obatlist) {
        this.obatlist = obatlist;
    }


    @Override
    public obat_adapter_as.obatViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from
                (viewGroup.getContext()).inflate(R.layout.cv_obat_as, viewGroup, false);
        return new obat_adapter_as.obatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(obat_adapter_as.obatViewHolder holder, int position) {
        final obat pr = obatlist.get(position);
//        userLocalStore  = new UserLocalStore(context);
//        User currUser = userLocalStore.getLoggedInUser();
//        CustomerID = currUser.getUserID();

        session = new SessionManagement(context);
        login = session.getMemberDetails();
        userName= login.get(SessionManagement.USERNAME);
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);


        df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp. ");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);

        Picasso.with(context).load(pr.productPhoto).into(holder.iv_picture_obat_as);
        holder.tv_nama_obat_as.setText(pr.getProductName());
        holder.tv_qty_obat_as.setText(String.valueOf(pr.getOutletProductStockQty()+" pcs"));
        holder.tv_price_obat_as.setText(String.valueOf(df.format(pr.getOutletProductPrice())));
        holder.iv_picture_obat_as.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(context,InformasiObatAs.class);
//                i.putExtra(InformasiObatAs.EXTRA_OBAT, pr);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
            }
        });
//        holder.btn_add_obat.setEnabled(true);
//        holder.btn_add_obat.setBackgroundResource(R.drawable.btn_unclicked_home);
//        for(int j=0;j<cartlist.size();j++){
//            if(pr.productID.equals(cartlist.get(j).productID)){
//                holder.btn_add_obat.setEnabled(false);
//                holder.btn_add_obat.setBackgroundResource(R.drawable.add_button_set_enabled);
//                break;
//            }
//        }

        holder.btn_add_obat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(pr.productID, pr.outletProductPrice,1, memberID);
                holder.btn_add_obat.setEnabled(false);
                holder.btn_add_obat.setBackgroundResource(R.drawable.add_button_set_enabled);
                Toast.makeText(context, ""+pr.productID, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "testclick", Toast.LENGTH_SHORT).show();

                Intent data = new Intent();
                String text = "test123123";
                data.putExtra(CONFIG.PREV_PAGE,text);
            }
        });

    }

    @Override
    public int getItemCount() {
        return obatlist.size();
    }

    public static class obatViewHolder extends RecyclerView.ViewHolder {
        int i=0;
        ImageView iv_picture_obat_as;
        LinearLayout ll_cv_obat_as;
        TextView tv_nama_obat_as, tv_qty_obat_as, tv_price_obat_as;
        Button btn_add_obat;
        public obatViewHolder(View v) {
            super(v);
            iv_picture_obat_as=(ImageView) v.findViewById(R.id.iv_picture_obat_as);
            ll_cv_obat_as = (LinearLayout) v.findViewById(R.id.ll_cv_obat_as);
            tv_nama_obat_as= (TextView) v.findViewById(R.id.tv_nama_obat_as);
            btn_add_obat=(Button)v.findViewById(R.id.btn_add_obat);
            tv_qty_obat_as = (TextView) v.findViewById(R.id.tv_qty_obat_as);
            tv_price_obat_as =(TextView) v.findViewById(R.id.tv_price_obat_as);
        }
    }


    public void add(String product_id, int product_price, int qty, String memberID) {
        Log.d("dsa", memberID);
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("ProductID", product_id);
//            objDetail.put("ProductName", product_name);
            objDetail.put("outletProductPrice", product_price);
            objDetail.put("Qty", qty);
            objDetail.put("CustomerID", memberID);
            objDetail.put("UpdatedBy",memberID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
//        Log.d("testtest1", objAdd.toString());
        Toast.makeText(context, "poipoi", Toast.LENGTH_SHORT).show();
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, add_url, objAdd,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(context, "asdqwe", Toast.LENGTH_SHORT).show();
                        try {
                            if (response.getString("status").equals("OK")) {
                                //CartApotekActivity.initiateBelowAdapter();
                                CartApotekActivity.show_cart(CartApotekActivity.urlbawahs,memberID);
//                                Toast.makeText(context, "obatadapterberhasil", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
//    public void updateList(ArrayList<obat> list){
//        obatlist = new ArrayList<>();
//        obatlist.addAll(list);
//        notifyDataSetChanged();
//    }
}
