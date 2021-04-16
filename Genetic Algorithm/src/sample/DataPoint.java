package sample;


/**
 * Created by HP on 22/4/2017.
 */
public class DataPoint
{
    double xPoint, yPoint;
    int assignedCluster;

    public DataPoint(double xPoint, double yPoint)
    {
        this.xPoint = xPoint;
        this.yPoint = yPoint;
    }

    public void setAssignedCluster(int assignedCluster)
    {
        this.assignedCluster = assignedCluster;
    }

    public double getXPoint()
    {
        return xPoint;
    }

    public double getYPoint()
    {
        return yPoint;
    }

    public int getAssignedCluster()
    {
        return assignedCluster;
    }
}
