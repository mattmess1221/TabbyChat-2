package mnm.mods.tabbychat;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public final class TCMarkers {

    public static final Marker STARTUP = MarkerManager.getMarker("Startup");
    public static final Marker CHATBOX = MarkerManager.getMarker("ChatBox");
    public static final Marker CONFIG = MarkerManager.getMarker("Config");
    public static final Marker NETWORK = MarkerManager.getMarker("Network");
    public static final Marker SPELLCHECK = MarkerManager.getMarker("Spellcheck");

    private TCMarkers() {
    }
}
