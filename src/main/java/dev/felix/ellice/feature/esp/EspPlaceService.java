


package dev.felix.ellice.feature.esp;

import dev.felix.ellice.feature.esp.EspData;
import dev.felix.ellice.feature.esp.EspLabelData;
import dev.felix.ellice.feature.esp.EspProjectService;
import dev.felix.ellice.feature.esp.EspRenderer;
import java.util.List;

public final class EspPlaceService {
    private EspPlaceService() {
    }

    public static EspRenderer.Captions place(EspProjectService.Bounds bounds, EspData espData, EspLabelData espLabelData, boolean bl, boolean bl2, boolean bl3, float f, float f2, float f3, float f4, List<Rect> list) {
        float f5 = espData.labelScale();
        float f6 = Float.intBitsToFloat(0x40400000) * f5;
        if (bounds.height() <= 0.0f || bounds.width() <= 0.0f || f3 < Float.intBitsToFloat(1106247680) * f5) {
            return EspRenderer.Captions.NONE;
        }
        float f7 = Math.min(f3 - 2.0f * f6, Math.clamp(f + Float.intBitsToFloat(0x41000000) * f5, Float.intBitsToFloat(1103101952) * f5, Float.intBitsToFloat(1126170624) * f5));
        float f8 = Math.min(f3 - 2.0f * f6, Math.max(Float.intBitsToFloat(1098907648) * f5, f2 + Float.intBitsToFloat(0x40800000) * f5));
        float f9 = Math.clamp(bounds.x() + (bounds.width() - f7) * Float.intBitsToFloat(0x3F000000), f6, f3 - f6 - f7);
        float f10 = Math.clamp(bounds.x() + (bounds.width() - f8) * Float.intBitsToFloat(0x3F000000), f6, f3 - f6 - f8);
        Rect rect = new Rect(f9, bounds.y() - EspRenderer.nameHeight(f5) - Float.intBitsToFloat(0x40800000) * f5, f7, EspRenderer.nameHeight(f5));
        Rect rect2 = new Rect(f10, bounds.bottom() + EspRenderer.distanceOffset(espData, espLabelData), f8, Float.intBitsToFloat(1093664768) * f5);
        boolean bl4 = bl && bounds.hasEdge(4) && rect.y >= f6 && EspPlaceService.mibwsupjyhvp(rect, bl3, list, 2.0f * f5);
        int n = bl2 && bounds.hasEdge(8) && rect2.y + rect2.height <= f4 - f6 && EspPlaceService.mibwsupjyhvp(rect2, bl3, list, 2.0f * f5) ? 1 : 0;
        return new EspRenderer.Captions(bl4, f9 - bounds.x(), f7, n != 0, f10 - bounds.x(), f8);
    }

    private static boolean mibwsupjyhvp(Rect rect, boolean bl, List<Rect> list, float f) {
        if (bl && list.stream().anyMatch(rect2 -> rect.overlaps((Rect)rect2, f))) {
            return false;
        }
        list.add(rect);
        return true;
    }

    public record Rect(float x, float y, float width, float height) {
        public boolean overlaps(Rect rect, float f) {
            return (this.x < rect.x + rect.width + f && this.x + this.width + f > rect.x && this.y < rect.y + rect.height + f && this.y + this.height + f > rect.y ? 1 : 0) != 0;
        }
    }
}

