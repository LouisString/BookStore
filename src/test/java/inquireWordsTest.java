
import com.light.springboot.utils.inquireWords;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.*;

import static org.testng.Assert.assertTrue;


/** 
* inquireWords Tester. 
* 
* @author <Authors name> 
* @since <pre>03/06/2018</pre> 
* @version 1.0 
*/ 
public class inquireWordsTest { 

@Before
public void before() throws Exception {
} 

@After
public void after() throws Exception {
} 

/** 
* 
* Method: get_words(Map word_list) 
* 
*/ 
@Test
public void testGet_words() throws Exception {
    Map test_list = new HashMap();

    test_list.put("a", 0);
    test_list.put("an", 0);
    test_list.put("b", 0);
    test_list.put("or", 0);
    test_list.put("on", 0);

    inquireWords i = new inquireWords();
    List<String> words = i.getWords(test_list);

    String word1 = words.get(0);
    String word2 = words.get(1);

    assertTrue(test_list.containsKey(word1));
    assertTrue(test_list.containsKey(word2));

    int len1 = word1.length();
    int len2 = word2.length();

    assertTrue(len1 == len2);
} 


} 
