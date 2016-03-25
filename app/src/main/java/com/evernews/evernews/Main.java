package com.evernews.evernews;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.util.Arrays;

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
    public static String catListArray[][]=new String[10000][7];
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
    public static String ARTICLEFONTSIZE="ARTICLEFONTSIZE";
    public static String APPLICATIONORIENTATION="APPLICATIONORIENTATION";
    public static String ERASETABLE_1="ERASETABLE_1";
    public static String uniqueID="";
    SQLiteDatabase db;
    ShareDialog shareDialog;

    Context context;

    TabLayout tabLayout;
    LinearLayout tabStrip;
    View virtualView;
    int mandetTab=10;


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
            finish();
            finish();
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
    private String extractLogToFile()
    {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo (this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
        }
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        // Make file name - file must be saved to external storage or it wont be readable by
        // the email app.
        String path = Environment.getExternalStorageDirectory() + "/" + "evernews/";
        String fullName = path +"evernews";

        // Extract to file.
        File file = new File (fullName);
        InputStreamReader reader = null;
        FileWriter writer = null;
        try
        {
            // For Android 4.0 and earlier, you will get all app's log output, so filter it to
            // mostly limit it to your app's output.  In later versions, the filtering isn't needed.
            String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
                    "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s" :
                    "logcat -d -v time";

            // get input stream
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new InputStreamReader (process.getInputStream());

            // write output stream
            writer = new FileWriter (file);
            writer.write ("Android version: " +  Build.VERSION.SDK_INT + "\n");
            writer.write ("Device: " + model + "\n");
            writer.write ("App version: " + (info == null ? "(null)" : info.versionCode) + "\n");

            char[] buffer = new char[10000];
            do
            {
                int n = reader.read (buffer, 0, buffer.length);
                if (n == -1)
                    break;
                writer.write (buffer, 0, n);
            } while (true);

            reader.close();
            writer.close();
        }
        catch (IOException e)
        {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e1) {
                }

            // You might want to write a failure message to the log here.
            return null;
        }

        return fullName;
    }

    public void shareByOther(String text) {
        try {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
        }catch (Exception e){e.printStackTrace();}
    }

    private Thread.UncaughtExceptionHandler handleAppCrash =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    Log.e("error", ex.toString());

                    StringWriter errors = new StringWriter();
                    ex.printStackTrace(new PrintWriter(errors));
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Error Log", errors.toString());
                    clipboard.setPrimaryClip(clip);
                    shareByOther(ex.toString());
                    finish();
                }
            };

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("In Main Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        if(sharedpreferences.getBoolean(NEWCHANNELADDED,false)) {
            Toast.makeText(context,"Channel change detected...Updating data Wait...",Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(Main.NEWCHANNELADDED, false);
            editor.apply();
            new GetNewsTask().execute();
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

        // mInstance = this;
        //AnalyticsTrackers.initialize(this);
        //AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);

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
                        final ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse("https://developers.facebook.com")).build();
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
                                                            String RSSUID=Initilization.getAddOnListRSSID.get(x);
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
                                                        if(ExceptionCode==0) {
                                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                                            editor.putBoolean(Main.NEWCHANNELADDED, true);
                                                            editor.commit();
                                                            Snackbar snackbar = Snackbar.make(v, "News removed successfully...", Snackbar.LENGTH_LONG);
                                                            progress.setVisibility(View.GONE);
                                                            snackbar.show();
                                                            new CountDownTimer(1000, 1000) {

                                                                public void onTick(long millisUntilFinished) {
                                                                }

                                                                public void onFinish() {
                                                                    recreate();
                                                                }
                                                            }.start();
                                                        }
                                                        else{
                                                            Snackbar snackbar = Snackbar.make(v, "Sorry news could not be removed...", Snackbar.LENGTH_LONG);
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
            case R.id.action_settings:
                Intent i=new Intent(Main.this,Settings.class);
                startActivity(i);
                return true;

            case R.id.action_refresh:
                Toast.makeText(context,"Refresh in background has started...",Toast.LENGTH_LONG).show();
                new GetNewsTask().execute();
                return true;

            case R.id.action_add:
                Intent iii=new Intent(Main.this,AddTab.class);
                iii.putExtra("CALLER", "MAIN");
                startActivity(iii);
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
                else if (position == 2) {
                    return YourView.newInstance("NewInstance","NewInstance");
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
                String fetchLink="http://rssapi.psweb.in/everapi.asmx/LoadXMLDefaultNews?AndroidId="+Initilization.androidId;//Over ride but should be Main.androidId
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
        protected void onPostExecute(Void aVoid) {
            if (ExceptionCode > 0) {
                if (ExceptionCode == 1)
                    Toast.makeText(getApplicationContext(), "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                if (ExceptionCode == 2)
                    Toast.makeText(getApplicationContext(), "Some server related issue occurred..please try again later", Toast.LENGTH_SHORT).show();
            }
            if (content != null) {
                String result = content.toString().replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&");
                parseResults(result);
                recreate();
                super.onPostExecute(aVoid);
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
        for(int i=0;i<15;i++)
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
            int cuDispOrder=0;
            currentNewsCategory=Initilization.resultArray[i][Initilization.DisplayOrder];
            db.insert(Initilization.TABLE_NAME, null, values);
            try {
                Initilization.resultArrayLength++;
                cuDispOrder = Integer.parseInt(currentNewsCategory);

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

    class GetCategoryList extends AsyncTask<Void,Void,Void>
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
                String fetchLink="http://rssapi.psweb.in/everapi.asmx/GetNewsChannelList";//Over ride but should be Main.androidId
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
                String result = content.toString().replaceAll("&lt;", "<").replaceAll("&gt;",">").replaceAll("&amp;","&");
                parseResultsList(result);
            }
            super.onPostExecute(aVoid);
        }
    }

    public void parseResultsList(String response)
    {
        XMLDOMParser parser = new XMLDOMParser();
        InputStream stream = new ByteArrayInputStream(response.getBytes());
        Document doc = parser.getDocument(stream);
        NodeList nodeList = doc.getElementsByTagName("Table");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element e = (Element) nodeList.item(i);
            Main.catListArray[i][0] = (parser.getValue(e, "RSSUrlId"));
            Main.catListArray[i][1] = (parser.getValue(e, "RSSURL"));
            Main.catListArray[i][2] = (parser.getValue(e, "RSSTitle"));
            Main.catListArray[i][3] = (parser.getValue(e, "Detail"));
            Main.catListArray[i][4] = (parser.getValue(e, "Comment"));
            Main.catListArray[i][5] = (parser.getValue(e, "MediaHouse"));
            Main.catListArray[i][6] = (parser.getValue(e, "NewsType"));
            validCategory=true;
        }
    }
}
