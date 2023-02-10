package org.example.dao;

import com.github.chengyuxing.common.DataRow;
import com.github.chengyuxing.sql.PagedResource;
import org.example.jpa.anno.Baki;

import java.util.List;
import java.util.Map;

//@Repository
public interface MyDao {

    @Baki(sql = "&test.now")
//    @Query(nativeQuery = true)
    PagedResource<DataRow> getNow(Map<String, Object> args);

    @Baki(sql = "&test.c")
//    @Query(nativeQuery = true)
    List<DataRow> getAll();

    @Baki(sql = "&test.mvn")
//    @Query(nativeQuery = true)
    List<Map<String, Object>> queryMaven(String keywords);
}
