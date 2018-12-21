package mobi.garden.bottomnavigationtest.Model;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import mobi.garden.bottomnavigationtest.Adapter.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Searching {
    String input;
    Context context;
    RecyclerView recyclerView;
    String url, cityList;
    SearchAdapter searchAdapter;
    List<CityItem> cityItemList = new ArrayList<>();
    RequestQueue queue;

    public Searching(Context context,String input, RecyclerView recyclerView) {
        this.input = input;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public void search(){
        cityItemList.clear();
        queue = Volley.newRequestQueue(context);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        getData();
    }

    public void getData(){
        url = "http://sayasehat.apodoc.id/listKota.php";

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
//                    Log.d("resultnya",response.getJSONArray("result").length()+"");
                    result = response.getJSONArray("result");
                } catch (JSONException e) {
                    Toast.makeText(context, "Result Error (JSONArray)", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                for (int i = 0; i<result.length(); i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        cityList = object.getString("Cty_Name");
                        cityItemList.add(new CityItem(cityList));
//                        Toast.makeText(context, ""+ pONumber, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        Toast.makeText(context, "Error JSON Object", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                searchAdapter = new SearchAdapter(cityItemList, context);
                recyclerView.setAdapter(searchAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "1"+error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}


