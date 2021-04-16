package sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 24/4/2017.
 */
public class GeneticOperation
{
    static final double CROSSOVER_RATE = 0.99;
    static final double MUTATION_RATE = 0.50;
            ;

    List<String> childList;

    public GeneticOperation()
    {
        childList = new ArrayList<String>();
    }

    public List<String> generateChildren(String parentOne, String parentTwo)
    {
        childList.clear();
        int crossOverPoint;
        String child;
        crossOverPoint = (int) (Math.random() * 52);
        if (Math.random() < CROSSOVER_RATE)
        {
            child = parentOne.substring(0, crossOverPoint);
            child += parentTwo.substring(crossOverPoint, 52);
            child = mutate(child);
            childList.add(child);

            child = parentTwo.substring(0, crossOverPoint);
            child += parentOne.substring(crossOverPoint, 52);
            child = mutate(child);
            childList.add(child);
        }
        else
        {
            child = parentOne;
            child = mutate(child);
            childList.add(child);
            child = parentTwo;
            child = mutate(child);
            childList.add(child);
        }
        return childList;
    }

    public String mutate(String child)
    {
        StringBuilder childString = new StringBuilder(child);
        for (int i = 2; i < 52; i++)
        {
            if (Math.random() > MUTATION_RATE)
            {
                if (child.charAt(i) == '0')
                    childString.setCharAt(i, '1');
                else
                    childString.setCharAt(i, '0');
            }
            if ((i % 8) == 7)
                i += 2;
        }
        return childString.toString();
    }
}
