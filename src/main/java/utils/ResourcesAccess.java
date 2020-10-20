package utils;

import java.io.File;
import java.nio.file.Files;

public class ResourcesAccess {
    String folderLink;
    ClassLoader classLoader = getClass().getClassLoader();

    public ResourcesAccess(){
        this.folderLink = ".";
    }

    public  ResourcesAccess(String folder){
        this.folderLink = folder;
    }

    public String getFileInString(String fileName){
        String content = "Sorry, error appeared..";
        try{
            File file = new File(classLoader.getResource(folderLink+"/"+fileName).getFile());

            //Read File Content
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            ErrorCatcher.catchMsg(e);
        }
        return content;
    }
}
