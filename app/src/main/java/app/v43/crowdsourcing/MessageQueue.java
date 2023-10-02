package app.v43.crowdsourcing;

import android.os.Message;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {
    private static MessageQueue instance;

    public static MessageQueue getInstance() {
        if (instance == null) {
            instance = new MessageQueue();
        }

        return instance;
    }

    private final Queue<Message> queue;

    public MessageQueue() {
        queue = new LinkedList<>();
    }

    public void add(Message message) {
        queue.add(message);
    }

    public Message poll() {
        return queue.poll();
    }
}
