package com.matburt.mobileorg.Gui.Agenda;

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

public class AgendaBlockSettings extends SherlockListActivity {
	public static final String AGENDA_NUMBER = "agenda_number";

	private int agendaPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getListView().setOnItemClickListener(agendaItemClick);
        getListView().setOnCreateContextMenuListener(this);
        registerForContextMenu(getListView());
        
        this.agendaPos = getIntent().getIntExtra(AGENDA_NUMBER, -1);
        
        if(this.agendaPos == -1)
        	this.agendaPos = BlockAgenda.addBlockAgenda(this, "test");
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
			removeAgendaBlockEntry(info.position);
			return true;
		}
		else
			return super.onContextItemSelected(item);
	}


	private OnItemClickListener agendaItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long id) {
			Intent intent = new Intent(AgendaBlockSettings.this, AgendaBlockEntrySetting.class);
			intent.putExtra(AgendaBlockEntrySetting.AGENDA_NUMBER, agendaPos);
			intent.putExtra(AgendaBlockEntrySetting.BLOCK_NUMBER, position);
			startActivity(intent);
		}
	};
	
    
	private void refresh() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				BlockAgenda.getBlockAgendaQueryTitles(agendaPos, this));
		setListAdapter(adapter);
		String agendaTitle = BlockAgenda.getBlockAgenda(agendaPos, this).title;
		getSupportActionBar().setTitle(agendaTitle + " blocks");
	}

    private void createAgendaBlockEntry() {
		Intent intent = new Intent(AgendaBlockSettings.this, AgendaBlockEntrySetting.class);
		intent.putExtra(AgendaBlockEntrySetting.AGENDA_NUMBER, this.agendaPos);
		startActivity(intent);
    }
    
	private void removeAgendaBlockEntry(int blockPos) {
		BlockAgenda.removeBlockAgendaEntry(agendaPos, blockPos, this);
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
			createAgendaBlockEntry();
			break;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
		return true;
	}
}
