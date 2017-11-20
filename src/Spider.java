import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Spider
{
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a searchForWord after the successful crawl
     *
     * @param url
     *            - The URL to visit
     * @return whether or not the crawl was successful
     */
    public static List<String> crawl(String url)
    {
        List<String> links = new LinkedList<String>();
        try
        {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            if(connection.response().statusCode() == 200) // 200 is the HTTP OK status code
            // indicating that everything is great.
            {
                System.out.println("\n**Visiting** Received web page at " + url);
            }
            if(!connection.response().contentType().contains("text/html"))
            {
                System.out.println("**Failure** Retrieved something other than HTML");
                return filterLinks(url, links);
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage)
            {
                links.add(link.absUrl("href"));
            }
            return filterLinks(url, links);
        }
        catch(IOException ioe)
        {
            // We were not successful in our HTTP request
            return filterLinks(url, links);
        }
    }

    private static List<String> filterLinks(String sUrl, List<String> links) {
        URL url = null;
        try {
            url = new URL(sUrl);
            System.out.println(url.getHost());
        }
        catch(MalformedURLException e) {
            System.out.println("Failed to connect to URL " + sUrl);
            return links;
        }
        try(BufferedReader in = new BufferedReader(
                new InputStreamReader(new URL(url.getProtocol()+"://"+url.getHost()+"/robots.txt").openStream()))) {
            String line = null;
            while((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Failed to get robots.txt file for " + sUrl);
            return links;
        }

        return links;
    }
}