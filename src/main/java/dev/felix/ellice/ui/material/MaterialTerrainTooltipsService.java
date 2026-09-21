



package dev.felix.ellice.ui.material;

import dev.felix.ellice.ui.text.TextTextureService;
import dev.felix.ellice.render.compositor.CompositorPushPresentationScaleService;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;

public final class MaterialTerrainTooltipsService extends SceneCornerRadiusService
{
    private boolean f6lkivw85ylc;
    private boolean fai0j4bbpsa0;
    
    public MaterialTerrainTooltipsService terrainTooltips(final boolean fai0j4bbpsa0) {
        this.fai0j4bbpsa0 = fai0j4bbpsa0;
        return this;
    }
    
    public boolean terrainTooltips() {
        return this.fai0j4bbpsa0;
    }
    
    @Override
    protected void draw(final CompositorPushPresentationScaleService compositorPushPresentationScaleService) {
        compositorPushPresentationScaleService.requestFontFamily("material-roboto", TextTextureService.FontSource.CLASSPATH, "/assets/ellice/fonts/material/Roboto-Regular.ttf");
        compositorPushPresentationScaleService.requestFontFamily("material-roboto-medium", TextTextureService.FontSource.CLASSPATH, "/assets/ellice/fonts/material/Roboto-Medium.ttf");
        final boolean f6lkivw85ylc = compositorPushPresentationScaleService.isFontFamilyReady("material-roboto") && compositorPushPresentationScaleService.isFontFamilyReady("material-roboto-medium");
        if (f6lkivw85ylc != this.f6lkivw85ylc) {
            this.f6lkivw85ylc = f6lkivw85ylc;
            this.invalidateLayout();
        }
        super.draw(compositorPushPresentationScaleService);
    }
}
