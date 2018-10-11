package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.R;

public class InformasiObatAs extends AppCompatActivity {
    public static String EXTRA_OBAT = "extra_obat";

    ImageView iv_picture_obat3;
    TextView tv_nama_obat3, tv_indikasi_obat,tv_kandungan_obat,tv_dosis_obat,tv_carapakai_obat,
            tv_kemasan_obat,tv_golongan_obat,tv_resepYN_obat,tv_kontraindikasi_obat,tv_carasimpan_obat,tv_principal_obat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informasi_obat_as);

        obat mobat = getIntent().getParcelableExtra(EXTRA_OBAT);

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

        Picasso.with(InformasiObatAs.this).load(mobat.getProductPhoto()).into(iv_picture_obat3);
        tv_nama_obat3.setText(mobat.getProductName());
        tv_indikasi_obat.setText(mobat.getIndikasi());
        tv_kandungan_obat.setText(mobat.getKandungan());
        tv_dosis_obat.setText(mobat.getDosis());
        tv_carapakai_obat.setText(mobat.getCarapakai());
        tv_kemasan_obat.setText(mobat.getKemasan());
        tv_golongan_obat.setText(mobat.getGolongan());
        tv_resepYN_obat.setText(mobat.getResepYN());
        tv_kontraindikasi_obat.setText(mobat.getKontraindikasi());
        tv_carasimpan_obat.setText(mobat.getCarasimpan());
        tv_principal_obat.setText(mobat.getPrincipal());


        setStatusBarGradiant(this);
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
