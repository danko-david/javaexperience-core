package eu.javaexperience.text;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import static eu.javaexperience.text.StringTools.*;
import org.junit.Test;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.map.MapTools;

public class StringToolsTest
{
	@Test
	public void testRandomString()
	{
		assertEquals(0, randomString(0).length());
		assertEquals(1, randomString(1).length());
		assertEquals(20, randomString(20).length());
		assertEquals(1024, randomString(1024).length());
	}
	
	@Test
	public void testRandomStringCustom()
	{
		assertEquals("aaaa", randomStringFrom("a", 4));
		assertEquals("ZZZ", randomStringFrom("Z", 3));
		String rnd = randomStringFrom("ab", 1024);
		assertTrue(rnd.contains("a"));
		assertTrue(rnd.contains("b"));
	}
	
	@Test
	public void testPassOnlyNumbers()
	{
		assertEquals("314", passOnlyNumbers("pi: 3.14"));
		assertEquals("314", passOnlyNumbers("pi: 3,14"));
	}
	
	@Test
	public void testClampLength()
	{
		assertEquals(null, clampLength(null, 0));
		assertEquals("", clampLength("sko", 0));
		assertEquals("s", clampLength("sko", 1));
		assertEquals("sk", clampLength("sko", 2));
		assertEquals("sko", clampLength("sko", 3));
		assertEquals("sko", clampLength("sko", 5));
	}
	
	@Test
	public void testFirstBetween()
	{
		final String subject = "http://subpage.mysite.eu/category-4892c";
		assertEquals("subpage", getFirstBetween(subject, "http://", ".mysite.eu"));
		assertEquals("4892", getFirstBetween(subject, "category-", "c"));
		assertEquals(subject, getFirstBetween(subject, "\\", "\\"));
		assertEquals("subpage.mysite.eu", getFirstBetween(subject, "//", "/"));
		
		assertEquals("home", getFirstBetween("/home/user/git/linux/.git", "/", "/"));
		
		assertEquals("", getFirstBetween(subject, "/", "/"));//returns the empty string between http://
		
		assertEquals("default_value", getFirstBetween(subject, "\\", "\\", "default_value"));
		
		assertEquals("home", getFirstBetween("/home/user/git/linux/.git", "/", "/"));
		
		assertEquals(null, getFirstBetween("/home", "/", " /", null));
	}
	
	@Test
	public void testLastBetween()
	{
		assertEquals("subpage", getLastBetween("http://subpage.mysite.eu/category-4892c", "http://", ".mysite.eu"));
		assertEquals("funnyPic", getLastBetween("/home/user/Images/funnyPic.jpg", "/", "."));
		assertEquals("", getLastBetween("/home/user/git/linux/.git", "/", ".git"));
		assertEquals("linux", getLastBetween("/home/user/git/linux/.git", "/", "/"));
		assertEquals("/home/user/git/linux/.git", getLastBetween("/home/user/git/linux/.git", "\\", " \\"));
		
		assertEquals(null, getLastBetween("/home", "/", "/", null));
		assertEquals(null, getLastBetween("home", "/", "/", null));
		assertEquals(null, getLastBetween("home/", "/", "/", null));
		
		assertEquals(null, getLastBetween("/home", "/", ".", null));
	}
	
	@Test
	public void testSubstringAfterLastString()
	{
		assertEquals("4892c", getSubstringAfterLastString("http://subpage.mysite.eu/category-4892c", "/category-"));
		
		assertEquals("file.jpg", getSubstringAfterLastString("/home/user/Document/file.jpg", "/"));
		assertEquals("file.jpg", getSubstringAfterLastString("file.jpg", "/"));
		assertEquals("jpg", getSubstringAfterLastString("/home/user/Document/file.jpg", "."));
		assertEquals("/home/user/Document/file", getSubstringAfterLastString("/home/user/Document/file", "."));
		
		assertEquals("file", getSubstringAfterLastString("/home/user/Document/file", "/"));
	}
	
	
	@Test
	public void testSubstringBeforeLastString()
	{
		assertEquals("myfile", getSubstringBeforeLastString("myfile.ext", "."));
		assertEquals("myfile", getSubstringBeforeLastString("myfile", "."));
		
		assertEquals("_main", getSubstringBeforeLastString("_main_", "_"));
		
		assertEquals("archive.tar", getSubstringBeforeLastString("archive.tar.gz", "."));
		
		assertEquals("/home/user/Document", getSubstringBeforeLastString("/home/user/Document/file", "/"));
		
		assertEquals(null, getSubstringBeforeLastString("myfile", ".", null));
	}
	
	@Test
	public void testSubstringAfterFirstString()
	{
		assertEquals("jpg", getSubstringAfterFirstString("image.jpg", "."));
		assertEquals("image", getSubstringAfterFirstString("image", "."));
		
		assertEquals("main_", getSubstringAfterFirstString("_main_", "_"));
		
		assertEquals("tar.gz", getSubstringAfterFirstString("archive.tar.gz", "."));
		
		assertEquals("home/user/Document/file", getSubstringAfterFirstString("/home/user/Document/file", "/"));
		
		assertEquals("user/Document/file", getSubstringAfterFirstString("home/user/Document/file", "/"));
		
		assertEquals(null, getSubstringAfterFirstString("image", ".", null));
	}
	
	@Test
	public void testSubstringBeforeFirstString()
	{
		assertEquals("image", getSubstringBeforeFirstString("image.jpg", "."));
		assertEquals("image", getSubstringBeforeFirstString("image", "."));
		
		assertEquals("", getSubstringBeforeFirstString("_main_", "_"));
		
		assertEquals("archive", getSubstringBeforeFirstString("archive.tar.gz", "."));
		
		assertEquals("", getSubstringBeforeFirstString("/home/user/Document/file", "/"));
		
		assertEquals("home", getSubstringBeforeFirstString("home/user/Document/file", "/"));
		
		assertEquals(null, getSubstringBeforeFirstString("image", ".", null));
	}
	
	
	@Test
	public void testUntilTerminator()
	{
		assertEquals("archive", untilTerminator("archive.tar.gz", '.'));
		assertEquals("user", untilTerminator("user/archive.tar.gz", '/'));
		assertEquals("/home/user/archive.tar.gz", untilTerminator("/home/user/archive.tar.gz", '_'));
		assertEquals(null, untilTerminator(null, '_'));
	}
	
	@Test
	public void testRepeatChar()
	{
		assertEquals("", repeatChar('a', -25));
		assertEquals("", repeatChar('a', 0));
		assertEquals("aaaaa", repeatChar('a', 5));
		assertEquals("\t\t\t", repeatChar('\t', 3));
		assertEquals("\n\n", repeatChar('\n', 2));
	}
	
	@Test
	public void testRepeatString()
	{
		assertEquals("", repeatString("a", -25));
		assertEquals("", repeatString("a", 0));
		assertEquals("aaaaa", repeatString("a", 5));
		assertEquals("\t\t\t", repeatString("\t", 3));
		assertEquals("\n\n", repeatString("\n", 2));
		
		assertEquals("asd asd asd ", repeatString("asd ", 3));
		assertEquals("asd \n\tasd \n\tasd \n\t", repeatString("asd \n\t", 3));
	}
	
	@Test
	public void testWhitoutNullAndEmptyString()
	{
		assertArrayEquals(new String[]{"a", "b"}, whitoutNullAndEmptyString("a", "b"));
		assertArrayEquals(new String[]{"a", "b"}, whitoutNullAndEmptyString("a", null, "", "b"));
		assertArrayEquals(new String[]{"a", "b"}, whitoutNullAndEmptyString(null, "a", null, "", "b"));
		
		assertArrayEquals(new String[0], whitoutNullAndEmptyString("", null, "", null, null));
	}
	
	
	@Test
	public void testReplaceAllStrings()
	{
		assertEquals("bbb", replaceAllStrings("aaa", "a", "b"));
		assertEquals("bbb", replaceAllStrings("bbb", "a", "b"));
		assertEquals("bbb", replaceAllStrings("   ", " ", "b"));
		assertEquals("cbc", replaceAllStrings("cac", "a", "b"));
		
		assertEquals("", replaceAllStrings("", "a", "b"));
		
		assertEquals("LazyFoxbcd", replaceAllStrings("abcd", "a", "LazyFox"));
		assertEquals("LazyFoxbcLazyFox", replaceAllStrings("abca", "a", "LazyFox"));
		
		assertEquals("For Richard, at $DAY ", replaceAllStrings("For $NAME, at $DAY ", "$NAME", "Richard"));
	}
	
	@Test
	public void testReplaceAllStringsCaseInsensitive()
	{
		assertEquals("bbb", replaceAllStringsCaseInsensitive("aAa", "a", "b"));
		assertEquals("bbb", replaceAllStringsCaseInsensitive("aAa", "A", "b"));
		assertEquals("bbb", replaceAllStringsCaseInsensitive("aaa", "A", "b"));
		assertEquals("bbb", replaceAllStringsCaseInsensitive("bbb", "a", "b"));
		assertEquals("bbb", replaceAllStringsCaseInsensitive("   ", " ", "b"));
		assertEquals("cbc", replaceAllStringsCaseInsensitive("cAc", "a", "b"));
		assertEquals("cbc", replaceAllStringsCaseInsensitive("cac", "A", "b"));
		
		assertEquals("", replaceAllStringsCaseInsensitive("", "a", "b"));
		
		assertEquals("LazyFoxbcd", replaceAllStringsCaseInsensitive("abcd", "A", "LazyFox"));
		assertEquals("LazyFoxbcLazyFox", replaceAllStringsCaseInsensitive("abcA", "a", "LazyFox"));
		
		assertEquals("For Richard, at $DAY ", replaceAllStringsCaseInsensitive("For $name, at $DAY ", "$NAME", "Richard"));
		
	}
	
	
	@Test
	public void testToString()
	{
		assertEquals("null", StringTools.toString(null));
		assertEquals("null", StringTools.toString("null"));
		assertEquals("hello", StringTools.toString("hello"));
		assertEquals("12", StringTools.toString(12));
	}
	
	@Test
	public void testDeAccent()
	{
		assertEquals("usual text", deAccent("usual text"));
		
		//hungarian panagramms
		//floodproof mirrordrill
		assertEquals("Arvizturo tukorfurogep", deAccent("Árvíztűrő tükörfúrógép"));
		//five branched impact tester paralyzer
		assertEquals("Otagu utomubenito", deAccent("Ötágú ütőműbénítő"));
		assertEquals("Egy hutlen vejet fuloncsipo, duhos mexikoi ur Wesselenyinel mazol Quitoban.", deAccent("Egy hűtlen vejét fülöncsípő, dühös mexikói úr Wesselényinél mázol Quitóban.")); 
	}
	
	@Test
	public void testPlainSplit()
	{
		assertArrayEquals(new String[]{"as_ds_fd"}, plainSplit("as_ds_fd", " "));
		assertArrayEquals(new String[]{"as", "ds", "fd"}, plainSplit("as_ds_fd", "_"));
		assertArrayEquals(new String[]{"^", "$"}, plainSplit("^.*$", ".*"));
		assertArrayEquals(new String[]{"a", "b", "c"}, plainSplit("a*b*c", "*"));
	}
	
	@Test
	public void testCutToBytes()
	{
		assertEquals("", cutToBytes("", 100));
		assertEquals("aaa", cutToBytes("aaaaa", 3));
		assertEquals("á", cutToBytes("ááá", 3));
		assertEquals("áá", cutToBytes("ááá", 4));
		assertEquals("Árvíztű", cutToBytes("Árvíztűrő tükörfúrógép", 10));
		assertEquals("Árvíztűr", cutToBytes("Árvíztűrő tükörfúrógép", 11));
		assertEquals("Árvíztűr", cutToBytes("Árvíztűrő tükörfúrógép", 12));
	}
	
	@Test
	public void testEscapeToJavaString()
	{
		assertEquals("usual", escapeToJavaString("usual"));
		assertEquals("usual\\\\n", escapeToJavaString("usual\n"));
		assertEquals("\\\\tpublic void test()\\\\n", escapeToJavaString("	public void test()\n"));
		assertEquals("10\\\\2", escapeToJavaString("10\\2"));
	}
	
	@Test
	public void testOrEmptyString()
	{
		assertEquals("", orEmptyString(null));
		assertEquals("", orEmptyString(""));
		assertEquals("asd", orEmptyString("asd"));
	}
	
	@Test
	public void testIsNullOrEmpty()
	{
		assertTrue(isNullOrEmpty(null));
		assertTrue(isNullOrEmpty(""));
		assertFalse(isNullOrEmpty(" "));
		assertFalse(isNullOrEmpty("\n"));
		assertFalse(isNullOrEmpty("\b"));
		assertFalse(isNullOrEmpty("asdsf"));
	}
	
	@Test
	public void testIsNullOrTrimEmpty()
	{
		assertTrue(isNullOrTrimEmpty(null));
		assertTrue(isNullOrTrimEmpty(""));
		assertTrue(isNullOrTrimEmpty(" "));
		assertTrue(isNullOrTrimEmpty("\n"));
		assertTrue(isNullOrTrimEmpty("\b"));
		assertFalse(isNullOrTrimEmpty("asdsf"));
		assertFalse(isNullOrTrimEmpty(" _ "));
	}
	
	@Test
	public void testEnsureStartsWith()
	{
		assertEquals(".git", ensureStartsWith("git", "."));
		assertEquals("git", ensureStartsWith("git", ""));
		assertEquals("/home/user/myfile", ensureStartsWith("myfile", "/home/user/"));
		assertEquals("/home/user/myfile", ensureStartsWith("/home/user/myfile", "/home/user/"));
	}
	
	@Test
	public void testMultiReplaceAllString()
	{
		assertEquals
		(
			"Dear Robert, yesterday you missed ...",
			multiReplaceAllString
			(
				"Dear $NAME, $REL_DAY you $ACTION ...",
				MapTools.inlineSmallMap
				(
					"$NAME", "Robert",
					"$REL_DAY", "yesterday",
					"$ACTION", "missed"
				)
			)
		);
		
		//this test is to warn of a pitfall. Note: SmallMap keeps the order of insertion 
		assertEquals
		(
			"Dear Robert, yesterday you missed ...",
			multiReplaceAllString
			(
				"$MSG",
				MapTools.inlineSmallMap
				(
					"$MSG", "Dear $NAME, $REL_DAY you $ACTION ...", 
					"$NAME", "Robert",
					"$REL_DAY", "yesterday",
					"$ACTION", "missed"
				)
			)
		);
	}
	
	@Test
	public void testEnsureEndsWith()
	{
		assertEquals("image.jpg", ensureEndsWith("image", ".jpg"));
		assertEquals("image.jpg", ensureEndsWith("image.jpg", ".jpg"));
	}
	
	@Test
	public void testIsIn()
	{
		assertTrue(isIn("a", "a", "b", "c"));
		assertTrue(isIn("a", null, "a", "b" ));
		assertFalse(isIn("a"));
		assertFalse(isIn("a", "b", "c"));
		
		assertFalse(isIn("a", null, "b", "c"));
		
		assertTrue(isIn("", ""));
		assertFalse(isIn(""));
	}
	
	
	@Test
	public void testConcat()
	{
		assertEquals("abc", concat("a", "b", "c"));
		assertEquals("anullc", concat("a", null, "c"));
	}
	
	@Test
	public void testConcatNotNull()
	{
		assertEquals("abc", concatNotNull("a", "b", "c"));
		assertEquals("ac", concatNotNull("a", null, "c"));
	}
	
	
	@Test
	public void testToStringOrNull()
	{
		assertEquals(null, toStringOrNull(null));
		assertEquals("12", toStringOrNull(12));
		assertEquals("asd", toStringOrNull("asd"));
	}
	
	
	@Test
	public void testoccurrenceIn()
	{
		assertEquals(0, occurrenceIn("asdff", "g"));
		assertEquals(1, occurrenceIn("asdff", "a"));
		assertEquals(2, occurrenceIn("asdff", "f"));
		
		assertEquals(7, occurrenceIn("this is a begining of a beatiful friendship", " "));
		assertEquals(2, occurrenceIn("this is a begining of a beatiful friendship", " a "));
		assertEquals(3, occurrenceIn("this is a begining of a beatiful friendship", "a"));
		
		assertEquals(-1, occurrenceIn("this is a begining of a beatiful friendship", ""));
	}
	
	
	@Test
	public void testJoin()
	{
		assertEquals("a b c d", join(" ", "a", "b", "c", "d"));
		assertEquals("a_b_c_d", join("_", "a", "b", "c", "d"));
		
		assertEquals("a b c d", join(" ", CollectionTools.inlineArrayList("a", "b", "c", "d")));
		assertEquals("a_b_c_d", join("_", CollectionTools.inlineArrayList("a", "b", "c", "d")));
	}
	
	
	@Test
	public void testReplaceAll()
	{
		assertArrayEquals(new String[]{"b", "bb", "bbb", "bcb"}, replaceAll(new String[]{"a","aa", "aaa", "aca"}, "a", "b"));
	}
	
	
	@Test
	public void testSplitModifyJoin()
	{
		assertEquals
		(
			"300;5321;-300000",
			splitModifyJoin
			(
				"3e2;5321;-3e5",
				Pattern.compile(";"),
				(e)->String.valueOf(((Double)Double.parseDouble(e)).intValue()),
				";"
			)
		);
	}
	
	@Test
	public void testTryTrim()
	{
		assertEquals(null, tryTrim(null));
		assertEquals("", tryTrim(""));
		assertEquals("", tryTrim("\n"));
		assertEquals("", tryTrim("\b"));
		assertEquals("", tryTrim(" "));
		assertEquals("asdf", tryTrim(" \n asdf "));
	}
}
