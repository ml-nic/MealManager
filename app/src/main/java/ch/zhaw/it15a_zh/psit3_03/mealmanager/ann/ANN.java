package ch.zhaw.it15a_zh.psit3_03.mealmanager.ann;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ShoppingListItemRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.ShoppingListItem;
import org.joda.time.LocalDateTime;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the domain class for an MultiLayerPerceptron artificial neural network (ann).
 * Input is an double[], where
 * the last element means today, the element before is yesterday and so on. Output is an double[], where the first
 * element means prediction for today, the second element means prediction for tomorrow and so on.
 */
public class ANN extends MultiLayerPerceptron {
    private final LocalDateTime creationdate;
    private double normalizedError;
    private double denormalizedError;
    private int iterations;
    private int inputsCount = 0;
    private int outputsCount = 0;
    private int trainingSetSize = 0;
    private LocalDateTime lastTrainingDate;
    private LocalDateTime lastCalculationDate;
    private double totaltime;
    private double trainingtime;
    private double testtime;
    private double calculationtime;
    private int ANNID;

    /**
     * Constructor for an ANN with a second hidden Layer.
     *
     * @param transferFunctionType Type of transferFunction
     * @param inputsCount          Amount of input neurons.
     * @param firstHiddenLayer     Amount of neurons in the first hidden layer.
     * @param secondHiddenLayer    Amount of neurons in the second hidden layer.
     * @param outputsCount         Amount of output neurons.
     */
    public ANN(TransferFunctionType transferFunctionType, int inputsCount, int firstHiddenLayer, int secondHiddenLayer,
               int outputsCount) {
        super(transferFunctionType, inputsCount, firstHiddenLayer, secondHiddenLayer, outputsCount);
        this.creationdate = new LocalDateTime();
        this.inputsCount = inputsCount;
        this.outputsCount = outputsCount;
    }

    /**
     * Constructor for an ANN with only one hidden layer.
     *
     * @param transferFunctionType Type of transferFunction
     * @param inputsCount          Amount of input neurons.
     * @param firstHiddenLayer     Amount of neurons in the first hidden layer.
     * @param outputsCount         Amount of output neurons.
     */
    public ANN(TransferFunctionType transferFunctionType, int inputsCount, int firstHiddenLayer, int outputsCount) {
        super(transferFunctionType, inputsCount, firstHiddenLayer, outputsCount);
        this.creationdate = new LocalDateTime();
        this.inputsCount = inputsCount;
        this.outputsCount = outputsCount;
    }


    /*
      TODO:entweder für jedes produkt ein neuronales netz, dann dort wo netz erstellt wird für jedes einzelnes testen oder
      einfach nur ein netz und mit diesem netz alle berechnen aber nur für das mit grösstem size trainieren
	*/

    /**
     * Validates the ann for each <itemid, dataSet>.
     *
     * @param validationSet The validationSet, which contains the corresponding DataSets for each itemid.
     */
    public void validate(HashMap<Integer, DataSet> validationSet) {
        if (validationSet == null) {
            throw new NullPointerException("validationSet can´t be null");
        }
        if (validationSet.entrySet().isEmpty()) {
            throw new IllegalArgumentException("validationSet have to contain at least one element");
        }

        long start = System.currentTimeMillis();
        int counter = 0;
        for (Map.Entry<Integer, DataSet> entry : validationSet.entrySet()) {
            DataSet testSet = entry.getValue();
            int i = 0;
            double[] normalizedDiffList = new double[testSet.size()];
            double[] denormalizedDiffList = new double[testSet.size()];

            for (DataSetRow trainingElement : testSet.getRows()) {
                this.setInput(trainingElement.getInput());
                this.calculate();

                double[] networkOutput = this.getOutput();
                double[] desiredOutput = testSet.getRowAt(i).getDesiredOutput();

                DataSetNormalizer dataSetNormalizer = new DataSetNormalizer();
                dataSetNormalizer.denormalize(networkOutput, 1);

                double normalized_middle_diffPerRow = 0.0D;
                double denormalized_middle_diffPerRow = 0.0D;
                for (int a = 0; a < networkOutput.length; a++) {
                    normalized_middle_diffPerRow += Math.abs(networkOutput[a] - desiredOutput[a]);
                    denormalized_middle_diffPerRow += Math.abs(
                            dataSetNormalizer.denormalize(networkOutput, 1)[a] - dataSetNormalizer.denormalize(desiredOutput, 1)[a]);
                }

                normalizedDiffList[i] = (normalized_middle_diffPerRow / outputsCount);
                denormalizedDiffList[i] = (denormalized_middle_diffPerRow / outputsCount);
                i++;
            }
            counter++;
            double normalized_middle_diff_perTestSet = 0.0D;
            double denormalized_middle_diff_perTestSet = 0.0D;
            for (int b = 0; b < normalizedDiffList.length; b++) {
                normalized_middle_diff_perTestSet += Math.abs(normalizedDiffList[b]);
                denormalized_middle_diff_perTestSet += Math.abs(denormalizedDiffList[b]);
            }
            normalizedError = normalized_middle_diff_perTestSet / normalizedDiffList.length;
            denormalizedError = denormalized_middle_diff_perTestSet / denormalizedDiffList.length;
            System.out.println("NormalizedError = " + normalizedError);
            System.out.println("DenormalizedError = " + denormalizedError);
        }
        testtime = System.currentTimeMillis() - start;
    }

    /**
     * Handles training of the ann, so that the ann could be trained for each item.
     *
     * @param trainingSetMap The Map, which contains the itemid and corresponding trainingSet.
     */
    public void handleLearning(HashMap<Integer, DataSet> trainingSetMap) {
        if (trainingSetMap == null) {
            throw new NullPointerException("trainingSetMap can´t be null");
        }
        if (trainingSetMap.entrySet().isEmpty()) {
            throw new IllegalArgumentException("trainingSetMap have to contain at least one element");
        }

        long start = System.currentTimeMillis();

        for (Map.Entry<Integer, DataSet> entry : trainingSetMap.entrySet()) {
            DataSet trainingSet = entry.getValue();
            this.learn(trainingSet);
        }

        trainingtime = System.currentTimeMillis() - start;
        lastTrainingDate = new LocalDateTime();
    }

    /**
     * Compares this ann with the other ann.
     * For comparison the denormailzedError of both ann´s is used.
     *
     * @param other The other ann
     * @return Returns true if this ann is better than the other, else return false.
     */
    public boolean isBetterThan(ANN other) {
        return this.getDenormalizedError() <= other.getDenormalizedError();
    }

    /**
     * Calculates an prediction for an given input dataSet.
     * Predicted values in an double[]. First element is prediction for today, next is for tomorrow and so on.
     *
     * @param toCalculatedSet The DataSet for which a prediction should be calculated
     * @return the predicted values in an double[]
     */
    public double[] calculatePrediction(DataSet toCalculatedSet) {
        if (toCalculatedSet == null) {
            throw new NullPointerException("toCalculatedSet can´t be null");
        }
        if (toCalculatedSet.getRows().isEmpty()) {
            throw new IllegalArgumentException("toCalculatedSet have to contain at least one row");
        }

        long start = System.currentTimeMillis();

        double[] predicted = null;
        for (DataSetRow testElement : toCalculatedSet.getRows()) {
            this.setInput(testElement.getInput());
            this.calculate();
            predicted = this.getOutput();
        }
        calculationtime = System.currentTimeMillis() - start;
        totaltime = trainingtime + testtime + calculationtime;
        lastCalculationDate = new LocalDateTime();

        System.out.println("calctime " + calculationtime + "  " + totaltime + "  " + lastCalculationDate);

        return predicted;
    }

    public int getTrainingSetSize() {
        return trainingSetSize;
    }

    public double getNormalizedError() {
        return normalizedError;
    }

    public double getDenormalizedError() {
        return denormalizedError;
    }

    public void setDenormalizedError(double denormalizedError) {
        this.denormalizedError = denormalizedError;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public LocalDateTime getLastTrainingDate() {
        return lastTrainingDate;
    }

    public LocalDateTime getLastCalculationDate() {
        return lastCalculationDate;
    }

    @Override
    public String toString() {
        return "ANN{" + "normalizedError=" + normalizedError + ", denormalizedError=" + denormalizedError +
                ", iterations=" + iterations + ", inputsCount=" + inputsCount + ", outputsCount=" + outputsCount +
                ", trainingSetSize=" + trainingSetSize + ", lastTrainingDate=" + lastTrainingDate + ", lastCalculationDate=" +
                lastCalculationDate + ", creationdate=" + creationdate + ", totaltime=" + totaltime + ", trainingtime=" +
                trainingtime + ", testtime=" + testtime + ", calculationtime=" + calculationtime + '}';
    }

    public int getANNID() {
        return ANNID;
    }
}