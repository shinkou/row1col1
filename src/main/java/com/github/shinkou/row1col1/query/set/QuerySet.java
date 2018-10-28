package com.github.shinkou.row1col1.query.set;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.shinkou.row1col1.query.QuerierException;

public class QuerySet
{
	private static Logger log = LoggerFactory.getLogger(QuerySet.class);
	protected String m_url;
	protected String m_user;
	protected String m_password;
	protected Map<String, String> m_sqls;
	protected Map<String, Object> m_results;

	public QuerySet(String url, String user, String password)
	{
		m_url = url;
		m_user = user;
		m_password = password;
		m_sqls = new HashMap<>();
		m_results = new HashMap<>();
	}

	public QuerySet(String url, String user)
	{
		this(url, user, null);
	}

	public QuerySet(String url)
	{
		this(url, null, null);
	}

	public void registerSql(String alias, String sql)
	{
		m_sqls.put(alias, sql);
	}

	public Map<String, String> getSqls()
	{
		return m_sqls;
	}

	public Map<String, Object> getResults()
	{
		return m_results;
	}

	public void execSqls(String dbAlias)
		throws QuerierException
	{
		try
		(
			Connection cnx = null == m_user
				? DriverManager.getConnection(m_url)
				: DriverManager.getConnection(m_url, m_user, m_password)
		)
		{
			cnx.setAutoCommit(false);
			Statement stmt = cnx.createStatement();
			for(Map.Entry<String, String> e: m_sqls.entrySet())
			{
				String alias = e.getKey();
				String sql = e.getValue();
				log.info("{}.sql.{}", dbAlias, alias);
				ResultSet rs = stmt.executeQuery(sql);
				m_results.put(alias, rs.next() ? rs.getObject(1) : null);
			}
		}
		catch(SQLException e)
		{
			throw new QuerierException
			(
				"Error executing SQL in '" + dbAlias + "'."
				, e
			);
		}
	}
}
