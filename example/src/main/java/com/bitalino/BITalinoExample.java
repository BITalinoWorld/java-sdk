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

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * Example on how to work with BITalino from Java.
 */
public class BITalinoExample {

  private static final Logger logger = Logger.getLogger(BITalinoExample.class
      .getName());

  private static final String MAC = "20:13:08:08:15:83";

  public static void main(String[] args) throws Throwable {
    // validate MAC address
    // if (!mac.matches("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$"))
    // throw new BITalinoException(BITalinoErrorTypes.MACADDRESS_NOT_VALID);
    final String mac = MAC.replace(":", "");

    final int samplerate = 1000;
    final int[] analogs = { 0 };
    BITalinoDevice device = new BITalinoDevice(samplerate, analogs);

    // connect to BITalino device
    final StreamConnection conn = (StreamConnection) Connector.open("btspp://"
        + mac + ":1", Connector.READ_WRITE);
    device.open(conn.openInputStream(), conn.openOutputStream());

    // get BITalino version
    logger.info("VERSION: " + device.version());

    // start acquisition on predefined analog channels
    device.start();

    // read 300 samples
    BITalinoFrame[] frames = device.read(10);
    for (BITalinoFrame frame : frames)
      logger.info("FRAME: " + frame.toString());

    // trigger digital outputs
    // int[] digital = { 1, 1, 1, 1 };
    // device.trigger(digital);

    // stop acquisition and close bluetooth connection
    device.stop();
  }

}