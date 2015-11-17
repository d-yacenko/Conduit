package ru.itx.conduit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import ru.itx.conduit.exceptions.DeletePasswordException;
import ru.itx.conduit.exceptions.PasswordIsDifferedException;
import ru.itx.conduit.exceptions.PasswordIsWrongException;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

@Root
public class ASecurity {
	
	public static final String TAG="EXCEPTION";
	@Attribute
	private long _id;
	@Element
	private String password;
	@Element
	private String dbPassword;
	@Element(required=false)
	private String replicaId;
	
	public ASecurity(@Attribute(name="_id") long _id,@Element(name="password") String password,
			@Element(name="dbPassword") String dbPassword,@Element(name="replicaId") String replicaId) {
		this._id=_id;
		this.password=password;
		this.dbPassword=dbPassword;
		this.replicaId=replicaId;
	}
	
	public ASecurity(String password) {
		this.password=password;
	}

	public SecretKey _decodeDBPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException{
		DESKeySpec dks = new DESKeySpec(password.getBytes());
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher decipher=null;
		try {
			decipher = Cipher.getInstance("DES");
			decipher.init(Cipher.DECRYPT_MODE, desKey);
		} catch (Exception e1) {
			Log.v(TAG,e1.getMessage());
		}
		byte [] dec=Base64.decode(dbPassword, Base64.DEFAULT);
		try {
			dec = decipher.doFinal(dec);
		} catch (Exception e) {
			Log.v(TAG,e.getMessage());
		}
		dks = new DESKeySpec(dec);
		skf = SecretKeyFactory.getInstance("DES");
		return skf.generateSecret(dks);
	}
	
	private static Cipher getCipher(SecretKey desKey){
		Cipher cipher=null;	
		try {
					cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.ENCRYPT_MODE, desKey);
			} catch (Exception e) {
				Log.v(TAG,e.getMessage());
			} 
		return cipher;
	}
	
	private static Cipher getDecipher(SecretKey desKey){
		Cipher decipher=null;
		try {
			decipher = Cipher.getInstance("DES");
			decipher.init(Cipher.DECRYPT_MODE, desKey);
		} catch (Exception e) {
			if(e.getMessage()!=null)
			Log.v(TAG,e.getMessage());
			else Log.v(TAG,"cipher or key problem");
		} 
	return decipher;
}

	public void _generateInitialDBPassword(String Phone_ID,byte[] password) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Random r=new Random(Phone_ID.hashCode()^System.currentTimeMillis());
		byte[] b=new byte[16];
		r.nextBytes(b);
		DESKeySpec dks = new DESKeySpec(password);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, desKey);
 	 	byte[] enc=new byte[0];
		enc = cipher.doFinal(b);
		dbPassword=Base64.encodeToString(enc, Base64.DEFAULT);
		replicaId="";
	}

	public static String _code(SecretKey SK,String str){
		if(str==null)return null;
		Cipher cipher = ASecurity.getCipher(SK);
		byte[] enc=new byte[0];
		try {
			enc = cipher.doFinal(str.getBytes());
		} catch (Exception e) {
			Log.v(TAG,e.getMessage());
		}
		return Base64.encodeToString(enc, Base64.DEFAULT).trim();
	}
	
	public static byte[] _code(SecretKey SK,byte[] str){
		if(str==null)return null;
		Cipher cipher = ASecurity.getCipher(SK);
 	 	byte[] enc=new byte[0];
		try {
			enc = cipher.doFinal(str);
		} catch (Exception e) {
			Log.v(TAG,e.getMessage());
		}
		return enc;
	}
	
	
	public static String _decode(SecretKey SK,String str){
		if(str==null)return null;
		byte [] dec=Base64.decode(str, Base64.DEFAULT);
		Cipher decipher = ASecurity.getDecipher(SK);
		try {
			dec = decipher.doFinal(dec);
		} catch (Exception e) {
			Log.v(TAG,e.getMessage());
		}
		return new String(dec).trim();
	}
	
	public static byte [] _decode(SecretKey SK,byte [] str){
		if(str==null)return null;
		byte [] dec=null;
		Cipher decipher = ASecurity.getDecipher(SK);
		try {
			dec = decipher.doFinal(str);
		} catch (Exception e) {
			Log.v(TAG,e.getMessage());
		}
		return dec;
	}
	
	public static String _getUniqueID(Activity activity){
		final TelephonyManager tm = (TelephonyManager) activity.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    String deviceId = deviceUuid.toString();
	    return deviceId;
	}
	
	public static void _deletePassword(Context context, String password1,String Password2) throws PasswordIsDifferedException, DeletePasswordException {
		if(!password1.equals(Password2))
			throw new PasswordIsDifferedException(context);
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
        	  Log.v(TAG,e.getMessage());
		}
		String encoded = Base64.encodeToString(md.digest(password1.getBytes()),Base64.DEFAULT);
		List<ASecurity> list=DataHelper.getDH().selectAll_security();
		if(list.size()<2)
			throw new DeletePasswordException(context);
		DataHelper.getDH().delete(new ASecurity(encoded));

	} 
	

	public static void _addPassword(Context context, String actualPassword,String password1,String Password2) throws PasswordIsDifferedException, PasswordIsWrongException {
		if(!password1.equals(Password2))
			throw new PasswordIsDifferedException(context);
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
        	  Log.v(TAG,e.getMessage());
		}
		String encoded = Base64.encodeToString(md.digest(actualPassword.getBytes()),Base64.DEFAULT);
		ASecurity as=DataHelper.getDH().selectAll_security(encoded);
		if(as==null) throw new PasswordIsWrongException(context);
		encoded = Base64.encodeToString(md.digest(password1.getBytes()),Base64.DEFAULT);
		ASecurity newAS=new ASecurity(encoded);
		newAS.setReplicaId(as.getReplicaId());
		//перешифровать DbPass
		Cipher decipher=null;
		DESKeySpec dks;
		SecretKeyFactory skf;
		SecretKey desKey=null;
		try {
			dks = new DESKeySpec(actualPassword.getBytes());
			skf = SecretKeyFactory.getInstance("DES");
			desKey = skf.generateSecret(dks);
			decipher = Cipher.getInstance("DES");
			decipher.init(Cipher.DECRYPT_MODE, desKey);
		} catch (Exception e1) {
			Log.v(TAG,e1.getMessage());
		}
		byte [] dec=Base64.decode(as.getDbPassword(), Base64.DEFAULT);
		try {
			dec = decipher.doFinal(dec);
			dks = new DESKeySpec(password1.getBytes());
			skf = SecretKeyFactory.getInstance("DES");
			desKey = skf.generateSecret(dks);
		} catch (Exception e) {
			Log.v(TAG,e.getMessage());
		}
		Cipher cipher=null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
		} catch (Exception e1) {
			Log.v(TAG,e1.getMessage());
		}
		byte[] enc=null;
		try {
			enc = cipher.doFinal(dec);
		} catch (Exception e) {
			Log.v(TAG,e.getMessage());
		}
		enc=Base64.encode(enc, Base64.DEFAULT);
		newAS.setDbPassword(new String(enc));
		DataHelper.getDH().insert(newAS);

	} 
	
	public static void _changePassword(Context context, String actualPassword,String password1,String Password2) throws PasswordIsDifferedException, PasswordIsWrongException {
		if(!password1.equals(Password2))
			throw new PasswordIsDifferedException(context);
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
        	  Log.v(TAG,e.getMessage());
		}
		String encoded = Base64.encodeToString(md.digest(actualPassword.getBytes()),Base64.DEFAULT);
		ASecurity as=DataHelper.getDH().selectAll_security(encoded);
		if(as==null) throw new PasswordIsWrongException(context);
		encoded = Base64.encodeToString(md.digest(password1.getBytes()),Base64.DEFAULT);
		ASecurity newAS=new ASecurity(encoded);
		newAS.setReplicaId(as.getReplicaId());
		//перешифровать DbPass
		Cipher decipher=null;
		DESKeySpec dks;
		SecretKeyFactory skf;
		SecretKey desKey=null;
		try {
			dks = new DESKeySpec(actualPassword.getBytes());
			skf = SecretKeyFactory.getInstance("DES");
			desKey = skf.generateSecret(dks);
			decipher = Cipher.getInstance("DES");
			decipher.init(Cipher.DECRYPT_MODE, desKey);
		} catch (Exception e1) {
			Log.v(TAG,e1.getMessage());
		}
		byte [] dec=Base64.decode(as.getDbPassword(), Base64.DEFAULT);
		try {
			dec = decipher.doFinal(dec);
			dks = new DESKeySpec(password1.getBytes());
			skf = SecretKeyFactory.getInstance("DES");
			desKey = skf.generateSecret(dks);
		} catch (Exception e) {
			Log.v(TAG,e.getMessage());
		}
		Cipher cipher=null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
		} catch (Exception e1) {
			Log.v(TAG,e1.getMessage());
		}
		byte[] enc=null;
		try {
			enc = cipher.doFinal(dec);
		} catch (Exception e) {
			Log.v(TAG,e.getMessage());
		}
		enc=Base64.encode(enc, Base64.DEFAULT);
		newAS.setDbPassword(new String(enc));
		DataHelper.getDH().update(as.get_id(),newAS);

	} 
	
	public static int _getNumberSecurity(){
		return DataHelper.getDH().selectAll_security().size();
	}
	
	public static void _setReplica(String replica_id){
		DataHelper.getDH().setReplica(replica_id);
	}
	
	public static String _getReplicaId() {
		List<ASecurity> list=DataHelper.getDH().selectAll_security();
		if (list.size()==0) return null;
		return DataHelper.getDH().selectAll_security().get(0).getReplicaId();
	}
	
	public long get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getReplicaId() {
		return replicaId;
	}

	public void setReplicaId(String replicaId) {
		this.replicaId = replicaId;
	}

}
