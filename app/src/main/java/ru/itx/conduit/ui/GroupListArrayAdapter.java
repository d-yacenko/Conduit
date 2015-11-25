package ru.itx.conduit.ui;

import java.util.List;

import ru.itx.conduit.R;
import ru.itx.conduit.RowGroupListModel;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupListArrayAdapter extends ArrayAdapter<RowGroupListModel> {
	private final List<RowGroupListModel> list;
	private final Activity context;

	public List<RowGroupListModel> getList() {
		return list;
	}

	public GroupListArrayAdapter(Activity context, List<RowGroupListModel> list) {
		super(context, R.layout.row_group_list, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		RowGroupListModel model = list.get(position);
		String name = model.getStudent().getFullName();
		if (view == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.row_group_list, null);
		}
		((CheckBox) view.findViewById(R.id.included))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						list.get((Integer) v.getTag()).setIncluded(
								!list.get((Integer) v.getTag()).isIncluded());
						notifyDataSetChanged();
					}
				});

		((TextView) view.findViewById(R.id.student)).setText(name);
		((CheckBox) view.findViewById(R.id.included)).setTag(new Integer(
				position));
		((CheckBox) view.findViewById(R.id.included)).setChecked(model
				.isIncluded());
		return view;
	}

}
