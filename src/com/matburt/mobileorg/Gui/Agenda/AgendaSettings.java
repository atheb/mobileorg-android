package com.matburt.mobileorg.Gui.Agenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.matburt.mobileorg.R;

public class AgendaSettings extends SherlockActivity {
	public static final String AGENDA_NUMBER = "agenda_number";

	private int agendaPos;

	private TextView titleView;
	private ListView agendaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_settings);
        this.titleView = (TextView) findViewById(R.id.agenda_title);
        this.agendaList = (ListView) findViewById(R.id.agenda_list);
        
        Button agendaCreateButton = ((Button) findViewById(R.id.agenda_create));
        agendaCreateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addAgendaEntry();
			}
		});
        
        agendaList.setOnItemClickListener(agendaItemClick);
        agendaList.setOnCreateContextMenuListener(this);
        registerForContextMenu(agendaList);
        
        getSupportActionBar().setTitle("Agenda Settings");
        
        this.agendaPos = getIntent().getIntExtra(AGENDA_NUMBER, -1);        
        if(this.agendaPos == -1)
        	this.agendaPos = OrgAgenda.addAgenda(this);
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		refresh();
	}
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	saveAgenda();
    }
    
	public void refresh() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				OrgAgenda.getAgendaQueryTitles(agendaPos, this));
		agendaList.setAdapter(adapter);
		String agendaTitle = OrgAgenda.getAgenda(agendaPos, this).title;
		this.titleView.setText(agendaTitle);
	}


	private OnItemClickListener agendaItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long id) {
			Intent intent = new Intent(AgendaSettings.this, AgendaEntrySetting.class);
			intent.putExtra(AgendaEntrySetting.AGENDA_NUMBER, agendaPos);
			intent.putExtra(AgendaEntrySetting.ENTRY_NUMBER, position);
			startActivity(intent);
		}
	};
	

    private void addAgendaEntry() {
		Intent intent = new Intent(AgendaSettings.this, AgendaEntrySetting.class);
		intent.putExtra(AgendaEntrySetting.AGENDA_NUMBER, this.agendaPos);
		startActivity(intent);
    }
    
    private void saveAgenda() {
    	OrgAgenda blockAgenda = OrgAgenda.getAgenda(agendaPos, this);
    	blockAgenda.title = this.titleView.getText().toString();
    	OrgAgenda.replaceAgenda(blockAgenda, agendaPos, this);
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
			OrgAgenda.removeAgendaEntry(agendaPos, info.position, this);
			refresh();
			return true;
		}
		else
			return super.onContextItemSelected(item);
	}
}
