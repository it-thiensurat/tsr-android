package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.CareerInfo;

public class CareerController extends BaseController {

	private static final String QUERY_CAREER_GET_ALL = "SELECT * FROM Career";

	public List<CareerInfo> getCareer() {
		return executeQueryList(QUERY_CAREER_GET_ALL, null, CareerInfo.class);
	}

    public CareerInfo getCareerByCareerCode (String CareerCode ) {
        CareerInfo ret = null;
        String sql = "SELECT * FROM Career where CareerCode = ?";
        ret = executeQueryObject(sql, new String[]{CareerCode}, CareerInfo.class);
        return ret;
    }

    public void addCareer(CareerInfo info) {
        String sql = "insert into Career(CareerCode,CareerName)"
                +" values(?,?)";
        executeNonQuery(sql, new String[] {info.CareerCode, info.CareerName});
    }

    public void deleteCareer() {
        executeNonQuery("delete from Career", null);
    }

}
