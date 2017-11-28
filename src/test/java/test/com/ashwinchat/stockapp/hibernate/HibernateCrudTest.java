package test.com.ashwinchat.stockapp.hibernate;

import javax.transaction.Transactional;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ashwinchat.stockapp.config.HibernateConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { HibernateConfig.class })
@Transactional
public abstract class HibernateCrudTest {
    public abstract void testUpdate();

    public abstract void testInsert();

    public abstract void queryNonExistent();
}
