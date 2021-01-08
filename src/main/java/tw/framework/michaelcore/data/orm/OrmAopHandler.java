package tw.framework.michaelcore.data.orm;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.InvocationHandler;
import tw.framework.michaelcore.aop.annotation.AopHandler;
import tw.framework.michaelcore.data.JdbcTemplate;
import tw.framework.michaelcore.data.orm.annotation.OrmRepository;
import tw.framework.michaelcore.ioc.CoreContext;
import tw.framework.michaelcore.ioc.annotation.Autowired;

@AopHandler
public class OrmAopHandler implements InvocationHandler {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isDefault()) {
            switch (method.getName()) {
            case "queryAll":
                Class<?> repositoryClazz = Class.forName(proxy.getClass().getName().split("\\$\\$EnhancerByCGLIB\\$\\$")[0]);
                String table = repositoryClazz.getAnnotation(OrmRepository.class).table();
                Class<?> entityClazz = repositoryClazz.getAnnotation(OrmRepository.class).entity();
                return jdbcTemplate.queryList("SELECT * FROM " + table, entityClazz);
            }
        }
        return method.invoke(CoreContext.getRealBeanByProxy(proxy), args);
    }

}