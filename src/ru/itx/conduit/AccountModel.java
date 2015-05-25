package ru.itx.conduit;

import java.util.List;

public class AccountModel {
		public List<RowAccountRecordModel> list;
		public long date;
		public String subject;
		public AccountModel(String subject,long date, List<RowAccountRecordModel> list) {
			this.subject=subject;
			this.date=date;
			this.list=list;
		}
		public static int _getNumberLesson(){
			return DataHelper.getDH().count_AccountLesson();
		}
}
