import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataField {
  public String tag;
  public char indicator1;
  public char indicator2;
  public Map<String, List<String>> values;

  public DataField(String tag, char indicator1, char indicator2) {
    this.tag = tag;
    this.indicator1 = indicator1;
    this.indicator2 = indicator2;
    this.values = new HashMap<>();
  }

  public void addValue(String data) {
    System.out.println("addValue: |" + data + "|");
    String subfield = data.substring(0, 1);
    List<String> vals = values.get(subfield);
    if (vals == null) { vals = new ArrayList<>(); }
    vals.add(data.substring(1));
    values.put(subfield, vals);
  }

  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(tag);
    buf.append(" ");
    buf.append(indicator1);
    buf.append(indicator2);
    for (String subfield : values.keySet()) {
      List<String> vals = values.get(subfield);
      for (String val : vals) {
        buf.append(" " + subfield + ":" + val);
      }
    }
    return buf.toString();
  }
}
