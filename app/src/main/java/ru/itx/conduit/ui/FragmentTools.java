package ru.itx.conduit.ui;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import ru.itx.conduit.R;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.string;

public class FragmentTools extends Fragment {
	public static final int REQUEST_IMAGE_CAPTURE = 1;
	public static final int COLUMN_WIDTH_PX=40;
	public static final int LEFT_COLUMN_WIDTH_PX=400;
	public static final String TAG="EXCEPTION";
	
	public void say(String str){
		Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
	}

	public void sendMail(String address) {
		sendMail(new String[] { address });
	}

	protected Intent prepareEmailIntent(String[] address) {
		final Intent emailIntent = new Intent(
				android.content.Intent.ACTION_SEND_MULTIPLE);
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, address);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					getString(R.string.default_subject));
			emailIntent.setType("text/html");
			return emailIntent;
	}
	
	public void sendMail(String[] address) {
		try {
			startActivity(Intent.createChooser(prepareEmailIntent(address),
					getString(R.string.email_sending)));
		} catch (Exception e) {
			Log.v(TAG,e.getMessage());
			say(getString(R.string.send_fail));
		}
	}

	public void sendSms(String[] phone) {
		String separator = "; ";
		if (android.os.Build.MANUFACTURER.equalsIgnoreCase("Samsung"))
			separator = ", ";
		String p = "";
		for (int i = 0; i < phone.length; i++) {
			if (i + 1 == phone.length)
				p += phone[i];
			else
				p += phone[i] + separator;
		}
		sendSms(p);
	}

	public void sendSms(String phone) {
		try {

			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.putExtra("address", phone);
			sendIntent.putExtra("sms_body", "IT School Samsung\n");
			sendIntent.setType("vnd.android-dir/mms-sms");
			startActivity(sendIntent);
		} catch (Exception e) {
			Log.v(TAG,e.getMessage());
			say(getString(R.string.send_fail));
		}
	}

	public void playPhone(String phone) {
		try {
		     Intent callIntent = new Intent(Intent.ACTION_DIAL);
	          callIntent.setData(Uri.parse("tel:"+Uri.encode(phone.trim())));
	          callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	          startActivity(callIntent);  		
	          } catch (Exception e) {
	        	  Log.v(TAG,e.getMessage());
			say(getString(R.string.call_fail));
		}
	}

	public void go2Fragment(Fragment frag) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment previousFragment = ((MainActivity) getActivity()).previousFragment;
		if (previousFragment != null)
			ft.remove(previousFragment);
		ft.replace(R.id.form, frag).commit();
		previousFragment = frag;

	}

	public void areYouSure(final Runnable act) {
		AlertDialog ad= new AlertDialog.Builder(this.getActivity())
				.setTitle(getString(R.string.critical_operation))
				.setMessage(getString(R.string.are_you_sure))
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								act.run();
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).create();
		ad.setCancelable(false);
		ad.setCanceledOnTouchOutside(false);
		ad.show();

	}
	
	public String saySimple(Date date){
		return date.getDate() + "."	+ (date.getMonth() + 1) + "."
					+ (1900 + date.getYear());
	}



	
}
