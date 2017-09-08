import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;


public class BootStrap {

    public static void main(String[] args) throws Exception {
        final ClassPathXmlApplicationContext ac;
        ac = new ClassPathXmlApplicationContext("spring.xml","spring-dubbo-provider.xml");

        if (args != null) {
            for (String str : args) {
                System.out.println(str);
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                ac.close();
            }
        });


        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
