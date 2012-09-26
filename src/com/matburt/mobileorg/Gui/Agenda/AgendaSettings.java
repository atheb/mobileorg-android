package com.matburt.mobileorg.Gui.Agenda;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.matburt.mobileorg.R;

public class AgendaSettings extends SherlockListActivity {
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getListView().setOnItemClickListener(agendaItemClick);
        getListView().setOnCreateContextMenuListener(this);
        registerForContextMenu(getListView());
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		refresh();
	}
    

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add("Remove");
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		if(item.getTitle().equals("Remove")) {
		    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			removeAgenda(info.position);
			return true;
		}
		else
			return super.onContextItemSelected(item);
	}


	private OnItemClickListener agendaItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long id) {
			Intent intent = new Intent(AgendaSettings.this, AgendaBlockSetting.class);
			intent.putExtra(AgendaBlockSetting.BLOCK_NUMBER, position);
			startActivity(intent);
		}
	};
	
    
	private void refresh() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getAgendas());
        setListAdapter(adapter);
	}
	
    private ArrayList<String> getAgendas() {
    	ArrayList<String> result = new ArrayList<String>();
		try {
			ArrayList<AgendaQuery> agendas = AgendaQuery.read(this);
			
			for(AgendaQuery agenda: agendas)
				result.add(agenda.title);
			
			return result;
		} catch (IOException e) {
			return result;
		}
    }
    
    private void createAgenda() {
		Intent intent = new Intent(AgendaSettings.this, AgendaBlockSetting.class);
		startActivity(intent);
    }
    
	private void removeAgenda(int position) {
		try {
			ArrayList<AgendaQuery> agendas = AgendaQuery.read(this);
			agendas.remove(position);
			AgendaQuery.write(agendas, this);
		} catch (IOException e) {}
		refresh();
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem create = menu.add("Create");
		create.setIcon(R.drawable.ic_menu_add);
		create.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case 0:
			createAgenda();
			break;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
		return true;
	}
}
