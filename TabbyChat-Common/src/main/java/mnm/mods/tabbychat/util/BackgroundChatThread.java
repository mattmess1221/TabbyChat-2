package mnm.mods.tabbychat.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mnm.mods.tabbychat.core.GuiChatTC;

import org.apache.commons.lang3.StringUtils;

public class BackgroundChatThread extends Thread {

    private static Lock lock = new ReentrantLock();

    private final GuiChatTC chat;
    private String[] messages;
    private long waitMilis;

    public BackgroundChatThread(GuiChatTC chat, String[] message, long milis) {
        this.chat = chat;
        this.messages = message;
        this.waitMilis = milis;
    }

    @Override
    public void run() {
        lock.lock();
        if (messages == null) {
            return;
        }
        for (String msg : messages) {
            if (!StringUtils.isEmpty(msg)) {

                // don't add this to the sent chat. I'll do it manually.
                chat.sendChatMessage(msg, false);

                try {
                    // wait a bit so we don't get kicked for spam.
                    // TODO configure
                    Thread.sleep(waitMilis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        lock.unlock();
    }
}
