package net.alex9849.cocktailpi.model.pump;

import net.alex9849.motorlib.pin.Pi4JInputPin;
import net.alex9849.motorlib.sensor.Flow;

public class FlowSensor {
    private Flow sensor;

    public enum Status {
        NO_FLOW,
        FLOWING
    }

    /*
     * The sensor does not output correct value right away. In the beginning, it may
     * output 0 (because not enough pulses came in yet), or it may output a number
     * that is too big (if run() was called in intervals that is more than a
     * second). Because of that, we have to take an average of 2 values, to
     * determine that status of the sensor.
     */
    private double currentReading = -1;
    private double lastReading = -1;

    public FlowSensor(Pi4JInputPin pin, int pulsesPerLiter) {
        sensor = new Flow(pin, pulsesPerLiter);
    }

    public void run(long millis) {
        sensor.run(millis);
        lastReading = currentReading;
        currentReading = sensor.read();
    }

    public Status get() {
        if (lastReading == 0 && currentReading == 0) {
            return Status.NO_FLOW;
        } else {
            return Status.FLOWING;
        }
    }
}
