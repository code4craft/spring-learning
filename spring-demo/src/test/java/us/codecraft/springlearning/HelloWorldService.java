package us.codecraft.springlearning;

/**
 * @author yihua.huang@dianping.com
 */
public class HelloWorldService {

    private String world;

    public void helloWorld(){
        System.out.println("Hello "+world+"!");
    }

    public void setWorld(String world) {
        this.world = world;
    }
}
