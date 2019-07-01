package th.co.thiensurat.data.controller;

import java.util.Date;
import java.util.List;

import th.co.thiensurat.data.info.SqlFileName;
import th.co.thiensurat.data.info.TransactionLogInfo;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TransactionLogController extends BaseController {

	public void addTransactionLog(TransactionLogInfo info) {
		executeNonQuery(SqlFileName.transaction_log_insert, new String[] { info.ServiceName, info.ServiceInputName, info.ServiceInputType, info.ServiceOutputType,
				info.ServiceInputData, valueOf(info.SyncStatus) });
	}
	
	public List<TransactionLogInfo> getUnSyncTransactionLogs() {
		return executeQueryList(SqlFileName.transaction_log_get_unsync, new String[] { valueOf(false) }, TransactionLogInfo.class);
	}
	
	public void updateSyncTransactionLog(TransactionLogInfo tran) {
		executeNonQuery(SqlFileName.transaction_log_update_sync, new String[] { valueOf(tran.SyncStatus), valueOf(tran.SyncDate), valueOf(tran.TransactionID) });
	}

	public void updateSyncTransactionLog(int transactionID) {
		executeNonQuery(SqlFileName.transaction_log_update_sync, new String[] { valueOf(true), valueOf(new Date()), valueOf(transactionID) });
	}

    public List<TransactionLogInfo> getTransactionLogBySyncStatus(boolean SyncStatus) {
		List<TransactionLogInfo> ret = null;
		String sql = "SELECT * FROM TransactionLog where SyncStatus = ? ORDER BY TransactionID ASC";
		ret = executeQueryList(sql, new String[] {valueOf(SyncStatus)}, TransactionLogInfo.class);
		return ret;
	}

	public List<TransactionLogInfo> getAllTransactionLog() {
		List<TransactionLogInfo> ret = null;
		String sql = "SELECT * FROM TransactionLog ORDER BY TransactionID ASC";
		ret = executeQueryList(sql, new String[] {}, TransactionLogInfo.class);
		return ret;
	}

	
	public void test() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
				String sql = "INSERT INTO Test1(value) VALUES(datetime('now'))";
				database.beginTransaction();
//				DatabaseManager.getInstance().beginTransaction();
				for (int ii = 0; ii < 100; ii++) {
					executeNonQuery(sql, null);
					Log.d("SQLite", String.format("Test1 %d", ii));
//					try {
//						Thread.sleep(50);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
				database.setTransactionSuccessful();
				database.endTransaction();
				DatabaseManager.getInstance().closeDatabase();
//				DatabaseManager.getInstance().commitTransaction();
				Log.d("SQLite", "Test1 Success");
			}
		}).start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
				String sql = "INSERT INTO Test2(value) VALUES(datetime('now'))";
				database.beginTransaction();
//				DatabaseManager.getInstance().beginTransaction();
				for (int ii = 0; ii < 100; ii++) {
					executeNonQuery(sql, null);
					Log.d("SQLite", String.format("Test2 %d", ii));
//					try {
//						Thread.sleep(50);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
//				DatabaseManager.getInstance().rollbackTransaction();
				database.setTransactionSuccessful();
				database.endTransaction();
				DatabaseManager.getInstance().closeDatabase();
				Log.d("SQLite", "Test2 Success");
			}
		}).start();

	}
}
