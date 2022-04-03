package com.infoecos.cn.destiny.app.ui;

import java.util.Map;

import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.os.Bundle;

import com.infoecos.cn.destiny.app.Destiny;
import com.infoecos.cn.destiny.app.utils.InfoExpandableListAdapter;
import com.infoecos.cn.destiny.app.utils.ListDataFetchTask;
import com.infoecos.cn.destiny.app.utils.ProgressDialoge;

public class BaseInfo extends ExpandableListActivity {
	private ProgressDialoge progress;
	private InfoExpandableListAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerForContextMenu(getExpandableListView());

		// Set up our adapter
		mAdapter = new InfoExpandableListAdapter(this);
		setListAdapter(mAdapter);

		progress = new ProgressDialoge(this, "四柱预测", "计算中...");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return progress.onCreateDialog(id);
	}

	@Override
	protected void onResume() {
		super.onResume();

		new ListDataFetchTask(progress, mAdapter, "") {
			@Override
			protected Map<String, String> getData() throws Exception {
				return Destiny.八字(BaseInfo.this);
			}
		}.execute();
	}

	// @Override
	// public void onCreateContextMenu(ContextMenu menu, View v,
	// ContextMenuInfo menuInfo) {
	// menu.setHeaderTitle("Sample menu");
	// menu.add(0, 0, 0, "Columns");
	// }
	//
	// @Override
	// public boolean onContextItemSelected(MenuItem item) {
	// ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item
	// .getMenuInfo();
	//
	// String title = ((TextView) info.targetView).getText().toString();
	//
	// int type = ExpandableListView
	// .getPackedPositionType(info.packedPosition);
	// if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
	// int groupPos = ExpandableListView
	// .getPackedPositionGroup(info.packedPosition);
	// int childPos = ExpandableListView
	// .getPackedPositionChild(info.packedPosition);
	// Toast.makeText(
	// this,
	// title + ": Child " + childPos + " clicked in group "
	// + groupPos, Toast.LENGTH_SHORT).show();
	// return true;
	// } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
	// int groupPos = ExpandableListView
	// .getPackedPositionGroup(info.packedPosition);
	// Toast.makeText(this, title + ": Group " + groupPos + " clicked",
	// Toast.LENGTH_SHORT).show();
	// return true;
	// }
	//
	// return false;
	// }
}
