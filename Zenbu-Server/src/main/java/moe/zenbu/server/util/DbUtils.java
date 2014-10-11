package moe.zenbu.server.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DbUtils
{
    private final static SqlSession sqlSession = new SqlSessionFactoryBuilder().build(DbUtils.class.getResourceAsStream("/mybatis_config.xml")).openSession();

    public static SqlSession getSqlSession()
    {
        return sqlSession;
    }
}
