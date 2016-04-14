package com.evernews.evernews;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsAddListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsAddListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsAddListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TYPE_KEY = "type_key";
    private static final String TAB_NAME = "tab_name";
    private static View rootView;
    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    private String predefCats[]={"Featured","Popular","Categories"};
    public NewsAddListFragment() {
        // Required empty public constructor
    }

    public static NewsAddListFragment newInstance(int sectionNumber, String tabName) {
        Bundle args = new Bundle();
        args.putSerializable(TYPE_KEY, sectionNumber);
        args.putSerializable(TAB_NAME, tabName);
        NewsAddListFragment fragment = new NewsAddListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsAddListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsAddListFragment newInstanceREE(int param1, String param2) {
        NewsAddListFragment fragment = new NewsAddListFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_KEY, param1);
        args.putString(TAB_NAME, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(TYPE_KEY);
            mParam2 = getArguments().getString(TAB_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_tab, container, false);
        //new GetCategoryList().execute();
        final ListView listView = (ListView)rootView.findViewById(R.id.list);
        final List<ListItemObject> allItems = loadDataTab1(getArguments().getInt(TYPE_KEY));
        CustomListAdapter customAdapter = new CustomListAdapter(getActivity(), allItems);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String RSSUID = allItems.get(position).getChannelRSSID();
                new AddTabNewsPreview().setRSSUID(RSSUID).setChannelDetails(allItems.get(position).getChannelMeta()).setImageURL(allItems.get(position).getChannelLogo()).setListener(new AddTabNewsPreview.AddListener() {
                    @Override
                    public void onAdd(AddTabNewsPreview dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onRemove(AddTabNewsPreview dialog) {
                        dialog.dismiss();
                    }
                }).show(getFragmentManager(), "#add");
            }
        });
        return rootView;
    }

    private List<ListItemObject> loadDataTab1(int ii) {
        List<ListItemObject> items = new ArrayList<>();
        try {
            if (ii == 0) {
                for (int i = 0; i < Main.catListArray.length; i++) {
                    if (Main.catListArray[i][7].compareToIgnoreCase(predefCats[0]) == 0) {
                        String channelLogo = Main.catListArray[i][3];
                        String channelTitle = Main.catListArray[i][6];
                        String channelMeta = Main.catListArray[i][4];
                        String channelRSSID = Main.catListArray[i][0];
                        String categoryType = Main.catListArray[i][7];
                        String channelRSSURL = Main.catListArray[i][2];
                        items.add(new ListItemObject(channelLogo, channelTitle, channelMeta, channelRSSID, categoryType,channelRSSURL));
                    }
                }
            } else if (ii == 1) {
                for (int i = 0; i < Main.catListArray.length; i++) {
                    if (Main.catListArray[i][7].compareToIgnoreCase(predefCats[1]) == 0) {
                        String channelLogo = Main.catListArray[i][3];
                        String channelTitle = Main.catListArray[i][6];
                        String channelMeta = Main.catListArray[i][4];
                        String channelRSSID = Main.catListArray[i][0];
                        String categoryType = Main.catListArray[i][7];
                        String channelRSSURL = Main.catListArray[i][2];
                        items.add(new ListItemObject(channelLogo, channelTitle, channelMeta, channelRSSID, categoryType,channelRSSURL));
                    }
                }
            } else {
                for (int i = 0; i < Main.catListArray.length; i++) {
                    if (Main.catListArray[i][7].compareToIgnoreCase(predefCats[1]) == 0) {continue;}
                    else
                    if (Main.catListArray[i][7].compareToIgnoreCase(predefCats[0]) == 0) {continue;}
                    else{
                        String channelLogo = Main.catListArray[i][3];
                        String channelTitle = Main.catListArray[i][6];
                        String channelMeta = Main.catListArray[i][4];
                        String channelRSSID = Main.catListArray[i][0];
                        String categoryType = Main.catListArray[i][7];
                        String channelRSSURL = Main.catListArray[i][2];
                        items.add(new ListItemObject(channelLogo, channelTitle, channelMeta, channelRSSID, categoryType,channelRSSURL));
                    }
                }
            }
        }catch(Exception e){}
        return items;
    }


    class GetCategoryList extends AsyncTask<Void,Integer,Void>
    {
        ProgressDialog progressDlg;
        String content;
        int ExceptionCode=0;

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

        @Override
        protected void onPreExecute()
        {
            progressDlg = ProgressDialog.show(getContext(), "Connecting", "Please wait while we connect to our servers", true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            if(!Main.validCategory) {
                try {
                    publishProgress(1);
                    String fetchLink = "http://rssapi.psweb.in/everapi.asmx/GetNewsChannelList";//Over ride but should be Main.androidId
                    content = Jsoup.connect(fetchLink).ignoreContentType(true).timeout(Initilization.timeout).execute().body();
                    if (content.length() > 50)
                        publishProgress(2);
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if(content!=null)
            {
                String result = content.toString().replaceAll("&lt;", "<").replaceAll("&gt;",">").replaceAll("&amp;","&");
                // Log.d("response", result);
                //after getting the response we have to parse it
                parseResultsList(result);
            }
            progressDlg.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    public void parseResultsList(String response)
    {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(response, "", org.jsoup.parser.Parser.xmlParser());
        try {
            for (int i = 0; i < 15; i++) {
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
                    for (org.jsoup.nodes.Element e : jsoupDoc.select("NewsType")) {
                        Main.catListArray[index][7] = e.text();
                        index++;
                    }
                }
            }
            Main.validCategory=true;
        }catch (Exception e){
            Main.validCategory=false;
        }
    }
}