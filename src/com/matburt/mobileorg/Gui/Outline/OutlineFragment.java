package com.matburt.mobileorg.Gui.Outline;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.matburt.mobileorg.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OutlineFragment extends SherlockFragment {
	private final static String OUTLINE_NODES = "nodes";
	private final static String OUTLINE_CHECKED_POS = "selection";
	private final static String OUTLINE_SCROLL_POS = "scrollPosition";
	
	private OutlineListView listView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setHasOptionsMenu(true);
		
		View view = inflater.inflate(R.layout.outline, container);
		listView = (OutlineListView) view.findViewById(R.id.outline_list);
		listView.setActivity(getSherlockActivity());
		listView.setEmptyView(view.findViewById(R.id.outline_list_empty));
		
		if(savedInstanceState != null)
			restoreInstanceState(savedInstanceState);
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLongArray(OUTLINE_NODES, listView.getState());
		outState.putInt(OUTLINE_CHECKED_POS, listView.getCheckedItemPosition());
		outState.putInt(OUTLINE_SCROLL_POS, listView.getFirstVisiblePosition());
	}
	
	public void restoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState == null)
			return;
		
		long[] state = savedInstanceState.getLongArray(OUTLINE_NODES);
		if(state != null)
			listView.setState(state);
		
		int checkedPos= savedInstanceState.getInt(OUTLINE_CHECKED_POS, 0);
		listView.setItemChecked(checkedPos, true);
		
		int scrollPos = savedInstanceState.getInt(OUTLINE_SCROLL_POS, 0);
		listView.setSelection(scrollPos);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.outline, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		
		case R.id.menu_capturechild:
			OutlineActionMode.runCaptureActivity(listView.getCheckedNodeId(),
					getSherlockActivity());
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void refresh() {
		listView.refresh();
	}
	
	public void collapseCurrent() {
		listView.collapseCurrent();
	}
}
