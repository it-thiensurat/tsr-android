package th.co.thiensurat.data.info;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHParcelable;

/**
 * Created by bighead on 3/11/2016.
 */
public class ShortReceiptInfo extends BHParcelable {
    public Bitmap receiptHeader;
    public String receiptDetail1;
    public Bitmap receiptDetail;
    public String receiptDetail2;
    public Bitmap receiptTailer;

    public List<String> listTxt = new ArrayList<>();
}
