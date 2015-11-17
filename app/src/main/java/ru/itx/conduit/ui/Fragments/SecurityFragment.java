package ru.itx.conduit.ui.Fragments;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import ru.itx.conduit.ASecurity;
import ru.itx.conduit.DataHelper;
import ru.itx.conduit.R;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import ru.itx.conduit.exceptions.ConduitException;
import ru.itx.conduit.exceptions.DeletePasswordException;
import ru.itx.conduit.exceptions.PasswordIsDifferedException;
import ru.itx.conduit.ui.FragmentTools;
import ru.itx.conduit.ui.MainActivity;
import android.R.string;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SecurityFragment extends FragmentTools {
	View view;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.security, container, false);
		TextView number_security=(TextView)view.findViewById(R.id.number_security);
		number_security.setText(String.valueOf(ASecurity._getNumberSecurity()));
		
		Button delete_password = (Button) view.findViewById(R.id.delete_password);
		delete_password.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					ASecurity._deletePassword(getActivity(), 
							((EditText)view.findViewById(R.id.password_for_delete_1)).getText().toString(), 
							((EditText)view.findViewById(R.id.password_for_delete_2)).getText().toString());
				} catch (ConduitException e) {
					say(e.getLocalizedMessage());
					return;
				} 
				say(getString(R.string.password_deleted));
			}
		});
		
		Button add_password=(Button) view.findViewById(R.id.add_password);
		add_password.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					ASecurity._addPassword(getActivity(),
							((EditText)view.findViewById(R.id.password_add_actual)).getText().toString(), 
							((EditText)view.findViewById(R.id.password_add_1)).getText().toString(), 
							((EditText)view.findViewById(R.id.password_add_2)).getText().toString());
				} catch (ConduitException e) {
					say(e.getLocalizedMessage());
					return;
				}
				say(getString(R.string.password_added));
			}
		});
		
		Button change_password=(Button) view.findViewById(R.id.change_password);
		change_password.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					ASecurity._changePassword(getActivity(),
							((EditText)view.findViewById(R.id.password_change_actual)).getText().toString(), 
							((EditText)view.findViewById(R.id.password_change_1)).getText().toString(), 
							((EditText)view.findViewById(R.id.password_change_2)).getText().toString());
				} catch (ConduitException e) {
					say(e.getLocalizedMessage());
					return;
				}
				say(getString(R.string.password_changed));
			}
		});
		TextView replica_id= (TextView)view.findViewById(R.id.replica_id);
		replica_id.setText(ASecurity._getReplicaId());
		Button set_replica=(Button) view.findViewById(R.id.set_replica);
		set_replica.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ASecurity._setReplica(((EditText)view.findViewById(R.id.replica_id)).getText().toString());
			}
		});
		return view;
	}

	
	
}
