package com.matburt.mobileorg.Gui.Agenda;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;

public class AgendaSettings extends SherlockListActivity {

	private ArrayList<String> agendas;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				Toast.makeText(AgendaSettings.this, agendas.get(position), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(AgendaSettings.this, AgendaBlockSetting.class);
				startActivity(intent);
			}
		});
        
        
        agendas = new ArrayList<String>();
        agendas.add("hej");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, agendas);
        setListAdapter(adapter);
    }
}
