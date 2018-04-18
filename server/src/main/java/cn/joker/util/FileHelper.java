package cn.joker.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * @author: pis
 * @description: good good study
 * @date: create in 10:14 2018/4/17
 */
public class FileHelper {

    private FileHelper() {
        throw new IllegalStateException("Utility class");
    }

    private static Logger logger = LoggerFactory.getLogger(JsonHelper.class);
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static String globalException = "Exception";

    private static String getRealFilePath(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }

    private static final String DIR = System.getProperty("user.dir") + "/annotator/";

    public static boolean saveZip(String taskID, MultipartFile file) {
        if (file.isEmpty()) {
            return false;
        }
        String fileName = file.getOriginalFilename();
        fileName = FileHelper.getRealFilePath(fileName);
        fileName = fileName.substring(fileName.lastIndexOf(FILE_SEPARATOR) + 1, fileName.lastIndexOf('.'));

        String path = DIR + "task/" + taskID + "/images/";
        File dest = new File(path + fileName);
        boolean bool = true;
        if (!dest.getParentFile().getParentFile().exists()) {
            bool = dest.getParentFile().getParentFile().mkdir();
        }
        if (!dest.getParentFile().exists()) {
            bool = bool && dest.getParentFile().mkdir();
        }
        if (!bool)
            return false;
        try {
            file.transferTo(dest);  //保存zip
            Project p = new Project();
            Expand e = new Expand();
            e.setProject(p);
            e.setSrc(new File(dest.getPath()));
            e.setOverwrite(false);
            e.setDest(new File(path));
           /*
           ant下的zip工具默认压缩编码为UTF-8编码，
           而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
           所以解压缩时要制定编码格式
           */
            e.setEncoding("gbk");
            e.execute();  //解压
            Path path1 = Paths.get(path + fileName);
            Files.delete(path1);
        } catch (IllegalStateException | IOException e) {
            logger.error(globalException);
            return false;
        }
        return true;
    }

    public static boolean saveFiles(String taskID, List<MultipartFile> files) {
        if (files.isEmpty())
            return false;
        String path = DIR + "task/" + taskID + "/images/";
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            fileName = FileHelper.getRealFilePath(fileName);
            fileName = fileName.substring(fileName.lastIndexOf(FILE_SEPARATOR) + 1);
            File dest = new File(path + fileName);
            if (!dest.getParentFile().getParentFile().exists()) {
                return dest.getParentFile().getParentFile().mkdir();
            }
            if (!dest.getParentFile().exists()) {
                return dest.getParentFile().mkdir();
            }
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                logger.error(globalException);
                return false;
            }
        }
        return true;
    }
}
