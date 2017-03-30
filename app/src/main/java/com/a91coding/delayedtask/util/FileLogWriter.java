package com.a91coding.delayedTask.util;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/03/24.
 */

public class FileLogWriter {

    public static void log(final String folder, String fileName, String str) {
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
            str = tf.format(new Date()) + "|" + str;
//            writeFileToSDCard(str.getBytes("UTF-8"), folder, fileName, true, true);
            writeFileToSDCard(str, folder, fileName, true, true);
    }

    public static void testLog(String str) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = df.format(new Date()) + "log.txt";
        String folder = "testlog";
        log(folder,fileName, str);
    }

    public static void writeFileSdcardFile(String fileName, String write_str) throws IOException {
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8");
            BufferedWriter writer = new BufferedWriter(out);
            writer.write(write_str);
            writer.close();
        } catch (Exception e) {
            String name = e.getClass().getName();
            if (name.equals("java.io.FileNotFoundException")) {
                generateLogFile(fileName);
            }
        }
    }

    /**
     * 此方法为android程序写入sd文件文件，用到了android-annotation的支持库@
     *
     * @param buffer   写入文件的内容
     * @param folder   保存文件的文件夹名称,如log；可为null，默认保存在sd卡根目录
     * @param fileName 文件名称，默认app_log.txt
     * @param append   是否追加写入，true为追加写入，false为重写文件
     * @param autoLine 针对追加模式，true为增加时换行，false为增加时不换行
     */
    public synchronized static void writeFileToSDCard(@NonNull final String buffer, @Nullable final String folder,
                                                      @Nullable final String fileName, final boolean append, final boolean autoLine) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean sdCardExist = Environment.getExternalStorageState().equals(
                        android.os.Environment.MEDIA_MOUNTED);
                String folderPath;
                if (sdCardExist) {
                    //TextUtils为android自带的帮助类
                    if (TextUtils.isEmpty(folder)) {
                        //如果folder为空，则直接保存在sd卡的根目录
                        folderPath = Environment.getExternalStorageDirectory()  + File.separator;
                    } else {
                        folderPath = Environment.getExternalStorageDirectory()
                                + File.separator + folder + File.separator;
                    }
                } else {
                    return;
                }

                File fileDir = new File(folderPath);
                if (!fileDir.exists()) {
                    if (!fileDir.mkdirs()) {
                        return;
                    }
                }
                File file;
                //判断文件名是否为空
                if (TextUtils.isEmpty(fileName)) {
                    file = new File(folderPath + "app_log.txt");
                } else {
                    file = new File(folderPath + fileName);
                }
                FileOutputStream out = null;
                BufferedWriter bw = null;
                try {
                    if (append) { //如果为追加则在原来的基础上继续写文件
                        out = new FileOutputStream(file, true);
                    } else { //重写文件，覆盖掉原来的数据
                        out = new FileOutputStream(file);
                    }
                    bw = new BufferedWriter(new OutputStreamWriter(out));
                    bw.append(buffer);
                    if (autoLine) {
                        bw.append("\r\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 此方法为android程序写入sd文件文件，用到了android-annotation的支持库@
     *
     * @param buffer   写入文件的内容
     * @param folder   保存文件的文件夹名称,如log；可为null，默认保存在sd卡根目录
     * @param fileName 文件名称，默认app_log.txt
     * @param append   是否追加写入，true为追加写入，false为重写文件
     * @param autoLine 针对追加模式，true为增加时换行，false为增加时不换行
     */
    public synchronized static void writeFileToSDCard(@NonNull final byte[] buffer, @Nullable final String folder,
                                                      @Nullable final String fileName, final boolean append, final boolean autoLine) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean sdCardExist = Environment.getExternalStorageState().equals(
                        android.os.Environment.MEDIA_MOUNTED);
                String folderPath;
                if (sdCardExist) {
                    //TextUtils为android自带的帮助类
                    if (TextUtils.isEmpty(folder)) {
                        //如果folder为空，则直接保存在sd卡的根目录
                        folderPath = Environment.getExternalStorageDirectory()
                                + File.separator;
                    } else {
                        folderPath = Environment.getExternalStorageDirectory()
                                + File.separator + folder + File.separator;
                    }
                } else {
                    return;
                }

                File fileDir = new File(folderPath);
                if (!fileDir.exists()) {
                    if (!fileDir.mkdirs()) {
                        return;
                    }
                }
                File file;
                //判断文件名是否为空
                if (TextUtils.isEmpty(fileName)) {
                    file = new File(folderPath + "app_log.txt");
                } else {
                    file = new File(folderPath + fileName);
                }
                RandomAccessFile raf = null;
                FileOutputStream out = null;
                try {
                    if (append) {
                        //如果为追加则在原来的基础上继续写文件
                        raf = new RandomAccessFile(file, "rw");
                        raf.seek(file.length());
                        raf.write(buffer);
                        if (autoLine) {
                            raf.write("\r\n".getBytes());
                        }
                    } else {
                        //重写文件，覆盖掉原来的数据
                        out = new FileOutputStream(file);
                        out.write(buffer);
                        out.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (raf != null) {
                            raf.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void generateLogFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //读SD中的文件
    public static String readFileSdcardFile(String fileName) throws IOException {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte[] buffer = new byte[length];
            fin.read(buffer);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
