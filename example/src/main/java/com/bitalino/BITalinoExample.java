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

import java.io.FileWriter;
import java.util.logging.Logger;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import com.google.gson.Gson;

/**
 * Example on how to work with BITalino from Java.
 */
public class BITalinoExample {

  private static final Logger logger = Logger.getLogger(BITalinoExample.class
      .getName());

  /*
   * TODO change to your device's MAC address
   */
  private static final String MAC = "20:13:08:08:15:83";

  public static void main(String[] args) throws Throwable {
    // validate MAC address
    // if (!mac.matches("^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$"))
    // throw new BITalinoException(BITalinoErrorTypes.MACADDRESS_NOT_VALID);
    final String mac = MAC.replace(":", "");

    final int samplerate = 100;
    final int[] analogs = { 0 };
    BITalinoDevice device = new BITalinoDevice(samplerate, analogs);

    // connect to BITalino device
    final StreamConnection conn = (StreamConnection) Connector.open("btspp://"
        + mac + ":1", Connector.READ_WRITE);
    device.open(conn.openInputStream(), conn.openOutputStream());

    // get BITalino version
    logger.info("Firmware Version: " + device.version());

    // start acquisition on predefined analog channels
    device.start();

    // read n samples
    final int numberOfSamplesToRead = 20;
    BITalinoFrame[] samplesRead = new BITalinoFrame[numberOfSamplesToRead];
    final String logFile = "bitalino_log.json";
    final FileWriter fileWriter = new FileWriter(logFile);
    logger.info("Reading " + numberOfSamplesToRead + " samples..");
    for (int counter = 0; counter < numberOfSamplesToRead; counter++) {
      final BITalinoFrame[] frames = device.read(1);
      samplesRead[counter] = frames[0];
      logger.info("FRAME: " + frames[0].toString());
      Thread.sleep(1000);
    }
    fileWriter.write(new Gson().toJson(samplesRead));
    fileWriter.close();
    logger.info("Generated " + logFile + " with frames encoded into JSON.");

    // trigger digital outputs
    // int[] digital = { 1, 1, 1, 1 };
    // device.trigger(digital);

    // stop acquisition and close bluetooth connection
    device.stop();
  }
}