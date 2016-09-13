import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.*;

public class URLParser{
	public URLParser(){

	}

	public String getRawTextFromURL(String urlString){
		try{
			URL url = new URL(urlString);
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
		
			String encoding = con.getContentEncoding();
			encoding = (encoding == null ? "UTF-8" : encoding);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buf = new byte[8192];
			int len = 0;
			while ((len = in.read(buf)) != -1) {
			    outputStream.write(buf, 0, len);
			}
			String textContent = new String(outputStream.toByteArray(), encoding);

			return textContent;
		} catch(Exception e){
			System.out.println("Error:" +e);
		}

		return null;
	}

	public void getSitesFromPage(String pageURL, int numOfSites){
		try {
			String[] lines = getRawTextFromURL(pageURL).split("\n");			
			Pattern patternLinesWithSites = Pattern.compile("siteinfo/(.*?)\"");

			for(String line : lines){
				Matcher matcherLinesWithSites = patternLinesWithSites.matcher(line);

				if (matcherLinesWithSites.find() && numOfSites>0)
				{
				    System.out.println(matcherLinesWithSites.group(1));
				    numOfSites--;
				}
			}

		} catch (Exception e){
			System.out.println("UnExpected Exception:"+e);
		}
	}

	public void listTopSites(int numOfSites){
		getSitesFromPage("http://www.alexa.com/topsites", (numOfSites>25?25:numOfSites)); //Printing 1st page's urls
		if(numOfSites >25){
			numOfSites -= 25;
			int N=1;
			while(numOfSites >0){
				getSitesFromPage("http://www.alexa.com/topsites/global;"+N, (numOfSites>25?25:numOfSites));
				numOfSites -= 25; 
				N++;
			}
		}
	}

	public static void main(String[] args){		
		try {
			int numberOfSites = Integer.parseInt(args[0]);
			
			if(numberOfSites <=0 || numberOfSites>500){
				System.out.println("Number must be between 0 and 500.");
				return;
			}

			URLParser urlParser = new URLParser();
			urlParser.listTopSites(numberOfSites);

		} catch (Exception e){
			System.out.println("Unexpected exception:"+e);
		}		
	}
}