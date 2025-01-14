package com.model;

public enum Shape { 
  I(1),O(2),T(3),S(4),L(5),J(6),Z(7);
  
  private int value;
  
  public int getValue() {
    return value;
  }
  
  public static Shape shapeOf(int value) {
    try {
      if (value <= 0) return null;
      return values()[value-1];
    } catch (Exception e) {
      return null;
    }
  }
  
  private Shape(int value) {
    this.value = value;
  }

}
