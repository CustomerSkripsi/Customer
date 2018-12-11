package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mobi.garden.bottomnavigationtest.LoginRegister.User;
import mobi.garden.bottomnavigationtest.LoginRegister.UserLocalStore;
import mobi.garden.bottomnavigationtest.Model.obat;
import mobi.garden.bottomnavigationtest.R;



public class ObatFavoriteAdapter extends RecyclerView.Adapter<ObatFavoriteAdapter.obatFavoriteViewHolder> {
    private List<obat> obatFavoriteList;
    private Context context;
    private LinearLayout linearLayout;
    private static UserLocalStore userLocal;
    static User currUser;

    public ObatFavoriteAdapter(Context context, List<obat> list) {
        this.context = context;
        userLocal = new UserLocalStore(context);
        currUser = userLocal.getLoggedInUser();
    }

    public List<obat> getobatFavoriteList() {
        return obatFavoriteList;
    }

    public void setobatFavoriteList(List<obat> obatFavoriteList) {
        this.obatFavoriteList = obatFavoriteList;
    }

    @NonNull
    @Override
    public ObatFavoriteAdapter.obatFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cv_obat_favorite, parent, false);
        return new ObatFavoriteAdapter.obatFavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull obatFavoriteViewHolder holder, int position) {
        final obat obat = obatFavoriteList.get(position);

        holder.namaObat.setText(obat.getProductName());
        holder.priceObat.setText(obat.getOutletProductPrice());
        Picasso.with(context).load(obat.getProductPhoto()).into(obatFavoriteViewHolder.obatPict);

    }

    @Override
    public int getItemCount() {
        return obatFavoriteList.size();
    }

    public static class obatFavoriteViewHolder extends RecyclerView.ViewHolder {

        RecyclerView cardListBrand;
        TextView namaObat,priceObat;
        static ImageView obatPict;
        LinearLayout linearLayout;


        public obatFavoriteViewHolder(View v) {
            super(v);
            cardListBrand = v.findViewById(R.id.cvObatFavorite);
            namaObat = v.findViewById(R.id.tv_name_obat_favorite);
            priceObat = v.findViewById(R.id.tv_price_obat_favorite);
            obatPict = v.findViewById(R.id.iv_picture_obat_favorite);
            linearLayout = v.findViewById(R.id.ll_cv_obat_favorite);
        }
    }
}



