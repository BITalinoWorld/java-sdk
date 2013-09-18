package com.bitalino;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Abstract socket.
 */
public class BITalinoSocket {

  private InputStream is;
  private OutputStream os;

  public BITalinoSocket(final InputStream is, final OutputStream os) {
    checkNotNull(is, "Input stream was not provided.");
    checkNotNull(os, "Output stream was not provided.");
    this.is = is;
    this.os = os;
  }

  /**
   * Writes data to Bluetooth.
   * 
   * @param command
   * @throws BITalinoException
   */
  public void write(final int command) throws BITalinoException {
    BITalinoFrameCodec.write(os, command);
  }

  /**
   * Reads data from Bluetooth.
   * 
   * @param numberOfSamples
   * @return
   * @throws BITalinoException
   */
  public BITalinoFrame[] read(final int[] analogChannels, final int totalBytes,
      final int numberOfSamples) throws BITalinoException {
    return BITalinoFrameCodec.read(is, numberOfSamples, analogChannels,
        totalBytes);
  }

  /**
   * Releases any open resources.
   * 
   * @throws BITalinoException
   */
  public void close() throws BITalinoException {
    try {
      is.close();
      os.close();
    } catch (Exception e) {
      throw new BITalinoException(BITalinoErrorTypes.BT_DEVICE_NOT_CONNECTED);
    } finally {
      is = null;
      os = null;
    }
  }

  public InputStream getInputStream() {
    return is;
  }

  public OutputStream getOutputStream() {
    return os;
  }

}