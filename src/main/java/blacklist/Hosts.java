package blacklist;

import static blacklist.Streams.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.FactoryTreeMap;

public class Hosts implements Consumer<String> {

	private static Set<String> hosts = new TreeSet<>();
	
	static final Predicate<String> LOCALHOST =  "localhost"::equalsIgnoreCase;
	
	@Override
	public void accept(String host) {
		if(!LOCALHOST.test(host)) {
			hosts.add(host); 
		}
	}
	
	private static String getHost(String dnsName) {
		return reverse(stream(reverse(dnsName).split("\\.", 3)).limit(2).collect(Collectors.joining(".")));
	}

	private static String reverse(String string) {
		return new StringBuffer(string).reverse().toString();
	}
	
   
	
	public Stream<String> toStream() {
		return hosts.stream();
	}
	
	public Stream<Entry<Integer, String>> stats() {
		Map<String, Integer> hostCount = FactoryTreeMap.create(0);
		SortedMap<Integer, String> orderHostCount = new TreeMap<>(Comparator.reverseOrder());
		Consumer<String> increaseHostCount = key -> hostCount.put(key, hostCount.get(key)+1);
		hosts.stream().map(Hosts::getHost).forEach(increaseHostCount);
		
		hostCount.entrySet().stream()
			.filter(e -> e.getValue() > 1)
			.forEach(e2 -> orderHostCount.put(e2.getValue(), e2.getKey()));
		
		return orderHostCount.entrySet().stream();
	}

}
