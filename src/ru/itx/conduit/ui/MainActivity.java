package ru.itx.conduit.ui;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.crypto.NoSuchPaddingException;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import ru.itx.conduit.ASecurity;
import ru.itx.conduit.Account;
import ru.itx.conduit.ReplicaModel;
import ru.itx.conduit.DataHelper;
import ru.itx.conduit.GroupList;
import ru.itx.conduit.R;
import ru.itx.conduit.R.drawable;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import ru.itx.conduit.R.string;
import ru.itx.conduit.ui.Fragments.AccountFragment;
import ru.itx.conduit.ui.Fragments.AddGroupFragment;
import ru.itx.conduit.ui.Fragments.AddStudentFragment;
import ru.itx.conduit.ui.Fragments.EditGroupFragment;
import ru.itx.conduit.ui.Fragments.EditStudentFragment;
import ru.itx.conduit.ui.Fragments.ExchangeFragment;
import ru.itx.conduit.ui.Fragments.GroupFragment;
import ru.itx.conduit.ui.Fragments.HelpFragment;
import ru.itx.conduit.ui.Fragments.SecurityFragment;
import ru.itx.conduit.ui.Fragments.StudentFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	public AccountFragment accountFragment;
	public StudentFragment studentFragment;
	public GroupFragment groupFragment;
	public AddStudentFragment addStudentFragment;
	public EditStudentFragment editStudentFragment;
	public AddGroupFragment addGroupFragment;
	public EditGroupFragment editGroupFragment;
	public HelpFragment helpFragment;
	public SecurityFragment securityFragment;
	public ExchangeFragment exchangeFragment;
	public Fragment previousFragment;
	private ASecurity security;
	private DataHelper dh;

	public static Context CONTEXT;
	public static final String TAG = "EXCEPTION";

	public DataHelper getDH() {
		return dh;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(CONTEXT==null) CONTEXT=this;
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			dh = getDataHelper();
			accountFragment = new AccountFragment();
			studentFragment = new StudentFragment();
			groupFragment = new GroupFragment();
			helpFragment = new HelpFragment();
			securityFragment = new SecurityFragment();
			exchangeFragment = new ExchangeFragment();
		} else {
			HashMap<String, Object> savedValues = (HashMap<String, Object>) this
					.getLastNonConfigurationInstance();
			dh = (DataHelper) savedValues.get("dh");
			accountFragment = (AccountFragment) savedValues
					.get("accountFragment");
			studentFragment = (StudentFragment) savedValues
					.get("studentFragment");
			addStudentFragment = (AddStudentFragment) savedValues
					.get("addStudentFragment");
			editStudentFragment = (EditStudentFragment) savedValues
					.get("editStudentFragment");
			addGroupFragment = (AddGroupFragment) savedValues
					.get("addGroupFragment");
			editGroupFragment = (EditGroupFragment) savedValues
					.get("editGroupFragment");
		}
	}

	public void toAccount(View V) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if (previousFragment != null)
			ft.remove(previousFragment);
		ft.replace(R.id.form, accountFragment).commit();
		previousFragment = accountFragment;
	}

	public void toGroup(View V) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if (previousFragment != null)
			ft.remove(previousFragment);
		ft.replace(R.id.form, groupFragment).commit();
		previousFragment = groupFragment;
	}

	public void toStudent(View V) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if (previousFragment != null)
			ft.remove(previousFragment);
		ft.replace(R.id.form, studentFragment).commit();
		previousFragment = studentFragment;
	}

	public void toSecurity(View V) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if (previousFragment != null)
			ft.remove(previousFragment);
		ft.replace(R.id.form, securityFragment).commit();
		previousFragment = securityFragment;
	}
	public void toExchange(View V) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if (previousFragment != null)
			ft.remove(previousFragment);
		ft.replace(R.id.form, exchangeFragment).commit();
		previousFragment = exchangeFragment;
	}

	public void toHelp(View V) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if (previousFragment != null)
			ft.remove(previousFragment);
		ft.replace(R.id.form, helpFragment).commit();
		previousFragment = helpFragment;
	}

	public void toExit(View V) {
		finish();

	}

	private DataHelper getDataHelper() {
		return new DataHelper(this);
	}

	private boolean login() {
		boolean rez = false;
		List<ASecurity> list = getDH().selectAll_security();
		if (list.size() == 0) {
			loginFirst(new Runnable() {
				@Override
				public void run() {
					// редактировать в базу
					getDH().insert(security);
					say(getString(R.string.relogin_needed));
					finish();
				}
			});
			rez = true;
		} else {
			loginStandart(new Runnable() {
				@Override
				public void run() {
					String s = security.getDbPassword();
					security = getDH().selectAll_security(
							security.getPassword());
					if (security == null) {
						finish();
						return;
					}
					try {
						getDH().setDBP(security._decodeDBPassword(s));
					} catch (Exception e) {
						Log.v(TAG, e.getMessage() == null ? "" : e.getMessage());
					}
					s = null;
				}
			});
			rez = true;
		}
		return rez;
	}

	private void loginStandart(final Runnable act) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View inputPasswordDialogView = factory.inflate(
				R.layout.input_password, null);
		final EditText input = (EditText) inputPasswordDialogView
				.findViewById(R.id.password);
		AlertDialog ad = new AlertDialog.Builder(this)
				.setTitle(getString(R.string.login))
				.setMessage(getString(R.string.login_to_program))
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								byte[] bytesOfMessage = input.getText()
										.toString().getBytes();
								MessageDigest md = null;
								try {
									md = MessageDigest.getInstance("MD5");
								} catch (NoSuchAlgorithmException e) {
									Log.v(TAG, e.getMessage());
								}
								String encoded = Base64.encodeToString(
										md.digest(bytesOfMessage),
										Base64.DEFAULT);
								security = new ASecurity(encoded);
								security.setDbPassword(input.getText()
										.toString());
								act.run();
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						}).setIcon(R.drawable.ic_key_variant_black_18dp)
						
				.create();
		ad.setCancelable(false);
		ad.setCanceledOnTouchOutside(false);
		ad.setView(inputPasswordDialogView);
		ad.show();
	}

	private void loginFirst(final Runnable act) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View setPasswordDialogView = factory.inflate(
				R.layout.set_password, null);
		final EditText input1 = (EditText) setPasswordDialogView
				.findViewById(R.id.password1);
		final EditText input2 = (EditText) setPasswordDialogView
				.findViewById(R.id.password2);
		;
		AlertDialog ad = new AlertDialog.Builder(this)
				.setTitle(getString(R.string.login_first))
				.setMessage(getString(R.string.enter_password))
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (!input1.getText().toString()
										.equals(input2.getText().toString())) {
									say(getString(R.string.differed_password));
									finish();
									return;
								}
								byte[] bytesOfMessage = input1.getText()
										.toString().getBytes();
								MessageDigest md = null;
								try {
									md = MessageDigest.getInstance("MD5");
								} catch (NoSuchAlgorithmException e) {
									Log.v(TAG, e.getMessage());
								}
								String encoded = Base64.encodeToString(
										md.digest(bytesOfMessage),
										Base64.DEFAULT);
								security = new ASecurity(encoded);
								try {
									security._generateInitialDBPassword(
											ASecurity
													._getUniqueID(MainActivity.this),
											bytesOfMessage);
								} catch (InvalidKeyException e1) {
									Log.v(TAG, e1.getMessage());
									say(getString(R.string.error_key));
									finish();
									return;
								} catch (Exception e) {
									Log.v(TAG, e.getMessage());
								}

								act.run();
							}

						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						}).setIcon(R.drawable.ic_key_change_black_18dp)
				.create();
		ad.setCancelable(false);
		ad.setCanceledOnTouchOutside(false);
		ad.setView(setPasswordDialogView);
		ad.show();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		HashMap<String, Object> savedValues = new HashMap<String, Object>();
		savedValues.put("dh", dh);
		savedValues.put("accountFragment", accountFragment);
		savedValues.put("studentFragment", studentFragment);
		savedValues.put("addStudentFragment", addStudentFragment);
		savedValues.put("editStudentFragment", editStudentFragment);
		savedValues.put("addGroupFragment", addGroupFragment);
		savedValues.put("editGroupFragment", editGroupFragment);
		return savedValues;
	}

	@Override
	protected void onDestroy() {
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}

	private void say(String string) {
		Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
	}

@Override
protected void onResume() {
	login();
	super.onResume();
}	
	
	
	

}
