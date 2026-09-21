package dev.felix.ellice.feature.studio;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public final class StudioListenerService {
  public StudioShapeService project;
  public boolean logic;
  private String text3;
  private String text4;
  public String selected = "";
  public boolean playing = true;
  public boolean snap = true;
  public String message = "Drag shapes. Connect ideas.";
  private final Deque<String> fdamkadde8lx = new ArrayDeque();
  private final Deque<String> text2 = new ArrayDeque();
  private Runnable runnable = () -> {};

  public StudioListenerService(StudioShapeService studioShapeService) {
    this.project = studioShapeService.copy();
    this.text4 = this.project.snapshot();
  }

  public void listener(Runnable runnable) {
    this.runnable = runnable;
  }

  public void notifyChanged() {
    this.runnable.run();
  }

  public boolean dirty() {
    return !this.text4.equals(this.project.snapshot());
  }

  public void saved() {
    this.text4 = this.project.snapshot();
    this.message = "Draft saved";
    this.runnable.run();
  }

  public void select(String str) {
    this.selected = str == null ? "" : str;
    this.runnable.run();
  }

  public StudioShapeService.Shape shape() {
    return this.project.shape(this.selected);
  }

  public StudioShapeService.Block block() {
    return this.project.block(this.selected);
  }

  public void edit(Consumer<StudioShapeService> consumer) {
    beginGesture();
    consumer.accept(this.project);
    endGesture();
  }

  public void beginGesture() {
    if (this.text3 == null) {
      this.text3 = this.project.snapshot();
    }
  }

  public boolean gestureActive() {
    return this.text3 != null;
  }

  public void endGesture() {
    if (this.text3 != null && !this.text3.equals(this.project.snapshot())) {
      this.fdamkadde8lx.push(this.text3);
      while (this.fdamkadde8lx.size() > 48) {
        this.fdamkadde8lx.removeLast();
      }
      this.text2.clear();
    }
    this.text3 = null;
    this.runnable.run();
  }

  public void cancelGesture() {
    if (this.text3 != null) {
      this.project = StudioShapeService.restore(this.text3);
    }
    this.text3 = null;
    this.runnable.run();
  }

  public boolean canUndo() {
    return !this.fdamkadde8lx.isEmpty();
  }

  public boolean canRedo() {
    return !this.text2.isEmpty();
  }

  public void undo() {
    if (this.fdamkadde8lx.isEmpty()) {
      return;
    }
    this.text2.push(this.project.snapshot());
    this.project = StudioShapeService.restore(this.fdamkadde8lx.pop());
    this.selected = "";
    this.message = "Undone";
    this.runnable.run();
  }

  public void redo() {
    if (this.text2.isEmpty()) {
      return;
    }
    this.fdamkadde8lx.push(this.project.snapshot());
    this.project = StudioShapeService.restore(this.text2.pop());
    this.selected = "";
    this.message = "Redone";
    this.runnable.run();
  }

  public void addShape(StudioShapeService.ShapeKind shapeKind, float f, float f2) {
    if (this.project.shapes.size() < 64) {
      edit(
          studioShapeService -> {
            StudioShapeService.Shape shape =
                StudioOrbitService.shape(
                    studioShapeService,
                    shapeKind,
                    title(shapeKind),
                    StudioValidateValidator.clamp(
                        grid(f),
                        Float.intBitsToFloat(-981860352),
                        Float.intBitsToFloat(1165623296)),
                    StudioValidateValidator.clamp(
                        grid(f2),
                        Float.intBitsToFloat(-981860352),
                        Float.intBitsToFloat(1165623296)),
                    shapeKind == StudioShapeService.ShapeKind.TEXT
                        ? Float.intBitsToFloat(1127481344)
                        : Float.intBitsToFloat(1120403456),
                    shapeKind == StudioShapeService.ShapeKind.TEXT
                        ? Float.intBitsToFloat(1108344832)
                        : Float.intBitsToFloat(1120403456),
                    -4213519);
            if (shapeKind == StudioShapeService.ShapeKind.BAR) {
              shape.width = Float.intBitsToFloat(1127481344);
              shape.height = Float.intBitsToFloat(1099956224);
              shape.radius = Float.intBitsToFloat(1091567616);
            }
            if (shapeKind == StudioShapeService.ShapeKind.GROUP) {
              shape.width = Float.intBitsToFloat(1128792064);
              shape.height = Float.intBitsToFloat(1124859904);
            }
            this.selected = shape.id;
            this.logic = false;
            this.message = "Added " + shape.name;
          });
    } else {
      this.message = "This project has reached 64 shapes.";
      this.runnable.run();
    }
  }

  public void addBlock(StudioMode studioMode, float f, float f2) {
    if (this.project.blocks.size() < 96) {
      edit(
          studioShapeService -> {
            StudioShapeService.Block block =
                StudioOrbitService.block(
                    studioShapeService,
                    studioMode,
                    StudioValidateValidator.clamp(
                        grid(f),
                        Float.intBitsToFloat(-969179136),
                        Float.intBitsToFloat(1178304512)),
                    StudioValidateValidator.clamp(
                        grid(f2),
                        Float.intBitsToFloat(-969179136),
                        Float.intBitsToFloat(1178304512)));
            if (studioMode.shapeOutput() && !studioShapeService.shapes.isEmpty()) {
              block.target = ((StudioShapeService.Shape) studioShapeService.shapes.getLast()).id;
            }
            this.selected = block.id;
            this.logic = true;
            this.message = "Added " + studioMode.title;
          });
    } else {
      this.message = "This project has reached 96 blocks.";
      this.runnable.run();
    }
  }

  public void connect(String str, String str2, String str3) {
    String strConnectionError =
        StudioValidateValidator.connectionError(this.project, str, str2, str3);
    if (strConnectionError != null) {
      this.message = strConnectionError;
      this.runnable.run();
    } else {
      edit(
          studioShapeService -> {
            studioShapeService.block(str2).inputs.put(str3, str);
          });
      this.message = "Connected";
      this.runnable.run();
    }
  }

  public void disconnect(String str, String str2) {
    edit(
        studioShapeService -> {
          studioShapeService.block(str).inputs.remove(str2);
        });
  }

  public void delete() {
    StudioShapeService.Shape shape = shape();
    StudioShapeService.Block block = block();
    if (shape == null && block == null) {
      return;
    }
    if (shape == null || !shape.locked) {
      edit(
          studioShapeService -> {
            boolean zAdd;
            if (shape != null) {
              HashSet hashSet = new HashSet();
              hashSet.add(shape.id);
              do {
                zAdd = false;
                for (StudioShapeService.Shape shape2 : studioShapeService.shapes) {
                  if (hashSet.contains(shape2.parent)) {
                    zAdd |= hashSet.add(shape2.id);
                  }
                }
              } while (zAdd);
              studioShapeService.shapes.removeIf(
                  shape3 -> {
                    return hashSet.contains(shape3.id);
                  });
              studioShapeService.blocks.removeIf(
                  block2 -> {
                    return block2.kind.shapeOutput() && hashSet.contains(block2.target);
                  });
            } else {
              studioShapeService.blocks.remove(block);
              Iterator<StudioShapeService.Block> it = studioShapeService.blocks.iterator();
              while (it.hasNext()) {
                Collection<String> collectionValues = it.next().inputs.values();
                String str = block.id;
                Objects.requireNonNull(str);
                collectionValues.removeIf(
                    inputId -> {
                      return str.equals(inputId);
                    });
              }
            }
            this.selected = "";
            this.message = "Deleted · Undo is available";
          });
    } else {
      this.message = "Unlock this shape before deleting it.";
      this.runnable.run();
    }
  }

  public void duplicate() {
    boolean zAdd;
    StudioShapeService.Shape shape = shape();
    StudioShapeService.Block block = block();
    if (shape == null || this.project.shapes.size() >= 64) {
      if (block == null || this.project.blocks.size() >= 96) {
        return;
      }
      edit(
          studioShapeService -> {
            StudioShapeService.Block block2 = studioShapeService.copy().block(block.id);
            block2.id = UUID.randomUUID().toString();
            block2.x =
                StudioValidateValidator.clamp(
                    block2.x + Float.intBitsToFloat(1107296256),
                    Float.intBitsToFloat(-969179136),
                    Float.intBitsToFloat(1178304512));
            block2.y =
                StudioValidateValidator.clamp(
                    block2.y + Float.intBitsToFloat(1107296256),
                    Float.intBitsToFloat(-969179136),
                    Float.intBitsToFloat(1178304512));
            studioShapeService.blocks.add(block2);
            this.selected = block2.id;
          });
      return;
    }
    LinkedHashSet linkedHashSet = new LinkedHashSet();
    linkedHashSet.add(shape.id);
    do {
      zAdd = false;
      for (StudioShapeService.Shape shape2 : this.project.shapes) {
        if (linkedHashSet.contains(shape2.parent)) {
          zAdd |= linkedHashSet.add(shape2.id);
        }
      }
    } while (zAdd);
    List<StudioShapeService.Block> list =
        this.project.blocks.stream()
            .filter(
                block2 -> {
                  return block2.kind.shapeOutput() && linkedHashSet.contains(block2.target);
                })
            .toList();
    if (this.project.shapes.size() + linkedHashSet.size() <= 64
        && this.project.blocks.size() + list.size() <= 96) {
      edit(
          studioShapeService2 -> {
            StudioShapeService studioShapeServiceCopy = studioShapeService2.copy();
            HashMap map = new HashMap();
            Iterator it = linkedHashSet.iterator();
            while (it.hasNext()) {
              map.put((String) it.next(), UUID.randomUUID().toString());
            }
            for (StudioShapeService.Shape shape3 : studioShapeServiceCopy.shapes) {
              if (linkedHashSet.contains(shape3.id)) {
                boolean zEquals = shape3.id.equals(shape.id);
                shape3.id = (String) map.get(shape3.id);
                shape3.parent = (String) map.getOrDefault(shape3.parent, shape3.parent);
                if (zEquals) {
                  shape3.name = createText(shape.name + " copy", 48);
                  shape3.x =
                      StudioValidateValidator.clamp(
                          shape3.x + Float.intBitsToFloat(1098907648),
                          Float.intBitsToFloat(-981860352),
                          Float.intBitsToFloat(1165623296));
                  shape3.y =
                      StudioValidateValidator.clamp(
                          shape3.y + Float.intBitsToFloat(1098907648),
                          Float.intBitsToFloat(-981860352),
                          Float.intBitsToFloat(1165623296));
                }
                studioShapeService2.shapes.add(shape3);
              }
            }
            for (StudioShapeService.Block block3 : studioShapeServiceCopy.blocks) {
              if (block3.kind.shapeOutput() && linkedHashSet.contains(block3.target)) {
                block3.id = UUID.randomUUID().toString();
                block3.target = (String) map.get(block3.target);
                block3.y =
                    StudioValidateValidator.clamp(
                        block3.y + Float.intBitsToFloat(1107296256),
                        Float.intBitsToFloat(-969179136),
                        Float.intBitsToFloat(1178304512));
                studioShapeService2.blocks.add(block3);
              }
            }
            this.selected = (String) map.get(shape.id);
            this.message = "Duplicated with bindings";
          });
    } else {
      this.message = "Not enough room to duplicate this group and its bindings.";
      this.runnable.run();
    }
  }

  public void reorder(int i) {
    StudioShapeService.Shape shape = shape();
    if (shape == null) {
      return;
    }
    int iIndexOf = this.project.shapes.indexOf(shape);
    int iMax = Math.max(0, Math.min(this.project.shapes.size() - 1, iIndexOf + i));
    if (iIndexOf != iMax) {
      edit(
          studioShapeService -> {
            studioShapeService.shapes.remove(iIndexOf);
            studioShapeService.shapes.add(iMax, shape);
          });
    }
  }

  public String parentError(String str) {
    StudioShapeService.Shape shape = shape();
    if (shape == null) {
      return "Select a shape.";
    }
    StudioShapeService.Shape shape2 = this.project.shape(str);
    if (shape2 == null && !str.isEmpty()) {
      return "Choose a group.";
    }
    if (shape2 != null && shape2.kind != StudioShapeService.ShapeKind.GROUP) {
      return "Choose a group.";
    }
    while (shape2 != null) {
      if (shape2.id.equals(shape.id)) {
        return "A group cannot contain itself.";
      }
      shape2 = this.project.shape(shape2.parent);
    }
    return null;
  }

  public float grid(float f) {
    return this.snap ? Math.round(f / Float.intBitsToFloat(1090519040)) * 8 : f;
  }

  public static String title(StudioShapeService.ShapeKind shapeKind) {
    String lowerCase = shapeKind.name().toLowerCase(Locale.ROOT);
    return Character.toUpperCase(lowerCase.charAt(0)) + lowerCase.substring(1);
  }

  private static String createText(String str, int i) {
    return str.length() > i ? str.substring(0, i) : str;
  }
}
