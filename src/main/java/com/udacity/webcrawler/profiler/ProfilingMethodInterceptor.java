package com.udacity.webcrawler.profiler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Clock;
import java.util.Objects;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;

/**
 * A method interceptor that checks whether {@link Method}s are annotated with the {@link Profiled}
 * annotation. If they are, the method interceptor records how long the method invocation took.
 */
final class ProfilingMethodInterceptor implements InvocationHandler {

  private final Clock clock;
  private final Object delegate;
  private final ProfilingState state;

  // TODO: You will need to add more instance fields and constructor arguments to this class.

  ProfilingMethodInterceptor(Clock clock, Object delegate, ProfilingState state){
    this.clock = Objects.requireNonNull(clock);
    this.delegate = Objects.requireNonNull(delegate);
    this.state = Objects.requireNonNull(state);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (method.getDeclaringClass().equals(Object.class) && method.getName().equals("equals")) {
      return method.invoke(delegate, args);
    }

    boolean isProfiled = method.isAnnotationPresent(Profiled.class);

    Instant start = null;

    if (isProfiled) {
      start = clock.instant();
    }

    try {
      Object result = method.invoke(delegate, args);

      return result;
    } 
    catch (InvocationTargetException e) {
      throw e.getCause();
    } 
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } 
    finally {
      if (isProfiled) {
        state.record(
            delegate.getClass(),
            method,
            Duration.between(start, clock.instant()));
      }
    }
  }
}
