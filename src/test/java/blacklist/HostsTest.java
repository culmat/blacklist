package blacklist;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.hamcrest.collection.IsEmptyCollection;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.MatcherAssert.assertThat;

public class HostsTest {

	@Test
	public void testAccept() throws IOException {
		Hosts hosts = new Hosts();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Hosts.txt")))) {
			in.lines().forEach(hosts);
		}
		hosts.stats().forEach(System.out::println);
		//hosts.toStream().forEach(System.out::println);
	}
	
	@Test
	public void testSubDomain1() throws IOException {
		Hosts hosts = new Hosts();
		hosts.accept("bla.de");
		hosts.accept("a.com");
		hosts.accept("b.a.com");
		assertThat(hosts.toStream().collect(toList()), contains("a.com", "bla.de"));
		
	}
	
	@Test
	public void testSubDomain3() throws IOException {
		Hosts hosts = new Hosts();
		hosts.accept("bla.de");
		hosts.accept("b.a.com");
		hosts.accept("a.com");
		assertThat(hosts.toStream().collect(toList()), contains("a.com", "bla.de"));
		
	}
	
	@Test
	public void testSubDomain2() throws IOException {
		Hosts hosts = new Hosts();
		hosts.accept("b.a.com");
		hosts.accept("a.com");
		hosts.accept("bla.de");
		assertThat(hosts.toStream().collect(toList()), contains("a.com", "bla.de"));
		
	}
	


}
