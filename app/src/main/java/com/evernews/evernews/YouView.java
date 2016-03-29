package com.evernews.evernews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;
import java.util.regex.Pattern;

public class YouView extends AppCompatActivity {

    static String newsID="";
    static String newsLink="";
    static String newsTitle="";
    static String fullText="";
    static String rssTitle="";
    static String caller="";
    static String newsImage="";
    static String UUIDD="";

    TextView title1,title2,content;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo);

        FloatingActionButton fab_new = (FloatingActionButton) findViewById(R.id.fabb);
        fab_new.setVisibility(View.GONE);

        title1=(TextView)findViewById(R.id.source);
        title1=(TextView)findViewById(R.id.source);
        title1=(TextView)findViewById(R.id.source);
        title1=(ImageView)findViewById(R.id.source);

        Intent intent = getIntent();
        newsID = intent.getStringExtra("NEWS_ID")+"";
        newsLink = intent.getStringExtra("NEWS_LINK")+"";
        newsTitle = intent.getStringExtra("NEWS_TITLE")+"";
        fullText = intent.getStringExtra("FULL_TEXT")+"";
        rssTitle = intent.getStringExtra("RSS_TITLE")+"";
        newsImage=intent.getStringExtra("NEWS_IMAGE")+"";
        boolean cleanUUID=true;
        {
            char newsImagec[] = newsImage.toCharArray();
            int iIndex=-1;
            int eIndex=-1;
            http://rss.psweb.in/NewsImages/8d3e1b1a-0eec-49fc-b796-56db971b46db.jpg
            iIndex=newsImage.indexOf("s/")+2;
            eIndex=newsImage.indexOf(".jpg");
            if (iIndex >= 0 && eIndex >= 0 && eIndex > iIndex) {
                UUIDD = String.copyValueOf(newsImagec, iIndex, (eIndex - iIndex));
                try{
                    cleanUUID=true;

                    UUID.fromString(UUIDD);
                }
                catch(IllegalArgumentException e){
                    cleanUUID=false;
                }
            }
        }
    }
}
