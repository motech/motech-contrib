package org.motechproject.couchdb.lucene.repository;

import com.github.ldriscoll.ektorplucene.CustomLuceneResult;
import com.github.ldriscoll.ektorplucene.LuceneAwareCouchDbConnector;
import com.github.ldriscoll.ektorplucene.LuceneQuery;
import org.codehaus.jackson.type.TypeReference;
import org.motechproject.couchdb.lucene.query.QueryBuilder;
import org.motechproject.couchdb.lucene.query.QueryDefinition;
import org.motechproject.dao.MotechBaseRepository;
import org.motechproject.model.MotechBaseDataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class LuceneAwareMotechBaseRepository<T extends MotechBaseDataObject> extends MotechBaseRepository<T> {

    protected LuceneAwareMotechBaseRepository(Class<T> type, LuceneAwareCouchDbConnector db) {
        super(type, db);
    }

    protected List<T> filter(QueryDefinition queryDefinition, Properties filterParams, Integer skip, Integer limit) {
        CustomLuceneResult luceneResult = getLuceneResult(queryDefinition, filterParams, limit, skip);
        List<CustomLuceneResult.Row<T>> resultRows = luceneResult.getRows();
        List<T> results = new ArrayList();
        for (CustomLuceneResult.Row<T> row : resultRows)
            results.add(row.getDoc());
        return results;
    }

    protected int count(QueryDefinition queryDefinition, Properties filterParams) {
        return getLuceneResult(queryDefinition, filterParams, 1, 0).getTotalRows();
    }

    private CustomLuceneResult getLuceneResult(QueryDefinition queryDefinition, Properties queryParams, Integer limit, Integer skip) {
        LuceneQuery query = getLuceneQuery(queryDefinition, queryParams, limit, skip);
        TypeReference resultDocType = getTypeReference();
        return ((LuceneAwareCouchDbConnector) db).queryLucene(query, resultDocType);
    }

    private LuceneQuery getLuceneQuery(QueryDefinition queryDefinition, Properties queryParams, Integer limit, Integer skip) {
        LuceneQuery query = new LuceneQuery(queryDefinition.viewName(), queryDefinition.searchFunctionName());
        String queryString = new QueryBuilder(queryParams, queryDefinition).build();
        query.setQuery(queryString.toString());
        query.setIncludeDocs(true);
        query.setLimit(limit);
        query.setSkip(skip);
        return query;
    }

    protected abstract TypeReference getTypeReference();
}