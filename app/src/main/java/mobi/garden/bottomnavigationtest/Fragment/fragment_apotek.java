package mobi.garden.bottomnavigationtest.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mobi.garden.bottomnavigationtest.Adapter.AdapterSearch_Apotek;
import mobi.garden.bottomnavigationtest.Model.Model_Apotek;
import mobi.garden.bottomnavigationtest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_apotek extends Fragment {
    static final String OUTLET_ID = "OutletID";
    String OutletID;
    String urlApotek = "Http://Pharmanet.Apodoc.id/select_apotek_online.php?id=";
    TextView tv_name_apotek,tv_address_apotek,tv_no_telepon,tv_jam_operasional,tv_no_sia,tv_no_sipa,tv_metode_pengiriman;


    final static String URL_SEARCH_APOTEK = "http://pharmanet.apodoc.id/select_search_apotek.php?keyword=";
    String input;
    AdapterSearch_Apotek adapterSearch;
    List<Model_Apotek> modelApoteks = new ArrayList<>();
    RecyclerView rvApotek;
    Context context;



    public fragment_apotek() {
    }

    @SuppressLint("ValidFragment")
    public fragment_apotek(String input) {
        this.input = input;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_fragment_apotek, container, false);
        if(input!=null) {
            rvApotek = view.findViewById(R.id.listitem_apotek);
            rvApotek.setHasFixedSize(true);
            rvApotek.setLayoutManager(new LinearLayoutManager(getContext()));
            JsonObjectRequest req = new JsonObjectRequest(URL_SEARCH_APOTEK + input, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray Apoteks = null;
                            try {
                                Apoteks = response.getJSONArray("result");
                                Log.d("resultnyaaa",Apoteks.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(Apoteks.length()!=0){
                                for (int i = 0; i < Apoteks.length(); i++) {
                                    try {
                                        JSONObject apotek = Apoteks.getJSONObject(i);
                                        modelApoteks.add(new Model_Apotek(apotek.getString("OutletID"),
                                                apotek.getString("OutletName"),
                                                apotek.getString("OutletAddress"),
                                        apotek.getString("OutletPhone"),
                                        apotek.getString("OutletSIA"),
                                        apotek.getString("OutletSIPA")
                                        ));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapterSearch = new AdapterSearch_Apotek(getActivity());
                                adapterSearch.setModelApotekList(modelApoteks);
                                rvApotek.setAdapter(adapterSearch);
                            }else {
                                Log.d("tsb1","keluar");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(getActivity(), "Terjadi Kendala Koneksi", Toast.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(req);
        }
    show_view(OutletID);
        return view;
    }
    public void show_view(String OutletID){
        final JsonObjectRequest rec= new JsonObjectRequest(urlApotek+OutletID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray apoteks = response.getJSONArray("result");
                    JSONObject apotek = apoteks.getJSONObject(0);
                    tv_name_apotek.setText(apotek.getString("OutletName"));
                    tv_address_apotek.setText(apotek.getString("OutletAddress"));
                    tv_no_telepon.setText(apotek.getString("OutletPhone"));
                    tv_jam_operasional.setText(apotek.getString("OutletPhone"));
                    tv_no_sia.setText(apotek.getString("OutletSIA"));
                    tv_no_sipa.setText(apotek.getString("OutletSIPA"));
                    tv_metode_pengiriman.setText(apotek.getString("deliveryYN"));

                    //Picasso.with(ApotekActivity.this).load("http://apotekkeluarga.com/wp-content/uploads/2015/06/apotik-terlengkap-di-pekanbaru.jpg").into(iv_picture_apotek);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //              Toast.makeText(ApotekActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(rec);
    }
}


