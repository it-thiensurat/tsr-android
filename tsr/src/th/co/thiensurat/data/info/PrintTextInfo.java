package th.co.thiensurat.data.info;

import th.co.bighead.utilities.BHParcelable;

public class PrintTextInfo extends BHParcelable {
    public final String text;
    public final FontType fontType;
    public final Boolean isBarcode;
    public final Boolean isBankBarcode;
    public final String language;
    public final  int barcodeSize;

    public enum FontType {
        Normal, Bold
    }

    public PrintTextInfo(String text) {
        this.text = text;
        this.fontType = FontType.Normal;
        this.isBarcode = false;
        this.isBankBarcode = false;
        this.language = "TH";
        this.barcodeSize = 0;
    }

    public PrintTextInfo(String text, String language) {
        this.text = text;
        this.fontType = FontType.Normal;
        this.isBarcode = false;
        this.isBankBarcode = false;
        this.language = language;
        this.barcodeSize = 0;
    }

    public PrintTextInfo(String text, FontType fontType) {
        this.text = text;
        this.fontType = fontType;
        this.isBarcode = false;
        this.isBankBarcode = false;
        this.language = "TH";
        this.barcodeSize = 0;
    }

    public PrintTextInfo(String text, FontType fontType, String language) {
        this.text = text;
        this.fontType = fontType;
        this.isBarcode = false;
        this.isBankBarcode = false;
        this.language = language;
        this.barcodeSize = 0;
    }

    public PrintTextInfo(String text, FontType fontType, Boolean isBarcode) {
        this.text = text;
        this.fontType = fontType;
        this.isBarcode = isBarcode;
        this.isBankBarcode = false;
        this.language = "TH";
        this.barcodeSize = 1;
    }

    public PrintTextInfo(String text, FontType fontType, Boolean isBarcode, Boolean isBankBarcode) {
        this.text = text;
        this.fontType = fontType;
        this.isBarcode = isBarcode;
        this.isBankBarcode = isBankBarcode;
        this.language = "TH";
        this.barcodeSize = 1;

    }

    public PrintTextInfo(String text, FontType fontType, Boolean isBarcode, Boolean isBankBarcode, int barcodeSize) {
        this.text = text;
        this.fontType = fontType;
        this.isBarcode = isBarcode;
        this.isBankBarcode = isBankBarcode;
        this.language = "TH";
        this.barcodeSize = barcodeSize;
    }

}
