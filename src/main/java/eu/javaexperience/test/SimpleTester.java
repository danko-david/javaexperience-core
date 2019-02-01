package eu.javaexperience.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import eu.javaexperience.log.JavaExperienceLoggingFacility;
import eu.javaexperience.log.LogLevel;
import eu.javaexperience.log.Loggable;
import eu.javaexperience.log.Logger;
import eu.javaexperience.log.LoggingDetailLevel;
import eu.javaexperience.semantic.TesterFunction;
import static eu.javaexperience.log.LoggingTools.*;


public class SimpleTester
{
	protected static final Logger LOG = JavaExperienceLoggingFacility.getLogger(new Loggable("Tester"));
	
	public static class TestStatistic
	{
		protected Class<?> testSubjectClass;
		protected int ignored = 0;
		protected int tests = 0;
		protected int errors = 0;
		protected int fails = 0;
		protected int success = 0;
		
		public int getIgnoredCount()
		{
			return ignored;
		}
		
		public int getTestsCount()
		{
			return tests;
		}
		
		public int getErrorsCount()
		{
			return errors;
		}
		
		public int getFailsCount()
		{
			return fails;
		}
		
		public int getSuccessCount()
		{
			return success;
		}

		public Class<?> getTestSubjectClass()
		{
			return testSubjectClass;
		}
		
		public boolean isAllPassed()
		{
			return 0 == ignored && tests == success;
		}
		
		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			
			//best case
			if(0 == ignored && tests == success)
			{
				sb.append("all of ");
				sb.append(tests);
				sb.append(" tests are passed.");
			}
			else
			{
				boolean needComma = false;
				if(0 != ignored)
				{
					sb.append(ignored);
					sb.append(" test ignored");
					needComma = true;
				}
				
				if(0 != errors)
				{
					if(needComma)
					{
						sb.append(", ");
					}
					
					sb.append(errors);
					sb.append(" test caused internal error");
					needComma = true;
				}
				
				if(0 != fails)
				{
					if(needComma)
					{
						sb.append(", ");
					}
					
					sb.append(fails);
					sb.append(" test failed");
					needComma = true;
				}
				
				{
					if(needComma)
					{
						sb.append(", ");
					}
					
					sb.append(success);
					sb.append(" test succeed");
					needComma = true;
				}
				
			}
			
			return sb.toString();
		}
		
		public LoggingDetailLevel getRecommendedLoggingPriorityForResult()
		{
			if(0 == ignored && tests == success)
			{
				return LogLevel.INFO;
			}
			else if(0 != errors)
			{
				return LogLevel.FATAL;
			}
			else
			{
				return LogLevel.WARNING;
			}
		}
	}
	
	public static TestStatistic testClass(Class<?> cls)
	{
		tryLogFormat(LOG, LogLevel.INFO, "Discovering class `%s` for @TesterFunction-s", cls);
		
		TestStatistic ts = new TestStatistic();
		
		Method[] ms = cls.getDeclaredMethods();
		for(Method m:ms)
		{
			if(null != m.getAnnotation(TesterFunction.class))
			{
				//if not static we warn, and not execute
				int mods = m.getModifiers();
				if(!Modifier.isStatic(mods))
				{
					tryLogFormat(LOG, LogLevel.WARNING, "Method `%s` has @TesterFunction annotation, but not declared as static, 'skip' testing this function.", m);
					++ts.ignored;
					continue;
				}
				
				Class<?>[] params = m.getParameterTypes();
				if(0 != params.length)
				{
					tryLogFormat(LOG, LogLevel.WARNING, "Method `%s` requires parameters of `%s`, 'skip' testing this function.", m, Arrays.toString(params));
					++ts.ignored;
					continue;
				}
				
				m.setAccessible(true);
				
				++ts.tests;
				
				Object ret = null;
				try
				{
					ret = m.invoke(null);
				}
				catch (IllegalAccessException e)
				{
					tryLogFormatException(LOG, LogLevel.ERROR, e, "Internal error raised at calling test function `%s`\n", m);
					++ts.errors;
					continue;
				}
				catch (IllegalArgumentException e)
				{
					tryLogFormatException(LOG, LogLevel.ERROR, e, "'strange, this should'nt to be happened' at calling test function `%s`\n", m);
					++ts.errors;
					continue;
				}
				catch (InvocationTargetException e)
				{
					Throwable t = e;
					if(null != e.getCause())
					{
						t = e.getCause();
					}
					tryLogFormatException(LOG, LogLevel.FATAL, t, "Exception raised in the `%s` test function, so this test failed!\n", m);
					++ts.fails;
					continue;
				}
				
				if(!Void.class.equals(m.getReturnType()))
				{
					tryLogFormat(LOG, LogLevel.INFO, "Test function `%s` has return value of type `%s` and resulted: `%s` test passed", m, m.getReturnType(), ret);
				}
				
				++ts.success;
			}
		}
		
		tryLogFormat(LOG, ts.getRecommendedLoggingPriorityForResult() , "Class testing `%s` finished. Result: %s", cls, ts);
		
		return ts;
	}
}
