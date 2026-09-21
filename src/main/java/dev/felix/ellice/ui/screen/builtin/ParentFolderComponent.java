


package dev.felix.ellice.ui.screen.builtin;

import dev.felix.ellice.config.LocalConfigRepository;
import dev.felix.ellice.ui.component.ComponentBoxService;
import dev.felix.ellice.ui.component.ComponentKeyService;
import dev.felix.ellice.ui.material.MaterialIsLightService;
import dev.felix.ellice.ui.material.MaterialResponsiveService;
import dev.felix.ellice.ui.material.MaterialTextService;
import dev.felix.ellice.ui.scene.LayoutContainerNode;
import dev.felix.ellice.ui.scene.SceneCornerRadiusService;
import dev.felix.ellice.ui.scene.ScenePctService;
import dev.felix.ellice.ui.scene.SceneTextService;
import dev.felix.ellice.ui.scene.control.ControlLetterSpacingService;
import dev.felix.ellice.ui.scene.layout.LayoutOperationHandler;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

final class ParentFolderComponent {
    private final LocalConfigRepository f4s9vfb2tit6;
    private final boolean ff0tg05668ze;
    private final Path fe14cm5984m3;
    private final Path fgnr6s32s3h0;
    private final Runnable faun6nf6nbnq;
    private final Runnable f6maesh246rs;
    private final Transfer f2bfxti0g6n0;
    private Path fd6ju8e3qi03;
    private Path f2wn3l2jsajg;
    private List<Entry> fijcp17nx5zn = List.of();
    private CompletableFuture<Page> faf16pu64949;
    private LocalConfigRepository.Profile f848pj05xtdu;
    private String ft9hsbptrat = "";
    private String f4oocazdv3z2;
    private String f4sdxybrg9mr = "";
    private String fjcdbc8q7qnx = "";

    ParentFolderComponent(LocalConfigRepository localConfigRepository, boolean bl, String string, Path path, Path path2, Path path3, Runnable runnable, Runnable runnable2, Transfer transfer) {
        this.f4s9vfb2tit6 = localConfigRepository;
        this.ff0tg05668ze = bl;
        this.f4oocazdv3z2 = string;
        this.fe14cm5984m3 = path;
        this.fgnr6s32s3h0 = path2;
        this.faun6nf6nbnq = runnable;
        this.f6maesh246rs = runnable2;
        this.f2bfxti0g6n0 = transfer;
        this.m5aptfdoxyz0(path3);
    }

    private void m5aptfdoxyz0(Path path) {
        this.f2wn3l2jsajg = null;
        this.f848pj05xtdu = null;
        this.fijcp17nx5zn = List.of();
        this.ft9hsbptrat = "";
        this.f4sdxybrg9mr = "";
        this.fjcdbc8q7qnx = "Opening folder\u2026";
        this.faf16pu64949 = CompletableFuture.supplyAsync(() -> {
            try {
                Path path2 = path.toRealPath(new LinkOption[0]);
                ArrayList<Entry> arrayList = new ArrayList<Entry>();
                int n = 0;
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path2);){
                    for (Path path3 : directoryStream) {
                        boolean bl;
                        if (path3.getFileName().toString().startsWith(".") || !(bl = Files.isDirectory(path3, new LinkOption[0])) && (this.ff0tg05668ze || !Files.isRegularFile(path3, new LinkOption[0]) || !path3.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".json"))) continue;
                        arrayList.add(new Entry(path3, bl));
                        if (arrayList.size() != 1000) continue;
                        n = 1;
                        break;
                    }
                }
                arrayList.sort(Comparator.comparing(Entry::folder).reversed().thenComparing(entry -> entry.path().getFileName().toString(), String.CASE_INSENSITIVE_ORDER));
                return new Page(path2, List.copyOf(arrayList), n != 0);
            }
            catch (Exception exception) {
                throw new CompletionException(exception);
            }
        });
        this.faun6nf6nbnq.run();
    }

    void tick() {
        if (this.faf16pu64949 == null || !this.faf16pu64949.isDone()) {
            return;
        }
        CompletableFuture<Page> completableFuture = this.faf16pu64949;
        this.faf16pu64949 = null;
        try {
            Page page = completableFuture.join();
            this.fd6ju8e3qi03 = page.directory();
            this.fijcp17nx5zn = page.entries();
            this.fjcdbc8q7qnx = page.truncated() ? "First 1,000 items shown. Narrow your search by opening a subfolder." : "";
        }
        catch (Exception exception) {
            this.fd6ju8e3qi03 = null;
            this.fjcdbc8q7qnx = "Folder unavailable. Choose Downloads, Desktop or Home.";
        }
        this.faun6nf6nbnq.run();
    }

    private void mhtmezlxvg1c(Path path) {
        this.f2wn3l2jsajg = path;
        this.f848pj05xtdu = null;
        this.f4sdxybrg9mr = "";
        try {
            this.f848pj05xtdu = this.f4s9vfb2tit6.previewProfile(path);
            this.f4oocazdv3z2 = this.f848pj05xtdu.name();
        }
        catch (Exception exception) {
            this.f4sdxybrg9mr = "This file cannot be imported. It may be damaged or from a newer version.";
        }
        this.faun6nf6nbnq.run();
    }

    private void meve62gsmk2z(boolean bl) {
        if (this.faf16pu64949 != null || (this.ff0tg05668ze ? this.fd6ju8e3qi03 == null : this.f848pj05xtdu == null)) {
            return;
        }
        try {
            this.f2bfxti0g6n0.run(this.ff0tg05668ze ? this.fd6ju8e3qi03 : this.f2wn3l2jsajg, this.f4oocazdv3z2, bl);
        }
        catch (Exception exception) {
            this.f4sdxybrg9mr = exception.getMessage() == null ? "Transfer failed. Please try again." : exception.getMessage();
            this.faun6nf6nbnq.run();
        }
    }

    ComponentKeyService<?> render(float f, float f2) {
        boolean bl = f < Float.intBitsToFloat(1142292480);
        ArrayList<ComponentKeyService<?>> arrayList = new ArrayList<ComponentKeyService<?>>();
        for (Entry entry : this.fijcp17nx5zn) {
            String fileName = entry.path().getFileName().toString();
            if (!fileName.toLowerCase(Locale.ROOT).contains(this.ft9hsbptrat.toLowerCase(Locale.ROOT))) continue;
            boolean bl2 = entry.path().equals(this.f2wn3l2jsajg);
            arrayList.add(ComponentBoxService.node("config-file", MaterialResponsiveService::new, arg_0 -> this.mf0vthqc4svm(bl2, fileName, entry, arg_0), MaterialTextService.icon(entry.folder() ? "folder" : "content_copy", entry.folder() ? MaterialIsLightService.ON_SURFACE_VARIANT : MaterialIsLightService.PRIMARY), MaterialTextService.text(fileName, Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)).overflow(SceneTextService.Overflow.ELLIPSIS)), MaterialTextService.icon(entry.folder() ? "chevron_right" : (bl2 ? "check" : "chevron_right"), MaterialIsLightService.ON_SURFACE_VARIANT)).key(entry.path().toString()));
        }
        if (arrayList.isEmpty()) {
            arrayList.add(MaterialTextService.text(this.faf16pu64949 != null ? "Opening folder\u2026" : (!this.ft9hsbptrat.isBlank() ? "No matches." : (this.ff0tg05668ze ? "No subfolders. You can export here." : "No JSON files here. Try Downloads or another folder.")), Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).padding(Float.intBitsToFloat(0x41400000))).wordWrap(true)));
        }
        ArrayList<ComponentKeyService<?>> arrayList2 = new ArrayList<>();
        arrayList2.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER), MaterialTextService.iconButton("configs.browser.up", "arrow_back", "Parent folder", () -> {
            if (this.fd6ju8e3qi03 != null && this.fd6ju8e3qi03.getParent() != null) {
                this.m5aptfdoxyz0(this.fd6ju8e3qi03.getParent());
            }
        }), MaterialTextService.text(this.fd6ju8e3qi03 == null ? "Choose a folder" : (this.fd6ju8e3qi03.getFileName() == null ? this.fd6ju8e3qi03.toString() : this.fd6ju8e3qi03.getFileName().toString()), Float.intBitsToFloat(1095761920), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)).overflow(SceneTextService.Overflow.ELLIPSIS).tooltip(this.fd6ju8e3qi03 == null ? "" : this.fd6ju8e3qi03.toString()))));
        arrayList2.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x40C00000)), this.mja8sp9t5te3("downloads", "Downloads", this.fgnr6s32s3h0), this.mja8sp9t5te3("desktop", "Desktop", this.fe14cm5984m3.resolve("Desktop")), this.mja8sp9t5te3("home", "Home", this.fe14cm5984m3)));
        arrayList2.add(MaterialTextService.search("configs.browser.search", this.ff0tg05668ze ? "Find a folder\u2026" : "Find a file\u2026", this.ft9hsbptrat, string -> {
            this.ft9hsbptrat = string;
            this.faun6nf6nbnq.run();
        }, true).props(interactiveSurfacePanel -> interactiveSurfacePanel.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))));
        if (!this.fjcdbc8q7qnx.isEmpty()) {
            arrayList2.add(MaterialTextService.text(this.fjcdbc8q7qnx, Float.intBitsToFloat(1095761920), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).wordWrap(true)));
        }
        arrayList2.add(ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("configs.browser.files")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x40C00000)), (ComponentKeyService[])arrayList.toArray(ComponentKeyService[]::new)).key("files:" + String.valueOf(this.fd6ju8e3qi03)));
        if (this.ff0tg05668ze) {
            arrayList2.add(MaterialTextService.text("Export \u201c" + this.f4oocazdv3z2 + "\u201d here. Existing files are kept; copies get a numbered filename.", Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).wordWrap(true)));
        }
        if (this.f848pj05xtdu != null) {
            arrayList2.clear();
            arrayList2.add(ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER), MaterialTextService.iconButton("configs.browser.choose-another", "arrow_back", "Choose another file", () -> {
                this.f848pj05xtdu = null;
                this.f2wn3l2jsajg = null;
                this.f4sdxybrg9mr = "";
                this.faun6nf6nbnq.run();
            }), MaterialTextService.text(this.f2wn3l2jsajg.getFileName().toString(), Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)).overflow(SceneTextService.Overflow.ELLIPSIS))));
            arrayList2.add(ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("configs.browser.preview")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).padding(Float.intBitsToFloat(1098907648))).gap(Float.intBitsToFloat(0x41000000))).backgroundColor(MaterialIsLightService.SECONDARY_CONTAINER).cornerRadius(Float.intBitsToFloat(1098907648)).direction(ScenePctService.Direction.COLUMN), MaterialTextService.text("CONFIG PREVIEW", Float.intBitsToFloat(0x41400000), MaterialIsLightService.PRIMARY), MaterialTextService.text(this.f848pj05xtdu.modules() + " modules \u00b7 " + this.f848pj05xtdu.enabled() + " enabled \u00b7 " + this.f848pj05xtdu.keybinds() + " keybinds", Float.intBitsToFloat(1096810496), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).wordWrap(true)), ComponentBoxService.node("import-name", ControlLetterSpacingService::new, controlLetterSpacingService -> {
                MaterialTextService.input(controlLetterSpacingService);
                ((ControlLetterSpacingService)((ControlLetterSpacingService)((ControlLetterSpacingService)((ControlLetterSpacingService)((ControlLetterSpacingService)controlLetterSpacingService.id("configs.browser.name")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(LayoutOperationHandler.px(Float.intBitsToFloat(0x42400000)))).flexShrink(0.0f)).maxLength(128).placeholder("Config name").tooltip("Name for the imported copy")).onChanged(string -> {
                    this.f4oocazdv3z2 = string;
                    this.f4sdxybrg9mr = "";
                    this.faun6nf6nbnq.run();
                });
                if (!controlLetterSpacingService.focused()) {
                    controlLetterSpacingService.text(this.f4oocazdv3z2);
                }
            }, new ComponentKeyService[0]).key("name"), MaterialTextService.text("Imported as a new config. Your existing configs stay intact.", Float.intBitsToFloat(0x41400000), MaterialIsLightService.ON_SURFACE_VARIANT).props(sceneTextService -> ((SceneTextService)sceneTextService.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).wordWrap(true))));
        }
        if (!this.f4sdxybrg9mr.isEmpty()) {
            arrayList2.add(MaterialTextService.text(this.f4sdxybrg9mr, Float.intBitsToFloat(1096810496), MaterialIsLightService.ERROR).props(sceneTextService -> ((SceneTextService)((SceneTextService)sceneTextService.id("configs.browser.error")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).wordWrap(true)));
        }
        arrayList2.replaceAll(componentKeyService -> componentKeyService.props(scenePctService -> scenePctService.flexShrink(0.0f)));
        boolean bl3 = this.faf16pu64949 == null && (this.ff0tg05668ze ? this.fd6ju8e3qi03 != null : this.f848pj05xtdu != null);
        ArrayList<ComponentKeyService<?>> actionButtons = new ArrayList<>();
        actionButtons.add(MaterialTextService.button("configs.browser.confirm", this.ff0tg05668ze ? "Export here" : "Import", () -> this.meve62gsmk2z(false), this.ff0tg05668ze).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.available(bl3).flex(1.0f)).minWidth(0.0f)));
        if (!this.ff0tg05668ze) {
            actionButtons.add(MaterialTextService.button("configs.browser.load", "Import & load", () -> this.meve62gsmk2z(true), true).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.available(bl3).flex(1.0f)).minWidth(0.0f)));
        }
        return ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.absolute()).inset(LayoutOperationHandler.px(0.0f))).size(LayoutOperationHandler.auto(), LayoutOperationHandler.auto())).padding(bl ? Float.intBitsToFloat(0x41000000) : Float.intBitsToFloat(1103101952))).direction(ScenePctService.Direction.COLUMN)).align(ScenePctService.Align.CENTER)).justify(ScenePctService.Justify.CENTER)).backgroundColor(-1728053248).interactive(true)).layerBreak(true), ComponentBoxService.panel(sceneCornerRadiusService -> ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)sceneCornerRadiusService.id("configs.browser")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).maxWidth(Float.intBitsToFloat(1144913920))).height(LayoutOperationHandler.px(Math.min(Float.intBitsToFloat(0x44340000), Math.max(Float.intBitsToFloat(1123024896), f2 - (float)(bl ? 16 : 48)))))).padding(bl ? Float.intBitsToFloat(0x41400000) : Float.intBitsToFloat(1103101952))).gap(Float.intBitsToFloat(0x41400000))).cornerRadius(Float.intBitsToFloat(1103101952)).backgroundColor(MaterialIsLightService.SURFACE_HIGH).direction(ScenePctService.Direction.COLUMN)).clip(true)).stopPropagation(true), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x41000000))).align(ScenePctService.Align.CENTER)).flexShrink(0.0f), MaterialTextService.text(this.ff0tg05668ze ? "Export location" : "Import config", Float.intBitsToFloat(1102053376), MaterialIsLightService.ON_SURFACE).props(sceneTextService -> ((SceneTextService)sceneTextService.flex(1.0f)).minWidth(0.0f)), MaterialTextService.iconButton("configs.browser.cancel", "close", "Cancel", this.f6maesh246rs)), ComponentBoxService.column(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.id("configs.browser.body")).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).flex(1.0f)).minHeight(0.0f)).gap(Float.intBitsToFloat(0x41400000))).scrollable(true)).scrollbarWidth(Float.intBitsToFloat(0x40400000))).scrollbarColor(MaterialIsLightService.OUTLINE)).clip(true), (ComponentKeyService[])arrayList2.toArray(ComponentKeyService[]::new)).key("browser-body:" + String.valueOf(this.fd6ju8e3qi03) + ":" + String.valueOf(this.f2wn3l2jsajg)), ComponentBoxService.row(layoutContainerNode -> ((LayoutContainerNode)((LayoutContainerNode)layoutContainerNode.width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).gap(Float.intBitsToFloat(0x41000000))).flexShrink(0.0f), actionButtons.toArray(ComponentKeyService[]::new)))).key("transfer-browser");
    }

    private ComponentKeyService<?> mja8sp9t5te3(String string, String string2, Path path) {
        return MaterialTextService.button("configs.browser." + string, string2, () -> this.m5aptfdoxyz0(path), false).props(materialJoinedService -> ((SceneCornerRadiusService)materialJoinedService.flex(1.0f)).minWidth(0.0f));
    }

    private  void mf0vthqc4svm(boolean bl, String string, Entry entry, MaterialResponsiveService materialResponsiveService) {
        ((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)((SceneCornerRadiusService)materialResponsiveService.surface(bl ? MaterialIsLightService.SECONDARY_CONTAINER : MaterialIsLightService.SURFACE_CONTAINER).selected(bl).responsive(true).id("configs.browser.entry." + string)).width(LayoutOperationHandler.percent(Float.intBitsToFloat(1120403456)))).height(LayoutOperationHandler.px(Float.intBitsToFloat(1112539136)))).flexShrink(0.0f)).padding(Float.intBitsToFloat(1092616192), Float.intBitsToFloat(0x41400000))).gap(Float.intBitsToFloat(0x41400000))).cornerRadius(Float.intBitsToFloat(0x41400000)).direction(ScenePctService.Direction.ROW)).align(ScenePctService.Align.CENTER)).cursorStyle(ScenePctService.CursorStyle.POINTER)).onClick(() -> {
            if (entry.folder()) {
                this.m5aptfdoxyz0(entry.path());
            } else {
                this.mhtmezlxvg1c(entry.path());
            }
        });
    }

    @FunctionalInterface
    static interface Transfer {
        public void run(Path var1, String var2, boolean var3) throws Exception;
    }

    record Page(Path directory, List<Entry> entries, boolean truncated) {
    }

    record Entry(Path path, boolean folder) {
    }
}
