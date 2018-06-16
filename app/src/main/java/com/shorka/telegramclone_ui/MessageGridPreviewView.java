package com.shorka.telegramclone_ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kyrylo Avramenko on 6/16/2018.
 */
public class MessageGridPreviewView extends RecyclerView.ViewHolder {

    private static final String TAG = "MessageGridPreviewView";

    private TextView title;
    private TextView messageContent;
    private TextView messageSentTime;
    private ImageView pinImage;
    private CircleImageView cotactImage;

    public MessageGridPreviewView(View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.message_title);
        messageContent = itemView.findViewById(R.id.message_content);
        messageSentTime = itemView.findViewById(R.id.message_sent_time);
        pinImage = itemView.findViewById(R.id.pin_image);
        cotactImage = itemView.findViewById(R.id.img_chat_contact);
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(TextView messageContent) {
        this.messageContent = messageContent;
    }

    public CircleImageView getCotactImage() {
        return cotactImage;
    }

    public void setCotactImage(CircleImageView cotactImage) {
        this.cotactImage = cotactImage;
    }

    public TextView getMessageSentTime() {
        return messageSentTime;
    }

    public void setMessageSentTime(TextView messageSentTime) {
        this.messageSentTime = messageSentTime;
    }

    public ImageView getPinImage() {
        return pinImage;
    }

    public void setPinImage(ImageView pinImage) {
        this.pinImage = pinImage;
    }
}
