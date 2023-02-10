package com.skynet.tests;

import com.github.chengyuxing.sql.Args;
import com.github.chengyuxing.sql.utils.SqlTranslator;
import org.junit.Test;

import java.util.Map;

public class SomeTests {
    @Test
    public void test() throws Exception {
        System.out.println(Map.class.isAssignableFrom(Class.forName("com.github.chengyuxing.common.DataRow")));
    }

    @Test
    public void test1() throws Exception {
        System.out.println(Class.forName("", false, ClassLoader.getSystemClassLoader()));
    }

    @Test
    public void testxsz() throws Exception{
        String sql = "insert into user (x, xm ,xb) values (:x, :xm, :xb)";
        SqlTranslator sqlTranslator = new SqlTranslator(':');
        System.out.println(sqlTranslator.getPreparedSql(sql, Args.create()));
    }
}
