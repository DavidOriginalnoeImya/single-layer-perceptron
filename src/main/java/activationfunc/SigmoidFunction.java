package activationfunc;

public class SigmoidFunction implements ActivationFunction {
    @Override
    public double getY(double potential, double offset) {
        return 1 / (1 + Math.exp(-offset * potential));
    }
}
