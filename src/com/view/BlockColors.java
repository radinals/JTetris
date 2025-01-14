package com.view;

import java.awt.Color;
import java.util.HashMap;

import com.model.Shape;

final class BlockColors {

  public class BlockColor {

    private final int  base, border, highlight;

    public BlockColor(int base, int border, int highlight) {
      this.border = border;
      this.base = base;
      this.highlight = highlight;
    }
    
    public Color getBaseColor() {
      return new Color(base);
    }

    public Color getBorderColor() {
      return new Color(border);
    }
    
    public Color getHighlightColor() {
      return new Color(highlight);
    }

  }

  private HashMap<Integer, BlockColor> colors;
  
  private static BlockColors blockColors;
  
  private BlockColors() {
    blockColors = null;

    colors = new HashMap<Integer, BlockColors.BlockColor>();

    //                                  BASE     BORDER   HIGHLIGHT
    colors.put(Shape.I.getValue(),new BlockColor(0xe02525, 0xb71c1b, 0xff6e5b));
    colors.put(Shape.O.getValue(),new BlockColor(0xe8e80b, 0xb3b71b, 0xfdfd88));
    colors.put(Shape.T.getValue(),new BlockColor(0x0000ff, 0x1010d1, 0x6969ff));
    colors.put(Shape.S.getValue(),new BlockColor(0xff00ff, 0xb71bb6, 0xff71e9));
    colors.put(Shape.L.getValue(),new BlockColor(0xff6600, 0xb74c1b, 0xffa75b));
    colors.put(Shape.J.getValue(),new BlockColor(0x008000, 0x226413, 0x8be376));
    colors.put(Shape.Z.getValue(),new BlockColor(0x2affd5, 0x00caa2, 0xa6ffee));
  }
  
  public static BlockColors getInstance() {
    
    if (blockColors == null) {
      blockColors = new BlockColors();
    }
    
    return blockColors;
    
  }
  
  public BlockColor getColor(int shapeValue) {
    return colors.getOrDefault(shapeValue, null);
  }
  
}
