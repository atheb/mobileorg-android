package com.matburt.mobileorg.Gui.Agenda;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.commonsware.cwac.merge.MergeAdapter;
import com.matburt.mobileorg.R;
import com.matburt.mobileorg.Gui.Outline.OutlineActionMode;
import com.matburt.mobileorg.Gui.Outline.OutlineAdapter;
import com.matburt.mobileorg.OrgData.OrgContract.OrgData;
import com.matburt.mobileorg.OrgData.OrgDatabase;
import com.matburt.mobileorg.OrgData.OrgDatabase.Tables;
import com.matburt.mobileorg.OrgData.OrgFile;
import com.matburt.mobileorg.util.OrgFileNotFoundException;
import com.matburt.mobileorg.util.SelectionBuilder;

public class AgendaFragment extends SherlockFragment {

	private ListView agendaList;
	private MergeAdapter mergeAdapter;
	private SQLiteDatabase db;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.agendaList = new ListView(getActivity());
		this.mergeAdapter = new MergeAdapter();

		this.db = new OrgDatabase(getActivity()).getReadableDatabase();
		
		setupAgendas();
		agendaList.setAdapter(mergeAdapter);
		agendaList.setOnItemClickListener(agendaClickListener);
		
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

	private void setupAgendas() {
		addAgenda("Home", getQuery("TODO", "Home"));
		addAgenda("Online", getQuery("TODO", "Online"));
	}
	
	public void addAgenda(String title, SelectionBuilder query) {
		TextView titleView = (TextView) View.inflate(getActivity(), R.layout.agenda_header, null);
		titleView.setText(title);
		mergeAdapter.addView(titleView);

		OutlineAdapter adapter = new OutlineAdapter(getActivity(), false);
		adapter.setState(getNodes(query));
		mergeAdapter.addAdapter(adapter);
	}
	
	private long[] getNodes(SelectionBuilder query) {
		long[] result = null;
		
		Cursor cursor = query.query(db, OrgData.DEFAULT_COLUMNS, OrgData.DEFAULT_SORT);
		
		result = new long[cursor.getCount()];
		cursor.moveToFirst();
		
		int i = 0;
		while(cursor.isAfterLast() == false) {
			result[i++] = cursor.getLong(cursor.getColumnIndex(OrgData.ID));
			cursor.moveToNext();
		}
		cursor.close();
		return result;
	}
	
	private SelectionBuilder getQuery(String todo, String tag) {
		final SelectionBuilder builder = new SelectionBuilder();
		builder.table(Tables.ORGDATA);
		builder.where("NOT " + OrgData.FILE_ID + "=?", Long.toString(getAgendaFileId()));
		
		if(todo != null && !TextUtils.isEmpty(todo))
			builder.where(OrgData.TODO + "=?", todo);
		
		if(tag != null && !TextUtils.isEmpty(tag))
			builder.where(OrgData.TAGS + " LIKE ?", "%" + tag+ "%");
		
		return builder;
	}
	
	private long getAgendaFileId() {
		try {
			OrgFile agendaFile = new OrgFile(OrgFile.AGENDA_FILE, getActivity().getContentResolver());
			return agendaFile.id;
		} catch (OrgFileNotFoundException e) { return -1;}
	}
}
