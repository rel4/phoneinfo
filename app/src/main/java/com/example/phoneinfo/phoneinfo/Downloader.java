package com.example.phoneinfo.phoneinfo;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Downloader
{
    public String data;

    public String getGen(String paramString)
            throws Exception
    {
        Log.d("", paramString);
        String str = LogUtils.strEncode(paramString);
        this.data = str;
        return paramString;
    }

    public String readData(InputStream paramInputStream)
            throws Exception
    {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrayOfByte1 = new byte[1024];
        while (true)
        {
            int i = paramInputStream.read(arrayOfByte1);
            if (i == -1)
            {
                byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
                localByteArrayOutputStream.close();
                return new String(arrayOfByte2);
            }
            localByteArrayOutputStream.write(arrayOfByte1, 0, i);
        }
    }

    public void save()
            throws IOException
    {
        if (this.data == null)
            throw new IOException("要保存的数据不正确：" + this.data);
        save("mobileSecurity");
    }

    public void save(String paramString)
            throws IOException
    {
        Log.e(getClass().getSimpleName(), paramString);
        FileOutputStream localFileOutputStream = new FileOutputStream(new File("/mnt/sdcard/" + paramString));
        localFileOutputStream.write(this.data.getBytes());
        localFileOutputStream.close();
    }
}