package th.co.thiensurat.fragments.credit;


import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Person implements ClusterItem {
    public final String name;
    public final Bitmap profilePhoto;
    public final LatLng mPosition;
    public final String RefNo;

    public Person(LatLng position, String name, Bitmap pictureResource, String refNo) {
        this.name = name;
        profilePhoto = pictureResource;
        mPosition = position;
        RefNo = refNo;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
