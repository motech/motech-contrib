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
import java.util.Map;
import java.util.Properties;

public abstract class LuceneAwareMotechBaseRepository<T extends MotechBaseDataObject> extends MotechBaseRepository<T> {

    protected LuceneAwareMotechBaseRepository(Class<T> type, LuceneAwareCouchDbConnector db) {
        super(type, db);
    }

    protected List<T> filter(QueryDefinition queryDefinition, Map<String, Object> filterParams, Map<String, Object> sortParams, Integer skip, Integer limit) {
        CustomLuceneResult luceneResult = getLuceneResult(queryDefinition, filterParams, sortParams, limit, skip);
        List<CustomLuceneResult.Row<T>> resultRows = luceneResult.getRows();
        List<T> results = new ArrayList();
        for (CustomLuceneResult.Row<T> row : resultRows)
            results.add(row.getDoc());
        return results;
    }

    protected int count(QueryDefinition queryDefinition, Map<String, Object> filterParams) {
        return getLuceneResult(queryDefinition, filterParams, null, 1, 0).getTotalRows();
    }

    private CustomLuceneResult getLuceneResult(QueryDefinition queryDefinition, Map<String, Object> queryParams, Map<String, Object> sortParams, Integer limit, Integer skip) {
        LuceneQuery query = getLuceneQuery(queryDefinition, queryParams, sortParams, limit, skip);
        TypeReference resultDocType = getTypeReference();
        return ((LuceneAwareCouchDbConnector) db).queryLucene(query, resultDocType);
    }

    private LuceneQuery getLuceneQuery(QueryDefinition queryDefinition, Map<String, Object> queryParams, Map<String, Object> sortParams, Integer limit, Integer skip) {
        LuceneQuery query = new LuceneQuery(queryDefinition.viewName(), queryDefinition.searchFunctionName());
        QueryBuilder queryBuilder = new QueryBuilder(queryParams, sortParams, queryDefinition);
        query.setQuery(queryBuilder.build());
        query.setSort(queryBuilder.buildSortCriteria());
        query.setIncludeDocs(true);
        query.setLimit(limit);
        query.setSkip(skip);
        return query;
    }

    protected abstract TypeReference getTypeReference();
}