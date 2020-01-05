package net.plang.HoWooAccount.common.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;

public class FileUploader {


    public static String doFileUpload(FileItem fileItem, String empId) throws IOException {
        InputStream in = fileItem.getInputStream();

        String realFileName = fileItem.getName().substring(fileItem.getName().lastIndexOf("//") + 1);

        String fileExt = realFileName.substring(realFileName.lastIndexOf("."));

        String saveFileName = empId + fileExt;

        String uploadPath = "C:\\Program Files\\nginx-1.15.8\\html\\photos\\";

        FileOutputStream fout = new FileOutputStream(uploadPath + saveFileName);

        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = in.read(buffer, 0, 8192)) != -1)
            fout.write(buffer, 0, bytesRead);

        in.close();
        fout.close();

        return "/photos/" + saveFileName;

    }
}
