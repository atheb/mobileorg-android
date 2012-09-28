package com.matburt.mobileorg.Gui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.matburt.mobileorg.R;
import com.matburt.mobileorg.Gui.Agenda.AgendaFragment;
import com.matburt.mobileorg.Gui.Outline.OutlineFragment;
import com.matburt.mobileorg.OrgData.OrgProviderUtils;
import com.matburt.mobileorg.Services.SyncService;
import com.matburt.mobileorg.Settings.SettingsActivity;
import com.matburt.mobileorg.Settings.WizardActivity;
import com.matburt.mobileorg.Synchronizers.Synchronizer;
import com.matburt.mobileorg.util.OrgUtils;

public class MainActivity extends SherlockFragmentActivity {
	public final static String NODE_ID = "node_id";

	private Long node_id;

	private SynchServiceReceiver syncReceiver;
	private MenuItem synchronizerMenuItem;

	private OutlineFragment outlineFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		OrgUtils.setTheme(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
				
		Intent intent = getIntent();
		node_id = intent.getLongExtra(NODE_ID, -1);

		if (this.node_id == -1)
			displayNewUserDialogs();

		this.syncReceiver = new SynchServiceReceiver();
		registerReceiver(this.syncReceiver, new IntentFilter(
				Synchronizer.SYNC_UPDATE));
		
		this.outlineFragment = (OutlineFragment) getSupportFragmentManager()
				.findFragmentById(R.id.outlineFragment);
		refreshDisplay();
	}

	
	private void displayNewUserDialogs() {
		if (OrgUtils.isSyncConfigured(this) == false)
			runShowWizard(null);

		if (OrgUtils.isUpgradedVersion(this))
			showUpgradePopup();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		refreshTitle();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(this.syncReceiver);
		super.onDestroy();
	}
		
	public void refreshDisplay() {
		outlineFragment.refresh();
		refreshTitle();
	}
	
	
	private void refreshTitle() {
		this.getSupportActionBar().setTitle("MobileOrg " + getChangesString());
	}
    
    private String getChangesString() {
    	int changes = OrgProviderUtils.getChangesCount(getContentResolver());
    	if(changes > 0)
    		return "[" + changes + "]";
    	else
    		return "";
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    
	    synchronizerMenuItem = menu.findItem(R.id.menu_sync);
	    
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			outlineFragment.collapseCurrent();
			return true;
			
		case R.id.menu_sync:
			runSynchronize(null);
			return true;

		case R.id.menu_settings:
			runShowSettings(null);
			return true;

		case R.id.menu_outline:
			runExpandableOutline(-1);
			return true;

		case R.id.menu_agenda:
			showAgenda();
			return true;
			
		case R.id.menu_search:
			return runSearch();

		case R.id.menu_help:
			runHelp(null);
			return true;
		}
		return false;
	}
	
	private void showAgenda() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		transaction.hide(outlineFragment);
		transaction.addToBackStack(outlineFragment.getTag());
		
		Fragment agendaFragment = new AgendaFragment();
		transaction.add(R.id.main_main, agendaFragment, "agendaFragment");
		transaction.show(agendaFragment);
		
		transaction.commit();
	}

	public void runHelp(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("https://github.com/matburt/mobileorg-android/wiki"));
    	startActivity(intent);
    }
    
    public void runSynchronize(View view) {
		startService(new Intent(this, SyncService.class));
    }

	public void runShowSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
    public void runShowWizard(View view) {
        startActivity(new Intent(this, WizardActivity.class));
    }
    
    
    private void runExpandableOutline(long id) {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(MainActivity.NODE_ID, id);
		startActivity(intent);
    }


	private boolean runSearch() {
		return onSearchRequested();
	}

	private void showUpgradePopup() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(OrgUtils.getStringFromResource(R.raw.upgrade, this));
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
	

	private class SynchServiceReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean syncStart = intent.getBooleanExtra(Synchronizer.SYNC_START, false);
			boolean syncDone = intent.getBooleanExtra(Synchronizer.SYNC_DONE, false);
			boolean showToast = intent.getBooleanExtra(Synchronizer.SYNC_SHOW_TOAST, false);
			int progress = intent.getIntExtra(Synchronizer.SYNC_PROGRESS_UPDATE, -1);
			
			if(syncStart) {
				synchronizerMenuItem.setVisible(false);
				setSupportProgress(Window.PROGRESS_START);
				setSupportProgressBarIndeterminate(true);
				setSupportProgressBarIndeterminateVisibility(true);
			} else if (syncDone) {
				setSupportProgressBarVisibility(false);
				setSupportProgressBarIndeterminateVisibility(false);
				refreshDisplay();
				synchronizerMenuItem.setVisible(true);

				if (showToast)
					Toast.makeText(context,
							R.string.outline_synchronization_successful,
							Toast.LENGTH_SHORT).show();
			} else if (progress >= 0 && progress <= 100) {
				if(progress == 100)
					setSupportProgressBarIndeterminateVisibility(false);
				
				setSupportProgressBarIndeterminate(false);
				int normalizedProgress = (Window.PROGRESS_END - Window.PROGRESS_START) / 100 * progress;
				setSupportProgress(normalizedProgress);
				refreshDisplay();
			}
		}
	}
}
