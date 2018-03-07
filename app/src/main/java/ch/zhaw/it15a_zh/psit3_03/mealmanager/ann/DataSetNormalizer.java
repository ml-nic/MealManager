package ch.zhaw.it15a_zh.psit3_03.mealmanager.ann;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.data.norm.Normalizer;

public class DataSetNormalizer implements Normalizer {

    /**
     * Denormalize a double[].
     *
     * @param normalizedVector The double[], which should be denormalized.
     * @param vectorFlag       The vectorFlag, 0 if input layer, 1 if output layer
     * @return The denormalized double[]
     * @throws IllegalArgumentException if the vectorFlag is not 0 or 1
     */
    public double[] denormalize(double[] normalizedVector, int vectorFlag) throws IllegalArgumentException {
        if (normalizedVector == null) {
            throw new NullPointerException("normalizedVector can´t be null");
        }
        if (vectorFlag < 0 || vectorFlag > 1) {
            throw new IllegalArgumentException("vectorFlag have to be 0 or 1");
        }

        double[] denormalizedVector = new double[normalizedVector.length];

        for (int i = 0; i < normalizedVector.length; i++) {
            denormalizedVector[i] = normalizedVector[i] * 15;
        }
        return denormalizedVector;
    }

    /**
     * Normalize a dataSet.
     *
     * @param dataSet The normalized dataSet.
     */
    @Override
    public void normalize(DataSet dataSet) {
        if (dataSet == null) {
            throw new NullPointerException("dataSet can´t be null");
        }

        for (DataSetRow row : dataSet.getRows()) {
            double[] normalizedInput = new double[row.getInput().length];
            double[] normalizedOutput = new double[row.getDesiredOutput().length];
            for (int i = 0; i < row.getInput().length; i++) {
                normalizedInput[i] = row.getInput()[i] / 15;
            }
            for (int i = 0; i < row.getDesiredOutput().length; i++) {
                normalizedOutput[i] = row.getDesiredOutput()[i] / 15;
            }
            row.setInput(normalizedInput);
            row.setDesiredOutput(normalizedOutput);
        }
    }
}

