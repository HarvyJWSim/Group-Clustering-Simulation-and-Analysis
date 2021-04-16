package sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 22/4/2017.
 */
public class Individual
{
    final static int PRIORIT_WEIGHT = 2;

    String chromosome, subChromosome;
    double sepalLength, sepalWidth, petalLength, petalWidth;
    boolean sepalLengthIsPrioritised, sepalWidthIsPrioritised, petalLengthIsPrioritised, petalWidthIsPrioritised;
    double xPoint, yPoint, penalty;
    List<Double> centroidList;
    List<DataPoint> dataGraph;
    List<Double> xPointAndCentroid, yPointAndCentroid;
    List<Integer> setosaCount, versiColorCount, virginicaCount;
    List<String> assignedClassLabel, classLabelList;

    BinaryDecimalConverter binaryDecimalConverter;

    public Individual(String chromosome)
    {
        sepalLengthIsPrioritised = sepalWidthIsPrioritised = petalLengthIsPrioritised = petalWidthIsPrioritised = false;
        this.chromosome = chromosome;
        dataGraph = new ArrayList<DataPoint>();
        binaryDecimalConverter = new BinaryDecimalConverter();
        centroidList = new ArrayList<Double>();
        xPointAndCentroid = new ArrayList<Double>();
        yPointAndCentroid = new ArrayList<Double>();
        setosaCount = new ArrayList<Integer>();
        versiColorCount = new ArrayList<Integer>();
        virginicaCount = new ArrayList<Integer>();
        assignedClassLabel = new ArrayList<String>();
        classLabelList = new ArrayList<String>();
        calcCentroid();
    }

    public void buildDataPoint(List<FlowerSample> sampleCollection)
    {
        dataGraph.clear();
        for (int i = 0; i < sampleCollection.size(); i++)
        {
            if (chromosome.charAt(48) == '1')
            {
                sepalLength = PRIORIT_WEIGHT * sampleCollection.get(i).getSepalLength();
                sepalLengthIsPrioritised = true;
            }
            else
                sepalLength = sampleCollection.get(i).getSepalLength();
            if (chromosome.charAt(49) == '1')
            {
                sepalWidth = PRIORIT_WEIGHT * sampleCollection.get(i).getSepalWidth();
                sepalWidthIsPrioritised = true;
            }
            else
                sepalWidth = sampleCollection.get(i).getSepalWidth();
            if (chromosome.charAt(50) == '1')
            {
                petalLength = PRIORIT_WEIGHT * sampleCollection.get(i).getPetalLength();
                petalLengthIsPrioritised = true;
            }
            else
                petalLength = sampleCollection.get(i).getPetalLength();
            if (chromosome.charAt(51) == '1')
            {
                petalWidth = PRIORIT_WEIGHT * sampleCollection.get(i).getPetalWidth();
                petalWidthIsPrioritised = true;
            }
            else
                petalWidth = sampleCollection.get(i).getPetalWidth();

            xPoint = sepalLength + sepalWidth;
            yPoint = petalLength + petalWidth;

            if (sepalLengthIsPrioritised == true && sepalWidthIsPrioritised == true &&
                    petalLengthIsPrioritised == true && petalWidthIsPrioritised == true)
            {
                xPoint /= (PRIORIT_WEIGHT * 2);
                yPoint /= (PRIORIT_WEIGHT * 2);
            }
            else if (sepalLengthIsPrioritised == true && sepalWidthIsPrioritised == true)
            {
                xPoint = sepalLength + petalLength;
                yPoint = sepalWidth + petalWidth;
                if (petalLengthIsPrioritised == true)
                    xPoint /= (PRIORIT_WEIGHT * 2);
                else
                    xPoint /= (1 + PRIORIT_WEIGHT);
                if (petalWidthIsPrioritised == true)
                    yPoint /= (PRIORIT_WEIGHT * 2);
                else
                    yPoint /= (1 + PRIORIT_WEIGHT);
            }
            else if (petalLengthIsPrioritised == true && petalWidthIsPrioritised == true)
            {
                xPoint = petalLength + sepalLength;
                yPoint = petalWidth + sepalWidth;
                if (sepalLengthIsPrioritised == true)
                    xPoint /= (PRIORIT_WEIGHT * 2);
                else
                    xPoint /= (1 + PRIORIT_WEIGHT);
                if (sepalWidthIsPrioritised == true)
                    yPoint /= (PRIORIT_WEIGHT * 2);
                else
                    yPoint /= (1 + PRIORIT_WEIGHT);
            }
            else
            {
                if (sepalLengthIsPrioritised == true || sepalWidthIsPrioritised == true)
                    xPoint /= (1 + PRIORIT_WEIGHT);
                else
                    xPoint /= 2;
                if (petalLengthIsPrioritised == true || petalWidthIsPrioritised == true)
                    yPoint /= (1 + PRIORIT_WEIGHT);
                else
                    yPoint /= 2;
            }
            dataGraph.add(new DataPoint(xPoint, yPoint));
        }

    }

    public void calcCentroid()
    {

        String binary, subBinary;
        for (int i = 0; i < 48; i += 8)
        {
            binary = chromosome.substring(i, i + 4);
            subBinary = chromosome.substring(i + 4, i + 8);
            centroidList.add((binaryDecimalConverter.convertBinaryToDecimal(binary)) +
                    (binaryDecimalConverter.convertBinaryToDecimal(subBinary) / 10));
        }
    }

    public void assignCluster()
    {
        int assignedCluster = 0;
        double minEuclideanDist = Double.MAX_VALUE;
        for (int i = 0; i < dataGraph.size(); i++)
        {
            for (int j = 0; j < 6; j += 2)
            {
                if (minEuclideanDist > calcEuclideanDist(centroidList.get(j), centroidList.get(j + 1),
                    dataGraph.get(i).getXPoint(), dataGraph.get(i).getYPoint()))
                {
                    minEuclideanDist = calcEuclideanDist(centroidList.get(j), centroidList.get(j + 1),
                            dataGraph.get(i).getXPoint(), dataGraph.get(i).getYPoint());

                    assignedCluster = j;
                }
                dataGraph.get(i).setAssignedCluster(assignedCluster);
            }
        }
    }

    public double calcSumOfError()
    {
        boolean containCOne, containCTwo, containCThree;
        containCOne = containCTwo = containCThree = false;
        penalty = 0;
        for (int i = 0; i < dataGraph.size(); i++)
        {
            if (dataGraph.get(i).getAssignedCluster() == 0)
            {
                penalty += calcEuclideanDist(centroidList.get(0), centroidList.get(1), dataGraph.get(i).getXPoint(),
                        dataGraph.get(i).getYPoint());
            }
            else if (dataGraph.get(i).getAssignedCluster() == 2)
            {
                penalty += calcEuclideanDist(centroidList.get(2), centroidList.get(3), dataGraph.get(i).getXPoint(),
                        dataGraph.get(i).getYPoint());
            }
            else
            {
                penalty += calcEuclideanDist(centroidList.get(2), centroidList.get(3), dataGraph.get(i).getXPoint(),
                        dataGraph.get(i).getYPoint());
            }
        }

        for (int i = 0; i < dataGraph.size(); i++)
        {
            if (dataGraph.get(i).getAssignedCluster() == 0)
                containCOne = true;
            else if(dataGraph.get(i).getAssignedCluster() == 2)
                containCTwo = true;
            else
                containCThree = true;
        }
        if (containCOne == false || containCTwo == false || containCThree == false)
        {
            penalty += 1000;
        }
        return penalty;
    }

    public double calcEuclideanDist(double centroidX, double centroidY, double xPoint, double yPoint)
    {
        return Math.sqrt(Math.pow((centroidX - xPoint), 2) + Math.pow((centroidY - yPoint), 2));
    }

    public void recomputeCentroid()
    {
        double sumX, sumY;
        subChromosome = chromosome.substring(48, 52);
        chromosome = "";

        for (int i = 0; i < 6; i += 2)            // for every centroid
        {
            xPointAndCentroid.clear();
            yPointAndCentroid.clear();
            xPointAndCentroid.add(centroidList.get(i));
            yPointAndCentroid.add(centroidList.get(i + 1));
            for (int j = 0; j < dataGraph.size(); j++)
            {
                if (dataGraph.get(j).getAssignedCluster() == i)
                {
                    xPointAndCentroid.add(dataGraph.get(j).getXPoint());
                    yPointAndCentroid.add(dataGraph.get(j).getYPoint());
                }
            }
            sumX = sumY = 0;
            for (int k = 0; k < xPointAndCentroid.size(); k++)
            {
                sumX += xPointAndCentroid.get(k);
                sumY += yPointAndCentroid.get(k);
            }
            centroidList.set(i, sumX / xPointAndCentroid.size());
            centroidList.set(i + 1, sumY / yPointAndCentroid.size());
        }
        for (int i = 0; i < centroidList.size(); i++)
        {
            chromosome += binaryDecimalConverter.convertDecimalToBinary(centroidList.get(i));
        }
        chromosome += subChromosome;
    }

    public void replaceIndividual(String chromosome)
    {
        this.chromosome = chromosome;
        sepalLengthIsPrioritised = sepalWidthIsPrioritised = petalLengthIsPrioritised = petalWidthIsPrioritised = false;
        dataGraph = new ArrayList<DataPoint>();
        centroidList = new ArrayList<Double>();
        xPointAndCentroid = new ArrayList<Double>();
        yPointAndCentroid = new ArrayList<Double>();
        calcCentroid();
    }

    public List<String> generateClassLabel(List<FlowerSample> sampleCollection)
    {
        assignedClassLabel.clear();
        setosaCount.clear();
        versiColorCount.clear();
        virginicaCount.clear();
        for (int i = 0; i < 3; i++)
        {
            setosaCount.add(0);
            versiColorCount.add(0);
            virginicaCount.add(0);
        }
        for (int i = 0; i < dataGraph.size(); i++)
        {
            if (dataGraph.get(i).getAssignedCluster() == 0)
            {
                if (sampleCollection.get(i).getClassLabel() == "Iris-setosa")
                    setosaCount.set(0, setosaCount.get(0) + 1);
                else if (sampleCollection.get(i).getClassLabel() == "Iris-versicolor")
                    versiColorCount.set(0, versiColorCount.get(0) + 1);
                else
                    virginicaCount.set(0, virginicaCount.get(0) + 1);
            }
            else if (dataGraph.get(i).getAssignedCluster() == 2)
            {
                if (sampleCollection.get(i).getClassLabel() == "Iris-setosa")
                    setosaCount.set(1, setosaCount.get(1) + 1);
                else if (sampleCollection.get(i).getClassLabel() == "Iris-versicolor")
                    versiColorCount.set(1, versiColorCount.get(1) + 1);
                else
                    virginicaCount.set(1, virginicaCount.get(1) + 1);
            }
            else
            {
                if (sampleCollection.get(i).getClassLabel() == "Iris-setosa")
                    setosaCount.set(2, setosaCount.get(2) + 1);
                else if (sampleCollection.get(i).getClassLabel() == "Iris-versicolor")
                    versiColorCount.set(2, versiColorCount.get(2) + 1);
                else
                    virginicaCount.set(2, virginicaCount.get(2) + 1);
            }
        }
        for (int i = 0; i < 3; i++)
        {
            if (setosaCount.get(i) >= versiColorCount.get(i) && setosaCount.get(i) >= virginicaCount.get(i))
                assignedClassLabel.add("Iris-setosa");
            else if (versiColorCount.get(i) >= setosaCount.get(i) && versiColorCount.get(i) >= virginicaCount.get(i))
                assignedClassLabel.add("Iris-versicolor");
            else
                assignedClassLabel.add("Iris-virginica");
        }
        return assignedClassLabel;
    }

    public List<String> generateClassLabelList()
    {
        classLabelList.clear();
        for (int i = 0; i < dataGraph.size(); i++)
        {
                if (dataGraph.get(i).getAssignedCluster() == 0)
                classLabelList.add("Iris-setosa");
            else if (dataGraph.get(i).getAssignedCluster() == 2)
                classLabelList.add("Iris-versicolor");
            else
                classLabelList.add("Iris-virginica");
        }
        return classLabelList;
    }

    public double getXPoint(int index)
    {
        return dataGraph.get(index).getXPoint();
    }

    public double getYPoint(int index)
    {
        return dataGraph.get(index).getYPoint();
    }

    public double getCentroid(int index)
    {
        return centroidList.get(index);
    }

    public String getChromosome()
    {
        return chromosome;
    }

    public boolean getSepalLengthIsPrioritised()
    {
        return sepalLengthIsPrioritised;
    }

    public boolean getSepalWidthIsPrioritised()
    {
        return sepalWidthIsPrioritised;
    }

    public boolean getPetalLengthIsPrioritised()
    {
        return petalLengthIsPrioritised;
    }

    public boolean getPetalWidthIsPrioritised()
    {
        return petalWidthIsPrioritised;
    }
}
