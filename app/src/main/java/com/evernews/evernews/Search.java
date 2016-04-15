package com.evernews.evernews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {
    TextView query;
    ImageButton search;
    String queryString;
    GridView gridView;
    Context context;
    List <ItemObject> itemCollection=new ArrayList<>();


    @Override
    public void onResume() {
        super.onResume();
        /**RESTORE BRIGHTNESS**/
        {
            SharedPreferences sharedpreferences = getSharedPreferences(Main.USERLOGINDETAILS, Context.MODE_PRIVATE);
            float arg1=sharedpreferences.getFloat(Main.SLIDERCURRENT,250);
            float BackLightValue = (float)arg1/100;
            int curBrightnessValue=0;
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
            layoutParams.screenBrightness = BackLightValue; // Set Value
            getWindow().setAttributes(layoutParams); // Set params
        }
        /**END**/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.go_backpng);
        context=this;
        query=(TextView)findViewById(R.id.edit_query);
        //query.setImeActionLabel("Search", KeyEvent.KEYCODE_SEARCH);

        search=(ImageButton)findViewById(R.id.search_button);
        gridView=(GridView)findViewById(R.id.gridview_search);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridView.setNumColumns(4);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridView.setNumColumns(2);
        }

        query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    queryString=query.getText().toString();
                    if(!queryString.isEmpty()){
                        new searchData().execute();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(query.getWindowToken(), 0);
                    }
                    else{
                        Toast.makeText(context,"Keyword cannot be empty",Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                return false;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryString=query.getText().toString();
                if(!queryString.isEmpty()){
                    new searchData().execute();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(query.getWindowToken(), 0);
                }
                else{
                    Toast.makeText(context,"Keyword cannot be empty",Toast.LENGTH_LONG).show();
                }
            }
        });



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
                final Intent i = new Intent(context, ViewNews.class);
                if (newsID == null)
                    i.putExtra("NEWS_ID", newsID);
                else
                    i.putExtra("NEWS_ID", newsID);
                i.putExtra("SUMMARY",itemCollection.get(position).getnewsSummary());
                i.putExtra("NEWS_TITLE", itemCollection.get(position).getnewsTitle());
                i.putExtra("RSS_TITLE", itemCollection.get(position).getnewsName());
                i.putExtra("FULL_TEXT", itemCollection.get(position).getFullText());
                i.putExtra("NEWS_IMAGE", itemCollection.get(position).getNewsImage());
                i.putExtra("NEWS_DATE", itemCollection.get(position).getnewsDate());
                if (itemCollection.get(position).getCategoryID().compareTo("-1") == 0)
                    i.putExtra("NEWS_LINK", "NULL_WITH_IMAGE");
                else
                    i.putExtra("NEWS_LINK", itemCollection.get(position).getNewsURL());
                i.putExtra("CALLER","SEARCH");
                /*new AsyncTask<Void, Void, String>() {
                    String newsLink = "";
                    String source = "", title = "", news = "",newsDate="";

                    @Override
                    protected String doInBackground(Void... params) {
                        try {
                            ViewNews.finalHtml = "";
                            String xmlUrl = "http://rssapi.psweb.in/everapi.asmx/LoadSingleNews?NewsID=" + newsID;
                            URL cleanURL = new URL(xmlUrl.toString());
                            String Xml = Jsoup.connect(xmlUrl).ignoreContentType(true).execute().body();
                            char Xmlchar[] = Xml.toCharArray();
                            int iIndex = -1;
                            int eIndex = -1;
                            iIndex = Xml.indexOf("<FullText>") + 10;
                            eIndex = Xml.indexOf("</FullText>");
                            if (iIndex >= 0 && eIndex >= 0 && eIndex > iIndex) {
                                SharedPreferences sharedpreferences=getSharedPreferences(Main.USERLOGINDETAILS, Context.MODE_PRIVATE);
                                Main.fontSize=sharedpreferences.getInt(Main.FONTSIZE,18);
                                news = "<p align=\"justify\"><p style=\"font-size:"+Main.fontSize+"px\">"+Xml.copyValueOf(Xmlchar, iIndex, (eIndex - iIndex))+"</p></p>";
                            }
                            iIndex = Xml.indexOf("<NewsTitle>") + 11;
                            eIndex = Xml.indexOf("</NewsTitle>");
                            if (iIndex >= 0 && eIndex >= 0 && eIndex > iIndex) {
                                title = Xml.copyValueOf(Xmlchar, iIndex, (eIndex - iIndex));
                                if (title == null)
                                    i.putExtra("NEWS_TITLE", "NULL");
                                else
                                    i.putExtra("NEWS_TITLE", title);
                                title = "<h1><center>" + title + "</center></h1><br>"+"<h2><center>"+ newsDate + "</center></h2><br>";
                            }
                            iIndex = Xml.indexOf("<RSSTitle>") + 10;
                            eIndex = Xml.indexOf("</RSSTitle>");
                            if (iIndex >= 0 && eIndex >= 0 && eIndex > iIndex) {
                                source = Xml.copyValueOf(Xmlchar, iIndex, (eIndex - iIndex));
                                source = "<h2><center>" + source + "</center></h2>";
                            }
                            iIndex = Xml.indexOf("<NewsURL>") + 9;
                            eIndex = Xml.indexOf("</NewsURL>");
                            if (iIndex >= 0 && eIndex >= 0 && eIndex > iIndex) {
                                newsLink = Xml.copyValueOf(Xmlchar, iIndex, (eIndex - iIndex));
                                if (title == null)
                                    i.putExtra("NEWS_LINK", "NULL");
                                else
                                    i.putExtra("NEWS_LINK", newsLink);
                            }
                            ViewNews.finalHtml = source + title + news;
                            String Temp = ViewNews.finalHtml;
                            Temp = Temp.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
                            Temp = "<p>" + Temp + "</p>";
                            ViewNews.finalHtml = Temp;
                        } catch (IOException e) {

                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String link) {
                        ViewNews.finalHtml = "<!DOCTYPE html> <html> <body>" + ViewNews.finalHtml + "</p> </body> </html>";
                    }
                }.execute();*/
                startActivity(i);
            }
        });
    }

    private class searchData extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;
        private String resp;
        int ExceptionCode=0;
        String JsoupResopnse="";
        @Override
        protected String doInBackground(String... params) {
            String urlStr="http://rssapi.psweb.in//everapi.asmx/SearchNews?Keyword="+queryString;
            URL url=null;
            try{
                url = new URL(urlStr);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
            }
            catch(Exception e){
                Toast.makeText(context,"Url encoding error",Toast.LENGTH_LONG).show();
                return ("");
            }
            final String urlRequest = url.toString();       //Encoded url since search query may have space and other stuff
            try {
                publishProgress(1);
                JsoupResopnse = Jsoup.connect(urlRequest).timeout(Initilization.timeout).ignoreContentType(true).execute().body();
                if(JsoupResopnse.length()>50)
                    publishProgress(2);
            }
            catch (Exception e){
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
            if(JsoupResopnse.length()>50 && ExceptionCode==0){
                 itemCollection = decodeResults(JsoupResopnse);
                if(itemCollection.size()>0) {
                    CustomAdapter customAdapter = new CustomAdapter(context, itemCollection);
                    gridView.setAdapter(customAdapter);
                    itemCollection.addAll(itemCollection);
                    query.setText("");
                    query.setHint("Search Results for "+queryString);
                }
                else{
                    Toast.makeText(context,"No results found for "+queryString,Toast.LENGTH_LONG).show();
                }
                progressDlg.dismiss();
            }else{
                Toast.makeText(context,"Error occurred while fetching the news",Toast.LENGTH_LONG).show();
            }
            progressDlg.dismiss();
        }
        @Override
        protected void onPreExecute() {
            progressDlg = ProgressDialog.show(context, "Connecting", "Please wait while we connect to our servers", true);
            progressDlg.setCancelable(true);
            itemCollection.clear();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            if(progress[0]==1){
                progressDlg.setTitle("Downloading");
                progressDlg.setMessage("Downloading data...");
            }
            if(progress[0]==2){
                progressDlg.setTitle("Formatting");
                progressDlg.setMessage("Formatting data...");
            }
        }
    }

    public List<ItemObject> decodeResults(String response)
    {
        List<ItemObject> items = new ArrayList<>();
        XMLDOMParser parser = new XMLDOMParser();
        InputStream stream = new ByteArrayInputStream(response.getBytes());
        Document doc = parser.getDocument(stream);
        NodeList nodeList = doc.getElementsByTagName("Table");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element e = (Element) nodeList.item(i);
            String NewsImage = (parser.getValue(e, "NewsImage"));
            String NewsTitle = (parser.getValue(e, "NewsTitle"));
            String RSSTitle = (parser.getValue(e, "RSSTitle"));
            String NewsId = (parser.getValue(e, "NewsId"));
            String CATID = (parser.getValue(e, "RSSUrlId"));
            String FullText = (parser.getValue(e, "FullText"));
            String NewsURL = (parser.getValue(e, "NewsURL"));
            String Summary = (parser.getValue(e, "Summary"));
            String NewsDate = (parser.getValue(e, "NewsDate"));
            String HTMLDesc = (parser.getValue(e, "HtmlDescription"));
            items.add(new ItemObject(NewsImage, NewsTitle, RSSTitle, NewsId,CATID,FullText,NewsURL,Summary,NewsDate,HTMLDesc));
        }
        return (items);
    }
}
