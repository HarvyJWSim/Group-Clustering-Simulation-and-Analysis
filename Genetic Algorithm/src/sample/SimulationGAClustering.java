package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by HP on 22/4/2017.
 */
public class SimulationGAClustering
{
    final static int POP_SIZE = 128;
    boolean childrenAccepted;
    int time;

    FlowerSample flowerSample;
    GeneticOperation geneticOperation;
    List<FlowerSample> sampleCollection;
    BinaryDecimalConverter binaryDecimalConverter;
    List<Individual> population, childPopulation;
    List<Double> penalty, subPenalty;
    List<Integer> bestParents, worstParents;
    List<String> children, classLabel, classLabelList;
    List<Double> accuracyList, penaltyList;
    List<Integer> featurePrioritisedCount;

    public SimulationGAClustering()
    {
        binaryDecimalConverter = new BinaryDecimalConverter();
        sampleCollection = new ArrayList<FlowerSample>();
        geneticOperation = new GeneticOperation();
        population = new ArrayList<Individual>();
        childPopulation = new ArrayList<Individual>();
        penalty = new ArrayList<Double>();
        subPenalty = new ArrayList<Double>();
        bestParents = new ArrayList<Integer>();
        worstParents = new ArrayList<Integer>();
        children = new ArrayList<String>();
        classLabel = new ArrayList<String>();
        classLabelList = new ArrayList<String>();
        accuracyList = new ArrayList<Double>();
        penaltyList = new ArrayList<Double>();
        featurePrioritisedCount = new ArrayList<Integer>();
        readDataFile();
        simulateGA();
    }

    public void readDataFile()
    {
        String lineText = "";
        File file = new File("iris.data");
        try
        {
            Scanner scan = new Scanner(file);
            while(scan.hasNext())
            {
                lineText = scan.next();
                String[] lineList = lineText.split(","); // Get only integer and store in string list.
                    flowerSample = new FlowerSample(Double.parseDouble(lineList[0]), Double.parseDouble(lineList[1]),
                            Double.parseDouble(lineList[2]), Double.parseDouble(lineList[3]), lineList[4]);
                // Add every integer from string list.

                // Add into the main arraylist.
                sampleCollection.add(flowerSample);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void simulateGA()
    {
        initialisePop();
        time = 0;
        while (time < 10000)
        {
            System.out.println();
            System.out.println("Generation " + (time + 1));
            penalty.clear();
            for (int i = 0; i < population.size(); i++)
            {
                population.get(i).buildDataPoint(sampleCollection);
                population.get(i).assignCluster();
                population.get(i).recomputeCentroid();

                penalty.add(population.get(i).calcSumOfError());
            }
            selectParent();
            classLabel = population.get(bestParents.get(1)).generateClassLabel(sampleCollection);
            classLabelList = population.get(bestParents.get(1)).generateClassLabelList();
            printCurrBestChromo();
            printBestCentroidList();
            printFeaturePrioritised();
            printLowestPenalty();
            printClassLabelList();
            printAccuracy();
            childrenAccepted = false;
            while (childrenAccepted == false)
            {
                children = geneticOperation.generateChildren(population.get(bestParents.get(0)).getChromosome(),
                        population.get(bestParents.get(1)).getChromosome());
                childPopulation.clear();
                for (String i : children)
                    childPopulation.add(new Individual(i));
                subPenalty.clear();

                for (int i = 0; i < childPopulation.size(); i++) {
                    childPopulation.get(i).buildDataPoint(sampleCollection);
                    childPopulation.get(i).assignCluster();
                    childPopulation.get(i).recomputeCentroid();
                    subPenalty.add(childPopulation.get(i).calcSumOfError());
                }
                if (subPenalty.get(0) < 500)
                    childrenAccepted = true;
            }
            if (subPenalty.get(0) < penalty.get(worstParents.get(0)) &&
                    subPenalty.get(1) < penalty.get(worstParents.get(0)))
            {
                population.get(worstParents.get(0)).replaceIndividual(childPopulation.get(0).getChromosome());
                population.get(worstParents.get(1)).replaceIndividual(childPopulation.get(1).getChromosome());
            }
            else if (subPenalty.get(0) < penalty.get(worstParents.get(1)))
                population.get(worstParents.get(1)).replaceIndividual(childPopulation.get(0).getChromosome());
            else if (subPenalty.get(1) < penalty.get(worstParents.get(1)))
                population.get(worstParents.get(1)).replaceIndividual(childPopulation.get(1).getChromosome());

            time++;
        }
        System.out.println();
        System.out.println(" --- Summary ---");
        printCurrBestChromo();
        printBestCentroidList();
        printFeaturePrioritised();
        printLowestPenalty();
        printClassLabelList();
        printAccuracy();
        printXYPoint();
        printAccuracyPenaltyList();
        printFeaturePrioritisedCountList();
    }

    public void initialisePop()
    {
        String chromosome;
        for (int i = 0; i < POP_SIZE; i++)
        {
            chromosome = "";
            for (int j = 0; j < 6; j++)
                chromosome += binaryDecimalConverter.convertDecimalToBinary(Math.random() * 8);
            for (int k = 0; k < 4; k++)
                if (Math.random() < 0.5)
                    chromosome += '0';
                else
                    chromosome += '1';
            population.add(new Individual(chromosome));
        }
    }

    public void selectParent()
    {
        int temp;
        if (penalty.get(0) > penalty.get(1))
        {
            bestParents.add(0);
            bestParents.add(1);
            worstParents.add(0);
            worstParents.add(1);
        }
        else
        {
            bestParents.add(1);
            bestParents.add(0);
            worstParents.add(1);
            worstParents.add(0);
        }
        for (int i = 2; i < penalty.size(); i++)
        {
            if (penalty.get(i) < penalty.get(bestParents.get(0)))
                bestParents.set(0, i);
            if (penalty.get(bestParents.get(0)) < penalty.get(bestParents.get(1)))
            {
                temp = bestParents.get(0);
                bestParents.set(0, bestParents.get(1));
                bestParents.set(1, temp);
            }
        }
        for (int i = 2; i < penalty.size(); i++)
        {
            if (penalty.get(i) > penalty.get(worstParents.get(0)))
                worstParents.set(0, i);
            if (penalty.get(worstParents.get(0)) > penalty.get(worstParents.get(1)))
            {
                temp = worstParents.get(0);
                worstParents.set(0, worstParents.get(1));
                worstParents.set(1, temp);
            }
        }
    }

    public void printCurrBestChromo()
    {
        System.out.println(" Current Best Chromosome : " + population.get(bestParents.get(1)).getChromosome());
    }

    public void printBestCentroidList()
    {
        for (int i = 0; i < 6; i += 2)
            System.out.print(" -> Centroid : (" + population.get(bestParents.get(1)).getCentroid(i) + ", "
                    + population.get(bestParents.get(1)).getCentroid(i + 1) + ")    ");
        System.out.println();
    }

    public void printFeaturePrioritised()
    {
        int count = 0;
        System.out.print(" -> Prioritising feature : ");
        if(population.get(bestParents.get(1)).getSepalLengthIsPrioritised())
        {
            count++;
            System.out.print("Sepal Length   ");
        }
        if(population.get(bestParents.get(1)).getSepalWidthIsPrioritised())
        {
            count++;
            System.out.print("Sepal Width   ");
        }
        if(population.get(bestParents.get(1)).getPetalLengthIsPrioritised())
        {
            count++;
            System.out.print("Petal Length   ");
        }
        if(population.get(bestParents.get(1)).getPetalWidthIsPrioritised())
        {
            count++;
            System.out.print("Petal Width   ");
        }
        if ((time % 100) == 0)
            featurePrioritisedCount.add(count);
        System.out.println();
    }

    public void printLowestPenalty()
    {
        System.out.println(" Lowest Penalty Point : " + penalty.get(bestParents.get(1)));
        if ((time % 100) == 0)
            penaltyList.add(penalty.get(bestParents.get(1)));
    }

    public void printClassLabelList()
    {
        System.out.println(" Identified Class Label : " + classLabelList);
    }

    public void printAccuracy()
    {
        double accurateCount = 0;
        for (int i = 0; i < classLabelList.size(); i++)
        {
            if (classLabelList.get(i).equals(sampleCollection.get(i).getClassLabel()))
                accurateCount++;
        }
        System.out.println(" Accuracy : " + ((accurateCount / classLabelList.size()) * 100) + " %");
        if ((time % 100) == 0)
            accuracyList.add((accurateCount / classLabelList.size()) * 100);
    }

    public void printXYPoint()
    {
        System.out.print(" List of points : ");
        for (int i = 0; i < classLabelList.size(); i++)
        {
            System.out.print("(" + population.get(bestParents.get(1)).getXPoint(i) + ", "
                    + population.get(bestParents.get(1)).getYPoint(i) + ") ");
        }
        System.out.println();
    }

    public void printAccuracyPenaltyList()
    {
        System.out.print(" Accuracy list: ");
        System.out.println(accuracyList);
        System.out.print(" Penalty list: ");
        System.out.println(penaltyList);
    }

    public void printFeaturePrioritisedCountList()
    {
        System.out.print(" Feature prioritised count list: ");
        System.out.println(featurePrioritisedCount);
    }
}

