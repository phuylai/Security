package com.ecnu.security.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ecnu.security.Controller.AsyncTaskController;
import com.ecnu.security.Controller.CurrentSession;
import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Helper.MLog;
import com.ecnu.security.Model.ActionType;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.Model.MicoUserExt;
import com.ecnu.security.Model.TrustedContact;
import com.ecnu.security.R;
import com.ecnu.security.Util.DialogUtil;
import com.ecnu.security.Util.MyPreference;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.Util.ToastUtil;
import com.ecnu.security.view.Adapter.ContactAdapter;
import com.ecnu.security.view.Adapter.ContactListAdapter;
import com.ecnu.security.view.activities.JsonHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.fog.callbacks.MiCOCallBack;

/**
 * Created by Phuylai on 2017/5/13.
 */

public class ContactListFragment extends BaseFragment implements ContactAdapter.ContactListener,
        DialogUtil.DialogContactListener, DialogUtil.EditContactListener, AdapterView.OnItemClickListener,
        ContactListAdapter.CallListener, DialogUtil.CallDialog {

    private String TAG = ContactListFragment.class.getSimpleName();

    private List<TrustedContact> contacts = new ArrayList<>();
    private List<TrustedContact> models = new ArrayList<>();
    //private ContactAdapter contactAdapter;
    private ContactListAdapter contactAdapter;
    //protected RecyclerView recyclerView;
    protected ListViewCompat listViewCompat;

    private String allContacts = "";

    protected long DELAYMILLIS = 200;
    private View mView;

    private AsyncTask<Object,Void,List<TrustedContact>> dbload = null;
    private AsyncTask<Object,Void,Void> dbsave = null;

    private MyPreference myPreference;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.PARAM_UPDATE:
                    models.clear();
                    //contacts = (List<TrustedContact>)msg.obj;
                    //models.addAll(contacts);
                    models.addAll((Collection<? extends TrustedContact>) msg.obj);
                    MLog.i("contact list","update view");
                    if(contactAdapter != null) {
                        contactAdapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    private void refreshList(){
        models.clear();
        models.addAll(contacts);
        MLog.i("contacts",String.valueOf(contacts.size()));
        if(contactAdapter != null)
            contactAdapter.notifyDataSetChanged();
    }

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
        //layoutId = R.layout.fragment_recyler;
        layoutId = R.layout.fragment_listview;
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
        //recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        listViewCompat = (ListViewCompat) rootView.findViewById(R.id.listview);
        //contactAdapter = new ContactAdapter(activity,models,this);
        contactAdapter = new ContactListAdapter(activity,models,this);
        //recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setAdapter(contactAdapter);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        listViewCompat.setAdapter(contactAdapter);
        listViewCompat.setOnItemClickListener(this);
        loadContact();
    }

    private synchronized void loadContact(){
        if(dbload != null)
            return;
        dbload = new AsyncTask<Object, Void, List<TrustedContact>>() {
            @Override
            protected List<TrustedContact> doInBackground(Object... objects) {
                return CurrentSession.getContacts(context,myPreference.getClientID());
            }

            @Override
            protected void onPostExecute(List<TrustedContact> trustedContacts) {
                dbload = null;
                if(trustedContacts != null && trustedContacts.size() > 0){
                    contacts.clear();
                    contacts.addAll(trustedContacts);
                    refreshList();
                }
            }
        };
        AsyncTaskController.startTask(dbload);
        getContactList();
    }

    private void getContactList(){
        micoUserExt.getUserInfo(new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                MLog.i("note",message);
                String note = JsonHelper.getNote(message);
                if(StringUtil.isNull(note)){
                    return;
                }
                contacts.clear();
                contacts.addAll(SplitString(note));
                List<TrustedContact> save = new ArrayList<TrustedContact>(contacts);
                saveContact(save);
                refreshList();
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToast(message);
            }
        }, myPreference.getToken());
    }

    private synchronized void saveContact(final List<TrustedContact> contacts){
        if(dbsave != null)
            return;
        dbsave = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... objects) {
                for(TrustedContact trustedContact:contacts){
                    CurrentSession.saveContact(activity,trustedContact,myPreference.getClientID());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dbsave = null;
            }
        };
        AsyncTaskController.startTask(dbsave);
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
        //allContacts = contact.getName() + "|" + contact.getPhonenumber();
        if(StringUtil.isNull(allContacts)) {
            allContacts += contact.getName() + "|" + contact.getPhonenumber();
        }else{
            allContacts += "," + contact.getName() + "|" + contact.getPhonenumber();
        }
        updateNote(contact);
    }

    private void updateNote(final int position, final TrustedContact contact){
        micoUserExt.setNote(allContacts, new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                MLog.i("contact list",message);
                contacts.get(position).setName(contact.getName());
                contacts.get(position).setPhonenumber(contact.getPhonenumber());
                updateList();
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToastShort(activity,message);
            }
        },myPreference.getToken());
    }

    private void updateNote(final TrustedContact contact){
        micoUserExt.setNote(allContacts, new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                MLog.i("contact list",message);
                contacts.add(contact);
                updateList();
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToastShort(activity,message);
            }
        },myPreference.getToken());
    }

    private void updateNote(final int position){
        micoUserExt.setNote(allContacts, new MiCOCallBack() {
            @Override
            public void onSuccess(String message) {
                MLog.i("contact list",message);
                contacts.remove(position);
                updateList();
            }

            @Override
            public void onFailure(int code, String message) {
                ToastUtil.showToastShort(activity,message);
            }
        },myPreference.getToken());
    }

    private void updateList(){
        models.clear();
        models.addAll(contacts);
        if(contactAdapter != null) {
            contactAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void yes(TrustedContact contact, int position) {
        String temp = "";
        for(int i=0;i<contacts.size();i++){
            if(i==position){
                temp+=contact.getName()+"|"+contact.getPhonenumber()+",";
            }else{
                temp+=contacts.get(i).getName() + "|" + contacts.get(i).getPhonenumber()+",";
            }
        }
        CurrentSession.updateContact(activity,contacts.get(position),contact);
        allContacts = temp.substring(0,temp.length()-1);
        updateNote(position,contact);
    }

    @Override
    public void delete(int position) {
        //contacts.remove(position);
        String temp = "";
        for(int i=0;i<contacts.size();i++){
            if(i != position) {
                temp += contacts.get(i).getName() + "|" + contacts.get(i).getPhonenumber() + ",";
            }
        }
        if(position == 0){
            allContacts = " ";
        }else {
            allContacts = temp.substring(0, temp.length() - 1);
        }
        CurrentSession.removeContact(activity,contacts.get(position));
        updateNote(position);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TrustedContact contact = models.get(i);
        DialogUtil.EditContactDialog(activity,contact,i,this);
    }

    @Override
    public void call(String phone) {
        DialogUtil.callContactDialog(activity,phone,this);
    }

    @Override
    public void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
        startActivity(intent);
    }

    @Override
    public void setSOS(String phone) {
        myPreference.setSOS(phone);
    }
}
