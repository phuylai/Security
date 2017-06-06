package com.ecnu.security.view.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecnu.security.Model.TrustedContact;
import com.ecnu.security.R;

import java.util.List;

/**
 * Created by Phuylai on 2017/5/21.
 */

public class ContactListAdapter extends BaseAdapter{

    private Context context;
    private List<TrustedContact> contacts;
    private CallListener callListener;


    public interface CallListener{
        void call(String phone);
    }

    public ContactListAdapter(Context context, List<TrustedContact> contacts,CallListener listener) {
        this.context = context;
        this.contacts = contacts;
        this.callListener = listener;
    }

    @Override
    public int getCount() {
        if(contacts == null)
            return 0;
        return contacts.size();
    }

    @Override
    public TrustedContact getItem(int i) {
        if(contacts == null)
            return null;
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        final TrustedContact contact = contacts.get(i);
        if(view == null){
            view =  LayoutInflater.from(context).inflate(R.layout.device_item,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_device_name);
            viewHolder.tv_decs = (TextView) view.findViewById(R.id.tv_desc);
            viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_action);
            viewHolder.cardView = (CardView) view.findViewById(R.id.card_view);
            view.setTag(viewHolder);
        }else{
           viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_name.setText(contact.getName());
        viewHolder.tv_decs.setText(contact.getPhonenumber());
        viewHolder.iv_icon.setImageResource(R.drawable.ic_call);
        viewHolder.iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callListener != null)
                    callListener.call(contact.getPhonenumber());
            }
        });
        return view;
    }

    public class ViewHolder{
        TextView tv_name;
        TextView tv_decs;
        ImageView iv_icon;
        CardView cardView;
    }

}
