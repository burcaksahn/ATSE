package atse.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import atse.common.Settings;
import atse.data.Cluster;

@SuppressWarnings("serial")
public class HomeAction extends BaseAction{

	
	
	private File wordListFile;
	private String searchWord;
	
	protected static Log logger = LogFactory.getLog(HomeAction.class);
	

	public String displayHome(){
		
		this.setNavPageId(NAV_PAGE_HOME);
		
		return SUCCESS;
	}

	public String displayClusterManagement(){
		
		this.setNavPageId(NAV_PAGE_CLUSTER_MANAGEMENT);
		
		return SUCCESS;
	}

	public String displayClusterSearch(){
		
		this.setNavPageId(NAV_PAGE_CLUSTER_SEARCH);
		
		return SUCCESS;
	}
	
	public String getClusterListJson() throws JSONException {
		JSONArray jsonArray = new JSONArray();
		int i=1;
		for(Cluster cluster : Settings.clusters) {
			JSONObject jsonObject = new JSONObject();
			String strList="";
			for(int m=0;m<cluster.getWordList().size();m++) {
				strList+=cluster.getWordList().get(m);
				if(m+1!=cluster.getWordList().size())
					strList+=", ";
			}
			jsonObject.put("clusterNo", cluster.getClusterNo());
			jsonObject.put("wordList", strList);
			jsonObject.put("index", i++);
			
			jsonArray.put(jsonObject);
		}
		
		return this.returnJsonResponse(jsonArray.toString());
	}
	
	public String searchClusterListJson() throws JSONException {
		JSONArray jsonArray = new JSONArray();
		HashMap<Integer, Boolean> isMatchedCluster = new HashMap<Integer, Boolean>();
		
		int i = 1;
		for (Cluster cluster : Settings.clusters) {
			for (String word : cluster.getWordList()) {
				if (StringUtils.contains(word, this.getSearchWord())) {
					if (isMatchedCluster.get(cluster.getClusterNo()) == null) {
						isMatchedCluster.put(cluster.getClusterNo(), true);

						JSONObject jsonObject = new JSONObject();
						String strList = "";
						for (int m = 0; m < cluster.getWordList().size(); m++) {
							strList += cluster.getWordList().get(m);
							if (m + 1 != cluster.getWordList().size())
								strList += ", ";
						}

						jsonObject.put("clusterNo", cluster.getClusterNo());
						jsonObject.put("wordList", strList);
						jsonObject.put("index", i++);

						jsonArray.put(jsonObject);
					}
				}
			}
		}

		return this.returnJsonResponse(jsonArray.toString());
	}
	
	public String uploadFile() throws IOException {
		if(this.getWordListFile()!=null) {
			BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(this.getWordListFile().getAbsolutePath()));
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
		else {
			return this.returnJsonErrorResponse("File not found");
		}
		return this.returnJsonSuccessResponse("ok");
	}
	
	public String displayError(){
		this.addActionMessage(getText("error.message"));
		return SUCCESS;
	}
	
	public String displayPermissionDeny(){
		return SUCCESS;
	}
	
	public String displayPermissionDenyJSON(){
		return returnJsonResponse(SUCCESS);
	}
	
	/////////////////////////////////////////////////////////////
	// GET / SET Methods
	//

	public File getWordListFile() {
		return wordListFile;
	}

	public void setWordListFile(File wordListFile) {
		this.wordListFile = wordListFile;
	}

	public String getSearchWord() {
		return searchWord;
	}

	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}


}