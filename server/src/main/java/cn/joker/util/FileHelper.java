package cn.joker.util;

import cn.joker.entity.ImageEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.namespace.StdName;
import cn.joker.sevice.ImgService;
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
        throw new IllegalStateException(StdName.UTILCLASS);
    }


    private static Logger logger = LoggerFactory.getLogger(JsonHelper.class);
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static String globalException = "Exception";

    private static String getRealFilePath(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }

    private static final String DIR = System.getProperty("user.dir") + "/annotator/";


    public static Integer saveFiles(TaskEntity taskEntity, MultipartFile file, ImgService imgService) {
        if (file.isEmpty())
            return 0;
        String path = DIR + "task/" + taskEntity.getId() + "/images/";
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
        logger.info(fileName);
        try {
            file.transferTo(dest);
            String attr = fileName.substring(fileName.lastIndexOf('.') + 1);
            if (attr.equals("zip")) {
                Project p = new Project();
                Expand e = new Expand();
                e.setProject(p);
                e.setSrc(new File(dest.getPath()));
                e.setOverwrite(false);
                e.setDest(new File(path + fileName));
                e.setEncoding("gbk");
                e.execute();  //解压
                Path path1 = Paths.get(path + fileName);
                Files.delete(path1);
                File file1 = new File(path + fileName);
                String strings[] = file1.list();
                for(String string : strings){
                    ImageEntity imageEntity = new ImageEntity();
                    imageEntity.setImg_task(taskEntity);
                    imageEntity.setType(taskEntity.getType());
                    imageEntity.setImgName(string.substring(0,string.lastIndexOf('.')));
                    imageEntity.setUrl("task/" + taskEntity.getId() + "/images/" + fileName + "/" + string);
                    imgService.add(imageEntity);
                    warpList(taskEntity, imageEntity);
                }
            }
            else {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setImg_task(taskEntity);
                imageEntity.setType(taskEntity.getType());
                imageEntity.setImgName(fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.')));
                imageEntity.setUrl("task/" + taskEntity.getId() + "/images/" + fileName);
                imgService.add(imageEntity);
                warpList(taskEntity, imageEntity);
            }
        } catch (IOException e) {
            logger.error(globalException);
        }

        return Objects.requireNonNull(dest.getParentFile().list()).length;
    }

    private static void warpList(TaskEntity taskEntity, ImageEntity imageEntity) {
        if (taskEntity.getImageEntityList() == null) {
            List<ImageEntity> imageEntities = new ArrayList<>();
            taskEntity.setImageEntityList(imageEntities);
        }
        else {
            List<ImageEntity> imageEntities = taskEntity.getImageEntityList();
            imageEntities.add(imageEntity);
        }
    }
}
