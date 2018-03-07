package ch.zhaw.it15a_zh.psit3_03.mealmanager.ann;

import android.util.Log;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ANNRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.db.repos.ShoppingListItemRepo;
import ch.zhaw.it15a_zh.psit3_03.mealmanager.models.ShoppingListItem;
import org.joda.time.LocalDateTime;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for the interaction with an ann. <p> Doings: creation of ann, training of ann, validation of ann, calculation
 * of ann, preparation of dataSets and creation of hyperparameter.
 */
class ANNHandler {
    private ANN currentANN = null;
    private ArrayList<HyperParameter> hyperParameterList = null;
    private ArrayList<ShoppingListItem> shoppingListItemArrayList;

    /**
     * Starts the handling process, includes: creation of ann, training of ann, validation of ann, calculation of ann,
     * preparation of dataSets and creation of hyperparameter.
     */
    public void start() {
        ANNRepo annRepo = new ANNRepo();
        ShoppingListItemRepo shoppinglistitemRepo = new ShoppingListItemRepo();
        shoppingListItemArrayList = shoppinglistitemRepo.getBoughtShoppinglistitems();
        if (shoppingListItemArrayList == null || shoppingListItemArrayList.size() < 40) {
            Log.d("annHandler", "shoppingListItemArrayList null or empty");
            return;
        }
        Log.d("size:", "  " + shoppingListItemArrayList.size());
        createListOfHyperparameter();
        currentANN = annRepo.getCurrentANN();
        ANN trainedANN = null;

        if (annHastoBeTrained()) {
            trainedANN = trainNetworksWithHyperparameterOptimization();
        }
        if (newAnnHasToBeCalculated(trainedANN)) {
            System.out.println("newannhastobecalc");
            calculateNet(trainedANN);
            System.out.println("Error after all");
            System.out.println("NormalizedError: " + trainedANN.getNormalizedError());
            System.out.println("DenormalizedError: " + trainedANN.getDenormalizedError());
        } else if (oldAnnHasToBeCalculated()) {
            System.out.println("oldannhastobecalc");
            calculateNet(currentANN);
        }

    }

    /**
     * Checks if an ann has to be trained.
     *
     * @return true if no ann exists, lastTrainingDate of currentAnn is before (now-24hours), trainingsetSize is grown at
     * 10 items (since last training) and if denormalizedError of current Ann is to bad. Else return false.
     */
    private boolean annHastoBeTrained() {
        if (currentANN == null) {
            return true;
        }
        if (currentANN.getLastTrainingDate().isBefore(new LocalDateTime().minusDays(10))) {
            return true;
        } else if (currentANN.getTrainingSetSize() <= (10 + shoppingListItemArrayList.size())) {
            return true;
        } else if (currentANN.getDenormalizedError() >= 1.0) {
            return true;
        }
        return false;
    }

    //Future enhancements: evtl. wenn heute noch nicht berechnet
    private boolean newAnnHasToBeCalculated(ANN trainedANN) {
        if (currentANN == null) {
            System.out.println("cur == null also calculated");
            return true;
        } else if (trainedANN.isBetterThan(currentANN)) {
            return true;
        }
        return false;
    }

    private boolean oldAnnHasToBeCalculated() {
        return currentANN.getLastCalculationDate().isBefore(new LocalDateTime().minusDays(1));
    }

    private ANN trainNetworksWithHyperparameterOptimization() {
        ANN bestANN = null;
        ANN trainedANN = null;
        int counter = 0;
        for (HyperParameter hyperParameter : hyperParameterList) {
            System.out.println("currenthp  = " + hyperParameter.toString());
            trainedANN = handleNetworkEvaluation(hyperParameter);

            if (bestANN == null) {
                bestANN = trainedANN;
            } else if (trainedANN.isBetterThan(bestANN)) {
                bestANN = trainedANN;
            }
            counter++;
        }
        return bestANN;
    }

    private void calculateNet(ANN neuralNet) {
        //new code
        System.out.println("new code----------------------------------------------");
        DataSetHandler dataSetHandler = new DataSetHandler();
        HashMap<Integer, DataSet> testSetMap = dataSetHandler.getTestSetMap();

        for (Map.Entry<Integer, DataSet> entry : testSetMap.entrySet()) {
            System.out.println("entrykey " + entry.getKey());
            DataSet testSet = entry.getValue();
            DataSetRow dataSetToCalculated = testSet.getRowAt(testSet.size() - 1);
            testSet = new DataSet(5);
            testSet.addRow(dataSetToCalculated);
            double[] predicted = neuralNet.calculatePrediction(testSet);
            //neuralNet.applyCalculation(predicted);
            System.out.println(dataSetToCalculated);
            System.out.println("---");
            System.out.println(testSet.size());
            System.out.println("--");
            DataSetNormalizer dataSetNormalizer = new DataSetNormalizer();
            double[] test = predicted;
            for (int i = 0; i < predicted.length; i++) {
                test[i] = predicted[i] / 2;
            }
            double[] denormtest = dataSetNormalizer.denormalize(test, 1);
            double[] denormprediction = dataSetNormalizer.denormalize(predicted, 1);
            double sum = 0.0;
            double denormsum = 0.0;
            double denormtestsum = 0.0;
            double testsum = 0.0;
            System.out.println("a");
            for (int i = 0; i < predicted.length; i++) {
                System.out.println(predicted[i]);
                System.out.println(denormprediction[i]);
                sum += predicted[i];
                denormsum += denormprediction[i];
                denormtestsum += denormtest[i];
                testsum += test[i];
            }
            System.out.println("sum" + sum);
            System.out.println("denormsum" + denormsum);
            System.out.println("denormtestsum " + denormtestsum);
            System.out.println("testsum " + testsum);
            double newAmount = Math.floor((denormsum - sum) * 1.5);


            ShoppingListItem shoppingListItem = new ShoppingListItem(1, entry.getKey(), newAmount+(2*(entry.getKey()*2)+1)/2, 0, null, new LocalDateTime(), 1, 0);
            ShoppingListItemRepo shoppingListItemRepo = new ShoppingListItemRepo();
            shoppingListItemRepo.insert(shoppingListItem);
        }
        System.out.println("finished----------------------------------------------");
        System.out.println("");
        System.out.println("");
        System.out.println("");

        ANNRepo annRepo = new ANNRepo();
        neuralNet = annRepo.insert(neuralNet);
    }

    //Future enhancements: get from database and find out how many hyperParameterAdjustments are possible on android
    private void createListOfHyperparameter() {
        hyperParameterList = new ArrayList<>();
        //hyperParameterList.add(new HyperParameter(10000, 1, 0, 0, 0.1, 0.1, 80, 5, 7));
        //hyperParameterList.add(new HyperParameter(10000, 3, 0, 0, 0.1, 0.1, 80, 5, 7));
        //hyperParameterList.add(new HyperParameter(10000, 5, 0, 0, 0.1, 0.1, 80, 5, 7));
        hyperParameterList.add(new HyperParameter(10000, 7, 0, 0, 0.1, 0.1, 80, 5, 7));
    }

    private ANN handleNetworkEvaluation(HyperParameter currentHyperParameter) {
        int secondHiddenLayer = currentHyperParameter.getSecondHiddenLayer();
        int inputsCount = currentHyperParameter.getInputsCount();
        int firstHiddenLayer = currentHyperParameter.getFirstHiddenLayer();
        int outputsCount = currentHyperParameter.getOutputsCount();
        double learningRate = currentHyperParameter.getLearningRate();
        double momentum = currentHyperParameter.getMomentum();
        int maxIterations = currentHyperParameter.getMaxIterations();

        // create multi layer perceptron
        ANN neuralNet = null;
        if (secondHiddenLayer == 0) {
            neuralNet = new ANN(TransferFunctionType.SIGMOID, inputsCount, firstHiddenLayer, outputsCount);
        } else {
            neuralNet = new ANN(TransferFunctionType.SIGMOID, inputsCount, firstHiddenLayer, secondHiddenLayer, outputsCount);
        }
        // set learning parametars
        MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet.getLearningRule();
        learningRule.setLearningRate(learningRate);
        learningRule.setMomentum(momentum);
        //learningRule.setMaxError(message.getMaxError());
        learningRule.setMaxIterations(maxIterations);

        DataSetHandler dataSetHandler = new DataSetHandler();
        // learn the training set
        System.out.println("Training neural network...");
        neuralNet.handleLearning(dataSetHandler.getTrainingSetMap());
        System.out.println("Done!");
        neuralNet.setIterations(learningRule.getCurrentIteration());

        System.out.println("Testing trained neural network");
        neuralNet.validate(dataSetHandler.getTestSetMap());
        return neuralNet;
    }
}
