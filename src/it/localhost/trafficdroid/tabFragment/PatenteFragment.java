package it.localhost.trafficdroid.tabFragment;

import com.google.ads.AdRequest;
import com.google.ads.InterstitialAd;

import it.localhost.trafficdroid.R;
import it.localhost.trafficdroid.activity.AbstractActivity;
import it.localhost.trafficdroid.common.TdAdListener;
import it.localhost.trafficdroid.common.Utility;
import it.localhost.trafficdroid.dao.PatenteService;
import it.localhost.trafficdroid.dto.BaseDTO;
import it.localhost.trafficdroid.dto.PatenteDTO;
import it.localhost.trafficdroid.fragment.MessageDialogFragment;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class PatenteFragment extends Fragment implements TabListener {
	private static final String BLANK = "";
	private static final String PATENTE_PWD = "PatentePwd";
	private static final String PATENTE_USR = "PatenteUsr";
	private TextView patenteSaldo, patenteNumero, patenteScadenza;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.patente, null);
		getActivity().setProgressBarIndeterminateVisibility(false);
		final EditText usrEdit = (EditText) v.findViewById(R.id.patenteUsr);
		final EditText pwdEdit = (EditText) v.findViewById(R.id.patentePwd);
		patenteSaldo = (TextView) v.findViewById(R.id.patenteSaldo);
		patenteNumero = (TextView) v.findViewById(R.id.patenteNumero);
		patenteScadenza = (TextView) v.findViewById(R.id.patenteScadenza);
		usrEdit.setText(Utility.getPrefString(getActivity(), PATENTE_USR, BLANK));
		pwdEdit.setText(Utility.getPrefString(getActivity(), PATENTE_PWD, BLANK));
		v.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String usr = usrEdit.getText().toString();
				String pwd = pwdEdit.getText().toString();
				Editor edit = Utility.getEditor(getActivity());
				edit.putString(PATENTE_USR, usr);
				edit.putString(PATENTE_PWD, pwd);
				edit.commit();
				if (!usr.equals(BLANK) && !pwd.equals(BLANK))
					new PatenteAsyncTask().execute(usr, pwd);
				else
					new MessageDialogFragment().show(getFragmentManager(), getString(R.string.error), getString(R.string.wrongData), false);
			}
		});
		if (!((AbstractActivity) getActivity()).isInterstitialFree()) {
			InterstitialAd interstitial = new InterstitialAd(getActivity(), getString(R.string.adUnitId));
			interstitial.setAdListener(new TdAdListener());
			AdRequest adRequest = new AdRequest();
			adRequest.addTestDevice(getString(R.string.testDevices));
			interstitial.loadAd(adRequest);
		}
		return v;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(android.R.id.content, this);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	private class PatenteAsyncTask extends PatenteService {
		@Override
		protected void onPreExecute() {
			getActivity().setProgressBarIndeterminateVisibility(true);
			InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}

		@Override
		protected void onPostExecute(BaseDTO abstractResult) {
			getActivity().setProgressBarIndeterminateVisibility(false);
			if (abstractResult.isSuccess()) {
				PatenteDTO patente = (PatenteDTO) abstractResult;
				patenteSaldo.setText(patente.getSaldo());
				patenteNumero.setText(patente.getNumeoPatente());
				patenteScadenza.setText(patente.getScadenzaPatente());
			} else
				new MessageDialogFragment().show(getFragmentManager(), getString(R.string.error), abstractResult.getMessage(), false);
		}
	}
}
