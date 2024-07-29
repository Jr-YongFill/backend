package com.yongfill.server.global.config;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.jpa.FactoryExpressionTransformer;
import com.querydsl.jpa.QueryHandler;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLTemplates;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import org.hibernate.transform.ResultTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.stream.Stream;

@Configuration
public class QueryDSLConfig{


    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }

    @Bean
    public com.querydsl.sql.Configuration sqlConfiguration(){
        return new com.querydsl.sql.Configuration(MySQLTemplates.DEFAULT);
    }

    @Bean
    public QueryHandler queryHandler() {
        return new QueryHandler()  {
            @Override
            public void addEntity(Query query, String alias, Class<?> type) {
                // do nothing
            }

            @Override
            public void addScalar(Query query, String alias, Class<?> type) {
                // do nothing
            }

            @Override
            public boolean createNativeQueryTyped() {
                return true;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T> CloseableIterator<T> iterate(Query query, final FactoryExpression<?> projection) {
                Stream<T> stream = stream(query, projection);
                return new IteratorAdapter<T>(stream.iterator(), stream::close);
            }

            @Override
            public <T> Stream<T> stream(Query query, FactoryExpression<?> projection) {
                final Stream resultStream = query.getResultStream();
                if (projection != null) {
                    return resultStream.map(element -> projection.newInstance((Object[]) (element.getClass().isArray() ? element : new Object[]{element})));
                }
                return resultStream;
            }

            @Override
            public boolean transform(Query query, FactoryExpression<?> projection) {
                try {
                    ResultTransformer transformer = new FactoryExpressionTransformer(projection);
                    query.unwrap(org.hibernate.query.Query.class).setResultTransformer(transformer);
                    return true;
                } catch (PersistenceException e) {
                    return false;
                }
            }

            @Override
            public boolean wrapEntityProjections() {
                return false;
            }
        };
    }
}
