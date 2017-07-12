package ehcache;

import model.WorkExperience;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LinShunkang on 7/12/17.
 */
public class EhcacheByXML {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
//        test1();
        test2();
    }

    private static void test1() {
        final URL myUrl = EhcacheByXML.class.getResource("/conf/ehcache.xml");
        XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
        CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();
        cacheManager.getRuntimeConfiguration();

        Cache<Long, List> cache = cacheManager.getCache("GeekWorkExp", Long.class, List.class);

        cache.put(1L, new ArrayList(1));
        cache.put(2L, new ArrayList(2));

    }

    private static void test2() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        XmlConfiguration xmlConfiguration = new XmlConfiguration(EhcacheByXML.class.getResource("/conf/ehcache.xml"));
        CacheConfigurationBuilder<Long, List> configurationBuilder = xmlConfiguration.newCacheConfigurationBuilderFromTemplate("defaultListCacheTemplate", Long.class, List.class);
        configurationBuilder = configurationBuilder.withResourcePools(ResourcePoolsBuilder.heap(1000));


    }
}
