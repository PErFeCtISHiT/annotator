package cn.joker.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: pis
 * @description: good good study
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
