package th.co.thiensurat.data.info;

import android.util.Log;

import java.io.Serializable;
import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.DistrictController;
import th.co.thiensurat.data.controller.ProvinceController;
import th.co.thiensurat.data.controller.SubDistrictController;

public class AddressInfo extends BHParcelable implements Serializable {
    public String AddressID;
    public String RefNo;
    public String AddressTypeCode;
    public String AddressDetail;
    public String AddressDetail2;
    public String AddressDetail3;
    public String AddressDetail4;
    public String ProvinceCode;
    public String DistrictCode;
    public String SubDistrictCode;
    public String Zipcode;
    public double Latitude;
    public double Longitude;
    public String AddressInputMethod;
    public String TelHome;
    public String TelMobile;
    public String TelOffice;
    public String EMail;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;

    // Extend Fields
    public String AddressTypeName;
    public String ProvinceName;
    public String DistrictName;
    public String SubDistrictName;

    public static enum AddressType {
        AddressIDCard, AddressPayment, AddressInstall
    }

    public static enum AddressInputMethodType {
        NORMAL,
        COPY_CARD,
        COPY_PAYMENT
    }

    public static String AddressInputMethodCode(AddressInputMethodType type) {
        return Integer.toString(type.ordinal());
    }

    public String Address() {
        StringBuilder sb = new StringBuilder();

        if(AddressDetail != null){
            if (AddressDetail.equals("-")) {
                AddressDetail = "";
                addDetail(sb, AddressDetail);
            } else {
                addDetail(sb, AddressDetail);
            }
        } else {
            AddressDetail = "";
            addDetail(sb, AddressDetail);
        }

        if(AddressDetail2 != null){
            if (AddressDetail2.equals("-")) {
                AddressDetail2 = "";
                addDetail(sb, AddressDetail2);
            } else {
                addDetail(sb, AddressDetail2, "ม.");
            }
        } else {
            AddressDetail2 = "";
            addDetail(sb, AddressDetail2);
        }


        if(AddressDetail3 != null){
            if (AddressDetail3.equals("-")) {
                AddressDetail3 = "";
                addDetail(sb, AddressDetail3);
            } else {
                addDetail(sb, AddressDetail3, "ซ.");
            }
        } else {
            AddressDetail3 = "";
            addDetail(sb, AddressDetail3);
        }

        if(AddressDetail4 != null){
            if (AddressDetail4.equals("-")) {
                AddressDetail4 = "";
                addDetail(sb, AddressDetail4);
            } else {
                addDetail(sb, AddressDetail4, "ถ.");
            }
        } else {
            AddressDetail4 = "";
            addDetail(sb, AddressDetail4);
        }



        /*addDetail(sb, AddressDetail);
        addDetail(sb, AddressDetail2, "ม.");
        addDetail(sb, AddressDetail3, "ซ.");
        addDetail(sb, AddressDetail4, "ถ.");*/

        /*** [START] :: Fixed - [BHPROJ-1036-9080] - ไม่สามารถค้นหาข้อมูลลูกค้าได้ในระบบเก็บเงินค่างวด ***/

        /*if ((SubDistrictName == null || (SubDistrictName != null && SubDistrictName.equals(""))) && (SubDistrictCode != null && !SubDistrictCode.equals(""))) {
            addDetail(sb, new SubDistrictController().getSubDistrictBySubDistrictCode(SubDistrictCode).SubDistrictName, isBKK() ? "แขวง" : "ต.");
        } else {
            addDetail(sb, SubDistrictName, isBKK() ? "แขวง" : "ต.");
        }

        if ((DistrictName == null || (DistrictName != null && DistrictName.equals(""))) && (DistrictCode != null && !DistrictCode.equals(""))) {
            addDetail(sb, new DistrictController().getDistrictByDistrictCode(DistrictCode).DistrictName, isBKK() ? "เขต" : "อ.");
        } else {
            addDetail(sb, DistrictName, isBKK() ? "เขต" : "อ.");
        }

        if ((ProvinceName == null || (ProvinceName != null && ProvinceName.equals(""))) && (ProvinceCode != null && !ProvinceCode.equals(""))) {
            addDetail(sb, new ProvinceController().getProvinceByProvinceCode(ProvinceCode).ProvinceName, isBKK() ? "" : "จ.");
        } else {
            addDetail(sb, ProvinceName, isBKK() ? "" : "จ.");
        }*/

        if ((SubDistrictName == null || (SubDistrictName != null && SubDistrictName.equals(""))) && (SubDistrictCode != null && !SubDistrictCode.equals(""))) {
            SubDistrictInfo _subDistrictInfo = new SubDistrictController().getSubDistrictBySubDistrictCode(SubDistrictCode);
            addDetail(sb, _subDistrictInfo != null ? _subDistrictInfo.SubDistrictName : SubDistrictName, isBKK() ? "แขวง" : "ต.");
        } else {
            addDetail(sb, SubDistrictName, isBKK() ? "แขวง" : "ต.");
        }

        if ((DistrictName == null || (DistrictName != null && DistrictName.equals(""))) && (DistrictCode != null && !DistrictCode.equals(""))) {
            DistrictInfo _districtInfo = new DistrictController().getDistrictByDistrictCode(DistrictCode);
            addDetail(sb, _districtInfo != null ? _districtInfo.DistrictName : DistrictName, isBKK() ? "เขต" : "อ.");
        } else {
            addDetail(sb, DistrictName, isBKK() ? "เขต" : "อ.");
        }

        if ((ProvinceName == null || (ProvinceName != null && ProvinceName.equals(""))) && (ProvinceCode != null && !ProvinceCode.equals(""))) {
            ProvinceInfo _provinceInfo = new ProvinceController().getProvinceByProvinceCode(ProvinceCode);
            addDetail(sb, _provinceInfo != null ? _provinceInfo.ProvinceName : ProvinceName, isBKK() ? "" : "จ.");
        } else {
            addDetail(sb, ProvinceName, isBKK() ? "" : "จ.");
        }

        /*** [END] :: Fixed - [BHPROJ-1036-9080] - ไม่สามารถค้นหาข้อมูลลูกค้าได้ในระบบเก็บเงินค่างวด ***/


//        addDetail(sb, SubDistrictName, isBKK() ? "แขวง" : "ต.");
//        addDetail(sb, DistrictName, isBKK() ? "เขต" : "อ.");
//        addDetail(sb, ProvinceName, isBKK() ? "" : "จ.");
        addDetail(sb, Zipcode);
        return sb.toString();
    }

    private boolean isBKK() {
        if (ProvinceCode != null) {
            return ProvinceCode.equals("1");
        }

        return false;
    }

    public String Telephone() {
        StringBuilder sb = new StringBuilder();

        if (TelHome == null || TelHome.equals("") || TelHome.equals("-"))
            addDetail(sb, TelMobile);
        else {
            if (TelMobile == null || TelMobile.equals("") || TelMobile.equals("-"))
                addDetail(sb, TelHome);
            else {
                addDetail(sb, TelHome + ",");
                addDetail(sb, TelMobile);
            }
        }

        return sb.toString();
    }

    public static AddressInfo copy(AddressInfo src, AddressType type, AddressInputMethodType methodType) {
        if (src != null) {
            AddressInfo info = new AddressInfo();
            info.AddressID = DatabaseHelper.getUUID();
            info.RefNo = src.RefNo;
            info.AddressTypeCode = type.name();
            info.AddressDetail = src.AddressDetail;
            info.AddressDetail2 = src.AddressDetail2;
            info.AddressDetail3 = src.AddressDetail3;
            info.AddressDetail4 = src.AddressDetail4;
            info.ProvinceCode = src.ProvinceCode;
            info.DistrictCode = src.DistrictCode;
            info.SubDistrictCode = src.SubDistrictCode;
            info.Zipcode = src.Zipcode;
            info.Latitude = src.Latitude;
            info.Longitude = src.Longitude;
            info.AddressInputMethod = AddressInputMethodCode(methodType);
            info.TelHome = src.TelHome;
            info.TelMobile = src.TelMobile;
            info.TelOffice = src.TelOffice;
            info.EMail = src.EMail;

            return info;
        }

        return null;
    }

    public AddressInfo() {
    }

    public AddressInfo(AddressInfo src, String AddressTypeCode) {
        this.AddressID = DatabaseHelper.getUUID();
        this.RefNo = src.RefNo;
        this.AddressTypeCode = AddressTypeCode;
        this.AddressDetail = src.AddressDetail;
        this.AddressDetail2 = src.AddressDetail2;
        this.AddressDetail3 = src.AddressDetail3;
        this.AddressDetail4 = src.AddressDetail4;
        this.ProvinceCode = src.ProvinceCode;
        this.ProvinceName = src.ProvinceName;
        this.DistrictCode = src.DistrictCode;
        this.DistrictName = src.DistrictName;
        this.SubDistrictCode = src.SubDistrictCode;
        this.SubDistrictName = src.SubDistrictName;
        this.Zipcode = src.Zipcode;
        this.Latitude = src.Latitude;
        this.Longitude = src.Longitude;
        this.AddressInputMethod = "";
        this.TelHome = src.TelHome;
        this.TelMobile = src.TelMobile;
        this.TelOffice = src.TelOffice;
        this.EMail = src.EMail;
    }

    private StringBuilder addDetail(StringBuilder sb, String text) {
        return addDetail(sb, text, "");
    }

    private StringBuilder addDetail(StringBuilder sb, String text, String prefix) {
        text = BHUtilities.trim(text);
        prefix = BHUtilities.trim(prefix);

        if (sb.length() > 0 && text.length() > 0) {
            sb.append(" ");
            if (text.length() > 0) {
                sb.append(prefix);
            }
        }

        sb.append(BHUtilities.trim(text));

        return sb;
    }

    public static String subAddress(String addr) {
        StringBuilder str = new StringBuilder();
        int index1 = addr.indexOf("ต.");
        int index2 = addr.indexOf("จ.");

        String addr1 = addr.substring(0, index1);
        String addr2 = addr.substring(addr1.length(), index2);
        String addr3 = addr.substring(addr2.length(), addr.length());
        str.append(addr1);
        str.append("\n");
        str.append(addr2);
        str.append("\n");
        str.append(addr3);

        return str.toString();
    }

    public static int addrCheckMultiLine(String addr) {
//        int soi = addr.indexOf("ซ.");
//        int road = addr.indexOf("ถ.");
//        if (soi > -1 || road > -1) {
//            return 3;
//        } else {
//            return 2;
//        }
        return 3;
    }

    public static String addr1(String addr) {
        int soi = -1;
        int road = -1;
        int index = -1;
        int province = -1;
        int district = -1;
        int subdistrict = -1;
        int moo = -1;
//        int line = addrCheckMultiLine(addr);

//        if (line == 2) {
//            index = addr.indexOf("อ.");
//            if (index == -1) {
//                index = addr.indexOf("เขต");
//            }
//        } else {
            moo = addr.indexOf(" ม.");
            soi = addr.indexOf(" ซ.");
            road = addr.indexOf(" ถ.");
            subdistrict = addr.indexOf(" ต.");
            if (subdistrict == -1) {
                subdistrict = addr.indexOf(" แขวง");
                if (subdistrict == -1) {
                    subdistrict = addr.indexOf(" อ.");
                    if (subdistrict == -1) {
                        subdistrict = addr.indexOf(" เขต");
                    }
                }
            }

            if (soi > -1) {
                if (road > -1) {
                    if (moo > -1) {
                        index = moo;
                    } else {
                        index = road;
                    }
                } else {
                    if (moo > -1) {
                        index = moo;
                    } else {
                        index = soi;
                    }
                }
            } else {
                if (road > -1) {
                    if (moo > -1) {
                        index = moo;
                    } else {
                        index = road;
                    }
                } else {
                    if (moo > -1) {
                        index = moo;
                    } else {
                        index = subdistrict;
                    }
                }
            }
//        }


        Log.e("addr1", addr.substring(0, index));
        return addr.substring(0, index);
    }

    public static String addr2(String addr) {
        int soi = -1;
        int road = -1;
        int index1 = -1;
        int index2 = -1;
        int province = -1;
        int district = -1;
        int subdistrict = -1;
        int moo = -1;

        moo = addr.indexOf(" ม.");
//        int line = addrCheckMultiLine(addr);

//        if (line == 2) {
//            index1 = addr.indexOf("อ.");
//            if (index1 == -1) {
//                index1 = addr.indexOf("เขต");
//            }
//            index2 = addr.length();
//        } else {
            soi = addr.indexOf(" ซ.");
            road = addr.indexOf(" ถ.");

            subdistrict = addr.indexOf(" ต.");
            if (subdistrict == -1) {
                subdistrict = addr.indexOf(" แขวง");
                if (subdistrict == -1) {
                    subdistrict = addr.indexOf(" อ.");
                    if (subdistrict == -1) {
                        subdistrict = addr.indexOf(" เขต");
                    }
                }
            }

//            if (soi > -1) {
//                if (road > -1) {
//                    index1 = road;
//                } else {
//                    index1 = subdistrict;
//                }
//            } else {
//                if (road > -1) {
//                    index1 = soi;
//                } else {
//                    index1 = road;
//                }
//            }
//        if (soi > -1) {
//            if (road > -1) {
//                index1 = moo; //มีซอย มีถนน
//            } else {
//                index1 = soi; //มีซอย ไม่มีถนน
//            }
//        } else {
//            if (road > -1) {
//                index1 = road; // ไม่มีซอย มีถนน
//            } else {
//                index1 = moo; // ไม่ีมีซอย ไม่มีถนน
//            }
//        }

        if (soi > -1) {
            if (road > -1) {
                if (moo > -1) {
                    index1 = moo;
                } else {
                    index1 = road;
                }
            } else {
                if (moo > -1) {
                    index1 = moo;
                } else {
                    index1 = soi;
                }
            }
        } else {
            if (road > -1) {
                if (moo > -1) {
                    index1 = moo;
                } else {
                    index1 = road;
                }
            } else {
                if (moo > -1) {
                    index1 = moo;
                } else {
                    index1 = subdistrict;
                }
            }
        }

//            province = addr.indexOf("จ.");
//            if (province == -1) {
//                province = addr.indexOf("กรุงเทพมหานคร");
//            }
//            index2 = province;
            district = addr.indexOf(" อ.");
            if (district == -1) {
                district = addr.indexOf(" เขต");
            }
            index2 = district;
//        }
        Log.e("index1", String.valueOf(index1));
        Log.e("index2", String.valueOf(index2));
        Log.e("addr2", addr.substring(index1, index2));
        return addr.substring(index1, index2);
    }

    public static String addr3(String addr) {
        int province = -1;
        int district = -1;
        province = addr.indexOf(" จ.");
        if (province == -1) {
            province = addr.indexOf("กรุงเทพมหานคร");
        }

        district = addr.indexOf(" อ.");
        if (district == -1) {
            district = addr.indexOf(" เขต");
        }
        Log.e("addr3", addr.substring(district, addr.length()));
        return addr.substring(district, addr.length());
    }
}
