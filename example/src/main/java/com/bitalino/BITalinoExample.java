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
package com.bitalino;

import java.util.logging.Logger;

/**
 * Example on how to work with BITalino from Java.
 */
public class BITalinoExample {

  private static final Logger logger = Logger.getLogger(BITalinoExample.class
      .getName());

  public static void main(String[] args) throws Throwable {

    final String mac = "20:13:08:08:15:83";
    final int samplerate = 1000;
    final int[] analogs = { 0 };
    BITalinoDevice device = new BITalinoDevice(mac, samplerate, analogs);

    // connect to BITalino device
    device.open();

    // get BITalino version
    logger.info("VERSION: " + device.version());

    // start acquisition on predefined analog channels
    device.start();

    // read 300 samples
    BITalinoFrame[] frames = device.read(10);
    for (BITalinoFrame frame : frames)
      logger.info("FRAME: " + frame.toString());

    // trigger digital outputs
    int[] digital = { 1, 1, 1, 1 };
    // device.trigger(digital);

    // stop acquisition and close bluetooth connection
    device.stop();
  }

}