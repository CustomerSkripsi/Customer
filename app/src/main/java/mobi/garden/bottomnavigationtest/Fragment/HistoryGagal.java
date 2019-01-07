package mobi.garden.bottomnavigationtest.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.HashMap;

import mobi.garden.bottomnavigationtest.Adapter.LacakPesananAdapter;
import mobi.garden.bottomnavigationtest.Model.Lacak;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryGagal extends Fragment {
    public static ArrayList<Lacak> lacakItem;
    private static RecyclerView recyclerView;
    private static LacakPesananAdapter lacakPesananAdapter;
    public static Context context;

//    private static UserLocalStore userLocal;
//    static User currUser;
    SessionManagement session;
    HashMap<String, String> login;
    public static String CustomerID,memberID, userName;



    public static RequestQueue mQueue;
    static String link;

    public HistoryGagal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_gagal, container, false);

            session = new SessionManagement(getContext());
            login = session.getMemberDetails();
            userName= login.get(SessionManagement.USERNAME);
            memberID = login.get(SessionManagement.KEY_KODEMEMBER);

//        Toast.makeText(context, memberID, Toast.LENGTH_SHORT).show();

        recyclerView = view.findViewById(R.id.rv_historyGagal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

//        userLocal = new UserLocalStore(getContext());
//        currUser = userLocal.getLoggedInUser();

        lacakItem = new ArrayList<>();

        link = "http://pharmanet.apodoc.id/select_lacak_gagal_customer.php?CustomerID=";
        mQueue = Volley.newRequestQueue(getContext());
        JSON();

        lacakPesananAdapter = new LacakPesananAdapter(lacakItem);
        return view;
    }


    public static void JSON()
    {
        Log.d("lnk", "JSON: "+link);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, link+memberID , null, new Response.Listener<JSONObject>() {
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
