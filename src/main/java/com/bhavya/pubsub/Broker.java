package com.bhavya.pubsub;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Broker {

  static {
    init();
  }

  static ConcurrentHashMap<String, ConcurrentHashMap<Integer, WeakReference<Object>>> channels;

  static void init() {
    channels = new ConcurrentHashMap<>();
  }

  public static void publish(String channelName, Message message) {
    if (!channels.containsKey(channelName)) {
      System.out.println("Channel not found, Ignoring request");
    }

    channels.get(channelName).forEach((key, subscriber) -> {
      deliverMessage(subscriber, message);
    });
  }

  public static void subscribe(String channelName, Object subscriber) {
    if (!channels.containsKey(channelName)) {
      channels.put(channelName, new ConcurrentHashMap<>());
    }
    channels.get(channelName).put(subscriber.hashCode(), new WeakReference<>(subscriber));
  }

  private static void deliverMessage(Object subscriber, Message message) {
    for(final Method method : subscriber.getClass().getDeclaredMethods()) {
      Optional<OnMessage> annotation = Optional.ofNullable(method.getAnnotation(OnMessage.class));
      if (annotation.isPresent()) {
        try {
          for(final Class paramClass : method.getParameterTypes()) {
            if (paramClass.equals(message.getValue().getClass())) {
              method.invoke(subscriber, message.getValue());
            }
          }
          method.invoke(subscriber, message);
        } catch (Exception e) {
          System.out.println("Method having OnMethod annotation does not have correct parameters");
        }
      }
    }
  }
}
