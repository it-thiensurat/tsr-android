package th.co.thiensurat.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import th.co.bighead.utilities.BHStorage;

public class AThailIDActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("application/zip".equals(type)) {
                Uri uri = intent.getClipData().getItemAt(0).getUri();

                if(uri.getLastPathSegment().endsWith("zip")){
                    //File zip = new File(uri.toString());
                    try {
                        unzip(uri.getPath(), BHStorage.getFolder(BHStorage.FolderType.Database) + "/");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /*File newFile = new File(uri.getPath());
                    try {
                        // is = file zip :: extracting
                        InputStream is = new FileInputStream(newFile);
                        ZipInputStream zis = BHUtilities.getFileFromZip(is);


                       *//* File oldFileDB = new File(DatabaseManager.getInstance().getDatabasePath());
                        if (oldFileDB.exists()) {
                            oldFileDB.delete();
                        }*//*

                        if(zis != null) {
                            writeExtractedFileToDisk(zis, new FileOutputStream(BHStorage.getFolder(BHStorage.FolderType.Database) + "/"));
                        }

                    } catch (IOException e) {

                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT);
                        //throw e;
                    }*/


                }


                //String text = intent.getStringExtra(Intent.EXTRA_TEXT);
                //String t = intent.get ;
                //textView.setText(text);
            } //else if (type.startsWith("image/")) {
                //Uri uri = (Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM);
                //imageView.setImageURI(uri);
            //}
        }
    }

    public static void writeExtractedFileToDisk(InputStream in, OutputStream outs) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            outs.write(buffer, 0, length);
        }
        outs.flush();
        outs.close();
        in.close();
    }


    public static void unzip(String zipFile, String location) throws IOException {
        try {
            File f = new File(location);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String[] strFolder = ze.getName().split("/");

                    for(int i = 0; i < (strFolder.length - 1); i++){
                        File pathFolder = new File(location + strFolder[i]);
                        if(!pathFolder.isDirectory()) {
                            pathFolder.mkdirs();
                        }
                    }
                    String[] strFile = strFolder[strFolder.length - 1].split("\\.");

                    if(strFile.length > 1){
                        String pathFile = location + ze.getName();

                        FileOutputStream fout = new FileOutputStream(pathFile, false);
                        try {

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = zin.read(buffer)) > 0) {
                                fout.write(buffer, 0, length);
                            }
                            zin.closeEntry();
                        }
                        finally {
                            fout.close();
                        }
                    }



                    /*if (ze.isDirectory()) {
                        File unzipFile = new File(path);
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    }
                    else {
                        File unzipFile = new File(location + ze.getName().split("/")[0]);
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }

                        FileOutputStream fout = new FileOutputStream(path, false);
                        //FileOutputStream fout = new FileOutputStream(path);
                        try {

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = zin.read(buffer)) > 0) {
                                fout.write(buffer, 0, length);
                            }
                            zin.closeEntry();

                            *//*for (int c = zin.read(); c != -1; c = zin.read()) {
                                fout.write(c);
                            }
                            zin.closeEntry();*//*
                        }
                        finally {
                            fout.close();
                        }
                    }*/
                }
            }
            finally {
                zin.close();
            }
        }
        catch (Exception e) {
            Log.e("TAG", "Unzip exception", e);
        }
    }

}
