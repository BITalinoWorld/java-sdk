package com.bitalino;

import com.google.common.base.Objects;

public class BITalinoFrame {
  private int crc;
  private int seq;
  private int[] analog = new int[6];
  private int[] digital = new int[4];

  public BITalinoFrame() {
  }

  public int getCRC() {
    return crc;
  }

  public void setCRC(int cRC) {
    crc = cRC;
  }

  public int getSequence() {
    return seq;
  }

  public void setSequence(int seq) {
    this.seq = seq;
  }

  public int getAnalog(final int pos) {
    return analog[pos];
  }

  public void setAnalog(final int pos, final int value)
      throws IndexOutOfBoundsException {
    this.analog[pos] = value;
  }

  public int getDigital(final int pos) {
    return digital[pos];
  }

  public void setDigital(final int pos, final int value)
      throws IndexOutOfBoundsException {
    this.digital[pos] = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof BITalinoFrame) {
      BITalinoFrame that = (BITalinoFrame) obj;
      return Objects.equal(this.crc, that.crc)
          && Objects.equal(this.seq, that.seq);
    }
    return false;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).add("crc", crc).add("seq", seq)
        .add("analog", analog).add("digital", digital).toString();
  }

}