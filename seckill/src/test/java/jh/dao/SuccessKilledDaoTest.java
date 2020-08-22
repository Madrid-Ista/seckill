package jh.dao;

import jh.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        int update = successKilledDao.insertSuccessKilled(1000L, 18629567777L);
        System.out.println("update:" + update);
    }

    @Test
    public void queryByIdWithSeckill() {
        SuccessKilled successkilled = successKilledDao.queryByIdWithSeckill(1000, 18629567777L);
        System.out.println(successkilled);
        System.out.println(successkilled.getSeckill());
    }
}