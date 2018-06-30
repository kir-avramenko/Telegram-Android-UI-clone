package com.shorka.telegramclone_ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shorka.telegramclone_ui.R;
import com.shorka.telegramclone_ui.adapter.MessageListAdapter;
import com.shorka.telegramclone_ui.models.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyrylo Avramenko on 6/22/2018.
 */
public class ContactChatActivity extends AppCompatActivity {

    private final Context mContext = ContactChatActivity.this;
    private static final String TAG = "ContactChatActivity";

    private TextView mTxtChatPersonName, mTxtLastSeen;
    private ImageButton mBtnAttachments, mBtnSend;
    private RecyclerView mRecyclerView;
    private MessageListAdapter mAdapter;
    private View mViewEditTextBox;
    private CharSequence mMessage;
    private ArrayList<Message> mListMessages;

    public static void open(Context context) {
        context.startActivity(new Intent(context, ContactChatActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_chat);
        setDefaultMessages();
        init();
        initEditText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_convo, menu);
        return true;
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.convo_toolbar);
        View viewConvoTop = findViewById(R.id.convo_header_view_top);
        mTxtChatPersonName = viewConvoTop.findViewById(R.id.name);
        mTxtLastSeen = viewConvoTop.findViewById(R.id.last_seen);

        mTxtChatPersonName.setTextColor(getResources().getColor(R.color.colorWhite));
        mTxtLastSeen.setTextColor(getResources().getColor(R.color.colorWhite));

        mViewEditTextBox = findViewById(R.id.snippet_edit_text_box);

        mBtnAttachments = mViewEditTextBox.findViewById(R.id.convo_attachment_btn);
        mBtnSend = mViewEditTextBox.findViewById(R.id.convo_send_btn);
        enableBtnSend(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessages();
            }
        });

        mRecyclerView = findViewById(R.id.convo_recycler_view_messages);
        mAdapter = new MessageListAdapter(mContext, mListMessages);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initEditText() {
        final EditText editText = mViewEditTextBox.findViewById(R.id.convo_message_edittext);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d(TAG, "onTextChanged: s: " + s + "\n start: " + start + " _before: " + before + " _count: " + count);
//                int height = editText.getLayoutParams().height+ (int) getResources().getDimension(R.dimen.convo_edittext_scale);
//                editText.getLayoutParams().height = height;

                if (before == 0 && count == 1) {
                    enableBtnSend(true);
                } else if (count == 0) {
                    enableBtnSend(false);
                }

                if (editText.toString().contains("\n")) {
                    Log.d(TAG, "onTextChanged: MAKE SPACEm");
                }

                mMessage = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void enableBtnSend(boolean doEnable) {

        mBtnSend.setVisibility(doEnable ? View.VISIBLE : View.GONE);
        mBtnAttachments.setVisibility(doEnable ? View.GONE : View.VISIBLE);
    }

    private void setDefaultMessages() {

        if (mListMessages == null)
            mListMessages = new ArrayList<>();

        mListMessages.add(new Message("How are you?", null, 123));
        mListMessages.add(new Message("fam", null, 1233));
    }

    private void sendMessages() {

        mListMessages.add(new Message(String.valueOf(mMessage), null, 12));
        mAdapter.notifyItemInserted(mListMessages.size() - 1);

    }

}
