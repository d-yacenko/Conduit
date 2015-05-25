package ru.itx.conduit.ui.Fragments;

import java.util.ArrayList;
import java.util.List;

import ru.itx.conduit.ASecurity;
import ru.itx.conduit.AccountModel;
import ru.itx.conduit.Group;
import ru.itx.conduit.R;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import ru.itx.conduit.Student;
import ru.itx.conduit.ui.FragmentTools;
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

public class HelpFragment extends FragmentTools {
	View view;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.help, container, false);
		((TextView)view.findViewById(R.id.number_security)).setText(String.valueOf(ASecurity._getNumberSecurity()));
		((TextView)view.findViewById(R.id.number_student)).setText(String.valueOf(Student._getNumberStudent()));
		((TextView)view.findViewById(R.id.number_group)).setText(String.valueOf(Group._getNumberGroup()));
		((TextView)view.findViewById(R.id.number_account)).setText(String.valueOf(AccountModel._getNumberLesson()));
		return view;
	}

}
