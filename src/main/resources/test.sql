/*[now]*/
select current_timestamp, now();

/*[c]*/
select 1,2,3,4,5,6;

/*[mvn]*/
select * from test.mvn_dependency_query(
    --#choose
        --#when :keywords | len > 10
            :keywords
        --#break
        --#default
            'chengyuxing'
        --#break
    --#end
    );