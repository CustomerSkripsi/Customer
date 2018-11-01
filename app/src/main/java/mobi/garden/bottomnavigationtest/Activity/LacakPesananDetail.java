package mobi.garden.bottomnavigationtest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.R;

public class LacakPesananDetail extends AppCompatActivity{

    static String ID;
    String Date,OutletName,StatusOrder;

    TextView lacakPesananDetail_OrderID,lacakPesananDetail_Date,lacakPesananDetail_apoName,lacakPesananDetail_NamaPenerima,lacakPesananDetail_Phone;
    TextView lacakPesananDetail_AlamatPenerima,lacakPesananDetail_StatusDelivery,lacakPesananDetail_Status,lacakPesananDetail_TglTerima,lacakPesananDetail_Alasan;
    TextView sub_total_detail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lacak_pesanan_detail);

        lacakPesananDetail_OrderID = findViewById(R.id.lacakPesananDetail_OrderID);
        lacakPesananDetail_Date = findViewById(R.id.lacakPesananDetail_Date);
        lacakPesananDetail_apoName = findViewById(R.id.lacakPesananDetail_apoName);
        lacakPesananDetail_NamaPenerima = findViewById(R.id.lacakPesananDetail_NamaPenerima);
        lacakPesananDetail_Phone = findViewById(R.id.lacakPesananDetail_Phone);
        lacakPesananDetail_AlamatPenerima = findViewById(R.id.lacakPesananDetail_AlamatPenerima);
        lacakPesananDetail_StatusDelivery = findViewById(R.id.lacakPesananDetail_StatusDelivery);
        lacakPesananDetail_Status = findViewById(R.id.lacakPesananDetail_Status);
        lacakPesananDetail_TglTerima = findViewById(R.id.lacakPesananDetail_TglTerima);
        sub_total_detail = findViewById(R.id.sub_total_detail);


        //parse data intent
        ID = getIntent().getStringExtra("OrderID");
        Date = getIntent().getStringExtra("Tanggal");
        OutletName = getIntent().getStringExtra("OutletName");
        StatusOrder = getIntent().getStringExtra("StatusOrder");

        //Masukan data Parse
        lacakPesananDetail_OrderID.setText(ID);
        lacakPesananDetail_Date.setText(Date);
        lacakPesananDetail_apoName.setText(OutletName);
        lacakPesananDetail_Status.setText(StatusOrder);

    }
}
