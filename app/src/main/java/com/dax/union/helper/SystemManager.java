package com.dax.union.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import android.app.Activity;
import android.util.Log;

public class SystemManager extends Activity
{
    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     * @param command 命令：String apkRoot="chmod 777 "+getPackageCodePath(); RootCommand(apkRoot);
     * @return 应用程序是/否获取Root权限
     */
    public static String RootCommand(String command,boolean hasResult)
    {
        Process process = null;
        DataOutputStream os = null;
        DataInputStream dis = null;
        try
        {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            dis = new DataInputStream(process.getInputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            StringBuilder sb=new StringBuilder();
            String line=null;
            if(hasResult) {
                while ((line = dis.readLine()) != null) {
                    Log.d("result", line);
                    sb.append(line+"\n");
                }

            }
            process.waitFor();
            return sb.toString();
        } catch (Exception e)
        {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return e.getMessage();
        } finally
        {
            try
            {
                if (os != null)
                {
                    os.close();
                }
                if (dis != null)
                {
                    dis.close();
                }
                process.destroy();
            } catch (Exception e)

            {
            }
        }

    }
}