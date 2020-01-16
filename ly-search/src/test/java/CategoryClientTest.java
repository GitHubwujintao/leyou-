import com.leyou.LySearchService;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void testQueryCategories() {
        List<String> names = this.categoryClient.queryNameByCids(Arrays.asList(1L, 2L, 3L));
        names.forEach(System.out::println);
       // String s = goodsClient.querySpuDetailBySpuId(5L);
        //Map<String, Object> stringObjectMap = goodsClient.querySpuDetailBySpuId(5L);
//        for (Map.Entry a:stringObjectMap.entrySet()) {
//            System.out.println(a.getKey()+"+"+a.getValue());
//
//        }
        //System.out.println(s);

    }
}