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
    private static final String FILESEPARATOR = System.getProperty("file.separator");

    /**
     *
     * @param path 路径
     * @return 实际路径
     */
    private static String getRealFilePath(String path) {
        return path.replace("/", FILESEPARATOR).replace("\\", FILESEPARATOR);
    }

    private static final String DIR = System.getProperty("user.dir") + "/annotator/";


    /**
     *
     * @param taskEntity 任务
     * @param file 文件
     * @param imgService 文件服务
     * @return 是否成功
     */
    public static Integer saveFiles(TaskEntity taskEntity, MultipartFile file, ImgService imgService) {
        if (file.isEmpty())
            return 0;
        String path = DIR + "task/" + taskEntity.getId() + "/images/";
        String fileName = taskEntity.getId().toString() + "-" + file.getOriginalFilename();
        fileName = FileHelper.getRealFilePath(fileName);
        fileName = fileName.substring(fileName.lastIndexOf(FILESEPARATOR) + 1);
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
                String str = path + fileName + "s";
                str =  str.replaceAll(" ","");
                str = str.replaceAll("%","");
                e.setDest(new File(str));
                e.setEncoding("gbk");
                e.execute();  //解压
                Path path1 = Paths.get(path + fileName);
                Files.delete(path1);
                File newFile = new File(str);
                String strings[] = newFile.list();
                assert strings != null;
                for(String string : strings){
                    ImageEntity imageEntity = new ImageEntity();
                    imageEntity.setImg_task(taskEntity);
                    imageEntity.setType(taskEntity.getType());
                    imageEntity.setImgName(string.substring(0,string.lastIndexOf('.')));
                    String temp = fileName;
                    temp = temp.replaceAll(" ","");
                    temp = temp.replaceAll("%","");
                    imageEntity.setUrl("task/" + taskEntity.getId() + "/images/" + temp + "s/" + string);
                    imgService.add(imageEntity);
                    warpList(taskEntity, imageEntity);
                }
                return Objects.requireNonNull(newFile.list()).length;
            }
            else {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setImg_task(taskEntity);
                imageEntity.setType(taskEntity.getType());
                imageEntity.setImgName(fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.')));
                imageEntity.setUrl("task/" + taskEntity.getId() + "/images/" + fileName);
                imgService.add(imageEntity);
                warpList(taskEntity, imageEntity);
                return 1;
            }
        } catch (IOException e) {
            logger.error(StdName.ERROR);
        }
        return -1;
    }

    /**
     *
     * @param taskEntity 任务
     * @param imageEntity 图片
     */
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
