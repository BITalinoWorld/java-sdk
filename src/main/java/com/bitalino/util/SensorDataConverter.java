/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bitalino.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * This class holds methods for converting/scaling raw data from BITalino
 * included sensors to human-readable data.
 */
public class SensorDataConverter {

    private static final double VCC = 3.3; // volts
    private static final int NBR_BITALINO_CHANNELS = 6;
    private static final int EMG_GAIN = 1009;
    private static final int ECG_GAIN = 1100;
    private static final double EDA_SCALE_FACTOR = 0.132;
    private static final int EEG_GAIN = 41782;

    /**
     * ElectroMyoGraphy conversion.
     *
     * @param port
     *          the port where the <tt>raw</tt> value was read from.
     * @param activePorts
     *          array containing the list of active <tt>ports</tt>, for example, when the 6
     *          available ports are active the array should be {0, 1, 2, 3, 4, 5}.
     * @param raw
     *          the value read.
     * @return a value ranging between -1.64 and 1.64mV
     */
    public static double scaleEMG(final int port, final Integer[] activePorts, final int raw) {
        final double result =
                ((raw * VCC / getResolution(port, activePorts) - VCC / 2) * 1000) / EMG_GAIN;
        return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * ElectroCardioGraphy conversion.
     *
     * @param port
     *          the port where the <tt>raw</tt> value was read from.
     * @param activePorts
     *          array containing the list of active <tt>ports</tt>, for example, when the 6
     *          available ports are active the array should be {0, 1, 2, 3, 4, 5}.
     * @param raw
     *          the value read.
     * @return a value ranging between -1.5 and 1.5mV
     */
    public static double scaleECG(final int port, final Integer[] activePorts, final int raw) {
        final double result = (((raw / getResolution(port, activePorts) - 0.5) * VCC) / ECG_GAIN) * 1000;
        return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * Accelerometer conversion based on reference calibration values. If you want
     * to acquire more precise values, use
     * {@link #scaleAccelerometerWithPrecision(int, Integer[], int)
     * scaleAccelerometerWithPrecision} method.
     *
     * @param port
     *          the port where the <tt>raw</tt> value was read from.
     * @param activePorts
     *          array containing the list of active <tt>ports</tt>, for example, when the 6
     *          available ports are active the array should be {0, 1, 2, 3, 4, 5}.
     * @param raw
     *          the value read.
     * @return a value ranging between -3 and 3g
     */
    public static double scaleAccelerometer(final int port, final Integer[] activePorts, final int raw) {
        final double result = scaleAccelerometerWithPrecision(port, activePorts, raw);
        return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * Accelerometer conversion based on parameterized calibration values.
     *
     * @param port
     *          the port where the <tt>raw</tt> value was read from.
     * @param activePorts
     *          array containing the list of active <tt>ports</tt>, for example, when the 6
     *          available ports are active the array should be {0, 1, 2, 3, 4, 5}.
     * @param raw
     *          the value read.
     * @return a value ranging between -4.85 and 4.85g (6 bits) or between -4.85 and 4.99 (10 bits).
     */
    public static double scaleAccelerometerWithPrecision(final int port, final Integer[] activePorts,
                                                         final int raw) {
        int min = getResolution(port, activePorts) == 1023 ? 400 : 25;
        int max = getResolution(port, activePorts) == 1023 ? 608 : 38;
        return 2 * ((double)(raw - min) / (max - min)) - 1;
    }

    /**
     * Electrodermal Activity conversion.
     *
     * @param port
     *          the port where the <tt>raw</tt> value was read from.
     * @param activePorts
     *          array containing the list of active <tt>ports</tt>, for example, when the 6
     *          available ports are active the array should be {0, 1, 2, 3, 4, 5}.
     * @param raw
     *          the value read.
     * @return  a value ranging from 0 to 25 uS (micro Siemens).
     */
    public static double scaleEDA(final int port, final Integer[] activePorts, final int raw) {
        final double result = ((raw / getResolution(port, activePorts)) * VCC) / EDA_SCALE_FACTOR;
        return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * Luminosity conversion.
     *
     * @param port
     *          the port where the <tt>raw</tt> value was read from.
     * @param activePorts
     *          array containing the list of active <tt>ports</tt>, for example, when the 6
     *          available ports are active the array should be {0, 1, 2, 3, 4, 5}.
     * @param raw
     *          the value read.
     * @return a value ranging from 0 and 100%.
     */
    public static double scaleLuminosity(final int port, final Integer[] activePorts, final int raw) {
        return 100 * (raw / getResolution(port, activePorts));
    }
    
        /**
     * Temperature conversion.
     *
     * @param port
     *          the port where the <tt>raw</tt> value was read from.
     * @param activePorts
     *          array containing the list of active <tt>ports</tt>, for example, when the 6
     *          available ports are active the array should be {0, 1, 2, 3, 4, 5}.
     * @param raw
     *          the value read.
     * @param celsius
     *          <tt>true</tt>:use celsius as metric,
     *          <tt>false</tt>: fahrenheit is used.
     * @return a value ranging between -50 and 280 Celsius (-58 and 536 Fahrenheit)
     */
    public static double scaleTMP(final int port, final Integer[] activePorts, final int raw, boolean celsius){
        double result = (((raw/getResolution(port, activePorts))*VCC) - 0.5)*100;

        if (!celsius)
            // Convert to fahrenheit
            result = result*((double)9/5) + 32;

        return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * Respiration conversion.
     *
     * @param port
     *          the port where the <tt>raw</tt> value was read from.
     * @param activePorts
     *          array containing the list of active <tt>ports</tt>, for example, when the 6
     *          available ports are active the array should be {0, 1, 2, 3, 4, 5}.
     * @param raw
     *          the value read.
     * @return a value ranging between -50% and 50%
     */
    public static double scalePZT(final int port, final Integer[] activePorts, final int raw){
        double result =  ((raw/getResolution(port, activePorts)) - 0.5)*100;
        return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

    }

    /**
     * Electroencephalography conversion.
     *
     * @param port
     *          the port where the <tt>raw</tt> value was read from.
     * @param activePorts
     *          array containing the list of active <tt>ports</tt>, for example, when the 6
     *          available ports are active the array should be {0, 1, 2, 3, 4, 5}.
     * @param raw
     *          the value read.
     * @return a value ranging between -39.49 and 39.49 microvolt
     */
    public static double scaleEEG(final int port, final Integer[] activePorts, final int raw){
        // result rescaled to microvolt
        double result = (((raw/getResolution(port, activePorts))-0.5)*VCC)/EEG_GAIN;
        result = result*Math.pow(10, 6);

        return new BigDecimal(result).setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * Returns the resolution (maximum value) for a certain port.
     * <p>
     * From port 0 to 3, the resolution is 10-bit, thus 2^10 - 1 = 1023. <br />
     * From port 4 to 5, the resolution is 6-bit, thus 2^6 - 1 = 63.
     *
     * @param port
     *          index of the port under analysis.
     * @param activePorts
     *          array containing the list of active <tt>ports</tt>, for example, when the 6
     *          available ports are active the array should be {0, 1, 2, 3, 4, 5}.
     * @return the resolution (maximum value) for a certain port.
     */
    private static final double getResolution(final int port, final Integer[] activePorts) {
        return (double) activePorts.length > NBR_BITALINO_CHANNELS - 2 && Arrays.asList(activePorts).indexOf(port) < NBR_BITALINO_CHANNELS - 2 || activePorts.length <= NBR_BITALINO_CHANNELS - 2 ? 1023 : 63;
    }

}
