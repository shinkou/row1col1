/*
 * Copyright (C) 2018 - 2020  Chun-Kwong Wong
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
