package com.ecnu.security.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Model.ActionType;
import com.ecnu.security.Model.MicoUserExt;
import com.ecnu.security.Model.TrustedContact;
import com.ecnu.security.R;
import com.ecnu.security.Util.DialogUtil;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.Util.ToastUtil;
import com.ecnu.security.view.Adapter.ContactAdapter;
import com.ecnu.security.view.activities.JsonHelper;

import java.util.ArrayList;
import java.util.List;

import io.fog.callbacks.MiCOCallBack;

/**
 * Created by Phuylai on 2017/5/13.
 */

public class ContactListFragment extends BaseFragment implements ContactAdapter.ContactListener, DialogUtil.DialogContactListener, DialogUtil.EditContactListener {

    private String TAG = ContactListFragment.class.getSimpleName();

    private List<TrustedContact> contacts = new ArrayList<>();
    private List<TrustedContact> models = new ArrayList<>();
    private ContactAdapter contactAdapter;
    protected RecyclerView recyclerView;

    private String allContacts = "";

    protected long DELAYMILLIS = 200;
    private View mView;

    private MyPreference myPreference;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.PARAM_UPDATE:
                    models.clear();
                    models.addAll(contacts);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            contactAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    private MicoUserExt micoUserExt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backIndicator = true;
        bottomView = false;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void no() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_recyler;
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(layoutId, null);
        }
        findViews(mView);
        return mView;
    }

    @Override
    protected void findViews(View rootView) {
        micoUserExt = new MicoUserExt();
        myPreference = MyPreference.getInstance(activity);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        contactAdapter = new ContactAdapter(activity,models,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getContactList();
    }

    private void getContactList(){
        micoUserExt.getUserInfo(new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                String note = JsonHelper.getNote(message);
                contacts.clear();
                contacts.addAll(SplitString(note));
                if(contacts != null) {
                    Message msg = new Message();
                    msg.what = Constants.PARAM_UPDATE;
                    handler.sendMessageDelayed(msg,DELAYMILLIS);
                }
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToast(message);
            }
        }, myPreference.getToken());
    }

    @Override
    public void contactClick(TrustedContact contact,int position) {
        //go to edit
        DialogUtil.EditContactDialog(activity,contact,position,this);
    }

    private List<TrustedContact> SplitString(String note){
        if(StringUtil.isNull(note))
            return null;
        allContacts = note;
        String[] list = note.split(",");
        List<TrustedContact> contacts = new ArrayList<>();
        for (String aList : list) {
            String[] namePhone = aList.split("\\|");
            contacts.add(new TrustedContact(namePhone[1],namePhone[0]));
        }
        return contacts;
    }

    @Override
    public void changeTitle() {
        setTitleId(R.string.trusted_contact);
    }

    @Override
    public void initMenu(Context context, Menu menu) {
        clearMenu(menu);
        ((Activity) context).getMenuInflater().inflate(R.menu.add_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.ic_add:
                //go to add
                DialogUtil.addContactDialog(activity,this);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void yes(TrustedContact contact) {
        allContacts = contact.getName() + "|" + contact.getPhonenumber();
        /*if(StringUtil.isNull(allContacts)) {
            allContacts += contact.getName() + "|" + contact.getPhonenumber();
        }else{
            allContacts += "," + contact.getName() + "|" + contact.getPhonenumber();
        }*/
        updateNote();
    }

    private void updateNote(){
        micoUserExt.setNote(allContacts, new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                ToastUtil.showToastShort(activity,message);
                getContactList();
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToastShort(activity,message);
            }
        },myPreference.getToken());
    }

    @Override
    public void yes(TrustedContact contact, int position) {
        //edit the info
        contacts.get(position).setPhonenumber(contact.getPhonenumber());
        contacts.get(position).setName(contact.getName());
        String temp = "";
        for(TrustedContact aList :contacts){
            temp += aList.getName() + "|" + aList.getPhonenumber() + ";";
        }
        allContacts = temp.substring(0,temp.length()-1);
        updateNote();
    }

    @Override
    public void delete(int position) {
        //delete the contact
        contacts.remove(position);
        String temp = "";
        for(TrustedContact aList :contacts){
            temp += aList.getName() + "|" + aList.getPhonenumber() + ";";
        }
        allContacts = temp.substring(0,temp.length()-1);
        updateNote();
    }
}
