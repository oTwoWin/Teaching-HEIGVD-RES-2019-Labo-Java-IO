package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;
import ch.heigvd.res.labio.impl.Utils;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  private int counter = 1;
  private boolean firstLine = true;
  private boolean readR = false;

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    int nbCharToAdd = 0;
    String tmp = str;
    str = "";
    String[] split;
    tmp = tmp.substring(off,off + len);
    if(firstLine){
      firstLine = false;
      tmp = counter + "\t" + tmp;
      ++counter;
      nbCharToAdd += 2;
    }

    while(!(split = Utils.getNextLine(tmp))[0].equals("")){
      str += split[0] + counter + "\t";
      tmp = split[1];
      nbCharToAdd += 1 + String.valueOf(counter).length();
      ++counter;
    }
    //if the last line doesn't have new line but contains text, add it to the String
    if(!split[1].equals("")){
      str += split[1];
    }
    super.write(str, 0, len + nbCharToAdd);

  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
      write(new String(cbuf), off, len);
  }

  @Override
  public void write(int c) throws IOException {
    char cara = (char) c;
    // test each possible case
    if(cara == '\r'){
      readR = true;
      super.write(c);
    } else if (cara == '\n'){
      String str = "\n" + counter + "\t" ;
      super.write(str, 0, 2 + String.valueOf(counter).length());
      readR = false;
      ++counter;
    } else if(firstLine || readR == true && cara != '\n') {
      firstLine = false;
      readR = false;
      String str = counter + "\t" + cara;
      super.write(str, 0, 2 + String.valueOf(counter).length());
      ++counter;
    } else {
      super.write(c);
    }
  }


}
