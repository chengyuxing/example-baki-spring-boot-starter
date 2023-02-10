package org.example.jpa;

import com.github.chengyuxing.common.DataRow;
import com.github.chengyuxing.common.tuple.Pair;
import com.github.chengyuxing.sql.page.IPageable;
import com.github.chengyuxing.sql.support.executor.QueryExecutor;
import com.github.chengyuxing.sql.types.Param;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.jpa.anno.Baki;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.jpa.ParameterUtil.buildArgs;
import static org.example.jpa.ParameterUtil.resolveGenericIfNecessary;

@Aspect
public class Baki4JpaAdvice {
    private static final Logger log = LoggerFactory.getLogger(Baki4JpaAdvice.class);
    private final com.github.chengyuxing.sql.Baki baki;

    public Baki4JpaAdvice(com.github.chengyuxing.sql.Baki baki) {
        this.baki = baki;
    }

    @Pointcut("@annotation(org.example.jpa.anno.Baki)")
    public void bakiExecutorCut() {

    }

    @Around("bakiExecutorCut()")
    public Object jpaRepositoryAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Type type = method.getGenericReturnType();

        Pair<String, String> returnType = resolveGenericIfNecessary(type);
        String typeName = returnType.getItem1();
        String genericTypeName = returnType.getItem2();

        Class<?> typeClass = Class.forName(typeName);

        if (method.isAnnotationPresent(Baki.class)) {
            Baki bakiAnno = method.getAnnotation(Baki.class);
            Baki.Type exeType = bakiAnno.value();
            String sql = bakiAnno.sql();
            Map<String, Object> args = buildArgs(methodSignature, joinPoint.getArgs());
            if (exeType == Baki.Type.QUERY) {
                QueryExecutor queryExecutor = baki.query(sql).args(args);
                Function<DataRow, Object> mapper = d -> d;
                if (!genericTypeName.equals("")) {
                    Class<?> genericClass = Class.forName(genericTypeName);
                    if (!Map.class.isAssignableFrom(genericClass)) {
                        mapper = d -> d.toEntity(genericClass);
                    }
                }
                if (typeName.equals("com.github.chengyuxing.sql.PagedResource")) {
                    int page = (int) args.get("page");
                    int size = (int) args.get("size");
                    IPageable<Object> iPageable = queryExecutor.pageable(page, size);
                    if (!bakiAnno.count().equals("")) {
                        iPageable.count(bakiAnno.count());
                    }
                    if (bakiAnno.disableDefaultPageSql()) {
                        iPageable.disableDefaultPageSql();
                    }
                    return iPageable.collect(mapper);
                }
                if (List.class.isAssignableFrom(typeClass)) {
                    try (Stream<DataRow> s = queryExecutor.stream()) {
                        return s.map(mapper).collect(Collectors.toList());
                    }
                }
                if (Set.class.isAssignableFrom(typeClass)) {
                    try (Stream<DataRow> s = queryExecutor.stream()) {
                        return s.map(mapper).collect(Collectors.toSet());
                    }
                }
                if (Stream.class.isAssignableFrom(typeClass)) {
                    return queryExecutor.stream().map(mapper);
                }
                if (Optional.class.isAssignableFrom(typeClass)) {
                    return queryExecutor.findFirst().map(mapper);
                }
                if (Map.class.isAssignableFrom(typeClass)) {
                    return queryExecutor.findFirstRow();
                }
                throw new UnsupportedOperationException("query unsupport return type: " + type.getTypeName());
            } else if (exeType == Baki.Type.MODIFY) {
                if (Map.class.isAssignableFrom(typeClass)) {
                    return baki.execute(sql, args);
                }
                throw new UnsupportedOperationException("modify unsupport return type: " + type.getTypeName());
            } else if (exeType == Baki.Type.CALL) {
                if (Map.class.isAssignableFrom(typeClass)) {
                    Map<String, Param> map = new HashMap<>();
                    args.forEach((k, v) -> map.put(k, (Param) v));
                    return baki.call(sql, map);
                }
                throw new UnsupportedOperationException("procedure/function unsupport return type: " + type.getTypeName());
            }
        }
        return joinPoint.proceed();
    }
}
