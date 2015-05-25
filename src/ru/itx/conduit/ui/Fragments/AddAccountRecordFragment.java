package ru.itx.conduit.ui.Fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

public class AddAccountRecordFragment extends FragmentTools {
	View view;
	AccountFragment frag;
	ListView listView;
	List<Student> students;
	List<Group> groups;
	Group group;
	List<RowAccountRecordModel> strs;
	EditText date, subject;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.add_account_record, container, false);
		subject = (EditText) view.findViewById(R.id.subject);
		date = (EditText) view.findViewById(R.id.date);
		date.setText(saySimple(new Date()));
		date.addTextChangedListener(new DateTextWatcher());
		groups = DataHelper.getDH().selectAll_group();
		Collections.sort(groups);
		List<String> gtrs=new ArrayList<String>();
		for(Group g:groups)
			gtrs.add(g.getName());
		// адаптер
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, gtrs);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner spinner = (Spinner) view.findViewById(R.id.group);
		spinner.setAdapter(adapter1);
		// заголовок
		spinner.setPrompt("Title");
		// выделяем элемент
		spinner.setSelection(0);
		// устанавливаем обработчик нажатия
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// показываем позиция нажатого элемента
				group=groups.get(position);
				makeListStudent();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				if(groups.size()>0){
				group=groups.get(0);
				makeListStudent();
				}
			}
		});

		Button addrecord = (Button) view.findViewById(R.id.add_record);
		addrecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// добавить в базу
				List<RowAccountRecordModel> list = ((AccountRecordArrayAdapter) listView
						.getAdapter()).getList();
				for(RowAccountRecordModel r:list)
					if(r.getValue().equals(""))
						r.setValue(v.getResources().getString(R.string._n));
				SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				df.setLenient(true);
				Date myDate = null;
				try {
					myDate = df.parse(((EditText) view.findViewById(R.id.date)).getText().toString());
					String myText = saySimple(myDate);
				} catch (ParseException e) {
					Log.v(TAG,e.getMessage());
					say(getString(R.string.error_date));
				}
				long ldate=myDate.getTime();
				String subj=((EditText)view.findViewById(R.id.subject)).getText().toString();
				int i=DataHelper.getDH().insertAccountRecords(ldate,subj,list);
				// ===
				go2Fragment(new AccountFragment());
			}
		});

		return view;
	}

	
	private void makeListStudent(){
		students = DataHelper.getDH().selectAll_studentForGroup(group);
		Collections.sort(students);
		strs = new ArrayList<RowAccountRecordModel>();
		if (students != null)
			for (Student s : students)
				strs.add(new RowAccountRecordModel(s,group));
		listView = (ListView) view.findViewById(R.id.list_student);
		ArrayAdapter adapter = new AccountRecordArrayAdapter(getActivity(),
				strs);
		listView.setAdapter(adapter);
	} 
	
	
	
	
	class DateTextWatcher implements TextWatcher{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
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
				if(!s.toString().equals(myText)) say(getString(R.string.error_date));
			} catch (ParseException e) {
				Log.v(TAG,e.getMessage());
				say(getString(R.string.error_date));
			}
		}
	}

	
}
