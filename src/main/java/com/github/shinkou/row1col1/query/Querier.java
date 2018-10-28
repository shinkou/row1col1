package com.github.shinkou.row1col1.query;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import com.opencsv.CSVWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.shinkou.row1col1.query.set.QuerySet;

public class Querier
{
	private static Logger log = LoggerFactory.getLogger(QuerySet.class);
	protected static Pattern reFirstWord
		= Pattern.compile("^(\\w+)(\\.\\w+)+$");
	protected static Pattern reSql
		= Pattern.compile("^\\w+\\.sql\\.(\\w+)$");

	protected String m_filepath;
	protected Map<String, QuerySet> m_querySets;

	public Querier(String filepath)
	{
		m_filepath = filepath;
		m_querySets = new HashMap<>();
	}

	// register connection info if we have URL
	// , with optional user and password
	private void registerDatabase
	(
		PropertiesConfiguration props
		, String dbAlias
	)
		throws QuerierException
	{
		String url = (String) props.getProperty(dbAlias + ".url");
		if (null == url)
			throw new QuerierException(dbAlias + ".url is missing.");

		String user = (String) props.getProperty(dbAlias + ".user");
		String password = (String) props.getProperty(dbAlias + ".password");

		QuerySet querySet = null;

		if (null != user && null != password)
			querySet = new QuerySet(url, user, password);
		else if (null != user)
			querySet = new QuerySet(url, user);
		else
			querySet = new QuerySet(url);

		m_querySets.put(dbAlias, querySet);
	}

	// register SQL to valid database alias
	private void registerSql(String dbAlias, String sqlAlias, String sql)
		throws QuerierException
	{
		QuerySet querySet = m_querySets.get(dbAlias);

		if (null == querySet)
		{
			throw new QuerierException
			(
				"Something wrong registering database alias '"
					+ dbAlias + "'."
			);
		}

		querySet.registerSql(sqlAlias, sql);
	}

	protected void prepare(PropertiesConfiguration props)
		throws QuerierException
	{
		for(Iterator<String> iter = props.getKeys(); iter.hasNext();)
		{
			String k = iter.next();
			Matcher mFirstWord = reFirstWord.matcher(k);
			if (! mFirstWord.matches()) continue;

			String dbAlias = mFirstWord.group(1);
			if (null == dbAlias) return;

			if (! m_querySets.containsKey(dbAlias))
				registerDatabase(props, dbAlias);

			Matcher mSqlAlias = reSql.matcher(k);
			if (! mSqlAlias.matches()) continue;

			String sqlAlias = mSqlAlias.group(1);
			if (null != sqlAlias)
				registerSql(dbAlias, sqlAlias, props.getString(k));
		};
	}

	protected void outputCsv(String filepath)
		throws IOException
	{
		CSVWriter csvw = new CSVWriter(new FileWriter(filepath));
		Set<String> sqlAliases = new HashSet<>();
		m_querySets.values().forEach(querySet -> {
			sqlAliases.addAll(querySet.getSqls().keySet());
		});

		List<String> rTitle = new ArrayList<>();
		rTitle.add((new Date()).toString());
		rTitle.addAll(m_querySets.keySet());
		csvw.writeNext(rTitle.toArray(new String[0]));
		csvw.flush();

		for(String sqlAlias: sqlAliases)
		{
			List<String> r = new ArrayList<>();
			r.add(sqlAlias);
			m_querySets.forEach((dbAlias, querySet) -> {
				Object result = querySet.getResults().get(sqlAlias);
				if (null != result)
					r.add(result.toString());
				else
					r.add(null);
			});
			csvw.writeNext(r.toArray(new String[0]));
			csvw.flush();
		};

		csvw.close();
	}

	public void run()
		throws ConfigurationException, ExecutionException
		, FileNotFoundException, IOException, InterruptedException
		, QuerierException
	{
		PropertiesConfiguration props = new PropertiesConfiguration();
		props.read(new FileReader(m_filepath));
		prepare(props);
		ExecutorService es = Executors.newFixedThreadPool
		(
			m_querySets.keySet().size()
		);
		List<Future> futures = new ArrayList<>();
		for(Map.Entry<String, QuerySet> e: m_querySets.entrySet())
		{
			futures.add
			(
				es.submit
				(() -> {
					try
					{
						e.getValue().execSqls(e.getKey());
					}
					catch(Throwable t)
					{
						log.error(t.getMessage(), t);
						System.exit(1);
					}
				})
			);
		}
		es.shutdown();
		for(Future f: futures) f.get();
		outputCsv(System.getProperty("csv.output", "row1col1.csv"));
	}
}