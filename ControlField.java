public class ControlField {
  public String tag;
  public String value;

  public ControlField(String tag, String value) {
    this.tag = tag;
    this.value = value;
  }

  public String toString() {
    return tag + ": " + value;
  }
}
