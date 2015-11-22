package ru.itx.conduit.ui.Fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.itx.conduit.DataHelper;
import ru.itx.conduit.Group;
import ru.itx.conduit.R;
import ru.itx.conduit.RowGroupListModel;
import ru.itx.conduit.Student;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import ru.itx.conduit.ui.FragmentTools;
import ru.itx.conduit.ui.GroupListArrayAdapter;
import ru.itx.conduit.ui.MainActivity;
import android.R.string;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ListGroupFragment extends FragmentTools {
	private View view;
	private ListView listView;
	private Group group;
	private List<Student> students;
	private List<RowGroupListModel> strs;

	public void setGroup(Group group) {
		this.group = group;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.list_group, container, false);
		((TextView) view.findViewById(R.id.group)).setText(group.getName());
		students = DataHelper.getDH().selectAll_student();
		for(Student s:students) 
			if(s.isHidden()) students.remove(s);
		Collections.sort(students);
		strs = new ArrayList<RowGroupListModel>();
		if (students != null)
			for (Student s : students)
				strs.add(new RowGroupListModel(s));
		// пройти по имеющейся таблице включить чек!
		students = DataHelper.getDH()
				.selectAll_studentForGroup(group);
		for (RowGroupListModel r : strs)
			for (Student s : students)
				if (s.get_id() == r.getStudent().get_id()) {
					r.setIncluded(true);
					break;
				}
		//
		listView = (ListView) view.findViewById(R.id.list_student);
		ArrayAdapter<RowGroupListModel> adapter = new GroupListArrayAdapter(
				getActivity(), strs);
		listView.setAdapter(adapter);
		Button edit_grouplist = (Button) view.findViewById(R.id.edit);
		edit_grouplist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// сохранить в базу
				// удалить все записи по группе
				DataHelper.getDH().deleteAllGrolupMember(
						group);
				// сохранить все записи по группе
				for (RowGroupListModel r : strs)
					if (r.isIncluded())
						DataHelper.getDH()
								.insertGroupMember(group, r.getStudent());
				// вернутся к нужному фрагменту
				go2Fragment(((MainActivity) getActivity()).groupFragment);
			}
		});
		TextView group=(TextView)view.findViewById(R.id.group);
		group.setOnClickListener(new onClickSendMailListener());
		ImageButton sendEmail_group = (ImageButton) view
				.findViewById(R.id.sendEmail);
		sendEmail_group.setOnClickListener(new onClickSendMailListener());

		ImageButton sendSms_group = (ImageButton)view.findViewById(R.id.sendSms);
		sendSms_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> addresses = new ArrayList<String>();
				for (RowGroupListModel r : strs)
						if (!r.getStudent().isHidden()
								&& r.getStudent().getPhone() != null
								&& r.getStudent().getPhone().toString() != null)
						addresses.add(r.getStudent().getPhone().toString());
				sendSms(addresses.toArray(new String[addresses.size()]));
			}
		});
		
		return view;
	}


	
	class  onClickSendMailListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			List<String> addresses = new ArrayList<String>();
			for (RowGroupListModel r : strs)
				if (!r.getStudent().isHidden()
						&& r.getStudent().getEmail() != null
						&& r.getStudent().getEmail().toString() != null)
					addresses.add(r.getStudent().getEmail().toString());
			sendMail(addresses.toArray(new String[addresses.size()]));
		}		
	}
	
}
