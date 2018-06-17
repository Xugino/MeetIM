package com.xieyangzhe.meetim.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.xieyangzhe.meetim.Adapters.ContactAdapter;
import com.xieyangzhe.meetim.Adapters.RecentAdapter;
import com.xieyangzhe.meetim.Models.RecentChat;
import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Utils.DBTool;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment {

    private Context context;
    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private RecentAdapter adapter;

    private List<RecentChat> recentChatList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getRecentChatList();

        view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        context = getActivity();
        recyclerView = view.findViewById(R.id.list_recent);
        manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        adapter = new RecentAdapter(context, recentChatList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getRecentChatList() {
        recentChatList = new ArrayList<>();
        DBTool dbTool = new DBTool();
        recentChatList = dbTool.getRecentChats();

        for (RecentChat recentChat: dbTool.getRecentChats()) {
            Log.d("bbbbbbb", recentChat.getChatMsg());
        }
    }
}
