package com.infoecos.cn.destiny.app.utils;

import java.util.Map;

import android.os.AsyncTask;

public abstract class ListDataFetchTask extends
		AsyncTask<String, Integer, Map<String, String>> {
	private ProgressDialoge progress;
	private InfoExpandableListAdapter mAdapter;
	private String query;

	public ListDataFetchTask(ProgressDialoge progress,
			InfoExpandableListAdapter mAdapter, String query) {
		this.progress = progress;
		this.mAdapter = mAdapter;
		this.query = query;
	}

	@Override
	protected void onPreExecute() {
		if (progress != null)
			progress.showDialog();
	}

	@Override
	protected Map<String, String> doInBackground(String... params) {
		try {
			return getData();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected abstract Map<String, String> getData() throws Exception;

	@Override
	protected void onPostExecute(Map<String, String> result) {
		mAdapter.setData(result, query);
		if (progress != null)
			progress.closeDialog();
	}
}