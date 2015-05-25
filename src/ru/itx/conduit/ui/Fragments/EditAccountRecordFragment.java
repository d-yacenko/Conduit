package ru.itx.conduit.ui.Fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ru.itx.conduit.AccountModel;
import ru.itx.conduit.DataHelper;
import ru.itx.conduit.Group;
import ru.itx.conduit.R;
import ru.itx.conduit.RowAccountRecordModel;
import ru.itx.conduit.Student;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import ru.itx.conduit.ui.AccountRecordArrayAdapter;
import ru.itx.conduit.ui.FragmentTools;
import ru.itx.conduit.ui.MainActivity;
import android.R.string;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditAccountRecordFragment extends FragmentTools {
	View view;
	AccountFragment frag;
	ListView listView;
	List<RowAccountRecordModel> strs;
	EditText date, subject;
	private AccountModel account;
	private String old_subject;
	private long old_date;
	

	public void setAccount(AccountModel account) {
		this.account = account;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.edit_account_record, container, false);
		subject = (EditText) view.findViewById(R.id.subject);
		old_subject=account.subject;
		old_date=account.date;
		subject.setText(account.subject);
		date = (EditText) view.findViewById(R.id.date);
		date.setText(saySimple(new Date(account.date)));
		date.addTextChangedListener(new DateTextWatcher());

		listView = (ListView) view.findViewById(R.id.list_student);
		ArrayAdapter adapter = new AccountRecordArrayAdapter(getActivity(),
				account.list);
		listView.setAdapter(adapter);
		Button editrecord = (Button) view.findViewById(R.id.edit_record);
		editrecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// изменить в базу
				List<RowAccountRecordModel> list = ((AccountRecordArrayAdapter) listView
						.getAdapter()).getList();
				for (RowAccountRecordModel r : list)
					if (r.getValue().equals(""))
						r.setValue(v.getResources().getString(R.string._n));
				SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				df.setLenient(true);
				Date myDate = null;
				try {
					myDate = df.parse(((EditText) view.findViewById(R.id.date))
							.getText().toString());
					String myText = saySimple(myDate);
				} catch (ParseException e) {
					Log.v(TAG,e.getMessage());
					say(getString(R.string.error_date));
				}
				long ldate = myDate.getTime();
				String subj = ((EditText) view.findViewById(R.id.subject))
						.getText().toString();
				int i = DataHelper.getDH()
						.editAccountRecords(old_date,old_subject,ldate, subj, list);
				// ===
				go2Fragment(new AccountFragment());
			}
		});

		Button deleterecord = (Button) view.findViewById(R.id.delete_record);
		deleterecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// удалить в базу
				int i = DataHelper.getDH().delete(account);
				// ===
				go2Fragment(new AccountFragment());
			}
		});

		return view;
	}

	class DateTextWatcher implements TextWatcher {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			Date myDate = null;
			try {
				df.setLenient(true);
				myDate = df.parse(s.toString());
				String myText = saySimple(myDate);
				if (!s.toString().equals(myText))
					say(getString(R.string.error_date));
			} catch (ParseException e) {
				Log.v(TAG,e.getMessage());
				say(getString(R.string.error_date));
			}
		}
	}

}
