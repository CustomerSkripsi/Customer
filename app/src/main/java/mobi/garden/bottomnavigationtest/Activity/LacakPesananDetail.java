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

    TextView lacakPesananDetail_OrderID,lacakPesananDetail_Date,lacakPesananDetail_apoName,lacakPesananDetail_NamaObat,lacakPesananDetail_Harga;
    TextView lacakPesananDetail_Jumlah,lacakPesananDetail_Berat; //lacakPesananDetail_Status;
    TextView sub_total_detail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lacak_pesanan_detail);
        

        lacakPesananDetail_OrderID = findViewById(R.id.lacakPesananDetail_OrderID);
        lacakPesananDetail_Date = findViewById(R.id.lacakPesananDetail_Date);
        lacakPesananDetail_apoName = findViewById(R.id.lacakPesananDetail_apoName);
        lacakPesananDetail_NamaObat = findViewById(R.id.lacakPesananDetail_Nama_Item);
        lacakPesananDetail_Harga = findViewById(R.id.lacakPesananDetail_Price);
        lacakPesananDetail_Jumlah = findViewById(R.id.lacakPesananDetail_Jumlah);
        lacakPesananDetail_Berat = findViewById(R.id.lacakPesananDetail_Berat);
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
        //lacakPesananDetail_Status.setText(StatusOrder);

    }
}
