package cn.joker.statisticalmethod;

import cn.joker.pojo.RecNode;
import cn.joker.pojo.RecNodeList;
import cn.joker.pojo.WorkerAnswer;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Segmentation {
    private List<String> words;
    private Map<String, Integer> frequency;
    private String strmax1 = "";
    private String strmax2 = "";
    private static final String DIR = System.getProperty("user.dir") + "/annotator/";

    public Segmentation() {
        words = new ArrayList<>();
        frequency = new HashMap<>();
    }

    public List<WorkerAnswer> segment(RecNodeList recNodeList) {
        List<WorkerAnswer> workerAnswers = new ArrayList<>();

        List<RecNode> recNodes = recNodeList.getRecNodes();

        // 分词结果一个个加到words的list中
        for (RecNode recNode : recNodes) {
            List<Word> temp = WordSegmenter.seg(recNode.getMark());

            for (Word str : temp) {
                words.add(str.getText());
            }
        }

        // 合并同义词，把同义词直接用我们的近义词替换掉
        List<String> synDictionary = loadFile();
        for (String syn : synDictionary) {
            for (int i = 0; i < words.size(); i++) {
                for (int m = 1; m < syn.split(" ").length; m++) {
                    if (words.get(i).equals(syn.split(" ")[m])) {
                        for (RecNode recNode : recNodes) {
                            if (recNode.getMark().contains(words.get(i))) {
                                words.set(i, syn.split(" ")[0]);
                            }
                        }
                    }
                }
            }
        }

        // 统计各个词出现的频率
        countFrequency();

        // 频率最高的是正确答案，第二高的就判为错误答案
        int countmax1 = 0;
        int countmax2 = 0;
        // 得到词频最高的两个词
        if (frequency.keySet().size() == 1) {
            strmax1 = recNodes.get(0).getMark();
        } else {
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
        }

        // 判断词频出现最高的词汇有没有出现在原始标注中
        // 只要和最高频率的词近似就算正确
        for (RecNode recNode : recNodes) {
            WorkerAnswer workerAnswer = new WorkerAnswer();
            workerAnswer.setUserEntity(recNode.getWorker());
            if (recNode.getMark().contains(strmax1)) {
                workerAnswer.setAnswer(true);
                workerAnswers.add(workerAnswer);
            } else if (recNode.getMark().contains(strmax2)) {
                workerAnswer.setAnswer(false);
                workerAnswers.add(workerAnswer);
            }
        }

        return workerAnswers;
    }

    private void countFrequency() {
        for (String str : words) {
            if (!frequency.containsKey(str))
                frequency.put(str, 1);
            else
                frequency.put(str, frequency.get(str) + 1);
        }
    }

    private List<String> loadFile() {
        Logger logger = LoggerFactory.getLogger(Segmentation.class);
        List<String> synDictionary = new ArrayList<>();
        String filename = DIR + "resources/synonym.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String tmp;
            while ((tmp = br.readLine()) != null) {
                synDictionary.add(tmp);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return synDictionary;
    }

    public String getStrmax1() {
        return strmax1;
    }

    public String getStrmax2() {
        return strmax2;
    }
}
