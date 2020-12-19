package com.example.contactsearchapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


public abstract class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ProductViewHolder> {
    Context mCtx;

    ArrayList<ContactsModel> contactslist;


    public ContactsAdapter(Context mCtx, ArrayList<ContactsModel> contactslist) {
        this.mCtx = mCtx;
        this.contactslist = contactslist;


    }
    public abstract void load();
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.contact_list_items, null);
        return new ContactsAdapter.ProductViewHolder(view);
    }




    @Override
    public void onBindViewHolder(ContactsAdapter.ProductViewHolder holder, int position) {
//
        Glide.with(mCtx)
                .load(contactslist.get(position).getImage())
                .placeholder(R.mipmap.userplaceholder)
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.clImgProfile);


        holder.txtcontactname.setText(contactslist.get(position).getName());
        holder.txtphonenumber.setText(contactslist.get(position).getNumber());

        if ((position >= getItemCount() - 1))
            load();
    }


    @Override
    public int getItemCount() {
        return contactslist.size();
    }

    public void setFilter(ArrayList<ContactsModel> filterlist) {
        ArrayList filters = new ArrayList<>();
        filters = filterlist;
        filters.addAll(filterlist);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setData(ArrayList<ContactsModel> contactsModelArrayList) {
        Toast.makeText(mCtx, "hey there"+ contactsModelArrayList, Toast.LENGTH_SHORT).show();
        contactslist.addAll(contactsModelArrayList);
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtphonenumber, txtcontactname;
        public AppCompatImageView clImgProfile;

        public ProductViewHolder(View itemView) {
            super(itemView);
            txtphonenumber = itemView.findViewById(R.id.txtphonenumber);
            txtcontactname = itemView.findViewById(R.id.txtcontactname);
            clImgProfile = itemView.findViewById(R.id.cardImgProfile);

        }
    }

}



