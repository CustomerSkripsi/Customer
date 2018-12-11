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

public class ObatPromoAdapter extends RecyclerView.Adapter<ObatPromoAdapter.obatPromoViewHolder>{
    private List<obat> obatPromoList;
    private Context context;
    private LinearLayout linearLayout;
    private static UserLocalStore userLocal;
    static User currUser;

    public ObatPromoAdapter(Context context) {
        this.context = context;
        userLocal = new UserLocalStore(context);
        currUser = userLocal.getLoggedInUser();
    }

    public List<obat> getObatPromoList() {
        return obatPromoList;
    }

    public void setObatPromoList(List<obat> obatPromoList) {
        this.obatPromoList = obatPromoList;
    }

    @NonNull
    @Override
    public ObatPromoAdapter.obatPromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cv_obat_promo, parent, false);
        return new ObatPromoAdapter.obatPromoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull obatPromoViewHolder holder, int position) {
        final obat obat = obatPromoList.get(position);

        holder.namaObat.setText(obat.getProductName());
        holder.priceObat.setText(obat.getOutletProductPrice());
//        holder.priceObatAfterDsc.setText();
        Picasso.with(context).load(obat.getProductPhoto()).into(ObatFavoriteAdapter.obatFavoriteViewHolder.obatPict);
    }

    @Override
    public int getItemCount() {
        return obatPromoList.size();
    }

    public static class obatPromoViewHolder extends RecyclerView.ViewHolder {

        RecyclerView cardListBrand;
        TextView namaObat, priceObat, priceObatAfterDsc;
        static ImageView obatPict;
        LinearLayout linearLayout;


        public obatPromoViewHolder(View v) {
            super(v);
            cardListBrand = v.findViewById(R.id.cv_produkPromo);
            namaObat = v.findViewById(R.id.tv_name_obat_promo);
            priceObat = v.findViewById(R.id.tv_price_obat_promo);
            obatPict = v.findViewById(R.id.iv_picture_obat_promo);
            priceObatAfterDsc = v.findViewById(R.id.tv_price_after_disc_obat_promo);
            linearLayout = v.findViewById(R.id.ll_cv_obat_promo);
        }
    }
}
