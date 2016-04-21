package com.evernews.evernews;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.acra.ACRA;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;


public class Initilization extends AppCompatActivity {
    public static final int CategoryId = 0;
    public static final int Category = 1;
    public static final int DisplayOrder = 2;
    public static final int RSSTitle = 3;
    public static final int RSSURL = 4;
    public static final int RSSUrlId = 5;
    public static final int NewsId =6;
    public static final int NewsTitle = 7;
    public static final int Summary = 8;
    public static final int NewsImage = 9;
    public static final int NewsDate = 10;
    public static final int NewsDisplayOrder = 11;
    public static final int CategoryorNews = 12;
    public static final int FullText = 13;
    public static final int NewsUrl = 14;
    public static final int HTMLDesc = 15;
    //Database related stuff
    public static final String TABLE_NAME = "FULLNEWS_REV2";                //FULLNEWS (OLDNAME) 2)FULLNEWS_REV1
    public static final String CATEGORYID = "CategoryId";
    public static final String CATEGORYNAME = "Category";
    public static final String DISPLAYORDER = "DisplayOrder";
    public static final String RSSTITLE = "RSSTitle";
    public static final String RSSURL_DB = "RSSURL";
    public static final String RSSURLID = "RSSUrlId";
    public static final String NEWSID = "NewsId";
    public static final String NEWSTITLE = "NewsTitle";
    public static final String SUMMARY = "Summary";
    public static final String NEWSIMAGE = "NewsImage";
    public static final String SUBTITLE = "subtitle";
    public static final String NEWSDATE = "NewsDate";
    public static final String NEWSDISPLAYORDER = "NewsDisplayOrder";
    public static final String CATEGORYORNEWS = "CategoryorNews";
    public static final String FULLTEXT = "FullText";
    public static final String NEWSURL = "NewsUrl";
    public static final String RESERVED_2 = "RESERVED_2";   /**USED FOR NEWS ID**/
    public static final String RESERVED_3 = "RESERVED_3";   /**USED FOR LONG TIME**/
    public static final String RESERVED_4 = "RESERVED_4";   /** USED FOR HTMLDESC**/
    public static final String col[] = {CATEGORYID, CATEGORYNAME, DISPLAYORDER, RSSTITLE, RSSURL_DB, RSSURLID, NEWSID, NEWSTITLE, SUMMARY, NEWSIMAGE, NEWSDATE, NEWSDISPLAYORDER, CATEGORYORNEWS, FULLTEXT, NEWSURL,RESERVED_2,RESERVED_3,RESERVED_4};
    public static long numRows=0;
    public static String SQL_CREATE_ENTRIES ="";
    public static String DB_PATH = "/data/data/com.evernews.evernews/databases/";
    public static String DB_NAME = "";
    public static String androidId="";
    public static int timeout = 30000;
    public static String resultArray[][]=new String[10000][16];
    //public static String newsCategories[][]=new String[100][2];
    public static int resultArrayLength=0;
    public static int newsCategoryLength=0;
    public static ArrayList<String> addOnList = new ArrayList <String>(10);
    public static ArrayList<String> addOnListTOCompare = new ArrayList <String>(10);
    public static ArrayList<String> getAddOnListRSSID = new ArrayList <String>(10);
    private static SharedPreferences sharedpreferences;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_initilization);
        getSupportActionBar().hide();
        DB_NAME=TABLE_NAME;
        String path = DB_PATH + DB_NAME;
        this.deleteDatabase("FULLNEWS");
        this.deleteDatabase("FULLNEWS_REV1");
        sharedpreferences = getSharedPreferences(Main.USERLOGINDETAILS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedpreferences.edit();
        if(sharedpreferences.getInt(Main.NOTIFICATIONENABLED,-1)==-1){
            editor.putInt(Main.NOTIFICATIONENABLED,1);
            editor.putString(Main.MORNINGTIME, "9:00 AM");
            editor.putString(Main.NOONTIME, "12:00 PM");
            editor.putString(Main.EVENINGTIME, "6:00 PM");
            editor.putInt(Main.ONALRAMCHANGED1,1);
            editor.putInt(Main.ONALRAMCHANGED2,1);
            editor.putInt(Main.ONALRAMCHANGED3,1);
            editor.putInt(Main.FONTSIZE,18);
            editor.apply();
        }
        new GetNewsTask().execute();
    }

    public void parseResults(String response) {
        Initilization.resultArrayLength = 0;
        ContentValues values = new ContentValues();
        String path = Initilization.DB_PATH + Initilization.DB_NAME;
        db = SQLiteDatabase.openDatabase(path, null, 0);
        /**Clear off resultArray**/
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 15; j++) {
                Initilization.resultArray[i][j] = "NULL";
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

        String currentNewsCategory = "";
        /**END**/
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(response, "", org.jsoup.parser.Parser.xmlParser());
        for (int i = 0; i < 16; i++) {
            if (i == Initilization.CategoryId) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("CategoryId")) {
                    Initilization.resultArray[index][Initilization.CategoryId] = e.text();
                    index++;
                }
            }
            if (i == Initilization.Category) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("Category")) {
                    Initilization.resultArray[index][Initilization.Category] = e.text();
                    index++;
                }
            }
            if (i == Initilization.DisplayOrder) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("DisplayOrder")) {
                    Initilization.resultArray[index][Initilization.DisplayOrder] = e.text();
                    index++;
                }
            }
            if (i == Initilization.RSSTitle) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSTitle")) {
                    Initilization.resultArray[index][Initilization.RSSTitle] = e.text();
                    index++;
                }
            }
            if (i == Initilization.RSSURL) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSURL")) {
                    Initilization.resultArray[index][Initilization.RSSURL] = e.text();
                    index++;
                }
            }
            if (i == Initilization.RSSUrlId) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSUrlId")) {
                    Initilization.resultArray[index][Initilization.RSSUrlId] = e.text();
                    index++;
                }
            }
            if (i == Initilization.NewsId) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsId")) {
                    Initilization.resultArray[index][Initilization.NewsId] = e.text();
                    index++;
                }
            }
            if (i == Initilization.NewsTitle) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsTitle")) {
                    Initilization.resultArray[index][Initilization.NewsTitle] = e.text();
                    index++;
                }
            }
            if (i == Initilization.Summary) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("Summary")) {
                    Initilization.resultArray[index][Initilization.Summary] = e.text();
                    index++;
                }
            }
            if (i == Initilization.NewsImage) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsImage")) {
                    Initilization.resultArray[index][Initilization.NewsImage] = e.text();
                    index++;
                }
            }
            if (i == Initilization.NewsDate) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsDate")) {
                    Initilization.resultArray[index][Initilization.NewsDate] = e.text();
                    index++;
                }
            }
            if (i == Initilization.NewsDisplayOrder) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsDisplayOrder")) {
                    Initilization.resultArray[index][Initilization.NewsDisplayOrder] = e.text();
                    index++;
                }
            }
            if (i == Initilization.CategoryorNews) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("CategoryorNews")) {
                    Initilization.resultArray[index][Initilization.CategoryorNews] = e.text();
                    index++;
                }
            }
            if (i == Initilization.FullText) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("FullText")) {
                    Initilization.resultArray[index][Initilization.FullText] = e.text();
                    index++;
                }
            }
            if (i == Initilization.NewsUrl) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsUrl")) {
                    Initilization.resultArray[index][Initilization.NewsUrl] = e.text();
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
            if (Initilization.resultArray[i][Initilization.CategoryId].contains("NULL") || Initilization.resultArray[i][Initilization.NewsId].contains("NULL") || Initilization.resultArray[i][Initilization.FullText].contains("NULL")) {
                continue;
            }
            values.put(Initilization.CATEGORYID, Initilization.resultArray[i][Initilization.CategoryId]);
            values.put(Initilization.CATEGORYNAME, Initilization.resultArray[i][Initilization.Category]);
            values.put(Initilization.DISPLAYORDER, Initilization.resultArray[i][Initilization.DisplayOrder]);
            values.put(Initilization.RSSTITLE, Initilization.resultArray[i][Initilization.RSSTitle]);
            values.put(Initilization.RSSURL_DB, Initilization.resultArray[i][Initilization.RSSURL]);
            values.put(Initilization.RSSURLID, Initilization.resultArray[i][Initilization.RSSUrlId]);
            values.put(Initilization.NEWSID, Initilization.resultArray[i][Initilization.NewsId]);
            values.put(Initilization.NEWSTITLE, Initilization.resultArray[i][Initilization.NewsTitle]);
            values.put(Initilization.SUMMARY, Initilization.resultArray[i][Initilization.Summary]);
            values.put(Initilization.NEWSIMAGE, Initilization.resultArray[i][Initilization.NewsImage]);
            values.put(Initilization.NEWSDATE, Initilization.resultArray[i][Initilization.NewsDate]);
            values.put(Initilization.NEWSDISPLAYORDER, Initilization.resultArray[i][Initilization.NewsDisplayOrder]);
            values.put(Initilization.CATEGORYORNEWS, Initilization.resultArray[i][Initilization.CategoryorNews]);
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

            int cuDispOrder = 0;

            currentNewsCategory = Initilization.resultArray[i][Initilization.DisplayOrder];

            db.insert(Initilization.TABLE_NAME, null, values);
            try {
                Initilization.resultArrayLength++;
                cuDispOrder = Integer.parseInt(currentNewsCategory);
                if(Initilization.resultArray[i][Initilization.Category].compareTo("YouView")!=0) {
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
            } catch (Exception ee) {/****/}
        }
        db.close(); // Closing database connection


        Set<String> hs = new LinkedHashSet<>();
        hs.addAll(addOnList);
        addOnList.clear();
        addOnList.addAll(hs);

        Initilization.addOnList.add(2, "EverYou");
        Initilization.addOnList.add(3, "YouView");
        Initilization.getAddOnListRSSID.add(2, "NULL");
        Initilization.getAddOnListRSSID.add(3, "NULL");
        Initilization.getAddOnListRSSID.removeAll(Arrays.asList(null, ""));
        Initilization.addOnList.removeAll(Arrays.asList(null, ""));
        Initilization.addOnListTOCompare.clear();
    }

    public void offlineparseResults()
    {
        /**Keep my house**/
        Initilization.resultArrayLength = 0;
        /**END**/
        /**Clear off resultArray**/
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < 15; j++) {
                Initilization.resultArray[i][j] = "NULL";
            }
        }
        /**END**/
        ContentValues values = new ContentValues();
        String path = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(path, null, 0);
        Cursor cur = db.query(TABLE_NAME, Initilization.col, null, null, null, null, RESERVED_3+" DESC");
        Integer num = cur.getCount();
        setTitle(Integer.toString(num));
        Initilization.addOnList.clear();
        Initilization.addOnListTOCompare.clear();
        Initilization.getAddOnListRSSID.clear();
        for (int i = 0; i < 20; i++) {
            Initilization.addOnList.add("");
            Initilization.addOnListTOCompare.add("");
            Initilization.getAddOnListRSSID.add("");
        }

        String currentNewsCategory = "";
        cur.moveToFirst();
        /**END**/
        for (int i = 0; i < numRows; i++) {
            Initilization.resultArray[i][Initilization.CategoryId] = cur.getString(CategoryId);//lets get data to database
            Initilization.resultArray[i][Initilization.Category] = cur.getString(Category);
            Initilization.resultArray[i][Initilization.DisplayOrder] = cur.getString(DisplayOrder);
            Initilization.resultArray[i][Initilization.RSSTitle] = cur.getString(RSSTitle);
            Initilization.resultArray[i][Initilization.RSSURL] = cur.getString(RSSURL);
            Initilization.resultArray[i][Initilization.RSSUrlId] = cur.getString(RSSUrlId);
            Initilization.resultArray[i][Initilization.NewsId] = cur.getString(NewsId);
            Initilization.resultArray[i][Initilization.NewsTitle] = cur.getString(NewsTitle);
            Initilization.resultArray[i][Initilization.Summary] = cur.getString(Summary);
            Initilization.resultArray[i][Initilization.NewsImage] = cur.getString(NewsImage);
            Initilization.resultArray[i][Initilization.NewsDate] = cur.getString(NewsDate);
            Initilization.resultArray[i][Initilization.NewsDisplayOrder] = cur.getString(NewsDisplayOrder);
            Initilization.resultArray[i][Initilization.CategoryorNews] = cur.getString(CategoryorNews);
            Initilization.resultArray[i][Initilization.FullText] = cur.getString(FullText);
            Initilization.resultArray[i][Initilization.NewsUrl] = cur.getString(NewsUrl);
            Initilization.resultArray[i][Initilization.HTMLDesc] = cur.getString(17);
            currentNewsCategory = Initilization.resultArray[i][Initilization.DisplayOrder];

            int cuDispOrder = 0;
            try {
                Initilization.resultArrayLength++;
                cuDispOrder = Integer.parseInt(currentNewsCategory);
                if(Initilization.resultArray[i][Initilization.Category].compareTo("YouView")!=0) {
                    if (!Initilization.addOnListTOCompare.contains(Initilization.resultArray[i][Initilization.Category]) && cuDispOrder != 0) {
                        Initilization.addOnList.set(cuDispOrder, Initilization.resultArray[i][Initilization.Category]);
                        Initilization.getAddOnListRSSID.set(cuDispOrder, Initilization.resultArray[i][Initilization.RSSUrlId]);
                        Initilization.addOnListTOCompare.set(cuDispOrder, Initilization.resultArray[i][Initilization.CategoryId]);
                    }
                    if (!Initilization.addOnListTOCompare.contains(Initilization.resultArray[i][Initilization.CategoryId]) && cuDispOrder == 0) {
                        Initilization.addOnList.add(Initilization.resultArray[i][Initilization.Category]);
                        Initilization.getAddOnListRSSID.add(Initilization.resultArray[i][Initilization.RSSUrlId]);
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

        Initilization.addOnList.add(2, "EverYou");
        Initilization.addOnList.add(3, "YouView");
        Initilization.getAddOnListRSSID.add(2, "NULL");
        Initilization.getAddOnListRSSID.add(3,"NULL");
        Initilization.getAddOnListRSSID.removeAll(Arrays.asList(null, ""));
        Initilization.addOnList.removeAll(Arrays.asList(null, ""));
        Initilization.addOnListTOCompare.clear();
    }

    class GetNewsTask extends AsyncTask<Void, Integer, Void> {
        int goCode = 0;
        String content;
        int ExceptionCode = 0;
        TextView tv = (TextView) findViewById(R.id.response);

        @Override
        protected void onPreExecute() {
            tv.setText("Connecting...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            if (progress[0] == 0)
                tv.setText("Downloading news for the first time...please wait as it might take some time");
            if (progress[0] == 1)
                tv.setText("Formatting news...please wait");
        }

        @Override
        protected Void doInBackground(Void... params) {
            SQL_CREATE_ENTRIES = "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME + "("
                    + CATEGORYID + " TEXT , "
                    + CATEGORYNAME + " TEXT , " + DISPLAYORDER
                    + " TEXT , " + RSSTITLE + " TEXT , "
                    + RSSURL_DB + " TEXT , " + RSSURLID
                    + " TEXT , " + NEWSID + " TEXT , "
                    + NEWSTITLE + " TEXT , " + SUMMARY
                    + " TEXT , " + NEWSIMAGE + " TEXT , " + NEWSDATE
                    + " TEXT , " + NEWSDISPLAYORDER
                    + " TEXT , " + CATEGORYORNEWS + " TEXT , " + FULLTEXT
                    + " TEXT , " + NEWSURL
                    + " TEXT , " + RESERVED_2 + " TEXT UNIQUE , " + RESERVED_3
                    + " LONG , " + RESERVED_4 + " TEXT );";

            db = openOrCreateDatabase(TABLE_NAME, MODE_PRIVATE, null);
            db.execSQL(SQL_CREATE_ENTRIES);
           // db.rawQuery("SELECT * FROM '"+TABLE_NAME+"' ORDER BY '"+RESERVED_3+"' ASC;", new String[] {});
            Cursor oldestDateCursor = db.query(Initilization.TABLE_NAME, null, null, null, null, null, RESERVED_3+" ASC LIMIT 0");
            if (oldestDateCursor.moveToFirst())
            {
                String date = oldestDateCursor.getColumnName(oldestDateCursor.getColumnIndex("date_column"));
            }
            oldestDateCursor.close();
            numRows = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
            if (numRows < 50) {
                try {
                    publishProgress(0);
                    androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    String fetchLink = "http://rssapi.psweb.in/everapi.asmx/LoadALLDefaultNews?AndroidId=" + androidId;//Over ride but should be Main.androidId
                    goCode = 1;
                    content = Jsoup.connect(fetchLink).ignoreContentType(true).timeout(timeout + timeout).execute().body();
                    content=content.replace("\n","$$$$");

                } catch (Exception e) {
                    if (e instanceof SocketTimeoutException) {
                        ExceptionCode = 1;
                        return null;
                    }
                    if (e instanceof HttpStatusException) {
                        ExceptionCode = 2;
                        return null;
                    }
                    e.printStackTrace();
                }
            } else {
                goCode = 2;
            }
            db.close();
            publishProgress(1);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (ExceptionCode > 0) {
                if (ExceptionCode == 1)
                    Toast.makeText(getApplicationContext(), "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                if (ExceptionCode == 2)
                    Toast.makeText(getApplicationContext(), "Some server related issue occurred..please try again later", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Unknown exception,program will now exit", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            if ((content != null && goCode == 1) || (content != null && sharedpreferences.getBoolean(Main.NEWCHANNELADDED, false))) {
                String result = content.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&");
                //Log.d("response", result);
                //after getting the response we have to parse it
                parseResults(result);
                //SharedPreferences.Editor editor = sharedpreferences.edit();
                //editor.putBoolean(Main.NEWCHANNELADDED, false);
                //editor.commit();
                Intent main = new Intent(Initilization.this, Main.class);
                startActivity(main);
                finish();
                return;
            } else if (goCode == 2) {
                offlineparseResults();
                Intent main = new Intent(Initilization.this, Main.class);
                startActivity(main);
                finish();
                return;
            } else {
                Toast.makeText(getApplicationContext(), "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                finish();
            }
            super.onPostExecute(aVoid);
        }
    }
}

