package th.co.thiensurat.data.controller;
 
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.AuthenticateInputInfo;
import th.co.thiensurat.service.data.AuthenticateOutputInfo;

public class UserController extends BaseController {

	public enum LoginType {
		ALLOW, NOT_ALLOW
	}

	public AuthenticateOutputInfo logout(AuthenticateInputInfo info) {
		return TSRService.Logout(info, false);
	}
}
