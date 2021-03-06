package com.evernews.evernews;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ReusableFragment extends Fragment {
    private static final String TYPE_KEY = "type";
    private static final int REQUESTCODE = 1900;
    private static final String TAB_NAME = "tab_name";
    static List<ItemObject> asyncitems = new ArrayList<>();
    private static GridView gridView;
    private static  String asyncCatId="";
    private static  String asyncNewsId="";
    boolean passKey=false;
    // private static ProgressBar progressBar;
    SQLiteDatabase db;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    String newsTitle;
    String newsLink;
    public static  CustomAdapter customAdapter;
    Context context;
    Button btn;
    List<ItemObject> itemCollection = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private int refrenceCounter=0;

    public ReusableFragment() {
    }

    public static ReusableFragment newInstanceRe(String sectionNumber,String tabName) {
        Bundle args = new Bundle();
        args.putSerializable(TYPE_KEY, sectionNumber);
        args.putSerializable(TAB_NAME, tabName);
        ReusableFragment fragment = new ReusableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("TAG", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String tabName=getArguments().getString(TAB_NAME);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView)rootView.findViewById(R.id.gridview);
        // progressBar=(ProgressBar)rootView.findViewById(R.id.progress_frag);
        //progressBar.setVisibility(View.GONE);
        context=getContext();
        refreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe_view);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetNewsTask().execute();
            }
        });
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridView.setNumColumns(4);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridView.setNumColumns(2);
        }
        List<ItemObject> allItems = getDefaultNews();
        customAdapter = new CustomAdapter(context, itemCollection);
        itemCollection.addAll(0,allItems);
        //customAdapter.notifyDataSetChanged();
        int postionToMaintain= gridView.getFirstVisiblePosition();
        gridView.setAdapter(customAdapter);
        gridView.setSelection(postionToMaintain);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "Position: " + position, Toast.LENGTH_SHORT).show();
                final String newsID = itemCollection.get(position).getNewsID();
                if (newsID == null || newsID.isEmpty()) {
                    try {
                        wait(200);
                    } catch (Exception e) {
                    }
                }

                Intent i = new Intent(getActivity().getBaseContext(), ViewNews.class);
                if (itemCollection.get(position).getCategoryID().compareTo("-1") == 0)
                    i = new Intent(getActivity().getBaseContext(), YouView.class);

                if (newsID == null)
                    i.putExtra("NEWS_ID", newsID + "");
                else
                    i.putExtra("NEWS_ID", newsID);
                i.putExtra("CALLER", "MAIN");
                i.putExtra("SUMMARY",itemCollection.get(position).getnewsSummary());
                i.putExtra("NEWS_TITLE", itemCollection.get(position).getnewsTitle());
                i.putExtra("RSS_TITLE", itemCollection.get(position).getnewsName());
                i.putExtra("HTML_DESC", itemCollection.get(position).getHTMLDesc());
                i.putExtra("FULL_TEXT", itemCollection.get(position).getFullText());
                i.putExtra("NEWS_IMAGE", itemCollection.get(position).getNewsImage());
                i.putExtra("NEWS_DATE", itemCollection.get(position).getnewsDate());
                if (itemCollection.get(position).getCategoryID().compareTo("-1") == 0)
                    i.putExtra("NEWS_LINK", "NULL_WITH_IMAGE");
                else
                    i.putExtra("NEWS_LINK", itemCollection.get(position).getNewsURL());
                startActivity(i);
            }
        });

        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(itemCollection.get(position).getCategoryID().compareTo("-1") == 0)
                    return(false);
                newsTitle = itemCollection.get(position).getnewsTitle();
                newsLink = itemCollection.get(position).getNewsURL();
                final ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse("https://developers.facebook.com")).build();
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle(newsTitle);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                arrayAdapter.add("Facebook");
                arrayAdapter.add("Twitter");
                arrayAdapter.add("Email");
                arrayAdapter.add("Open in browser");
                arrayAdapter.add("Copy link");
                arrayAdapter.add("Share by other means");
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
                                String strName = arrayAdapter.getItem(which);
                                switch (which) {
                                    case 0:
                                        if (ShareDialog.canShow(ShareLinkContent.class)) {
                                            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                                    .setRef(" #EVERNEWS")
                                                    .setContentDescription("Shared via Evernews")
                                                    .setContentTitle(newsTitle + " #EVERNEWS")
                                                    .setContentUrl(Uri.parse(newsLink))
                                                    .build();
                                            shareDialog.show(linkContent);
                                        }
                                        break;
                                    case 1:
                                        tweet();
                                        break;
                                    case 2:
                                        sharebyMail();
                                        break;
                                    case 3:
                                        openInBrowser();
                                        break;
                                    case 4:
                                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText(newsTitle, newsLink);
                                        clipboard.setPrimaryClip(clip);
                                        Toast.makeText(getContext() == null ? context : context, "Link copied to clipboard", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 5:
                                        shareByOther();
                                        break;
                                }
                            }
                        });
                builderSingle.show();
                return (true);
            }
        });


        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            FloatingActionButton fab=(FloatingActionButton) getActivity().findViewById(R.id.fabMain);
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    fab.setVisibility(View.VISIBLE);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getContext(),"Loading more news",Toast.LENGTH_LONG).show();
                            fab.setVisibility(View.GONE);
                            Main.progress.setVisibility(View.VISIBLE);
                            new AsyncTask<Void,Void,String>()
                            {
                                String content;
                                int ExceptionCode=0;
                                @Override
                                protected void onPreExecute()
                                {
                                    asyncitems.clear();
                                    Main.progress.setVisibility(View.VISIBLE);
                                    ExceptionCode=0;
                                    super.onPreExecute();
                                }
                                @Override
                                protected String doInBackground(Void... params)
                                {
                                    try
                                    {
                                        asyncNewsId=itemCollection.get(itemCollection.size()-1).getNewsID();
                                        asyncCatId=itemCollection.get(itemCollection.size()-1).getCategoryID();
                                        Initilization.androidId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                                        String fetchLink="http://rssapi.psweb.in/everapi.asmx/LoadMoreNewsForCategory?CategoryId="+asyncCatId+"&LastNewsId="+asyncNewsId;//+"&NewsChannel="+passKey;//+Initilization.androidId;//Over ride but should be Main.androidId
                                        content= Jsoup.connect(fetchLink).ignoreContentType(true).timeout(Initilization.timeout).execute().body();
                                        content=content.replace("\n", "$$$$");
                                        Log.d("Load_fetchLink", fetchLink);
                                        Log.d("Load_More", content);
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
                                protected void onPostExecute(String link)
                                {
                                    Main.progress.setVisibility(View.VISIBLE);
                                    if(ExceptionCode>0) {
                                        if(ExceptionCode==1)
                                            Toast.makeText(context, "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                                        else if (ExceptionCode == 2)
                                            Toast.makeText(context, "Some server related issue occurred..please try again later", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(context, "Unknown error occurred,check your internet connection", Toast.LENGTH_SHORT).show();
                                        Main.progress.setVisibility(View.GONE);
                                    }
                                    if(content!=null && ExceptionCode==0)
                                    {
                                        String result = content.toString().replaceAll("&lt;", "<").replaceAll("&gt;",">").replaceAll("&amp;","&");
                                        //Log.d("response", result);
                                        //after getting the response we have to parse it
                                        parseResults(result);
                                        int tabNameI=getArguments().getInt(TYPE_KEY);
                                        int resultsID=0;
                                        try{
                                            String num=asyncitems.get(0).getCategoryID();
                                            resultsID=Integer.parseInt(num);
                                        }catch(Exception e){/****/}
                                        if(tabNameI==resultsID){
                                            asyncitems.remove(0);
                                            itemCollection.addAll(asyncitems);
                                            //customAdapter.notifyDataSetChanged();-
                                            //Toast.makeText(getContext(),"More news loaded",Toast.LENGTH_LONG).show();
                                        }
                                        Main.progress.setVisibility(View.GONE);
                                    }
                                }
                            }.execute();
                        }
                    });
                }
                else{
                    fab.setVisibility(View.GONE);           //hide load more
                }
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });
        return rootView;
    }

    private List<ItemObject> getDefaultNews(){
        List<ItemObject> items = new ArrayList<>();
        items.add(new ItemObject("", "", "", "", "","","","","",""));
        int i= getArguments().getInt(TYPE_KEY);
        String tabName=getArguments().getString(TAB_NAME);
        if(i==1)
            return items;
        if(i>=0) {
            for(int j=0;j<Initilization.resultArrayLength;j++){
                    if (tabName.compareTo(Initilization.resultArray[j][Initilization.Category]) == 0) {
                        String NewsImage = Initilization.resultArray[j][Initilization.NewsImage];
                        String NewsTitle = Initilization.resultArray[j][Initilization.NewsTitle];
                        String RSSTitle = Initilization.resultArray[j][Initilization.RSSTitle];
                        String NewsId = Initilization.resultArray[j][Initilization.NewsId];
                        String CategoryId = Initilization.resultArray[j][Initilization.CategoryId];
                        String FullText = Initilization.resultArray[j][Initilization.FullText];
                        String NewsUrl = Initilization.resultArray[j][Initilization.NewsUrl];
                        String Summary = Initilization.resultArray[j][Initilization.Summary];
                        String newsDate = Initilization.resultArray[j][Initilization.NewsDate];
                        String HTMLDesc = Initilization.resultArray[j][Initilization.HTMLDesc];
                        int xj=j;
                        items.add(new ItemObject(NewsImage, NewsTitle, RSSTitle, NewsId, CategoryId, FullText, NewsUrl,Summary,newsDate,HTMLDesc));
                        refrenceCounter++;
                        //Need to implement a filter to prevent re adding of data
                        for (int k = 0; k < itemCollection.size(); k++) {
                            if (itemCollection.get(k).getNewsID().compareTo(NewsId) == 0 && items.size()-1>=0 ) {
                                items.remove(items.size() - 1);
                            }
                        }
                    }
            }
        }
        Set<ItemObject> hs = new LinkedHashSet<>();
        hs.addAll(items);
        items.clear();
        items.addAll(hs);
        if(items.size()>=1)
            items.remove(0);
        return items;
    }

    public void parseResultsPerCategory(String response) {
        {
            ContentValues values = new ContentValues();
            String path = Initilization.DB_PATH + Initilization.DB_NAME;
            db = SQLiteDatabase.openDatabase(path, null, 0);
            int beginIndex=Initilization.resultArrayLength;
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(response, "", org.jsoup.parser.Parser.xmlParser());
            for (int i = 0; i < 16; i++) {
                if (i == Initilization.CategoryId) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("CategoryId")) {
                        Initilization.resultArray[index][Initilization.CategoryId] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.Category) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("Category")) {
                        Initilization.resultArray[index][Initilization.Category] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.DisplayOrder) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("DisplayOrder")) {
                        Initilization.resultArray[index][Initilization.DisplayOrder] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.RSSTitle) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSTitle")) {
                        Initilization.resultArray[index][Initilization.RSSTitle] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.RSSURL) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSURL")) {
                        Initilization.resultArray[index][Initilization.RSSURL] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.RSSUrlId) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSUrlId")) {
                        Initilization.resultArray[index][Initilization.RSSUrlId] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.NewsId) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsId")) {
                        Initilization.resultArray[index][Initilization.NewsId] = e.text();
                        Initilization.resultArrayLength++;
                        index++;
                    }
                }
                if (i == Initilization.NewsTitle) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsTitle")) {
                        Initilization.resultArray[index][Initilization.NewsTitle] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.Summary) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("Summary")) {
                        Initilization.resultArray[index][Initilization.Summary] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.NewsImage) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsImage")) {
                        Initilization.resultArray[index][Initilization.NewsImage] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.NewsDate) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsDate")) {
                        Initilization.resultArray[index][Initilization.NewsDate] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.NewsDisplayOrder) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsDisplayOrder")) {
                        Initilization.resultArray[index][Initilization.NewsDisplayOrder] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.CategoryorNews) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("CategoryorNews")) {
                        Initilization.resultArray[index][Initilization.CategoryorNews] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.FullText) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("FullText")) {
                        Initilization.resultArray[index][Initilization.FullText] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.NewsUrl) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsUrl")) {
                        Initilization.resultArray[index][Initilization.NewsUrl] = e.text();
                        index++;
                    }
                }
                if (i == Initilization.HTMLDesc) {
                    int index = beginIndex;
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("HtmlDescription")) {
                        Initilization.resultArray[index][Initilization.HTMLDesc] = e.text();
                        index++;
                    }
                }
            }
            for (int i = beginIndex; i < 10000; i++) {
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
                db.insert(Initilization.TABLE_NAME, null, values);
            }
            db.close(); // Closing database connection
        }
    }



    //Non reuse lol
    public void parseResults(String response)
    {
        int tabName=getArguments().getInt(TYPE_KEY);
        String NewsImage="",NewsTitle="",RSSTitle="",NewsId="",CategoryId="",FullText="",NewsUrl="",NewsSummary="",NewsDate="",HTMLDesc="";
        ContentValues values = new ContentValues();
        String path=Initilization.DB_PATH+Initilization.DB_NAME;
        SQLiteDatabase db= SQLiteDatabase.openDatabase(path, null, 0);
        String tempResults[][]=new String[1000][16];
        for(int i=0;i<1000;i++){
            for(int j=0;j<15;j++){
             tempResults[i][j]="NULL";
            }
        }
        asyncitems.clear();
        asyncitems.add(new ItemObject(tabName+"", tabName+"", tabName+"", tabName+"", tabName+"", tabName+"", tabName+"",tabName+"",tabName+"",tabName+""));
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(response, "", org.jsoup.parser.Parser.xmlParser());
        for(int i=0;i<16;i++)
        {
            if(i==Initilization.CategoryId) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("CategoryId")) {
                    tempResults[index][Initilization.CategoryId]=e.text();
                    index++;
                }
            }
            if(i==Initilization.Category) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("Category")) {
                    tempResults[index][Initilization.Category]=e.text();
                    index++;
                }
            }
            if(i==Initilization.DisplayOrder) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("DisplayOrder")) {
                    tempResults[index][Initilization.DisplayOrder]=e.text();
                    index++;
                }
            }
            if(i==Initilization.RSSTitle) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSTitle")) {
                    tempResults[index][Initilization.RSSTitle]=e.text();
                    index++;
                }
            }
            if(i==Initilization.RSSURL) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSURL")) {
                    tempResults[index][Initilization.RSSURL]=e.text();
                    index++;
                }
            }
            if(i==Initilization.RSSUrlId) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSUrlId")) {
                    tempResults[index][Initilization.RSSUrlId]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsId ) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsId")) {
                    tempResults[index][Initilization.NewsId]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsTitle ) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsTitle")) {
                    tempResults[index][Initilization.NewsTitle]=e.text();
                    index++;
                }
            }
            if(i==Initilization.Summary) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("Summary")) {
                    tempResults[index][Initilization.Summary]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsImage) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsImage")) {
                    tempResults[index][Initilization.NewsImage]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsDate) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsDate")) {
                    tempResults[index][Initilization.NewsDate]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsDisplayOrder) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsDisplayOrder")) {
                    tempResults[index][Initilization.NewsDisplayOrder]=e.text();
                    index++;
                }
            }
            if(i==Initilization.CategoryorNews) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("CategoryorNews")) {
                    tempResults[index][Initilization.CategoryorNews]=e.text();
                    index++;
                }
            }
            if(i==Initilization.FullText) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("FullText")) {
                    tempResults[index][Initilization.FullText]=e.text();
                    index++;
                }
            }
            if(i==Initilization.NewsUrl) {
                int index=0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsUrl")) {
                    tempResults[index][Initilization.NewsUrl]=e.text();
                    index++;
                }
            }
            if (i == Initilization.HTMLDesc) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("HtmlDescription")) {
                    tempResults[index][Initilization.HTMLDesc] = e.text();
                    index++;
                }
            }
        }
        for(int i=0;i<1000;i++){
            if(tempResults[i][Initilization.CategoryId].contains("NULL")||tempResults[i][Initilization.NewsId].contains("NULL")){
                continue;
            }
            values.put(Initilization.CATEGORYID,tempResults[i][Initilization.CategoryId]);
            values.put(Initilization.CATEGORYNAME,tempResults[i][Initilization.Category]);
            values.put(Initilization.DISPLAYORDER,tempResults[i][Initilization.DisplayOrder]);
            values.put(Initilization.RSSTITLE,tempResults[i][Initilization.RSSTitle]);
            values.put(Initilization.RSSURL_DB,tempResults[i][Initilization.RSSURL]);
            values.put(Initilization.RSSURLID,tempResults[i][Initilization.RSSUrlId]);
            values.put(Initilization.NEWSID,tempResults[i][Initilization.NewsId]);
            values.put(Initilization.NEWSTITLE,tempResults[i][Initilization.NewsTitle]);
            values.put(Initilization.SUMMARY,tempResults[i][Initilization.Summary]);
            values.put(Initilization.NEWSIMAGE,tempResults[i][Initilization.NewsImage]);
            values.put(Initilization.NEWSDATE,tempResults[i][Initilization.NewsDate]);
            values.put(Initilization.NEWSDISPLAYORDER,tempResults[i][Initilization.NewsDisplayOrder]);
            values.put(Initilization.CATEGORYORNEWS,tempResults[i][Initilization.CategoryorNews]);
            values.put(Initilization.FULLTEXT,tempResults[i][Initilization.FullText]);
            values.put(Initilization.NEWSURL, tempResults[i][Initilization.NewsUrl]);
            values.put(Initilization.RESERVED_4, tempResults[i][Initilization.HTMLDesc]);

            if(Initilization.resultArray[i][Initilization.CategoryId].compareTo("2")!=0)
                values.put(Initilization.RESERVED_2, tempResults[i][Initilization.NewsId]);
            else
                values.put(Initilization.RESERVED_2, "SOMERANDOMTEXT"+tempResults[i][Initilization.NewsId]);

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                Date date = sdf.parse(tempResults[i][Initilization.NewsDate].replace("T"," ").replace("+5:30",""));
                long timeInMillisSinceEpoch = date.getTime();
                Random r = new Random();
                int i1 = r.nextInt(1000);
                long timeInSecondsSinceEpoch = (timeInMillisSinceEpoch / (60))+i1;
                values.put(Initilization.RESERVED_3, timeInSecondsSinceEpoch);
            }catch(ParseException e){
                values.put(Initilization.RESERVED_3,0);
            }

            db.insert(Initilization.TABLE_NAME, null, values);

            NewsImage=tempResults[i][Initilization.NewsImage];
            NewsTitle=tempResults[i][Initilization.NewsTitle];
            RSSTitle = tempResults[i][Initilization.RSSTitle];
            NewsId=tempResults[i][Initilization.NewsId];
            CategoryId=tempResults[i][Initilization.CategoryId];
            FullText=tempResults[i][Initilization.FullText];
            NewsUrl=tempResults[i][Initilization.NewsUrl];
            NewsSummary=tempResults[i][Initilization.Summary];
            NewsDate=tempResults[i][Initilization.NewsDate];
            HTMLDesc=tempResults[i][Initilization.HTMLDesc];
            asyncitems.add(new ItemObject(NewsImage, NewsTitle, RSSTitle, NewsId, CategoryId, FullText, NewsUrl,NewsSummary,NewsDate,HTMLDesc));
            for(int k=0;k<itemCollection.size();k++){
                if((itemCollection.get(k).getNewsID().contains(NewsId) && asyncitems.size()-1>=0 )|| (itemCollection.get(k).getnewsTitle().compareTo(NewsTitle)==0&&asyncitems.size()-1>=0)){
                    asyncitems.remove(asyncitems.size()-1);
                }
            }
        }
        db.close();
    }


    public void tweet() {
        try {
            String tweetUrl =
                    String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                            urlEncode(newsTitle+" #EVERNEWS\n "), urlEncode(newsLink));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

            List<ResolveInfo> matches = ((getContext() == null ? getContext() : getContext())).getPackageManager().queryIntentActivities(intent, 0);
            boolean exists = false;
            for (ResolveInfo info : matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                    exists = true;
                    intent.setPackage(info.activityInfo.packageName);
                }
            }
            if (!exists) {
                Toast.makeText(getContext() == null ? getContext() : getContext(), "Twitter not found", Toast.LENGTH_SHORT).show();
                Intent tweet = new Intent(Intent.ACTION_VIEW);
                tweet.setData(Uri.parse("http://twitter.com/?status=" + Uri.encode(newsLink)));//where message is your string message
                startActivity(tweet);
            }
            else {
                if (context == null)
                    context.startActivity(intent);
                else context.startActivity(intent);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void openInBrowser() {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(newsLink));
            if (context == null)
                startActivity(i);
            else
                (context).startActivity(i);
        }catch (Exception e){e.printStackTrace();}
    }

    public void shareByOther() {
        try {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, newsLink);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
        }catch (Exception e){e.printStackTrace();}
    }

    public void sharebyMail() {
        try {
            Intent gmail = new Intent(Intent.ACTION_VIEW);
            gmail.setType("plain/text");
            gmail.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            gmail.putExtra(Intent.EXTRA_SUBJECT, newsTitle);
            gmail.putExtra(Intent.EXTRA_TEXT, newsLink);
            if (getContext() != null) {
                getContext().startActivity(gmail);
                return;
            }
            startActivity(gmail);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext() == null ? getContext() : getContext(), "Gmail client not found \nUse Share by other ", Toast.LENGTH_SHORT).show();
        }

    }

    class GetNewsTask extends AsyncTask<Void, Void, Void> {
        String content;
        int ExceptionCode = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Initilization.androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                String fetchLink = "http://rssapi.psweb.in/everapi.asmx/RefreshCategoryNews?CategoryId=" + getArguments().getString(TYPE_KEY);//Over ride but should be Main.androidId
                Log.i("#fetchLink",fetchLink);
                content = Jsoup.connect(fetchLink).ignoreContentType(true).timeout(Initilization.timeout).execute().body();
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
                ExceptionCode=3;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            refreshLayout.setRefreshing(false);
            refreshLayout.destroyDrawingCache();
            refreshLayout.clearAnimation();
            //new Main.DeleteRecords().execute();
            Main.progress.setVisibility(View.GONE);
            if (ExceptionCode > 0) {
                if (ExceptionCode == 1)
                    Toast.makeText(context, "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                if (ExceptionCode == 2)
                    Toast.makeText(context, "Some server related issue occurred..please try again later", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Some server related issue occurred..please try again later", Toast.LENGTH_SHORT).show();
            }
            if (content != null) {
                String result = content.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&");

                parseResultsPerCategory(result);

                List<ItemObject> allItems = getDefaultNews();
                //if(itemCollection.get(0).getnewsName().isEmpty())
                /**CAUTION HERE**/
                if(itemCollection!=null) {
                   // customAdapter = new CustomAdapter(context, itemCollection);
                    itemCollection.addAll(0,allItems);
                    //customAdapter.notifyDataSetChanged();
                    int postionToMaintain = gridView.getFirstVisiblePosition();
                    //gridView.setAdapter(customAdapter);
                    gridView.setSelection(postionToMaintain);
                }
                super.onPostExecute(aVoid);
            }
        }
    }
}
