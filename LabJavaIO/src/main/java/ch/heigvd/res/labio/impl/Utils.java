package ch.heigvd.res.labio.impl;

import java.util.logging.Logger;

/**
 *
 * @author Olivier Liechti
 */
public class Utils {

  private static final Logger LOG = Logger.getLogger(Utils.class.getName());

  /**
   * This method looks for the next new line separators (\r, \n, \r\n) to extract
   * the next line in the string passed in arguments. 
   * 
   * @param lines a string that may contain 0, 1 or more lines
   * @return an array with 2 elements; the first element is the next line with
   * the line separator, the second element is the remaining text. If the argument does not
   * contain any line separator, then the first element is an empty string.
   */
  public static String[] getNextLine(String lines) {
    String tmp;
    String[] split = lines.split("(?<=(\r?\n|\n))",2);
    if(split.length != 2) {
      // Check if it's MACOS
      split = lines.split("(?<=(\r))",2);
      if(split.length != 2) {
        tmp = split[0];
        split = new String[2];
        split[1] = tmp;
        split[0] = "";
      }
    }
    return split;
  }

}
