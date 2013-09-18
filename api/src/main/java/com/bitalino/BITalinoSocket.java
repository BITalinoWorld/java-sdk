package com.bitalino;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Abstract socket.
 */
final class BITalinoSocket {

  private DataInputStream dis;
  private OutputStream os;

  public BITalinoSocket(final DataInputStream is, final OutputStream os) {
    checkNotNull(is, "Input stream was not provided.");
    checkNotNull(os, "Output stream was not provided.");
    this.dis = is;
    this.os = os;
  }

  /**
   * Reads data from Bluetooth.
   * 
   * @param numberOfSamples
   *          the number of samples to read
   * @param analogChannels
   *          the analog channels to read from
   * @param totalBytes
   *          total available bytes to read
   * @return an array of decoded {@link BITalinoFrame}s.
   * @throws BITalinoException
   */
  public BITalinoFrame[] read(final int[] analogChannels, final int totalBytes,
      final int numberOfSamples) throws BITalinoException {
    try {
      BITalinoFrame[] frames = new BITalinoFrame[numberOfSamples];
      byte[] buffer = new byte[totalBytes];
      byte[] bTemp = new byte[1];
      int sampleCounter = 0;

      // parse frames
      while (sampleCounter < numberOfSamples) {
        System.out.println("Reading sample " + sampleCounter);
        final long start = System.currentTimeMillis();
        // read number_bytes from buffer
        dis.readFully(buffer, 0, totalBytes);
        // let's try to decode the buffer
        BITalinoFrame f = BITalinoFrameDecoder.decode(buffer,
            analogChannels.length, totalBytes);
        // if CRC isn't valid, sequence equals -1
        if (f.getSequence() == -1) {
          // we're missing data, so let's wait and try to rebuild the buffer or
          // throw exception
          System.out
              .println("Missed a sequence. Are we too far from BITalino? Retrying..");
          while (f.getSequence() == -1) {
            dis.readFully(bTemp, 0, 1);
            for (int j = totalBytes - 2; j >= 0; j--)
              buffer[j + 1] = buffer[j];
            buffer[0] = bTemp[0];
            f = BITalinoFrameDecoder.decode(buffer, analogChannels.length,
                totalBytes);
          }
        }
        final long elapsed = System.currentTimeMillis() - start;
        System.out.println("Sample frame decoded in " + elapsed + " ms");
        frames[sampleCounter] = f;
        sampleCounter++;
      }
      return frames;
    } catch (Exception e) {
      throw new BITalinoException(BITalinoErrorTypes.LOST_COMMUNICATION);
    }
  }

  /**
   * Writes data to Bluetooth.
   * 
   * @param command
   * @throws BITalinoException
   */
  public void write(final int data) throws BITalinoException {
    try {
      os.write(data);
      os.flush();
      Thread.sleep(1000);
    } catch (Exception e) {
      throw new BITalinoException(BITalinoErrorTypes.LOST_COMMUNICATION);
    }
  }

  /**
   * Releases any open resources.
   * 
   * @throws BITalinoException
   */
  public void close() throws BITalinoException {
    try {
      dis.close();
      os.close();
    } catch (Exception e) {
      throw new BITalinoException(BITalinoErrorTypes.BT_DEVICE_NOT_CONNECTED);
    } finally {
      dis = null;
      os = null;
    }
  }

  public InputStream getInputStream() {
    return dis;
  }

  public OutputStream getOutputStream() {
    return os;
  }

}