package ar.edu.itba.iot.iot_android.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import ar.edu.itba.iot.iot_android.R;

import com.mindorks.placeholderview.PlaceHolderView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ar.edu.itba.iot.iot_android.controller.UserController;
import ar.edu.itba.iot.iot_android.model.Device;
import ar.edu.itba.iot.iot_android.model.User;
import ar.edu.itba.iot.iot_android.view.DrawerHeader;
import ar.edu.itba.iot.iot_android.view.DrawerMenuItem;
import ar.edu.itba.iot.iot_android.view.MyAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] devicesNames = null;
    private String[] targetTemps = null;
    private String[] currentTemps = null;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private UserController userController;
    private User user;

    private Observer userChange = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            if(((String) arg).equals("token")) userController.getFullUserData();
            else if(((String) arg).equals("id")) userController.getDevices();
            else if(((String) arg).equals("deviceList")) populateAdapter();
        }
    };

    private Observer deviceChange = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            if(devicesNames == null || targetTemps == null || currentTemps == null) return;

            Device device = (Device)o;
            String id = device.getId();
            double temperature = device.getTemperature();
            double targetTemperature = device.getTargetTemperature();
            Log.d("-", "---------------");
            Log.d("new temperature", id + ": " + temperature);

            List<Device> devices = user.getDevices();

            int index = devices.indexOf(device);
            devicesNames[index] = id;
            currentTemps[index] = Double.toString(temperature);
            targetTemps[index] = Double.toString(targetTemperature);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setupDrawer();

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        //setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        View v = this.findViewById(R.id.addDevice);
        v.setOnClickListener(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        user = new User("julian", "julian");
        user.addObserver(userChange);

        userController = new UserController(user, prefs, deviceChange);

        userController.login();


        /*Device device = new Device("lalala", 30);
        device.addObserver(deviceChange);
        device.setTemperature(15.34);*/



    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    private void populateAdapter() {
        int n = user.getDevices().size();

        devicesNames = new String[n];
        targetTemps = new String[n];
        currentTemps = new String[n];

        int i = 0;

        for(Device d: user.getDevices()){
            devicesNames[i] = d.getNickname();
            currentTemps[i] = String.valueOf(d.getTemperature());
            targetTemps[i] = String.valueOf(d.getTargetTemperature());
        }



        mAdapter = new MyAdapter(devicesNames, currentTemps, targetTemps);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void setupDrawer(){
        mDrawerView
                .addView(new DrawerHeader())
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_REQUESTS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_MESSAGE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_GROUPS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_NOTIFICATIONS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_TERMS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_SETTINGS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LOGOUT));

        ActionBarDrawerToggle  drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }
}
