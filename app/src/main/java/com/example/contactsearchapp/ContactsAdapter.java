package com.example.contactsearchapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public abstract class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ProductViewHolder> {
    Context mCtx;

    ArrayList<ContactsModel> contactslist;
    private BottomSheetDialog dialog;


    public ContactsAdapter(Context mCtx, ArrayList<ContactsModel> contactslist) {
        this.mCtx = mCtx;
        this.contactslist = contactslist;


    }

    public abstract void load();

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.contact_list_items, parent, false);
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
        holder.cl_cardcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new BottomSheetDialog(mCtx);
                View convertView = LayoutInflater.from(mCtx).inflate(R.layout.item_list_my_contact, null);
                dialog.setContentView(convertView);

                CircleImageView clImgProfile = convertView.findViewById(R.id.clImgProfile);
                AppCompatTextView tvEmailValueProfile = convertView.findViewById(R.id.tvEmailValueProfile);
                AppCompatTextView txt_name = convertView.findViewById(R.id.txt_name);
                AppCompatTextView tvPhoneValueProfile = convertView.findViewById(R.id.tvPhoneValueProfile);
                AppCompatTextView tvAddressValueProfile = convertView.findViewById(R.id.tvAddressValueProfile);
                AppCompatTextView tvNote = convertView.findViewById(R.id.tvNote);
                tvEmailValueProfile.setText(contactslist.get(position).getEmail());
                txt_name.setText(contactslist.get(position).getName());
                tvPhoneValueProfile.setText(contactslist.get(position).getNumber());
                tvAddressValueProfile.setText(contactslist.get(position).getOrganization());
                tvNote.setText(contactslist.get(position).getOther());

                Glide.with(mCtx)
                        .load(contactslist.get(position).getImage())
                        .placeholder(R.mipmap.userplaceholder)
                        .apply(RequestOptions.skipMemoryCacheOf(false))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                        .into(clImgProfile);

                dialog.show();


            }
        });


        if ((position >= getItemCount() - 1))
            load();
    }


    @Override
    public int getItemCount() {
        return contactslist.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtphonenumber, txtcontactname;
        public AppCompatImageView clImgProfile;
        public ConstraintLayout cl_cardcontact;

        public ProductViewHolder(View itemView) {
            super(itemView);
            txtphonenumber = itemView.findViewById(R.id.txtphonenumber);
            txtcontactname = itemView.findViewById(R.id.txtcontactname);
            clImgProfile = itemView.findViewById(R.id.cardImgProfile);
            cl_cardcontact = itemView.findViewById(R.id.cl_cardcontact);

        }
    }

}



