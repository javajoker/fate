package com.infoecos.cn.destiny.app.ui;

import java.util.Map;

import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.infoecos.cn.destiny.app.Destiny;
import com.infoecos.cn.destiny.app.utils.InfoExpandableListAdapter;
import com.infoecos.cn.destiny.app.utils.ListDataFetchTask;
import com.infoecos.cn.destiny.app.utils.ProgressDialoge;
import com.infoecos.cn.destiny.birth.R;

public class FamilyInfo extends ExpandableListActivity {

	private ProgressDialoge progress;
	private InfoExpandableListAdapter mAdapter;
	private EditText searchEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.destiny_list);

		ExpandableListView listView = getExpandableListView();
		registerForContextMenu(listView);

		// Set up our adapter
		mAdapter = new InfoExpandableListAdapter(this);
		setListAdapter(mAdapter);

		searchEditText = (EditText) findViewById(R.id.editTextSearch);
		searchEditText.setSingleLine(true);

		ImageButton searchBtn = (ImageButton) findViewById(R.id.buttonSearch);
		searchBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View b) {
				mAdapter.updateQuery(FamilyInfo.this.searchEditText.getText()
						.toString());
			}
		});

		progress = new ProgressDialoge(this, "四柱预测", "计算中...");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return progress.onCreateDialog(id);
	}

	@Override
	protected void onResume() {
		super.onResume();

		new ListDataFetchTask(progress, mAdapter, searchEditText.getText()
				.toString()) {
			@Override
			protected Map<String, String> getData() throws Exception {
				return Destiny.家人(FamilyInfo.this);
			}
		}.execute();
	}
}