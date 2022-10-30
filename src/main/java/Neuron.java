import activationfunc.ActivationFunction;

public class Neuron {

    private final static double INITIAL_WEIGHT = 0.000001;

    private final int inputNumber;

    private double[] weights;

    public Neuron(int inputNumber) {
        this.inputNumber = inputNumber;
        initializeWeights();
    }

    public double getWeight(int index) {
        return weights[index];
    }

    public void setWeight(int index, double weight) {
        weights[index] = weight;
    }

    public double[] getWeights() {
        return weights;
    }

    private void initializeWeights() {
        weights = new double[inputNumber];

        for (int index = 0; index < inputNumber; ++index) {
            weights[index] = INITIAL_WEIGHT;
        }
    }
}
