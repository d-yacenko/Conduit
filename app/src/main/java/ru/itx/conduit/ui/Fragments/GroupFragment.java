package ru.itx.conduit.ui.Fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.itx.conduit.DataHelper;
import ru.itx.conduit.Group;
import ru.itx.conduit.R;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import ru.itx.conduit.ui.FragmentTools;
import ru.itx.conduit.ui.MainActivity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupFragment extends FragmentTools {
	View view;
	ListView listView;

	List<Group> groups;
	List<String> strs;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.group, container, false);
		groups = DataHelper.getDH().selectAll_group();
		Collections.sort(groups);
		strs = new ArrayList<String>();
		if (groups != null)
			for (Group s : groups)
				strs.add(s.getName());
		listView = (ListView) view.findViewById(R.id.list_group);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, strs);
		listView.setAdapter(adapter);
		Button add_group = (Button) view.findViewById(R.id.add_group);

		add_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				go2Fragment(((MainActivity)getActivity()).addGroupFragment=new AddGroupFragment());
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View itemClicked,
					int position, long id) {
				((MainActivity)getActivity()).editGroupFragment=new EditGroupFragment();
				((MainActivity)getActivity()).editGroupFragment.setGroup(groups.get(position));
				go2Fragment(((MainActivity)getActivity()).editGroupFragment);
			}
		});	
		
		return view;
	}
}
