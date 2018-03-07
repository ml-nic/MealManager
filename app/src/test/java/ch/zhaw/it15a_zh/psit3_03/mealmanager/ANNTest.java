package ch.zhaw.it15a_zh.psit3_03.mealmanager;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.ann.ANN;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ANNTest {

    private ANN ann;

    @Mock
    private
    DataSet mockDataSet;
    @Mock
    private
    HashMap<Integer, DataSet> mockTrainingSetMap;

    @Before
    public void setUp() {
        int inputsCount = 5;
        int firstHiddenLayer = 14;
        int outputsCount = 7;

        ann = new ANN(TransferFunctionType.SIGMOID, inputsCount, firstHiddenLayer, outputsCount);
        ann.setDenormalizedError(1.00);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isBetterThanTrue_Test() {
        int inputsCount = 5;
        int firstHiddenLayer = 14;
        int outputsCount = 7;

        ANN tempANN = new ANN(TransferFunctionType.SIGMOID, inputsCount, firstHiddenLayer, outputsCount);
        tempANN.setDenormalizedError(2);
        boolean actual = ann.isBetterThan(tempANN);

        assertEquals(true, actual);
    }

    @Test
    public void isBetterThanFalse_Test() {
        int inputsCount = 5;
        int firstHiddenLayer = 14;
        int outputsCount = 7;

        ANN tempANN = new ANN(TransferFunctionType.SIGMOID, inputsCount, firstHiddenLayer, outputsCount);
        tempANN.setDenormalizedError(0.50);
        boolean actual = ann.isBetterThan(tempANN);

        assertEquals(false, actual);
    }

    @Test
    public void calculatePrediction_Correct_Test() {
        List<DataSetRow> dataSetRows = new ArrayList<>();
        dataSetRows.add(new DataSetRow(new double[]{0, 0, 0, 0, 0}));
        when(mockDataSet.getRows()).thenReturn(dataSetRows);
        ann.calculatePrediction(mockDataSet);

        verify(mockDataSet, times(2)).getRows();
    }

    @Test(expected = NullPointerException.class)
    public void calculatePrediction_Null_Test() {
        ann.calculatePrediction(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void calculatePrediction_EmptyDataSet_Test() {
        ann.calculatePrediction(mockDataSet);
    }

    @Test(expected = NullPointerException.class)
    public void handleLearning_trainingSetMap_isNull_Test() {
        ann.handleLearning(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void handleLearning_trainingSetMap_isEmpty_Test() {
        ann.handleLearning(mockTrainingSetMap);
    }

    @Test(expected = NullPointerException.class)
    public void validate_testSetMap_isNull_Test() {
        ann.validate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_testSetMap_isEmpty_Test() {
        ann.validate(mockTrainingSetMap);
    }
}
