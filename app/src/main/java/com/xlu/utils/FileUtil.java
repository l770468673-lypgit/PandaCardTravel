package com.xlu.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by giant on 2017/7/21.
 */

public class FileUtil {
    public static File getExternalDir(Context context) {
        System.err.println("-----------context-------:"+context);
        final String cacheDir = context.getPackageName() + "/cache";
        return new File(Environment.getExternalStorageDirectory().getPath()+ cacheDir);
    }

    // 应用所下载文件的存储目录
    public static File getApkStorageFile(Context context) {
        return new File(context.getFilesDir()+"/cache");
    }

    public static File getExternalApkStorageFile(Context context) {
        return new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + "/" + context.getPackageName() + "/cache");
    }

    public static boolean checkIsExistInLocal(File parent, String name) {
        File[] listFile = parent.listFiles();
        if(listFile != null){
            for (File f : listFile) {
                if(f.getName().contains(name)){
                    return true;
                }
                if (f.isDirectory()) {
                    if(checkIsExistInLocal(f, name))
                        return true;

                }
            }
        }
        return false;
    }

    public static File getLocalFile(File parent, String name) {
        File[] listFile = parent.listFiles();
        if(listFile != null){
            for (File f : listFile) {
                if(name.equals(f.getName().split("[.]")[0])){
                    return f;
                }
                if (f.isDirectory()){
                    File file = getLocalFile(f, name);
                    if(file != null)
                        return file;
                }
            }
        }
        return null;
    }



    public static void deleteFile(File file) {
        if(file.isDirectory()){
            File[] listFile = file.listFiles();
            if(listFile != null){
                for (File f : listFile) {
                    deleteFile(f);
                }
            }
        }
        file.delete();
    }

    // 从文件中读取字符串
    public static String readFromFile(File f) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "gbk"));
            String s = "";
            String line = null;
            while ((line = br.readLine()) != null) {
                s += line;
            }
            return s;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void CreateDir(File file) {
        // TODO Auto-generated method stub
        if(new File(file.getParent()).exists()){
            file.mkdirs();
        }
        else{
            CreateDir(new File(file.getParent()));
            file.mkdirs();
        }
    }

    public static boolean checkIsExistInLocal1(File parent, String name) {
        File[] listFile = parent.listFiles();
        if(listFile != null){
            for (File f : listFile) {
                if(f.getName().equals(name)){
                    return true;
                }
                if (f.isDirectory()) {
                    if(checkIsExistInLocal1(f, name))
                        return true;

                }
            }
        }
        return false;
    }

    public static File getLocalFile1(File parent, String name) {
        File[] listFile = parent.listFiles();
        if(listFile != null){
            for (File f : listFile) {
                if(f.getName().equals(name)){
                    return f;
                }
                if (f.isDirectory()){
                    File file = getLocalFile1(f, name);
                    if(file != null)
                        return file;
                }
            }
        }
        return null;
    }

}
