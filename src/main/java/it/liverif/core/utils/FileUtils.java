package it.liverif.core.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static String createRandomFilename(String repository, String modelName) {
        String random = RandomStringUtils.generateAlfanumeric(20);
        String filename = repository + File.separator + modelName + File.separator + random;
        for(File file = new File(filename); file.exists(); file = new File(filename)) {
            random = RandomStringUtils.generateAlfanumeric(20);
            filename = repository + File.separator + modelName + File.separator + random;
        }
        return filename.substring(repository.length() + 1);
    }

    public static String createFilename(String repository, String modelName, String filename) {
        String pathfilename = repository + File.separator + modelName + File.separator + filename;
        Integer count=0;
        for(File file = new File(pathfilename); file.exists(); file = new File(pathfilename)) {
            count++;
            pathfilename = repository + File.separator + modelName + File.separator + count+"-"+filename;
        }
        return pathfilename.substring(repository.length() + 1);
    }

    public static String createDirectory(String repository, String modelName) throws IOException {
        String filename = repository + File.separator + modelName;
        File dir=new File(filename);
        if (!dir.exists()) dir.mkdirs();
        return dir.getCanonicalPath();
    }

    public static String sanitizeFilename(String name) {
        return name.replaceAll("[:\\\\/*?|<> '\"]", "_");
    }
    
}
