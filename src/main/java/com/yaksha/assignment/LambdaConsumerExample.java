package com.yaksha.assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LambdaConsumerExample {

	public static void main(String[] args) {
		// Task 1: Create a Consumer lambda expression to print each element
		Consumer<String> printElement = (element) -> System.out.println("Element: " + element);

		// Task 2: Create an ArrayList and use the Consumer to print each element
		List<String> names = new ArrayList<>();
		names.add("Alice");
		names.add("Bob");
		names.add("Charlie");
		names.add("Dave");

		// Task 3: Use the Consumer to print elements in the list
		names.forEach(printElement);

		// Task 4: Create another Consumer to transform the element (convert to
		// uppercase)
		Consumer<String> printUppercase = (element) -> System.out.println("Uppercase: " + element.toUpperCase());

		// Task 5: Use the second Consumer to print elements in uppercase
		names.forEach(printUppercase);
	}
}
