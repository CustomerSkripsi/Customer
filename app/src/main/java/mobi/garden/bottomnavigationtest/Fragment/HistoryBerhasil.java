package mobi.garden.bottomnavigationtest.Fragment;


import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import mobi.garden.bottomnavigationtest.Activity.LacakPesananDetail;
import mobi.garden.bottomnavigationtest.Adapter.LacakPesananAdapter;
import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.Lacak;
import mobi.garden.bottomnavigationtest.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryBerhasil extends Fragment {

    public static ArrayList<Lacak> lacakItem;
    private static RecyclerView recyclerView;
    private static LacakPesananAdapter lacakPesananAdapter;

    private static UserLocalStore userLocal;
    static User currUser;


    public static RequestQueue mQueue;
    static String link;


    public HistoryBerhasil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_berhasil, container, false);


        recyclerView = view.findViewById(R.id.rv_historyBerhasil);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        userLocal = new UserLocalStore(getContext());
        currUser = userLocal.getLoggedInUser();



        lacakItem = new ArrayList<>();

        link = "http://pharmanet.apodoc.id/select_lacak_berhasil_customer.php?CustomerID="+currUser.getUserID();
        mQueue = Volley.newRequestQueue(getContext());
        JSON();

        lacakPesananAdapter = new LacakPesananAdapter(lacakItem);
        return view;
    }


    public static void JSON()
    {
        Log.d("lnk", "JSON: "+link);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, link , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");

                    for (int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject result = jsonArray.getJSONObject(i);

                        Lacak L = new Lacak();
                        L.setOrderID(result.getString("OrderID"));
                        L.setTanggal(result.getString("OrderDate"));
                        L.setOutletName(result.getString("OutletName"));
                        L.setStatusOrder(result.getString("StatusOrderName"));
                        L.setStatusOrderID(result.getString("StatusOrderID"));
                        lacakItem.add(L);
                        recyclerView.setAdapter(lacakPesananAdapter);


                    }
//                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mQueue.add(request);

    }

}
