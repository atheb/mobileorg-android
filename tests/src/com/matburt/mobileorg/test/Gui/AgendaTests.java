package com.matburt.mobileorg.test.Gui;

import java.io.IOException;
import java.util.ArrayList;

import com.matburt.mobileorg.Gui.Agenda.AgendaQueryBuilder;
import com.matburt.mobileorg.Gui.Agenda.BlockAgenda;

import android.content.Context;
import android.test.AndroidTestCase;

public class AgendaTests extends AndroidTestCase {

	private Context context;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.context = getContext();
	}
	
	public void testBlockSerialization() throws IOException {
		ArrayList<BlockAgenda> agendas = new ArrayList<BlockAgenda>();
		BlockAgenda blockAgenda = new BlockAgenda();
		blockAgenda.title = "test";
		agendas.add(blockAgenda);
		
		BlockAgenda.writeAgendas(agendas, context);
		ArrayList<BlockAgenda> readAgendas = BlockAgenda.readAgendas(context);
		
		assertEquals(agendas.size(), readAgendas.size());
		BlockAgenda readBlockAgenda = readAgendas.get(0);
		assertEquals(blockAgenda.title, readBlockAgenda.title);
	}
	
	public void testQuerySerialization() throws IOException {
		ArrayList<BlockAgenda> agendas = new ArrayList<BlockAgenda>();
		BlockAgenda blockAgenda = new BlockAgenda();
		agendas.add(blockAgenda);

		AgendaQueryBuilder builder = new AgendaQueryBuilder("test");
		blockAgenda.queries.add(builder);
		
		BlockAgenda.writeAgendas(agendas, context);
		ArrayList<BlockAgenda> readAgendas = BlockAgenda.readAgendas(context);
		
		BlockAgenda readBlockAgenda = readAgendas.get(0);
		assertEquals(blockAgenda.queries.size(), readBlockAgenda.queries.size());
		assertEquals(blockAgenda.queries.get(0).title, readBlockAgenda.queries.get(0).title);
	}
}
