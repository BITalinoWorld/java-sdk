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
    BITalinoFrame[] frames = device.read(2000);
    logger.info("FRAMES: " + frames);

    // trigger digital outputs
    int[] digital = { 1, 1, 1, 1 };
    // device.trigger(digital);

    // stop acquisition and close bluetooth connection
    device.stop();
  }

}