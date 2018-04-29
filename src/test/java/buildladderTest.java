
import com.light.springboot.utils.buildLadder;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.*;

import static org.testng.Assert.assertTrue;

/** 
* buildladder Tester. 
* 
* @author <Authors name> 
* @since <pre>03/06/2018</pre> 
* @version 1.0 
*/ 
public class buildladderTest { 

@Before
public void before() throws Exception {
} 

@After
public void after() throws Exception {
} 

/** 
* 
* Method: find_ladder(String word1, String word2, Map word_list) 
* 
*/ 
@Test
public void testFind_ladder() throws Exception {

    Map test_list = new HashMap();

    test_list.put("an", 0);
    test_list.put("or", 0);
    test_list.put("on", 0);
    test_list.put("op", 0);

    buildLadder b = new buildLadder();

    LinkedList<String> ladder = b.return_ladder("an", "op", test_list);

    int lsize = ladder.size();
    String pre_wd, nex_wd;
    int wd_len;
    int diff_num;

    for (int i = 0; i < lsize - 1; ++i){
        diff_num = 0;
        pre_wd = ladder.get(i);
        nex_wd = ladder.get(i + 1);
        wd_len = pre_wd.length();
        for (int j = 0; j < wd_len; ++j){
            char pre_letter =  pre_wd.charAt(j);
            char nex_letter =  nex_wd.charAt(j);
            if (pre_letter != nex_letter){
                diff_num += 1;
            }
        }
        assertTrue(diff_num == 1);
    }


} 


} 
