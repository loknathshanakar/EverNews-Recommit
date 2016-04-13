package com.evernews.evernews;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

public class YouView extends AppCompatActivity {

    static String newsID="";
    static String newsLink="";
    static String newsTitle="";
    static String fullText="";
    static String rssTitle="";
    static String newsSummary="";
    static String caller="";
    static String newsImage="";
    static String UUIDD="";
    Context context;
    TextView title1,title2,content;
    ImageView imageView;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_you_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;

        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.go_backpng);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton fab_new = (FloatingActionButton) findViewById(R.id.fabb);
        fab_new.setVisibility(View.GONE);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        fab_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse("https://developers.facebook.com")).build();
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle(newsTitle);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
                arrayAdapter.add("Facebook");
                arrayAdapter.add("Twitter");
                arrayAdapter.add("Email");
                arrayAdapter.add("Copy Content");
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
                                                    .setContentUrl(Uri.parse(newsImage))
                                                    .setContentDescription(fullText)
                                                    .setContentTitle(newsTitle + " #EVERNEWS")
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
                                        copyToClipBoard();
                                        break;
                                    case 4:
                                        shareByOther();
                                        break;
                                }
                            }
                        });
                builderSingle.show();
            }
        });




        title1=(TextView)findViewById(R.id.source);
        title2=(TextView)findViewById(R.id.title);
        content=(TextView)findViewById(R.id.content);
        imageView=(ImageView)findViewById(R.id.imageView2);

        Intent intent = getIntent();
        newsID = intent.getStringExtra("NEWS_ID")+"";
        newsLink = intent.getStringExtra("NEWS_LINK")+"";
        newsTitle = intent.getStringExtra("NEWS_TITLE")+"";
        fullText = intent.getStringExtra("FULL_TEXT")+"";
        fullText=fullText.replace("$$$$","\n\n");
        rssTitle = intent.getStringExtra("RSS_TITLE")+"";
        newsImage=intent.getStringExtra("NEWS_IMAGE")+"";
        newsSummary=intent.getStringExtra("SUMMARY")+"\nShared via #EVERNEWS";
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
                    title1.setText(rssTitle);
                    title2.setText(newsTitle);
                    content.setText(fullText);
                    Glide.with(this).load(newsImage).into(imageView);
                    fab_new.setVisibility(View.VISIBLE);
                }
                catch(IllegalArgumentException e){
                    cleanUUID=false;
                    //newsImage="";
                    title1.setText(rssTitle);
                    title2.setText(newsTitle);
                    content.setText(fullText);
                    imageView.setVisibility(View.GONE);
                }
            }
        }
    }


    public void tweet() {
        try {
            String tweetUrl =
                    String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                            urlEncode(newsTitle+" #EVERNEWS\n "), urlEncode(newsSummary));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

            List<ResolveInfo> matches = ((context == null ? context : context)).getPackageManager().queryIntentActivities(intent, 0);
            boolean exists = false;
            for (ResolveInfo info : matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                    exists = true;
                    intent.setPackage(info.activityInfo.packageName);
                }
            }
            if (!exists) {
                Toast.makeText(context == null ? context : context, "Twitter not found", Toast.LENGTH_SHORT).show();
                Intent tweet = new Intent(Intent.ACTION_VIEW);
                tweet.setData(Uri.parse("http://twitter.com/?status=" + newsSummary));//where message is your string message
                startActivity(tweet);
            }
            else {
                if (context == null)
                    context.startActivity(intent);
                else context.startActivity(intent);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("TAG", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
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
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, newsSummary);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
        }catch (Exception e){e.printStackTrace();}
    }

    public void sharebyMail() {
        try {
            Intent gmail = new Intent(Intent.ACTION_VIEW);
            gmail.setType("plain/text");
            gmail.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            gmail.putExtra(Intent.EXTRA_SUBJECT, newsTitle);
            gmail.putExtra(Intent.EXTRA_TEXT, newsSummary);
            if (context != null) {
                context.startActivity(gmail);
                return;
            }
            startActivity(gmail);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context == null ? context : context, "Gmail client not found \nUse Share by other ", Toast.LENGTH_SHORT).show();
        }

    }

    public void copyToClipBoard() {
        try {
            ClipboardManager clipboard = (ClipboardManager) ((context == null ? this : context).getSystemService(Context.CLIPBOARD_SERVICE));
            ClipData clip = ClipData.newPlainText("link", newsSummary);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context == null ? context : context, "Link copied to clipboard", Toast.LENGTH_SHORT).show();
        }catch (Exception e){e.printStackTrace();}
    }
}
