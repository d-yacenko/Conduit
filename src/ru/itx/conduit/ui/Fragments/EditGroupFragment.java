package ru.itx.conduit.ui.Fragments;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditGroupFragment extends FragmentTools {
	private View view;
	private GroupFragment frag;
	private ListGroupFragment frag1;
	private Group group;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.edit_group, container, false);
		if (group == null)
			group = new Group();
		// ((TextView) view.findViewById(R.id._id)).setText(String.valueOf(group
		// .get_id()));
		((EditText) view.findViewById(R.id.name)).setText(group.getName());
		((EditText) view.findViewById(R.id.city)).setText(group.getCity());
		((EditText) view.findViewById(R.id.teacher))
				.setText(group.getTeacher());
		((EditText) view.findViewById(R.id.additinal)).setText(group
				.getAdditional());
		((EditText) view.findViewById(R.id.timetable)).setText(group
				.getTimetable());
		Button edit_group = (Button) view.findViewById(R.id.edit);
		edit_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// редактировать в базу
				String name = ((EditText) view.findViewById(R.id.name))
						.getText().toString();
				String city = ((EditText) view.findViewById(R.id.city))
						.getText().toString();
				String teacher = ((EditText) view.findViewById(R.id.teacher))
						.getText().toString();
				String additional = ((EditText) view
						.findViewById(R.id.additinal)).getText().toString();
				String timetable = ((EditText) view
						.findViewById(R.id.timetable)).getText().toString();
				group = new Group(group.get_id(), name, city, teacher,
						additional, timetable);
				DataHelper.getDH().update(group);
				// ===
				go2Fragment(((MainActivity) getActivity()).groupFragment);
			}
		});

		Button delete_group = (Button) view.findViewById(R.id.delete);
		delete_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				areYouSure(new Runnable() {
					@Override
					public void run() {
						// редактировать в базу
						DataHelper.getDH().delete(group);
						// ===
						go2Fragment(new GroupFragment());
					}
				});
				
			}
		});
		Button list_group = (Button) view.findViewById(R.id.list);
		list_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ===
				ListGroupFragment frag=new ListGroupFragment();
				frag.setGroup(group);
				go2Fragment(frag);
			}
		});

		return view;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}
