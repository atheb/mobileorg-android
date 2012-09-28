package com.matburt.mobileorg.Gui.Agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.matburt.mobileorg.R;

public class AgendaFragment extends SherlockFragment {

	private ListView agendaList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.agendaList = new ListView(getActivity());
		this.agendaList.setOnItemClickListener(agendaClickListener);
		
		setHasOptionsMenu(true);
		
		return agendaList;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setupAgendas();
	}

	private void setupAgendas() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1,
				BlockAgenda.getAgendasTitles(getActivity()));
		agendaList.setAdapter(adapter);

	}

	private OnItemClickListener agendaClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			showBlockAgendaFragment(position);
		}
	};
	
	private void showBlockAgendaFragment(int position) {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.hide(this);
		
		AgendaBlockFragment blockFragment = new AgendaBlockFragment();
		blockFragment.agendaPos = position;
		transaction.add(R.id.main_main, blockFragment, "agendaBlockFragment");
		transaction.show(blockFragment);
		transaction.addToBackStack(getTag());
		
		transaction.commit();
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
