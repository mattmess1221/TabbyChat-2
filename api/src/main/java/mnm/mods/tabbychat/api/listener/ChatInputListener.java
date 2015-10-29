package mnm.mods.tabbychat.api.listener;

public interface ChatInputListener extends TabbyListener {

    boolean onMouseClicked(int xPos, int yPos, int button);

    boolean onKeyTyped(char ch, int code);
}
