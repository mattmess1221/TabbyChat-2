package mnm.mods.tabbychat.liteloader;

import com.mumfrey.liteloader.core.runtime.Obf;

public class TabbyObf extends Obf {

    public static final Obf persistantChatGUI = new TabbyObf("field_73840_e", "l", "persistantChatGUI");
    public static final Obf defaultInputFieldText = new TabbyObf("field_146409_v", "u", "defaultInputFieldText");
    public static final Obf sndRegistry = new TabbyObf("field_147697_e", "e", "sndRegistry");

    protected TabbyObf(String seargeName, String obfName, String mcpName) {
        super(seargeName, obfName, mcpName);
        // TODO Auto-generated constructor stub
    }

}
