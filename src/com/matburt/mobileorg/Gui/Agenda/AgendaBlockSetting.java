package com.matburt.mobileorg.Gui.Agenda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.matburt.mobileorg.R;

public class AgendaBlockSetting extends SherlockActivity {
	public static final String BLOCK_NUMBER = "block_number";
	
	private int position;
	private ArrayList<AgendaQuery> agendas;
	
	private EditText titleView;
	private EditText payloadView;
	private EditText todoView;
	private EditText priorityView;
	private EditText tagsView;
	private CheckBox filterHabitsView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agenda_block_setting);
		
		this.titleView = (EditText) findViewById(R.id.agenda_block_title);
		this.priorityView = (EditText) findViewById(R.id.agenda_block_priority);
		this.todoView = (EditText) findViewById(R.id.agenda_block_todo);
		this.tagsView = (EditText) findViewById(R.id.agenda_block_tag);
		this.payloadView = (EditText) findViewById(R.id.agenda_block_payload);

		this.filterHabitsView = (CheckBox) findViewById(R.id.agenda_block_habits);
		
		this.position = getIntent().getIntExtra(BLOCK_NUMBER, -1);
		setupSettings(loadAgenda(position));
	}
	
	public void setupSettings(AgendaQuery agenda) {
		titleView.setText(agenda.title);
		payloadView.setText(combineToString(agenda.payloads));
		todoView.setText(combineToString(agenda.todos));
		priorityView.setText(combineToString(agenda.priorities));
		tagsView.setText(combineToString(agenda.tags));
		filterHabitsView.setChecked(agenda.filterHabits);
	}

	public AgendaQuery getQueryFromSettings() {
		AgendaQuery agenda = new AgendaQuery(titleView.getText().toString());
		
		agenda.tags = splitToArrayList(tagsView.getText().toString());
		agenda.payloads = splitToArrayList(payloadView.getText().toString());
		agenda.priorities = splitToArrayList(priorityView.getText().toString());
		agenda.todos = splitToArrayList(todoView.getText().toString());
		agenda.filterHabits = filterHabitsView.isChecked();
		
		return agenda;
	}
	
	private String combineToString(ArrayList<String> list) {
		if(list.size() == 0)
			return "";
		
		StringBuilder builder = new StringBuilder();
		for(String item: list)
			builder.append(item).append(":");
		
		builder.delete(builder.length() - 1, builder.length());
		return builder.toString();
	}
	
	private ArrayList<String> splitToArrayList(String string) {
		ArrayList<String> result = new ArrayList<String>();
		
		if(string == null || string.trim().length() == 0)
			return result;
		
		String[] split = string.split(":");
		return new ArrayList<String>(Arrays.asList(split));
	}
	
	public AgendaQuery loadAgenda(int position) {
		try {
			agendas = AgendaQuery.read(this);
			
			if(position >= 0 && position < agendas.size())
				return agendas.get(position);
		} catch (IOException e) {}
		return new AgendaQuery("");
	}
	
	public void saveAgenda() {
		if(position >= 0 && position < agendas.size()) {
			agendas.remove(position);
			agendas.add(position, getQueryFromSettings());
		} else {
			agendas.add(getQueryFromSettings());
		}
		try {
			AgendaQuery.write(agendas, this);
		} catch (IOException e) {}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getSupportMenuInflater().inflate(R.menu.agenda_block, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.agenda_block_save:
			saveAgenda();
			finish();
			break;
		case R.id.agenda_block_cancel:
			finish();
			break;
		
		default:
			return super.onOptionsItemSelected(item);
		}
		
		return true;
	}
}
