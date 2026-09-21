package dev.felix.ellice.changelog;

public record ChangelogData(ChangelogFeatureType type, String text) {
   public ChangelogData(ChangelogFeatureType type, String text) {
      type = type == null ? ChangelogFeatureType.CHANGED : type;
      text = text == null ? "" : text;
      this.type = type;
      this.text = text;
   }
}
