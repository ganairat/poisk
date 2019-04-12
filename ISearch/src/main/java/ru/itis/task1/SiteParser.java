/*package ru.itis.task1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.itis.dao.ArticleDao;
import ru.itis.models.Article;
import ru.itis.util.Constants;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.util.List;

public class SiteParser {
    {
        static void Main(string[] args)
        {
            List<string> links = new List<string>();
            string uri = "https://www.lostfilm.tv";
            List<Article> serials = new List<Article>();

            for (int i = 51; i < 57; i++)
            {
                var url = "https://www.lostfilm.tv/series/?type=search&l=" + i + "&s=1&t=2";

                HtmlDocument doc = new HtmlDocument();
                doc.LoadHtml(getRequest(url));
                HtmlNodeCollection c = doc.DocumentNode.SelectNodes("//*[@class='row']/a/@href");

                if (c != null)
                {
                    foreach (HtmlNode n in c)
                    {
                        var temp = n.Attributes["href"].Value;
                        if (temp != null)
                        {
                            links.Add(temp);
                        }
                    }
                }

            }

            foreach (var t in links)
            {
                HtmlDocument d = new HtmlDocument();
                d.LoadHtml(getRequest(uri + t));
                HtmlNode qq = d.DocumentNode.SelectSingleNode("//*[@class='title-ru']/text()");
                string name = qq.InnerText;
                string tags = "";
                HtmlNodeCollection r1 = d.DocumentNode.SelectNodes("//*[@class='right-box']/a/text()");
                foreach(var tag in r1)
                {
                    tags += tag.InnerText + " ";
                }
                var xbody = d.DocumentNode.SelectNodes("//*[@class='text-block description']/div[@class='body']/div[1]/text()");
                string body = "";
                if (xbody!= null)
                    foreach (var x in xbody)
                {
                    body += x.InnerText;
                }
                Article s = new Article(name, tags, body, uri+t);
                serials.Add(s);
            }

            using (ApplicationContext db = new ApplicationContext())
            {
                foreach(var ser in serials)
                {
                    db.Add(ser);
                }
                db.SaveChanges();
            }
        }

        static string getRequest(string uri)
        {
            StringBuilder sb = new StringBuilder();
            byte[] buf = new byte[8192];
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(uri);
            HttpWebResponse response = (HttpWebResponse)request.GetResponse();
            Stream resStream = response.GetResponseStream();
            int count = 0;
            do
            {
                count = resStream.Read(buf, 0, buf.Length);
                if (count != 0)
                {
                    sb.Append(Encoding.UTF8.GetString(buf, 0, count));
                }
            }
            while (count > 0);
            return sb.ToString();
        }
    }
}
*/