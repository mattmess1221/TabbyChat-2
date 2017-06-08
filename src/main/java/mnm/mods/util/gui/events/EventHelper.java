package mnm.mods.util.gui.events;

import java.util.function.Consumer;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class EventHelper {

    public static <T> Listener<T> register(EventBus bus, Class<T> event, Consumer<T> listener) {
      Listener<T> lis = new Listener<>(event, listener);
      bus.register(bus);
      return lis;
    }

    public static class Listener<T> {

        private Class<T> clazz;
        private Consumer<T> listener;

        private Listener(Class<T> clazz, Consumer<T> listener) {
            this.clazz = clazz;
            this.listener = listener;
        }

        @SuppressWarnings("unchecked")
        @Subscribe
        public void event(Object o) {
            if (clazz.isInstance(o)) {
                listener.accept((T) o);
            }
        }
    }
}
