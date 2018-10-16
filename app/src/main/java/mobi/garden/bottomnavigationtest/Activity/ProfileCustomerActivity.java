package mobi.garden.bottomnavigationtest.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import mobi.garden.bottomnavigationtest.LoginRegister.Register;
import mobi.garden.bottomnavigationtest.R;

public class ProfileCustomerActivity extends AppCompatActivity {
    ImageView IvChevronLeft;
    TextView TvCustomerName,TvMemberID, TvCustomerDOB, TvCustomerPhone, TvCustomerEmail;
    LinearLayout ll_CustomerDOB, ll_CustomerPhone, ll_CustomerEmail, ll_CustomerAlamat, ll_ChangePassword;
    CircleImageView ProfileCIV;
    public Calendar myCalendar;



    Context context;


    private static final String TAG = "Profile";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        IvChevronLeft = findViewById(R.id.ivChevronLeftProfile);
        TvCustomerName = findViewById(R.id.tvCustomerName);
        TvMemberID = findViewById(R.id.tvMemberID);
        TvCustomerDOB = findViewById(R.id.tvCustomerDOB);
        TvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        TvCustomerEmail = findViewById(R.id.tvCustomerEmail);
        ll_CustomerDOB = findViewById(R.id.llCustomerDOB);
        ll_CustomerPhone = findViewById(R.id.llCustomerPhone);
        ll_CustomerEmail = findViewById(R.id.llCustomerEmail);
        ll_CustomerAlamat = findViewById(R.id.llDaftarAlamatCustomer);
        ll_ChangePassword = findViewById(R.id.llChangePassword);

        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date;

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        IvChevronLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileCustomerActivity.this, MyMenuActivity.class);
                startActivity(i);
            }
        });

        ll_CustomerDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileCustomerActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ll_CustomerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileCustomerActivity.this, EmailProfile.class );
                startActivity(i);
            }
        });

        ll_CustomerPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_CustomerAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileCustomerActivity.this, AlamatProfile.class );
                startActivity(i);
            }
        });

        ll_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword changePassword = new ChangePassword(ProfileCustomerActivity.this);
                changePassword.changePass();
            }
        });

        DatePickerDialog.OnDateSetListener nDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                Log.d(TAG,"onDateSet: yyyy/mm/dd: " + year + "/" + month + "/" + day);

                String date = year + "/" + month + "/" + day;
                TvCustomerDOB.setText(date);
            }
        };
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        TvCustomerDOB.setText(sdf.format(myCalendar.getTime()));
    }
}
