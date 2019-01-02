package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/10/27 0027.
 */
public class FileUtil {

    public static void deleteFile(String filePath){
        try{
            File file = new File(filePath);
            file.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void copyFile(String oldPath, String newPath) {
        try {

            int bytesum = 0;

            int byteread = 0;

            File oldfile = new File(oldPath);
            File newFile = new File(newPath);
            if(!newFile.getParentFile().exists()){
                newFile.getParentFile().mkdir();
            }

            if (oldfile.exists()) { //文件存在时

                InputStream inStream = new FileInputStream(oldPath); //读入原文件

                FileOutputStream fs = new FileOutputStream(newPath);

                byte[] buffer = new byte[1444];

                int length;

                while ((byteread = inStream.read(buffer)) != -1) {

                    bytesum += byteread; //字节数 文件大小

                    System.out.println(bytesum);

                    fs.write(buffer, 0, byteread);

                }

                inStream.close();

            }

        } catch (Exception e) {


            System.out.println("复制单个文件操作出错");

            e.printStackTrace();
        }
    }

    public static void fileMove(String oldPath, String newPath){
        copyFile(oldPath,newPath);
        deleteFile(oldPath);
    }
}
