package th.co.thiensurat.data.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHPreference;
import th.co.thiensurat.data.info.TripInfo;

public class TripController extends BaseController {

	public List<TripInfo> getTrip() {
		final String QUERY_TRIP_GET_ALL = "SELECT * FROM Trip";
		return executeQueryList(QUERY_TRIP_GET_ALL, null, TripInfo.class);
	}

	public TripInfo getTrip(String tripID) {
		final String QUERY_TRIP_GET_BY_ID = "SELECT * FROM Trip WHERE TripID = ?";
		return executeQueryObject(QUERY_TRIP_GET_BY_ID, new String[]{tripID}, TripInfo.class);
	}

	public TripInfo getCurrentTrip() {
		final String QUERY_TRIP_GET_BY_CURRENT = "SELECT * FROM Trip WHERE DATE() BETWEEN DATE(StartDate) AND DATE(EndDate)";
		return executeQueryObject(QUERY_TRIP_GET_BY_CURRENT, null, TripInfo.class);
	}
	
	public TripInfo getTrip(Date date) {
		final String QUERY_TRIP_GET_BY_ID = "SELECT * FROM Trip WHERE DATE(?) BETWEEN DATE(StartDate) AND DATE(EndDate)";
		return executeQueryObject(QUERY_TRIP_GET_BY_ID, new String[] { valueOf(date) }, TripInfo.class);
	}

	public void addTrip(TripInfo info) {
		String sql = "insert into Trip([TripID],[OrganizationCode],[TripNumber],[Year],[StartDate],[EndDate])"
				+" values(?,?,?,?,?,?)";
		executeNonQuery(sql, new String[] {info.TripID, info.OrganizationCode, valueOf(info.TripNumber), valueOf(info.Year), valueOf(info.StartDate), valueOf(info.EndDate)});
	}

	/***
	 * [START] - Fixed - [Android-เก็บเงินค่างวด] เปลี่ยนเงื่อนไขการนับจำนวนงวดที่ค้าง
	 ***/
	public Date getDueDate(){
		TripInfo info = getCurrentTrip();
		if(info != null && info.StartDate != null) {
			return info.StartDate;
		}else{
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(BHPreference.OVER_DUE_KEY));
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.MONTH, -1);


			return calendar.getTime();
		}
	}
	/***
	 * [END] - Fixed - [Android-เก็บเงินค่างวด] เปลี่ยนเงื่อนไขการนับจำนวนงวดที่ค้าง
	 ***/

	public void deleteTrip() {
		executeNonQuery("delete from Trip", null);
	}
}
