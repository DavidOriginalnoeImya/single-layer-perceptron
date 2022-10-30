package activationfunc;

public class StepFunction implements ActivationFunction {
    @Override
    public double getY(double potential, double offset) {
        return Double.compare(potential, offset) < 0 ? 0 : 1;
    }
}
