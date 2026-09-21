package dev.felix.ellice.feature.studio;

import java.util.List;

public enum StudioMode {
   HEALTH("Player health", "Data", StudioMode.Type.NUMBER),
   HUNGER("Hunger", "Data", StudioMode.Type.NUMBER),
   FPS("Frame rate", "Data", StudioMode.Type.NUMBER),
   PING("Ping", "Data", StudioMode.Type.NUMBER),
   TIME("Time", "Data", StudioMode.Type.NUMBER),
   NUMBER("Number", "Values", StudioMode.Type.NUMBER),
   TEXT("Text", "Values", StudioMode.Type.TEXT),
   COLOR("Color", "Values", StudioMode.Type.COLOR),
   ADD("Add", "Math", StudioMode.Type.NUMBER, createPort("A", 0.0F), createPort("B", 1.0F)),
   MULTIPLY("Multiply", "Math", StudioMode.Type.NUMBER, createPort("A", 1.0F), createPort("B", 1.0F)),
   DIVIDE("Divide", "Math", StudioMode.Type.NUMBER, createPort("A", 1.0F), createPort("B", 20.0F)),
   CLAMP("Clamp 0–1", "Math", StudioMode.Type.NUMBER, createPort("Value", 0.0F)),
   WAVE("Sine wave", "Motion", StudioMode.Type.NUMBER, createPort("Time", 0.0F), createPort("Speed", 1.0F)),
   LESS("Less than", "Logic", StudioMode.Type.BOOLEAN, createPort("A", 0.0F), createPort("B", 8.0F)),
   GREATER("Greater than", "Logic", StudioMode.Type.BOOLEAN, createPort("A", 0.0F), createPort("B", 8.0F)),
   AND("Both true", "Logic", StudioMode.Type.BOOLEAN, createPort2("A"), createPort2("B")),
   NOT("Not", "Logic", StudioMode.Type.BOOLEAN, createPort2("Value")),
   FORMAT("Format text", "Transform", StudioMode.Type.TEXT, createPort("Value", 0.0F)),
   MIX_COLOR(
      "Blend colors",
      "Transform",
      StudioMode.Type.COLOR,
      createPort3("From"),
      createPort3("To"),
      createPort("Amount", 0.5F)
   ),
   FILL("Set progress", "Output", null, createPort("Value", 1.0F), createPort2("When")),
   TINT("Set color", "Output", null, createPort3("Value"), createPort2("When")),
   SHOW("Set visibility", "Output", null, createPort2("Value")),
   CAPTION("Set text", "Output", null, new StudioMode.Port("Value", StudioMode.Type.TEXT, 0.0F), createPort2("When")),
   SCALE("Set scale", "Output", null, createPort("Value", 1.0F), createPort2("When")),
   OPACITY("Set opacity", "Output", null, createPort("Value", 1.0F), createPort2("When")),
   POSITION_X("Set X", "Output", null, createPort("Value", 0.0F), createPort2("When")),
   POSITION_Y("Set Y", "Output", null, createPort("Value", 0.0F), createPort2("When")),
   MODULE_GATE("Switch a module", "Actions", null, createPort2("Enabled"));

   public final String title;
   public final String group;
   public final StudioMode.Type output;
   public final List<StudioMode.Port> ports;

   StudioMode(String text, String currentText, StudioMode.Type type, StudioMode.Port... currentPorts) {
      this.title = text;
      this.group = currentText;
      this.output = type;
      this.ports = List.of(currentPorts);
   }

   public boolean shapeOutput() {
      return this.output == null && this != MODULE_GATE;
   }

   public int color() {
      return this.output != null ? this.output.ink : (this == MODULE_GATE ? -737381 : -3557131);
   }

   public StudioMode.Port port(String text) {
      return this.ports.stream().filter(item -> item.name.equals(text)).findFirst().orElse(null);
   }

   private static StudioMode.Port createPort(String text, float value) {
      return new StudioMode.Port(text, StudioMode.Type.NUMBER, value);
   }

   private static StudioMode.Port createPort2(String text) {
      return new StudioMode.Port(text, StudioMode.Type.BOOLEAN, 1.0F);
   }

   private static StudioMode.Port createPort3(String text) {
      return new StudioMode.Port(text, StudioMode.Type.COLOR, 0.0F);
   }


   private static StudioMode[] $values() {
      return new StudioMode[]{
         HEALTH,
         HUNGER,
         FPS,
         PING,
         TIME,
         NUMBER,
         TEXT,
         COLOR,
         ADD,
         MULTIPLY,
         DIVIDE,
         CLAMP,
         WAVE,
         LESS,
         GREATER,
         AND,
         NOT,
         FORMAT,
         MIX_COLOR,
         FILL,
         TINT,
         SHOW,
         CAPTION,
         SCALE,
         OPACITY,
         POSITION_X,
         POSITION_Y,
         MODULE_GATE
      };
   }

   public record Port(String name, StudioMode.Type type, float fallback) {
   }

   public enum Type {
      NUMBER(-6366747),
      BOOLEAN(-5449555),
      TEXT(-2440970),
      COLOR(-737381);

      public final int ink;

      Type(int value) {
         this.ink = value;
      }


      private static StudioMode.Type[] $values() {
         return new StudioMode.Type[]{NUMBER, BOOLEAN, TEXT, COLOR};
      }
   }
}
