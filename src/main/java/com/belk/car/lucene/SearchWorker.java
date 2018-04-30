package com.belk.car.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.springframework.core.io.ClassPathResource;

import com.belk.car.app.model.Car;
import com.belk.car.app.service.CarManager;

public class SearchWorker {
	protected final Log logger = LogFactory.getLog(SearchWorker.class);
	
	private String indexDir;
	private CarManager carManager ;
	
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public synchronized int index() {
		int numIndexed = 0;
		try {
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriter writer = new IndexWriter(getIndexDir(), analyzer, true);
			numIndexed += indexCars(writer);
			writer.optimize();
			writer.close();
		} catch (IOException e) {
			logger.warn("Index could not be created. ", e);
		}
		return numIndexed;
	}

	private int indexCars(IndexWriter writer) throws IOException, CorruptIndexException {
		int numIndexed = 0;
		try {
			List<Car> cars = carManager.getAllCars() ;
			for (Car c : cars) {
				Document doc = getDocument(c);
				writer.addDocument(doc);
				numIndexed++;
			}
		} catch (Exception e) {
			logger.warn("Querying for all CARS failed. ", e);
		}
		return numIndexed;
	}

	private Document getDocument(Car c) {
		Document doc = new Document();
		doc.add(new Field("carid", String.valueOf(c.getCarId()), 
				Field.Store.YES, Field.Index.TOKENIZED));
		doc.add(new Field("vendorStyle#", c.getVendorStyle().getVendorStyleNumber(), 
				Field.Store.YES, Field.Index.TOKENIZED));
		doc.add(new Field("dept#", c.getDepartment().getDeptCd(), Field.Store.YES, Field.Index.TOKENIZED));
		doc.add(new Field("class#", String.valueOf(c.getVendorStyle().getClassification().getBelkClassNumber()), 
				Field.Store.YES, Field.Index.TOKENIZED));
		doc.add(new Field("dueDate", String.format("MMddyyyy", c.getDueDate()), Field.Store.YES, 
				Field.Index.TOKENIZED));
		doc.add(new Field("status", c.getStatusCd(), 
				Field.Store.YES, Field.Index.TOKENIZED));
		doc.add(new Field("wfStatus",c.getCurrentWorkFlowStatus().getStatusCd(), 
				Field.Store.YES, Field.Index.TOKENIZED));
		return doc;
	}
	
	public List<Car> search(Map<String, String> inpQueries) {
		List<Car> cars = null ;
		String[] allFields = {"carid", "vendorStyle#", "dept#", "class#", "wfStatus", "dueDate"};

		List<String> queries  = new ArrayList<String>() ;
		List<String> fields  = new ArrayList<String>() ;
		List<BooleanClause.Occur> flags = new ArrayList<BooleanClause.Occur>() ;
		for (int i=0;i<allFields.length;i++) {
			if (allFields[i].equals("dueDate")) {
				String dueDateFrom = inpQueries.get("dueDateFrom") ;
				String dueDateTo = inpQueries.get("dueDateTo");
				if (StringUtils.isNotBlank(dueDateFrom) && StringUtils.isNotBlank(dueDateTo)) {
					fields.add(allFields[i]);
					queries.add("["+dueDateFrom + " TO " + dueDateTo+"]");
					flags.add(BooleanClause.Occur.MUST);
				} else if (StringUtils.isNotBlank(dueDateFrom)) {
					fields.add(allFields[i]);
					queries.add(dueDateFrom);
					flags.add(BooleanClause.Occur.MUST);
				} else if (StringUtils.isNotBlank(dueDateTo)) {
					fields.add(allFields[i]);
					queries.add(dueDateTo);
					flags.add(BooleanClause.Occur.MUST);
				}
			} else {
				if (StringUtils.isNotBlank(inpQueries.get(allFields[i]))) {
					fields.add(allFields[i]);
					queries.add(inpQueries.get(allFields[i]));
					flags.add(BooleanClause.Occur.MUST);
				}
			}
		}
		
		try {
			Query query = getQuery(queries.toArray(new String[queries.size()]), fields.toArray(new String[fields.size()]), flags.toArray(new BooleanClause.Occur[flags.size()]));
	
			Hits hits = getSearcher().search(query);
			if (hits != null) {
				if (logger.isDebugEnabled())
					logger.debug("No of Hits:" + hits.length());
				
				cars = new ArrayList<Car>(hits.length());
				for (int i = 0; i < hits.length(); i++) {
					Document doc = hits.doc(i);
					Car car = carManager.getCarFromId(Long.valueOf(doc.getField("carid").stringValue()));
					cars.add(car);
				}
			}
		} catch (IOException e) {
			logger.error("Could not perform search: ", e);
		} catch (ParseException e) {
			logger.error("Could not perform search: ", e);
		}

		
		
		return cars ;
	}
	
	
	/**
	 * 
	 * @param allQueries
	 * @return
	 */
	public List<Car> search(String[] allQueries) {
		List<Car> carResults = null;
		try {
			String[] allFields = {"carid", "vendorStyle#", "dept#", "class#", "wfStatus", "dueDate"};
			int len=0;
			for(int j=0; j<allQueries.length; j++){
				if(! allQueries[j].equalsIgnoreCase("")){
					len++;
				}
			}
			String[] fields = new String[len];
			String[] queries = new String[len];
			BooleanClause.Occur[] flags = new BooleanClause.Occur[len];
			
			int x = 0;
			for(int j=0; j<allQueries.length; j++){
				if(! allQueries[j].equalsIgnoreCase("")){
					fields[x]=allFields[j];
					queries[x]=allQueries[j];
					flags[x] = BooleanClause.Occur.MUST;
					x++;
				}
			}
			
			Query query = getQuery(queries, fields, flags);

			Hits hits = getSearcher().search(query);
			if (hits != null) {
				if (logger.isDebugEnabled())
					logger.debug("No of Hits:" + hits.length());
				
				carResults = new ArrayList<Car>(hits.length());
				for (int i = 0; i < hits.length(); i++) {
					Document doc = hits.doc(i);
					Car car = carManager.getCarFromId(Long.valueOf(doc.getField("carid").stringValue()));
					carResults.add(car);
				}
			}
			
		} catch (IOException e) {
			logger.error("Could not perform search: ", e);
		} catch (ParseException e) {
			logger.error("Could not perform search: ", e);
		}
		return carResults;
	}

	private Searcher getSearcher() throws IOException {
		return new IndexSearcher(getIndexDir());
	}
	
	private Query getQuery(String[] queries, String[] fields, BooleanClause.Occur[] flags) throws ParseException{
		Analyzer analyzer = new StandardAnalyzer();
		//QueryParser parser = new MultiFieldQueryParser(fields, analyzer);
		//Query query = parser.parse(queryStr);
		Query query = MultiFieldQueryParser.parse(queries, fields, flags, analyzer);

		return query;
	}
	
	private String getIndexDir() {
		return new ClassPathResource(indexDir).getPath();
	}
	
	public void setIndexDir(String indexDir) {
		this.indexDir = indexDir;
	}
	
}
