package atse.data;
import java.util.ArrayList;
import java.util.List;

import atse.common.Settings;
public class Cluster {
    private int clusterNo;
    private List<String> wordList;
    public Cluster() {
        wordList = new ArrayList<>();
        clusterNo = Settings.CLUSTER_COUNT++;
    }
    public List<String> getWordList() {
        return wordList;
    }
    public void setWordList(List<String> wordList) {
        this.wordList = wordList;
    }
    public int getClusterNo() {
        return clusterNo;
    }
    public void setClusterNo(int clusterNo) {
        this.clusterNo = clusterNo;
    }
}
