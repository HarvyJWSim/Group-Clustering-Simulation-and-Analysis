package sample;

import java.util.ArrayList;

/**
 * Created by HP on 22/4/2017.
 */
public class FlowerSample
{
    double sepalLength, sepalWidth, petalLength, petalWidth;
    String classLabel;
    ArrayList<Double> sampleData;

    public FlowerSample(double sepalLength, double sepalWidth, double petalLength, double petalWidth, String classLabel)
    {
        sampleData = new ArrayList<Double>();
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
        this.classLabel = classLabel;
        sampleData.add(sepalLength);
        sampleData.add(sepalWidth);
        sampleData.add(petalLength);
        sampleData.add(petalWidth);
    }

    public ArrayList<Double> getSampleData()
    {
        return sampleData;
    }

    public double getSepalLength()
    {
        return sepalLength;
    }

    public double getSepalWidth()
    {
        return sepalWidth;
    }

    public double getPetalLength()
    {
        return petalLength;
    }

    public double getPetalWidth()
    {
        return petalWidth;
    }

    public String getClassLabel()
    {
        return classLabel;
    }
}
