package com.evernews.evernews;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

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
    public boolean  initialTabs(){
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

    public void setAnimation(){
        /**Set animation type**/
        String animationCode=sharedpreferences.getString(Main.ANIMATIONTYPE,"CubeOut");
        if(animationCode.compareTo("CubeOut")==0)
            mViewPager.setPageTransformer(true, new CubeOutTransformer());
        if(animationCode.compareTo("Accordion")==0)
            mViewPager.setPageTransformer(true, new AccordionTransformer());
        if(animationCode.compareTo("RotateDown")==0)
            mViewPager.setPageTransformer(true, new RotateDownTransformer());
        if(animationCode.compareTo("Tablet")==0)
            if(animationCode.compareTo("Tablet")==0)
                mViewPager.setPageTransformer(true, new TabletTransformer());
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
        pendingIntent1 = PendingIntent.getBroadcast(context, 387032, alarmIntent, 0);
        pendingIntent2 = PendingIntent.getBroadcast(context, 387033, alarmIntent, 0);
        pendingIntent3 = PendingIntent.getBroadcast(context, 387034, alarmIntent, 0);


        if(sharedpreferences.getInt(NOTIFICATIONENABLED,-1)==1)
            startAlarm(viewGroup);
        else
            cancelAlarm(viewGroup);

        if(sharedpreferences.getBoolean(NEWCHANNELADDED,false)) {
            Snackbar snackbar = Snackbar.make(viewGroup, "Channel Updated.", Snackbar.LENGTH_LONG);
            snackbar.show();
            TabAdderWithoutRefresh();
        }

        /**RESTORE BRIGHTNESS**/
        {
            float arg1=sharedpreferences.getFloat(Main.SLIDERCURRENT,250);
            float BackLightValue = arg1/100;
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
            layoutParams.screenBrightness = BackLightValue; // Set Value
            getWindow().setAttributes(layoutParams); // Set params
        }
        /**END**/
        setAnimation();
        /**Track Application**/
        AnalyticsApplication application_new = (AnalyticsApplication) getApplication();
        mTracker = application_new.getDefaultTracker();
        mTracker.setScreenName("In Main Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


        /**Reset Long Clicker**/
        ResetOnLongClickListener();
    }

    void ResetOnLongClickListener(){
        tabStrip = (LinearLayout) tabLayout.getChildAt(0);
        int tabPos=tabLayout.getSelectedTabPosition();
        //tabStrip.getChildAt(tabPos).setBackgroundResource(R.drawable.tab_color1);

        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            final int x = i;
            final ArrayList  <String>defaultTabs=new ArrayList<>();
            defaultTabs.add("Top News");
            defaultTabs.add("YouView");
            defaultTabs.add("EverYou");
            defaultTabs.add("Biz");
            defaultTabs.add("Tech");
            defaultTabs.add("India");
            defaultTabs.add("World");
            defaultTabs.add("Entertainment");
            defaultTabs.add("Politics");
            defaultTabs.add("LifeStyle");
            defaultTabs.add("Sports");
            tabStrip.getChildAt(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    if (!defaultTabs.contains(Initilization.addOnList.get(x))) {
                        //final ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse("https://developers.facebook.com")).build();
                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                        builderSingle.setIcon(R.drawable.ic_launcher);
                        builderSingle.setTitle("Remove Tab");
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
                        arrayAdapter.add("Remove " + Initilization.addOnList.get(x));
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
                                                    String RSSUID = "";
                                                    String JsoupResopnse = "";
                                                    int ExceptionCode = 0;       //sucess;
                                                    ProgressDialog progressdlg;

                                                    @Override
                                                    protected void onProgressUpdate(Integer... text) {
                                                        if (text[0] == 1)
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
                                                        //progressdlg.show();
                                                    }

                                                    @Override
                                                    protected String doInBackground(Void... params) {
                                                        try {
                                                            RSSUID = Initilization.getAddOnListRSSID.get(x);
                                                            String RSSNAME = Initilization.addOnList.get(x);
                                                            publishProgress(1);
                                                            Initilization.androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                                                            String xmlUrl = "http://rssapi.psweb.in/everapi.asmx/RemoveNewsTAB?RSSID=" + RSSUID.replace(" ", "") + "&AndroidId=" + Initilization.androidId;
                                                            JsoupResopnse = Jsoup.connect(xmlUrl).ignoreContentType(true).timeout(Initilization.timeout).execute().body();
                                                            int iIndex = JsoupResopnse.indexOf("\">") + 2;
                                                            int eIndex = JsoupResopnse.indexOf("</");
                                                            char jChar[] = JsoupResopnse.toCharArray();
                                                            if (iIndex >= 0 && eIndex >= 0 && eIndex > iIndex)
                                                                JsoupResopnse = JsoupResopnse.copyValueOf(jChar, iIndex, (eIndex - iIndex));
                                                            int JsoupResp = -99;
                                                            try {
                                                                JsoupResp = Integer.valueOf(JsoupResopnse);
                                                            } catch (NumberFormatException e) {
                                                            }
                                                            if (JsoupResp <= 0) {
                                                                ExceptionCode = 2;//Add failure but not connection
                                                            }
                                                        } catch (IOException e) {
                                                            ExceptionCode = 1;    //failure
                                                        }
                                                        ExceptionCode=0;
                                                        return null;
                                                    }

                                                    @Override
                                                    protected void onPostExecute(String link) {
                                                        progressdlg.dismiss();
                                                        progress.setVisibility(View.GONE);
                                                        int deleteNum = 0;
                                                        if (ExceptionCode == 0) {
                                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                                            editor.putBoolean(Main.NEWCHANNELADDED, true);
                                                            editor.apply();
                                                            {
                                                                String path = Initilization.DB_PATH + Initilization.DB_NAME;
                                                                db = SQLiteDatabase.openDatabase(path, null, 0);
                                                                try {
                                                                    deleteNum = db.delete(Initilization.TABLE_NAME, Initilization.RSSURLID + " = " + RSSUID, null);
                                                                    db.delete(Initilization.TABLE_NAME, Initilization.RSSURLID + " = " + RSSUID, null);
                                                                } catch (Exception e) {
                                                                    /****/
                                                                }
                                                                db.close();
                                                            }


                                                            Snackbar snackbar = Snackbar.make(v, "News removed successfully...updates are being changed...(" + deleteNum + " records were removed)", Snackbar.LENGTH_LONG);
                                                            progress.setVisibility(View.GONE);
                                                            snackbar.show();
                                                            TabAdderWithoutRefresh();

                                                        } else {
                                                            Snackbar snackbar = Snackbar.make(v, "Sorry news could not be removed... (Error Code : " + RSSUID + " )(" + deleteNum + " records were removed)", Snackbar.LENGTH_LONG);
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

        calendarN.set(Calendar.HOUR_OF_DAY, d2.getHours()); // For 1 PM or 2 PM
        calendarN.set(Calendar.MINUTE, d2.getMinutes());
        calendarN.set(Calendar.SECOND, 0);

        calendarE.set(Calendar.HOUR_OF_DAY, d3.getHours()); // For 1 PM or 2 PM
        calendarE.set(Calendar.MINUTE, d3.getMinutes());
        calendarE.set(Calendar.SECOND, 0);

        boolean shown=false;
        if(sharedpreferences.getInt(Main.ONALRAMCHANGED1,-1)==1 && sharedpreferences.getInt(NOTIFICATIONENABLED,-1)==1) {
            Log.i("#setMorning", "Going to register Intent.RegisterAlramBroadcast Morning");
            SharedPreferences.Editor editor = sharedpreferences.edit();
            managerM.setRepeating(AlarmManager.RTC_WAKEUP, calendarM.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
            editor.putInt(Main.ONALRAMCHANGED1, 0);
            editor.commit();
            if (!shown){
                Snackbar snackbar = Snackbar.make(view, "Push Notification Time Updated.", Snackbar.LENGTH_LONG);
                snackbar.show();
                shown = true;
            }
        }

        if(sharedpreferences.getInt(Main.ONALRAMCHANGED2,-1)==1 && sharedpreferences.getInt(NOTIFICATIONENABLED,-1)==1) {
            Log.i("#setEvening", "Going to register Intent.RegisterAlramBroadcast Evening");
            SharedPreferences.Editor editor=sharedpreferences.edit();
            managerM.setRepeating(AlarmManager.RTC_WAKEUP, calendarN.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);
            editor.putInt(Main.ONALRAMCHANGED2,0);
            editor.commit();
            if (!shown){
                Snackbar snackbar = Snackbar.make(view, "Push Notification Time Updated.", Snackbar.LENGTH_LONG);
                snackbar.show();
                shown = true;
            }
        }

        if(sharedpreferences.getInt(Main.ONALRAMCHANGED3,-1)==1 && sharedpreferences.getInt(NOTIFICATIONENABLED,-1)==1) {
            Log.i("#setNight", "Going to register Intent.RegisterAlramBroadcast Night");
            SharedPreferences.Editor editor=sharedpreferences.edit();
            managerM.setRepeating(AlarmManager.RTC_WAKEUP, calendarE.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent3);
            editor.putInt(Main.ONALRAMCHANGED3,0);
            editor.commit();
            if (!shown){
                Snackbar snackbar = Snackbar.make(view, "Push Notification Time Updated.", Snackbar.LENGTH_LONG);
                snackbar.show();
                shown = true;
            }
        }
        //Toast.makeText(this, "Push Notification Time Updated.", Toast.LENGTH_SHORT).show();

    }

    public void cancelAlarm(View view) {
        if (managerN != null && pendingIntent1!=null && pendingIntent2!=null && pendingIntent3!=null ) {
            managerN.cancel(pendingIntent1);
            managerN.cancel(pendingIntent3);
            managerN.cancel(pendingIntent2);
            //Toast.makeText(this, "Push Notification Disabled Canceled", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(view, "Push Notification Disabled Canceled", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
    ViewGroup viewGroup=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
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
                Snackbar snackbar = Snackbar.make(viewGroup, "Refresh has started in  background...", Snackbar.LENGTH_LONG);
                snackbar.show();
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
            ReusableFragment fragArray[]=new ReusableFragment[100];
            if(position<Initilization.addOnList.size()) {
                if (position == 1) {
                    if(!sharedpreferences.getBoolean(LOGGEDIN,false)) {
                        return SignUp.newInstance("SignUpInstance");
                    }
                    else if(sharedpreferences.getBoolean(LOGGEDIN,false)){
                        return PostArticle.newInstance("PostArticle","PostArticle_2");
                    }
                }
                fragArray[position] = ReusableFragment.newInstanceRe(Initilization.getAddOnListRSSID.get(position), Initilization.addOnList.get(position));
                return fragArray[position];
            }
            return null;
        }

        @Override
        public int getCount() {
            return Initilization.addOnList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
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
                String fetchLink="http://rssapi.psweb.in/everapi.asmx/LoadALLDefaultNews?AndroidId="+Initilization.androidId;//Over ride but should be Main.androidId
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
                Initilization i=new Initilization();
                i.RefillVariablesFromDatabase();
                try {
                    ReusableFragment.customAdapter.notifyDataSetChanged();
                }catch (Exception e){/**Attempt**/}
                new DeleteRecords().execute();
                //super.onPostExecute(aVoid);
            }
        }
    }


    class GetNewsTaskNoRestart extends AsyncTask<Void,Void,Void>
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
                String fetchLink="http://rssapi.psweb.in/everapi.asmx/LoadALLDefaultNews?AndroidId="+Initilization.androidId;//Over ride but should be Main.androidId
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
            parseResults(content);
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
                //parseResults(content);
                new DeleteRecords().execute();
                //super.onPostExecute(aVoid);
            }
        }
    }

     class DeleteRecords extends AsyncTask<String, String, String> {
        SQLiteDatabase db ;
        String content="";
        int ExceptionCode=0;
        @Override
        protected String doInBackground(String... params) {
            try
            {
                String fetchLink="http://rssapi.psweb.in/everapi.asmx/DeleteNewsFromAPP";//Over ride but should be Main.androidId
                content= Jsoup.connect(fetchLink).ignoreContentType(true).timeout(Initilization.timeout).execute().body();
                Log.d("Link_fetch",content);
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
            return("");
        }
        @Override
        protected void onPostExecute(String result) {
            if(ExceptionCode==0) {
                ArrayList  <String>newsIDSList=new ArrayList<>();
                String removables="";
                try {
                    removables  = content.substring(content.indexOf("<Column1>") + 9, content.indexOf("</Column1>"));
                    //Log.d("newsIDS_removables", removables+"");
                    String[] newsIDS = removables.split(",");
                    for(int i=0;i<newsIDS.length;i++) {
                        newsIDSList.add(newsIDS[i]);
                        //Log.d("newsIDS", newsIDS[i]+"");
                    }
                } catch (Exception e) {
                    //Means invalid data from server
                }
                int deletNum=0;
                for(int i=0;i<newsIDSList.size();i++) {
                    int t=0;
                    try {
                        t = db.delete(Initilization.TABLE_NAME, Initilization.NEWSID + " = " +"'"+ newsIDSList.get(i)+"'", null);

                    }catch (Exception e){
                        //Log.d("delete_que_error",e.toString()+"");
                    }
                    deletNum=t+deletNum;

                }
                //Log.d("delete_query",deletNum+"");
                //Toast.makeText(context,"Records deleted " + deletNum,Toast.LENGTH_LONG).show();
            }
            db.close();
        }
        @Override
        protected void onPreExecute() {
            String path=Initilization.DB_PATH+Initilization.DB_NAME;
            db=SQLiteDatabase.openDatabase(path,null,0);
        }
    }

    public void parseResults(String response)
    {
        Initilization.resultArrayLength=0;
        ContentValues values = new ContentValues();
        String path=Initilization.DB_PATH+Initilization.DB_NAME;
        SQLiteDatabase db=SQLiteDatabase.openDatabase(path,null,0);
        /**Clear off resultArray**/
        for(int i=0;i<10000;i++){
            for(int j=0;j<15;j++){
                Initilization.resultArray[i][j]="NULL";
            }
        }
        /**END**/
        /**Clear adOnList and etc**/
        mSectionsPagerAdapter.notifyDataSetChanged();
        Initilization.addOnList.clear();
        Initilization.addOnListTOCompare.clear();
        Initilization.getAddOnListRSSID.clear();
        for (int i = 0; i < 20; i++) {
            mSectionsPagerAdapter.notifyDataSetChanged();
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
                Date date = sdf.parse(Initilization.resultArray[i][Initilization.NewsDate].replace("T"," ").replace("+5:30",""));
                long timeInMillisSinceEpoch = date.getTime();
                Random r = new Random();
                int i1 = r.nextInt(1000);
                long timeInSecondsSinceEpoch = (timeInMillisSinceEpoch / (60))+i1;
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
                        mSectionsPagerAdapter.notifyDataSetChanged();
                        Initilization.addOnList.set(cuDispOrder, Initilization.resultArray[i][Initilization.Category]);
                        Initilization.getAddOnListRSSID.set(cuDispOrder, Initilization.resultArray[i][Initilization.CategoryId]);
                        Initilization.addOnListTOCompare.set(cuDispOrder, Initilization.resultArray[i][Initilization.Category]);
                    }
                    if (!Initilization.addOnListTOCompare.contains(Initilization.resultArray[i][Initilization.CategoryId]) && cuDispOrder == 0) {
                        mSectionsPagerAdapter.notifyDataSetChanged();
                        Initilization.addOnList.add(Initilization.resultArray[i][Initilization.Category]);
                        Initilization.getAddOnListRSSID.add(Initilization.resultArray[i][Initilization.CategoryId]);
                        Initilization.addOnListTOCompare.add(Initilization.resultArray[i][Initilization.CategoryId]);
                    }
                }
            }
            catch (Exception ee){/****/}
        }
        db.close(); // Closing database connection

        mSectionsPagerAdapter.notifyDataSetChanged();
        Initilization.addOnList.add(2, "EverYou");
        Initilization.addOnList.add(3, "YouView");
        Initilization.getAddOnListRSSID.add(2, "NULL");
        Initilization.getAddOnListRSSID.add(3, "NULL");
        Initilization.getAddOnListRSSID.removeAll(Arrays.asList(null, ""));
        mSectionsPagerAdapter.notifyDataSetChanged();
        Initilization.addOnList.removeAll(Arrays.asList(null, ""));
        Initilization.addOnListTOCompare.clear();
        mSectionsPagerAdapter.notifyDataSetChanged();
        initialTabs();
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
            }else{
                Initilization.resultArrayLength++;
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
                Date date = sdf.parse(Initilization.resultArray[i][Initilization.NewsDate].replace("T"," ").replace("+5:30",""));
                long timeInMillisSinceEpoch = date.getTime();
                Random r = new Random();
                int i1 = r.nextInt(1000);
                long timeInSecondsSinceEpoch = (timeInMillisSinceEpoch / (60))+i1;
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


    public void TabAdderWithoutRefresh()
    {
        Main.progress.setVisibility(View.VISIBLE);
        /**Keep my house**/
        Initilization.resultArrayLength = 0;
        ArrayList<String> addOnList = new ArrayList <String>(10);
        /**END**/
        /**Clear off resultArray**/
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 15; j++) {
                Initilization.resultArray[i][j] = "NULL";
            }
        }
        /**END**/
        String path = Initilization.DB_PATH + Initilization.DB_NAME;
        db = SQLiteDatabase.openDatabase(path, null, 0);
        Cursor cur = db.query(Initilization.TABLE_NAME, Initilization.col, null, null, null, null, Initilization.RESERVED_3+" DESC");
        Integer num = cur.getCount();
        setTitle(Integer.toString(num));
        addOnList.clear();
        Initilization.addOnListTOCompare.clear();
        Initilization.getAddOnListRSSID.clear();
        for (int i = 0; i < 20; i++) {
            addOnList.add("");
            Initilization.addOnListTOCompare.add("");
            Initilization.getAddOnListRSSID.add("");
        }

        String currentNewsCategory = "";
        cur.moveToFirst();
        /**END**/
        for (int i = 0; i < num; i++) {
            Initilization.resultArray[i][Initilization.CategoryId] = cur.getString(Initilization.CategoryId);//lets get data to database
            Initilization.resultArray[i][Initilization.Category] = cur.getString(Initilization.Category);
            Initilization.resultArray[i][Initilization.DisplayOrder] = cur.getString(Initilization.DisplayOrder);
            Initilization.resultArray[i][Initilization.RSSTitle] = cur.getString(Initilization.RSSTitle);
            Initilization.resultArray[i][Initilization.RSSURL] = cur.getString(Initilization.RSSURL);
            Initilization.resultArray[i][Initilization.RSSUrlId] = cur.getString(Initilization.RSSUrlId);
            Initilization.resultArray[i][Initilization.NewsId] = cur.getString(Initilization.NewsId);
            Initilization.resultArray[i][Initilization.NewsTitle] = cur.getString(Initilization.NewsTitle);
            Initilization.resultArray[i][Initilization.Summary] = cur.getString(Initilization.Summary);
            Initilization.resultArray[i][Initilization.NewsImage] = cur.getString(Initilization.NewsImage);
            Initilization.resultArray[i][Initilization.NewsDate] = cur.getString(Initilization.NewsDate);
            Initilization.resultArray[i][Initilization.NewsDisplayOrder] = cur.getString(Initilization.NewsDisplayOrder);
            Initilization.resultArray[i][Initilization.CategoryorNews] = cur.getString(Initilization.CategoryorNews);
            Initilization.resultArray[i][Initilization.FullText] = cur.getString(Initilization.FullText);
            Initilization.resultArray[i][Initilization.NewsUrl] = cur.getString(Initilization.NewsUrl);
            Initilization.resultArray[i][Initilization.HTMLDesc] = cur.getString(17);           //dont cange 17
            currentNewsCategory = Initilization.resultArray[i][Initilization.DisplayOrder];

            int cuDispOrder = 0;
            try {
                Initilization.resultArrayLength++;
                cuDispOrder = Integer.parseInt(currentNewsCategory);
                if(Initilization.resultArray[i][Initilization.Category].compareTo("YouView")!=0) {
                    if (!Initilization.addOnListTOCompare.contains(Initilization.resultArray[i][Initilization.Category]) && cuDispOrder != 0) {
                        addOnList.set(cuDispOrder, Initilization.resultArray[i][Initilization.Category]);
                        Initilization.getAddOnListRSSID.set(cuDispOrder, Initilization.resultArray[i][Initilization.CategoryId]);
                        Initilization.addOnListTOCompare.set(cuDispOrder, Initilization.resultArray[i][Initilization.CategoryId]);
                    }
                    if (!Initilization.addOnListTOCompare.contains(Initilization.resultArray[i][Initilization.CategoryId]) && cuDispOrder == 0) {
                        addOnList.add(Initilization.resultArray[i][Initilization.Category]);
                        Initilization.getAddOnListRSSID.add(Initilization.resultArray[i][Initilization.CategoryId]);
                        Initilization.addOnListTOCompare.add(Initilization.resultArray[i][Initilization.CategoryId]);
                    }
                }
                try {
                    cur.moveToNext();
                } catch (Exception e) {/*Index out of bounds*/}
            } catch (Exception ee) {/****/}
        }
        db.close(); // Closing database connection

        Set<String> hs = new LinkedHashSet<>();
        hs.addAll(addOnList);
        addOnList.clear();
        addOnList.addAll(hs);
        addOnList.add(2, "EverYou");
        addOnList.add(3, "YouView");
        Initilization.getAddOnListRSSID.add(2, "NULL");
        Initilization.getAddOnListRSSID.add(3,"NULL");
        Initilization.getAddOnListRSSID.removeAll(Arrays.asList(null, ""));
        addOnList.removeAll(Arrays.asList(null, ""));
        Initilization.addOnListTOCompare.clear();
        Initilization.addOnList.clear();
        try {
            //mSectionsPagerAdapter.notifyDataSetChanged();
        }catch (Exception e){/**ON PURPOSE**/}
        Initilization.addOnList.addAll(addOnList);
        try {
            mSectionsPagerAdapter.notifyDataSetChanged();
        }catch (Exception e){
            int x=0;
            int y=0;
            int z=0;
        }

        initialTabs();

        SharedPreferences.Editor editor=sharedpreferences.edit();
        editor.putBoolean(Main.NEWCHANNELADDED, false);
        editor.apply();
        new CountDownTimer(2000,500){
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                ResetOnLongClickListener();
            }
        }.start();
        Main.progress.setVisibility(View.GONE);
    }
}
