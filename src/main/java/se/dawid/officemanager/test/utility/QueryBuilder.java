package se.dawid.officemanager.test.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryBuilder {



    private enum QueryType { SELECT, INSERT, UPDATE, DELETE }

    private QueryType queryType;
    private String table;
    private List<String> columns = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    private List<String> sets = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private List<String> whereConditions = new ArrayList<>();
    private List<String> orderByColumns = new ArrayList<>();
    private String groupBy;
    private Integer limit;

    public QueryBuilder select(String... columns) {
        queryType = QueryType.SELECT;
        this.columns.addAll(List.of(columns));
        return this;
    }

    public QueryBuilder insertInto(String table, String... columns) {
        queryType = QueryType.INSERT;
        this.table = table;
        this.columns.addAll(List.of(columns));
        return this;
    }

    public QueryBuilder values(String... values) {
        if (queryType != QueryType.INSERT) {
            throw new IllegalStateException("VALUES can only be used with INSERT INTO queries.");
        }
        this.values.addAll(List.of(values));
        return this;
    }

    public QueryBuilder update(String table) {
        queryType = QueryType.UPDATE;
        this.table = table;
        return this;
    }

    public QueryBuilder set(String column, String value) {
        if (queryType != QueryType.UPDATE) {
            throw new IllegalStateException("SET can only be used with UPDATE queries.");
        }
        sets.add(column + " = " + value);
        return this;
    }

    public QueryBuilder deleteFrom(String table) {
        queryType = QueryType.DELETE;
        this.table = table;
        return this;
    }

    public QueryBuilder from(String table) {
        if (queryType != QueryType.SELECT) {
            throw new IllegalStateException("FROM can only be used with SELECT queries.");
        }
        this.table = table;
        return this;
    }

    public QueryBuilder join(String type, String table, String onCondition) {
        joins.add(type + " JOIN " + table + " ON " + onCondition);
        return this;
    }

    public QueryBuilder where(String condition) {
        whereConditions.add(condition);
        return this;
    }

    public QueryBuilder groupBy(String column) {
        this.groupBy = column;
        return this;
    }

    public QueryBuilder orderBy(String... columns) {
        orderByColumns.addAll(List.of(columns));
        return this;
    }

    public QueryBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public String build() {
        if (queryType == null || table == null) {
            throw new IllegalStateException("Query type and table must be specified.");
        }

        StringBuilder query = new StringBuilder();

        switch (queryType) {
            case SELECT -> {
                query.append("SELECT ").append(columns.isEmpty() ? "*" : String.join(", ", columns));
                query.append(" FROM ").append(table);
                joins.forEach(query::append);
                if (!whereConditions.isEmpty()) {
                    query.append(" WHERE ").append(String.join(" AND ", whereConditions));
                }
                if (groupBy != null) {
                    query.append(" GROUP BY ").append(groupBy);
                }
                if (!orderByColumns.isEmpty()) {
                    query.append(" ORDER BY ").append(String.join(", ", orderByColumns));
                }
                if (limit != null) {
                    query.append(" LIMIT ").append(limit);
                }
            }
            case INSERT -> {
                query.append("INSERT INTO ").append(table);
                query.append(" (").append(String.join(", ", columns)).append(")");
                query.append(" VALUES (").append(values.stream().map(value -> "?").collect(Collectors.joining(", "))).append(")");
            }
            case UPDATE -> {
                query.append("UPDATE ").append(table).append(" SET ").append(String.join(", ", sets));
                if (!whereConditions.isEmpty()) {
                    query.append(" WHERE ").append(String.join(" AND ", whereConditions));
                }
            }
            case DELETE -> {
                query.append("DELETE FROM ").append(table);
                if (!whereConditions.isEmpty()) {
                    query.append(" WHERE ").append(String.join(" AND ", whereConditions));
                }
            }
            default -> throw new UnsupportedOperationException("Query type not supported");
        }

        return query.toString();
    }



}
