package ch.zhaw.it15a_zh.psit3_03.mealmanager.ann;

/**
 * This is the domain class for the Hyperparameter of an ann.
 */
class HyperParameter {
  private int secondHiddenLayer;
  private int inputsCount;
  private int firstHiddenLayer;
  private int outputsCount;
  private double learningRate;
  private double momentum;
  private int maxIterations;
  private double maxError;
  private int percent;
  
  public HyperParameter(int maxIterations, int firstHiddenLayer, int secondHiddenLayer, double maxError,
      double learningRate, double momentum, int percent, int inputsCount, int outputsCount) {
    this.maxIterations = maxIterations;
    this.firstHiddenLayer = firstHiddenLayer;
    this.secondHiddenLayer = secondHiddenLayer;
    this.maxError = maxError;
    this.learningRate = learningRate;
    this.momentum = momentum;
    this.percent = percent;
    this.inputsCount = inputsCount;
    this.outputsCount = outputsCount;
  }
  
  public int getSecondHiddenLayer() {
    return secondHiddenLayer;
  }
  
  public int getInputsCount() {
    return inputsCount;
  }
  
  public int getFirstHiddenLayer() {
    return firstHiddenLayer;
  }
  
  public int getOutputsCount() {
    return outputsCount;
  }
  
  public double getLearningRate() {
    return learningRate;
  }
  
  public double getMomentum() {
    return momentum;
  }
  
  public int getMaxIterations() {
    return maxIterations;
  }
  
  @Override
  public String toString() {
    return "HyperParameter{" + "secondHiddenLayer=" + secondHiddenLayer + ", inputsCount=" + inputsCount +
        ", firstHiddenLayer=" + firstHiddenLayer + ", outputsCount=" + outputsCount + ", learningRate=" + learningRate +
        ", momentum=" + momentum + ", maxIterations=" + maxIterations + ", maxError=" + maxError + ", percent=" +
        percent + '}';
  }
}