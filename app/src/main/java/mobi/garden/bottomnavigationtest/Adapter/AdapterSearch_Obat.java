package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.DetailObatHome;
import mobi.garden.bottomnavigationtest.Model.Model_Obat;
import mobi.garden.bottomnavigationtest.Model.session_obat;
import mobi.garden.bottomnavigationtest.R;

public class AdapterSearch_Obat extends RecyclerView.Adapter<AdapterSearch_Obat.SearchApotekViewHolder> {

    private List<Model_Obat> modelObatList = new ArrayList<>();
    Context context;
    private int selectedPos = RecyclerView.NO_POSITION;



    public AdapterSearch_Obat(Context context) {
        this.context = context;
    }

    public List<Model_Obat> getModelObatList() {
        return modelObatList;
    }

    public void setModelObatList(List<Model_Obat> modelObatList) {
        this.modelObatList = modelObatList;
    }


    @NonNull
    @Override
    public SearchApotekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_search, parent
                , false);
        return new SearchApotekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchApotekViewHolder holder, int position) {

        Model_Obat modelSearch = modelObatList.get(position);
        holder.tvModelName.setText(modelSearch.ProductName);

        holder.itemView.setBackgroundColor(selectedPos == position ? Color.LTGRAY: Color.TRANSPARENT);
        holder.tvModelName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_obat session = new session_obat(context);
                session.createLoginSession(modelSearch.ProductID,modelSearch.ProductName,modelSearch.productPhoto,modelSearch.productDescription,modelSearch.indikasi
                        ,modelSearch.kandungan,modelSearch.dosis,modelSearch.carapakai,modelSearch.kemasan,modelSearch.golongan,modelSearch.resepYN,modelSearch.kontraindikasi
                        ,modelSearch.carasimpan,modelSearch.principal,modelSearch.categoryID);
                Intent intent = new Intent(context, DetailObatHome.class);
                intent.putExtra("productname", modelSearch.ProductName+"");
                intent.putExtra("productImage",modelSearch.productPhoto);
                Toast.makeText(context, "You've Just Press : " + modelSearch.ProductName , Toast.LENGTH_SHORT).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelObatList.size();
    }

    public class SearchApotekViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvModelName;
        public SearchApotekViewHolder(View itemView) {
            super(itemView);
            tvModelName = itemView.findViewById(R.id.listSearch);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();

            if(pos != RecyclerView.NO_POSITION){
                Model_Obat onClick = modelObatList.get(pos);
                Toast.makeText(v.getContext(),"you clicked " + onClick.ProductName, Toast.LENGTH_SHORT).show();
                notifyItemChanged(selectedPos);
                selectedPos = getLayoutPosition();
                notifyItemChanged(selectedPos);
            }
        }
    }
}


