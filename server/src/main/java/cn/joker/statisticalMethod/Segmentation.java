package cn.joker.statisticalMethod;

import cn.joker.vo.RecNode;
import cn.joker.vo.RecNodeList;
import cn.joker.vo.WorkerAnswer;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

import java.util.*;

public class Segmentation {
    private List<String> words;
    private Map<String, Integer> frequency;

    public Segmentation(){
        words = new ArrayList<>();
        frequency = new HashMap<>();
    }

    public List<WorkerAnswer> segment(RecNodeList recNodeList){
        List<WorkerAnswer> workerAnswers = new ArrayList<>();

        List<RecNode> recNodes = recNodeList.getRecNodes();

        // 分词结果一个个加到words的list中
        for (RecNode recNode : recNodes) {
            List<Word> temp = WordSegmenter.seg(recNode.getMark());

            for (Word str : temp) {
                words.add(str.getText());
            }
        }

        // 同义词的合并

        // 统计各个词出现的频率
        countFrequency();

        // 频率最高的是正确答案，第二高的就判为错误答案
        String strmax1 = "";
        String strmax2 = "";
        int countmax1 = 0;
        int countmax2 = 0;
        // 得到词频最高的两个词
        if(frequency.keySet().size() == 1){
            strmax1 = recNodes.get(0).getMark();
        }
        else {
            for (String key : frequency.keySet()) {
                if (frequency.get(key) > countmax1) {
                    strmax2 = strmax1;
                    countmax2 = countmax1;
                    strmax1 = key;
                    countmax1 = frequency.get(key);
                } else if (frequency.get(key) > countmax2 && frequency.get(key) <= countmax1) {
                    strmax2 = key;
                    countmax2 = frequency.get(key);
                }
            }
            System.out.println("over");
            System.out.println(strmax1 + " " + countmax1);
            System.out.println(strmax2 + " " + countmax2);
        }

        // 判断词频出现最高的词汇有没有出现在原始标注中
        // 只要和最高频率的词近似就算正确
        for (RecNode recNode : recNodes) {
            WorkerAnswer workerAnswer = new WorkerAnswer();
            workerAnswer.setUserEntity(recNode.getWorker());
            if(recNode.getMark().contains(strmax1)) {
                workerAnswer.setAnswer(true);
                workerAnswers.add(workerAnswer);
            }
            else if(recNode.getMark().contains(strmax2)) {
                workerAnswer.setAnswer(false);
                workerAnswers.add(workerAnswer);
            }
        }

        return workerAnswers;
    }

    private void countFrequency(){
        for (String str: words) {
            if(!frequency.containsKey(str))
                frequency.put(str, 1);
            else
                frequency.put(str, frequency.get(str) + 1);
        }
    }
}
