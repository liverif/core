package it.liverif.core.utils;

import org.springframework.util.FileCopyUtils;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZipUtils {
    
    private InputStream is;
    private ZipInputStream zis;
    
    public UnZipUtils(String filename) throws FileNotFoundException {
        is = new FileInputStream(filename);
        zis = new ZipInputStream(new BufferedInputStream(is));
    }

    public UnZipUtils(byte[] fileondb) {
        is = new ByteArrayInputStream(fileondb);
        zis = new ZipInputStream(new BufferedInputStream(is));
    }
    
    public byte[] getFileByteArray() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (zis!=null){
            ZipEntry entry = zis.getNextEntry();
            if (entry != null){
                try (InputStream in = zis) {
                    FileCopyUtils.copy(in, out);
                }
                return out.toByteArray();
            }
        }
        return null;
    }
   
}
