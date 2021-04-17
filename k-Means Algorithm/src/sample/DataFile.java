package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by WEI TAO on 4/19/2017.
 */
public class DataFile {

    private String lineText;
    private List<Double> listOfDataList;

    DataFile(){
        lineText = "";
    }

    public void readFile(List<List<Double>> dataList, List<String> dataClusterList){
        File file = new File("iris.data");

        try {
            Scanner scan = new Scanner(file);

            while(scan.hasNext()) {
                listOfDataList = new ArrayList<Double>();
                lineText = scan.next();

                String[] lineList = lineText.split(","); // Get only integer and store in string list.

                for (String i : lineList) {
                    if (isDouble(i))
                        listOfDataList.add(Double.parseDouble(i));  // Add every integer from string list.
                    else
                        dataClusterList.add(i);
                }
                // Add into the main arraylist.
                dataList.add(listOfDataList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
