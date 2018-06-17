package com.xieyangzhe.meetim.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieyangzhe.meetim.Activities.ChatActivity;
import com.xieyangzhe.meetim.Models.RecentChat;
import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Utils.TimeFormatter;

import java.util.List;

/**
 * Created by joseph on 6/15/18.
 */

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {

    private Context context;
    private List<RecentChat> recentChats;

    public RecentAdapter(Context context, List<RecentChat> recentChats) {
        this.context = context;
        this.recentChats = recentChats;
    }

    public RecentAdapter setContent(List<RecentChat> content) {
        recentChats.clear();
        recentChats = content;
        return this;
    }

    @Override
    public RecentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecentAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recent, parent, false));
    }

    @Override
    public void onBindViewHolder(RecentAdapter.ViewHolder holder, int position) {
        final RecentChat recentChat = recentChats.get(position);
        holder.imageRecent.setImageResource(R.drawable.ic_user);
        holder.textRecentName.setText(recentChat.getContact().getName());
        holder.textRecentMsg.setText(recentChat.getChatMsg());
        TimeFormatter timeFormatter = new TimeFormatter();
        holder.textRecentTime.setText(timeFormatter.getFormattedTimeText(recentChat.getChatTime()) );

        holder.content.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("J" +
                    "ID_TO", recentChat.getContact().getJid());
            bundle.putString("USERNAME_TO", recentChat.getContact().getName());
            bundle.putString("HEAD_TO", recentChat.getContact().getHead());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        holder.itemView.findViewById(R.id.recent_delete).setOnClickListener(view -> {
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    recentChats.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            };

            new Thread(() -> {
                //TODO: delete message record in database when recent chat is deleted.
            }).start();

        });

    }

    @Override
    public int getItemCount() {
        return recentChats == null ? 0 : recentChats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textRecentName;
        ImageView imageRecent;
        TextView textRecentMsg;
        TextView textRecentTime;
        View content;


        public ViewHolder(View itemView) {
            super(itemView);

            textRecentName = itemView.findViewById(R.id.recent_name);
            imageRecent = itemView.findViewById(R.id.recent_head);
            textRecentMsg = itemView.findViewById(R.id.recent_message);
            textRecentTime = itemView.findViewById(R.id.recent_time);

            content = itemView.findViewById(R.id.recent);
        }
    }
}
