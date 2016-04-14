package com.evernews.evernews;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;

public class Settings extends AppCompatActivity {
    TextView newsChannel, newsOrientationType,newsOrientation, newsFont, newsSupport, newsReview, newsRecomend, newsPolicy, newsTerms, newsCredits, newsWeb, newsVersion,animationType,morningTime,noonTime,eveningTime;
    //private static Activity context;
    RelativeLayout orientationListiner,logoutlistiner,animationLayout,morningLayout,noonLayout,eveningLayout,fontLayout;
    String mTime="",nTime="",eTime="";
    Switch enableSwitch;
    int enabled=-1;
    Context context;
    private static SharedPreferences sharedpreferences;
    static final int TIME_DIALOG_ID = 1111;
    private int hour;
    private int minute;
    int whichBox=0;
    SharedPreferences.Editor editor;
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute, false);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour   = hourOfDay;
            minute = minutes;
            updateTime(hour,minute);

        }

    };

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        if(whichBox==1) {
            morningTime.setText(aTime);
            editor.putString(Main.MORNINGTIME, aTime);
            editor.putInt(Main.ONALRAMCHANGED1,1);
            editor.apply();
        }
        else if(whichBox==2) {
            noonTime.setText(aTime);
            editor.putString(Main.NOONTIME,aTime);
            editor.putInt(Main.ONALRAMCHANGED2,1);
            editor.apply();
        }
        else if(whichBox==3) {
            eveningTime.setText(aTime);
            editor.putString(Main.EVENINGTIME,aTime);
            editor.putInt(Main.ONALRAMCHANGED3,1);
            editor.apply();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.go_backpng);
        getSupportActionBar().setTitle("");

        context=this;
        sharedpreferences = getSharedPreferences(Main.USERLOGINDETAILS, Context.MODE_PRIVATE);
        if(sharedpreferences.getString(Main.APPLICATIONORIENTATION,"A").compareTo("L")==0){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else if(sharedpreferences.getString(Main.APPLICATIONORIENTATION,"A").compareTo("P")==0){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }



        /**Font Settings**/
        fontLayout=(RelativeLayout)findViewById(R.id.fontSizeListiner);
        newsFont=(TextView)findViewById(R.id.fontSize);
        newsFont.setText(sharedpreferences.getInt(Main.FONTSIZE,18)+"px");
        fontLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                builderSingle.setIcon(R.drawable.ic_launcher);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
                builderSingle.setTitle("Select font size");
                arrayAdapter.add("16px");
                arrayAdapter.add("18px");
                arrayAdapter.add("20px");
                arrayAdapter.add("22px");
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
                                    case 0:
                                        editor.putInt(Main.FONTSIZE, 16);
                                        newsFont.setText("16px");
                                        editor.apply();
                                        break;
                                    case 1:
                                        editor.putInt(Main.FONTSIZE, 18);
                                        newsFont.setText("18px");
                                        editor.apply();
                                        editor.apply();
                                        break;
                                    case 2:
                                        editor.putInt(Main.FONTSIZE, 20);
                                        newsFont.setText("20px");
                                        editor.apply();
                                        editor.apply();
                                        break;
                                    case 3:
                                        editor.putInt(Main.FONTSIZE, 22);
                                        newsFont.setText("22px");
                                        editor.apply();
                                        break;
                                    case 4:
                                        break;
                                    case 5:
                                        break;
                                }
                            }
                        });
                builderSingle.show();
            }
        });
        /**END**/
        /**Notification**/
        editor=sharedpreferences.edit();
        enableSwitch=(Switch)findViewById(R.id.switch1);
        enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    editor.putInt(Main.NOTIFICATIONENABLED, 1);
                }
                else {
                    editor.putInt(Main.NOTIFICATIONENABLED, 0);
                }
                editor.apply();
            }
        });
        enabled=sharedpreferences.getInt(Main.NOTIFICATIONENABLED,-1);
        if(enabled==1)
            enableSwitch.setChecked(true);
        else
            enableSwitch.setChecked(false);
        mTime=sharedpreferences.getString(Main.MORNINGTIME,"");
        nTime=sharedpreferences.getString(Main.NOONTIME,"");
        eTime=sharedpreferences.getString(Main.EVENINGTIME,"");
        morningLayout=(RelativeLayout)findViewById(R.id.morningLayout);
        noonLayout=(RelativeLayout)findViewById(R.id.noonLayout);
        eveningLayout=(RelativeLayout)findViewById(R.id.eveningLayout);
        morningTime=(TextView)findViewById(R.id.morningTimenum);
        morningTime.setText(mTime);
        noonTime=(TextView)findViewById(R.id.noonTimenum);
        noonTime.setText(nTime);
        eveningTime=(TextView)findViewById(R.id.eveningTimenum);
        eveningTime.setText(eTime);


        morningLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Morning", Toast.LENGTH_LONG).show();
                whichBox=1;
                showDialog(TIME_DIALOG_ID);
            }
        });

        noonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Noon",Toast.LENGTH_LONG).show();
                whichBox=2;
                showDialog(TIME_DIALOG_ID);
            }
        });

        eveningLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Evening",Toast.LENGTH_LONG).show();
                whichBox=3;
                showDialog(TIME_DIALOG_ID);
            }
        });

        /**END**/
        newsChannel= (TextView) findViewById(R.id.newsChannel);
        newsChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean override=true;
                    if(Main.validCategory ||override) {
                        Intent iii = new Intent(Settings.this, AddTab.class);
                        iii.putExtra("CALLER", "SETTINGS");
                        finish();
                        startActivity(iii);
                        return;
                    }
                    else {
                        Toast.makeText(context,"News channel list is loading please try again after some time",Toast.LENGTH_LONG).show();
                        new Main.GetCategoryList().execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        newsOrientation= (TextView) findViewById(R.id.orientationtype);
        newsOrientationType= (TextView) findViewById(R.id.orientationtype);
        if(sharedpreferences.getString(Main.APPLICATIONORIENTATION,"A").compareTo("L")==0){
            newsOrientationType.setText("Landscape");
        }
        else if(sharedpreferences.getString(Main.APPLICATIONORIENTATION,"A").compareTo("P")==0){
            newsOrientationType.setText("Portrait");
        }else {
            newsOrientationType.setText("Automatic");
        }


        orientationListiner=(RelativeLayout)findViewById(R.id.orientation_listiner);
        orientationListiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                builderSingle.setIcon(R.drawable.ic_launcher);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
                builderSingle.setTitle("Select orientation");
                arrayAdapter.add("Automatic");
                arrayAdapter.add("Portrait");
                arrayAdapter.add("Landscape");
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
                                    case 0:
                                        editor.putString(Main.APPLICATIONORIENTATION, "A");
                                        newsOrientationType.setText("Automatic");
                                        editor.apply();
                                        recreate();
                                        break;
                                    case 1:
                                        editor.putString(Main.APPLICATIONORIENTATION, "P");
                                        newsOrientationType.setText("Portrait");
                                        recreate();
                                        editor.apply();
                                        break;
                                    case 2:
                                        editor.putString(Main.APPLICATIONORIENTATION, "L");
                                        newsOrientationType.setText("Landscape");
                                        recreate();
                                        editor.apply();
                                        break;
                                    case 3:
                                        break;
                                    case 4:
                                        break;
                                    case 5:
                                        break;
                                }
                            }
                        });
                builderSingle.show();
            }
        });

        animationLayout=(RelativeLayout)findViewById(R.id.animationType);
        animationType=(TextView)findViewById(R.id.animation_type);
        animationType.setText("Page change type : "+sharedpreferences.getString(Main.ANIMATIONTYPE,"CubeOut Transformer"));
        animationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                builderSingle.setIcon(R.drawable.ic_launcher);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
                builderSingle.setTitle("Page change animation");

                arrayAdapter.add("CubeOut Transformer");

                arrayAdapter.add("Accordion Transformer");

                arrayAdapter.add("BackgroundToForeground Transformer");

                arrayAdapter.add("DepthPage Transformer");

                arrayAdapter.add("ForegroundToBackground Transformer");

                arrayAdapter.add("RotateDown Transformer");

                arrayAdapter.add("RotateUp Transformer");

                arrayAdapter.add("ScaleInOut Transformer");

                arrayAdapter.add("Stack Transformer");

                arrayAdapter.add("Tablet Transformer");

                arrayAdapter.add("ZoomIn Transformer");

                arrayAdapter.add("ZoomOutSlide Transformer");

                arrayAdapter.add("ZoomOut Transformer");

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
                                    case 0:
                                        editor.putString(Main.ANIMATIONTYPE, "CubeOut");
                                        animationType.setText("Page change type : CubeOut Transformer");
                                        editor.apply();
                                        break;
                                    case 1:
                                        editor.putString(Main.ANIMATIONTYPE, "Accordion");
                                        animationType.setText("Page change type : Accordion Transformer");
                                        editor.apply();
                                        break;
                                    case 2:
                                        editor.putString(Main.ANIMATIONTYPE, "BackgroundToForeground");
                                        animationType.setText("Page change type : BackgroundToForeground Transformer");
                                        editor.apply();
                                        break;
                                    case 3:
                                        editor.putString(Main.ANIMATIONTYPE, "DepthPage");
                                        animationType.setText("Page change type : DepthPage Transformer");
                                        editor.apply();
                                        break;
                                    case 4:
                                        editor.putString(Main.ANIMATIONTYPE, "ForegroundToBackground");
                                        animationType.setText("Page change type : ForegroundToBackground Transformer");
                                        editor.apply();
                                        break;

                                    case 5:
                                        editor.putString(Main.ANIMATIONTYPE, "RotateDown");
                                        animationType.setText("Page change type : RotateDown Transformer");
                                        editor.apply();
                                        break;

                                    case 6:
                                        editor.putString(Main.ANIMATIONTYPE, "RotateUp");
                                        animationType.setText("Page change type : RotateUp Transformer");
                                        editor.apply();
                                        break;

                                    case 7:
                                        editor.putString(Main.ANIMATIONTYPE, "ScaleInOut");
                                        animationType.setText("Page change type : ScaleInOut Transformer");
                                        editor.apply();
                                        break;

                                    case 8:
                                        editor.putString(Main.ANIMATIONTYPE, "Stack");
                                        animationType.setText("Page change type : Stack Transformer");
                                        editor.apply();
                                        break;

                                    case 9:
                                        editor.putString(Main.ANIMATIONTYPE, "Tablet");
                                        animationType.setText("Page change type : Tablet Transformer");
                                        editor.apply();
                                        break;

                                    case 10:
                                        editor.putString(Main.ANIMATIONTYPE, "ZoomIn");
                                        animationType.setText("Page change type : ZoomIn Transformer");
                                        editor.apply();
                                        break;

                                    case 11:
                                        editor.putString(Main.ANIMATIONTYPE, "ZoomOutSlide");
                                        animationType.setText("Page change type : ZoomOutSlide Transformer");
                                        editor.apply();
                                        break;

                                    case 12:
                                        editor.putString(Main.ANIMATIONTYPE, "ZoomOut");
                                        animationType.setText("Page change type : ZoomOut Transformer");
                                        editor.apply();
                                        break;
                                }
                            }
                        });
                builderSingle.show();
            }
        });


        logoutlistiner=(RelativeLayout)findViewById(R.id.logout_lisetiner);
        logoutlistiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(sharedpreferences.getBoolean(Main.ISREGISTRED,false) && sharedpreferences.getBoolean(Main.LOGGEDIN,false)) {
                    editor.putBoolean(Main.ISREGISTRED, false);
                    editor.putBoolean(Main.LOGGEDIN, false);
                    editor.apply();
                    //Toast.makeText(context, "You were logged out application will now restart", Toast.LENGTH_SHORT).show();
                    ProgressDialog progressdlg = new ProgressDialog(context);
                    progressdlg.setMessage("Restarting Application");
                    progressdlg.setTitle("Log out successful,Please Wait...");
                    progressdlg.setCancelable(false);
                    progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressdlg.setIndeterminate(true);
                    progressdlg.show();

                    new CountDownTimer(3000, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            Intent i = context.getPackageManager().getLaunchIntentForPackage( context.getPackageName() );
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }.start();
                }
                else{
                    Toast.makeText(context, "You are not logged in to EverNews", Toast.LENGTH_LONG).show();
                }
            }
        });


        newsSupport= (TextView) findViewById(R.id.newsSupport);
        newsSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Settings.this, "Support Setting is populated soon..", Toast.LENGTH_SHORT).show();
                //Intent intent=new Intent(Settings.this, SupportActivity.class);
                //startActivity(intent);
            }
        });


        newsReview= (TextView) findViewById(R.id.newsReview);
        newsReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Settings.this, "Review Setting is populated soon..", Toast.LENGTH_SHORT).show();
            }
        });



        newsRecomend= (TextView) findViewById(R.id.newsRecomend);
        newsRecomend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Settings.this, "Recomend Setting is populated soon..", Toast.LENGTH_SHORT).show();
            }
        });



        newsPolicy= (TextView) findViewById(R.id.newsPolicy);
        newsPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Settings.this, "Policy Setting is populated soon..", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Settings.this, ViewMessage.class);
                startActivity(intent);
            }
        });


        newsTerms= (TextView) findViewById(R.id.newsTerms);
        newsTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Settings.this, "Terms Setting is populated soon..", Toast.LENGTH_SHORT).show();
            }
        });



        newsCredits= (TextView) findViewById(R.id.newsCredits);
        newsCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Settings.this, "Credits Setting is populated soon..", Toast.LENGTH_SHORT).show();
            }
        });


        newsWeb= (TextView) findViewById(R.id.newsWeb);
        newsWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Settings.this, "Web Setting is populated soon..", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent=new Intent(Settings.this,Main.class);
            //startActivity(intent);
            finish();
            return(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==android.R.id.home){
            Intent intent=new Intent(Settings.this,Main.class);
            //startActivity(intent);
            finish();
            return(true);
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void tweet() {
        try {
            String tweetUrl = String.format("https://twitter.com/");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

            List<ResolveInfo> matches = ((context == null ? getApplicationContext() : context)).getPackageManager().queryIntentActivities(intent, 0);
            boolean exists = false;
            for (ResolveInfo info : matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                    exists = true;
                    intent.setPackage(info.activityInfo.packageName);
                }
            }
            if (!exists)
                Toast.makeText(context == null ? getApplicationContext() : context, "Twitter not found", Toast.LENGTH_SHORT).show();
            else {
                if (context == null)
                    context.startActivity(intent);
                else context.startActivity(intent);
            }
        }catch (Exception e){e.printStackTrace();}
    }

}

