package it.localhost.trafficdroid.activity;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.common.Const;
import it.localhost.trafficdroid.dao.MainDAO;
import it.localhost.trafficdroid.dto.BadNewsDTO;
import it.localhost.trafficdroid.dto.MainDTO;
import it.localhost.trafficdroid.dto.StreetDTO;
import it.localhost.trafficdroid.dto.ZoneDTO;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AbstractActivity {
	private LinearLayout tableLayout;
	private LayoutInflater layoutInflater;
	private IntentFilter intentFilter;
	private OnClickListener webcamOnClickListener;
	private OnClickListener badNewsOnClickListener;
	private OnClickListener streetOnClickListener;
	private BroadcastReceiver receiver;
	private MainDTO mainDTO;
	private SharedPreferences sharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		setTheme(Const.themes[Integer.parseInt(sharedPreferences.getString(getString(R.string.themeKey), getString(R.string.themeDefault)))]);
		super.onCreate(savedInstanceState);
		trackEvent(Const.eventCatApp, Const.eventActionVersion, versionName());
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.linearlayout_main);
		intentFilter = new IntentFilter();
		intentFilter.addAction(Const.beginUpdate);
		intentFilter.addAction(Const.endUpdate);
		tableLayout = (LinearLayout) findViewById(R.id.mainTable);
		layoutInflater = LayoutInflater.from(this);
		webcamOnClickListener = new OnClickListener() {
			public void onClick(View v) {
				String code = (String) v.getTag();
				if (code.charAt(0) == Const.webcamNone) {
					trackEvent(Const.eventCatWebcam, Const.eventActionNone, code);
					new AlertDialog.Builder(MainActivity.this).setTitle(R.string.info).setPositiveButton(R.string.ok, null).setMessage(R.string.webcamNone).show();
				} else if (code.charAt(0) == Const.webcamTrue) {
					trackEvent(Const.eventCatWebcam, Const.eventActionOpen, code);
					Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
					String provider = sharedPreferences.getString(getString(R.string.providerCamKey), getString(R.string.providerCamDefault));
					String url = Const.http + provider + Const.popupTelecamera + Const.decodeCam(code);
					intent.putExtra(Const.url, url);
					startActivity(intent);
				} else {
					trackEvent(Const.eventCatWebcam, Const.eventActionRequest, code);
					new AlertDialog.Builder(MainActivity.this).setTitle(R.string.info).setPositiveButton(R.string.ok, null).setMessage(R.string.webcamAdd).show();
				}
			}
		};
		badNewsOnClickListener = new OnClickListener() {
			public void onClick(View v) {
				Dialog d = new Dialog(MainActivity.this);
				ScrollView sv = new ScrollView(MainActivity.this);
				LinearLayout ll = new LinearLayout(MainActivity.this);
				ll.setOrientation(LinearLayout.VERTICAL);
				d.setContentView(sv);
				sv.addView(ll);
				StreetDTO street = mainDTO.getStreets().get((Integer) v.getTag());
				d.setTitle(street.getName());
				for (BadNewsDTO event : street.getBadNews()) {
					View eventRow = layoutInflater.inflate(R.layout.linearlayout_badnews, null);
					((TextView) eventRow.findViewById(R.id.BNDText)).setText(event.getTitle() + " " + event.getDescription());
					// TODO icona personalizzata in base all'evento
					ll.addView(eventRow);
				}
				d.show();
			}
		};
		streetOnClickListener = new OnClickListener() {
			public void onClick(View v) {
				String streetVkey = (Integer) v.getTag(R.id.streetId) + "V";
				boolean streetVisible = !sharedPreferences.getBoolean(streetVkey, true);
				sharedPreferences.edit().putBoolean(streetVkey, streetVisible).commit();
				for (int i = (Integer) v.getTag(R.id.streetStart); i < (Integer) v.getTag(R.id.streetEnd); i++) {
					tableLayout.getChildAt(i).setVisibility(streetVisible ? View.VISIBLE : View.GONE);
				}
			}
		};
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
		if (sharedPreferences.getString(getString(R.string.providerTrafficKey), getString(R.string.providerTrafficDefault)).equals(getString(R.string.providerTrafficDefault)))
			new AlertDialog.Builder(this).setTitle(R.string.warning).setPositiveButton(R.string.ok, null).setMessage(R.string.badConf).show();
		else if (sharedPreferences.getBoolean(getString(R.string.berserkKey), Boolean.parseBoolean(getString(R.string.berserkDefault))))
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
			String provider = sharedPreferences.getString(getString(R.string.providerFuelKey), getString(R.string.providerFuelDefault));
			intent.putExtra(Const.url, Const.http + provider + Const.fuel);
			startActivity(intent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void refresh() {
		tableLayout.removeAllViews();
		mainDTO = MainDAO.retrieve(getApplicationContext());
		if (sharedPreferences.getBoolean(Const.exceptionCheck, false)) {
			String msg = sharedPreferences.getString(Const.exceptionName, null) + ": " + sharedPreferences.getString(Const.exceptionMsg, null);
			new AlertDialog.Builder(this).setTitle(R.string.error).setPositiveButton(R.string.ok, null).setMessage(msg).show();
			setTitle(msg);
		} else {
			if (mainDTO != null && mainDTO.getTrafficTime() != null)
				setTitle(getString(R.string.app_name) + ": " + DateFormat.getTimeFormat(this).format(mainDTO.getTrafficTime()));
			for (int i = 0; i < mainDTO.getStreets().size(); i++) {
				StreetDTO street = mainDTO.getStreets().get(i);
				boolean streetVisible = sharedPreferences.getBoolean(street.getId() + "V", true);
				LinearLayout streetRow = (LinearLayout) layoutInflater.inflate(R.layout.tablerow_street, tableLayout, false);
				((TextView) streetRow.findViewById(R.id.streetName)).setText(street.getName());
				((TextView) streetRow.findViewById(R.id.streetDirLeft)).setText(street.getDirectionLeft());
				((TextView) streetRow.findViewById(R.id.streetDirRight)).setText(street.getDirectionRight());
				tableLayout.addView(streetRow);
				streetRow.setTag(R.id.streetId, street.getId());
				streetRow.setTag(R.id.streetStart, tableLayout.getChildCount());
				if (sharedPreferences.getBoolean(getString(R.string.badNewsKey), Boolean.parseBoolean(getString(R.string.badNewsDefault))) && street.getBadNews().size() != 0) {
					LinearLayout badNewsRow = (LinearLayout) layoutInflater.inflate(R.layout.tablerow_badnews, tableLayout, false);
					((TextView) badNewsRow.findViewById(R.id.BNTNumber)).setText(Integer.toString(street.getBadNews().size()));
					badNewsRow.setTag(i);
					badNewsRow.setOnClickListener(badNewsOnClickListener);
					badNewsRow.setVisibility(streetVisible ? View.VISIBLE : View.GONE);
					tableLayout.addView(badNewsRow);
				}
				for (ZoneDTO zoneDTO : street.getZones()) {
					LinearLayout zoneNameRow = (LinearLayout) layoutInflater.inflate(R.layout.tablerow_zonefirst, tableLayout, false);
					LinearLayout zoneSpeedRow = (LinearLayout) layoutInflater.inflate(R.layout.tablerow_zonesecond, tableLayout, false);
					TextView zoneNameText = (TextView) zoneNameRow.findViewById(R.id.zoneName);
					TextView zoneKmText = (TextView) zoneNameRow.findViewById(R.id.zoneKm);
					TextView leftZoneSpeedText = (TextView) zoneSpeedRow.findViewById(R.id.zoneSpeedLeft);
					TextView rightZoneSpeedText = (TextView) zoneSpeedRow.findViewById(R.id.zoneSpeedRight);
					ImageView trendLeftText = (ImageView) zoneSpeedRow.findViewById(R.id.trendLeft);
					ImageView trendRightText = (ImageView) zoneSpeedRow.findViewById(R.id.trendRight);
					zoneNameText.setText(zoneDTO.getName());
					zoneKmText.setText(zoneDTO.getKm());
					if (zoneDTO.getTrendLeft() != 0)
						trendLeftText.setImageResource(zoneDTO.getTrendLeft());
					if (zoneDTO.getTrendRight() != 0)
						trendRightText.setImageResource(zoneDTO.getTrendRight());
					if (zoneDTO.getSpeedLeft() != 0)
						leftZoneSpeedText.setText(Byte.toString(zoneDTO.getSpeedLeft()));
					else
						leftZoneSpeedText.setText("-");
					if (zoneDTO.getSpeedRight() != 0)
						rightZoneSpeedText.setText(Byte.toString(zoneDTO.getSpeedRight()));
					else
						rightZoneSpeedText.setText("-");
					leftZoneSpeedText.setTextColor(Const.colorCat[zoneDTO.getCatLeft()]);
					rightZoneSpeedText.setTextColor(Const.colorCat[zoneDTO.getCatRight()]);
					leftZoneSpeedText.setTypeface(zoneDTO.getCatLeft() == 1 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
					rightZoneSpeedText.setTypeface(zoneDTO.getCatRight() == 1 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
					zoneSpeedRow.setTag(zoneDTO.getId());
					zoneSpeedRow.setOnClickListener(webcamOnClickListener);
					ImageView cam = (ImageView) zoneSpeedRow.findViewById(R.id.zoneCam);
					if (zoneDTO.getId().charAt(0) == Const.webcamTrue)
						cam.setImageResource(android.R.drawable.ic_menu_camera);
					else if (zoneDTO.getId().charAt(0) == Const.webcamNone)
						cam.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
					else
						cam.setImageResource(android.R.drawable.ic_menu_add);
					zoneNameRow.setVisibility(streetVisible ? View.VISIBLE : View.GONE);
					zoneSpeedRow.setVisibility(streetVisible ? View.VISIBLE : View.GONE);
					tableLayout.addView(zoneNameRow);
					tableLayout.addView(zoneSpeedRow);
				}
				streetRow.setTag(R.id.streetEnd, tableLayout.getChildCount());
				streetRow.setOnClickListener(streetOnClickListener);
			}
		}
	}
}