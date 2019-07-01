package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.TaskInfo;

public class TaskController extends BaseController {

	public void addTask(TaskInfo info) {
		String sql = "insert into Task(TaskCode,OrganizationCode,TaskName)"
				+" values(?,?,?)";
		executeNonQuery(sql, new String[] {info.TaskCode, info.OrganizationCode, info.TaskName});
	}
	
	public void deleteTask() {
		executeNonQuery("delete from Task", null);
	}
}
