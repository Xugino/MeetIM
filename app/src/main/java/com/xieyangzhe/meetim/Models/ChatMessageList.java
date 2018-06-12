package com.xieyangzhe.meetim.Models;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.github.bassaer.chatmessageview.model.Message;
import com.xieyangzhe.meetim.Utils.BitmapAndStringUtils;
import com.xieyangzhe.meetim.Utils.PictureTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joseph on 5/25/18.
 */

public class ChatMessageList {
    private List<ChatMessage> messageList;

    public ChatMessageList() {
        this.messageList = new ArrayList<>();
    }

    public List<ChatMessage> getMessageList() {
        return messageList;
    }

    public ArrayList<Message> getMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        for (ChatMessage chatMessage : messageList) {
            messages.add(convertToMessage(chatMessage));
        }
        return messages;
    }

    public void saveChatMessages(List<Message> messages) {
            for (Message message: messages) {
                messageList.add(convertToChatMessage(message));
            }
    }

    public void add(Message message) {
        messageList.add(convertToChatMessage(message));
    }

    public void addChatMessage(ChatMessage chatMessage) {
        messageList.add(chatMessage);
    }

    public Message get(int index) {
        return convertToMessage(messageList.get(index));
    }

    public ChatMessage convertToChatMessage(Message message) {
        ChatMessage chatMessage;
        if (message.getType() == Message.Type.PICTURE) {
            chatMessage = new ChatMessage(
                    message.getText(),
                    message.getUser().getName(),
                    message.getSendTime(),
                    message.isRight(),
                    true);
        } else {
            chatMessage = new ChatMessage(
                    message.getText(),
                    message.getUser().getName(),
                    message.getSendTime(),
                    message.isRight(),
                    false);
        }
        return chatMessage;
    }

    public Message convertToMessage(ChatMessage chatMessage) {
        Contact contact = new Contact("", chatMessage.getUsername(), "");
        Message message;
        if (chatMessage.isPic()) {
            Log.d("asdasdasd", "convertToMessage: " + PictureTool.DIR + chatMessage.getMsgBody());
            message = new Message.Builder()
                    .setUser(contact)
                    .setRight(chatMessage.isMe())
                    .setType(Message.Type.PICTURE)
                    .setPicture(BitmapFactory.decodeFile(PictureTool.DIR + chatMessage.getMsgBody()))
                    .setSendTime(chatMessage.getMsgTime())
                    .build();
        } else {
            message = new Message.Builder()
                    .setUser(contact)
                    .setRight(chatMessage.isMe())
                    .setText(chatMessage.getMsgBody())
                    .setSendTime(chatMessage.getMsgTime())
                    .build();
        }
        message.setIconVisibility(true);
        message.hideIcon(false);
        return message;
    }

    public int size() {
        return messageList.size();
    }
}
