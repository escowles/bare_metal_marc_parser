import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

public class metal {

  public static int DIRECTORY_LENGTH = 5;
  public static int DIRECTORY_POSITION = 12;
  public static int DIRECTORY_ENTRY_LENGTH = 12;
  public static int LEADER_LENGTH = 24;
  public static int RECORD_SEPARATOR = 0x1d;
  public static int SUBFIELD_DELIM = 0x1f;
  public static String FIELD_TERMINATOR = String.valueOf((char)0x1e);

  public static void main(String[] args) throws FileNotFoundException, IOException {
    BufferedReader in = new BufferedReader(new FileReader(args[0]));

    StringBuilder buf = new StringBuilder();
    int i = 0;
    while (i != -1) {
      i = in.read();
      if (i == RECORD_SEPARATOR) {
        MarcRecord marc = parse_record(buf);
        System.out.println(marc.toString());
        buf.setLength(0);
      } else {
        buf.append((char)i);
      }
    }
  }

  public static MarcRecord parse_record(StringBuilder buf) {
    System.out.println("parse_record: |" + buf.toString() + "|");
    String leader = buf.substring(0, LEADER_LENGTH);
    MarcRecord marc = new MarcRecord(leader);

    int dirlen = Integer.parseInt(buf.substring(DIRECTORY_POSITION, DIRECTORY_LENGTH+DIRECTORY_POSITION));
    String directory = buf.substring(LEADER_LENGTH, dirlen - LEADER_LENGTH - 1);
    int field_count = directory.length() / DIRECTORY_ENTRY_LENGTH;
    for (int i = 0; i < (field_count -1); i++) {
      int entry_start = i * DIRECTORY_ENTRY_LENGTH;
      int entry_end = entry_start + DIRECTORY_ENTRY_LENGTH;
      String entry = directory.substring(entry_start, entry_end);
      String tag = entry.substring(0, 3);
      int field_length = Integer.parseInt(entry.substring(3, 7));
      int field_offset = Integer.parseInt(entry.substring(7, 12)) + dirlen;
      String field_data = buf.substring(field_offset, field_offset+field_length-1);
      if (tag.startsWith("00")) {
        marc.addControlField(parse_control_field(tag, field_data));
      } else {
        marc.addDataField(parse_data_field(tag, field_data));
      }
    }
    return marc;
  }

  public static ControlField parse_control_field(String tag, String data) {
    return new ControlField(tag, data.substring(0, data.length()-1));
  }
  public static DataField parse_data_field(String tag, String data) {
    System.out.println("XXX: " + tag + "|" + data + "|");
    DataField df = new DataField(tag, data.charAt(0), data.charAt(1));
    int last = 3;
    for (int i = 3; i < data.length() - 1; i++) {
      if (data.charAt(i) == SUBFIELD_DELIM) {
        df.addValue(data.substring(last, i));
        last = i + 1;
      }
    }

    // sometimes last value ends with ^^ (0x1e)
    if (data.endsWith(FIELD_TERMINATOR)) {
      df.addValue(data.substring(last, data.length() - 1));
    } else {
      df.addValue(data.substring(last, data.length()));
    }
    return df;
  }
}
