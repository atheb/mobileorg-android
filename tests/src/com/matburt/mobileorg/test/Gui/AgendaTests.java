package com.matburt.mobileorg.test.Gui;

import java.io.IOException;
import java.util.ArrayList;

import com.matburt.mobileorg.Gui.Agenda.AgendaQuery;

import android.content.Context;
import android.test.AndroidTestCase;

public class AgendaTests extends AndroidTestCase {

	private Context context;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.context = getContext();
	}

	public void testSerialization() throws IOException {
		AgendaQuery test = new AgendaQuery("test");
		test.todos.add("TODO");
		
		ArrayList<AgendaQuery> agendas = new ArrayList<AgendaQuery>();
		agendas.add(test);
		
		AgendaQuery.write(agendas, context);
		
		ArrayList<AgendaQuery> read = AgendaQuery.read(context);
		
		assertEquals(agendas.size(), read.size());
		assertEquals(test.title, read.get(0).title);
	}
}
