package eu.javaexperience.cli;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import eu.javaexperience.reflect.CastTo;

public class CliTest
{
	protected static final CliEntry<String> WORK_DIR = CliEntry.createFirstArgParserEntry
	(
		(e) -> e,
		"Working directory",
		"d", "-working-directory"
	);
	
	protected static final CliEntry<String> HOST = CliEntry.createFirstArgParserEntry
	(
		(e) -> e,
		"host",
		"h", "-host"
	);
	
	protected static final CliEntry<Integer> PORT = CliEntry.createFirstArgParserEntry
	(
		(e) -> Integer.parseInt(e),
		"port",
		"p", "-port"
	);
	
	protected static final CliEntry<Boolean> ENABLE = CliEntry.createFirstArgParserEntry
	(
		(e) -> (Boolean) CastTo.Boolean.cast(e),
		"enable",
		"e", "-enable"
	);
	
	protected static final CliEntry<String> ETC = CliEntry.createFirstArgParserEntry
	(
		(e) -> e,
		"etc",
		"x", "-etc"
	);
	
	protected static final CliEntry[] PROG_CLI_ENTRIES =
	{
		WORK_DIR,
		HOST,
		PORT,
		ENABLE
	};
	
	@Test
	public void testParseArgs1()
	{
		Map<String, List<String>> pa = CliTools.parseCliOpts("-d", "/tmp/");
		
		assertEquals("/tmp/", WORK_DIR.getSimple(pa));
		List<String> args = WORK_DIR.getAll(pa);
		assertEquals(1, args.size());
		assertEquals("/tmp/", args.get(0));
	}
	
	@Test
	public void testParseArgs2()
	{
		Map<String, List<String>> pa = CliTools.parseCliOpts("--working-directory", "/tmp/");
		
		assertEquals("/tmp/", WORK_DIR.getSimple(pa));
		List<String> args = WORK_DIR.getAll(pa);
		assertEquals(1, args.size());
		assertEquals("/tmp/", args.get(0));
	}
	
	@Test
	public void testParseArgs3()
	{
		Map<String, List<String>> pa = CliTools.parseCliOpts("--working-directory", "/tmp/", "-d", "/var/tmp/");

		//parameters appears in specification order
		assertEquals("/var/tmp/", WORK_DIR.getSimple(pa));
		List<String> args = WORK_DIR.getAll(pa);
		assertEquals(2, args.size());
		assertEquals("/var/tmp/", args.get(0));
		assertEquals("/tmp/", args.get(1));
	}
	
	@Test
	public void testParseArgs4()
	{
		Map<String, List<String>> pa = CliTools.parseCliOpts("-d", "/tmp/", "-h", "127.0.0.1", "--port", "80", "-e");
		
		assertEquals("/tmp/", WORK_DIR.tryParse(pa));
		assertEquals("127.0.0.1", HOST.tryParse(pa));
		assertEquals((Integer) 80, PORT.tryParse(pa));
		assertEquals("80", PORT.getSimple(pa));
		assertEquals(null, ENABLE.tryParse(pa));
		assertTrue(ENABLE.hasOption(pa));
		assertFalse(ETC.hasOption(pa));
	}
	
	@Test
	public void testParseArgs5()
	{
		Map<String, List<String>> pa = CliTools.parseCliOpts("-d", "/tmp/", "-h", "127.0.0.1", "--port", "80", "-e", "false");
		
		assertEquals("/tmp/", WORK_DIR.tryParse(pa));
		assertEquals("127.0.0.1", HOST.tryParse(pa));
		assertEquals((Integer) 80, PORT.tryParse(pa));
		assertEquals("80", PORT.getSimple(pa));
		assertEquals(false, ENABLE.tryParse(pa));
		assertTrue(ENABLE.hasOption(pa));
		assertFalse(ETC.hasOption(pa));
	}
	
	@Test
	public void testHelp()
	{
		String help = CliTools.renderListAllOption(PROG_CLI_ENTRIES);
		assertTrue(help.contains("\t-d, --working-directory"));
		assertTrue(help.contains("\t-h, --host"));
		assertTrue(help.contains("\t-p, --port"));
		assertTrue(help.contains("\t-e, --enable"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBadParam()
	{
		Map<String, List<String>> pa = CliTools.parseCliOpts("-h", "127.0.0.1", "-e", "-p", "x");
		PORT.tryParse(pa);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBadParam2()
	{
		Map<String, List<String>> pa = CliTools.parseCliOpts("-h", "127.0.0.1", "-p", "80", "-");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBadUsage()
	{
		Map<String, List<String>> pa = CliTools.parseCliOpts("asdf");
	}
	
}
