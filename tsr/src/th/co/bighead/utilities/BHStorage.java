package th.co.bighead.utilities;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.os.Build;

public class BHStorage {
	public enum FolderType {
		Database, Picture
	}

	private static final String DATA_ROOT_FOLDER_NAME = "/Android/data/" + BHApplication.getContext().getPackageName() + "/files";
	private static final String DATA_DATABASE_FOLDER_NAME = "database";
	private static final String DATA_PICTURE_FOLDER_NAME = "pictures";
	private static final String ENVIRONMENT_EXTERNAL_STORAGE = "SECONDARY_STORAGE";
	private static final String ENVIRONMENT_INTERNAL_STORAGE = "EXTERNAL_STORAGE";

	public static String getFolder(FolderType type) {
		String childFolder = null;
		switch (type) {
		case Database:
			childFolder = DATA_DATABASE_FOLDER_NAME;
			break;

		case Picture:
			childFolder = DATA_PICTURE_FOLDER_NAME;
			break;

		default:
			break;
		}

		String folder = getDataStorage();
		if (childFolder != null) {
			folder += "/" + childFolder;
		}

		File currentFolder = new File(folder);
		if (!currentFolder.exists() || !currentFolder.isDirectory()) {
			currentFolder.mkdirs();
		}

		return folder;
	}

	private static String getDataStorage() {
		String device = BHPreference.dataStorage();

		if (device == null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				File[] files = BHApplication.getContext().getExternalFilesDirs(null);

				if (files != null && files.length > 1 && files[1] != null && files[1].getPath() != null) {
					device = files[1].getPath();
				} else {
					device = files[0].getPath();
				}
			} else {
				device = getExternalStorage();

				if (device == null) {
					device = getInternalStorage();
				}

				if (device != null) {
					device = device + DATA_ROOT_FOLDER_NAME;
				}
			}

			if (device != null) {
				BHPreference.setDataStorage(device);
			}
		}

		return device;
	}

	private static String getExternalStorage() {
		return getStorage(ENVIRONMENT_EXTERNAL_STORAGE);
	}

	private static String getInternalStorage() {
		return getStorage(ENVIRONMENT_INTERNAL_STORAGE);
	}

	private static String getStorage(String type) {
		String storage = System.getenv(type);
		if (storage != null) {
			String[] storages = storage.split(":");
			String mountDevices = getMountDevices();
			for (String device : storages) {
				if (mountDevices.contains(device)) {
					return device;
				}
			}
		}

		return null;
	}

	private static String getMountDevices() {
		StringBuilder sb = new StringBuilder();
		try {
			// Open the file
			FileInputStream fs = new FileInputStream("/proc/mounts");

			DataInputStream in = new DataInputStream(fs);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Remember each line
				sb.append(strLine);
			}

			// Close the stream
			in.close();
		} catch (Exception e) {
			// Catch exception if any
			e.printStackTrace();
		}

		return sb.toString();
	}
}
