package ru.itx.conduit.exceptions;

import ru.itx.conduit.R;
import android.content.Context;

public class PasswordIsWrongException extends ConduitException {
	private Context context;
	
	public PasswordIsWrongException(Context context) {
		this.context=context;
	}
	
	@Override
	public String getMessage() {
		return "Passwords is differed";
	}

	@Override
	public String getLocalizedMessage() {
		return context.getString(R.string.password_is_wrong);
	}
	
}
