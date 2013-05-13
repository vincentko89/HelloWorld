package com.uiproject.headliner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.uiproject.headliner.data.Data;
import com.uiproject.headliner.view.DragListView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class DragListActivity extends Activity {

	private DragListAdapter adapter = null;
	private List<HashMap<String, Object>> mapList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_list_activity);

		mapList = Data.topicList;

		DragListView dragListView = (DragListView) findViewById(R.id.drag_list);
		adapter = new DragListAdapter(this);
		dragListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.setting_menu, menu);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			Intent intent = new Intent(this, HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		case R.id.done: {
			Intent intent = new Intent(this, HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class DragListAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public DragListAdapter(final Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void remove(HashMap<String, Object> map) {
			mapList.remove(map);
		}

		public void insert(HashMap<String, Object> map, int location) {
			mapList.add(location, map);
			this.notifyDataSetChanged();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.drag_list_item, null);
				holder.checkbox = (CheckBox) convertView
						.findViewById(R.id.topicCheckBox);
				holder.text = (TextView) convertView
						.findViewById(R.id.drag_list_item_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			HashMap<String, Object> map = mapList.get(position);
			if ((Boolean) map.get(Data.CHECKED))
				holder.checkbox.setChecked(true);
			else
				holder.checkbox.setChecked(false);
			holder.checkbox
					.setOnClickListener(new MyOnClickListener(position));

			holder.text.setText((String) map.get(Data.TOPICS));
			return convertView;
		}

		public int getCount() {
			return mapList.size();
		}

		public HashMap<String, Object> getItem(int position) {
			return mapList.get(position);
		}

		public long getItemId(int position) {
			return 0;
		}

		private class MyOnClickListener implements OnClickListener {
			private HashMap<String, Object> map;

			public MyOnClickListener(int position) {
				map = mapList.get(position);
			}
			
			public void onClick(View view) {
				CheckBox checkbox = (CheckBox) view;
				if(checkbox.isChecked()) {
					map.remove("checked");
					map.put("checked", true);
				}else {
					map.remove("checked");
					map.put("checked", false);
				}
				notifyDataSetChanged();
			}		
		}

		private class ViewHolder {
			public CheckBox checkbox;
			public TextView text;
		}
	}
}