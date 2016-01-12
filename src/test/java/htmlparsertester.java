import java.io.*;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.hadoop.baiduSpider.Spider.getContext;

/**
 * Created by v-shaoleili on 2016/1/12.
 */
public class htmlparsertester {

    private static String ENCODE = "UTF-8";

    private static void message(String szMsg) {
        try {
            System.out.println(new String(szMsg.getBytes(ENCODE), System.getProperty("file.encoding")));
        } catch (Exception e) {
        }
    }

    public static String openFile(String szFileName) {
        try {
            BufferedReader bis = new BufferedReader(new InputStreamReader(new FileInputStream(new File(szFileName)), ENCODE));
            String szContent = "";
            String szTemp;

            while ((szTemp = bis.readLine()) != null) {
                szContent += szTemp + "\n";
            }
            bis.close();
            return szContent;
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) {
        String url = "http://baike.baidu.com/view/3080925.htm";
        String context = getContext(url);
        Document doc = Jsoup.parse(context);
        Elements resultLinks = doc.select("dd > h1");
        for (Element element :resultLinks) {
            Element attr = element.parent().after("class");
            String text = element.text();

            System.out.println(attr + ":" + text);
        }


    }

}
