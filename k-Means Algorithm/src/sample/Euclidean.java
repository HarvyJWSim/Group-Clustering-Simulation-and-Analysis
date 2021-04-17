package sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WEI TAO on 4/18/2017.
 */
public class Euclidean {

    private double minEuclDist;
    private int cluster;
    private List<Integer> clusterList;

    Euclidean(){
        minEuclDist = Double.MAX_EXPONENT;
        cluster = 0;
        clusterList = new ArrayList<Integer>();

    }

    public double calcEuclDist(List<Double> dataPoint1, List<Double> dataPoint2){

        double temp = 0;

        for(int i = 0; i < dataPoint1.size(); i++){
            temp += Math.pow(dataPoint1.get(i) - dataPoint2.get(i), 2);
        }

        temp = Math.sqrt(temp);

        return temp;
    }

    public void clusterAnalysis(List<List<Double>> dataList,List<List<Double>> centroidList){

        double temp = 0;
        clusterList.clear();

        for(int i = 0; i < dataList.size(); i++) {
            minEuclDist = Double.MAX_EXPONENT;
            for (int j = 0; j < centroidList.size(); j++) {

                temp = calcEuclDist(dataList.get(i), centroidList.get(j));

                if (minEuclDist > temp) {
                    minEuclDist = temp;
                    cluster = j + 1;
                }
            }
            clusterList.add(cluster);
        }
    }

    public List<Integer> getClusterList(){
        return clusterList;
    }
}
