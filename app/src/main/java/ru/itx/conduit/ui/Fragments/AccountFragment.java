package ru.itx.conduit.ui.Fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.itx.conduit.AccountModel;
import ru.itx.conduit.DataHelper;
import ru.itx.conduit.Group;
import ru.itx.conduit.R;
import ru.itx.conduit.RowAccountRecordModel;
import ru.itx.conduit.Student;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import ru.itx.conduit.ui.FragmentTools;
import ru.itx.conduit.ui.MainActivity;
import android.R.string;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AccountFragment extends FragmentTools {
	View view;
	AddAccountRecordFragment frag;
	List<Group> groups;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.account, container, false);
		Button add_account_row = (Button) view.findViewById(R.id.add_lesson);

		add_account_row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				go2Fragment(new AddAccountRecordFragment());
			}
		});
		groups = DataHelper.getDH().selectAll_group();
		List<String> gtrs = new ArrayList<String>();
		for (Group g : groups)
			gtrs.add(g.getName());
		// адаптер
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, gtrs);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner spinner = (Spinner) view.findViewById(R.id.group);
		spinner.setAdapter(adapter1);
		// заголовок
		spinner.setPrompt("Group");
		// выделяем элемент
		spinner.setSelection(0);
		// устанавливаем обработчик нажатия
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				drawTable(groups.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				if (groups.size() > 0)
					drawTable(groups.get(0));
			}
		});

		return view;
	}

	private TextView getVBorder() {
		TextView vborder = new TextView(getActivity());
		vborder.setBackgroundColor(getResources().getColor(R.color.black));
		// обрисовка линиями
		TableRow.LayoutParams blp = new TableRow.LayoutParams(1,
				LayoutParams.MATCH_PARENT, 1);
		vborder.setLayoutParams(blp);
		return vborder;
	}

	private void drawTable(Group group) {
		List<AccountModel> list = DataHelper.getDH()
				.selectAll_Account(group);
		TableLayout table = (TableLayout) view.findViewById(R.id.account_table);
		table.removeAllViews();
		if (list == null)
			return;
		// выбираем всех учащихся
		List<Student> students = new ArrayList<Student>();
		for (AccountModel a : list)
			for (RowAccountRecordModel r : a.list)
				if (!students.contains(r.getStudent())
						&& !r.getStudent().isHidden())
					students.add(r.getStudent());
		//
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int maxNum = (width - LEFT_COLUMN_WIDTH_PX) / COLUMN_WIDTH_PX;
		int start = list.size() > maxNum ? list.size() - maxNum : 0;
		// строим таблицу
		TableRow.LayoutParams llp = new TableRow.LayoutParams(COLUMN_WIDTH_PX,
				LayoutParams.WRAP_CONTENT, 1);
		TableRow.LayoutParams llh = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// table.setStretchAllColumns(false);
		table.setShrinkAllColumns(false);
		// фомируем шапку таблицы: поле (0,0)
		TableRow header = new TableRow(getActivity());
		TextView cellLeftUp = new TextView(getActivity());
		cellLeftUp.setLayoutParams(llh);
		cellLeftUp.setLines(3);
		cellLeftUp.setMaxLines(3);
		header.addView(cellLeftUp);
		header.addView(getVBorder());
		for (int i = start; i < list.size(); i++) {
			TextView cellHeader = new TextView(getActivity());
			cellHeader.setText(saySimple(new Date(list.get(i).date)) + "\n"
					+ list.get(i).subject);
			cellHeader.setTypeface(Typeface.SERIF, Typeface.BOLD);
			cellHeader.setBackgroundColor(getResources().getColor(
					R.color.lightgray));
			cellHeader.setLines(2);
			cellHeader.setMaxLines(2);
			cellHeader.setLayoutParams(llp);
			cellHeader.setRotation(-90);
			cellHeader.setClickable(true);
			cellHeader.setTag(list.get(i));
			cellHeader.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					EditAccountRecordFragment editAccountRecordFragment = new EditAccountRecordFragment();
					editAccountRecordFragment.setAccount(((AccountModel) v.getTag()));
					go2Fragment(editAccountRecordFragment);
				}
			});
			header.addView(cellHeader);
			header.addView(getVBorder());
		}
		table.addView(header);
		// строим левый хидер
		int j=0;
		for (Student s : students) {
			j++;
			TableRow row = new TableRow(getActivity());
			TextView cellHeader = new TextView(getActivity());
			cellHeader.setText(s.getName() + " " + (s.getLastName()==null?"":s.getLastName()) + " "
					+ s.getSurName());
			cellHeader.setTypeface(Typeface.SERIF, Typeface.BOLD);
			if(j%2==0)
			cellHeader.setBackgroundColor(getResources().getColor(
					R.color.lightgray));
			else
				cellHeader.setBackgroundColor(getResources().getColor(
						R.color.gray));
			cellHeader.setLayoutParams(llh);
			cellHeader.setLines(1);
			cellHeader.setMaxLines(1);
			cellHeader.setClickable(true);
			cellHeader.setTag(s);
			cellHeader.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((MainActivity) getActivity()).editStudentFragment = new EditStudentFragment();
					((MainActivity) getActivity()).editStudentFragment
							.setStudent(((Student) v.getTag()));
					go2Fragment(((MainActivity) getActivity()).editStudentFragment);
				}
			});
			row.addView(cellHeader);
			row.addView(getVBorder());

			for (int i = start; i < list.size(); i++) {
				String rez = "";
				for (RowAccountRecordModel r : list.get(i).list) {
					if (s.equals(r.getStudent())) {
						rez = r.getValue();
					}
				}
				TextView cell = new TextView(getActivity());
				cell.setText(rez);
				cell.setLayoutParams(llp);
				if(j%2==0)
					cell.setBackgroundColor(getResources().getColor(R.color.white));
				else
					cell.setBackgroundColor(getResources().getColor(R.color.lightpink));
				cell.setGravity(Gravity.CENTER);
				row.addView(cell);
				row.addView(getVBorder());
			}

			table.addView(row);
		}
		List<TableRow> listRow = new ArrayList<TableRow>();
	}

}