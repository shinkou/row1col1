package com.github.shinkou.row1col1;

import com.github.shinkou.row1col1.query.Querier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Starter
{
	private static Logger log = LoggerFactory.getLogger(Starter.class);

	public static void main(String[] args)
	{
		if (0 < args.length)
		{
			String arg = args[0];
			try
			{
				Querier q = new Querier(arg);
				q.run();
			}
			catch(Exception e)
			{
				log.error("Error processing file '" + arg + "'.", e);
			}
		}
	}
}
