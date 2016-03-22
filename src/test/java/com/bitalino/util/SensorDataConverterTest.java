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

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SensorDataConverterTest {

    @Test
    public void test_emg_conversion() {
        assertEquals(SensorDataConverter.scaleEMG(0, 0), -1.65);
        assertEquals(SensorDataConverter.scaleEMG(0, 1023), 1.65);
    }

    @Test
    public void test_ecg_conversion() {
        assertEquals(SensorDataConverter.scaleECG(0, 0), -1.5);
        assertEquals(SensorDataConverter.scaleECG(0, 1023), 1.5);
    }

    @Test
    public void test_eda_conversion() {
        assertEquals(SensorDataConverter.scaleEDA(0, 0), 1.0);
        assertEquals(SensorDataConverter.scaleEDA(0, 1023), Double.POSITIVE_INFINITY);
    }

    @Test
    public void test_luminosity_conversion() {
        assertEquals(SensorDataConverter.scaleLuminosity(0, 0), 0.0);
        assertEquals(SensorDataConverter.scaleLuminosity(0, 1023), 100.0);
    }
    
    @Test
    public void test_tmp_celsius_conversion() {
        assertEquals(SensorDataConverter.scaleTMP(0, 0, true), -50.0);
        assertEquals(SensorDataConverter.scaleTMP(0, 1023, true), 280.0);
    }
        
    @Test
    public void test_tmp_fahrenheit_conversion() {
        assertEquals(SensorDataConverter.scaleTMP(0, 0, false), -58.0);
        assertEquals(SensorDataConverter.scaleTMP(0, 1023, false), 536.0);
    }

    @Test
    public void test_pzt_conversion() {
        assertEquals(SensorDataConverter.scalePZT(0, 0), -50.0);
        assertEquals(SensorDataConverter.scalePZT(0, 1023), 50.0);
    }
    
    @Test
    public void test_EEG_conversion() {
        assertEquals(SensorDataConverter.scaleEEG(0, 0), -41.25);
        assertEquals(SensorDataConverter.scaleEEG(0, 1023), 41.25);
    }

}
