import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class _151_SimCityTest {

	private static final String basePath = "./src/test/resources/_151_SimCity/";
	private static ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private static String[] in, out;
	private static NelsonOracle oracle;
	private static final int seed = 343;
	private static Random random = new Random();

	@BeforeAll
	static void setupAll() throws IOException, InterruptedException {
		oracle = new NelsonOracle(basePath + "_151_SimCity.cpp");
		random.setSeed(seed);
		in = new File(basePath + "in/").list();
		out = new File(basePath + "out/").list();
		Arrays.sort(in); Arrays.sort(out);
		System.setOut(new PrintStream(outputStream));
	}

	@BeforeEach
	void resetOutput() {
		outputStream.reset();
	}

	@RepeatedTest(3)
	void repeatedTest(RepetitionInfo repetitionInfo) throws IOException {
		int i = repetitionInfo.getCurrentRepetition() - 1;
		String expected = InOutReader.getStringFromFile(basePath + "out/" + out[i]);
		System.setIn(new FileInputStream(basePath + "in/" + in[i]));
		final String myAnswer = assertTimeoutPreemptively(Duration.ofMillis(4000), () -> {
			_151_SimCity.HuxleyCode.main(null);
			return(InOutReader.uniformString(outputStream.toString()));
		});
		assertEquals(expected, myAnswer, "Failing " + in[i] + " test case");
	}

	private void generateInput() throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(NelsonOracle.in));
		Integer testCasesAmount = random.nextInt(100);
		for (int i = 0; i < testCasesAmount; i ++) {
			Integer cities = random.nextInt(99) + 1;
			bufferedWriter.write(cities.toString() + "\n");
			for (int j = 0; j < cities; j ++) {
				Double x = Math.round(100*random.nextDouble()) / 100.0;
				Double y = Math.round(100*random.nextDouble()) / 100.0;
				bufferedWriter.write(x.toString() + " " + y.toString() + "\n");
			}
		}
		bufferedWriter.write("0\n");
		bufferedWriter.close();
	}

	@RepeatedTest(10)
	void randomTest() throws IOException, InterruptedException {

		generateInput();
		final String oracleAnswer = oracle.getAnswer();

		System.setIn(new FileInputStream(NelsonOracle.in));
		final String myAnswer = assertTimeoutPreemptively(Duration.ofMillis(4000), () -> {
			_151_SimCity.HuxleyCode.main(null);
			return(InOutReader.uniformString(outputStream.toString()));
		});

		String input = InOutReader.getStringFromFile(NelsonOracle.in);

		assertEquals(oracleAnswer, myAnswer, "Failed test case of input: <" + input + ">");
	}
}
