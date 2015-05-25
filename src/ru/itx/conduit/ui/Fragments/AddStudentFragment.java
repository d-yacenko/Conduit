package ru.itx.conduit.ui.Fragments;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

public class AddStudentFragment extends FragmentTools {
	View view;
	StudentFragment frag;
	static final int REQUEST_IMAGE_CAPTURE = 1;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.add_student, container, false);
		Button add_student = (Button) view.findViewById(R.id.add);
		add_student.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// добавить в базу
				String name = ((EditText) view.findViewById(R.id.name))
						.getText().toString();
				String lastname = ((EditText) view.findViewById(R.id.lastname))
						.getText().toString();
				String surname = ((EditText) view.findViewById(R.id.surname))
						.getText().toString();
				String email = ((EditText) view.findViewById(R.id.email))
						.getText().toString();
				String phone = ((EditText) view.findViewById(R.id.phone))
						.getText().toString();
				String parentphone = ((EditText) view
						.findViewById(R.id.parentphone)).getText().toString();
				String birthday = ((EditText) view.findViewById(R.id.birthday))
						.getText().toString();
				String numclass = ((EditText) view.findViewById(R.id.numclass))
						.getText().toString();
				boolean hidden = ((CheckBox) view.findViewById(R.id.hidden))
						.isChecked();
				byte[] byteArray=null;
				if(((Boolean)((ImageView) view.findViewById(R.id.photo)).getTag())){
					((ImageView) view.findViewById(R.id.photo)).buildDrawingCache();
					Bitmap bitmap = ((ImageView) view.findViewById(R.id.photo))
						.getDrawingCache();
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.PNG, 100, bos);
					byteArray = bos.toByteArray();
				}
				SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				Date myDate = null;
				try {
					myDate = df.parse(birthday);
					String myText = saySimple(myDate);
				} catch (ParseException e) {
					Log.v(TAG,e.getMessage());
					say(getString(R.string.error_date));
				}
				Student student = new Student(name, lastname, surname, email,
						phone, parentphone, myDate, numclass, hidden, byteArray);
				DataHelper.getDH().insert(student);
				// ===
				go2Fragment(((MainActivity) getActivity()).studentFragment);
			}
		});
		
		ImageView photo = (ImageView) view.findViewById(R.id.photo);
		photo.setTag(new Boolean(false));
		photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
					startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
				} else {
					say(getString(R.string.cant_take_shot));
				}
			}
		});
			
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE
				&& resultCode == getActivity().RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			((ImageView)view.findViewById(R.id.photo)).setImageBitmap(imageBitmap);
			((ImageView)view.findViewById(R.id.photo)).setTag(new Boolean(true));
		} else
			say(getString(R.string.dont_press_back));
	}

}
