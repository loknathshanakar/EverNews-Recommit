package com.evernews.evernews;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AddTabNewsPreview extends DialogFragment {
    SwipeRefreshLayout refresh;
    private static String RSSUID="";
    private static String imageURl="";
    private Context context;
    private static String channelDetails="";
    boolean newsReady=false;
    String tempResults[][]=new String[1000][16];
    public interface AddListener {
        void onAdd(AddTabNewsPreview dialog);
        void onRemove(AddTabNewsPreview dialog);
    }

    private static SharedPreferences sharedpreferences;
    private static AddListener listener;
    public AddTabNewsPreview setListener(AddListener listener) {
        AddTabNewsPreview.listener = listener;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        final View add = inflater.inflate(R.layout.activity_add_tab_news_preview, container, false);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        final ProgressBar prog=(ProgressBar)add.findViewById(R.id.progressBar_ADD);
        final Button addBtn=(Button)add.findViewById(R.id.add_add);
        final TextView channelName=(TextView) add.findViewById(R.id.channel_name_add);
        final TextView channelMeta=(TextView) add.findViewById(R.id.channelMeta_add);
        final ImageView channelLogo=(ImageView)add.findViewById(R.id.logo_View);
        context=getContext();
        Glide.with(context).load(imageURl).fitCenter().placeholder(R.drawable.ic_launcher).into(channelLogo);
        prog.setVisibility(View.VISIBLE);
        GridView gridView = (GridView)add.findViewById(R.id.gridview_add);
        List <ItemObject> tempItems = parseResultsEmpty();
        CustomAdapter customAdapter = new CustomAdapter(getActivity(), tempItems);
        gridView.setAdapter(customAdapter);
        sharedpreferences = getActivity().getSharedPreferences(Main.USERLOGINDETAILS, Context.MODE_PRIVATE);
        new AsyncTask<Void, Void, String>() {
            int ExceptionCode=0;    //Success
            String returnContent="";
            @Override
            protected String doInBackground(Void... params) {
                try {
                    String xmlUrl = "http://rssapi.psweb.in/everapi.asmx/LoadDefaultNewsbyNewsChannel?RSSUrlId=" + RSSUID.replace(" ","");
                    URL cleanURL=new URL(xmlUrl.toString());
                    returnContent = Jsoup.connect(xmlUrl).timeout(Initilization.timeout).ignoreContentType(true).execute().body();
                }
                catch (IOException e) {
                    ExceptionCode=1;        //Failure
                }
                return null;
            }
            @Override
            protected void onPostExecute(String link) {
                if(ExceptionCode==0 && returnContent.length()>50){
                    String result = returnContent.toString().replaceAll("&lt;", "<").replaceAll("&gt;",">").replaceAll("&amp;","&");
                    GridView gridView = (GridView)add.findViewById(R.id.gridview_add);
                    List <ItemObject> tempItems = parseResults(result);
                    if(tempItems.size()>0){
                    CustomAdapter customAdapter = new CustomAdapter(getActivity(), tempItems);
                    gridView.setAdapter(customAdapter);
                    channelName.setText(tempItems.get(0).getnewsName());
                    channelMeta.setText(channelDetails);
                    newsReady=true;
                    }
                    else{
                        List<ItemObject> items = new ArrayList<>();
                        String NewsImage = "No news avaliable";
                        String NewsTitle ="No news avaliable";
                        String RSSTitle = "No news avaliable";
                        String NewsId = "No news avaliable";
                        String CATID = "No news avaliable";
                        String fullText="NA";
                        String newsLink="NA";
                        String Summary="NA";
                        String NewsTime="NA";
                        channelName.setText("Not avaliable");
                        channelMeta.setText("Not avaliable");
                        items.add(new ItemObject(NewsImage, NewsTitle, RSSTitle, NewsId, CATID,fullText,newsLink,Summary,NewsTime,"NA"));
                        CustomAdapter customAdapter = new CustomAdapter(getActivity(), items);
                        gridView.setAdapter(customAdapter);
                    }
                    prog.setVisibility(View.INVISIBLE);
                }else {
                    Toast.makeText(getContext(),"An error occured while fetching the news...",Toast.LENGTH_LONG).show();
                    prog.setVisibility(View.INVISIBLE);
                }
            }
        }.execute();

        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AsyncTask<Void, Integer, String>() {
                    int ExceptionCode = 0;       //sucess;
                    String JsoupResopnse = "";
                    int FtpExceptions= 0;
                    ProgressDialog progressdlg;
                    @Override
                    protected void onProgressUpdate(Integer... text) {
                        if(text[0]==1 && newsReady==true)
                            progressdlg.setMessage("Adding channel");
                    }

                    @Override
                    protected void onPreExecute() {
                        Main.progress.setVisibility(View.VISIBLE);
                        progressdlg = new ProgressDialog(getContext());
                        progressdlg.setMessage("Connecting to server...");
                        progressdlg.setTitle("Adding channel");
                        progressdlg.setCancelable(false);
                        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressdlg.setIndeterminate(true);
                        progressdlg.show();
                    }
                    @Override
                    protected String doInBackground(Void... params) {
                        try {
                            publishProgress(1);
                            Initilization.androidId = android.provider.Settings.Secure.getString(getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                            String xmlUrl = "http://rssapi.psweb.in/everapi.asmx/AddNewsTAB?RSSID="+RSSUID.replace(" ","")+"&AndroidId="+Initilization.androidId;
                            JsoupResopnse= Jsoup.connect(xmlUrl).ignoreContentType(true).timeout(Initilization.timeout).execute().body();
                            if(!JsoupResopnse.contains("<int xmlns=\"http://tempuri.org/\">1</int>")){
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
                        if(ExceptionCode==0) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(Main.NEWCHANNELADDED, true);
                            editor.apply();
                            AddNewsTab();
                            Snackbar snackbar = Snackbar.make(add, "News added successfully...("+RSSUID+")", Snackbar.LENGTH_LONG);
                            newsReady=false;
                            snackbar.show();
                            AddNewsTab();

                            new CountDownTimer(1500, 500) {
                                public void onTick(long millisUntilFinished) {
                                }
                                public void onFinish() {
                                    dismiss();
                                }
                            }.start();

                        }
                        else{
                            Snackbar snackbar = Snackbar.make(add, "Sorry news could not be added...", Snackbar.LENGTH_LONG);
                            new CountDownTimer(1500, 500) {

                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    dismiss();
                                }
                            }.start();
                            snackbar.show();
                        }
                    }
                }.execute();
            }
        });

        return add;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    public void AddNewsTab() {
        ContentValues values = new ContentValues();
        String path = Initilization.DB_PATH + Initilization.DB_NAME;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, 0);

        for (int i = 0; i < 1000; i++) {
            if (tempResults[i][Initilization.CategoryId].contains("NULL") || tempResults[i][Initilization.NewsId].contains("NULL")) {
                continue;
            }
            values.put(Initilization.CATEGORYID, tempResults[i][Initilization.RSSUrlId]);
            values.put(Initilization.CATEGORYNAME, tempResults[i][Initilization.RSSTitle]);
            values.put(Initilization.DISPLAYORDER, "0");
            values.put(Initilization.RSSTITLE, tempResults[i][Initilization.RSSTitle]);
            values.put(Initilization.RSSURL_DB, tempResults[i][Initilization.RSSURL]);
            values.put(Initilization.RSSURLID, tempResults[i][Initilization.RSSUrlId]);
            values.put(Initilization.NEWSID, tempResults[i][Initilization.NewsId]);
            values.put(Initilization.NEWSTITLE, tempResults[i][Initilization.NewsTitle]);
            values.put(Initilization.SUMMARY, tempResults[i][Initilization.Summary]);
            values.put(Initilization.NEWSIMAGE, tempResults[i][Initilization.NewsImage]);
            values.put(Initilization.NEWSDATE, tempResults[i][Initilization.NewsDate]);
            values.put(Initilization.NEWSDISPLAYORDER, tempResults[i][Initilization.NewsDisplayOrder]);
            values.put(Initilization.CATEGORYORNEWS, tempResults[i][Initilization.CategoryorNews]);
            values.put(Initilization.FULLTEXT, tempResults[i][Initilization.FullText]);
            values.put(Initilization.NEWSURL, tempResults[i][Initilization.NewsUrl]);
            values.put(Initilization.RESERVED_4, tempResults[i][Initilization.HTMLDesc]);

            if (Initilization.resultArray[i][Initilization.CategoryId].compareTo("2") != 0)
                values.put(Initilization.RESERVED_2, tempResults[i][Initilization.NewsId]);
            else
                values.put(Initilization.RESERVED_2, "SOMERANDOMTEXT" + tempResults[i][Initilization.NewsId]);

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                Date date = sdf.parse(tempResults[i][Initilization.NewsDate].replace("T", " ").replace("+5:30", ""));
                long timeInMillisSinceEpoch = date.getTime();
                Random r = new Random();
                int i1 = r.nextInt(1000);
                long timeInSecondsSinceEpoch = (timeInMillisSinceEpoch / (60)) + i1;
                values.put(Initilization.RESERVED_3, timeInSecondsSinceEpoch);
            } catch (ParseException e) {
                values.put(Initilization.RESERVED_3, 0);
            }
            db.insert(Initilization.TABLE_NAME, null, values);
        }
        db.close();
    }

    public ArrayList<ItemObject> parseResults(String response) {
        ArrayList <ItemObject> resultArray=new ArrayList<>();
        String NewsImage="",NewsTitle="",RSSTitle="",NewsId="",CategoryId="",FullText="",NewsUrl="",NewsSummary="",NewsDate="",HTMLDesc="";
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 15; j++) {
                tempResults[i][j] = "NULL";
            }
        }
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(response, "", org.jsoup.parser.Parser.xmlParser());
        for (int i = 0; i < 16; i++) {
            if (i == Initilization.CategoryId) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSUrlId")) {
                    tempResults[index][Initilization.CategoryId] = e.text();
                    index++;
                }
            }
            if (i == Initilization.Category) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSTitle")) {
                    tempResults[index][Initilization.Category] = e.text();
                    index++;
                }
            }
            if (i == Initilization.DisplayOrder) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("DisplayOrder")) {
                    tempResults[index][Initilization.DisplayOrder] = "0";
                    index++;
                }
            }
            if (i == Initilization.RSSTitle) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSTitle")) {
                    tempResults[index][Initilization.RSSTitle] = e.text();
                    index++;
                }
            }
            if (i == Initilization.RSSURL) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSURL")) {
                    tempResults[index][Initilization.RSSURL] = e.text();
                    index++;
                }
            }
            if (i == Initilization.RSSUrlId) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("RSSUrlId")) {
                    tempResults[index][Initilization.RSSUrlId] = e.text();
                    index++;
                }
            }
            if (i == Initilization.NewsId) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsId")) {
                    tempResults[index][Initilization.NewsId] = e.text();
                    index++;
                }
            }
            if (i == Initilization.NewsTitle) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsTitle")) {
                    tempResults[index][Initilization.NewsTitle] = e.text();
                    index++;
                }
            }
            if (i == Initilization.Summary) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("Summary")) {
                    tempResults[index][Initilization.Summary] = e.text();
                    index++;
                }
            }
            if (i == Initilization.NewsImage) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsImage")) {
                    tempResults[index][Initilization.NewsImage] = e.text();
                    index++;
                }
            }
            if (i == Initilization.NewsDate) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsDate")) {
                    tempResults[index][Initilization.NewsDate] = e.text();
                    index++;
                }
            }
            if (i == Initilization.NewsDisplayOrder) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsDisplayOrder")) {
                    tempResults[index][Initilization.NewsDisplayOrder] = e.text();
                    index++;
                }
            }
            if (i == Initilization.CategoryorNews) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("CategoryorNews")) {
                    tempResults[index][Initilization.CategoryorNews] = e.text();
                    index++;
                }
            }
            if (i == Initilization.FullText) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("FullText")) {
                    tempResults[index][Initilization.FullText] = e.text();
                    index++;
                }
            }
            if (i == Initilization.NewsUrl) {
                int index = 0;
                for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsUrl")) {
                    tempResults[index][Initilization.NewsUrl] = e.text();
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

        for (int i = 0; i < 1000; i++) {
            if (tempResults[i][Initilization.RSSUrlId].contains("NULL") || tempResults[i][Initilization.NewsId].contains("NULL")) {
                continue;
            }
            NewsImage = tempResults[i][Initilization.NewsImage];
            NewsTitle = tempResults[i][Initilization.NewsTitle];
            RSSTitle = tempResults[i][Initilization.RSSTitle];
            NewsId = tempResults[i][Initilization.NewsId];
            CategoryId = tempResults[i][Initilization.CategoryId];
            FullText = tempResults[i][Initilization.FullText];
            NewsUrl = tempResults[i][Initilization.NewsUrl];
            NewsSummary = tempResults[i][Initilization.Summary];
            NewsDate = tempResults[i][Initilization.NewsDate];
            HTMLDesc = tempResults[i][Initilization.HTMLDesc];
            resultArray.add(new ItemObject(NewsImage, NewsTitle, RSSTitle, NewsId, CategoryId, FullText, NewsUrl, NewsSummary, NewsDate, HTMLDesc));
        }
        return (resultArray);
    }

    public List<ItemObject> parseResultsEmpty()
    {
        List<ItemObject> items = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            String NewsImage = "Updating";
            String NewsTitle ="Updating";
            String RSSTitle = "Updating";
            String NewsId = "Updating";
            String CATID = "Updating";
            items.add(new ItemObject(NewsImage, NewsTitle, RSSTitle, NewsId,CATID,"FullText","NewsLink","Summary","XXX","HTMLDESC"));
        }
        return (items);
    }
    public AddTabNewsPreview setRSSUID(String RSSUID) {
        AddTabNewsPreview.RSSUID = RSSUID;
        return this;
    }

    public AddTabNewsPreview setChannelDetails(String channelDetails) {
        AddTabNewsPreview.channelDetails = channelDetails;
        return this;
    }
    public AddTabNewsPreview setImageURL(String imageURl) {
        AddTabNewsPreview.imageURl = imageURl;
        return this;
    }

}
