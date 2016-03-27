package com.evernews.evernews;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUp.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    public static String uniqueID = "";
    private static SharedPreferences sharedpreferences;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp.
     */
    // TODO: Rename and change types and number of parameter
    private final String USERLOGINDETAILS = "USERLOGINDETAILS" ;
    Context context;
    TextView userName; //= (TextView) view.findViewById(R.id.userName);
    TextView userEmail; // = (TextView) getActivity().findViewById(R.id.userEmail);
    TextView userNumber ; //= (TextView) getActivity().findViewById(R.id.userNumber);
    TextView password ; //= (TextView) getActivity().findViewById(R.id.password);
    TextView comfirmpassword;
    Button signUp; // = (Button) getActivity().findViewById(R.id.signup);
    Button login; // = (Button) getActivity().findViewById(R.id.noAction);
    Button fbSignup ; //= (Button) getActivity().findViewById(R.id.facebook);
    Button gSignup; // = (Button) getActivity().findViewById(R.id.google);
    Button twittersignup;
    Button fLogin;
    Button gLogin;
    Button tLogin;
    TextView uName, pwd;
    int loginbyWhich = 0;
    Button loginButton, cancelButton;
    EditText mSpinner;
    Pattern emailPattern;
    Account[] accounts;
    String email = "", fullName = "";
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private OnFragmentInteractionListener mListener;

    public SignUp() {
        // Required empty public constructor
    }

    public static SignUp newInstance(String param1) {
        SignUp fragment = new SignUp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {        //fb related callback
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            accessTokenTracker.stopTracking();
        }catch (Exception e){}
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            FacebookSdk.sdkInitialize(getContext());
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(final LoginResult loginResult) {
                            accessToken = AccessToken.getCurrentAccessToken();
                            GraphRequest request = GraphRequest.newMeRequest(
                                    accessToken,
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(
                                                JSONObject object,
                                                final GraphResponse response) {
                                            try {
                                                String emailL = object.getString("email") + "";
                                                String fullNameL = object.getString("name") + "";
                                                userName.setText(fullNameL);
                                                userEmail.setText(emailL);
                                                if (fullNameL.length() > 0 && isValidEmail(emailL)) {
                                                    password.setHint("Password is not needed for Facebook login");
                                                    comfirmpassword.setHint("Password is not needed for Facebook login");
                                                    if (uName != null) uName.setText(emailL);
                                                    password.setEnabled(false);
                                                    comfirmpassword.setEnabled(false);
                                                    if(loginbyWhich==1)
                                                    {
                                                        String usernameoremail = uName.getText().toString();
                                                        String pass = pwd.getText().toString();
                                                        String urlStr = "";
                                                        if (loginbyWhich == 1)
                                                            urlStr = "http://rssapi.psweb.in/everapi.asmx/ValidateSocialLogin?EmailorMobile=" + usernameoremail;
                                                        else if (loginbyWhich == 0)
                                                            urlStr = "http://rssapi.psweb.in/everapi.asmx/ValidateLogin?EmailorMobile=" + usernameoremail + "&Password=" + pass;
                                                        URL url = null;
                                                        try {
                                                            url = new URL(urlStr);
                                                            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                                                            url = uri.toURL();
                                                        } catch (Exception e) {/****/}
                                                        final String urlRequest = url.toString() + "";
                                                        final ProgressDialog progressdlg;
                                                        progressdlg = new ProgressDialog(getContext());
                                                        progressdlg.setMessage("Connecting to server...");
                                                        progressdlg.setTitle("Authentication");
                                                        progressdlg.setCancelable(false);
                                                        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                        progressdlg.setIndeterminate(true);
                                                        progressdlg.show();
                                                        if (isValidEmail(usernameoremail) || isValidNumber(usernameoremail)) {
                                                            new AsyncTask<Void, Integer, String>() {
                                                                int ExceptionCode = 0;
                                                                String JsoupResopnse = "";

                                                                @Override
                                                                protected void onProgressUpdate(Integer... text) {
                                                                    if (text[0] == 2)
                                                                        progressdlg.setMessage("Connecting to server...");
                                                                    if (text[0] == 3)
                                                                        progressdlg.setMessage("Authenticating");
                                                                }

                                                                @Override
                                                                protected String doInBackground(Void... params) {
                                                                    try {
                                                                        publishProgress(3);
                                                                        JsoupResopnse = Jsoup.connect(urlRequest).timeout(Initilization.timeout - 5).ignoreContentType(true).execute().body();
                                                                        int iIndex = JsoupResopnse.indexOf("\">") + 2;
                                                                        int eIndex = JsoupResopnse.indexOf("</");
                                                                        char jChar[] = JsoupResopnse.toCharArray();
                                                                        if (iIndex >= 0 && eIndex >= 0 && eIndex > iIndex)
                                                                            JsoupResopnse = String.copyValueOf(jChar, iIndex, (eIndex - iIndex));
                                                                    } catch (IOException e) {
                                                                        if (e instanceof SocketTimeoutException) {
                                                                            ExceptionCode = 1;
                                                                            return null;
                                                                        }
                                                                        if (e instanceof HttpStatusException) {
                                                                            ExceptionCode = 2;
                                                                            return null;
                                                                        }
                                                                    } finally {
                                                                    }
                                                                    return null;
                                                                }

                                                                @Override
                                                                protected void onPostExecute(String link) {
                                                                    int JsoupResp = -99;
                                                                    try {
                                                                        JsoupResp = Integer.valueOf(JsoupResopnse);
                                                                    } catch (NumberFormatException e) {

                                                                    }
                                                                    if (ExceptionCode == 1)
                                                                        Toast.makeText(getContext(), "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                                                                    else if (ExceptionCode == 2)
                                                                        Toast.makeText(getContext(), "Some server related issue occurred..please try again later ", Toast.LENGTH_SHORT).show();
                                                                    else if (JsoupResopnse.isEmpty())
                                                                        Toast.makeText(getContext(), "Something went wrong..sorry", Toast.LENGTH_SHORT).show();
                                                                    progressdlg.dismiss();

                                                                    if (JsoupResopnse.compareTo("<int xmlns=\"http://tempuri.org/\">1</int>") == 0 || JsoupResp > 0) {
                                                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                                                        editor.putBoolean(Main.LOGGEDIN, true);
                                                                        editor.putBoolean(Main.ISREGISTRED, true);
                                                                        editor.apply();
                                                                        loginbyWhich = 0;
                                                                        ProgressDialog progressdlg = new ProgressDialog(getContext());
                                                                        progressdlg.setMessage("Restarting Application");
                                                                        progressdlg.setTitle("Login done,Please Wait...");
                                                                        progressdlg.setCancelable(false);
                                                                        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                                        progressdlg.setIndeterminate(true);
                                                                        progressdlg.show();

                                                                        new CountDownTimer(3000, 1000) {

                                                                            public void onTick(long millisUntilFinished) {
                                                                            }

                                                                            public void onFinish() {
                                                                                Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
                                                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                startActivity(i);
                                                                            }
                                                                        }.start();
                                                                    } else if (JsoupResopnse.compareTo("<int xmlns=\"http://tempuri.org/\">0</int>") == 0 || JsoupResp == 0) {
                                                                        Toast.makeText(getContext(), "Email not registered", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            }.execute();
                                                        } else {
                                                            Toast.makeText(getContext(), "Entered Email or Mobile number is not valid", Toast.LENGTH_LONG).show();
                                                            progressdlg.dismiss();
                                                        }
                                                    }
                                                }else{
                                                    Toast.makeText(getContext(), "Some details were not available in your facebook account", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            catch (Exception e){
                                                Toast.makeText(getContext(),"Failed to retrieve info from facebook",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(getContext(),"Login Cancel",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Toast.makeText(getContext(),"Login Error",Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        // Inflate the layout for this fragment
        emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        accounts = AccountManager.get(getContext()).getAccounts();
        sharedpreferences = getActivity().getSharedPreferences(USERLOGINDETAILS, Context.MODE_PRIVATE);
        userName = (TextView) view.findViewById(R.id.userName);
        userEmail = (TextView) view.findViewById(R.id.userEmail);
        userNumber = (TextView) view.findViewById(R.id.userNumber);
        password = (TextView) view.findViewById(R.id.password);
        comfirmpassword = (TextView) view.findViewById(R.id.comfirmpassword);
        signUp = (Button) view.findViewById(R.id.signup);
        signUp.setOnClickListener(this);
        login = (Button) view.findViewById(R.id.noAction);
        login.setOnClickListener(this);
        gSignup = (Button) view.findViewById(R.id.google);
        gSignup.setOnClickListener(this);
        twittersignup= (Button) view.findViewById(R.id.twitter);
        twittersignup.setOnClickListener(this);
        fbSignup = (Button) view.findViewById(R.id.facebook_normal);
        fbSignup.setOnClickListener(this);
        mSpinner = (EditText) view.findViewById(R.id.spinner);



        /*//Fill spinner
        String[] arraySpinner=new String[] {
                "Select your city", "Delhi", "Mumbai", "Bangalore", "One more city"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        mSpinner.setAdapter(adapter);*/
        return view;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.signup: {
                final String userNames = userName.getText().toString();
                final String emails = userEmail.getText().toString();
                final String pnumbers = userNumber.getText().toString();
                final String passWord = password.getText().toString();
                final String confirmpassWord = comfirmpassword.getText().toString();
                final String city = mSpinner.getText().toString();
                    String errorString = "Please correct the following errors...\r\n";
                    boolean validEmail = isValidEmail(emails);
                    boolean validNumber = isValidNumber(pnumbers);
                    boolean validUsername = isValidName(userNames);
                    boolean validPassword = isValidPassword(passWord);
                    boolean validCity=false;
                if (!password.isEnabled()) {
                    validPassword = true;
                }
                    int validSpinner=mSpinner.getText().toString().length();
                if (!validUsername)
                        errorString = errorString + ">Invalid Username (6 Char min)\r\n";
                if (!validEmail)
                        errorString = errorString + ">Invalid email address\r\n";
                if (!validNumber)
                        errorString = errorString + ">Invalid mobile number\r\n";
                if (!validPassword)
                        errorString = errorString + ">Invalid Password\r\n";
                    if(passWord.compareTo(confirmpassWord)!=0){
                        validPassword=false;
                        errorString = errorString + ">Passwords do not match!\r\n";
                    }
                    if(validSpinner<=2){
                        errorString = errorString + ">City not entered\r\n";
                        validCity=false;
                    }else{
                        validCity=true;
                    }
                if (validEmail && validNumber && validUsername && validPassword && validCity) {
                        //Post details to server
                        Initilization.androidId = android.provider.Settings.Secure.getString(getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                        String urlStr="http://rssapi.psweb.in/everapi.asmx/RegisterUser?FullName=" + userNames + "&Email=" + emails + "&Password=" + passWord + "&Mobile=" + pnumbers + "&AndroidId=" + Initilization.androidId+"&City="+city;
                        URL url=null;
                        try{
                            url = new URL(urlStr);
                            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                            url = uri.toURL();
                        }
                        catch(Exception e){}
                        final String urlRequest = url.toString();
                        Main.progress.setVisibility(View.VISIBLE);
                        new AsyncTask<Void, Void, String>() {
                            int ExceptionCode = 0;
                            String JsoupResopnse = "";
                            @Override
                            protected String doInBackground(Void... params) {
                                try {
                                    JsoupResopnse = Jsoup.connect(urlRequest).timeout(Initilization.timeout).ignoreContentType(true).execute().body();
                                    int iIndex = JsoupResopnse.indexOf("\">") + 2;
                                    int eIndex = JsoupResopnse.indexOf("</");
                                    char jChar[] = JsoupResopnse.toCharArray();
                                    if (iIndex >= 0 && eIndex >= 0 && eIndex > iIndex)
                                        JsoupResopnse = String.copyValueOf(jChar, iIndex, (eIndex - iIndex));
                                } catch (IOException e) {
                                    if (e instanceof SocketTimeoutException) {
                                        ExceptionCode = 1;
                                        return null;
                                    }
                                    if (e instanceof HttpStatusException) {
                                        ExceptionCode = 2;
                                        return null;
                                    }
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(String link) {
                                if (ExceptionCode > 0) {
                                    if (ExceptionCode == 1)
                                        Toast.makeText(getContext(), "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                                    if (ExceptionCode == 2)
                                        Toast.makeText(getContext(), "Some server related issue occurred..please try again later", Toast.LENGTH_SHORT).show();
                                    Main.progress.setVisibility(View.GONE);
                                }
                                int JsoupResp=-99;
                                try{
                                    JsoupResp=Integer.valueOf(JsoupResopnse);
                                }catch (NumberFormatException e){}
                                if (!JsoupResopnse.isEmpty() &&  JsoupResp > 0) {
                                    //Store all user data into shared prefrence
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(Main.USERNAME, userNames);
                                    editor.putInt(Main.USERID, JsoupResp);
                                    editor.putString(Main.USEREMAIL, emails);
                                    editor.putString(Main.USERPHONENUMBER, pnumbers);
                                    editor.putBoolean(Main.ISREGISTRED, true);
                                    editor.putBoolean(Main.LOGGEDIN, true);
                                    editor.apply();
                                    Toast.makeText(getContext(), "Registration complete your UserId is " + JsoupResopnse, Toast.LENGTH_LONG).show();
                                    userName.setText("");
                                    userEmail.setText("");
                                    userNumber.setText("");
                                    password.setText("");
                                    comfirmpassword.setText("");
                                    mSpinner.setText("");
                                    Main.progress.setVisibility(View.GONE);
                                    ProgressDialog progressdlg = new ProgressDialog(getContext());
                                    progressdlg.setMessage("Restarting Application");
                                    progressdlg.setTitle("Login done,Please Wait...");
                                    progressdlg.setCancelable(false);
                                    progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressdlg.setIndeterminate(true);
                                    progressdlg.show();
                                    /**Restart application**/
                                    new CountDownTimer(3000, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                        }

                                        public void onFinish() {
                                            Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(i);
                                        }
                                    }.start();
                                    /**END**/
                                } else if(ExceptionCode==0){
                                    Toast.makeText(getContext(), "Please check the filled details", Toast.LENGTH_SHORT).show();
                                    Main.progress.setVisibility(View.GONE);
                                }
                            }
                        }.execute();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(errorString)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        return;
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            break;
            case R.id.noAction: {
                LayoutInflater linf = LayoutInflater.from(getContext());

                final View inflator = linf.inflate(R.layout.login_dialog, null);

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final AlertDialog alert = builder.create();

                final LayoutInflater inflater = getActivity().getLayoutInflater();

                fLogin = (Button) inflator.findViewById(R.id.facebook_login);
                gLogin = (Button) inflator.findViewById(R.id.google_login);
                tLogin = (Button) inflator.findViewById(R.id.twitter_login);
                loginButton = (Button) inflator.findViewById(R.id.login_button);
                cancelButton = (Button) inflator.findViewById(R.id.cancel_button);
                uName = (TextView) inflator.findViewById(R.id.username);
                pwd = (TextView) inflator.findViewById(R.id.password);


                fLogin.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        loginbyWhich = 1;     /** one for social logins**/
                        LoginManager.getInstance().logInWithReadPermissions(SignUp.this, Arrays.asList("public_profile", "user_friends", "email"));
                        uName.setText(email);
                        pwd.setHint("Password is not needed for Facebook login");
                        pwd.setEnabled(false);
                    }
                });

                gLogin.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Toast.makeText(getContext(),"Clicked glogin",Toast.LENGTH_LONG).show();
                        loginbyWhich = 1;
                        String email = getgoogleMailId();
                        uName.setText(email);
                        pwd.setHint("Password is not needed for Google login");
                        pwd.setEnabled(false);
                        {
                            String usernameoremail = uName.getText().toString();
                            String pass = pwd.getText().toString();
                            String urlStr = "";
                            if (loginbyWhich == 1)
                                urlStr = "http://rssapi.psweb.in/everapi.asmx/ValidateSocialLogin?EmailorMobile=" + usernameoremail;
                            else if (loginbyWhich == 0)
                                urlStr = "http://rssapi.psweb.in/everapi.asmx/ValidateLogin?EmailorMobile=" + usernameoremail + "&Password=" + pass;
                            URL url = null;
                            try {
                                url = new URL(urlStr);
                                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                                url = uri.toURL();
                            } catch (Exception e) {/****/}
                            final String urlRequest = url.toString() + "";
                            final ProgressDialog progressdlg;
                            progressdlg = new ProgressDialog(getContext());
                            progressdlg.setMessage("Connecting to server...");
                            progressdlg.setTitle("Authentication");
                            progressdlg.setCancelable(false);
                            progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressdlg.setIndeterminate(true);
                            progressdlg.show();
                            if (isValidEmail(usernameoremail) || isValidNumber(usernameoremail)) {
                                new AsyncTask<Void, Integer, String>() {
                                    int ExceptionCode = 0;
                                    String JsoupResopnse = "";

                                    @Override
                                    protected void onProgressUpdate(Integer... text) {
                                        if (text[0] == 2)
                                            progressdlg.setMessage("Connecting to server...");
                                        if (text[0] == 3)
                                            progressdlg.setMessage("Authenticating");
                                    }

                                    @Override
                                    protected String doInBackground(Void... params) {
                                        try {
                                            publishProgress(3);
                                            JsoupResopnse = Jsoup.connect(urlRequest).timeout(Initilization.timeout - 5).ignoreContentType(true).execute().body();
                                            int iIndex = JsoupResopnse.indexOf("\">") + 2;
                                            int eIndex = JsoupResopnse.indexOf("</");
                                            char jChar[] = JsoupResopnse.toCharArray();
                                            if (iIndex >= 0 && eIndex >= 0 && eIndex > iIndex)
                                                JsoupResopnse = String.copyValueOf(jChar, iIndex, (eIndex - iIndex));
                                        } catch (IOException e) {
                                            if (e instanceof SocketTimeoutException) {
                                                ExceptionCode = 1;
                                                return null;
                                            }
                                            if (e instanceof HttpStatusException) {
                                                ExceptionCode = 2;
                                                return null;
                                            }
                                        } finally {
                                        }
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(String link) {
                                        int JsoupResp = -99;
                                        try {
                                            JsoupResp = Integer.valueOf(JsoupResopnse);
                                        } catch (NumberFormatException e) {

                                        }
                                        if (ExceptionCode == 1)
                                            Toast.makeText(getContext(), "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                                        else if (ExceptionCode == 2)
                                            Toast.makeText(getContext(), "Some server related issue occurred..please try again later", Toast.LENGTH_SHORT).show();
                                        else if (JsoupResopnse.isEmpty())
                                            Toast.makeText(getContext(), "Something went wrong..sorry", Toast.LENGTH_SHORT).show();
                                        progressdlg.dismiss();

                                        if (JsoupResopnse.compareTo("<int xmlns=\"http://tempuri.org/\">1</int>") == 0 || JsoupResp > 0) {
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putBoolean(Main.LOGGEDIN, true);
                                            editor.putBoolean(Main.ISREGISTRED, true);
                                            editor.apply();
                                            loginbyWhich = 0;
                                            ProgressDialog progressdlg = new ProgressDialog(getContext());
                                            progressdlg.setMessage("Restarting Application");
                                            progressdlg.setTitle("Login done,Please Wait...");
                                            progressdlg.setCancelable(false);
                                            progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                            progressdlg.setIndeterminate(true);
                                            progressdlg.show();

                                            new CountDownTimer(3000, 1000) {

                                                public void onTick(long millisUntilFinished) {
                                                }

                                                public void onFinish() {
                                                    Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);
                                                }
                                            }.start();
                                        } else if (JsoupResopnse.compareTo("<int xmlns=\"http://tempuri.org/\">0</int>") == 0 || JsoupResp == 0) {
                                            Toast.makeText(getContext(), "Email not registered", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }.execute();
                            } else {
                                Toast.makeText(getContext(), "Entered Email or Mobile number is not valid", Toast.LENGTH_LONG).show();
                                progressdlg.dismiss();
                            }
                        }
                    }
                });

                tLogin.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Twitter login coming soon!", Toast.LENGTH_LONG).show();
                        //loginbyWhich=1;
                    }
                });

                loginButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String usernameoremail = uName.getText().toString();
                        String pass = pwd.getText().toString();
                        String urlStr = "";
                        if (loginbyWhich == 1)
                            urlStr = "http://rssapi.psweb.in/everapi.asmx/ValidateSocialLogin?EmailorMobile=" + usernameoremail;
                        else if (loginbyWhich == 0)
                            urlStr = "http://rssapi.psweb.in/everapi.asmx/ValidateLogin?EmailorMobile=" + usernameoremail + "&Password=" + pass;
                        URL url = null;
                        try {
                            url = new URL(urlStr);
                            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                            url = uri.toURL();
                        } catch (Exception e) {/****/}
                        final String urlRequest = url.toString() + "";
                        final ProgressDialog progressdlg;
                        progressdlg = new ProgressDialog(getContext());
                        progressdlg.setMessage("Connecting to server...");
                        progressdlg.setTitle("Authentication");
                        progressdlg.setCancelable(false);
                        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressdlg.setIndeterminate(true);
                        progressdlg.show();
                        if (isValidEmail(usernameoremail) || isValidNumber(usernameoremail)) {
                            new AsyncTask<Void, Integer, String>() {
                                int ExceptionCode = 0;
                                String JsoupResopnse = "";

                                @Override
                                protected void onProgressUpdate(Integer... text) {
                                    if (text[0] == 2)
                                        progressdlg.setMessage("Connecting to server...");
                                    if (text[0] == 3)
                                        progressdlg.setMessage("Authenticating");
                                }

                                @Override
                                protected String doInBackground(Void... params) {
                                    try {
                                        publishProgress(3);
                                        JsoupResopnse = Jsoup.connect(urlRequest).timeout(Initilization.timeout - 5).ignoreContentType(true).execute().body();
                                        int iIndex = JsoupResopnse.indexOf("\">") + 2;
                                        int eIndex = JsoupResopnse.indexOf("</");
                                        char jChar[] = JsoupResopnse.toCharArray();
                                        if (iIndex >= 0 && eIndex >= 0 && eIndex > iIndex)
                                            JsoupResopnse = String.copyValueOf(jChar, iIndex, (eIndex - iIndex));
                                    } catch (IOException e) {
                                        if (e instanceof SocketTimeoutException) {
                                            ExceptionCode = 1;
                                            return null;
                                            }
                                        if (e instanceof HttpStatusException) {
                                            ExceptionCode = 2;
                                            return null;
                                        }
                                    } finally {
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(String link) {
                                    int JsoupResp = -99;
                                    try {
                                        JsoupResp = Integer.valueOf(JsoupResopnse);
                                    } catch (NumberFormatException e) {

                                    }
                                    if (ExceptionCode == 1)
                                        Toast.makeText(getContext(), "Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                                    else if (ExceptionCode == 2)
                                        Toast.makeText(getContext(), "Some server related issue occurred..please try again later", Toast.LENGTH_SHORT).show();
                                    else if (JsoupResopnse.isEmpty())
                                        Toast.makeText(getContext(), "Something went wrong..sorry", Toast.LENGTH_SHORT).show();
                                    progressdlg.dismiss();

                                    if (JsoupResopnse.compareTo("<int xmlns=\"http://tempuri.org/\">1</int>") == 0 || JsoupResp > 0) {
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putBoolean(Main.LOGGEDIN, true);
                                        editor.putBoolean(Main.ISREGISTRED, true);
                                        editor.apply();
                                        loginbyWhich = 0;
                                        ProgressDialog progressdlg = new ProgressDialog(getContext());
                                        progressdlg.setMessage("Restarting Application");
                                        progressdlg.setTitle("Login done,Please Wait...");
                                        progressdlg.setCancelable(false);
                                        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressdlg.setIndeterminate(true);
                                        progressdlg.show();

                                        new CountDownTimer(3000, 1000) {

                                            public void onTick(long millisUntilFinished) {
                                            }

                                            public void onFinish() {
                                                Intent i = getContext().getPackageManager().getLaunchIntentForPackage(getContext().getPackageName());
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);
                                            }
                                        }.start();
                                    } else if (JsoupResopnse.compareTo("<int xmlns=\"http://tempuri.org/\">0</int>") == 0 || JsoupResp == 0) {
                                        Toast.makeText(getContext(), "Email not registered", Toast.LENGTH_LONG).show();
                                        }
                                }
                            }.execute();
                        } else {
                            Toast.makeText(getContext(), "Entered Email or Mobile number is not valid", Toast.LENGTH_LONG).show();
                            progressdlg.dismiss();
                            }
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // alert.dismiss();
                    }
                });
                builder.setView(inflator).show();
            }
            break;
            case R.id.facebook_normal: {
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
            }
            break;
            case R.id.google: {
                String fullname=getUsername();
                String email=getgoogleMailId();
                userName.setText(fullname);
                userEmail.setText(email);
                if(fullname.length()>0 && isValidEmail(email)) {
                    password.setHint("Password is not needed for Google login");
                    comfirmpassword.setHint("Password is not needed for Google login");
                    password.setEnabled(false);
                    comfirmpassword.setEnabled(false);
                }
                else{
                    Toast.makeText(getContext(), "Some details were not available in your google account", Toast.LENGTH_LONG).show();
                }
            }
            break;
            case R.id.twitter: {
                Toast.makeText(getContext(), "Twitter coming soon", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    public boolean isValidNumber(String inString) {
        return inString.length() == 10;
    }

    public boolean isValidName(String inString){
        return inString.length() >= 6;
    }

    public boolean isValidPassword(String inString){
        return inString.length() >= 6;
    }

    public String getUsername() {
        AccountManager manager = AccountManager.get(getContext());
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");
            if (parts.length > 0 && parts[0] != null)
                return parts[0];
            else
                return "";
        } else
            return "";
    }

    public String getgoogleMailId(){
        AccountManager manager = AccountManager.get(getContext());
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            possibleEmails.add(account.name);
        }
        if(possibleEmails.size()>0)
            return possibleEmails.get(0);
        else
            return "";
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
