package ru.itx.conduit.ui.Fragments;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.itx.conduit.DataHelper;
import ru.itx.conduit.Group;
import ru.itx.conduit.R;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import ru.itx.conduit.ui.FragmentTools;
import ru.itx.conduit.ui.MainActivity;
import android.R.string;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddGroupFragment extends FragmentTools {
	View view;
	GroupFragment frag;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.add_group, container, false);
		Button add_group = (Button) view.findViewById(R.id.add);
		add_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// добавить в базу
				String name = ((EditText) view.findViewById(R.id.name))
						.getText().toString();
				String city = ((EditText) view.findViewById(R.id.city))
						.getText().toString();
				String teacher = ((EditText) view.findViewById(R.id.teacher))
						.getText().toString();
				String additional = ((EditText) view.findViewById(R.id.additinal))
						.getText().toString();
				String timetable = ((EditText) view.findViewById(R.id.timetable))
						.getText().toString();
				Group group = new Group(name, city, teacher, additional, timetable);
				DataHelper.getDH().insert(group);
				// ===
				go2Fragment(((MainActivity)getActivity()).groupFragment);
			}
		});
		return view;
	}

}
