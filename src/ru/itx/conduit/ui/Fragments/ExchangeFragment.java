package ru.itx.conduit.ui.Fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import ru.itx.conduit.Account;
import ru.itx.conduit.AccountModel;
import ru.itx.conduit.OpenHelper;
import ru.itx.conduit.ReplicaModel;
import ru.itx.conduit.DataHelper;
import ru.itx.conduit.Group;
import ru.itx.conduit.GroupList;
import ru.itx.conduit.R;
import ru.itx.conduit.RowAccountRecordModel;
import ru.itx.conduit.R.id;
import ru.itx.conduit.R.layout;
import ru.itx.conduit.Student;
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
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ExchangeFragment extends FragmentTools {
	View view;
	GroupFragment frag;
	List<File> files;

	private static final int EMAIL = 101;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.exchange, container, false);
		files=new ArrayList<File>();
		ImageButton replica_email_send = (ImageButton) view
				.findViewById(R.id.replica_email_send);
		replica_email_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText replica_email = (EditText) view
						.findViewById(R.id.replica_email);
				if (replica_email.getText().toString().equals(""))
					return;
				SendReplica sender = new SendReplica();
				sender.email = replica_email.getText().toString();
				sender.execute();
			}
		});
		ImageButton replica_to_file = (ImageButton) view
				.findViewById(R.id.replica_to_file);
		replica_to_file.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText replica_file = (EditText) view
						.findViewById(R.id.replica_file);
				if (replica_file.getText().toString().equals(""))
					replica_file.setText("Replica"
							+ new SimpleDateFormat("yyyyMMddhhmm'.xml'")
									.format(new Date()));
				SendReplica sender = new SendReplica();
				sender.fname = replica_file.getText().toString();
				sender.execute();
			}
		});
		ImageButton replica_from_file = (ImageButton) view
				.findViewById(R.id.replica_from_file);
		replica_from_file.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				areYouSure(new Runnable() {
					public void run() {
						EditText replica_file = (EditText) view
								.findViewById(R.id.replica_file);
						ReceivReplica receiver = new ReceivReplica();
						receiver.fname = replica_file.getText().toString();
						receiver.execute();
					}
				});

			}
		});
		ImageButton report_email_send = (ImageButton) view
				.findViewById(R.id.report_email_send);
		report_email_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText report_email = (EditText) view
						.findViewById(R.id.report_email);
				if (report_email.getText().toString().equals(""))
					return;
				SendReport sender = new SendReport();
				sender.email = report_email.getText().toString();
				sender.execute();
			}
		});
		return view;
	}

	private void sendEmail(String[] address) {
		Intent emailIntent = prepareEmailIntent(address);
		ArrayList<Uri> uris=new ArrayList<Uri>();
		try {
			for(File file:files){
				if (!file.exists() || !file.canRead()) {
					say(getString(R.string.email_attach_error));
				} else uris.add(Uri.fromFile(file));
			
			}
			if(uris.size()==0){
				say(getString(R.string.email_attach_error));
				return;
			}
			
			emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris);
			startActivityForResult(Intent.createChooser(emailIntent,
					getString(R.string.email_sending)), EMAIL);
		} catch (Exception e) {
			Log.v(TAG, e.getMessage());
			say(getString(R.string.send_fail));
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EMAIL && files!=null)
			for(File file:files)
				if (file != null && file.exists())
					file.delete();
	}

	class ReceivReplica extends AsyncTask<Void, Void, Void> {
		public String fname;

		@Override
		protected Void doInBackground(Void... params) {
			ReplicaModel dbreplica = new ReplicaModel();
			String root = Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DOWNLOADS).toString();
			if (fname == null || fname.equals("")) {
				say(getString(R.string.replica_file_not_specified));
				return null;
			}
			fname = root + "/Conduit/" + fname;
			receiveReplicaFromFile(fname);
			return null;
		}

	}

	public void receiveReplicaFromFile(String fname) {
		Serializer serializer = new Persister();
		File file = new File(fname);
		ReplicaModel dbreplica = null;
		try {
			dbreplica = serializer.read(ReplicaModel.class, file);
			if (dbreplica == null)
				throw new Exception();
		} catch (Exception e) {
			say(getString(R.string.replica_load_error));
			e.printStackTrace();
		}
		DataHelper.getDH().clearAll();
		DataHelper.getDH().replicaASecurity(dbreplica.getList_asecurity());
		if (dbreplica.getList_student() != null)
			DataHelper.getDH().replicaStudent(dbreplica.getList_student());
		if (dbreplica.getList_group() != null)
			DataHelper.getDH().replicaGroup(dbreplica.getList_group());
		if (dbreplica.getList_group_list() != null)
			DataHelper.getDH().replicaGroupList(dbreplica.getList_group_list());
		if (dbreplica.getList_account() != null)
			DataHelper.getDH().replicaAccount(dbreplica.getList_account());
		// say(getString(R.string.relogin_needed));
		ExchangeFragment.this.getActivity().finish();
	}

	class SendReplica extends AsyncTask<Void, Void, Void> {
		public String email;
		public String fname;

		@Override
		protected Void doInBackground(Void... params) {
			ReplicaModel dbreplica = new ReplicaModel();
			dbreplica.setSave_date(new Date());
			dbreplica.setList_asecurity(DataHelper.getDH().replicaASecurity());
			dbreplica.setList_student(DataHelper.getDH().replicaStudent());
			dbreplica.setList_group(DataHelper.getDH().replicaGroup());
			dbreplica.setList_group_list(DataHelper.getDH().replicaGroupList());
			dbreplica.setList_account(DataHelper.getDH().replicaAccount());
			String root = Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DOWNLOADS).toString();
			File myDir = new File(root + "/Conduit");
			myDir.mkdirs();
			if (fname == null) {
				fname = "Replica"
						+ new SimpleDateFormat("yyyyMMddhhmm'.xml'")
								.format(new Date());
			}
			File file = new File(myDir, fname);
			if (file.exists())
				file.delete();
			Serializer serializer = new Persister();
			try {
				serializer.write(dbreplica, file);
				files.add(file);
				if (email != null)
					sendEmail(new String[] { email });
			} catch (Exception e) {
				say(getString(R.string.replica_save_error));
				e.printStackTrace();
			}
			return null;
		}
	}

	class SendReport extends AsyncTask<Void, Void, Void> {
		public String email;
		public String fname;

		@Override
		protected Void doInBackground(Void... params) {
			//!! получить список групп
			List<Group> groups= DataHelper.getDH().selectAll_group();
			//!! для каждой группы делаем отчет.
			for(Group group:groups){
				File file=createReprt(group);
				if(file!=null)	//!! добавить файл в список
					files.add(file);			
			}
			if (email != null)
				sendEmail(new String[] { email });
			return null;
		}
	}

	private File createReprt(Group group) {
		FileOutputStream out;
		String fname = "Report"+group.getName()
				+ new SimpleDateFormat("yyyyMMddhhmm'.xls'")
						.format(new Date());
		String root = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOWNLOADS).toString();
		File myDir = new File(root + "/Conduit/");
		myDir.mkdirs();
		File file=new File(root + "/Conduit/"+fname);
		List<String>[] table=getAccountTable(group);
		if(table==null || table.length==0) return null;
		// create a new workbook
		Workbook wb = new HSSFWorkbook();
		// create a new sheet
		Sheet s = wb.createSheet();
		// declare a row object reference
		Row r = null;
		// declare a cell object reference
		Cell c = null;
		// create 3 cell styles
		CellStyle csLeftHeader = wb.createCellStyle();
		CellStyle csValue = wb.createCellStyle();
		CellStyle csTopHeader = wb.createCellStyle();
		DataFormat df = wb.createDataFormat();
		Font f = wb.createFont();
		f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		f.setFontHeightInPoints((short) 10);
		csLeftHeader.setFont(f);
		csLeftHeader.setBorderBottom(csValue.BORDER_THIN);
		csLeftHeader.setBorderTop(csValue.BORDER_THIN);
		csLeftHeader.setBorderLeft(csValue.BORDER_THIN);
		csLeftHeader.setBorderRight(csValue.BORDER_THIN);
		csLeftHeader.setDataFormat(df.getFormat("#"));
		Font f1 = wb.createFont();
		f1.setFontHeightInPoints((short) 10);
		csValue.setFont(f1);
		csValue.setBorderBottom(csValue.BORDER_THIN);
		csValue.setBorderTop(csValue.BORDER_THIN);
		csValue.setBorderLeft(csValue.BORDER_THIN);
		csValue.setBorderRight(csValue.BORDER_THIN);
		csValue.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		csValue.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		csValue.setDataFormat(df.getFormat("#"));
		Font f2 = wb.createFont();
		f2.setFontHeightInPoints((short) 10);
		csTopHeader.setFont(f2);
		csTopHeader.setBorderBottom(csValue.BORDER_THIN);
		csTopHeader.setBorderTop(csValue.BORDER_THIN);
		csTopHeader.setBorderLeft(csValue.BORDER_THIN);
		csTopHeader.setBorderRight(csValue.BORDER_THIN);
		csTopHeader.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
		csTopHeader.setRotation((short)90);
		wb.setSheetName(0, "Отчет от "+saySimple(new Date())+" по "+group.getName()+" ("+group.getTeacher()+")");
		int rownum;
		for (rownum = (short) 0; rownum < table.length; rownum++) {
			// create a row
			r = s.createRow(rownum);
			if(rownum==0) r.setHeight((short)2000);
			for (short cellnum = (short) 0; cellnum < table[0].size(); cellnum++) {
				// create a numeric cell
				c = r.createCell(cellnum);
				c.setCellValue(table[rownum].get(cellnum));
				if(rownum==0 && cellnum!=0){
					c.setCellStyle(csTopHeader);
				} else if (cellnum==0) {
					s.setColumnWidth(cellnum, 8000);
					c.setCellStyle(csLeftHeader);
					}
				else{
					s.setColumnWidth(cellnum, 1300);
					c.setCellStyle(csValue);
				}
			}
		}

		try {
			out=new FileOutputStream(file);
			wb.write(out);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	private List<String>[] getAccountTable(Group group){
		List<String> rez[]=null;
		List<AccountModel> list = DataHelper.getDH()
				.selectAll_Account(group);
		if (list == null)
			return rez;
		// выбираем всех учащихся
		List<Student> students = new ArrayList<Student>();
		for (AccountModel a : list)
			for (RowAccountRecordModel r : a.list)
				if (!students.contains(r.getStudent())
						&& !r.getStudent().isHidden())
					students.add(r.getStudent());
		rez=new List[students.size()+1];
		rez[0]=new ArrayList<String>();
		// строим таблицу
		// фомируем шапку таблицы: поле (0,0)
		rez[0].add(group.getName() + " ("+group.getTeacher()+")" );
		for(int i=0;i<list.size();i++){
			rez[0].add(saySimple(new Date(list.get(i).date)) + "\n"
					+ list.get(i).subject);
		}
		// строим левый хидер
		int j=0;
		for (Student s : students) {
			j++;
			rez[j]=new ArrayList<String>();
			rez[j].add(s.getName() + " " + (s.getLastName()==null?"":s.getLastName()) + " "
					+ s.getSurName());

			for (int i = 0; i < list.size(); i++) {
				String rez1 = "";
				for (RowAccountRecordModel r : list.get(i).list) {
					if (s.equals(r.getStudent())) {
						rez1 = r.getValue();
					}
				}
				rez[j].add(rez1);
			}
		}
		return rez;
	}

}