package us.codecraft.springlearning;

/**
 * @author yihua.huang@dianping.com
 */
public class OutputService {

	private int a = 1;

	public void output(String text) {
		System.out.println(text);
	}

    public int getA() {
        return a;
    }
}
