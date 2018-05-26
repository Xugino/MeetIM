package com.xieyangzhe.meetim.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xieyangzhe.meetim.Activities.ChatActivity;
import com.xieyangzhe.meetim.Models.Contact;
import com.xieyangzhe.meetim.R;
import com.xieyangzhe.meetim.Utils.IMApplication;
import com.xieyangzhe.meetim.Utils.XMPPTool;

import java.util.List;

/**
 * Created by joseph on 5/24/18.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context context;
    private List<Contact> contactList;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    public ContactAdapter setContent(List<Contact> content) {
        contactList.clear();
        contactList = content;
        return this;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contact contact = contactList.get(position);
        holder.imageHead.setImageResource(R.mipmap.ic_launcher);
        holder.textContactName.setText(contact.getName());
        holder.content.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("JID_TO", contactList.get(position).getJid());
            bundle.putString("USERNAME_TO", contactList.get(position).getName());
            bundle.putString("HEAD_TO", contactList.get(position).getHead());
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        holder.itemView.findViewById(R.id.button_delete).setOnClickListener(view -> {
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    contactList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            };

            new Thread(() -> {
                XMPPTool.getXmppTool().deleteContact(contactList.get(position));
                handler.sendEmptyMessage(0);
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textContactName;
        View content;
        ImageView imageHead;

        public ViewHolder(View view) {
            super(view);
            textContactName = view.findViewById(R.id.contact_name);
            imageHead = view.findViewById(R.id.head_image);
            content = view.findViewById(R.id.contact);
        }
    }
}
