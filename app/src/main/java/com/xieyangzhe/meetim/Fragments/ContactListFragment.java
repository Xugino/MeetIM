package com.xieyangzhe.meetim.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xieyangzhe.meetim.Adapters.ContactAdapter;
import com.xieyangzhe.meetim.Models.Contact;
import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Utils.XMPPTool;

import java.util.ArrayList;
import java.util.List;


public class ContactListFragment extends Fragment {

    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ContactAdapter adapter;

    private List<Contact> contactList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getContactData();
        view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        context = getActivity();
        recyclerView = view.findViewById(R.id.list_contacts);
        manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        adapter = new ContactAdapter(context, contactList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getContactData() {
        contactList = XMPPTool.getXmppTool().contactList;
    }
}
