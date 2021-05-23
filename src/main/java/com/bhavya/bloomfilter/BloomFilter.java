package com.bhavya.bloomfilter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter<E> {
  private int noOfHashes;
  private int maxNoOfElements;
  private int numberOfAddedElements;
  private BitSet bitSet;
  private int bitSetSize;
  private static final MessageDigest digestFunction;

  static {
    MessageDigest tmp;
    try {
      tmp = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      System.out.println("MD5 hash algorithm is not found");
      tmp = null;
    }
    digestFunction = tmp;
  }

  public BloomFilter(double noOfBitsPerElement, int maxNoOfElements, int noOfHashes) {
    this.maxNoOfElements = maxNoOfElements;
    this.noOfHashes = noOfHashes;
    this.bitSetSize = (int) Math.ceil(noOfBitsPerElement * maxNoOfElements);
    this.bitSet = new BitSet(bitSetSize);
  }

  public BloomFilter(int bitSetSize, int maxNoOfElements) {
    this(Math.ceil(bitSetSize/(double) maxNoOfElements),
      maxNoOfElements,
      (int) (Math.round(bitSetSize/(double) maxNoOfElements) * Math.log(2.0)));
  }

  public void add(E element) {
    add(element.toString().getBytes(StandardCharsets.UTF_8));
  }

  public boolean contains(E element) {
    return contains(element.toString().getBytes(StandardCharsets.UTF_8));
  }

// private method helpers

  public static int[] createHashes(byte[] data, int hashes) {
    int[] result = new int[hashes];
    int k = 0;
    byte salt = 0;

    while (k < hashes) {
      byte[] digest;
      synchronized (digestFunction) {
        digestFunction.update(salt);
        salt++;
        digest = digestFunction.digest(data);
      }

      for (int i = 0; i < digest.length/4 && k < hashes; i++) {
        int h = 0;
        for(int j = (i * 4); j < (i * 4) + 4; j++) {
          h <<= 8;
          h |= ((int)digest[j]) & 0xFF;
        }
        result[k] = h;
        k++;
      }
    }
    return result;
  }

  private void add(byte[] bytes) {
    int[] hashes = createHashes(bytes, noOfHashes);
    for (int hash : hashes) {
      bitSet.set(Math.abs(hash % bitSetSize), true);
    }
    numberOfAddedElements++;
  }

  private boolean contains(byte[] bytes) {
    int[] hashes = createHashes(bytes, noOfHashes);
    for (int hash : hashes) {
      if (!bitSet.get(Math.abs(hash %  bitSetSize))) {
        return false;
      }
    }
    return true;
  }
}
