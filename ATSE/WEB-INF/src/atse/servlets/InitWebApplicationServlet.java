package atse.servlets;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import atse.common.Settings;
import atse.common.properties.PropertyLoader;
import atse.data.Cluster;

public class InitWebApplicationServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 1554578775116253156L;
	
	protected static Log LOGGER = LogFactory.getLog(InitWebApplicationServlet.class);
	
	public static Date UPDATE_TIME = new Date();

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);

	
		if (!StringUtils.isBlank(getServletContext().getInitParameter("properties-file"))) {
			LOGGER.debug("Setting the Properties File");
			Settings.getInstance().setPropertyFile(getServletContext().getInitParameter("properties-file"));
		}

	
		
		boolean firstLoad = true;
		String keyName="", newValue="";
		PropertyLoader.setApplicationProperties(firstLoad, keyName, newValue);
		
		if(StringUtils.isNotEmpty(Settings.getInstance().getClusterFilePath())) {
			try {
				
				BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(Settings.getInstance().getClusterFilePath()));
				int count = 1;
				while (true) {
					Cluster cluster = new Cluster();
					String line = bufferedReader.readLine();
					if (StringUtils.isNotEmpty(line)) {
						cluster.setClusterNo(count++);
						String[] words = line.split(Settings.WORD_SPLIT_CHAR);
						
						for (String word : words) {
							if (StringUtils.isNotEmpty(word)) {
								cluster.getWordList().add(word);
							}
						}
						Settings.clusters.add(cluster);
					} else
						break;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		}
	}
}
