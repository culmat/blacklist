package blacklist.stream;

import java.util.Arrays;
import java.util.stream.Stream;

public interface Streams {
	static <T> Stream<T> stream(T ... elements) {
		return Arrays.stream(elements);
	}
}
