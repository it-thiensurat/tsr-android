package th.co.bighead.utilities.ThaiNationalIDCard;

public abstract class APDU_THAILAND_IDCARD implements IAPDU_THAILAND_IDCARD {

    // MOI AID
    @Override
    public byte[] AID_MOI() {
        return new byte[] { (byte)0xA0, (byte)0X00, (byte)0x00, (byte)0x00, (byte)0x54, (byte)0x48, (byte)0x00, (byte)0x01 };
    }

    // Select/Reset
    @Override
    public byte[] APDU_SELECT(byte[] AID) {
        byte[] _select = new byte[] { (byte)0x00, (byte)0xA4, (byte)0x04, (byte)0x00, (byte)AID.length };
        byte[] _tmp = ConcatenateTwoByteArrays(_select,  AID);
        return _tmp;
    }

    //Citizen ID
    @Override
    public byte[] CID() {
        return new byte[] { (byte)0x80, (byte)0xb0, (byte)0x00, (byte)0x04, (byte)0x02, (byte)0x00, (byte)0x0d };
    }

    @Override
    public byte[] TH_Fullname() {
        return new byte[] { (byte)0x80, (byte)0xb0, (byte)0x00, (byte)0x11, (byte)0x02, (byte)0x00, (byte)0x64 };
    }

    @Override
    public byte[] EN_Fullname() {
        return new byte[] { (byte)0x80, (byte)0xb0, (byte)0x00, (byte)0x75, (byte)0x02, (byte)0x00, (byte)0x64 };
    }

    @Override
    public byte[] Date_Of_Birth() {
        return new byte[] { (byte)0x80, (byte)0xb0, (byte)0x00, (byte)0xd9, (byte)0x02, (byte)0x00, (byte)0x08 };
    }

    @Override
    public byte[] Gender() {
        return new byte[] { (byte)0x80, (byte)0xb0, (byte)0x00, (byte)0xe1, (byte)0x02, (byte)0x00, (byte)0x01 };
    }

    // issue/expire
    @Override
    public byte[] Card_Issuer() {
        return new byte[] { (byte)0x80, (byte)0xb0, (byte)0x00, (byte)0xf6, (byte)0x02, (byte)0x00, (byte)0x64 };
    }

    @Override
    public byte[] Issue_Date() {
        return new byte[] { (byte)0x80, (byte)0xb0, (byte)0x01, (byte)0x67, (byte)0x02, (byte)0x00, (byte)0x08 };
    }

    @Override
    public byte[] Expire_Date() {
        return new byte[] { (byte)0x80, (byte)0xb0, (byte)0x01, (byte)0x6f, (byte)0x02, (byte)0x00, (byte)0x08 };
    }

    @Override
    public byte[] Address() {
        return new byte[] { (byte)0x80, (byte)0xb0, (byte)0x15, (byte)0x79, (byte)0x02, (byte)0x00, (byte)0x64 };
    }

    @Override
    public byte[][] Photo() {
        return new byte[][]
                {
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x01, (byte)0x7B, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x02, (byte)0x7A, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x03, (byte)0x79, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x04, (byte)0x78, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x05, (byte)0x77, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x06, (byte)0x76, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x07, (byte)0x75, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x08, (byte)0x74, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x09, (byte)0x73, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x0A, (byte)0x72, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x0B, (byte)0x71, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x0C, (byte)0x70, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x0D, (byte)0x6F, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x0E, (byte)0x6E, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x0F, (byte)0x6D, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x10, (byte)0x6C, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x11, (byte)0x6B, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x12, (byte)0x6A, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x13, (byte)0x69, (byte)0x02, (byte)0x00, (byte)0xFF },
                        new byte[]{ (byte)0x80, (byte)0xB0, (byte)0x14, (byte)0x68, (byte)0x02, (byte)0x00, (byte)0xFF }
                };
    }

    public byte[] ConcatenateTwoByteArrays(byte[] a,  byte[] b) {

        byte[] bytes = new byte[a.length + b.length];
        for (int i = 0; i < a.length; i++) {
            bytes[i] = a[i];
        }

        int offsetA = a.length;
        for (int i = offsetA; i < b.length + offsetA; i++) {
            bytes[i] = b[i - offsetA];
        }

        return bytes;
    }


}
