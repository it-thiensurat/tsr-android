package th.co.bighead.utilities.ThaiNationalIDCard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

public class Personal {
    private final int lengthName = 4;
    private final int lengthAddress = 8;

    private String _CID;//เลขบัตรประชาชน
    private String _TH_Fullname;//ชื่อ th
    private String _EN_Fullname;//ชื่อ en
    private String _Date_Of_Birth;//วันเกิด
    private String _Gender;//เพศ
    private String _Card_Issuer;//สถานที่ออกบัตร
    private String _Issue_Date;//วันออกบัตร
    private String _Expire_Date;//วันบัตรหมดอายุ
    private String _Address;//ที่อยู่

    private byte[] _Photo;//รูปบัตร

    private String getValueByIndex(String fullString, int maxLength, int index) {
        String[] strArray = this.getStringSplit(fullString);
        if (strArray != null && strArray.length == maxLength && index < maxLength) {
            String str = strArray[index].trim();
            if (!str.isEmpty()) {
                return str;
            }
        }
        return null;
    }

    private String[] getStringSplit(String str){
        if (str != null) {
            return  str.split("#");
        }

        return null;
    }

    private Date getDate(String str) {
        if (str != null && str.length() == 8) {
            //yyyyMMdd พ.ศ.
            try{
                return new GregorianCalendar(
                        Integer.parseInt(str.substring(0, 4)) - 543,
                        Integer.parseInt(str.substring(4, 6)) - 1,
                        Integer.parseInt(str.substring(6, 8))
                ).getTime();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    //region CID
    public void setCID(String value) {
        this._CID = value.trim();
    }
    public String getIDCard() {
        return this._CID;
    }
    //endregion

    //region TH Fullname
    public void setTHFullname(String value) {
        if (value != null) {
            this._TH_Fullname = value.trim();
        }
    }
    public String getTHPrefixAndFullname() {
        String prefixrefix = this.getTHPrefix();
        String firstname = this.getTHFirstname();
        String middlename = this.getTHMiddlename();
        String lastname = this.getTHLastname();

        if (prefixrefix != null && firstname != null && middlename != null && lastname != null) {
            return String.format("%s %s %s %s", prefixrefix, firstname, middlename, lastname);
        } else if (prefixrefix != null && firstname != null && lastname != null) {
            return String.format("%s %s %s", prefixrefix, firstname, lastname);
        }

        return null;
    }

    public String getTHPrefixAndFullname2() {
        String prefixrefix = this.getTHPrefix();

        if (prefixrefix != null ) {
            return String.format("%s", prefixrefix);
        } else if (prefixrefix != null ) {
            return String.format("%s", prefixrefix);
        }

        return null;
    }
    public String getTHFullname() {
        String firstname = this.getTHFirstname();
        String middlename = this.getTHMiddlename();
        String lastname = this.getTHLastname();

        if (firstname != null && middlename != null && lastname != null) {
            return String.format("%s %s %s", firstname, middlename, lastname);
        } else if (firstname != null && lastname != null) {
            return String.format("%s %s", firstname, lastname);
        }

        return null;
    }
    public String getTHPrefix()
    {
        return this.getValueByIndex(this._TH_Fullname, this.lengthName, 0);
    }
    public String getTHFirstname()
    {
        return this.getValueByIndex(this._TH_Fullname, this.lengthName, 1);
    }
    public String getTHMiddlename()
    {
        return this.getValueByIndex(this._TH_Fullname, this.lengthName, 2);
    }
    public String getTHLastname()
    {
        return this.getValueByIndex(this._TH_Fullname, this.lengthName, 3);
    }
    //endregion

    //region EN Fullname
    public void setENFullname(String value) {
        if (value != null) {
            this._EN_Fullname = value.trim();
        }
    }
    public String getENPrefixAndFullname() {
        String prefixrefix = this.getENPrefix();
        String firstname = this.getENFirstname();
        String middlename = this.getENMiddlename();
        String lastname = this.getENLastname();

        if (prefixrefix != null && firstname != null && middlename != null && lastname != null) {
            return String.format("%s %s %s %s", prefixrefix, firstname, middlename, lastname);
        } else if (prefixrefix != null && firstname != null && lastname != null) {
            return String.format("%s %s %s", prefixrefix, firstname, lastname);
        }

        return null;
    }
    public String getENFullname() {
        String firstname = this.getENFirstname();
        String middlename = this.getENMiddlename();
        String lastname = this.getENLastname();

        if (firstname != null && middlename != null && lastname != null) {
            return String.format("%s %s %s", firstname, middlename, lastname);
        } else if (firstname != null && lastname != null) {
            return String.format("%s %s", firstname, lastname);
        }

        return null;
    }
    public String getENPrefix()
    {
        return this.getValueByIndex(this._EN_Fullname, this.lengthName, 0);
    }
    public String getENFirstname()
    {
        return this.getValueByIndex(this._EN_Fullname, this.lengthName, 1);
    }
    public String getENMiddlename()
    {
        return this.getValueByIndex(this._EN_Fullname, this.lengthName, 2);
    }
    public String getENLastname()
    {
        return this.getValueByIndex(this._EN_Fullname, this.lengthName, 3);
    }
    //endregion

    //region Date Of Birth
    public void setDateOfBirth(String value) {
        if (value != null) {
            this._Date_Of_Birth = value.trim();
        }
    }
    public Date getDateOfBirth() {
        return this.getDate(this._Date_Of_Birth);
    }
    //endregion

    //region Gender
    public void setGender(String value) {
        if (value != null) {
            this._Gender = value.trim();
        }
    }
    public String getGender() {
        //1 = ชาย 2 = หญิง
        if (this._Gender != null) {
            if ( this._Gender.equals("1")) {
                return "ชาย";
            } else  if ( this._Gender.equals("2")) {
                return "หญิง";
            }
        }

        return null;
    }
    //endregion

    //region Card Issuer
    public void setCardIssuer(String value) {
        if (value != null) {
            this._Card_Issuer = value.trim();
        }
    }
    public String getCardIssuer() {
        return this._Card_Issuer;
    }
    //endregion

    //region Issue Date
    public void setIssueDate(String value) {
        if (value != null) {
            this._Issue_Date = value.trim();
        }
    }
    public Date getIssueDate() {
        return this.getDate(this._Issue_Date);
    }
    //endregion

    //region Expire Date
    public void setExpireDate(String value) {
        if (value != null) {
            this._Expire_Date = value.trim();
        }
    }
    public Date getExpireDate() {
        return this.getDate(this._Expire_Date);
    }
    //endregion

    //region Address
    public void setAddress(String value) {
        if (value != null) {
            this._Address = value.trim();
        }
    }
    public String getFullAddres() {
        String houseNo = this.getAddresHouseNo();//บ้านเลขที่
        String villageNo = this.getAddresVillageNo();//หมู่ที่
        String lane = this.getAddresLane();//ตรอก
        String soi = this.getAddresSoi();//ซอย
        String road = this.getAAddresRoad();//ถนน
        String tambol = this.getAddresTambol();//แขวง/ตำบล
        String amphur = this.getAddresAmphur();//เขต/อำเภอ
        String province = this.getAddresProvince();//จังหวัด

        String fullAddres = "";
        if (houseNo != null) {
            fullAddres += String.format(" %s", houseNo);
        }
        if (villageNo != null) {
            fullAddres += String.format(" %s", villageNo);
        }
        if (lane != null) {
            fullAddres += String.format(" %s", lane);
        }
        if (soi != null) {
            fullAddres += String.format(" %s", soi);
        }
        if (road != null) {
            fullAddres += String.format(" %s", road);
        }
        if (tambol != null) {
            fullAddres += String.format(" %s", tambol);
        }
        if (amphur != null) {
            fullAddres += String.format(" %s", amphur);
        }
        if (province != null) {
            fullAddres += String.format(" %s", province);
        }



        return fullAddres.trim();
    }

    //บ้านเลขที่
    public String getAddresHouseNo()
    {
        return this.getValueByIndex(this._Address, this.lengthAddress, 0);
    }

    //หมู่ที่
    public String getAddresVillageNo()
    {
        return this.getValueByIndex(this._Address, this.lengthAddress, 1);
    }

    //ตรอก
    public String getAddresLane()
    {
        return this.getValueByIndex(this._Address, this.lengthAddress, 2);
    }

    //ซอย
    public String getAddresSoi()
    {
        return this.getValueByIndex(this._Address, this.lengthAddress, 3);
    }

    //ถนน
    public String getAAddresRoad()
    {
        return this.getValueByIndex(this._Address, this.lengthAddress, 4);
    }

    //ตำบล/แขวง
    public String getAddresTambol()
    {
        return this.getValueByIndex(this._Address, this.lengthAddress, 5);
    }

    //อำเภอ/เขต
    public String getAddresAmphur()
    {
        return this.getValueByIndex(this._Address, this.lengthAddress, 6);
    }

    //จังหวัด
    public String getAddresProvince()
    {
        return this.getValueByIndex(this._Address, this.lengthAddress, 7);
    }
    //endregion

    //region Photo
    public void setPhotoRaw(byte[] value)
    {
        this._Photo = value;
    }

    public Bitmap PhotoBitmap()
    {
        if (this._Photo == null) {
            return null;
        }


        int index = this._Photo.length;
        //while (index >= 0 && this._Photo[index - 1] == 0x20) index--;

        return BitmapFactory.decodeByteArray(this._Photo, 0, index);

    }
    //endregion


}
