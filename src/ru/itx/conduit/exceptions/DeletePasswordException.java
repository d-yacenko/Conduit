package ru.itx.conduit.exceptions;

import android.content.Context;
import ru.itx.conduit.R;

public class DeletePasswordException extends ConduitException {

	private Context context;
	
	public DeletePasswordException(Context context) {
		this.context=context;
	}
	
	@Override
	public String getMessage() {
		return "Passwords is differed";
	}

	@Override
	public String getLocalizedMessage() {
		return context.getString(R.string.cannot_delete_one_password);
	}
	

}
