import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarcRecord {
  public String leader;
  public Map<String, ControlField> controlFields;
  public Map<String, List<DataField>> dataFields;

  public MarcRecord(String leader) {
    this.leader = leader;
    this.controlFields = new HashMap<>();
    this.dataFields = new HashMap<>();
  }

  public void addControlField(ControlField cf) {
    controlFields.put(cf.tag, cf);
  }

  public void addDataField(DataField df) {
    List<DataField> fields = dataFields.get(df.tag);
    if (fields == null) { fields = new ArrayList<>(); }
    fields.add(df);
    dataFields.put(df.tag, fields);
  }

  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append(leader);
    buf.append("\n");
    for (ControlField field : controlFields.values()) {
      buf.append(field.toString());
      buf.append("\n");
    }
    for (List<DataField> values : dataFields.values()) {
      for (DataField field : values) {
        buf.append(field.toString());
        buf.append("\n");
      }
    }
    return buf.toString();
  }
}
