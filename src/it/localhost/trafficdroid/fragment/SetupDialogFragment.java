package it.localhost.trafficdroid.fragment;

import it.localhost.trafficdroid.R;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class SetupDialogFragment extends DialogFragment {
	private static final String APP_URL = "https://code.google.com/p/trafficdroid";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Builder builder = new Builder(getActivity());
		builder.setTitle(R.string.warning);
		builder.setMessage(R.string.badConf);
		builder.setPositiveButton(R.string.setup, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getActivity().getActionBar().setSelectedNavigationItem(6);
			}
		});
		builder.setNegativeButton(R.string.info, new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(APP_URL)));
			}
		});
		setCancelable(false);
		builder.setCancelable(false);
		return builder.create();
	}
}