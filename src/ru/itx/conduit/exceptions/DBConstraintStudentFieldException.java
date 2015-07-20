package ru.itx.conduit.exceptions;

import ru.itx.conduit.R;
import android.content.Context;

public class DBConstraintStudentFieldException extends DBConstraintException {
	private Context context;
	
	public DBConstraintStudentFieldException(Context context) {
		super(context);
		this.context=context;
	}
	
	@Override
	public String getMessage() {
		return "DB constraint error - set all required fields";
	}

	@Override
	public String getLocalizedMessage() {
		return context.getString(R.string.students_required_fields_error);
	}
}
