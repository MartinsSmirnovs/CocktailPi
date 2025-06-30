package net.alex9849.cocktailpi.model.pump;

import net.alex9849.motorlib.pin.Pi4JInputPin;
import net.alex9849.motorlib.sensor.Flow;

import com.pi4j.io.gpio.digital.PullResistance;

import net.alex9849.cocktailpi.model.gpio.Pin;
import net.alex9849.cocktailpi.utils.PinUtils;
import net.alex9849.cocktailpi.utils.SpringUtility;

public class FlowSensor {
    private Flow sensor;
    private int id;
    private Pin pin;

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

    public FlowSensor(Pin pin, int pulsesPerLiter, int id) {
        PinUtils pinUtils = SpringUtility.getBean(PinUtils.class);
        var inputPin = pinUtils.getBoardInputPin(pin.getPinNr(), PullResistance.OFF);
        sensor = new Flow((Pi4JInputPin) inputPin, pulsesPerLiter);

        this.id = id;
        this.pin = pin;
    }

    public void run(long millis) {
        final boolean isNewValueAvailable = sensor.run(millis);
        if (isNewValueAvailable) {
            lastReading = currentReading;
            currentReading = sensor.read();
        }
    }

    public Status get() {
        if (lastReading == 0 && currentReading == 0) {
            return Status.NO_FLOW;
        } else {
            return Status.FLOWING;
        }
    }

    public int getId() {
        return id;
    }

    public Pin getPin() {
        return pin;
    }
}
