package it.liverif.core.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    
    private ByteArrayOutputStream bos = null;
    private FileOutputStream fos = null;
    private ZipOutputStream zos = null;

    public ZipUtils(String zipFileName) throws FileNotFoundException {
        this.fos = new FileOutputStream(zipFileName);
        this.zos = new ZipOutputStream(new BufferedOutputStream(this.fos));
    }

    public ZipUtils(ByteArrayOutputStream bos) {
        this.bos = bos;
        this.zos = new ZipOutputStream(bos);
    }

    public void addFile(String fileName, byte[] data) throws FileNotFoundException, IOException {
        ZipEntry zipEntry = new ZipEntry(fileName);
        this.zos.putNextEntry(zipEntry);
        this.zos.write(data);
        this.zos.closeEntry();
    }

    public void close() throws IOException {
        this.zos.flush();
        this.zos.close();
        if (this.fos != null) {
            this.fos.flush();
            this.fos.close();
        }

    }
}