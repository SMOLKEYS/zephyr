package zeph;

import arc.*;
import arc.util.*;
import arc.util.serialization.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.mod.*;

@SuppressWarnings("unused")
public class Zephyr extends Mod{

    public static String apiUrl = "https://api.github.com/repos/SMOLKEYS/zephyr/releases";


    public Zephyr(){
        Log.info("Zephyr initialized.");

        Events.run(EventType.ClientLoadEvent.class, () -> {
            Core.settings.getBoolOnce("zephyr-first-unfinished-install", () -> Vars.ui.showSmall(
                    "@zeph",
                    "@zeph.initial-install"
            ));

            JsonReader parser = new JsonReader();

            Http.get(apiUrl, resp -> {
                String jsonResult = resp.getResultAsString();

                JsonValue root = parser.parse(jsonResult);

                String s = root.get(0).getString("name", "v0.0.1").split(" ")[0];

                if(s != null && s != meta().version) Time.run(60*2, () -> {
                    Vars.ui.showInfoToast(Core.bundle.format("zeph.update-available", meta().version, s), 5);
                    meta().subtitle = Core.bundle.format("zeph.update-available", meta().version, s);
                });
            }, err -> Log.err(
                    "Zephyr autoupdater failed to check for updates. Check your internet!", err
            ));
        });
    }

    public static Mods.ModMeta meta(){
        return Vars.mods.getMod(Zephyr.class).meta;
    }
}
