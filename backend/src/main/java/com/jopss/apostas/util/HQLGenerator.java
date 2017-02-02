package com.jopss.apostas.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;

public abstract class HQLGenerator {
        
        protected StringBuilder selectQuery = new StringBuilder();
	protected StringBuilder fromQuery = new StringBuilder();
	protected StringBuilder orderQuery = new StringBuilder();
	protected List<String> lWhere = new ArrayList<>();
	protected List<String> lHaving = new ArrayList<>();
	protected Map<String, Object> mapParametrosQuery = new HashMap<>();
	protected List<String> listAttr = new ArrayList<>();

	private StringBuilder montarClausulaWhere() {
                
                StringBuilder whereQuery = new StringBuilder();
		if (!lWhere.isEmpty()) {
			whereQuery.append(" WHERE ");
			Iterator iWhere = lWhere.iterator();
			while (iWhere.hasNext()) {
				whereQuery.append("(").append(iWhere.next()).append(")");
				if (iWhere.hasNext()) {
					whereQuery.append(" AND ");
				}
			}
		}
                
                return whereQuery;
	}
        
        public String getQuery(){
                StringBuilder query = new StringBuilder();
                query.append(selectQuery).append(fromQuery).append(montarClausulaWhere()).append(orderQuery);
                return query.toString();
        }
        
        public String getCountQuery(){
                StringBuilder query = new StringBuilder();
                query.append(" SELECT COUNT(*) ").append(fromQuery).append(montarClausulaWhere());
                return query.toString();
        }

	public void setParameters(Query query) {
		for (String parameter : mapParametrosQuery.keySet()) {
			query.setParameter(parameter, mapParametrosQuery.get(parameter));
		}
	}

	public StringBuilder getFromQuery() {
		return fromQuery;
	}

	public Map<String, Object> getMapParametrosQuery() {
		return mapParametrosQuery;
	}
}
