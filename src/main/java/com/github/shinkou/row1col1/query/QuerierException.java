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
