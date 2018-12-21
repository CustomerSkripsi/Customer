package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.SearchApotek;
import mobi.garden.bottomnavigationtest.Activity.SearchProduk;
import mobi.garden.bottomnavigationtest.R;

public class GlobalSearchAdapter extends RecyclerView.Adapter<GlobalSearchAdapter.GlobalSearchViewHolder> {
    private List<String> productList;
    private Context context;
    private int context_pilihan;

    public void setContext_pilihan(int context_pilihan) {
        this.context_pilihan = context_pilihan;
    }

    public GlobalSearchAdapter(Context context) {
        this.context = context;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public GlobalSearchAdapter.GlobalSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dropdown,parent,false);
        return new GlobalSearchAdapter.GlobalSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GlobalSearchAdapter.GlobalSearchViewHolder holder, int position) {
        final String ProductName = productList.get(position);
        holder.searchProduct.setText(ProductName);
        holder.searchProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context_pilihan!=0){
                    if(context_pilihan == 1){ // INTEN KE APOTEK
                        Intent i = new Intent(context,SearchApotek.class);
                        i.putExtra("ApotekName", ProductName);
                        context.startActivity(i);
                    }else{
                        Intent i = new Intent(context,SearchProduk.class);
                        i.putExtra("ProdukName", ProductName);
                        context.startActivity(i);
//                        intent.putExtra("PreregisOwnerID",u.getUserid());//UserID
//                        intent.putExtra("OutletID",u.getOutletid());
                    }
                }

            }

        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class GlobalSearchViewHolder extends RecyclerView.ViewHolder {
        TextView searchProduct;
        public GlobalSearchViewHolder(View itemView) {
            super(itemView);
            searchProduct = itemView.findViewById(R.id.textAutoComplete);
        }
    }
}