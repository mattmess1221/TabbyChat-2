package mnm.mods.tabbychat.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.gui.GuiScreen;

public class BackgroundChatThread extends Thread {

    private static Lock lock = new ReentrantLock();

    private final GuiScreen chat;
    private String[] messages;
    private long waitMilis;

    public BackgroundChatThread(GuiScreen chat, String[] message, long milis) {
        this.chat = chat;
        this.messages = message;
        this.waitMilis = milis;
    }

    @Override
    public void run() {
        if (messages == null) {
            return;
        }
        lock.lock();
        for (String msg : messages) {
            if (!StringUtils.isEmpty(msg)) {

                // don't add this to the sent chat. I'll do it manually.
                chat.sendChatMessage(msg, false);

                try {
                    // wait a bit so we don't get kicked for spam.
                    Thread.sleep(waitMilis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        lock.unlock();
    }
}
