package testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ReferenceType;

public class AutoGrader {

	// Test if the code correctly implements the Consumer interface in lambda
	// expressions
	public boolean testLambdaConsumer(String filePath) throws IOException {
		System.out.println("Starting testLambdaConsumer with file: " + filePath);

		File participantFile = new File(filePath); // Path to participant's file
		if (!participantFile.exists()) {
			System.out.println("File does not exist at path: " + filePath);
			return false;
		}

		FileInputStream fileInputStream = new FileInputStream(participantFile);
		JavaParser javaParser = new JavaParser();
		CompilationUnit cu;
		try {
			cu = javaParser.parse(fileInputStream).getResult()
					.orElseThrow(() -> new IOException("Failed to parse the Java file"));
		} catch (IOException e) {
			System.out.println("Error parsing the file: " + e.getMessage());
			throw e;
		}

		System.out.println("Parsed the Java file successfully.");

		// Flags to check for lambda expression and Consumer interface usage
		boolean hasMainMethod = false;
		boolean usesConsumerLambda = false;
		boolean usesForEach = false;
		boolean usesTwoConsumers = false;

		// Check for method declarations
		for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
			String methodName = method.getNameAsString();
			// Check for the presence of the main method
			if (methodName.equals("main")) {
				hasMainMethod = true;
			}
		}

		// Check for variables declared with Consumer<> (explicitly using Consumer
		// interface)
		for (VariableDeclarator variable : cu.findAll(VariableDeclarator.class)) {
			if (variable.getType() instanceof ReferenceType) {
				String typeName = variable.getType().toString(); // Get the full type name
				if (typeName.startsWith("Consumer")) { // Check if the type is Consumer<>
					usesConsumerLambda = true;
				}
			}
		}

		// Check for usage of forEach
		for (MethodCallExpr methodCallExpr : cu.findAll(MethodCallExpr.class)) {
			if (methodCallExpr.getNameAsString().equals("forEach")) {
				usesForEach = true;
			}
		}

		// Check if two consumers are used
		if (usesConsumerLambda && cu.findAll(LambdaExpr.class).size() >= 2) {
			usesTwoConsumers = true;
		}

		// Log results of the checks
		System.out.println("Method 'main' is " + (hasMainMethod ? "present" : "NOT present"));
		System.out.println("Lambda expression with Consumer interface is used: " + (usesConsumerLambda ? "YES" : "NO"));
		System.out.println("forEach method is used: " + (usesForEach ? "YES" : "NO"));
		System.out.println("Two Consumer lambda expressions are used: " + (usesTwoConsumers ? "YES" : "NO"));

		// Final result - all conditions should be true
		boolean result = hasMainMethod && usesConsumerLambda && usesForEach && usesTwoConsumers;

		System.out.println("Test result: " + result);
		return result;
	}
}
