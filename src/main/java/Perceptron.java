import activationfunc.ActivationFunction;
import activationfunc.StepFunction;

import java.util.Arrays;

public class Perceptron {

    private Neuron[] neurons;

    private final ActivationFunction activationFunction;

    public Perceptron(int inputNumber, int outputNumber, ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
        initializeNeurons(inputNumber, outputNumber);
    }

    public void learn(int[][] inputSamples, int[][] outputSamples, double learningRate, double eps) {
        int dataIndex = 0, epochNumber = 0;

        double rmsError;

        do {
            //System.out.println(Arrays.toString(inputData[dataIndex]) + " " + Arrays.toString(outputData[dataIndex]));
            int[] neuronsOutputValues = getNeuronsOutputValues(getNeuronsPotentials(inputSamples[dataIndex]));
            int[] neuronsErrors = getNeuronsErrors(neuronsOutputValues, outputSamples[dataIndex]);
            setNeuronsWeights(inputSamples[dataIndex], neuronsErrors, learningRate);
            rmsError = getRmsError(neuronsOutputValues, outputSamples[dataIndex], inputSamples.length);
            ++dataIndex; dataIndex %= outputSamples.length; ++epochNumber;
        } while (rmsError >= eps);

        System.out.println("Epoch number: " + epochNumber);
    }

    public int[] getResult(int[] inputSample) {
        return getNeuronsOutputValues(getNeuronsPotentials(inputSample));
    }

    private double getRmsError(int[] neuronsOutputValues, int[] expectedValues, int samplesNumber) {
        double rmsError = 0;

        for (int index = 0; index < neuronsOutputValues.length; ++index) {
            rmsError += Math.pow(expectedValues[index] - neuronsOutputValues[index], 2);
        }

        return rmsError / samplesNumber;
    }

    private void setNeuronsWeights(int[] inputSample, int[] neuronsErrors, double learningRate) {
        for (int index1 = 0; index1 < neurons.length; ++index1) {
            for (int index2 = 0; index2 < neurons[index1].getWeights().length; ++index2) {
                neurons[index1].setWeight(
                        index2,
                        neurons[index1].getWeight(index2) + learningRate * inputSample[index2] * neuronsErrors[index1]
                );
            }
            System.out.print(Arrays.toString(neurons[index1].getWeights()));
        }
        System.out.println();
    }

    private int[] getNeuronsErrors(int[] neuronsOutputValues, int[] expectedValues) {
        int[] neuronsErrors = new int[neuronsOutputValues.length];

        for (int index = 0; index < neuronsOutputValues.length; ++index) {
            neuronsErrors[index] = expectedValues[index] - neuronsOutputValues[index];
        }

        //System.out.println(Arrays.toString(neuronsErrors));

        return neuronsErrors;
    }

    private double[] getNeuronsOutputValues(double[] potentials) {
        int[] neuronsOutputValues = new int[potentials.length];

        for (int index = 0; index < potentials.length; ++index) {
            neuronsOutputValues[index] = activationFunction.getY(potentials[index], );
        }

        return neuronsOutputValues;
    }

    private double[] getNeuronsPotentials(int[] inputSample) {
        double[] neuronsPotentials = new double[neurons.length];

        for (int index = 0; index < neurons.length; ++index) {
            neuronsPotentials[index] = getNeuronPotential(neurons[index], inputSample);
        }

        return neuronsPotentials;
    }

    private double getNeuronPotential(Neuron neuron, int[] inputSample) {
        double potential = 0;

        for (int index = 0; index < neuron.getWeights().length; ++index) {
            potential += neuron.getWeight(index) * inputSample[index];
        }

        return potential;
    }


    private void initializeNeurons(int inputNumber, int outputNumber) {
        neurons = new Neuron[outputNumber];

        for (int index = 0; index < outputNumber; ++index) {
            neurons[index] = new Neuron(inputNumber);
        }
    }

    public static void main(String[] args) {
        int[][] inputSamples = {
                {0, 0, 0}, {0, 0, 1}, {0, 1, 0},
                 {1, 0, 0}, {1, 0, 1},
                {1, 1, 0}, {0, 1, 1}, {1, 1, 1}
        };

        int[][] outputSamples = {
                {1, 1, 1}, {1, 1, 0}, {1, 0, 1},
                 {0, 1, 1}, {0, 1, 0},
                {0, 0, 1}, {1, 0, 0}, {0, 0, 0}
        };

        int[][] outputSamples1 = {
                {1, 1, 1}, {1, 1, 0}, {1, 0, 1},
                {0, 1, 1}, {0, 1, 0},
                {0, 0, 1}, {1, 0, 0}, {0, 0, 0}
        };

        Perceptron perceptron = new Perceptron(3, 3, new StepFunction());
        perceptron.learn(outputSamples, inputSamples, 0.9, 0.001);

        for (int[] inputSample : outputSamples1) {
            System.out.println(Arrays.toString(perceptron.getResult(inputSample)));
        }
    }
}
