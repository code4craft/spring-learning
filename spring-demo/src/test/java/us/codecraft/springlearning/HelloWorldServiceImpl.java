package us.codecraft.springlearning;

/**
 * @author yihua.huang@dianping.com
 */
public class HelloWorldServiceImpl implements HelloWorldService {

    private OutputService outputService;

    @Override
    public void helloWorld(){
        outputService.output("Hello World!");
    }

    public void setOutputService(OutputService outputService) {
        this.outputService = outputService;
    }
}
