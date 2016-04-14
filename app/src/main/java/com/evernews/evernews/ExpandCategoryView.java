package com.evernews.evernews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExpandCategoryView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExpandCategoryView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpandCategoryView extends Fragment {
    private DrawerLayout mDrawerLayout;
    CustomExpandAdapter customAdapter;
    private int lastExpandedPosition = -1;
    private ExpandableListView mDrawerList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String predefCats[]={"Featured","Popular","Categories"};
    private static View rootView;
    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    private ArrayList <String> parentNames=new ArrayList<>();
    private ArrayList <ListItemObject> childItems=new ArrayList<>();
    public ExpandCategoryView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpandCategoryView.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpandCategoryView newInstance(int param1, String param2) {
        ExpandCategoryView fragment = new ExpandCategoryView();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_expand_category_view, container, false);/**Filling Drawer List View**/
        //navDrawerView = (LinearLayout) findViewById(R.id.navDrawerView);
        ArrayList <ListItemObject> mPlanetTitles=new ArrayList<>();
        HashMap<String, List<ListItemObject>> listDataChild=new HashMap<String, List<ListItemObject>>();;
        ArrayList <String>listParent = new ArrayList<>();
        for(int i=0;i<10;i++){
            //mPlanetTitles.add(new ListItemObject("Sub Product\t"+i,"URL"+i,"DESC"+i));
        }
        // mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList = (ExpandableListView) rootView.findViewById(R.id.category_exp_list);


        parentNames=getParentNames();
        listParent.addAll(parentNames);
        /*listParent.add("Cat 2");
        listParent.add("Cat 3");
        listParent.add("Cat N");
        listParent.add("Cat N2");*/

        for(int i=0;i<parentNames.size();i++){
            listDataChild.put(parentNames.get(i),getChildNames(parentNames.get(i)));
        }
        /*listDataChild.put("Cat 1", mPlanetTitles);
        listDataChild.put("Cat 2", mPlanetTitles);
        listDataChild.put("Cat 3", mPlanetTitles);
        listDataChild.put("Cat N", mPlanetTitles);
        listDataChild.put("Cat N2", mPlanetTitles);*/

        customAdapter = new CustomExpandAdapter(getActivity(), listParent, listDataChild);
        // setting list adapter
        mDrawerList.setAdapter(customAdapter);
        mDrawerList.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
        mDrawerList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    mDrawerList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        /**END**/

        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(groupPosition));
                parent.setItemChecked(index, true);
               // String parentTitle = (customAdapter.getGroup(groupPosition));
                return false;
            }
        });

        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Log.d("CHECK", "child click");

                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);
                parent.setItemChecked(index, true);
                String parentTitle = customAdapter.getGroup(groupPosition);
                String childTitle= customAdapter.getChild(groupPosition,childPosition).getChannelTitle();
                String imagUrl= customAdapter.getChild(groupPosition,childPosition).getChannelLogo();
                String meta= customAdapter.getChild(groupPosition,childPosition).getChannelMeta();
                String RSSUID=customAdapter.getChild(groupPosition,childPosition).getChannelRSSID();
                //Toast.makeText(getContext(),"Clicked at parent\t"+parentTitle+"\tChild at \t"+childTitle,Toast.LENGTH_LONG).show();

                //String RSSUID = allItems.get(position).getChannelRSSID();
                new AddTabNewsPreview().setRSSUID(RSSUID).setChannelDetails(meta).setListener(new AddTabNewsPreview.AddListener() {
                    @Override
                    public void onAdd(AddTabNewsPreview dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onRemove(AddTabNewsPreview dialog) {
                        dialog.dismiss();
                    }
                }).show(getFragmentManager(), "#add");
                return false;
            }
        });
        return rootView;
    }

    private ArrayList<String> getParentNames(){
        ArrayList <String>names=new ArrayList<>();
        for (int i = 0; i < Main.catListArrayLength; i++) {
            if (Main.catListArray[i][7].compareToIgnoreCase("NULL") != 0 && Main.catListArray[i][7].compareToIgnoreCase(predefCats[0]) != 0 && Main.catListArray[i][7].compareToIgnoreCase(predefCats[1])!=0) {
                names.add(Main.catListArray[i][7]);
            }
        }

        Set<String> hs = new LinkedHashSet<>();
        hs.addAll(names);
        names.clear();
        names.addAll(hs);
        return(names);
    }

    private ArrayList<ListItemObject> getChildNames(String assertAgainst){
        ArrayList <ListItemObject>names=new ArrayList<>();

        for(int i=0;i<Main.catListArrayLength;i++){
            if(Main.catListArray[i][7].compareToIgnoreCase(assertAgainst) == 0) {
                String channelLogo = Main.catListArray[i][3];
                String channelTitle = Main.catListArray[i][6];
                String channelMeta = Main.catListArray[i][4];
                String channelRSSID = Main.catListArray[i][0];
                String categoryType = Main.catListArray[i][7];
                String channelRSSURL = Main.catListArray[i][2];
                names.add(new ListItemObject(channelLogo, channelTitle, channelMeta, channelRSSID, categoryType,channelRSSURL));
            }
        }
        return names;
    }
}
