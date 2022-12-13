import activationfunc.ActivationFunction;
import activationfunc.SigmoidFunction;
import activationfunc.StepFunction;

import java.util.Arrays;

public class Perceptron {

    private Neuron[] neurons;

    private double[][] neuronInputWeights;

    private final ActivationFunction activationFunction;

    public Perceptron(int inputNumber, int outputNumber, ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
        initializeNeurons(inputNumber, outputNumber);
    }

    public void learn(double[][] inputSamples, double[][] outputSamples, double learningRate, double eps) {
        int epochNumber = 0;

        double rmsError;

        do {
            rmsError = 0;

            saveNeuronInputWeights();

            for (int index = 0; index < inputSamples.length; ++index) {
                double[] neuronsOutputValues = getNeuronsOutputValues(getNeuronsPotentials(inputSamples[index]));
                double[] neuronsErrors = getNeuronsErrors(neuronsOutputValues, outputSamples[index]);
                setNeuronsWeights(inputSamples[index], neuronsErrors, learningRate);
                rmsError += getRmsError(neuronsOutputValues, outputSamples[index], inputSamples.length);
            }
            rmsError /= inputSamples.length;
            System.out.println(rmsError);
            ++epochNumber;
        } while (rmsError >= eps && isWeightsChange());

        System.out.println("Epoch number: " + epochNumber);
    }

    public double[] getResult(double[] inputSample) {
        return getNeuronsOutputValues(getNeuronsPotentials(inputSample));
    }

    private boolean isWeightsChange() {
        for (int index1 = 0; index1 < neurons.length; ++index1) {
            for (int index2 = 0; index2 < neurons[index1].getWeights().length; ++index2) {
                if (Double.compare(neurons[index1].getWeight(index2), neuronInputWeights[index1][index2]) != 0) {
                    return true;
                }
            }
        }

        return false;
    }

    private void saveNeuronInputWeights() {
        neuronInputWeights = new double[neurons.length][];

        for (int index = 0; index < neurons.length; ++index) {
            neuronInputWeights[index] = neurons[index].getWeights().clone();
            //System.out.println(Arrays.toString(neuronInputWeights[index]));
        }
    }

    private double getRmsError(double[] neuronsOutputValues, double[] expectedValues, int samplesNumber) {
        double rmsError = 0;

        for (int index = 0; index < neuronsOutputValues.length; ++index) {
            rmsError += Math.pow(expectedValues[index] - neuronsOutputValues[index], 2);
        }

        return rmsError / samplesNumber;
    }

    private void setNeuronsWeights(double[] inputSample, double[] neuronsErrors, double learningRate) {
        for (int index1 = 0; index1 < neurons.length; ++index1) {
            for (int index2 = 0; index2 < neurons[index1].getWeights().length; ++index2) {
                neurons[index1].setWeight(
                        index2,
                        neurons[index1].getWeight(index2) + learningRate * inputSample[index2] * neuronsErrors[index1]
                );
            }
        }
    }

    private double[] getNeuronsErrors(double[] neuronsOutputValues, double[] expectedValues) {
        double[] neuronsErrors = new double[neuronsOutputValues.length];

        for (int index = 0; index < neuronsOutputValues.length; ++index) {
            neuronsErrors[index] = expectedValues[index] - neuronsOutputValues[index];
        }

        //System.out.println(Arrays.toString(neuronsErrors));

        return neuronsErrors;
    }

    private double[] getNeuronsOutputValues(double[] potentials) {
        double[] neuronsOutputValues = new double[potentials.length];

        for (int index = 0; index < potentials.length; ++index) {
            neuronsOutputValues[index] = activationFunction.getY(potentials[index], 0);
        }

        return neuronsOutputValues;
    }

    private double[] getNeuronsPotentials(double[] inputSample) {
        double[] neuronsPotentials = new double[neurons.length];

        for (int index = 0; index < neurons.length; ++index) {
            neuronsPotentials[index] = getNeuronPotential(neurons[index], inputSample);
        }

        return neuronsPotentials;
    }

    private double getNeuronPotential(Neuron neuron, double[] inputSample) {
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
        double[][] inputSamples = {
                {0, 0, 0, 0}, {0, 0, 1, 0}, {0, 0, 1, 1},
                {0, 1, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 1},
                {1, 0, 0, 0}, {1, 0, 1, 0}, {1, 1, 0, 0},
                {1, 1, 0, 1}, {1, 1, 1, 1}
        };

        double[][] outputSamples = {
                {1, 1, 1, 1}, {1, 1, 0, 1}, {1, 1, 0, 0},
                {1, 0, 1, 0}, {1, 0, 0, 1}, {1, 0, 0, 0},
                {0, 1, 1, 1}, {0, 1, 0, 1}, {0, 0, 1, 1},
                {0, 0, 1, 0}, {0, 0, 0, 0}
        };

        double[][] testSamples = {
                {0, 0, 0, 0}, {0, 0, 0, 1}, {0, 0, 1, 0}, {0, 0, 1, 1},
                {0, 1, 0, 0}, {0, 1, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 1},
                {1, 0, 0, 0}, {1, 0, 0, 1}, {1, 0, 1, 0}, {1, 0, 1, 1},
                {1, 1, 0, 0}, {1, 1, 0, 1}, {1, 1, 1, 0}, {1, 1, 1, 1}
        };

        Perceptron perceptron = new Perceptron(inputSamples[0].length, outputSamples[0].length, new StepFunction());
        perceptron.learn(inputSamples, outputSamples, 0.1, 0.001);

        for (double[] inputSample : testSamples) {
            System.out.println(Arrays.toString(perceptron.getResult(inputSample)));
            perceptron.getResult(inputSample);
        }
    }
}
