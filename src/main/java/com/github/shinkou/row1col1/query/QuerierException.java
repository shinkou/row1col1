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
package com.github.shinkou.row1col1.query;

public class QuerierException extends Exception
{
	public QuerierException()
	{
		super();
	}

	public QuerierException(String msg)
	{
		super(msg);
	}

	public QuerierException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

	public QuerierException
	(
		String msg
		, Throwable cause
		, boolean enableSuppression
		, boolean writableStackTrace
	)
	{
		super(msg, cause, enableSuppression, writableStackTrace);
	}

	public QuerierException(Throwable cause)
	{
		super(cause);
	}
}
