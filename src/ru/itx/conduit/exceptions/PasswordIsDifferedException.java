package ru.itx.conduit.exceptions;

import android.content.Context;
import ru.itx.conduit.R;

public class PasswordIsDifferedException extends ConduitException {

	private Context context;
	
	public PasswordIsDifferedException(Context context) {
		this.context=context;
	}
	
	@Override
	public String getMessage() {
		return "Passwords is differed";
	}

	@Override
	public String getLocalizedMessage() {
		return context.getString(R.string.differed_password);
	}
	

}
