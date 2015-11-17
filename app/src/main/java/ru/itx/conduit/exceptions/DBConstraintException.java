package ru.itx.conduit.exceptions;

import ru.itx.conduit.R;
import android.content.Context;

public class DBConstraintException extends ConduitException {

	private Context context;
	
	public DBConstraintException(Context context) {
		this.context=context;
	}
	
	@Override
	public String getMessage() {
		return "DB constraint error";
	}

	@Override
	public String getLocalizedMessage() {
		return context.getString(R.string.database_constraint_error);
	}
	
}
