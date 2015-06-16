package ru.itx.conduit.ui;

import java.util.List;

import ru.itx.conduit.R;
import ru.itx.conduit.RowAccountRecordModel;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import ru.itx.conduit.R.string;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class AccountRecordArrayAdapter extends
		ArrayAdapter<RowAccountRecordModel> {
	private final List<RowAccountRecordModel> list;
	private final Activity context;

	public List<RowAccountRecordModel> getList() {
		return list;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	public AccountRecordArrayAdapter(Activity context,
			List<RowAccountRecordModel> list) {
		super(context, R.layout.row_account_record, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		RowAccountRecordModel model = list.get(position);
		if (view == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.row_account_record, null);
		}
		String name = model.getStudent().getName()
				+ " "
				+ (model.getStudent().getLastName() == null ? "" : model
						.getStudent().getLastName()) + " "
				+ model.getStudent().getSurName();
		((TextView) view.findViewById(R.id.student)).setText(name);
		String s = model.getValue();
		if (!s.equals(view.getResources().getString(R.string._q))
				&& !s.equals(view.getResources().getString(R.string._h))
				&& !s.equals(view.getResources().getString(R.string._n))
				&& !s.equals(""))
			((EditText) view.findViewById(R.id.rate)).setText(s);
		else
			((EditText) view.findViewById(R.id.rate)).setText("");
		if (s.equals(view.getResources().getString(R.string._n)))
			((RadioButton) view.findViewById(R.id.radio_n)).setChecked(true);
		if (s.equals(view.getResources().getString(R.string._h)))
			((RadioButton) view.findViewById(R.id.radio_h)).setChecked(true);
		if (s.equals(view.getResources().getString(R.string._q)))
			((RadioButton) view.findViewById(R.id.radio_q)).setChecked(true);
		((EditText) view.findViewById(R.id.rate))
				.addTextChangedListener(new RateTextWatcher(position));
		((RadioButton) view.findViewById(R.id.radio_n))
				.setOnClickListener(new RateOnClickListener(position, view
						.getResources().getString(R.string._n)));
		((RadioButton) view.findViewById(R.id.radio_h))
				.setOnClickListener(new RateOnClickListener(position, view
						.getResources().getString(R.string._h)));
		((RadioButton) view.findViewById(R.id.radio_q))
				.setOnClickListener(new RateOnClickListener(position, view
						.getResources().getString(R.string._q)));
		return view;
	}

	class RateOnClickListener implements OnClickListener {
		private int position;
		String sign;

		RateOnClickListener(int position, String sign) {
			this.position = position;
			this.sign = sign;
		}

		@Override
		public void onClick(View v) {
			String s = list.get(position).getValue();
			if (s.equals(v.getResources().getString(R.string._n))
					|| s.equals(v.getResources().getString(R.string._h))
					|| s.equals(v.getResources().getString(R.string._q))
					|| s.equals("")) {
				list.get(position).setValue(sign);
				// ((RadioButton)v.getTag()).setChecked(true);
			}
		}
	}

	class RateTextWatcher implements TextWatcher {
		private int position;

		public RateTextWatcher(int position) {
			this.position = position;
		}

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
			if (String.valueOf(s).length() == 0)
				return;
			list.get(position).setValue(String.valueOf(s));
			// Toast.makeText(context,"Check!"+position,
			// Toast.LENGTH_SHORT).show();
		}

	}
}
