package mobi.garden.bottomnavigationtest.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

////import com.pharmanet.member.EditActivity;
//import com.pharmanet.member.EditMemberLama;
//import com.pharmanet.member.R;
//import com.pharmanet.member.RegisterActivity;
//import com.pharmanet.member.model.CityItem;


import java.util.List;

import mobi.garden.bottomnavigationtest.Activity.EditEMember;
import mobi.garden.bottomnavigationtest.Activity.RegisterMember;
import mobi.garden.bottomnavigationtest.Model.CityItem;
import mobi.garden.bottomnavigationtest.R;
public class SearchAdapterEdit extends RecyclerView.Adapter<SearchAdapterEdit.searchViewHolder> {
    CityItem cityItem;
    List<CityItem> cityItemList;
    Context context;


    public SearchAdapterEdit(List<CityItem> cityItemList, Context context) {
        this.cityItemList = cityItemList;
        this.context = context;
    }


    @Override
    public searchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_search,parent ,false);
        return new searchViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(searchViewHolder holder, final int position) {
        cityItem = cityItemList.get(position);
        holder.poNumber.setText(cityItem.getNamaKota());

        holder.poNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityItem cityItem = cityItemList.get(position);
                if(RegisterMember.etKota != null) {
                    RegisterMember.etKota.setText(cityItem.getNamaKota());
                    RegisterMember.etTanggalLahir.requestFocus();
                }else {
                    EditEMember.etKota.setText(cityItem.getNamaKota());
//                    EditActivity.etTanggalLahir.requestFocus();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityItemList.size();
    }



    public class searchViewHolder extends RecyclerView.ViewHolder{
        TextView poNumber;
        public searchViewHolder(View itemView) {
            super(itemView);

            poNumber = itemView.findViewById(R.id.tvPONumber);
        }
    }
}
