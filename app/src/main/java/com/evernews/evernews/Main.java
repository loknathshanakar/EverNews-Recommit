package com.evernews.evernews;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.TabletTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomOutTranformer;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Main extends AppCompatActivity implements SignUp.OnFragmentInteractionListener,PostArticle.OnFragmentInteractionListener {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public static SectionsPagerAdapter mSectionsPagerAdapter;
    public static String catListArray[][]=new String[10000][8];
    public static int catListArrayLength=0;
    public static boolean validCategory=false;
    public static boolean doThisflag=false;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public static ProgressBar progress=null;
    private ViewPager mViewPager;
    public static final String USERLOGINDETAILS = "USERLOGINDETAILS" ;
    private static SharedPreferences sharedpreferences;
    public static String USERNAME="USERNAME";
    public static String USERID="USERID";
    public static String USEREMAIL="USEREMAIL";
    public static String USERPHONENUMBER="USERPHONENUMBER";
    public static String ISREGISTRED="ISREGISTRED";
    public static String LOGGEDIN="LOGGEDIN";
    public static String NEWCHANNELADDED="NEWCHANNELADDED";
    public static String APPLICATIONORIENTATION="APPLICATIONORIENTATION";
    public static String ERASETABLE_1="ERASETABLE_1";
    public static String ANIMATIONTYPE="ANIMATIONTYPE";

    public static String NOTIFICATIONENABLED="NOTIFICATIONENABLED";
    public static String MORNINGTIME="MORNINGTIME";
    public static String NOONTIME="NOONTIME";
    public static String EVENINGTIME="EVENINGTIME";
    public static String ONALRAMCHANGED1="ONALRAMCHANGED1";
    public static String ONALRAMCHANGED2="ONALRAMCHANGED2";
    public static String ONALRAMCHANGED3="ONALRAMCHANGED3";
    public static String FONTSIZE="FONTSIZE";
    public static String SLIDERMAX="SLIDERMAX";
    public static String SLIDERCURRENT="SLIDERCURRENT";
    public static int fontSize=18;
    public static String uniqueID="";
    SQLiteDatabase db;
    ShareDialog shareDialog;

    Context context;

    TabLayout tabLayout;
    LinearLayout tabStrip;
    View virtualView;
    int mandetTab=-1;


    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.app_tracker);
        }
        return mTracker;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
           // finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }
    private boolean initialTabs(){
        int offset=0;
        try {
            for (int i = 0; i < Initilization.addOnList.size(); i++) {
                offset = i + 5;
                if ((offset % 5) == 0) {
                    View v = View.inflate(context, R.layout.layout_tab_1, null);
                    TextView tvt = (TextView) v.findViewById(R.id.tab_tv_1);
                    tvt.setText(Initilization.addOnList.get(i));
                    tvt.setGravity(Gravity.CENTER_VERTICAL);
                    tabLayout.getTabAt(i).setCustomView(v);
                    tabLayout.getTabAt(i).getCustomView().setBackgroundResource(R.drawable.tab_color1);
                    //tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_1_color));
                    //tabLayout.setSelectedTabIndicatorHeight(5);
                }
                if ((offset % 5) == 1) {
                    View v = View.inflate(context, R.layout.layout_tab_2, null);
                    TextView tvt = (TextView) v.findViewById(R.id.tab_tv_2);
                    tvt.setText(Initilization.addOnList.get(i));
                    tvt.setGravity(Gravity.CENTER);
                    tabLayout.getTabAt(i).setCustomView(v);
                    tabLayout.getTabAt(i).getCustomView().setBackgroundResource(R.drawable.tab_color2);
                    //tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_2_color));
                    //tabLayout.setSelectedTabIndicatorHeight(5);
                }
                if ((offset % 5) == 2) {
                    View v = View.inflate(context, R.layout.layout_tab_3, null);
                    TextView tvt = (TextView) v.findViewById(R.id.tab_tv_3);
                    tvt.setText(Initilization.addOnList.get(i));
                    tvt.setGravity(Gravity.CENTER);
                    tabLayout.getTabAt(i).setCustomView(v);
                    tabLayout.getTabAt(i).getCustomView().setBackgroundResource(R.drawable.tab_color3);
                    //tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_3_color));
                    //tabLayout.setSelectedTabIndicatorHeight(5);
                }
                if ((offset % 5) == 3) {
                    View v = View.inflate(context, R.layout.layout_tab_4, null);
                    TextView tvt = (TextView) v.findViewById(R.id.tab_tv_4);
                    tvt.setText(Initilization.addOnList.get(i));
                    tvt.setGravity(Gravity.CENTER);
                    tabLayout.getTabAt(i).setCustomView(v);
                    tabLayout.getTabAt(i).getCustomView().setBackgroundResource(R.drawable.tab_color4);
                    //tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_4_color));
                    //tabLayout.setSelectedTabIndicatorHeight(5);
                }
                if ((offset % 5) == 4) {
                    View v = View.inflate(context, R.layout.layout_tab_5, null);
                    TextView tvt = (TextView) v.findViewById(R.id.tab_tv_5);
                    tvt.setText(Initilization.addOnList.get(i));
                    tvt.setGravity(Gravity.CENTER);
                    tabLayout.getTabAt(i).setCustomView(v);
                    tabLayout.getTabAt(i).getCustomView().setBackgroundResource(R.drawable.tab_color5);
                    //tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_5_color));
                    //tabLayout.setSelectedTabIndicatorHeight(5);
                }
            }
        }catch (Exception e){
            return(false);
        }
        return(true);
    }

    public void resetOtherTabs(int skipTab){
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            if(i==skipTab) {
                continue;
            }
            else{
                tabStrip.getChildAt(i).setBackgroundResource(R.drawable.tab_color1_rised_uncolored);
            }
        }
    }
    public void shareByOther(String text) {
        try {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
        }catch (Exception e){e.printStackTrace();}
    }

    public void setAnimation(){
        /**Set animation type**/
        String animationCode=sharedpreferences.getString(Main.ANIMATIONTYPE,"CubeOut");
        if(animationCode.compareTo("CubeOut")==0)
            mViewPager.setPageTransformer(true, new CubeOutTransformer());
        if(animationCode.compareTo("Accordion")==0)
            mViewPager.setPageTransformer(true, new AccordionTransformer());
        //if(animationCode.compareTo("BackgroundToForeground")==0)
            //mViewPager.setPageTransformer(true, new BackgroundToForegroundTransformer());
        //if(animationCode.compareTo("CubeIn")==0)
            //mViewPager.setPageTransformer(true, new CubeInTransformer());
        //if(animationCode.compareTo("DepthPage")==0)
            //mViewPager.setPageTransformer(true, new DepthPageTransformer());
        //if(animationCode.compareTo("FlipHorizontal")==0)
            //mViewPager.setPageTransformer(true, new FlipHorizontalTransformer());
        //if(animationCode.compareTo("FlipVertical")==0)
           // mViewPager.setPageTransformer(true, new FlipVerticalTransformer());
        ////if(animationCode.compareTo("ForegroundToBackground")==0)
            //mViewPager.setPageTransformer(true, new ForegroundToBackgroundTransformer());
        if(animationCode.compareTo("RotateDown")==0)
            mViewPager.setPageTransformer(true, new RotateDownTransformer());
        //if(animationCode.compareTo("RotateUp")==0)
            //mViewPager.setPageTransformer(true, new RotateUpTransformer());
        //if(animationCode.compareTo("ScaleInOut")==0)
            //mViewPager.setPageTransformer(true, new ScaleInOutTransformer());
        //if(animationCode.compareTo("Stack")==0)
            //mViewPager.setPageTransformer(true, new StackTransformer());
        if(animationCode.compareTo("Tablet")==0)
            if(animationCode.compareTo("Tablet")==0)
                mViewPager.setPageTransformer(true, new TabletTransformer());
        //if(animationCode.compareTo("ZoomIn")==0)
            //mViewPager.setPageTransformer(true, new ZoomInTransformer());
        //if(animationCode.compareTo("ZoomOutSlide")==0)
           // mViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
        if(animationCode.compareTo("ZoomOut")==0)
            mViewPager.setPageTransformer(true, new ZoomOutTranformer());
        /**END**/
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent i = context.getPackageManager().getLaunchIntentForPackage( context.getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent1 = PendingIntent.getService(context, 0, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent2 = PendingIntent.getService(context, 0, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent3 = PendingIntent.getService(context, 0, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        if(sharedpreferences.getInt(Main.NOTIFICATIONENABLED,-1)==1 &&(sharedpreferences.getInt(Main.ONALRAMCHANGED1,-1)==1 || sharedpreferences.getInt(Main.ONALRAMCHANGED2,-1)==1 ||sharedpreferences.getInt(Main.ONALRAMCHANGED3,-1)==1)) {
            //startAlarm(viewGroup);
            //Register AlarmManager Broadcast receive.
            firingCal= Calendar.getInstance();
            firingCal.set(Calendar.HOUR, 1); // At the hour you want to fire the alarm
            firingCal.set(Calendar.MINUTE, 35); // alarm minute
            firingCal.set(Calendar.SECOND, 0); // and alarm second

            Calendar calendarM = Calendar.getInstance();
            Calendar calendarN = Calendar.getInstance();
            Calendar calendarE = Calendar.getInstance();

            Date d1=convertStr2Date(sharedpreferences.getString(Main.MORNINGTIME, "00:00 AM"));
            Date d2=convertStr2Date(sharedpreferences.getString(Main.NOONTIME, "00:00 AM"));
            Date d3=convertStr2Date(sharedpreferences.getString(Main.EVENINGTIME,"00:00 AM"));

            calendarM.set(Calendar.HOUR_OF_DAY, d1.getHours()); // For 1 PM or 2 PM
            calendarM.set(Calendar.MINUTE, d1.getMinutes());
            calendarM.set(Calendar.SECOND, 0);
            //calendarM.add(Calendar.DAY_OF_YEAR, 1);

            calendarN.set(Calendar.HOUR_OF_DAY, d2.getHours()); // For 1 PM or 2 PM
            calendarN.set(Calendar.MINUTE, d2.getMinutes());
            calendarN.set(Calendar.SECOND, 0);
            //calendarN.add(Calendar.DAY_OF_YEAR, 1);

            calendarE.set(Calendar.HOUR_OF_DAY, d3.getHours()); // For 1 PM or 2 PM
            calendarE.set(Calendar.MINUTE, d3.getMinutes());
            calendarE.set(Calendar.SECOND, 0);
            //calendarE.add(Calendar.DAY_OF_YEAR, 1);

            long intendedTime = firingCal.getTimeInMillis();
            SharedPreferences.Editor editor=sharedpreferences.edit();
            registerMyAlarmBroadcast();
            if(sharedpreferences.getInt(Main.ONALRAMCHANGED1,-1)==1) {
                alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calendarM.getTimeInMillis(), AlarmManager.INTERVAL_DAY, myPendingIntent1);
                editor.putInt(Main.ONALRAMCHANGED1,0);
                editor.apply();
            }
            if(sharedpreferences.getInt(Main.ONALRAMCHANGED2,-1)==1) {
                alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, calendarN.getTimeInMillis(), AlarmManager.INTERVAL_DAY, myPendingIntent2);
                editor.putInt(Main.ONALRAMCHANGED2,0);
                editor.apply();
            }
            if(sharedpreferences.getInt(Main.ONALRAMCHANGED3,-1)==1) {
                alarmManager3.setRepeating(AlarmManager.RTC_WAKEUP, calendarE.getTimeInMillis(), AlarmManager.INTERVAL_DAY, myPendingIntent3);
                editor.putInt(Main.ONALRAMCHANGED3,0);
                editor.apply();
            }

        }
        else {
            //cancelAlarm(viewGroup);
            if(myBroadcastReceiver!=null) {
                unregisterReceiver(myBroadcastReceiver);
                UnregisterAlarmBroadcast();
            }
        }


        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        setAnimation();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("In Main Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        if(sharedpreferences.getBoolean(NEWCHANNELADDED,false)) {
            Toast.makeText(context,"Channel change detected...Updating data please wait as changes are applied...",Toast.LENGTH_LONG).show();
            new GetNewsTaskRestart().execute();
        }

        /**RESTORE BRIGHTNESS**/
        {
            float arg1=sharedpreferences.getFloat(Main.SLIDERCURRENT,250);
            float BackLightValue = (float)arg1/100;
            int curBrightnessValue=0;
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
            layoutParams.screenBrightness = BackLightValue; // Set Value
            getWindow().setAttributes(layoutParams); // Set params
        }
        /**END**/
    }

    private PendingIntent pendingIntent1,pendingIntent2,pendingIntent3;
    private AlarmManager managerM,managerN,managerE;
    public Date convertStr2Date(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return(convertedDate);
    }
    public void startAlarm(View view) {
        managerM = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        managerN = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        managerE = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);


        Calendar calendarM = Calendar.getInstance();
        Calendar calendarN = Calendar.getInstance();
        Calendar calendarE = Calendar.getInstance();
        Date d1=convertStr2Date(sharedpreferences.getString(Main.MORNINGTIME, "00:00 AM"));
        Date d2=convertStr2Date(sharedpreferences.getString(Main.NOONTIME, "00:00 AM"));
        Date d3=convertStr2Date(sharedpreferences.getString(Main.EVENINGTIME,"00:00 AM"));

        calendarM.set(Calendar.HOUR_OF_DAY, d1.getHours()); // For 1 PM or 2 PM
        calendarM.set(Calendar.MINUTE, d1.getMinutes());
        calendarM.set(Calendar.SECOND, 0);
        calendarM.add(Calendar.DAY_OF_YEAR, 1);

        calendarN.set(Calendar.HOUR_OF_DAY, d2.getHours()); // For 1 PM or 2 PM
        calendarN.set(Calendar.MINUTE, d2.getMinutes());
        calendarN.set(Calendar.SECOND, 0);
        calendarN.add(Calendar.DAY_OF_YEAR, 1);

        calendarE.set(Calendar.HOUR_OF_DAY, d3.getHours()); // For 1 PM or 2 PM
        calendarE.set(Calendar.MINUTE, d3.getMinutes());
        calendarE.set(Calendar.SECOND, 0);
        calendarE.add(Calendar.DAY_OF_YEAR, 1);

        /*managerM.setRepeating(AlarmManager.RTC_WAKEUP, calendarM.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
        managerN.setRepeating(AlarmManager.RTC_WAKEUP, calendarN.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);
        managerE.setRepeating(AlarmManager.RTC_WAKEUP, calendarE.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent3);*/

        managerM.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pendingIntent1);
        managerN.setRepeating(AlarmManager.RTC_WAKEUP, calendarN.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);
        managerE.setRepeating(AlarmManager.RTC_WAKEUP, calendarE.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent3);
        //Toast.makeText(this, "Push notification Enabled", Toast.LENGTH_SHORT).show();
    }

    PendingIntent myPendingIntent1,myPendingIntent2,myPendingIntent3;
    AlarmManager alarmManager1,alarmManager2,alarmManager3;
    BroadcastReceiver myBroadcastReceiver;
    Calendar firingCal;
    private void registerMyAlarmBroadcast()
    {
        Log.i("#log", "Going to register Intent.RegisterAlramBroadcast");

        //This is the call back function(BroadcastReceiver) which will be call when your
        //alarm time will reached.
        myBroadcastReceiver = new AlarmReceiver();

        registerReceiver(myBroadcastReceiver, new IntentFilter("com.evernews.evernews") );
        myPendingIntent1 = PendingIntent.getBroadcast( this, 0, new Intent("com.evernews.evernews"),0 );
        myPendingIntent2 = PendingIntent.getBroadcast( this, 0, new Intent("com.evernews.evernews"),0 );
        myPendingIntent3 = PendingIntent.getBroadcast( this, 0, new Intent("com.evernews.evernews"),0 );
        alarmManager1 = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
        alarmManager2 = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
        alarmManager3 = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
    }
    private void UnregisterAlarmBroadcast()
    {
        if(alarmManager1!=null && myPendingIntent1!=null) {
            try {
                alarmManager1.cancel(myPendingIntent1);
                getBaseContext().unregisterReceiver(myBroadcastReceiver);
            }catch(Exception e){}
        }
    }

    @Override
    protected void onDestroy() {
        if(myBroadcastReceiver!=null)
            unregisterReceiver(myBroadcastReceiver);
        super.onDestroy();
    }

    public void cancelAlarm(View view) {
        if (managerN != null && pendingIntent1!=null ) {
            managerN.cancel(pendingIntent1);
            managerN.cancel(pendingIntent3);
            managerN.cancel(pendingIntent2);
            Toast.makeText(this, "Push Notification Disabled Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        //Thread.setDefaultUncaughtExceptionHandler(handleAppCrash);
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("In Main Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setTitle("");
        context=this;

        /*Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                handleUncaughtException (thread, e);
            }
        });*/
        progress=(ProgressBar)findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        if(validCategory==false)
            new GetCategoryList().execute();



        sharedpreferences = getSharedPreferences(USERLOGINDETAILS, Context.MODE_PRIVATE);
        if(sharedpreferences.getString(Main.APPLICATIONORIENTATION,"A").compareTo("L")==0){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else if(sharedpreferences.getString(Main.APPLICATIONORIENTATION,"A").compareTo("P")==0){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setAnimation();


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        initialTabs();
        virtualView=(View) findViewById(R.id.virtual_tab);
        virtualView.setBackgroundColor(getResources().getColor(R.color.tab_1_color));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                int x = tab.getPosition() + 5;
                if ((x % 5) == 0) {
                    int i = x - 5;
                    tabStrip.getChildAt(i).setBackgroundResource(R.drawable.tab_color1);
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_1_color));
                    virtualView.setBackgroundColor(getResources().getColor(R.color.tab_1_color));
                    tabLayout.setSelectedTabIndicatorHeight(2);
                }
                if ((x % 5) == 1) {
                    int i = x - 5;
                    tabStrip.getChildAt(i).setBackgroundResource(R.drawable.tab_color2);
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_2_color));
                    virtualView.setBackgroundColor(getResources().getColor(R.color.tab_2_color));
                    tabLayout.setSelectedTabIndicatorHeight(2);
                }
                if ((x % 5) == 2) {
                    int i = x - 5;
                    tabStrip.getChildAt(i).setBackgroundResource(R.drawable.tab_color3);
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_3_color));
                    virtualView.setBackgroundColor(getResources().getColor(R.color.tab_3_color));
                    tabLayout.setSelectedTabIndicatorHeight(2);
                }
                if ((x % 5) == 3) {
                    int i = x - 5;
                    tabStrip.getChildAt(i).setBackgroundResource(R.drawable.tab_color4);
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_4_color));
                    virtualView.setBackgroundColor(getResources().getColor(R.color.tab_4_color));
                    tabLayout.setSelectedTabIndicatorHeight(2);
                }
                if ((x % 5) == 4) {
                    int i = x - 5;
                    tabStrip.getChildAt(i).setBackgroundResource(R.drawable.tab_color5);
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.tab_5_color));
                    virtualView.setBackgroundColor(getResources().getColor(R.color.tab_5_color));
                    tabLayout.setSelectedTabIndicatorHeight(2);
                }
                resetOtherTabs(x - 5);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabStrip = (LinearLayout) tabLayout.getChildAt(0);
        int tabPos=tabLayout.getSelectedTabPosition();
        tabStrip.getChildAt(tabPos).setBackgroundResource(R.drawable.tab_color1);

            for (int i = 0; i < tabStrip.getChildCount(); i++) {
            final int x=i;
            tabStrip.getChildAt(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    if (x > mandetTab) {
                        //final ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse("https://developers.facebook.com")).build();
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                        builderSingle.setIcon(R.drawable.ic_launcher);
                        builderSingle.setTitle("Remove Tab");
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
                        arrayAdapter.add("Remove " +Initilization.addOnList.get(x));
                        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builderSingle.setAdapter(
                                arrayAdapter,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0: {
                                                new AsyncTask<Void, Integer, String>() {
                                                    String RSSUID="";
                                                    String JsoupResopnse="";
                                                    int ExceptionCode = 0;       //sucess;
                                                    ProgressDialog progressdlg;
                                                    @Override
                                                    protected void onProgressUpdate(Integer... text) {
                                                        if(text[0]==1)
                                                            progressdlg.setMessage("Removing channel");
                                                    }

                                                    @Override
                                                    protected void onPreExecute() {
                                                        Main.progress.setVisibility(View.VISIBLE);
                                                        progressdlg = new ProgressDialog(context);
                                                        progressdlg.setMessage("Connecting to server...");
                                                        progressdlg.setTitle("Removing channel");
                                                        progressdlg.setCancelable(false);
                                                        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                        progressdlg.setIndeterminate(true);
                                                        progressdlg.show();
                                                    }
                                                    @Override
                                                    protected String doInBackground(Void... params) {
                                                        try {
                                                            RSSUID=Initilization.getAddOnListRSSID.get(x);
                                                            String RSSNAME=Initilization.addOnList.get(x);
                                                            publishProgress(1);
                                                            Initilization.androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                                                            String xmlUrl = "http://rssapi.psweb.in/everapi.asmx/RemoveNewsTAB?RSSID="+RSSUID.replace(" ","")+"&AndroidId="+Initilization.androidId;
                                                            JsoupResopnse= Jsoup.connect(xmlUrl).ignoreContentType(true).timeout(Initilization.timeout).execute().body();
                                                            int iIndex = JsoupResopnse.indexOf("\">") + 2;
                                                            int eIndex = JsoupResopnse.indexOf("</");
                                                            char jChar[] = JsoupResopnse.toCharArray();
                                                            if (iIndex >= 0 && eIndex >= 0 && eIndex > iIndex)
                                                                JsoupResopnse = JsoupResopnse.copyValueOf(jChar, iIndex, (eIndex - iIndex));
                                                            int JsoupResp=-99;
                                                            try{
                                                                JsoupResp=Integer.valueOf(JsoupResopnse);
                                                            }catch (NumberFormatException e){}
                                                            if(JsoupResp<=0){
                                                                ExceptionCode=2;//Add failure but not connection
                                                            }
                                                        }
                                                        catch (IOException e) {
                                                            ExceptionCode=1;    //failure
                                                        }
                                                        return null;
                                                    }
                                                    @Override
                                                    protected void onPostExecute(String link) {
                                                        progressdlg.dismiss();
                                                        progress.setVisibility(View.GONE);
                                                        int deleteNum=0;
                                                        if(ExceptionCode==0) {
                                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                                            editor.putBoolean(Main.NEWCHANNELADDED, true);
                                                            editor.apply();
                                                            {
                                                                String path = Initilization.DB_PATH + Initilization.DB_NAME;
                                                                db = SQLiteDatabase.openDatabase(path, null, 0);
                                                                deleteNum=db.delete(Initilization.TABLE_NAME,Initilization.RSSURLID + " = "+RSSUID,null);
                                                                db.close();
                                                            }
                                                            Snackbar snackbar = Snackbar.make(v, "News removed successfully...updates are being changed...("+deleteNum+" records were removed)", Snackbar.LENGTH_LONG);
                                                            progress.setVisibility(View.GONE);
                                                            snackbar.show();

                                                            new GetNewsTaskRestart().execute();

                                                            /*final ProgressDialog progressdlg = new ProgressDialog(context);
                                                            progressdlg.setMessage("Updating Application");
                                                            progressdlg.setTitle("Updating contents,Please Wait...");
                                                            progressdlg.setCancelable(false);
                                                            progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                            progressdlg.setIndeterminate(true);
                                                            progressdlg.show();
                                                            new CountDownTimer(1000, 1000) {

                                                                public void onTick(long millisUntilFinished) {
                                                                }

                                                                public void onFinish() {
                                                                    new GetNewsTaskRestart().execute();
                                                                    progressdlg.dismiss();
                                                                }
                                                            }.start();*/

                                                        }
                                                        else{
                                                            Snackbar snackbar = Snackbar.make(v, "Sorry news could not be removed... (Error Code : "+RSSUID+" )("+deleteNum+" records were removed)", Snackbar.LENGTH_LONG);
                                                            snackbar.show();
                                                            progress.setVisibility(View.GONE);
                                                        }
                                                    }
                                                }.execute();
                                            }
                                                break;
                                        }
                                    }
                                });
                        builderSingle.show();
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_brightness:
                LayoutInflater linf = LayoutInflater.from(context);
                final View inflator = linf.inflate(R.layout.brightness_dialog, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(inflator).show();
                final SharedPreferences.Editor editor=sharedpreferences.edit();
                builder.setCancelable(true);
                SeekBar sk=(SeekBar)inflator.findViewById(R.id.seekBar);
                editor.putFloat(Main.SLIDERMAX,sk.getMax());
                editor.apply();
                final TextView tv=(TextView)inflator.findViewById(R.id.textView);
                {
                    float arg1=sharedpreferences.getFloat(Main.SLIDERCURRENT,255);
                    float BackLightValue = (float)arg1/100;
                    WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
                    layoutParams.screenBrightness = BackLightValue; // Set Value
                    getWindow().setAttributes(layoutParams); // Set params
                    sk.setProgress((int)arg1);
                }
                sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                        float BackLightValue = (float)arg1/100;
                        int curBrightnessValue=0;
                        try {
                            curBrightnessValue = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
                        }catch (android.provider.Settings.SettingNotFoundException e){/****/}
                        WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
                        layoutParams.screenBrightness = BackLightValue; // Set Value
                        getWindow().setAttributes(layoutParams); // Set params
                        editor.putFloat(Main.SLIDERCURRENT,arg1);
                        editor.apply();
                    }
                });
                return true;
            case R.id.action_settings:
                Intent i=new Intent(Main.this,Settings.class);
                startActivity(i);
                return true;

            case R.id.action_refresh:
                Toast.makeText(context,"Refresh in background has started...",Toast.LENGTH_SHORT).show();
                new GetNewsTaskRestart().execute();
                return true;

            case R.id.action_add:
                if(validCategory) {
                    Intent iii = new Intent(Main.this, AddTab.class);
                    iii.putExtra("CALLER", "MAIN");
                    startActivity(iii);
                }
                else{
                    Toast.makeText(context,"News channel list is loading please try again after some time",Toast.LENGTH_LONG).show();
                    new GetCategoryList().execute();
                }
                return true;

            case R.id.action_search:
                Intent intent=new Intent(Main.this,Search.class);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public Fragment getItem(int position) {
            //args.putInt(ARG_SECTION_NUMBER, position);
            ReusableFragment fragArray[]=new ReusableFragment[100];
            if(position<Initilization.addOnList.size()) {
                if (position == 1) {
                    if(!sharedpreferences.getBoolean(LOGGEDIN,false)) {
                        return SignUp.newInstance("SignUpInstance");
                    }
                    else if(sharedpreferences.getBoolean(LOGGEDIN,false)){
                        return PostArticle.newInstance("PostArticle","PostArticle_2");
                        //return SignUp.newInstance("SignUpInstance");
                    }
                }
                ////fragArray[position] = ReusableFragment.newInstanceRe(position, Initilization.newsCategories[position][1]);
                fragArray[position] = ReusableFragment.newInstanceRe(position, Initilization.addOnList.get(position));
                return fragArray[position];
            }
            return null;
        }

        @Override
        public int getCount() {
            //Initilization.newsCategoryLength=15;
            //return Initilization.newsCategoryLength;
            return Initilization.addOnList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return(Initilization.newsCategories[position][1]);
            return (Initilization.addOnList.get(position));
        }
    }


    //Non reuse lol
    class GetNewsTaskRestart extends AsyncTask<Void,Void,Void>
    {
        String content;
        int ExceptionCode=0;
        @Override
        protected void onPreExecute()
        {
            progress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                Initilization.androidId = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                String fetchLink="http://rssapi.psweb.in/everapi.asmx/LoadDefaultNews?AndroidId="+Initilization.androidId;//Over ride but should be Main.androidId
                content= Jsoup.connect(fetchLink).ignoreContentType(true).timeout(Initilization.timeout).execute().body();
                content=content.replace("\n","$$$$");
            }
            catch(Exception e)
            {
                if(e instanceof SocketTimeoutException) {
                    ExceptionCode=1;
                    return null;
                }
                if(e instanceof HttpStatusException) {
                    ExceptionCode=2;
                    return null;
                }
                ExceptionCode=3;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progress.setVisibility(View.GONE);
            if (ExceptionCode > 0) {
                if (ExceptionCode == 1)
                    Toast.makeText(context, "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                if (ExceptionCode == 2)
                    Toast.makeText(context, "Some server related issue occurred..please try again later", Toast.LENGTH_SHORT).show();
                if(ExceptionCode ==3)
                    Toast.makeText(context, "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
            if (content != null) {
                parseResultsRefresh(content);
                final ProgressDialog progressdlg = new ProgressDialog(context);
                progressdlg.setMessage("Updating Application");
                progressdlg.setTitle("Updating contents,Please Wait...");
                progressdlg.setCancelable(false);
                progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressdlg.setIndeterminate(true);
                progressdlg.show();
                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        //recreate();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(Main.NEWCHANNELADDED, false);
                        editor.apply();
                        Intent i=new Intent(Main.this,Initilization.class);
                        finish();
                        startActivity(i);
                        progressdlg.dismiss();
                        return;
                    }
                }.start();
                //super.onPostExecute(aVoid);
            }
        }
    }


    //Non reuse lol
    class GetNewsTask extends AsyncTask<Void,Void,Void>
    {
        String content;
        int ExceptionCode=0;
        @Override
        protected void onPreExecute()
        {
            progress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                Initilization.androidId = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                String fetchLink="http://rssapi.psweb.in/everapi.asmx/LoadDefaultNews?AndroidId="+Initilization.androidId;//Over ride but should be Main.androidId
                content= Jsoup.connect(fetchLink).ignoreContentType(true).timeout(Initilization.timeout).execute().body();
                content=content.replace("\n","$$$$");
            }
            catch(Exception e)
            {
                if(e instanceof SocketTimeoutException) {
                    ExceptionCode=1;
                    return null;
                }
                if(e instanceof HttpStatusException) {
                    ExceptionCode=2;
                    return null;
                }
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progress.setVisibility(View.GONE);
            if (ExceptionCode > 0) {
                if (ExceptionCode == 1)
                    Toast.makeText(getApplicationContext(), "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                if (ExceptionCode == 2)
                    Toast.makeText(getApplicationContext(), "Some server related issue occurred..please try again later", Toast.LENGTH_SHORT).show();
            }
            if (content != null) {
                String result = content.toString().replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&");
                parseResults(result);

                final ProgressDialog progressdlg = new ProgressDialog(context);
                progressdlg.setMessage("Updating Application");
                progressdlg.setTitle("Updating contents,Please Wait...");
                progressdlg.setCancelable(false);
                progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressdlg.setIndeterminate(true);
                progressdlg.show();
                new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        /*Intent i = context.getPackageManager().getLaunchIntentForPackage( context.getPackageName() );
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);*/
                        recreate();
                        return;
                    }
                }.start();
                //super.onPostExecute(aVoid);
            }
        }
    }

    public void parseResults(String response)
    {
        Initilization.resultArrayLength=0;
        ContentValues values = new ContentValues();
        String path=Initilization.DB_PATH+Initilization.DB_NAME;
        db=SQLiteDatabase.openDatabase(path,null,0);
        /**Clear off resultArray**/
        for(int i=0;i<10000;i++){
            for(int j=0;j<15;j++){
                Initilization.resultArray[i][j]="NULL";
            }
        }
        /**END**/
        /**Clear adOnList and etc**/
        Initilization.addOnList.clear();
        Initilization.addOnListTOCompare.clear();
        Initilization.getAddOnListRSSID.clear();
        for (int i = 0; i < 20; i++) {
            Initilization.addOnList.add("");
            Initilization.addOnListTOCompare.add("");
            Initilization.getAddOnListRSSID.add("");
        }

        String currentNewsCategory="";
        /**END**/
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(response, "", org.jsoup.parser.Parser.xmlParser());
        for(int i=0;i<16;i++)
        {
            if(i==Initilization.CategoryId) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("CategoryId")) {
                    Initilization.resultArray[index][Initilization.CategoryId]=e.text();
                    index++;
                }
            }
            if(i==Initilization.Category) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("Category")) {
                    Initilization.resultArray[index][Initilization.Category]=e.text();
                    index++;
                }
            }
            if(i==Initilization.DisplayOrder) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("DisplayOrder")) {
                    Initilization.resultArray[index][Initilization.DisplayOrder]=e.text();
                    index++;
                }
            }
            if(i==Initilization.RSSTitle) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSTitle")) {
                    Initilization.resultArray[index][Initilization.RSSTitle]=e.text();
                    index++;
                }
            }
            if(i==Initilization.RSSURL) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSURL")) {
                    Initilization.resultArray[index][Initilization.RSSURL]=e.text();
                    index++;
                }
            }
            if(i==Initilization.RSSUrlId) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSUrlId")) {
                    Initilization.resultArray[index][Initilization.RSSUrlId]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsId ) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsId")) {
                    Initilization.resultArray[index][Initilization.NewsId]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsTitle ) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsTitle")) {
                    Initilization.resultArray[index][Initilization.NewsTitle]=e.text();
                    index++;
                }
            }
            if(i==Initilization.Summary) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("Summary")) {
                    Initilization.resultArray[index][Initilization.Summary]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsImage) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsImage")) {
                    Initilization.resultArray[index][Initilization.NewsImage]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsDate) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsDate")) {
                    Initilization.resultArray[index][Initilization.NewsDate]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsDisplayOrder) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsDisplayOrder")) {
                    Initilization.resultArray[index][Initilization.NewsDisplayOrder]=e.text();
                    index++;
                }
            }
            if(i==Initilization.CategoryorNews) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("CategoryorNews")) {
                    Initilization.resultArray[index][Initilization.CategoryorNews]=e.text();
                    index++;
                }
            }
            if(i==Initilization.FullText) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("FullText")) {
                    Initilization.resultArray[index][Initilization.FullText]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsUrl) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsUrl")) {
                    Initilization.resultArray[index][Initilization.NewsUrl]=e.text();
                    index++;
                }
            }
            if (i == Initilization.HTMLDesc) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("HtmlDescription")) {
                    Initilization.resultArray[index][Initilization.HTMLDesc] = e.text();
                    index++;
                }
            }
        }

        for (int i = 0; i < 10000; i++) {
            if(Initilization.resultArray[i][Initilization.CategoryId].contains("NULL")||Initilization.resultArray[i][Initilization.NewsId].contains("NULL")||Initilization.resultArray[i][Initilization.FullText].contains("NULL")){
                continue;
            }
            values.put(Initilization.CATEGORYID,Initilization.resultArray[i][Initilization.CategoryId]);
            values.put(Initilization.CATEGORYNAME,Initilization.resultArray[i][Initilization.Category]);
            values.put(Initilization.DISPLAYORDER,Initilization.resultArray[i][Initilization.DisplayOrder]);
            values.put(Initilization.RSSTITLE,Initilization.resultArray[i][Initilization.RSSTitle]);
            values.put(Initilization.RSSURL_DB,Initilization.resultArray[i][Initilization.RSSURL]);
            values.put(Initilization.RSSURLID,Initilization.resultArray[i][Initilization.RSSUrlId]);
            values.put(Initilization.NEWSID,Initilization.resultArray[i][Initilization.NewsId]);
            values.put(Initilization.NEWSTITLE,Initilization.resultArray[i][Initilization.NewsTitle]);
            values.put(Initilization.SUMMARY,Initilization.resultArray[i][Initilization.Summary]);
            values.put(Initilization.NEWSIMAGE,Initilization.resultArray[i][Initilization.NewsImage]);
            values.put(Initilization.NEWSDATE,Initilization.resultArray[i][Initilization.NewsDate]);
            values.put(Initilization.NEWSDISPLAYORDER,Initilization.resultArray[i][Initilization.NewsDisplayOrder]);
            values.put(Initilization.CATEGORYORNEWS,Initilization.resultArray[i][Initilization.CategoryorNews]);
            values.put(Initilization.FULLTEXT, Initilization.resultArray[i][Initilization.FullText]);
            values.put(Initilization.NEWSURL, Initilization.resultArray[i][Initilization.NewsUrl]);
            values.put(Initilization.RESERVED_4, Initilization.resultArray[i][Initilization.HTMLDesc]);

            if(Initilization.resultArray[i][Initilization.CategoryId].compareTo("2")!=0)
                values.put(Initilization.RESERVED_2, Initilization.resultArray[i][Initilization.NewsId]);
            else
                values.put(Initilization.RESERVED_2, "SOMERANDOMTEXT"+Initilization.resultArray[i][Initilization.NewsId]);

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                Date date = sdf.parse(Initilization.resultArray[i][Initilization.NewsDate]);
                long timeInMillisSinceEpoch = date.getTime();
                long timeInSecondsSinceEpoch = timeInMillisSinceEpoch / (60);
                values.put(Initilization.RESERVED_3, timeInSecondsSinceEpoch);
            }catch(ParseException e){
                values.put(Initilization.RESERVED_3,0);
            }

            int cuDispOrder=0;

            currentNewsCategory=Initilization.resultArray[i][Initilization.DisplayOrder];

            db.insert(Initilization.TABLE_NAME, null, values);

            try {
                Initilization.resultArrayLength++;
                cuDispOrder = Integer.parseInt(currentNewsCategory);
                if (Initilization.resultArray[i][Initilization.Category].compareTo("YouView") != 0) {
                    if (!Initilization.addOnListTOCompare.contains(Initilization.resultArray[i][Initilization.Category]) && cuDispOrder != 0) {
                        Initilization.addOnList.set(cuDispOrder, Initilization.resultArray[i][Initilization.Category]);
                        Initilization.getAddOnListRSSID.set(cuDispOrder, Initilization.resultArray[i][Initilization.RSSUrlId]);
                        Initilization.addOnListTOCompare.set(cuDispOrder, Initilization.resultArray[i][Initilization.Category]);
                    }
                    if (!Initilization.addOnListTOCompare.contains(Initilization.resultArray[i][Initilization.CategoryId]) && cuDispOrder == 0) {
                        Initilization.addOnList.add(Initilization.resultArray[i][Initilization.Category]);
                        Initilization.getAddOnListRSSID.add(Initilization.resultArray[i][Initilization.RSSUrlId]);
                        Initilization.addOnListTOCompare.add(Initilization.resultArray[i][Initilization.CategoryId]);
                    }
                }
            }
            catch (Exception ee){/****/}
        }
        db.close(); // Closing database connection

        Initilization.addOnList.add(2, "EverYou");
        Initilization.addOnList.add(3, "YouView");
        Initilization.getAddOnListRSSID.add(2, "NULL");
        Initilization.getAddOnListRSSID.add(3, "NULL");
        Initilization.getAddOnListRSSID.removeAll(Arrays.asList(null, ""));
        Initilization.addOnList.removeAll(Arrays.asList(null, ""));
        Initilization.addOnListTOCompare.clear();
    }


    public void parseResultsRefresh(String response)
    {
        Initilization.resultArrayLength=0;
        ContentValues values = new ContentValues();
        String path=Initilization.DB_PATH+Initilization.DB_NAME;
        db=SQLiteDatabase.openDatabase(path,null,0);
        /**Clear off resultArray**/
        for(int i=0;i<10000;i++){
            for(int j=0;j<15;j++){
                Initilization.resultArray[i][j]="NULL";
            }
        }
        /**END**/
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(response, "", org.jsoup.parser.Parser.xmlParser());
        for(int i=0;i<16;i++)
        {
            if(i==Initilization.CategoryId) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("CategoryId")) {
                    Initilization.resultArray[index][Initilization.CategoryId]=e.text();
                    index++;
                }
            }
            if(i==Initilization.Category) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("Category")) {
                    Initilization.resultArray[index][Initilization.Category]=e.text();
                    index++;
                }
            }
            if(i==Initilization.DisplayOrder) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("DisplayOrder")) {
                    Initilization.resultArray[index][Initilization.DisplayOrder]=e.text();
                    index++;
                }
            }
            if(i==Initilization.RSSTitle) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSTitle")) {
                    Initilization.resultArray[index][Initilization.RSSTitle]=e.text();
                    index++;
                }
            }
            if(i==Initilization.RSSURL) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSURL")) {
                    Initilization.resultArray[index][Initilization.RSSURL]=e.text();
                    index++;
                }
            }
            if(i==Initilization.RSSUrlId) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSUrlId")) {
                    Initilization.resultArray[index][Initilization.RSSUrlId]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsId ) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsId")) {
                    Initilization.resultArray[index][Initilization.NewsId]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsTitle ) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsTitle")) {
                    Initilization.resultArray[index][Initilization.NewsTitle]=e.text();
                    index++;
                }
            }
            if(i==Initilization.Summary) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("Summary")) {
                    Initilization.resultArray[index][Initilization.Summary]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsImage) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsImage")) {
                    Initilization.resultArray[index][Initilization.NewsImage]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsDate) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsDate")) {
                    Initilization.resultArray[index][Initilization.NewsDate]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsDisplayOrder) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsDisplayOrder")) {
                    Initilization.resultArray[index][Initilization.NewsDisplayOrder]=e.text();
                    index++;
                }
            }
            if(i==Initilization.CategoryorNews) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("CategoryorNews")) {
                    Initilization.resultArray[index][Initilization.CategoryorNews]=e.text();
                    index++;
                }
            }
            if(i==Initilization.FullText) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("FullText")) {
                    Initilization.resultArray[index][Initilization.FullText]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsUrl) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsUrl")) {
                    Initilization.resultArray[index][Initilization.NewsUrl]=e.text();
                    index++;
                }
            }
            if (i == Initilization.HTMLDesc) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("HtmlDescription")) {
                    Initilization.resultArray[index][Initilization.HTMLDesc] = e.text();
                    index++;
                }
            }
        }

        for (int i = 0; i < 10000; i++) {
            if(Initilization.resultArray[i][Initilization.CategoryId].contains("NULL")||Initilization.resultArray[i][Initilization.NewsId].contains("NULL")||Initilization.resultArray[i][Initilization.FullText].contains("NULL")){
                continue;
            }
            values.put(Initilization.CATEGORYID,Initilization.resultArray[i][Initilization.CategoryId]);
            values.put(Initilization.CATEGORYNAME,Initilization.resultArray[i][Initilization.Category]);
            values.put(Initilization.DISPLAYORDER,Initilization.resultArray[i][Initilization.DisplayOrder]);
            values.put(Initilization.RSSTITLE,Initilization.resultArray[i][Initilization.RSSTitle]);
            values.put(Initilization.RSSURL_DB,Initilization.resultArray[i][Initilization.RSSURL]);
            values.put(Initilization.RSSURLID,Initilization.resultArray[i][Initilization.RSSUrlId]);
            values.put(Initilization.NEWSID,Initilization.resultArray[i][Initilization.NewsId]);
            values.put(Initilization.NEWSTITLE,Initilization.resultArray[i][Initilization.NewsTitle]);
            values.put(Initilization.SUMMARY,Initilization.resultArray[i][Initilization.Summary]);
            values.put(Initilization.NEWSIMAGE,Initilization.resultArray[i][Initilization.NewsImage]);
            values.put(Initilization.NEWSDATE,Initilization.resultArray[i][Initilization.NewsDate]);
            values.put(Initilization.NEWSDISPLAYORDER,Initilization.resultArray[i][Initilization.NewsDisplayOrder]);
            values.put(Initilization.CATEGORYORNEWS,Initilization.resultArray[i][Initilization.CategoryorNews]);
            values.put(Initilization.FULLTEXT, Initilization.resultArray[i][Initilization.FullText]);
            values.put(Initilization.NEWSURL, Initilization.resultArray[i][Initilization.NewsUrl]);
            values.put(Initilization.RESERVED_4, Initilization.resultArray[i][Initilization.HTMLDesc]);

            if(Initilization.resultArray[i][Initilization.CategoryId].compareTo("2")!=0)
                values.put(Initilization.RESERVED_2, Initilization.resultArray[i][Initilization.NewsId]);
            else
                values.put(Initilization.RESERVED_2, "SOMERANDOMTEXT"+Initilization.resultArray[i][Initilization.NewsId]);

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                Date date = sdf.parse(Initilization.resultArray[i][Initilization.NewsDate]);
                long timeInMillisSinceEpoch = date.getTime();
                long timeInSecondsSinceEpoch = timeInMillisSinceEpoch / (60);
                values.put(Initilization.RESERVED_3, timeInSecondsSinceEpoch);
            }catch(ParseException e){
                values.put(Initilization.RESERVED_3,0);
            }
            db.insert(Initilization.TABLE_NAME, null, values);
        }
        db.close(); // Closing database connection
    }

    public static  class GetCategoryList extends AsyncTask<Void,Void,Void>
    {
        String content;
        int ExceptionCode=0;
        @Override
        protected void onPreExecute()
        {
            //progress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                String fetchLink="http://rssapi.psweb.in/everapi.asmx/GetNewsChannelList";
                content= Jsoup.connect(fetchLink).ignoreContentType(true).timeout(Initilization.timeout).execute().body();
            }
            catch(Exception e)
            {
                if(e instanceof SocketTimeoutException) {
                    ExceptionCode=1;
                    return null;
                }
                if(e instanceof HttpStatusException) {
                    ExceptionCode=2;
                    return null;
                }
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if(content!=null)
            {
                String result = content.replaceAll("&lt;", "<").replaceAll("&gt;",">").replaceAll("&amp;","&");
                parseResultsList(result);
            }
            super.onPostExecute(aVoid);
        }
    }

    public static void parseResultsList(String response)
    {
        catListArrayLength=0;
        for(int i=0;i<10000;i++){
            for(int j=0;j<2;j++){
                Main.catListArray[i][j]="NULL";
            }
        }
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(response, "", org.jsoup.parser.Parser.xmlParser());
        try {
            for (int i = 0; i < 8; i++) {
                if (i == 0) {
                    int index = 0;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSUrlId")) {
                        Main.catListArray[index][0] = e.text();
                        index++;
                    }
                }
                if (i == 1) {
                    int index = 0;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSURL")) {
                        Main.catListArray[index][1] = e.text();
                        index++;
                    }
                }
                if (i == 2) {
                    int index = 0;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSTitle")) {
                        Main.catListArray[index][2] = e.text();
                        index++;
                    }
                }
                if (i == 3) {
                    int index = 0;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("Image")) {
                        Main.catListArray[index][3] = e.text();
                        index++;
                    }
                }
                if (i == 4) {
                    int index = 0;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("Detail")) {
                        Main.catListArray[index][4] = e.text();
                        index++;
                    }
                }
                if (i == 5) {
                    int index = 0;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("Comment")) {
                        Main.catListArray[index][5] = e.text();
                        index++;
                    }
                }
                if (i == 6) {
                    int index = 0;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("MediaHouse")) {
                        Main.catListArray[index][6] = e.text();
                        index++;
                    }
                }
                if (i == 7) {
                    int index = 0;
                    catListArrayLength=0;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsType")) {
                        Main.catListArray[index][7] = e.text();
                        index++;
                        catListArrayLength++;
                    }
                }
            }
            validCategory=true;
        }catch (Exception e){
            validCategory=false;
        }
    }
}
