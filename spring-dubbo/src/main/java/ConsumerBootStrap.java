import com.libin.service2.TestService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by libin on 2017/9/8.
 */
public class ConsumerBootStrap {

    public static void main(String[] args) {

        final ClassPathXmlApplicationContext ac;
        ac = new ClassPathXmlApplicationContext("spring-dubbo-consumer.xml");
        TestService bean = ac.getBean(TestService.class);
        System.out.println(bean.test(1));
    }
}
