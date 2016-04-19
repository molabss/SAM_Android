package br.com.constran.mobile.persistence;


public class Query {

    private boolean distinct;

    private String[] columns;
    private String[] conditionsArgs;

    private String conditions;
    private String tableJoin;
    private String groupBy;
    private String orderBy;
    private String having;
    private String limit;

    public Query() {

    }

    public Query(boolean distinct) {
        this.distinct = distinct;
    }

    public Query(String limit) {
        this.limit = limit;
    }

    public String[] getConditionsArgs() {
        return conditionsArgs;
    }

    public void setConditionsArgs(String[] conditionsArgs) {
        this.conditionsArgs = conditionsArgs;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getTableJoin() {
        return tableJoin;
    }

    public void setTableJoin(String tableJoin) {
        this.tableJoin = tableJoin;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getHaving() {
        return having;
    }

    public void setHaving(String having) {
        this.having = having;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

}
