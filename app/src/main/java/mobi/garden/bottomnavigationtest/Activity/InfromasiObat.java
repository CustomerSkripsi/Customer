package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import mobi.garden.bottomnavigationtest.BaseActivity;
import mobi.garden.bottomnavigationtest.Model.session_obat;
import mobi.garden.bottomnavigationtest.R;

/**
 * Created by ASUS on 5/9/2018.
 */

public class InfromasiObat extends BaseActivity {

    ImageView iv_picture_obat3;
    TextView tv_nama_obat3, tv_indikasi_obat,tv_kandungan_obat,tv_dosis_obat,tv_carapakai_obat,
            tv_kemasan_obat,tv_golongan_obat,tv_resepYN_obat,tv_kontraindikasi_obat,tv_carasimpan_obat,tv_principal_obat;

    mobi.garden.bottomnavigationtest.Model.session_obat session_obat;
    HashMap<String,String> detail_obat;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        session_obat = new session_obat(getApplicationContext());
        detail_obat = session_obat.getUserDetails();

        iv_picture_obat3=(ImageView) findViewById(R.id.iv_picture_obat3);
        tv_nama_obat3=(TextView) findViewById(R.id.tv_nama_obat3);
        tv_indikasi_obat= (TextView) findViewById(R.id.tv_indikasi_obat);
        tv_kandungan_obat= (TextView) findViewById(R.id.tv_kandungan_obat);
        tv_dosis_obat= (TextView) findViewById(R.id.tv_dosis_obat);
        tv_carapakai_obat= (TextView) findViewById(R.id.tv_carapakai_obat);
        tv_kemasan_obat= (TextView) findViewById(R.id.tv_kamasan_obat);
        tv_golongan_obat= (TextView) findViewById(R.id.tv_golongan_obat);
        tv_resepYN_obat= (TextView) findViewById(R.id.tv_resepYN_obat);
        tv_kontraindikasi_obat= (TextView) findViewById(R.id.tv_kontraindikasi_obat);
        tv_carasimpan_obat= (TextView) findViewById(R.id.tv_carasimpan_obat);
        tv_principal_obat= (TextView) findViewById(R.id.tv_principal_obat);

        setStatusBarGradiant(this);

        Picasso.with(InfromasiObat.this).load(detail_obat.get(session_obat.PHOTO)).into(iv_picture_obat3);
        tv_nama_obat3.setText(detail_obat.get(session_obat.NAMA));
        tv_indikasi_obat.setText(detail_obat.get(session_obat.INDIKASI));
        tv_kandungan_obat.setText(detail_obat.get(session_obat.KANDUNGAN));
        tv_dosis_obat.setText(detail_obat.get(session_obat.DOSIS));
        tv_carapakai_obat.setText(detail_obat.get(session_obat.CARAPAKAI));
        tv_kemasan_obat.setText(detail_obat.get(session_obat.KEMASAN));
        tv_golongan_obat.setText(detail_obat.get(session_obat.GOLONGAN));
        tv_resepYN_obat.setText(detail_obat.get(session_obat.RESEPYN));
        tv_kontraindikasi_obat.setText(detail_obat.get(session_obat.KONTRAINDIKASI));
        tv_carasimpan_obat.setText(detail_obat.get(session_obat.CARASIMPAN));
        tv_principal_obat.setText(detail_obat.get(session_obat.PRINCIPAL));


    }

    public int getContentViewId() {

        return R.layout.informasi_obat;
    }

    public  int getNavigationMenuItemId() {

        return R.id.navigation_home;
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
