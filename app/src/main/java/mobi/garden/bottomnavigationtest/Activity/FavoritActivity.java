package mobi.garden.bottomnavigationtest.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import mobi.garden.bottomnavigationtest.R;
import mobi.garden.bottomnavigationtest.Searching;

public class FavoritActivity extends AppCompatActivity {
    RecyclerView rvFavorit;
    Searching searching;
    Context context;
    public static String geturl;
    TextView etSearchFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);
        context = FavoritActivity.this;

        rvFavorit = findViewById(R.id.rvActivityFavorit);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        Intent i = getIntent();
        geturl = i.getStringExtra("allfavorit");
        Log.d("URL", geturl);
        rvFavorit.setLayoutManager(llm);

        etSearchFavorite = findViewById(R.id.search);
//        etSearchFavorite.setOnClickListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if(actionId== EditorInfo.IME_ACTION_SEARCH) {
//                    searchProcess(etSearchFavorite.getText().toString());
//                    return true;
//                }
//                return false;
//            }
//        });

        etSearchFavorite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchProcess(etSearchFavorite.getText().toString());
            }
        });
        searchProcess(etSearchFavorite.getText().toString());

        Toolbar dToolbar = findViewById(R.id.toolbar2);
        dToolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        dToolbar.setTitle("FAVORIT");
        dToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FavoritActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });


    }


    public void searchProcess(String text){
        searching = new Searching(context,text,rvFavorit);
        searching.favorit();
    }

//    public void searchpromocategory(){
//        searching = new Searching(context, rvFavorit);
//        searching.promocategory();
//    }

    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.toolbar);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
