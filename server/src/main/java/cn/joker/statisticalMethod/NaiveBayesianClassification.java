package cn.joker.statisticalMethod;

import cn.joker.entity.ImageEntity;
import cn.joker.entity.ImgMarkEntity;
import cn.joker.entity.TaskEntity;
import cn.joker.namespace.stdName;
import cn.joker.sevice.ImgMarkService;
import cn.joker.vo.RecNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Component
public class NaiveBayesianClassification {
    @Resource
    private ImgMarkService imgMarkService;

    /*
    public static void main(String args[]){
        SpringApplication.run(ImgAnnotatorServer.class, args);

        System.out.println("start");
        List<RecNode> markList = new ArrayList<>();
        ArrayList<RecNode> result = getRecMarkByClass(markList);
        for (RecNode aResult : result) {
            System.out.println(aResult.getTop() + " " + aResult.getLeft());
        }
        getAllRecNode();
    }*/

    public ArrayList<RecNode> getAllRecNode(){
        ArrayList<RecNode> markList = new ArrayList<>(); //把重复标注的结果单独取出来

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(9);
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(12);
        //List<ImgMarkEntity> imgMarkEntityList = imgMarkService.findByImageAndTask(imageEntity, taskEntity);
        List<ImgMarkEntity> imgMarkEntityList = imgMarkService.findAll();

        System.out.println("start");

        for (ImgMarkEntity aImageEntity: imgMarkEntityList) {
            String nodeRec = aImageEntity.getNoteRectangle();
            System.out.println(nodeRec);
            JSONArray jsonArray = new JSONArray(nodeRec);
            for(Object o : jsonArray){
                JSONObject jsonObject = (JSONObject) o;
                RecNode recNode = new RecNode(jsonObject.getDouble(stdName.TOP),jsonObject.getDouble(stdName.LEFT),
                        jsonObject.getDouble(stdName.HEIGHT),jsonObject.getDouble(stdName.WIDTH),jsonObject.getString(stdName.MARK));
                markList.add(recNode);

            }

        }

        markList = getRecMarkByClass(markList);
        return markList;
    }

    /**
     * @param markList 对于某张图片所有的长方形标注数据
     * @return 整合之后的对单张图片的所有标注信息
     */
    private static ArrayList<RecNode> getRecMarkByClass(List<RecNode> markList){
        ArrayList<RecNode> recNodeArrayList = new ArrayList<>(); //整合之后的结果
        ArrayList<Integer> frequency = new ArrayList<>(); // 单独的频率数组，方便后期按权重调整相应数据

        for(int i = 0; i < markList.size(); i++){
            if(recNodeArrayList.size() == 0){ // 对于第一个标注结果，默认直接加进分类中
                recNodeArrayList.add(markList.get(i));
                frequency.add(1);
            }
            else {
                double minOffset = 1000; // 初始化的时候要把最小值变到极大，否则每次比较都不满足
                int classIndex = 0; // 记录最小偏移量对应的值对应在recNodeArrayList中的位置
                for(int j = 0; j < recNodeArrayList.size(); j++){
                    // 计算和每个具体分类的差异，用差异来表示概率，差异越大，表明在此分类中的概率越小
                    double offset = 0.0;
                    offset += Math.abs(markList.get(i).getTop() - recNodeArrayList.get(j).getTop()) / recNodeArrayList.get(j).getTop();
                    offset += Math.abs(markList.get(i).getLeft() - recNodeArrayList.get(j).getLeft()) / recNodeArrayList.get(j).getLeft();
                    offset += Math.abs(markList.get(i).getWidth() - recNodeArrayList.get(j).getWidth()) / recNodeArrayList.get(j).getWidth();
                    offset += Math.abs(markList.get(i).getHeight() - recNodeArrayList.get(j).getHeight()) / recNodeArrayList.get(j).getHeight();

                    //System.out.println(offset);
                    if(offset < minOffset){ //选择
                        minOffset = offset;
                        classIndex = j;
                    }
                }

                // 得到差异最小的数据之后进行差异量和偏移范围的比较
                // 如果假设成立，在偏移范围之内就把该结果放入相应分类中，调整该分类中的数据
                // 如果假设不成立，就单独建立一个新的类
                if(minOffset < 1){ // 假设成立
                    RecNode node = new RecNode(recNodeArrayList.get(classIndex).getTop(), recNodeArrayList.get(classIndex).getLeft(),
                            recNodeArrayList.get(classIndex).getHeight(), recNodeArrayList.get(classIndex).getWidth(),
                            recNodeArrayList.get(classIndex).getMark());
                    // 加权调整数据
                    node.setTop(((node.getTop() * frequency.get(classIndex) + markList.get(i).getTop()) / (frequency.get(classIndex) + 1)));
                    node.setLeft(((node.getLeft() * frequency.get(classIndex) + markList.get(i).getLeft()) / (frequency.get(classIndex) + 1)));
                    node.setHeight(((node.getTop() * frequency.get(classIndex) + markList.get(i).getHeight()) / (frequency.get(classIndex) + 1)));
                    node.setWidth(((node.getTop() * frequency.get(classIndex) + markList.get(i).getWidth()) / (frequency.get(classIndex) + 1)));

                    recNodeArrayList.set(classIndex, node);

                    frequency.set(classIndex, frequency.get(classIndex) + 1);
                }
                else { // 假设不成立
                    recNodeArrayList.add(markList.get(i));
                    frequency.add(1);
                }
            }
        }

        return recNodeArrayList;
    }
}
