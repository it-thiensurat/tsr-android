package th.co.bighead.utilities.ThaiNationalIDCard;

public interface IAPDU_THAILAND_IDCARD {
    // Get response
    byte[] APDU_GET_RESPONSE(byte[] le);

    // MOI AID
    byte[] AID_MOI();

    // Select/Reset
    byte[] APDU_SELECT(byte[] AID);

    // Citizen ID
    byte[] CID();

    byte[] TH_Fullname();

    byte[] EN_Fullname();

    byte[] Date_Of_Birth();

    byte[] Gender();

    byte[] Card_Issuer();

    byte[] Issue_Date();

    byte[] Expire_Date();

    byte[] Address();

    byte[][] Photo();
}
