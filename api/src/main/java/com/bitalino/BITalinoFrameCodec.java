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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class BITalinoFrameCodec {

  /**
   * Decode frame.
   * 
   * @param buffer
   * @return decoded frame
   * @see {@link BITalinoFrame}
   */
  private static BITalinoFrame decode(final byte[] buffer,
      final int totalAnalogs, final int totalBytes) throws IOException,
      BITalinoException {

    try {
      BITalinoFrame frame = new BITalinoFrame();
      final int j = (totalBytes - 1);
      int x0 = 0, x1 = 0, x2 = 0, x3 = 0, out = 0, inp = 0;
      final int CRC = (buffer[j - 0] & 0x0F) & 0xFF;

      // check CRC
      for (int bytes = 0; bytes < totalBytes; bytes++) {
        for (int bit = 7; bit > -1; bit--) {
          inp = (buffer[bytes]) >> bit & 0x01;
          if (bytes == (totalBytes - 1) && bit < 4)
            inp = 0;
          out = x3;
          x3 = x2;
          x2 = x1;
          x1 = out ^ x0;
          x0 = inp ^ out;
        }
      }
      // if the message was correctly received, start decoding
      if (CRC == ((x3 << 3) | (x2 << 2) | (x1 << 1) | x0)) {
        frame = new BITalinoFrame();
        frame.setSequence(((buffer[j - 0] & 0xF0) >> 4) & 0xf);
        frame.setDigital(0, (buffer[j - 1] >> 7) & 0x01);
        frame.setDigital(1, (buffer[j - 1] >> 6) & 0x01);
        frame.setDigital(2, (buffer[j - 1] >> 5) & 0x01);
        frame.setDigital(3, (buffer[j - 1] >> 4) & 0x01);

        // parse buffer frame
        switch (totalAnalogs - 1) {
        case 5:
          frame.setAnalog(5, (buffer[j - 7] & 0x3F));
        case 4:

          frame
              .setAnalog(
                  4,
                  (((buffer[j - 6] & 0x0F) << 2) | ((buffer[j - 7] & 0xc0) >> 6)) & 0x3f);
        case 3:

          frame
              .setAnalog(
                  3,
                  (((buffer[j - 5] & 0x3F) << 4) | ((buffer[j - 6] & 0xf0) >> 4)) & 0x3ff);
        case 2:

          frame
              .setAnalog(
                  2,
                  (((buffer[j - 4] & 0xff) << 2) | (((buffer[j - 5] & 0xc0) >> 6))) & 0x3ff);
        case 1:

          frame.setAnalog(1,
              (((buffer[j - 2] & 0x3) << 8) | (buffer[j - 3]) & 0xff) & 0x3ff);
        case 0:

          frame
              .setAnalog(
                  0,
                  (((buffer[j - 1] & 0xF) << 6) | ((buffer[j - 2] & 0XFC) >> 2)) & 0x3ff);
        }
      } else {
        frame = new BITalinoFrame();
        frame.setSequence(-1);
      }
      return frame;
    } catch (Exception e) {
      throw new BITalinoException(BITalinoErrorTypes.INCORRECT_DECODE);
    }
  }

  /**
   * 
   * Read defined number of samples from BITalino device.
   * 
   * @param: numberOfSamples number of samples
   * @return decoded frames
   * @see {@link BITalinoFrame}
   */
  public static BITalinoFrame[] read(final InputStream is,
      final int numberOfSamples, final int[] analogChannels,
      final int totalBytes) throws BITalinoException {

    try {
      BITalinoFrame[] frames = new BITalinoFrame[numberOfSamples];
      byte[] buffer = new byte[totalBytes];
      byte[] bTemp = new byte[1];
      int sampleCounter = 0;

      // parse frames
      while (sampleCounter < numberOfSamples) {
        // read number_bytes from buffer
        is.read(buffer, 0, totalBytes);
        // let's try to decode the buffer
        BITalinoFrame f = decode(buffer, analogChannels.length, totalBytes);
        System.out.println("Sample " + sampleCounter + " has sequence "
            + f.getSequence());
        // if CRC isn't valid, sequence equals -1
        if (f.getSequence() == -1) {
          // we're missing data, so let's wait and try to rebuild the buffer or
          // throw exception
          while (f.getSequence() == -1) {
            is.read(bTemp, 0, 1);
            for (int j = totalBytes - 2; j >= 0; j--)
              buffer[j + 1] = buffer[j];
            buffer[0] = bTemp[0];
            f = decode(buffer, analogChannels.length, totalBytes);
          }
        }
        frames[sampleCounter] = f;
        sampleCounter++;
      }
      return frames;
    } catch (Exception e) {
      throw new BITalinoException(BITalinoErrorTypes.LOST_COMMUNICATION);
    }
  }

  /**
   * Send a command to BITalino
   * 
   * @param data
   *          value to send to BITalino.
   */
  public static void write(final OutputStream os, final int data)
      throws BITalinoException {
    try {
      os.write(data);
      os.flush();
      Thread.sleep(1000);
    } catch (Exception e) {
      throw new BITalinoException(BITalinoErrorTypes.LOST_COMMUNICATION);
    }
  }

}