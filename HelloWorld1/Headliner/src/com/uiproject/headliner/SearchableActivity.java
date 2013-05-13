package com.uiproject.headliner;

import java.util.HashMap;
import java.util.List;

import com.uiproject.headliner.data.Data;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class SearchableActivity extends TabActivity implements TabContentFactory {

	private TabHost th;
	private List<HashMap<String, Object>> topicList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        Intent intent = getIntent();
        final String query = (String) intent.getCharSequenceExtra("query");
        System.out.println(query);

        
        topicList = Data.topicList;
        Data.search(query);        
        
        th = getTabHost();
		th.setup();
		
		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				FragmentManager fm = getFragmentManager();

				FragmentTransaction ft = fm.beginTransaction();

				for (int i = 0; i < Data.topics.length; i++) {
					SearchListFragment fragment = (SearchListFragment) fm
							.findFragmentByTag(Data.topics[i]);
					if (fragment != null)
						ft.detach(fragment);
				}

				System.out.println(tabId);

				for (int i = 0; i < Data.topics.length; i++) {
					if (tabId.equalsIgnoreCase(Data.topics[i])) {
						SearchListFragment fragment = (SearchListFragment) fm
								.findFragmentByTag(Data.topics[i]);
						if (fragment == null) {
							ft.add(android.R.id.tabcontent,
									new SearchListFragment(Data.topics[i]),
									Data.topics[i]);
						} else {
							ft.attach(fragment);
						}
						break;
					}
				}
				ft.commit();
			}
		};
		
		th.setOnTabChangedListener(tabChangeListener);

		for (int i = 0; i < topicList.size(); i++) {
			HashMap<String, Object> map = topicList.get(i);
			if(!(Boolean) map.get("checked")) continue;
			String topic = (String) map.get(Data.TOPICS);
			TabSpec tabSpec = th.newTabSpec(topic);
			tabSpec.setIndicator((String) topic);
			tabSpec.setContent(this);
			th.addTab(tabSpec);
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.context_menu, menu);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    System.out.println("haha" + getComponentName());
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false);
	    
	    final Intent searchintent = new Intent(this, SearchableActivity.class);
	    
	    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				searchintent.putExtra("query", query);
				startActivity(searchintent);
				return true;
			}
	    	
	    });
	    
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
		case R.id.favorite: {
			Intent intent = new Intent(this, FavoriteActivity.class);
			startActivity(intent);
			return true;
		}
		case R.id.menu_settings: {
			Intent intent = new Intent(this, DragListActivity.class);
			startActivity(intent);
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public View createTabContent(String arg0) {
		View v = new View(getBaseContext());
		return v;
	}
}
