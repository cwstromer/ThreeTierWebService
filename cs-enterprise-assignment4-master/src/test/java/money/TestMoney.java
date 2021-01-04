//package money;
//
//import static org.junit.Assert.*;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.junit.Test;
//
//public class TestMoney {
//	public static final Logger logger = LogManager.getLogger();
//
//	@Test
//	public final void sumOfTenDimes() {
//		Money val = Money.dollars(0.00);
//		for(int i = 0; i < 10; i++) {
//			val = val.add(Money.dollars(0.10));
//
//			logger.info("Money object created with amount of " + val.amount());
//
//		}
//		assertEquals("$1.00", val.toString());
//	}
//
//	@Test
//	public final void divide5DollarsBy3() {
//		Money [] expected = new Money[3];
//		expected[0] = Money.dollars(1.67);
//		expected[1] = Money.dollars(1.67);
//		expected[2] = Money.dollars(1.66);
//
//		Money val = Money.dollars(5.00);
//		assertArrayEquals(expected, val.divide(3));
//	}
//
//}
