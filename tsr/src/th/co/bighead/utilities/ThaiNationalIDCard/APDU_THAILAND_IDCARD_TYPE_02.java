package th.co.bighead.utilities.ThaiNationalIDCard;

public class APDU_THAILAND_IDCARD_TYPE_02 extends APDU_THAILAND_IDCARD {
    @Override
    public byte[] APDU_GET_RESPONSE(byte[] le) {
        byte[] getResponse = new byte[] { (byte)0x00, (byte)0xc0, (byte)0x00, (byte)0x00 };

        return ConcatenateTwoByteArrays(getResponse, le);
    }
}
