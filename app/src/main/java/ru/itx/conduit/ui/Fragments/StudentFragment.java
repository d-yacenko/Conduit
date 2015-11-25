package ru.itx.conduit.ui.Fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.itx.conduit.DataHelper;
import ru.itx.conduit.R;
import ru.itx.conduit.Student;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import ru.itx.conduit.ui.FragmentTools;
import ru.itx.conduit.ui.MainActivity;
import android.R.string;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class StudentFragment extends FragmentTools {
	View view;
	ListView listView;
	
	List<Student> students;
	List<String> strs;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.student, container, false);
		students = DataHelper.getDH().selectAll_student();
		Collections.sort(students);
		strs = new ArrayList<String>();
		if (students != null)
			for (Student s : students)
				strs.add(s.getFullName());

		listView = (ListView) view.findViewById(R.id.list_student);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, strs);
		listView.setAdapter(adapter);
		Button add_student = (Button) view.findViewById(R.id.add_student);
		add_student.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				go2Fragment(((MainActivity)getActivity()).addStudentFragment=new AddStudentFragment());
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View itemClicked,
					int position, long id) {
				//frag1 = new EditStudentFragment();
				((MainActivity)getActivity()).editStudentFragment=new EditStudentFragment();
				((MainActivity)getActivity()).editStudentFragment.setStudent(students.get(position));
				go2Fragment(((MainActivity)getActivity()).editStudentFragment);
			}
		});
		return view;
	}

}
