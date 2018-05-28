package cn.joker.statisticalMethod;

import cn.joker.entity.ImgMarkEntity;
import cn.joker.namespace.stdName;
import cn.joker.vo.RecNode;
import cn.joker.vo.RecNodeList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NaiveBayesianClassification {

    public static List<RecNodeList> integration(List<ImgMarkEntity> imgMarkEntityList) {
        ArrayList<RecNode> markList = new ArrayList<>(); //把重复标注的结果单独取出来

        for (ImgMarkEntity aImageEntity : imgMarkEntityList) {
            String nodeRec = aImageEntity.getNoteRectangle();
            System.out.println(nodeRec);
            JSONArray jsonArray = new JSONArray(nodeRec);
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                RecNode recNode = new RecNode(jsonObject.getDouble(stdName.TOP), jsonObject.getDouble(stdName.LEFT),
                        jsonObject.getDouble(stdName.HEIGHT), jsonObject.getDouble(stdName.WIDTH), jsonObject.getString(stdName.MARK), aImageEntity.getWorker());
                markList.add(recNode);

            }

        }

        return getRecMarkByClass(markList);
    }

    /**
     * @param markList 对于某张图片所有的长方形标注数据
     * @return 整合之后的对单张图片的所有标注信息
     */
    private static ArrayList<RecNodeList> getRecMarkByClass(List<RecNode> markList) {
        ArrayList<RecNodeList> recNodeArrayList = new ArrayList<>(); //整合之后的结果
        ArrayList<Integer> frequency = new ArrayList<>(); // 单独的频率数组，方便后期按权重调整相应数据

        for (RecNode aMarkList : markList) {
            if (recNodeArrayList.size() == 0) { // 对于第一个标注结果，默认直接加进分类中
                RecNodeList recNodeList = new RecNodeList();
                recNodeList.getRecNodes().add(aMarkList);
                recNodeList.setRecNode(aMarkList);
                recNodeArrayList.add(recNodeList);
                frequency.add(1);
            } else {
                double minOffset = 1000; // 初始化的时候要把最小值变到极大，否则每次比较都不满足
                int classIndex = 0; // 记录最小偏移量对应的值对应在recNodeArrayList中的位置
                for (int j = 0; j < recNodeArrayList.size(); j++) {
                    // 计算和每个具体分类的差异，用差异来表示概率，差异越大，表明在此分类中的概率越小
                    double offset = 0.0;
                    offset += Math.abs(aMarkList.getTop() - recNodeArrayList.get(j).getRecNode().getTop()) / recNodeArrayList.get(j).getRecNode().getTop();
                    offset += Math.abs(aMarkList.getLeft() - recNodeArrayList.get(j).getRecNode().getLeft()) / recNodeArrayList.get(j).getRecNode().getLeft();
                    offset += Math.abs(aMarkList.getWidth() - recNodeArrayList.get(j).getRecNode().getWidth()) / recNodeArrayList.get(j).getRecNode().getWidth();
                    offset += Math.abs(aMarkList.getHeight() - recNodeArrayList.get(j).getRecNode().getHeight()) / recNodeArrayList.get(j).getRecNode().getHeight();

                    //System.out.println(offset);
                    if (offset < minOffset) { //选择
                        minOffset = offset;
                        classIndex = j;
                    }
                }

                // 得到差异最小的数据之后进行差异量和偏移范围的比较
                // 如果假设成立，在偏移范围之内就把该结果放入相应分类中，调整该分类中的数据
                // 如果假设不成立，就单独建立一个新的类
                if (minOffset < 1) { // 假设成立
                    RecNode node = new RecNode(recNodeArrayList.get(classIndex).getRecNode().getTop(), recNodeArrayList.get(classIndex).getRecNode().getLeft(),
                            recNodeArrayList.get(classIndex).getRecNode().getHeight(), recNodeArrayList.get(classIndex).getRecNode().getWidth(),
                            recNodeArrayList.get(classIndex).getRecNode().getMark(), null);
                    // 加权调整数据
                    node.setTop(((node.getTop() * frequency.get(classIndex) + aMarkList.getTop()) / (frequency.get(classIndex) + 1)));
                    node.setLeft(((node.getLeft() * frequency.get(classIndex) + aMarkList.getLeft()) / (frequency.get(classIndex) + 1)));
                    node.setHeight(((node.getTop() * frequency.get(classIndex) + aMarkList.getHeight()) / (frequency.get(classIndex) + 1)));
                    node.setWidth(((node.getTop() * frequency.get(classIndex) + aMarkList.getWidth()) / (frequency.get(classIndex) + 1)));
                    recNodeArrayList.get(classIndex).setRecNode(node);
                    recNodeArrayList.get(classIndex).getRecNodes().add(aMarkList);

                    frequency.set(classIndex, frequency.get(classIndex) + 1);
                } else { // 假设不成立
                    RecNodeList recNodeList = new RecNodeList();
                    recNodeList.setRecNode(aMarkList);
                    recNodeList.getRecNodes().add(aMarkList);
                    recNodeArrayList.add(recNodeList);
                    frequency.add(1);
                }
            }
        }

        return recNodeArrayList;
    }
}
