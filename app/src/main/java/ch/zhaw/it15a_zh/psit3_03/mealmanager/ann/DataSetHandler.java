package ch.zhaw.it15a_zh.psit3_03.mealmanager.ann;

import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ShoppingListItemRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.ShoppingListItem;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class initialize the datasets for the ann.
 */
class DataSetHandler {
    private final static int INPUT_SIZE = 5;
    private final static int OUTPUT_SIZE = 7;
    private final static int trainingsetpercent = 80;
    private ArrayList<ShoppingListItem> shoppingListItemArrayList = null;
    private HashMap<Integer, ArrayList<ShoppingListItem>> map = null;
    private HashMap<Integer, DataSet> trainingSetMap = null;
    private HashMap<Integer, DataSet> testSetMap = null;
    private DataSetNormalizer normalizer = null;

    public DataSetHandler() {
        ShoppingListItemRepo shoppinglistitemRepo = new ShoppingListItemRepo();
        shoppingListItemArrayList = shoppinglistitemRepo.getBoughtShoppinglistitems();
        map = new HashMap<>();
        normalizer = new DataSetNormalizer();
        seperateEachItem();
        initDataSets();
    }

    /**
     * Puts each itemid from bought shoppingitems into an map
     */
    private void seperateEachItem() {
        for (ShoppingListItem shoppingListItem : shoppingListItemArrayList) {
            Integer id_key = shoppingListItem.getItemid();

            if (map.get(id_key) == null) {
                map.put(id_key, new ArrayList<ShoppingListItem>());
            } else {
                map.get(id_key).add(shoppingListItem);
            }
        }
    }

    /**
     * Generates for each itemid the training and testing set.
     * Example:
     * trainingset:  Input: 0.0, 0.0, 0.0, 0.0, 2.0  Desired output: 2.0, 0.0, 5.0, 0.0, 0.0, 0.0, 0.0
     * 2.0 im input heist heute gekauft und im output heute zu kaufen
     */
    private void initDataSets() {
        trainingSetMap = new HashMap<>();
        testSetMap = new HashMap<>();
        for (Map.Entry<Integer, ArrayList<ShoppingListItem>> entry : map.entrySet()) {
            int id = entry.getKey();
            ArrayList<ShoppingListItem> list = entry.getValue();
            double maxValue = 0.0;
            for (ShoppingListItem shoppingListItem : list) {
                double tempValue = shoppingListItem.getAmount();
                if (tempValue >= maxValue) {
                    maxValue = tempValue;
                    System.out.println("ShoppingListItem with maxValue: " + shoppingListItem);
                }
            }

            //Attention: 10 is for testing purpose
            int size = list.size() - 10;
            int trainingSetSize = size * trainingsetpercent / 100;
            DataSet trainingSet = new DataSet(INPUT_SIZE, OUTPUT_SIZE);
            DataSet testSet = new DataSet(INPUT_SIZE, OUTPUT_SIZE);

            if (size < 12) {
                break;
            }
            for (int i = 0; i < size - (INPUT_SIZE + OUTPUT_SIZE); i++) {
                double input0 = list.get(i).getAmount();
                double input1 = list.get(i + 1).getAmount();
                double input2 = list.get(i + 2).getAmount();
                double input3 = list.get(i + 3).getAmount();
                double input4 = list.get(i + 4).getAmount();

                double output0 = list.get(i + 4).getAmount();
                double output1 = list.get(i + 5).getAmount();
                double output2 = list.get(i + 6).getAmount();
                double output3 = list.get(i + 7).getAmount();
                double output4 = list.get(i + 8).getAmount();
                double output5 = list.get(i + 9).getAmount();
                double output6 = list.get(i + 10).getAmount();

                double[] input = new double[]{input0, input1, input2, input3, input4};
                double[] desiredOutput = new double[]{output0, output1, output2, output3, output4, output5, output6};

                if (i < trainingSetSize) {
                    trainingSet.addRow(new DataSetRow(input, desiredOutput));
                } else {
                    testSet.addRow(new DataSetRow(input, desiredOutput));
                }
            }

            //Reverese: vector = max * normalizedVector - min * normalizedVector + min
            //Normal:   normalizedVector[i] = (vector[i] - min[i]) / (max[i] - min[i])
            normalizer.normalize(trainingSet);
            normalizer.normalize(testSet);
            trainingSetMap.put(id, trainingSet);
            testSetMap.put(id, testSet);
        }
    }

    public HashMap<Integer, DataSet> getTrainingSetMap() {
        return trainingSetMap;
    }

    public HashMap<Integer, DataSet> getTestSetMap() {
        return testSetMap;
    }

}