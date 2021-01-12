package main.java.simulation.utils;

import fr.sorbonne_u.devs_simulation.models.time.Duration;
import fr.sorbonne_u.devs_simulation.models.time.Time;

/**
 * Washer simulation program representation
 *
 * @author  Bello Memmi
 */
public class SimProgram {

    private Time beginProgram;
    private Duration durationProgram;

    public Duration getDurationProgram() {
        return durationProgram;
    }

    public void setDurationProgram(Duration durationProgram) {
        this.durationProgram = durationProgram;
    }

    public SimProgram(Time beginProgram, Duration durationProgram){
        this.beginProgram = beginProgram;
        this.durationProgram = durationProgram;
    }

    public Time getBeginProgram() {
        return beginProgram;
    }

    public void setBeginProgram(Time beginProgram) {
        this.beginProgram = beginProgram;
    }
}
