package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.ImageTypeInfo;

public class ImageTypeController extends BaseController {

	public void addImageType(ImageTypeInfo info) {
		executeNonQuery("insert into ImageType(ImageTypeCode,ImageTypeName) values(?,?)", new String[] {info.ImageTypeCode, info.ImageTypeName});
	}
	
	public void deleteImageType() {
		executeNonQuery("delete from ImageType", null);
	}
}
