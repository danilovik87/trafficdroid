package it.localhost.trafficdroid.fragment;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.adapter.MainAdapter;
import it.localhost.trafficdroid.adapter.item.AbstractItem;
import it.localhost.trafficdroid.common.Utility;
import it.localhost.trafficdroid.dao.PersistanceService;
import it.localhost.trafficdroid.dto.MainDTO;
import it.localhost.trafficdroid.exception.GenericException;
import it.localhost.trafficdroid.service.TdListener;
import it.localhost.trafficdroid.service.TdService;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;

public class MainFragment extends Fragment {
	private ExpandableListView listView;
	private IntentFilter intentFilter;
	private BroadcastReceiver receiver;
	private TdListener tdListener;
	private static final String removePrefToastUndo = " è stato aggiunto ai preferiti.";
	private static final String removePrefToast = " è stato rimosso dai preferiti.";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		receiver = new UpdateReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(TdService.beginUpdate);
		intentFilter.addAction(TdService.endUpdate);
		View v = inflater.inflate(R.layout.main, null);
		listView = (ExpandableListView) v.findViewById(R.id.mainTable);
		listView.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				((AbstractItem) parent.getExpandableListAdapter().getChild(groupPosition, childPosition)).onClick();
				return true;
			}
		});
		tdListener = new TdListener();
		if (Utility.getPrefString(getActivity(), R.string.providerTrafficKey, R.string.providerTrafficDefault).equals(getString(R.string.providerTrafficDefault))) {
			new SetupDialogFragment().show(getFragmentManager(), SetupDialogFragment.class.getSimpleName());
		} else if (Utility.getPrefBoolean(getActivity(), R.string.berserkKey, R.string.berserkDefault))
			tdListener.sendWakefulWork(getActivity());
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().registerReceiver(receiver, intentFilter);
		new RefreshTask().execute();
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(receiver);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
		int packedPositionType = ExpandableListView.getPackedPositionType(info.packedPosition);
		View item = info.targetView;
		if (packedPositionType == ExpandableListView.PACKED_POSITION_TYPE_GROUP || (packedPositionType == ExpandableListView.PACKED_POSITION_TYPE_CHILD && ((Integer) item.getTag(R.id.zoneType)) == AbstractItem.itemTypes[4])) {
			getActivity().getMenuInflater().inflate(R.menu.main_context, menu);
			menu.getItem(0).setChecked(Utility.getPrefBoolean(getActivity(), Integer.toString((Integer) item.getTag(R.id.itemKey)), false));
			menu.setHeaderTitle((String) item.getTag(R.id.itemName));
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		View v = ((ExpandableListContextMenuInfo) item.getMenuInfo()).targetView;
		String itemKey = Integer.toString((Integer) v.getTag(R.id.itemKey));
		String itemName = (String) v.getTag(R.id.itemName);
		switch (item.getItemId()) {
			case R.id.removePref:
				String msg;
				if (Utility.getPrefBoolean(getActivity(), itemKey, false)) {
					item.setChecked(false);
					Utility.getEditor(getActivity()).putBoolean(itemKey, false).commit();
					msg = removePrefToast;
				} else {
					item.setChecked(true);
					Utility.getEditor(getActivity()).putBoolean(itemKey, true).commit();
					msg = removePrefToastUndo;
				}
				Toast.makeText(getActivity(), itemName + msg, Toast.LENGTH_SHORT).show();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	private final class UpdateReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(TdService.beginUpdate)) {
				getActivity().setProgressBarIndeterminateVisibility(true);
			} else if (intent.getAction().equals(TdService.endUpdate)) {
				getActivity().setProgressBarIndeterminateVisibility(false);
				new RefreshTask().execute();
			}
		}
	}

	private class RefreshTask extends AsyncTask<Void, Void, MainDTO> {
		@Override
		protected MainDTO doInBackground(Void... params) {
			try {
				return PersistanceService.retrieve(getActivity());
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(MainDTO mainDTO) {
			if (mainDTO != null && mainDTO.getTrafficTime() != null) {
				// setTitle(DateFormat.getTimeFormat(getActivity()).format(mainDTO.getTrafficTime())
				// + blank + getString(R.string.app_name));
				listView.setAdapter(new MainAdapter(getActivity(), mainDTO, true));// isAdFree()
				registerForContextMenu(listView);
				for (int i = 0; i < listView.getExpandableListAdapter().getGroupCount(); i++)
					if (Utility.getPrefBoolean(getActivity(), MainAdapter.expanded + i, false))
						listView.expandGroup(i);
					else
						listView.collapseGroup(i);
			}
			if (Utility.getPrefBoolean(getActivity(), GenericException.exceptionCheck, false)) {
				String msg = Utility.getPrefString(getActivity(), GenericException.exceptionMsg, "Unknown Error");
				new MessageDialogFragment().show(getFragmentManager(), getString(R.string.error), msg, false);
				// setTitle(msg);
				Utility.getEditor(getActivity()).putBoolean(GenericException.exceptionCheck, false).commit();
			}
		}
	}
}
