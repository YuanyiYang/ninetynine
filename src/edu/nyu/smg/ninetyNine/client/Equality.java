package edu.nyu.smg.ninetyNine.client;

import com.google.common.base.*;
/**
 * An instance of this class must have an immutable ID.
 * That ID is used to define equals and hashCode.
 */
public abstract class Equality {

  public abstract Object getId();

  @Override
  public final boolean equals(Object other) {
    if (!(other instanceof Equality)) {
      return false;
    }
    return Objects.equal(getId(), ((Equality) other).getId());
  }

  @Override
  public final int hashCode() {
    return getId().hashCode();
  }
}