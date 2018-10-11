package mobi.garden.bottomnavigationtest.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import mobi.garden.bottomnavigationtest.Adapter.AdapterSearch_Obat;
import mobi.garden.bottomnavigationtest.Model.Model_Obat;
import mobi.garden.bottomnavigationtest.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_obat extends Fragment {
    public Activity activity;
    public LayoutInflater inflater;
    final static String URL_SEARCH_OBAT = "http://pharmanet.apodoc.id/select_search_obat.php?keyword=";
    String input;
    private RecyclerView rvObat;
    AdapterSearch_Obat adapterSearch;
    List<Model_Obat> modelObats = new ArrayList<>();
    @SuppressLint("ValidFragment")
    public fragment_obat(String input) {
        this.input = input;
    }



    public fragment_obat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_obat, container, false);

        if(input!=null) {
            rvObat = view.findViewById(R.id.listitem_obat);
            rvObat.setHasFixedSize(true);
            rvObat.setLayoutManager(new LinearLayoutManager(getContext()));
            JsonObjectRequest req = new JsonObjectRequest(URL_SEARCH_OBAT + input, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray Obats = null;
                            try {
                                Obats = response.getJSONArray("result");
                                Log.d("raa",Obats.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(Obats.length()!=0){
                                for (int i = 0; i < Obats.length(); i++) {
                                    try {
                                        JSONObject obat = Obats.getJSONObject(i);
                                        modelObats.add(new Model_Obat(obat.getString("ProductID")
                                                ,obat.getString("ProductName"),
                                                obat.getString("ProductImage"),
                                                obat.getString("ProductDescription"),
                                                obat.getString("ProductIndicationUsage"),
                                                obat.getString("ProductIngredients"),
                                                obat.getString("ProductDosage"),
                                                obat.getString("ProductHowToUse"),
                                                obat.getString("ProductPackage"),
                                                obat.getString("ProductClassification"),
                                                obat.getString("ProductRecipe"),
                                                obat.getString("ProductContraindication"),
                                                obat.getString("ProductStorage"),
                                                obat.getString("PrincipalName"),
                                                obat.getString("CategoryID")
                                                ));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapterSearch = new AdapterSearch_Obat(getActivity());
                                adapterSearch.setModelObatList(modelObats);
                                rvObat.setAdapter(adapterSearch);
                            }else {
                                Log.d("tsb1","keluar");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("errornya",error.getMessage());
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(req);
        }

        return view;


    }
}