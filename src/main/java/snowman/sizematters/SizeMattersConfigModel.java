package snowman.sizematters;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = "sizematters")
@Config(name = "sizematters-server", wrapperName = "SizeMattersConfig")
public class SizeMattersConfigModel {
    public double min_height = 0.7;
    public double max_height = 1.3;
    public boolean uuid_dependant = false;
}
