package cn.joker.statisticalmethod;

import cn.joker.entity.ImgMarkEntity;
import cn.joker.namespace.StdName;
import cn.joker.vo.RecNode;
import cn.joker.vo.RecNodeList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NaiveBayesianClassification {
    private static double MIN_OFFSET = 1.2;
    private static List<RecNode> resultList = new ArrayList<>();
    private static List<Integer> userID = new ArrayList<>();
    private static List<Integer> startIndex = new ArrayList<>();

    NaiveBayesianClassification(){
        throw new IllegalStateException(StdName.UTILCLASS);
    }

    /**
     * 得到所有的标注结果
     * @param imgMarkEntityList 图片信息
     * @return 标注结果的list
     */
    public static List<RecNode> getAllMark(List<ImgMarkEntity> imgMarkEntityList) {
        ArrayList<RecNode> markList = new ArrayList<>(); //把重复标注的结果单独取出来

        for (ImgMarkEntity aImageEntity : imgMarkEntityList) {
            String nodeRec = aImageEntity.getNoteRectangle();
            JSONArray jsonArray = new JSONArray(nodeRec);
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                RecNode recNode = new RecNode(jsonObject.getDouble(StdName.TOP), jsonObject.getDouble(StdName.LEFT),
                        jsonObject.getDouble(StdName.HEIGHT), jsonObject.getDouble(StdName.WIDTH), jsonObject.getString(StdName.MARK), aImageEntity.getWorker());
                markList.add(recNode);
            }
        }

        return markList;
    }

    //对答案进行排序，对用户的答案进行分类（只是排序），结果存在resultList，userID，startIndex中
    private static void classificationByMarkedUser(List<RecNode> markList){
        // 把标注根据用户进行分类，最后还是一个list，但是调整顺序，提供一个用户id的list和一个该用户开始的index的list
        resultList = new ArrayList<>();
        userID = new ArrayList<>();
        startIndex = new ArrayList<>();

        for (RecNode recNode: markList) {
            if(userID.contains(recNode.getWorker().getId())){
                int index = userID.indexOf(recNode.getWorker().getId());
                //先把该标注放到结果集中
                resultList.add(index, recNode);
                //维护用户开始的index相关的list
                for(int i = index+1; i < startIndex.size(); i++){
                    startIndex.set(i, startIndex.get(i) + 1);
                }
            }
            else { //该用户标注的结果本来不在userID的list里面
                startIndex.add(resultList.size());
                resultList.add(recNode);
                userID.add(recNode.getWorker().getId());
            }
        }
    }

    //进行聚类，会先对答案进行排序（调用上一个方法）
    public static List<RecNodeList> getRecMarkByClass(List<RecNode> markList){
        classificationByMarkedUser(markList);

        //整合之后的标注集合
        ArrayList<RecNodeList> classificationList = new ArrayList<>();
        // 单独的频率数组，方便后期按权重调整相应数据
        ArrayList<Integer> frequency = new ArrayList<>();
        //先把第一个用户的标注结果作为最初分类
        for(int i = 0; i < startIndex.get(1); i++){
            RecNodeList recNodeList = new RecNodeList();
            recNodeList.getRecNodes().add(resultList.get(i));
            recNodeList.setRecNode(resultList.get(i));
            classificationList.add(recNodeList);
            frequency.add(1);
        }
        //后续的用户标注结果就和第一个进行对比
        for(int i = startIndex.get(1); i < resultList.size(); i++){
            RecNode aMark = resultList.get(i);
            double minOffset = 1000; // 初始化的时候要把最小值变到极大，否则每次比较都不满足
            int classIndex = 0; // 记录最小偏移量对应的值对应在recNodeArrayList中的位置
            for (int j = 0; j < classificationList.size(); j++) {
                // 计算和每个具体分类的差异，用差异来表示概率，差异越大，表明在此分类中的概率越小
                double offset = calculateOffset(classificationList.get(j).getRecNode(), aMark);

                if (offset < minOffset) { //选择
                    minOffset = offset;
                    classIndex = j;
                }
            }

            // 得到差异最小的数据之后进行差异量和偏移范围的比较
            // 如果假设成立，在偏移范围之内就把该结果放入相应分类中，调整该分类中的数据
            // 如果假设不成立，就单独建立一个新的类
            if (minOffset < MIN_OFFSET) { // 假设成立
                RecNode node = new RecNode(classificationList.get(classIndex).getRecNode().getTop(), classificationList.get(classIndex).getRecNode().getLeft(),
                        classificationList.get(classIndex).getRecNode().getHeight(), classificationList.get(classIndex).getRecNode().getWidth(),
                        classificationList.get(classIndex).getRecNode().getMark(), null);
                // 加权调整数据
                node.setTop(((node.getTop() * frequency.get(classIndex) + aMark.getTop()) / (frequency.get(classIndex) + 1)));
                node.setLeft(((node.getLeft() * frequency.get(classIndex) + aMark.getLeft()) / (frequency.get(classIndex) + 1)));
                node.setHeight(((node.getTop() * frequency.get(classIndex) + aMark.getHeight()) / (frequency.get(classIndex) + 1)));
                node.setWidth(((node.getTop() * frequency.get(classIndex) + aMark.getWidth()) / (frequency.get(classIndex) + 1)));
                classificationList.get(classIndex).setRecNode(node);
                classificationList.get(classIndex).getRecNodes().add(aMark);

                frequency.set(classIndex, frequency.get(classIndex) + 1);
            } else { // 假设不成立
                RecNodeList recNodeList = new RecNodeList();
                recNodeList.setRecNode(aMark);
                recNodeList.getRecNodes().add(aMark);
                classificationList.add(recNodeList);
                frequency.add(1);
            }
        }

        return classificationList;
    }

    private static double calculateOffset(RecNode standardNode, RecNode toTestNode){
        double offset = 0.0;
        offset += Math.abs(toTestNode.getTop() - standardNode.getTop()) / standardNode.getTop();
        offset += Math.abs(toTestNode.getLeft() - standardNode.getLeft()) / standardNode.getLeft();
        offset += Math.abs(toTestNode.getWidth() - standardNode.getWidth()) / standardNode.getWidth();
        offset += Math.abs(toTestNode.getHeight() - standardNode.getHeight()) / standardNode.getHeight();

        return offset;
    }

    public static List<Boolean> getCorrectNumber(List<RecNode> testMark, List<RecNode> markList){
        //对之前做过的测试结果进行分类
        List<RecNodeList> classificationList = getRecMarkByClass(markList);

        List<Boolean> correct = new ArrayList<>();

        List<Boolean> match = new ArrayList<>();
        for(int i = 0; i < classificationList.size(); i++){
            match.add(false);
        }
        // 判断当前做的标注是否在已有的类中
        for (RecNode aMark : testMark) {
            double minOffset = 1000; // 初始化的时候要把最小值变到极大，否则每次比较都不满足
            int classIndex = 0; // 记录最小偏移量对应的值对应在recNodeArrayList中的位置
            for (int j = 0; j < classificationList.size(); j++) {
                if (!match.get(j)) {
                    // 计算和每个具体分类的差异，用差异来表示概率，差异越大，表明在此分类中的概率越小
                    double offset = calculateOffset(classificationList.get(j).getRecNode(), aMark);

                    if (offset < minOffset) { //选择
                        minOffset = offset;
                        classIndex = j;
                    }
                }
            }
            // 如果在的话表示标注成功+1,更新list
            if (minOffset < MIN_OFFSET) {
                correct.add(true);
                match.set(classIndex, true);
            }
            else
                correct.add(false);
        }

        return correct;
    }

    public static List<RecNodeList> integration(List<ImgMarkEntity> imgMarkEntityList) {
        List<RecNode> markList = getAllMark(imgMarkEntityList);

        return getRecMarkByClass(markList);
    }

//    /**
//     * @param markList 对于某张图片所有的长方形标注数据
//     * @return 整合之后的对单张图片的所有标注信息
//     */
//    private static ArrayList<RecNodeList> getRecMarkByClass(List<RecNode> markList) {
//        ArrayList<RecNodeList> recNodeArrayList = new ArrayList<>(); //整合之后的结果
//        ArrayList<Integer> frequency = new ArrayList<>(); // 单独的频率数组，方便后期按权重调整相应数据
//
//        for (RecNode aMarkList : markList) {
//            if (recNodeArrayList.isEmpty()) { // 对于第一个标注结果，默认直接加进分类中
//                RecNodeList recNodeList = new RecNodeList();
//                recNodeList.getRecNodes().add(aMarkList);
//                recNodeList.setRecNode(aMarkList);
//                recNodeArrayList.add(recNodeList);
//                frequency.add(1);
//            } else {
//                double minOffset = 1000; // 初始化的时候要把最小值变到极大，否则每次比较都不满足
//                int classIndex = 0; // 记录最小偏移量对应的值对应在recNodeArrayList中的位置
//                for (int j = 0; j < recNodeArrayList.size(); j++) {
//                    // 计算和每个具体分类的差异，用差异来表示概率，差异越大，表明在此分类中的概率越小
//                    double offset = 0.0;
//                    offset += Math.abs(aMarkList.getTop() - recNodeArrayList.get(j).getRecNode().getTop()) / recNodeArrayList.get(j).getRecNode().getTop();
//                    offset += Math.abs(aMarkList.getLeft() - recNodeArrayList.get(j).getRecNode().getLeft()) / recNodeArrayList.get(j).getRecNode().getLeft();
//                    offset += Math.abs(aMarkList.getWidth() - recNodeArrayList.get(j).getRecNode().getWidth()) / recNodeArrayList.get(j).getRecNode().getWidth();
//                    offset += Math.abs(aMarkList.getHeight() - recNodeArrayList.get(j).getRecNode().getHeight()) / recNodeArrayList.get(j).getRecNode().getHeight();
//
//                    if (offset < minOffset) { //选择
//                        minOffset = offset;
//                        classIndex = j;
//                    }
//                }
//
//                // 得到差异最小的数据之后进行差异量和偏移范围的比较
//                // 如果假设成立，在偏移范围之内就把该结果放入相应分类中，调整该分类中的数据
//                // 如果假设不成立，就单独建立一个新的类
//                if (minOffset < MINOFFSET) { // 假设成立
//                    RecNode node = new RecNode(recNodeArrayList.get(classIndex).getRecNode().getTop(), recNodeArrayList.get(classIndex).getRecNode().getLeft(),
//                            recNodeArrayList.get(classIndex).getRecNode().getHeight(), recNodeArrayList.get(classIndex).getRecNode().getWidth(),
//                            recNodeArrayList.get(classIndex).getRecNode().getMark(), null);
//                    // 加权调整数据
//                    node.setTop(((node.getTop() * frequency.get(classIndex) + aMarkList.getTop()) / (frequency.get(classIndex) + 1)));
//                    node.setLeft(((node.getLeft() * frequency.get(classIndex) + aMarkList.getLeft()) / (frequency.get(classIndex) + 1)));
//                    node.setHeight(((node.getTop() * frequency.get(classIndex) + aMarkList.getHeight()) / (frequency.get(classIndex) + 1)));
//                    node.setWidth(((node.getTop() * frequency.get(classIndex) + aMarkList.getWidth()) / (frequency.get(classIndex) + 1)));
//                    recNodeArrayList.get(classIndex).setRecNode(node);
//                    recNodeArrayList.get(classIndex).getRecNodes().add(aMarkList);
//
//                    frequency.set(classIndex, frequency.get(classIndex) + 1);
//                } else { // 假设不成立
//                    RecNodeList recNodeList = new RecNodeList();
//                    recNodeList.setRecNode(aMarkList);
//                    recNodeList.getRecNodes().add(aMarkList);
//                    recNodeArrayList.add(recNodeList);
//                    frequency.add(1);
//                }
//            }
//        }
//
//        return recNodeArrayList;
//    }
}
