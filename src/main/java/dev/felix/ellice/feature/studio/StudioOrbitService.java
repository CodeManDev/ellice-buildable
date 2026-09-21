package dev.felix.ellice.feature.studio;

public final class StudioOrbitService {
  private StudioOrbitService() {}

  public static StudioShapeService orbit() {
    StudioShapeService studioShape = new StudioShapeService();
    studioShape.name = "Vital orbit";
    StudioShapeService.Shape currentShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.PANEL,
            "Backdrop",
            24.0F,
            24.0F,
            352.0F,
            192.0F,
            -14208444);
    currentShape.finish = StudioShapeService.Finish.AURORA;
    currentShape.color2 = -10335357;
    currentShape.radius = 32.0F;
    StudioShapeService.Shape nextShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.RING,
            "Health orbit",
            52.0F,
            59.0F,
            118.0F,
            118.0F,
            -7019307);
    nextShape.color2 = -4083213;
    nextShape.finish = StudioShapeService.Finish.GRADIENT;
    nextShape.stroke = 9.0F;
    StudioShapeService.Shape previousShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.TEXT,
            "Health value",
            76.0F,
            98.0F,
            70.0F,
            40.0F,
            -724737);
    previousShape.text = "20";
    previousShape.fontSize = 30.0F;
    previousShape.alignment = StudioShapeService.TextAlignment.CENTER;
    StudioShapeService.Shape sourceShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.TEXT,
            "Title",
            196.0F,
            69.0F,
            158.0F,
            28.0F,
            -3555353);
    sourceShape.text = "VITAL SIGNS";
    sourceShape.fontSize = 12.0F;
    StudioShapeService.Shape targetShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.TEXT,
            "Player",
            196.0F,
            104.0F,
            158.0F,
            36.0F,
            -658945);
    targetShape.text = "{player.name}";
    targetShape.fontSize = 24.0F;
    StudioShapeService.Shape inputShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.TEXT,
            "Caption",
            196.0F,
            146.0F,
            158.0F,
            24.0F,
            -7227201);
    inputShape.text = "A little more alive.";
    inputShape.fontSize = 12.0F;
    StudioShapeService.Block currentBlock = block(studioShape, StudioMode.HEALTH, 32.0F, 48.0F);
    StudioShapeService.Block nextBlock = block(studioShape, StudioMode.DIVIDE, 266.0F, 48.0F);
    StudioShapeService.Block previousBlock = block(studioShape, StudioMode.FILL, 506.0F, 48.0F);
    nextBlock.inputs.put("A", currentBlock.id);
    previousBlock.inputs.put("Value", nextBlock.id);
    previousBlock.target = nextShape.id;
    StudioShapeService.Block sourceBlock = block(studioShape, StudioMode.FORMAT, 266.0F, 240.0F);
    StudioShapeService.Block targetBlock = block(studioShape, StudioMode.CAPTION, 506.0F, 240.0F);
    sourceBlock.text = "{value}";
    sourceBlock.inputs.put("Value", currentBlock.id);
    targetBlock.inputs.put("Value", sourceBlock.id);
    targetBlock.target = previousShape.id;
    return studioShape;
  }

  public static StudioShapeService pulse() {
    StudioShapeService studioShape = new StudioShapeService();
    studioShape.name = "Signal garden";
    StudioShapeService.Shape currentShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.PANEL,
            "Garden",
            64.0F,
            46.0F,
            272.0F,
            148.0F,
            -14470076);
    currentShape.finish = StudioShapeService.Finish.GRADIENT;
    currentShape.color2 = -12636079;
    currentShape.radius = 30.0F;
    StudioShapeService.Shape nextShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.ELLIPSE,
            "Breathing light",
            100.0F,
            98.0F,
            48.0F,
            48.0F,
            -6758963);
    nextShape.finish = StudioShapeService.Finish.AURORA;
    nextShape.color2 = -2182951;
    StudioShapeService.Shape previousShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.TEXT,
            "Title",
            184.0F,
            91.0F,
            124.0F,
            28.0F,
            -1251080);
    previousShape.text = "Breathe.";
    previousShape.fontSize = 24.0F;
    StudioShapeService.Shape sourceShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.TEXT,
            "Subtitle",
            184.0F,
            128.0F,
            124.0F,
            20.0F,
            -5002041);
    sourceShape.text = "Time becomes motion";
    sourceShape.fontSize = 10.0F;
    StudioShapeService.Block currentBlock = block(studioShape, StudioMode.TIME, 24.0F, 64.0F);
    StudioShapeService.Block nextBlock = block(studioShape, StudioMode.WAVE, 248.0F, 64.0F);
    StudioShapeService.Block previousBlock = block(studioShape, StudioMode.SCALE, 696.0F, 64.0F);
    nextBlock.inputs.put("Time", currentBlock.id);
    StudioShapeService.Block sourceBlock = block(studioShape, StudioMode.ADD, 472.0F, 64.0F);
    sourceBlock.inputs.put("A", nextBlock.id);
    previousBlock.inputs.put("Value", sourceBlock.id);
    previousBlock.target = nextShape.id;
    return studioShape;
  }

  public static StudioShapeService blank() {
    return new StudioShapeService();
  }

  public static StudioShapeService session() {
    StudioShapeService studioShape = new StudioShapeService();
    studioShape.name = "Session glass";
    studioShape.height = 160.0F;
    StudioShapeService.Shape currentShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.PANEL,
            "Card",
            32.0F,
            30.0F,
            336.0F,
            100.0F,
            -13946812);
    currentShape.finish = StudioShapeService.Finish.GRADIENT;
    currentShape.color2 = -10992804;
    currentShape.radius = 26.0F;
    StudioShapeService.Shape nextShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.TEXT,
            "Label",
            55.0F,
            50.0F,
            270.0F,
            20.0F,
            -4210471);
    nextShape.text = "IN YOUR ELEMENT";
    nextShape.fontSize = 11.0F;
    StudioShapeService.Shape previousShape =
        shape(
            studioShape,
            StudioShapeService.ShapeKind.TEXT,
            "Frame rate",
            55.0F,
            77.0F,
            270.0F,
            34.0F,
            -987655);
    previousShape.text = "144 FPS";
    previousShape.fontSize = 28.0F;
    StudioShapeService.Block currentBlock = block(studioShape, StudioMode.FPS, 32.0F, 72.0F);
    StudioShapeService.Block nextBlock = block(studioShape, StudioMode.FORMAT, 264.0F, 72.0F);
    StudioShapeService.Block previousBlock = block(studioShape, StudioMode.CAPTION, 496.0F, 72.0F);
    nextBlock.text = "{value} FPS";
    nextBlock.inputs.put("Value", currentBlock.id);
    previousBlock.inputs.put("Value", nextBlock.id);
    previousBlock.target = previousShape.id;
    return studioShape;
  }

  public static StudioShapeService.Shape shape(
      StudioShapeService studioShape,
      StudioShapeService.ShapeKind shapeKind,
      String text,
      float value,
      float currentValue,
      float nextValue,
      float previousValue,
      int sourceValue) {
    StudioShapeService.Shape currentShape = new StudioShapeService.Shape();
    currentShape.kind = shapeKind;
    currentShape.name = text;
    currentShape.x = value;
    currentShape.y = currentValue;
    currentShape.width = nextValue;
    currentShape.height = previousValue;
    currentShape.color = sourceValue;
    studioShape.shapes.add(currentShape);
    return currentShape;
  }

  public static StudioShapeService.Block block(
      StudioShapeService studioShape, StudioMode studioMode, float value, float currentValue) {
    StudioShapeService.Block currentBlock = new StudioShapeService.Block();
    currentBlock.kind = studioMode;
    currentBlock.x = value;
    currentBlock.y = currentValue;
    studioShape.blocks.add(currentBlock);
    return currentBlock;
  }
}
