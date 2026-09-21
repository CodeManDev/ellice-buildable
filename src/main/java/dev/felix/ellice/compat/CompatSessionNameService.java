











package dev.felix.ellice.compat;

import dev.felix.ellice.feature.privacy.PrivacyRevisionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringDecomposer;

public final class CompatSessionNameService {
    private CompatSessionNameService() {
    }

    public static String sessionName() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft == null ? "" : minecraft.getUser().getName();
    }

    public static void refreshChat() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null && minecraft.gui != null) {
            minecraft.gui.getChat().rescaleChat();
        }
    }

    public static String replace(String string) {
        if (string == null || !PrivacyRevisionService.active() || string.indexOf(167) < 0) {
            return PrivacyRevisionService.replace(string);
        }
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 0; i < string.length(); ++i) {
            if (string.charAt(i) == '\u00a7' && i + 1 < string.length()) {
                ++i;
                continue;
            }
            arrayList.add(i);
            stringBuilder.append(string.charAt(i));
        }
        PrivacyRevisionService.Mapping mapping = PrivacyRevisionService.map(stringBuilder.toString());
        if (!mapping.changed()) {
            return string;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        int n = 0;
        for (PrivacyRevisionService.Span span : mapping.spans()) {
            int n2 = (Integer)arrayList.get(span.start());
            int n3 = (Integer)arrayList.get(span.end() - 1) + 1;
            stringBuilder2.append(string, n, n2).append(mapping.text(), span.displayStart(), span.displayEnd());
            for (int i = n2; i < n3 - 1; ++i) {
                if (string.charAt(i) != '\u00a7') continue;
                stringBuilder2.append(string, i, i + 2);
                ++i;
            }
            n = n3;
        }
        return stringBuilder2.append(string, n, string.length()).toString();
    }

    public static FormattedCharSequence replace(FormattedCharSequence formattedCharSequence) {
        if (formattedCharSequence == null || !PrivacyRevisionService.active()) {
            return formattedCharSequence;
        }
        Capture capture = new Capture();
        formattedCharSequence.accept(capture::accept);
        List<Glyph> list = capture.replace();
        if (list == null) {
            return formattedCharSequence;
        }
        return formattedCharSink -> {
            int n = 0;
            for (Glyph glyph : list) {
                if (!formattedCharSink.accept(n, glyph.style(), glyph.codePoint())) {
                    return false;
                }
                n += Character.charCount(glyph.codePoint());
            }
            return true;
        };
    }

    public static FormattedText replace(FormattedText formattedText) {
        if (formattedText == null || !PrivacyRevisionService.active()) {
            return formattedText;
        }
        Capture capture = new Capture();
        formattedText.visit((style, string) -> {
            StringDecomposer.iterateFormatted((String)string, (Style)style, capture::accept);
            return Optional.empty();
        }, Style.EMPTY);
        List<Glyph> list = capture.replace();
        if (list == null) {
            return formattedText;
        }
        MutableComponent mutableComponent = Component.empty();
        StringBuilder stringBuilder = new StringBuilder();
        Style style2 = null;
        for (Glyph glyph : list) {
            if (style2 != null && !style2.equals((Object)glyph.style())) {
                mutableComponent.append((Component)Component.literal((String)stringBuilder.toString()).setStyle(style2));
                stringBuilder.setLength(0);
            }
            style2 = glyph.style();
            stringBuilder.appendCodePoint(glyph.codePoint());
        }
        if (!stringBuilder.isEmpty()) {
            mutableComponent.append((Component)Component.literal((String)stringBuilder.toString()).setStyle(style2));
        }
        return mutableComponent;
    }

    private static final class Capture {
        final StringBuilder text = new StringBuilder();
        final List<Style> styles = new ArrayList<Style>();

        private Capture() {
        }

        boolean accept(int n, Style style, int n2) {
            this.text.appendCodePoint(n2);
            for (int i = 0; i < Character.charCount(n2); ++i) {
                this.styles.add(style);
            }
            return true;
        }

        List<Glyph> replace() {
            int n;
            PrivacyRevisionService.Mapping mapping = PrivacyRevisionService.map(this.text.toString());
            if (!mapping.changed()) {
                return null;
            }
            ArrayList<Glyph> arrayList = new ArrayList<Glyph>();
            for (int i = 0; i < mapping.text().length(); i += Character.charCount(n)) {
                n = mapping.text().codePointAt(i);
                arrayList.add(new Glyph(n, this.styles.get(mapping.sourceOffset(i))));
            }
            return arrayList;
        }
    }

    private record Glyph(int codePoint, Style style) {
    }
}

