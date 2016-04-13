package com.evernews.evernews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

public class CustomExpandAdapter extends BaseExpandableListAdapter {

    private List<String> parentRecord;
    private HashMap<String, List<ExpandListViewModel>> childRecord;
    private LayoutInflater inflater = null;
    private Activity mContext;

    public CustomExpandAdapter(Activity context, List<String> parentList, HashMap<String, List<ExpandListViewModel>> childList) {
        this.parentRecord = parentList;
        this.childRecord = childList;
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ExpandListViewModel getChild(int groupPosition, int childPosition) {
        return this.childRecord.get(getGroup(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ExpandListViewModel childConfig = getChild(groupPosition, childPosition);

        ViewHolder holder;
        try {
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.list_view_single_item_for_category_tab, null);
                holder.childTitle = (TextView) convertView.findViewById(R.id.channelTitle);
                holder.childDesc = (TextView) convertView.findViewById(R.id.channelMeta);
                holder.channelLogo = (ImageView) convertView.findViewById(R.id.channel_logo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.childTitle.setText(childConfig.getTitle());
            holder.childDesc.setText(childConfig.getDesc());
            Glide.with(mContext).load(childConfig.getIcon()).placeholder(R.drawable.ic_launcher).into(holder.channelLogo);
        } catch (Exception e) {
        }
        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String parentSampleTo = parentRecord.get(groupPosition);

        ViewHolder holder;

        try {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.category_parent_name, null);
                holder = new ViewHolder();
                holder.parentTitle = (TextView) convertView.findViewById(R.id.parent_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.parentTitle.setText(parentSampleTo);

        } catch (Exception e) {
        }
        return convertView;
    }

    public static class ViewHolder {

        private TextView childTitle;
        private TextView childDesc;
        private ImageView channelLogo;
        // Content
        private TextView parentTitle;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childRecord.get(( getGroup(groupPosition))).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return this.parentRecord.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.parentRecord.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
