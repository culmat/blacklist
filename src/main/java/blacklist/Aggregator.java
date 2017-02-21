package blacklist;

import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Aggregator {

	static Set<String> hosts = new TreeSet<>();
	static final Predicate<String> NOT_LOCALHOST = ((Predicate<String>) "localhost"::equalsIgnoreCase).negate();
	static Predicate<String> all = s -> {
		return true;
	};
	static Function<String, String> identity = s -> {
		return s;
	};
	public static void main(String[] args) throws IOException {

		Predicate<String> notEmpty = s -> {
			return !s.isEmpty();
		};
		Predicate<String> noComment = s -> {
			return !s.startsWith("#");
		};

		 get(
			 "https://raw.githubusercontent.com/AdAway/adaway.github.io/master/hosts.txt",
			 notEmpty.and(noComment),
			 s -> { return s.split("\\s+",2)[1];}
		 );

		get("https://raw.githubusercontent.com/disconnectme/disconnect-tracking-protection/master/services.json",
			(ThrowingConsumer<String>)
			json -> {
				 Map<String, Map<String, List<Map<String, Map<String, List<String>>>>>> map = JSON.parseJson(json);
			        stream("Analytics", "Advertising").forEach(category -> {
			        	map
			        			.get("categories")
			        			.get(category).stream().forEach(m-> {
			        		m.entrySet().iterator().next().getValue().entrySet().iterator().next().getValue().stream().forEach(hosts::add);
			        	});
			        });
			}
		);
		
		
		hosts.stream().forEach(System.out::println);
	}

	private static void get(String url, Consumer<String> c) throws IOException {
		try (Scanner s = new Scanner(new URL(url).openStream())) {
			c.accept(s.useDelimiter("\\A").hasNext() ? s.next() : "");
		}
	}

	private static void get(String url, Predicate<String> filter, Function<String, String> mapper) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
			in.lines().map(s -> s.trim()).filter(filter).map(mapper).filter(NOT_LOCALHOST).forEach(hosts::add);
		}
	}
	
	private static <T> Stream<T> stream(T ... elements) {
		return asList(elements).stream();
	}

}
