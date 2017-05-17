package com.ecnu.security.view.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecnu.security.Model.TrustedContact;
import com.ecnu.security.R;

import java.util.List;

/**
 * Created by Phuylai on 2017/5/13.
 */

public class ContactAdapter  extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{

    private Context context;
    private List<TrustedContact> contacts;
    private ContactListener listener;

    public interface ContactListener{
        void contactClick(TrustedContact contact,int position);
    }

    public void ContactClick(TrustedContact contact,int position){
        if( listener!= null)
            listener.contactClick(contact,position);
    }

    public ContactAdapter(Context context,List<TrustedContact> contacts,ContactListener contactListener){
        this.context = context;
        this.contacts = contacts;
        this.listener = contactListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TrustedContact contact = contacts.get(i);
        viewHolder.tv_name.setText(contact.getName());
        viewHolder.tv_decs.setText(contact.getPhonenumber());
        viewHolder.iv_icon.setImageResource(R.drawable.ic_call);
    }

    @Override
    public int getItemCount() {
        if(contacts == null)
            return 0;
        return contacts.size();
    }

    @Override
    public long getItemId(int position) {
       return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_item,viewGroup,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name;
        TextView tv_decs;
        ImageView iv_icon;
        CardView cardView;

        ViewHolder(View view){
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_device_name);
            tv_decs = (TextView) view.findViewById(R.id.tv_desc);
            iv_icon = (ImageView) view.findViewById(R.id.iv_action);
            cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            TrustedContact contact = contacts.get(getAdapterPosition());
            ContactClick(contact,getAdapterPosition());
        }
    }
}
