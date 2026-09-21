package dev.felix.ellice.ui.screen.builtin.keybind;

import dev.felix.ellice.module.ModuleBindService;
import java.util.List;
import java.util.Locale;

public final class KeyCatalog {
   public static final List<List<KeyCatalog.Key>> MAIN = List.of(
      List.of(
         key(256),
         gap(1.0F),
         key(290),
         key(291),
         key(292),
         key(293),
         gap(0.5F),
         key(294),
         key(295),
         key(296),
         key(297),
         gap(0.5F),
         key(298),
         key(299),
         key(300),
         key(301)
      ),
      List.of(
         key(96),
         key(49),
         key(50),
         key(51),
         key(52),
         key(53),
         key(54),
         key(55),
         key(56),
         key(57),
         key(48),
         key(45),
         key(61),
         key(259, 2.0F)
      ),
      List.of(
         key(258, 1.5F),
         key(81),
         key(87),
         key(69),
         key(82),
         key(84),
         key(89),
         key(85),
         key(73),
         key(79),
         key(80),
         key(91),
         key(93),
         key(92, 1.5F)
      ),
      List.of(
         key(280, 1.75F),
         key(65),
         key(83),
         key(68),
         key(70),
         key(71),
         key(72),
         key(74),
         key(75),
         key(76),
         key(59),
         key(39),
         key(257, 2.25F)
      ),
      List.of(
         key(340, 2.25F),
         key(90),
         key(88),
         key(67),
         key(86),
         key(66),
         key(78),
         key(77),
         key(44),
         key(46),
         key(47),
         key(344, 2.75F)
      ),
      List.of(
         key(341, 1.25F),
         key(343, 1.25F),
         key(342, 1.25F),
         key(32, 6.25F),
         key(346, 1.25F),
         key(347, 1.25F),
         key(348, 1.25F),
         key(345, 1.25F)
      )
   );
   public static final List<List<KeyCatalog.Key>> NAV = List.of(
      List.of(key(283), key(281), key(284)),
      List.of(key(260), key(268), key(266)),
      List.of(key(261), key(269), key(267)),
      List.of(gap(3.0F)),
      List.of(gap(1.0F), key(265), gap(1.0F)),
      List.of(key(263), key(264), key(262))
   );

   private KeyCatalog() {
   }

   public static KeyCatalog.Key key(int value) {
      return new KeyCatalog.Key(value, name(value), 1.0F);
   }

   public static KeyCatalog.Key key(int value, float currentValue) {
      return new KeyCatalog.Key(value, name(value), currentValue);
   }

   public static KeyCatalog.Key gap(float value) {
      return new KeyCatalog.Key(-1, "", value);
   }

   public static String name(int value) {
      if (value < 0) {
         return "Unassigned";
      }

      if ((value < 65 || value > 90) && (value < 48 || value > 57)) {
         if (value >= 290 && value <= 314) {
            return "F" + (value - 290 + 1);
         }

         if (value >= 320 && value <= 329) {
            return "Num " + (value - 320);
         }

         return switch (value) {
            case 32 -> "Space";
            case 39 -> "'";
            case 44 -> ",";
            case 45 -> "−";
            case 46 -> ".";
            case 47 -> "/";
            case 59 -> ";";
            case 61 -> "=";
            case 91 -> "[";
            case 92 -> "\\";
            case 93 -> "]";
            case 96 -> "`";
            case 161 -> "Intl 1";
            case 162 -> "Intl 2";
            case 256 -> "Esc";
            case 257 -> "Enter";
            case 258 -> "Tab";
            case 259 -> "Backspace";
            case 260 -> "Ins";
            case 261 -> "Del";
            case 262 -> "→";
            case 263 -> "←";
            case 264 -> "↓";
            case 265 -> "↑";
            case 266 -> "PgUp";
            case 267 -> "PgDn";
            case 268 -> "Home";
            case 269 -> "End";
            case 280 -> "Caps";
            case 281 -> "ScrLk";
            case 282 -> "NumLk";
            case 283 -> "PrtSc";
            case 284 -> "Pause";
            case 330 -> "Num .";
            case 331 -> "Num /";
            case 332 -> "Num ×";
            case 333 -> "Num −";
            case 334 -> "Num +";
            case 335 -> "Num Enter";
            case 336 -> "Num =";
            case 340 -> "L Shift";
            case 341 -> "L Ctrl";
            case 342 -> "L Alt";
            case 343 -> "L Super";
            case 344 -> "R Shift";
            case 345 -> "R Ctrl";
            case 346 -> "R Alt";
            case 347 -> "R Super";
            case 348 -> "Menu";
            default -> "Key " + value;
         };
      } else {
         return Character.toString(value);
      }
   }

   public static boolean valid(int value) {
      return value == -1 || ModuleBindService.isKeyboardKey(value);
   }

   public static String displayLegend(String text, int value) {
      if (text == null || text.isBlank()) {
         return name(value);
      } else {
         return text.equals("ß") ? text : text.toUpperCase(Locale.ROOT);
      }
   }

   public static String reserved(int value) {
      return switch (value) {
         case 256 -> "Close / cancel";
         case 293 -> "Performance overlay";
         case 295 -> "Performance recording";
         case 297, 345 -> "ellice HUD editor";
         case 344 -> "ellice ClickGUI";
         default -> "";
      };
   }

   public record Key(int code, String legend, float units) {
      public boolean spacer() {
         return this.code < 0;
      }
   }
}
