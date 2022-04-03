package com.infoecos.cn.destiny.app.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.infoecos.cn.destiny.birth.R;

public class MainActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final TabHost tabHost = getTabHost();

		tabHost.addTab(tabHost
				.newTabSpec("tab0")
				.setIndicator("设置",
						getResources().getDrawable(R.drawable.settings))
				.setContent(new Intent(this, PreferenceInfo.class)));

		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator("基本信息",
						getResources().getDrawable(R.drawable.base))
				.setContent(new Intent(this, BaseInfo.class)));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator("时运预测",
						getResources().getDrawable(R.drawable.destiny))
				.setContent(new Intent(this, DestinyInfo.class)));

		tabHost.addTab(tabHost
				.newTabSpec("tab3")
				.setIndicator("命主人事",
						getResources().getDrawable(R.drawable.body))
				.setContent(new Intent(this, BodyInfo.class)));

		tabHost.addTab(tabHost
				.newTabSpec("tab4")
				.setIndicator("家人变故",
						getResources().getDrawable(R.drawable.home))
				.setContent(new Intent(this, FamilyInfo.class)));
	}

}
