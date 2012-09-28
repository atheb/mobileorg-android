package com.matburt.mobileorg.Gui.Agenda;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.commonsware.cwac.merge.MergeAdapter;
import com.matburt.mobileorg.R;
import com.matburt.mobileorg.Gui.Outline.OutlineActionMode;
import com.matburt.mobileorg.Gui.Outline.OutlineAdapter;
import com.matburt.mobileorg.OrgData.OrgDatabase;

public class AgendaFragment extends SherlockFragment {

	private ListView agendaList;
	private MergeAdapter mergeAdapter;
	private SQLiteDatabase db;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.db = new OrgDatabase(getActivity()).getReadableDatabase();

		this.agendaList = new ListView(getActivity());
		this.agendaList.setOnItemClickListener(agendaClickListener);
		
		setHasOptionsMenu(true);
		
		return agendaList;
	}
	
	private OnItemClickListener agendaClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			long nodeId = mergeAdapter.getItemId(position);
			OutlineActionMode.runEditNodeActivity(nodeId, getActivity());
		}
	};
	
	
	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	public void refresh() {
		this.mergeAdapter = new MergeAdapter();
		setupAgendas();
		this.agendaList.setAdapter(mergeAdapter);
	}

	private void setupAgendas() {	
		try {
			ArrayList<BlockAgenda> agendas = BlockAgenda.readAgendas(getActivity());
			
			if(agendas.size() == 0)
				return;
			
			for(AgendaQueryBuilder agenda: agendas.get(0).queries)
				addAgenda(agenda);
		} catch (IOException e) {}
	}
	
	public void addAgenda(AgendaQueryBuilder query) {
		TextView titleView = (TextView) View.inflate(getActivity(), R.layout.agenda_header, null);
		titleView.setText(query.title);
		mergeAdapter.addView(titleView);

		OutlineAdapter adapter = new OutlineAdapter(getActivity(), false);
		adapter.setState(query.getNodes(db, getActivity()));
		mergeAdapter.addAdapter(adapter);
	}
	

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		MenuItem item = menu.add("Agenda settings");
		item.setIcon(R.drawable.ic_menu_preferences);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == 0) {
			startActivity(new Intent(getActivity(), AgendaSettings.class));
			return true;
		}
		else
			return super.onOptionsItemSelected(item);
	}
}
