package com.infoecos.cn.destiny.app.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class InfoExpandableListAdapter extends BaseExpandableListAdapter {
	private String[] groups = {};
	private Map<String, String> _data, data;
	private Context context;

	public InfoExpandableListAdapter(Context context) {
		this.context = context;
	}

	public void setData(Map<String, String> data) {
		setData(data, "");
	}

	public void setData(Map<String, String> data, String query) {
		this._data = data;
		updateQuery(query);
	}

	public TextView getGenericView() {
		// Layout parameters for the ExpandableListView
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 64);

		TextView textView = new TextView(context);
		textView.setLayoutParams(lp);
		// Center the text vertically
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		// Set the text starting position
		textView.setPadding(64, 0, 0, 0);
		return textView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return data.get(groups[groupPosition]).split("\n")[childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TextView textView = getGenericView();
		textView.setText(getChild(groupPosition, childPosition).toString());
		return textView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return "".equals(data.get(groups[groupPosition])) ? 0 : data.get(
				groups[groupPosition]).split("\n").length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups[groupPosition];
	}

	@Override
	public int getGroupCount() {
		return groups.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView textView = getGenericView();
		textView.setText(getGroup(groupPosition).toString());
		return textView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void updateQuery(String query) {
		data = new HashMap<String, String>();
		if (_data != null) {
			for (String key : _data.keySet()) {
				StringBuilder val = new StringBuilder();
				for (String v : _data.get(key).split("\n")) {
					if (null == query || "".equals(query)
							|| v.indexOf(query) >= 0)
						val.append(String.format("%s\n", v));
				}
				if ("".equals(val.toString()))
					continue;
				data.put(key, val.toString());
			}
		}

		groups = new String[data.keySet().size()];
		int i = 0;
		for (String key : data.keySet()) {
			groups[i] = key;
			++i;
		}
		notifyDataSetChanged();
	}
}