package cn.joker.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: pis
 * @description: 在图片整合过程中用到的临时对象，把标注信息单独提取出来
 * @date: create in 19:40 2018/5/27
 */
public class RecNodeList {
    private List<RecNode> recNodes = new ArrayList<>();

    private RecNode recNode;

    public RecNode getRecNode() {
        return recNode;
    }

    public void setRecNode(RecNode recNode) {
        this.recNode = recNode;
    }

    public List<RecNode> getRecNodes() {

        return recNodes;
    }

    public void setRecNodes(List<RecNode> recNodes) {
        this.recNodes = recNodes;
    }
}
