package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.ModelPromo;
import mobi.garden.bottomnavigationtest.Model.cart;
import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Session.SessionManagement;

public class PickUpActivity extends AppCompatActivity {

    static final String OUTLET_ID = "OutletID";

    //static final String CUSTOMER_ID = "CustomerID";

    static List<cart> cartlist = new ArrayList<>();

    public static Context context;

    String OutletID;
    ImageView iv_barcode,iv_barcode_member;
    Button BRegister,btnBack;
    static TextView tv_apotek_name,tv_apotek_address,tv_apotek_noTlp,tv_apotek_pic,tv_apotek_pic_noTlp;
    String urlApotek = "Http://Pharmanet.Apodoc.id/select_detail_transaction.php?id=";
    UserLocalStore userLocalStore;
    LinearLayout containerDaftar;
    public static String CustomerID,memberID, userName;
    TextView tvApotek, etNopesanan;

    SessionManagement session;
    HashMap<String, String> login;
    List<ModelPromo> CartModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);
        Intent intent = getIntent();
        OutletID = intent.getStringExtra(OUTLET_ID);
        context=getApplicationContext();

//        userLocalStore = new UserLocalStore(this);
//        User currUser = userLocalStore.getLoggedInUser();
//        CustomerID = currUser.getUserID();
//        if(userLocalStore.getUserLoggedIn()){
//            //set visibility container gone
//            containerDaftar.setVisibility(containerDaftar.GONE);
//        }


        session = new SessionManagement(getApplicationContext());
        login = session.getMemberDetails();
        userName= login.get(SessionManagement.USERNAME);
        memberID = login.get(SessionManagement.KEY_KODEMEMBER);

        containerDaftar = (LinearLayout) findViewById(R.id.containerDaftar);

        String barcode_data = "123456";
        String barcode_member = "444111444";

        // barcode image
        Bitmap bitmap = null;
        btnBack = (Button) findViewById(R.id.btnBack);

        BRegister = (Button) findViewById(R.id.BRegister);
        tv_apotek_name = (TextView) findViewById(R.id.tv_apotek_name);
        tv_apotek_address = (TextView) findViewById(R.id.tv_apotek_address);
        tv_apotek_noTlp = (TextView) findViewById(R.id.tv_apotek_noTlp);
        tv_apotek_pic = (TextView) findViewById(R.id.tv_apotek_pic);
        tv_apotek_pic_noTlp = (TextView) findViewById(R.id.tv_apotek_pic_noTlp);

        //barcode
        //        iv_barcode = (ImageView) findViewById(R.id.iv_barcode);
//        iv_barcode_member = (ImageView) findViewById(R.id.iv_barcode_member);
//        try {
//            bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
//            iv_barcode.setImageBitmap(bitmap);
//        } catch (WriterException e) {
//            e.printStackTrace(); }
//        try {
//            bitmap = encodeAsBitmap(barcode_member, BarcodeFormat.CODE_128, 600, 300);
//            iv_barcode_member.setImageBitmap(bitmap);
//        } catch (WriterException e) {
//            e.printStackTrace(); }

        //show_view(CustomerID);
        Log.d("customerID",CustomerID+"");
        Toast.makeText(this, CustomerID+"", Toast.LENGTH_SHORT).show();
        //show_view(urlApotek,memberID);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PickUpActivity.this, HomeActivity.class));
            }
        });


       // CartActivity.refresh_total_cart(CartModel);
        tvApotek = findViewById(R.id.tvApotek);
        etNopesanan = findViewById(R.id.etNopesanan);
        viewdetail();
        setStatusBarGradiant(this);
    }

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    public void viewdetail(){
        String url="http://pharmanet.apodoc.id/customer/showNoPesanan.php?CustomerID=";
        JsonObjectRequest req = new JsonObjectRequest(url+memberID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray result = null;
                try {
                    //Toast.makeText(DetailKategori.this, "aaar", Toast.LENGTH_SHORT).show();
                    result = response.getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i=0; i< result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        tv_apotek_name.setText(object.getString("OutletName"));
                        etNopesanan.setText(object.getString("OrderID"));
                        tvApotek.setText("Kami tunggu kehadiran Anda di Apotek "+object.getString("OutletName"));
                        tv_apotek_address.setText(object.getString("OutletAddress"));
                        Log.d("rwar", object.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PickUpActivity.this, "Sedang gangguan", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(PickUpActivity.this);
        queue.add(req);
    }

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    public static void show_view(String urlbawah, String CustomerID) {
        JSONObject objAdd = new JSONObject();
        try {
            JSONArray arrData = new JSONArray();
            JSONObject objDetail = new JSONObject();
            objDetail.put("CustomerID", CustomerID);
            arrData.put(objDetail);
            objAdd.put("data", arrData);
            Log.d("ini showPickUp", objAdd.toString());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest rec = new JsonObjectRequest(urlbawah, objAdd, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray products = null;
                try {
                    products = response.getJSONArray("result");
                    cartlist.clear();
                    for (int i = 0; i < products.length(); i++) {
                        try {
                            JSONObject obj = products.getJSONObject(i);
                            cartlist.add(new cart(
                                    obj.getString("OutletName"),
                                    obj.getString("OutletAddress"),
                                    obj.getString("OutletPhone"),
                                    obj.getString("UserName"),
                                    obj.getString("UserPhone")
                            ));
                            tv_apotek_name.setText(obj.getString("OutletName"));
                            tv_apotek_address.setText(obj.getString("OutletAddress"));
                            tv_apotek_noTlp.setText(obj.getString("OutletPhone"));
                            tv_apotek_pic.setText(obj.getString("UserName"));
                            tv_apotek_pic_noTlp.setText(obj.getString("UserPhone"));

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Toast.makeText(context, e1.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Main2Activity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(rec);
    }

    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

}
