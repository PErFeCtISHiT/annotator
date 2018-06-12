package cn.joker.statisticalmethod;

/**
 * @author: pis
 * @description: 工作评估问题模型
 * @date: create in 19:13 2018/5/27
 */
public class QuestionModel {
    private Double p0 = 0.5;
    private Double p1 = 0.5;

    public Double getP0() {
        return p0;
    }

    public void setP0(Double p0) {
        this.p0 = p0;
    }

    public Double getP1() {
        return p1;
    }

    public void setP1(Double p1) {
        this.p1 = p1;
    }

    public void psUpdate(Double gamma, Boolean ans){
        if(ans){
            p0 = gamma * p0 / (gamma * p0 + (1 - gamma) * (1 - p0));
            p1 = 1 - p0;
        }
        else{
            p0 = (1 - gamma) * p0 / ((1 - gamma) * p0 + gamma + (1 - p0));
            p1 = 1 - p0;
        }
    }
}
