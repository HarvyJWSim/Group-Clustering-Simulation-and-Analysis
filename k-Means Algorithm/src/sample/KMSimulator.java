package sample;

import javafx.scene.control.TextArea;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WEI TAO on 4/18/2017.
 */
public class KMSimulator {

    private int terminate;

    private String clusterName;
    private int totalNumOfCluster;

    private List<List<Double>> dataList;
    private List<List<Double>> centroidList;
    private List<List<Double>> terminationList;
    private List<Double> temp;

    private List<String> dataClusterList;
    private List<Integer> clusterList;
    private List<Integer> majorClusterList;

    private DataFile data;
    private Centroid centrd;
    private Euclidean eucl;
    private DecimalFormat df;

    private int numCorrect;
    private int numWrong;
    private int numTotal;
    private int totalNumCorrect;
    private int totalNumWrong;
    private int totalNum;
    private int stepIterate;

    private double correctPercentage;
    private double wrongPercentage;
    private double totalPercentage;

    KMSimulator(){
        terminate = 0;


        clusterName = "";
        totalNumOfCluster = 0;

        dataList = new ArrayList<List<Double>>();
        centroidList = new ArrayList<List<Double>>();
        terminationList = new ArrayList<List<Double>>();

        majorClusterList = new ArrayList<Integer>();
        dataClusterList = new ArrayList<String>();
        clusterList = new ArrayList<Integer>();

        data = new DataFile();
        centrd = new Centroid();
        eucl = new Euclidean();
        df = new DecimalFormat();

        numCorrect = 0;
        numWrong = 0;
        numTotal = 0;
        totalNumCorrect = 0;
        totalNumWrong = 0;
        totalNum = 0;
        stepIterate = 0;

        correctPercentage = 0;
        wrongPercentage = 0;
        totalPercentage = 0;
    }

    public void simulate(TextArea resultOne, TextArea resultOneSummary, TextArea resultTwo, TextArea resultTwoSummary,
                          TextArea resultThree, TextArea resultThreeSummary, TextArea resultSummary, TextArea resultCluster, FileWriter writer, int num) {

        data.readFile(dataList, dataClusterList);

        // Find the total number of cluster.
        for(int i = 0; i < dataClusterList.size(); i++){
            if(!(clusterName.equals(dataClusterList.get(i))))
                totalNumOfCluster++;
            clusterName = dataClusterList.get(i);
        }

        // Step2 : Initialization. Generate 3 cluster points.
        centrd.centroidSupervisedInitialization(dataList, centroidList, totalNumOfCluster, eucl, terminationList);

        while(terminate < 2) {
            // Now i got centroidList with 3 centroid points.
            eucl.clusterAnalysis(dataList, centroidList);

            // now having clustered List
            clusterList = eucl.getClusterList();

            centrd.centroidAnalysis(dataList, clusterList, totalNumOfCluster);

            centroidList = centrd.getCentroidList();

            if (terminationList.equals(centroidList))
                terminate++;
            else
                terminate = 0;

            // deep copy of terminationList from centroidList
            for(int i = 0; i < centroidList.size(); i++){
                for(int j = 0; j < centroidList.get(i).size(); j++){
                        terminationList.get(i).set(j, centroidList.get(i).get(j));
                }
            }
            stepIterate++;
        }

        df.setMaximumFractionDigits(2);

        int clusterOne = 0;
        int clusterTwo = 0;
        int clusterThree = 0;

        for(int i = 0; i < clusterList.size(); i++){
            if(clusterList.get(i) == 1){
                clusterOne++;
            }
            if(clusterList.get(i) == 2){
                clusterTwo++;
            }
            if(clusterList.get(i) == 3){
                clusterThree++;
            }
        }

        System.out.println("Cluster 1: " + clusterOne);
        System.out.println("Cluster 2: " + clusterTwo);
        System.out.println("Cluster 3: " + clusterThree);

        resultCluster.appendText("Cluster 1: " + clusterOne + '\n');
        resultCluster.appendText("Cluster 2: " + clusterTwo + '\n');
        resultCluster.appendText("Cluster 3: " + clusterThree + '\n');


        for(int i  = 0; i < totalNumOfCluster; i++){
            int majorCluster = 0;
            numCorrect = 0;
            numWrong = 0;
            numTotal = 0;


            majorCluster = getCluster(clusterList, clusterList.size() / totalNumOfCluster * i);
            for (int j = clusterList.size() / totalNumOfCluster * i; j < dataClusterList.size() / totalNumOfCluster * (i + 1); j++) {

                if(majorCluster == clusterList.get(j)){
                    System.out.println(dataList.get(j) + "  Standard Cluster = " + dataClusterList.get(j) + "  Kmean Cluster = Correct");

                    if(i == 0)
                        resultOne.appendText(dataList.get(j) + "  Standard Cluster = " + dataClusterList.get(j) + "  Kmean Cluster = Correct" + '\n');
                    if(i == 1)
                        resultTwo.appendText(dataList.get(j) + "  Standard Cluster = " + dataClusterList.get(j) + "  Kmean Cluster = Correct" + '\n');
                    if(i == 2)
                        resultThree.appendText(dataList.get(j) + "  Standard Cluster = " + dataClusterList.get(j) + "  Kmean Cluster = Correct" + '\n');

                    numCorrect++;

                }
                else {
                    System.out.println(dataList.get(j) + "  Standard Cluster = " + dataClusterList.get(j) + "  Kmean Cluster = Wrong");

                    if(i == 0)
                        resultOne.appendText(dataList.get(j) + "  Standard Cluster = " + dataClusterList.get(j) + "  Kmean Cluster = Wrong" + '\n');
                    if(i == 1)
                        resultTwo.appendText(dataList.get(j) + "  Standard Cluster = " + dataClusterList.get(j) + "  Kmean Cluster = Wrong" + '\n');
                    if(i == 2)
                        resultThree.appendText(dataList.get(j) + "  Standard Cluster = " + dataClusterList.get(j) + "  Kmean Cluster = Wrong" + '\n');

                    numWrong++;
                }
                numTotal++;
            }

            correctPercentage = (double)numCorrect / (double)numTotal * 100;
            wrongPercentage = (double)numWrong / (double)numTotal * 100;

            totalNumCorrect += numCorrect;
            totalNumWrong += numWrong;
            totalNum += numTotal;

            System.out.println("Cluster " + (i+1));
            System.out.println("Correct = " + numCorrect + " / " + numTotal);
            System.out.println("        = " + df.format(correctPercentage) + " %");
            System.out.println("Wrong   = " + numWrong + " / " + numTotal);
            System.out.println("        = " + df.format(wrongPercentage) + " %");

            if(i == 0) {
                resultOneSummary.appendText("Correct = " + numCorrect + " / " + numTotal + '\n');
                resultOneSummary.appendText("        = " + df.format(correctPercentage) + " %" + '\n');
                resultOneSummary.appendText("Wrong   = " + numWrong + " / " + numTotal + '\n');
                resultOneSummary.appendText("        = " + df.format(wrongPercentage) + " %" + '\n');
            }
            if(i == 1) {
                resultTwoSummary.appendText("Correct = " + numCorrect + " / " + numTotal + '\n');
                resultTwoSummary.appendText("        = " + df.format(correctPercentage) + " %" + '\n');
                resultTwoSummary.appendText("Wrong   = " + numWrong + " / " + numTotal + '\n');
                resultTwoSummary.appendText("        = " + df.format(wrongPercentage) + " %" + '\n');
            }
            if(i == 2) {
                resultThreeSummary.appendText("Correct = " + numCorrect + " / " + numTotal + '\n');
                resultThreeSummary.appendText("        = " + df.format(correctPercentage) + " %" + '\n');
                resultThreeSummary.appendText("Wrong   = " + numWrong + " / " + numTotal + '\n');
                resultThreeSummary.appendText("        = " + df.format(wrongPercentage) + " %" + '\n');
            }
        }

        correctPercentage = (double)totalNumCorrect / (double)totalNum * 100;
        wrongPercentage = (double)totalNumWrong / (double)totalNum * 100;

        System.out.println("Total Correct = " + totalNumCorrect + " / "  + totalNum + '\n');
        System.out.println("              = " + df.format(correctPercentage) + " %" + '\n');
        System.out.println("Total Wrong   = " + totalNumWrong + " / "  + totalNum + '\n');
        System.out.println("              = " + df.format(wrongPercentage) + " %" + '\n');

        resultSummary.appendText("Total Correct = " + totalNumCorrect + " / "  + totalNum + '\n');
        resultSummary.appendText("              = " + df.format(correctPercentage) + " %" + '\n');
        resultSummary.appendText("Total Wrong   = " + totalNumWrong + " / "  + totalNum + '\n');
        resultSummary.appendText("              = " + df.format(wrongPercentage) + " %" + '\n');
        resultSummary.appendText("Step Iterated = " + (stepIterate - 4));

        try {
            writer.append(Integer.toString(num));
            writer.append(',');
            writer.append(Integer.toString(stepIterate - 1));
            writer.append(',');
            writer.append(Double.toString(correctPercentage));
            writer.append(',');
            writer.append(Double.toString(wrongPercentage));
            writer.append(',');
            writer.append('\n');
        }catch(IOException e){
            e.printStackTrace();
        }
        initialize();

    }

    private void initialize(){
        numCorrect = 0;
        numWrong = 0;
        numTotal = 0;
        totalNumCorrect = 0;
        totalNumWrong = 0;
        totalNum = 0;
        stepIterate = 0;

        correctPercentage = 0;
        wrongPercentage = 0;
        totalPercentage = 0;
    }

    private int getCluster(List<Integer> clusterList, int initial){
        int clusterOne = 0;
        int clusterTwo = 0;
        int clusterThree = 0;
        int maxCluster = 0;
        int cluster = 0;

        for(int i = initial; i < initial + clusterList.size()/totalNumOfCluster; i++) {
            if(clusterList.get(i) == 1)
                clusterOne++;
            if(clusterList.get(i) == 2)
                clusterTwo++;
            if(clusterList.get(i) == 3)
                clusterThree++;
        }

        maxCluster = clusterOne;
        cluster = 1;

        if(clusterTwo > maxCluster) {
            maxCluster = clusterTwo;
            cluster = 2;
        }

        if(clusterThree > maxCluster) {
            maxCluster = clusterThree;
            cluster = 3;
        }

        if(majorClusterList.contains(cluster)){
            if(!(majorClusterList.contains(1))){
                cluster = 1;
            }
            if(!(majorClusterList.contains(2))){
                cluster = 2;
            }
            if(!(majorClusterList.contains(3))){
                cluster = 3;
            }
        }

        majorClusterList.add(cluster);

        return cluster;
    }
}

