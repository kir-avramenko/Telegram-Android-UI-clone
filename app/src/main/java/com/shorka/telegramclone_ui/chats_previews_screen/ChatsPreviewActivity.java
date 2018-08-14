package com.shorka.telegramclone_ui.chats_previews_screen;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.shorka.telegramclone_ui.DividerCustomPaddingItemDecoration;
import com.shorka.telegramclone_ui.FabScroll;
import com.shorka.telegramclone_ui.Injection;
import com.shorka.telegramclone_ui.R;
import com.shorka.telegramclone_ui.RecyclerItemClickListener;
import com.shorka.telegramclone_ui.Utils;
import com.shorka.telegramclone_ui.activities.ContactChatActivity;
import com.shorka.telegramclone_ui.activities.ContactsActivity;
import com.shorka.telegramclone_ui.db.User;
import com.shorka.telegramclone_ui.settings_screen.SettingsActivity;
import com.shorka.telegramclone_ui.adapter.MessagesGridRecycleViewAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsPreviewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChatPreviewContract.View {

    //region Define global variables
    private final Context mContext = ChatsPreviewActivity.this;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private FloatingActionButton mFab;
    private FabScroll mFabScroll;
    private TextView txtUsername, txtPhoneNumber;
    private MenuItem menuCurrAccount;
    private ChatPreviewContract.UserActionsListener chatActionListener;

    private static final String TAG = "ChatsPreviewActivity";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpUI();
        initPresenter();
    }

    private void setUpUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //region navigation view set-up
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Menu menuNav = navigationView.getMenu();
        menuNav.setGroupVisible(R.id.nav_group_accounts, false);
        menuCurrAccount = menuNav.findItem(R.id.nav_curr_account);

        CircleImageView imageView = new CircleImageView(mContext);
        imageView.setMinimumHeight(62);
        imageView.setMinimumWidth(62);

        imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.profile_default_male));
        menuCurrAccount.setActionView(imageView);

        final View navHeaderView = navigationView.getHeaderView(0);
        ToggleButton toggleBtn = navHeaderView.findViewById(R.id.account_view_icon_button);
        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                menuNav.setGroupVisible(R.id.nav_group_accounts, isChecked);
            }
        });

        txtUsername = navHeaderView.findViewById(R.id.text_username);
        txtPhoneNumber = navHeaderView.findViewById(R.id.text_phonenumber);
        //endregion

    }

    private void initPresenter() {
        chatActionListener = new ChatPreviewPresenter(Injection.provideUserRepo(getApplication()), this);
        chatActionListener.loadChats();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_search:
                Log.d(TAG, "onOptionsItemSelected: Press action_search");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (item.getItemId()) {

            case R.id.nav_groupchat:
                break;

            case R.id.nav_gallery:
                break;

            case R.id.nav_settings:
                Log.d(TAG, "onNavigationItemSelected: press nav_settings");

                drawer.closeDrawer(GravityCompat.START);
                SettingsActivity.open(mContext);
                break;

            case R.id.nav_contacts:
                Log.d(TAG, "onNavigationItemSelected: press nav_contacts");
                drawer.closeDrawer(GravityCompat.START);
                ContactsActivity.open(mContext);
                break;
        }


        return true;
    }

    //region Implements methods from ChatPreviewContract.View
    @Override
    public void showChats(List<User> users) {

        mRecycleView = findViewById(R.id.main_recycler_view_messages);
        mRecycleView.setAdapter(new MessagesGridRecycleViewAdapter(mContext));
        mRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycleView.setNestedScrollingEnabled(false);


        DividerCustomPaddingItemDecoration itemCustomDecor = new DividerCustomPaddingItemDecoration(mContext,
                DividerItemDecoration.VERTICAL,
                Utils.dpToPx(getResources().getDimension(R.dimen.message_image_preview_scale), mContext) -
                        Utils.dpToPx(9, mContext)
        );
        mRecycleView.addItemDecoration(itemCustomDecor);

        mRecycleView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, mRecycleView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: click on pos: " + position + "_  " + view.getId());
                ContactChatActivity.open(mContext);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void showNewMessage() {

    }

    @Override
    public void showChatMessages(long idRecipient) {

    }

    @Override
    public void updateUserDetail(User user) {

//        Log.d(TAG, "updateUserDetail: " + user.name);
        txtUsername.setText(user.name);
        txtPhoneNumber.setText(user.phoneNumber);
        menuCurrAccount.setTitle(user.name);
    }


    //endregion
}
