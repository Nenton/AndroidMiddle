package com.nenton.androidmiddle.ui.screens.address;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nenton.androidmiddle.R;
import com.nenton.androidmiddle.data.storage.UserAddressDto;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder>{

    private ArrayList<UserAddressDto> mAddresses = new ArrayList<>();

    public void addItem(UserAddressDto address){
        mAddresses.add(address);
        notifyDataSetChanged();
    }

    public void reloadAdapter(){
        mAddresses.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserAddressDto addressDto = mAddresses.get(position);
        holder.mAddress.setText(addressToString(addressDto));
        holder.mComment.setText(addressDto.getComment());
        holder.mLabelAddress.setText(addressDto.getName());
    }

    private String addressToString(UserAddressDto addressDto) {
        return "ул." +
                addressDto.getStreet() +
                " " +
                addressDto.getHouse() +
                "-" +
                addressDto.getFloor() +
                " этаж " +
                addressDto.getApartment() +
                " квартира";
    }

    @Override
    public int getItemCount() {
        return mAddresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.label_address)
        TextView mLabelAddress;
        @BindView(R.id.address_txt)
        TextView mAddress;
        @BindView(R.id.comment_txt)
        TextView mComment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
