package ru.itx.conduit.ui.Fragments;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.itx.conduit.DataHelper;
import ru.itx.conduit.R;
import ru.itx.conduit.Student;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import ru.itx.conduit.R.string;
import ru.itx.conduit.ui.FragmentTools;
import ru.itx.conduit.ui.MainActivity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditStudentFragment extends FragmentTools {
	private View view;
	private StudentFragment frag;
	private Student student;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.edit_student, container, false);
		if (student == null)
			student = new Student();
		((EditText) view.findViewById(R.id.name)).setText(student.getName());
		((EditText) view.findViewById(R.id.lastname)).setText(student
				.getLastName());
		((EditText) view.findViewById(R.id.surname)).setText(student
				.getSurName());
		((EditText) view.findViewById(R.id.email)).setText(student.getEmail());
		((EditText) view.findViewById(R.id.phone)).setText(student.getPhone());
		((EditText) view.findViewById(R.id.parentphone)).setText(student
				.getParentPhone());
		if (student.getBirthDate() != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			((EditText) view.findViewById(R.id.birthday)).setText(df
					.format(student.getBirthDate()));
		}
		((EditText) view.findViewById(R.id.numclass)).setText(student
				.getNumClass());
		((CheckBox) view.findViewById(R.id.hidden)).setChecked(student
				.isHidden());
		// !!! не проверено
		ImageView photo = (ImageView) view.findViewById(R.id.photo);
		photo.setTag(new Boolean(false));
		if(student.getPhoto()!=null){
		Bitmap bm = BitmapFactory.decodeByteArray(student.getPhoto(), 0,
				student.getPhoto().length);
		photo.setImageBitmap(bm);
		}
		Button edit_student = (Button) view.findViewById(R.id.edit);
		edit_student.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// редактировать в базу
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
					bitmap.compress(CompressFormat.JPEG, 100, bos);
					byteArray = bos.toByteArray();
				}//else из базы
				else{
					byteArray= student.getPhoto();
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
				student = new Student(student.get_id(), name, lastname,
						surname, email, phone, parentphone, myDate, numclass,
						hidden, byteArray);
				DataHelper.getDH().update(student);
				// ===
				go2Fragment(((MainActivity) getActivity()).studentFragment);
			}
		});

		Button delete_student = (Button) view.findViewById(R.id.delete);
		delete_student.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				// !!! Запрос подтверждения
				areYouSure(new Runnable() {
					@Override
					public void run() {						// редактировать в базу
						DataHelper.getDH().delete(student);
						go2Fragment(new StudentFragment());
						}
				});
			}
		});

		ImageButton sendSms_student = (ImageButton) view
				.findViewById(R.id.sendSms);
		sendSms_student.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendSms(((EditText) view.findViewById(R.id.phone)).getText()
						.toString());
			}
		});
		
		ImageButton callPhone_student = (ImageButton) view
				.findViewById(R.id.callPhone);
		callPhone_student.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playPhone(((EditText) view.findViewById(R.id.phone)).getText()
						.toString());
			}
		});
		
		ImageButton sendSms_parent = (ImageButton) view
				.findViewById(R.id.sendSmsParent);
		sendSms_parent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendSms(((EditText) view.findViewById(R.id.parentphone)).getText()
						.toString());
			}
		});

		ImageButton playPhoneParent_student = (ImageButton) view
				.findViewById(R.id.callParentPhone);
		playPhoneParent_student.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playPhone(((EditText) view.findViewById(R.id.parentphone)).getText()
						.toString());
			}
		});
		
		
		ImageButton sendEmail_student = (ImageButton) view
				.findViewById(R.id.sendEmail);
		sendEmail_student.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMail(((EditText) view.findViewById(R.id.email)).getText()
						.toString());
			}
		});
		
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

	
	public void setStudent(Student student) {
		this.student = student;
	}

}
