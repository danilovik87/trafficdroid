package it.localhost.trafficdroid.activity;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.adapter.MainAdapter;
import it.localhost.trafficdroid.adapter.item.AbstractChildItem;
import it.localhost.trafficdroid.common.Const;
import it.localhost.trafficdroid.common.TdAnalytics;
import it.localhost.trafficdroid.common.TdApp;
import it.localhost.trafficdroid.dao.MainDAO;
import it.localhost.trafficdroid.dto.MainDTO;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class MainActivity extends AbstractActivity {
	private ExpandableListView listView;
	private IntentFilter intentFilter;
	private BroadcastReceiver receiver;

	private class RefreshTask extends AsyncTask<Void, Void, MainDTO> {
		private Exception e = null;

		@Override
		protected MainDTO doInBackground(Void... params) {
			try {
				return MainDAO.retrieve();
			} catch (Exception e) {
				this.e = e;
				return null;
			}
		}

		@Override
		protected void onPostExecute(MainDTO mainDTO) {
			if (e == null) {
				if (mainDTO.getTrafficTime() != null) {
					setTitle(getString(R.string.app_name) + Const.blank + DateFormat.getTimeFormat(MainActivity.this).format(mainDTO.getTrafficTime()));
					listView.setAdapter(new MainAdapter(MainActivity.this, mainDTO));
					for (int i = 0; i < listView.getExpandableListAdapter().getGroupCount(); i++)
						if (TdApp.getPrefBoolean(Const.expanded + i, false))
							listView.expandGroup(i);
						else
							listView.collapseGroup(i);
				}
			} else {
				sendBroadcast(Const.doUpdateIntent);
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//setTheme(Const.themes[Integer.parseInt(TdApp.getPrefString(R.string.themeKey, R.string.themeDefault))]);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		TdAnalytics.trackEvent(Const.eventCatApp, Const.eventActionVersion, versionName(), versionCode());
		intentFilter = new IntentFilter();
		intentFilter.addAction(Const.beginUpdate);
		intentFilter.addAction(Const.endUpdate);
		listView = (ExpandableListView) findViewById(R.id.mainTable);
		listView.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				((AbstractChildItem) parent.getExpandableListAdapter().getChild(groupPosition, childPosition)).onClick();
				return true;
			}
		});
		receiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Const.beginUpdate)) {
					setProgressBarIndeterminateVisibility(true);
				} else if (intent.getAction().equals(Const.endUpdate)) {
					setProgressBarIndeterminateVisibility(false);
					refresh();
				}
			}
		};
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(receiver, intentFilter);
		((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(Const.notificationId);
		if (TdApp.getPrefString(R.string.providerTrafficKey, R.string.providerTrafficDefault).equals(getString(R.string.providerTrafficDefault)))
			new AlertDialog.Builder(this).setTitle(R.string.warning).setPositiveButton(R.string.ok, null).setMessage(R.string.badConf).show();
		else if (TdApp.getPrefBoolean(R.string.berserkKey, R.string.berserkDefault))
			sendBroadcast(Const.doUpdateIntent);
		refresh();
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menusettings:
			startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
			return true;
		case R.id.menurefresh:
			sendBroadcast(Const.doUpdateIntent);
			return true;
		case R.id.menufuel:
			Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
			String provider = TdApp.getPrefString(R.string.providerFuelKey, R.string.providerFuelDefault);
			intent.putExtra(Const.url, Const.http + provider + Const.fuel);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void refresh() {
		if (!TdApp.getPrefBoolean(Const.exceptionCheck, false)) {
			new RefreshTask().execute((Void[]) null);
		} else {
			String msg = TdApp.getPrefString(Const.exceptionMsg, Const.unknowError);
			new AlertDialog.Builder(this).setTitle(R.string.error).setPositiveButton(R.string.ok, null).setMessage(msg).show();
			setTitle(msg);
		}
	}
}