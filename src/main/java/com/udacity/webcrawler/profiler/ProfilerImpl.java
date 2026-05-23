package com.udacity.webcrawler.profiler;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.io.BufferedWriter;
import java.io.UncheckedIOException;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

/**
 * Concrete implementation of the {@link Profiler}.
 */
final class ProfilerImpl implements Profiler {

  private final Clock clock;
  private final ProfilingState state = new ProfilingState();
  private final ZonedDateTime startTime;

  @Inject
  ProfilerImpl(Clock clock) {
    this.clock = Objects.requireNonNull(clock);
    this.startTime = ZonedDateTime.now(clock);
  }

  @Override
  public <T> T wrap(Class<T> klass, T delegate) {
    Objects.requireNonNull(klass);
    Objects.requireNonNull(delegate);

    boolean hasProfiledMethod = false;
    for (var method : klass.getMethods()) {
      if (method.isAnnotationPresent(Profiled.class)) {
        hasProfiledMethod = true;
        break;
      }
    }

    if (!hasProfiledMethod) {
      throw new IllegalArgumentException(
       "Class does not contain any @Profiled methods");
    }

    Object proxy = Proxy.newProxyInstance(
      klass.getClassLoader(),
        new Class<?>[]{klass},
        new ProfilingMethodInterceptor(
          clock,
          delegate,
          state));

    return klass.cast(proxy);
  }

  @Override
  public void writeData(Path path) {
    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      writeData(writer);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void writeData(Writer writer) throws IOException {
    writer.write("Run at " + RFC_1123_DATE_TIME.format(startTime));
    writer.write(System.lineSeparator());
    state.write(writer);
    writer.write(System.lineSeparator());
  }
}
