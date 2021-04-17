package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.io.IOException;

public class Controller {

    @FXML private TextField text;

    @FXML private TextArea resultOne;
    @FXML private TextArea resultTwo;
    @FXML private TextArea resultThree;
    @FXML private TextArea resultOneSummary;
    @FXML private TextArea resultTwoSummary;
    @FXML private TextArea resultThreeSummary;
    @FXML private TextArea resultSummary;
    @FXML private TextArea resultCluster;

    @FXML private void onClickKMeans() throws IOException {

        FileWriter writer = new FileWriter("ExternalSupervised.csv");
        for(int i = 1; i <= 1; i++) {
            KMSimulator KM = new KMSimulator();
            KM.simulate(resultOne, resultOneSummary, resultTwo, resultTwoSummary, resultThree, resultThreeSummary, resultSummary, resultCluster, writer, i);
        }
        writer.close();

    }
}
