package com.dylanops.review.api;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

public class Fruit {

    public Long id;
    public String name;

    public Fruit(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private static Fruit from(Row row) {
        return new Fruit(row.getLong("id"), row.getString("name"));
    }

    public static Multi<Fruit> findAll(MySQLPool client) {
        return client.query("SELECT id, name FROM fruits ORDER BY name ASC").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Fruit::from);
    }

    public static Uni<Fruit> findById(MySQLPool client, Long id) {
        return client.preparedQuery("SELECT id, name FROM fruits WHERE id = ?").execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Uni<Integer> save(MySQLPool client) {

        return client.preparedQuery("INSERT INTO fruits (name) VALUES (?)").execute(Tuple.of(name))
                .onItem().transform(r -> r.size() > 0 ? 1 : 0);
    }

    public static Uni<Boolean> delete(MySQLPool client, Long id) {
        return client.preparedQuery("DELETE FROM fruits WHERE id = ?").execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

}
