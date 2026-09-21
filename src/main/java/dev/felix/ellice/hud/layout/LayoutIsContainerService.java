package dev.felix.ellice.hud.layout;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public final class LayoutIsContainerService {
  public static final float AUTO_SIZE = -1.0F;
  public String id;
  public String type;
  public LayoutCodec anchor = LayoutCodec.TOP_LEFT;
  public float offsetX;
  public float offsetY;
  public float width = -1.0F;
  public float height = -1.0F;
  public float rotation = 0.0F;
  public float opacity = 1.0F;
  public boolean visible = true;
  public boolean locked = false;
  public JsonObject props = new JsonObject();
  public List<LayoutIsContainerService> children;

  public LayoutIsContainerService() {}

  public LayoutIsContainerService(String text, String currentText) {
    this.id = text;
    this.type = currentText;
  }

  public boolean isContainer() {
    return this.children != null;
  }

  public List<LayoutIsContainerService> ensureChildren() {
    if (this.children == null) {
      this.children = new ArrayList<>();
    }

    return this.children;
  }
}
