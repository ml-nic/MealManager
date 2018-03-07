package ch.zhaw.it15a_zh.psit3_03.mealmanager;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.ann.DataSetNormalizer;
import org.junit.Before;
import org.junit.Test;
import org.neuroph.core.data.DataSet;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class NormalizerTest {

    private DataSetNormalizer dataSetNormalizer;
    private DataSet dataSet;

    @Before
    public void setUp() {
        dataSetNormalizer = new DataSetNormalizer();
        dataSet = new DataSet(5, 7);
    }

    @Test
    public void nomalize_correct_Test() {
        dataSet.addRow(new double[]{10, 9, 8, 7, 6}, new double[]{10, 9, 8, 7, 6, 5, 4});
        dataSetNormalizer.normalize(dataSet);

        double[] normalizedArray = dataSet.getRowAt(0).getInput();
        double[] expectedArray = new double[]{0.66, 0.6, 0.53, 0.46, 0.4};

        int inputSize = 5;
        for (int i = 0; i < inputSize; i++) {
            assertEquals(expectedArray[i], normalizedArray[i], 0.01);
        }
    }

    @Test
    public void nomalize_smallArray_Test() {
        dataSet = new DataSet(1, 1);
        dataSet.addRow(new double[]{10}, new double[]{10});
        dataSetNormalizer.normalize(dataSet);

        double[] normalizedArray = dataSet.getRowAt(0).getInput();
        double[] expectedArray = new double[]{0.66};

        int inputSize = 1;
        for (int i = 0; i < inputSize; i++) {
            assertEquals(expectedArray[i], normalizedArray[i], 0.01);
        }
    }

    @Test(expected = NullPointerException.class)
    public void nomalize_nullDataSet_Test() {
        dataSetNormalizer.normalize(null);
    }

    @Test
    public void denormalizeOutput_correct_Test() {
        double[] output = new double[]{0.7, 0.6, 0.5, 0.5, 0.4};

        double[] denormalizedOutput = dataSetNormalizer.denormalize(output, 1);

        double[] expectedDenomOutput = new double[]{10.5, 9, 7.5, 7.5, 6};
        assertArrayEquals(expectedDenomOutput, denormalizedOutput, 0.1);
    }

    @Test(expected = NullPointerException.class)
    public void denormalize_nullArray_Test() {
        dataSetNormalizer.denormalize(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void denormalize_vectorFlagToLow_Test() {
        dataSetNormalizer.denormalize(new double[]{1, 0.9, 0.8, 0.7, 0.6}, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void denormalize_vectorFlagToHigh_Test() {
        dataSetNormalizer.denormalize(new double[]{1, 0.9, 0.8, 0.7, 0.6}, 2);
    }
}