package cn.joker.util;

import cn.joker.entity.ImageEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.namespace.stdName;
import cn.joker.serviceimpl.ImgServiceImpl;
import cn.joker.sevice.ImgService;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author: pis
 * @description: good good study
 * @date: create in 10:14 2018/4/17
 */
public class FileHelper {
    private FileHelper() {
        throw new IllegalStateException(stdName.UTILCLASS);
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

    public static Integer saveFiles(TaskEntity taskEntity, MultipartFile file,ImgService imgService) {
        if (file.isEmpty())
            return 0;
        String path = DIR + "task/" + String.valueOf(taskEntity.getId()) + "/images/";
        String fileName = taskEntity.getId().toString() + "-" + file.getOriginalFilename();
        fileName = FileHelper.getRealFilePath(fileName);
        fileName = fileName.substring(fileName.lastIndexOf(FILE_SEPARATOR) + 1);
        File dest = new File(path + fileName);
        boolean bool = true;
        if (!dest.getParentFile().getParentFile().exists()) {
            bool = dest.getParentFile().getParentFile().mkdir();
        }
        if (!dest.getParentFile().exists()) {
            bool = bool && dest.getParentFile().mkdir();
        }
        if (!bool)
            return 0;
        try {
            file.transferTo(dest);
            String attr = fileName.substring(fileName.lastIndexOf('.') + 1);
            if (attr.equals("zip")) {
                Project p = new Project();
                Expand e = new Expand();
                e.setProject(p);
                e.setSrc(new File(dest.getPath()));
                e.setOverwrite(false);
                e.setDest(new File(path));
                e.setEncoding("gbk");
                e.execute();  //解压
                Path path1 = Paths.get(path + fileName);
                Files.delete(path1);
            }
        } catch (IOException e) {
            logger.error(globalException);
        }

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImg_task(taskEntity);
        imageEntity.setImgName(fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.')));
        imageEntity.setUrl("task/" + String.valueOf(taskEntity.getId()) + "/images/" + fileName);
        imgService.add(imageEntity);
        if(taskEntity.getImageEntityList() == null) {
            List<ImageEntity> imageEntities = new ArrayList<>();
            taskEntity.setImageEntityList(imageEntities);
        }
        List<ImageEntity> imageEntities = taskEntity.getImageEntityList();
        imageEntities.add(imageEntity);
        return Objects.requireNonNull(dest.getParentFile().list()).length;
    }
}
