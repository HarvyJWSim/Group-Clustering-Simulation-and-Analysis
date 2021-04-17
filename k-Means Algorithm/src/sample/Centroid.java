package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by WEI TAO on 4/18/2017.
 */
public class Centroid{

    private int counterCentroid;

    private double sumDist;
    private double centroidDist;

    private double dist;
    private double minDist;
    private double minDistOfCentroid;
    private int minPos;
    private int minPosOfCentroid;
    private int posOfCentroid;
    private int posOfNewCentroid;

    private List<Double> centroid;

    private List<Double> distList;
    private List<Double> distListOfCentroid;

    private List<List<Double>> centroidList;

    private List<Integer> randomList;
    private Random randomGenerator;
    private int randomNumber;

    Centroid(){
        counterCentroid = 0;

        sumDist = 0;
        dist = 0;
        minDist = Double.MAX_EXPONENT;
        minDistOfCentroid = Double.MAX_EXPONENT;
        minPos = 0;
        minPosOfCentroid = 0;
        posOfCentroid = 0;
        posOfNewCentroid = 0;

        centroid = new ArrayList<Double>();
        distList = new ArrayList<Double>();
        distListOfCentroid = new ArrayList<Double>();
        centroidList = new ArrayList<List<Double>>();

        randomList = new ArrayList<Integer>();
        randomGenerator = new Random();
        randomNumber = 0;
    }

    public void centroidUnsupervisedInitialization(List<List<Double>> dataList, List<List<Double>> centroidList, int numCluster, Euclidean eucl, List<List<Double>> terminationList) {
        boolean checkRandom = true;

        while (centroidList.size() < numCluster) {
            checkRandom = true;

            randomNumber = randomGenerator.nextInt(150);

            if (randomList.contains(randomNumber)){;
                checkRandom = false;
            }

            if (checkRandom) {
                randomList.add(randomNumber);

                centroid = new ArrayList<Double>();
                for (int i = 0; i < dataList.get(randomNumber).size(); i++)
                    centroid.add(dataList.get(randomNumber).get(i));

                centroidList.add(centroid);
                terminationList.add(centroid);
            }
        }
        randomList.clear();
    }

    public void centroidSupervisedInitialization(List<List<Double>> dataList, List<List<Double>> centroidList, int numCluster, Euclidean eucl, List<List<Double>> terminationList){
        for(int i = 0; i < numCluster; i++){
            randomNumber = randomGenerator.nextInt(50) + (i * 50);

            centroid = new ArrayList<Double>();
            for (int j = 0; j < dataList.get(randomNumber).size(); j++)
                centroid.add(dataList.get(randomNumber).get(j));

            centroidList.add(centroid);
            terminationList.add(centroid);
        }
    }

    public void centroidSelfSupervisedInitialization(List<List<Double>> dataList, List<List<Double>> centroidList, int numCluster, Euclidean eucl, List<List<Double>> terminationList){

        // 1. Calculate distance matrix Dist mxm in which dist(X i ,X j ) represents distance from X i to X j .
        // 2. Find Sumv in which Sumv(i) is the sum of the distances from X i th point to all other points.
        for(int i = 0; i < dataList.size(); i++){
            dist = 0;
            for(int j = 0; j < dataList.size(); j++){
                    if(!(dataList.get(i).equals(dataList.get(j)))) {
                        dist += eucl.calcEuclDist(dataList.get(i), dataList.get(j));
                    }
            }

            // 3. Find the index,h of minimum value of Sumv and find highest density point X h .
            if(minDist > dist) {
                minDist = dist;
                minPos = i;
            }
        }

        for(int i = 0; i < dataList.get(minPos).size(); i++)
            centroid.add(dataList.get(minPos).get(i));

            // 4. Add X h  to C as the first centroid.

            centroidList.add(centroid);
            terminationList.add(centroid);
            counterCentroid++;

        while(counterCentroid  - 1 < numCluster - 1) {
            //5. For each point X i , set d (X i ) to be the distance between X i and the nearest point in C.
            // Find nearest point in C.
            for (int i = 0; i < centroidList.get(counterCentroid - 1).size(); i++) {
                for (int j = 0; j < dataList.size(); j++) {
                    dist = 0;

                    if (!(centroidList.get(counterCentroid - 1).equals(dataList.get(j)))) {
                        dist += eucl.calcEuclDist(centroidList.get(counterCentroid - 1), dataList.get(j));

                        if (minDistOfCentroid > dist) {
                            minDistOfCentroid = dist;
                            minPosOfCentroid = j;
                        }
                    }
                }
            }

            // Now the position of nearest point in C is known.
            // For each point X i , set d (X i ) to be the distance between X i and the nearest point in C.

            for (int i = 0; i < dataList.size(); i++) {
                dist = 0;
                dist += eucl.calcEuclDist(dataList.get(minPosOfCentroid), dataList.get(i));
                distListOfCentroid.add(dist);
            }

            for (int i = 0; i < dataList.size(); i++) {
                dist = 0;
                dist += eucl.calcEuclDist(dataList.get(minPos), dataList.get(i));
                distList.add(dist);
            }

            // 6. Find y as the sum of distances of first m/k nearest points from the X h .
            // distList = distance from X h. (C) ..  distListOfCentroid = distance from nearest X h.

            Collections.sort(distList);

            for (int i = 1; i < dataList.size() / numCluster; i++) {
                sumDist += distList.get(i);
            }

            // sumDist = sum of distances of first m/k nearest points from the X h .

            // 7. Find the unique integr i so that
            // 8. d(X 1 ) 2 +d(X 2 ) 2 +...+d(X i ) 2 > = y>d(X 1 ) 2 +d(X 2 ) 2 +...+d(X (i-1) ) 2
            // d(X 1) refers to distListOfCentroid
            for (int i = 0; i < distList.size(); i++) {

                centroidDist += distListOfCentroid.get(i);
                posOfCentroid = i;

                if (centroidDist >= sumDist) {
                    break;
                }
            }

            // 9. Add X i to C
            centroid = new ArrayList<Double>();

            for (int i = 0; i < dataList.get(posOfCentroid).size(); i++)
                centroid.add(dataList.get(posOfCentroid).get(i));

            centroidList.add(centroid);
            terminationList.add(centroid);

            // 10. Repeat steps 5-8 until k centroids are found
            counterCentroid++;

            initialize();
        }
    }

    public void initialize(){
        sumDist = 0;
        centroidDist = 0;

        minDistOfCentroid = Double.MAX_EXPONENT;
        minPosOfCentroid = 0;
        posOfCentroid = 0;
        distList.clear();
        distListOfCentroid.clear();
    }

    public void centroidAnalysis(List<List<Double>> dataList, List<Integer> clusterList, int numCluster){

        int numClusterPoint = 0;

        centroidList.clear();

        for(int i = 1; i <= numCluster; i++){
            centroid = new ArrayList<Double>();
            numClusterPoint = 0;
            for(int j = 0; j < clusterList.size(); j++){
                if(clusterList.get(j) == i) {
                    for (int k = 0; k < dataList.get(j).size(); k++) {
                        if(!(centroid.size() == dataList.get(j).size()))
                            centroid.add(dataList.get(j).get(k));
                        else
                            centroid.set(k, centroid.get(k) + dataList.get(j).get(k));
                    }
                    numClusterPoint++;
                }
            }

            for(int j = 0; j < centroid.size(); j++)
                centroid.set(j, centroid.get(j) / numClusterPoint);

            centroidList.add(centroid);
        }
    }

    public List<List<Double>> getCentroidList(){
        return centroidList;
    }
}
