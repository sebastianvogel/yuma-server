package at.ait.dme.yuma.server.bootstrap;

public class Data {
	
	public static final String ANNOTATION_JSON_ORIGINAL =
		"[{ \"parent-id\" : null , " +
		  "\"root-id\" : null , " +
		  "\"title\" : \"Ponte 25 de Abril\" , " +
		  "\"text\" : \"The 25 de Abril Bridge is a suspension bridge connecting the city of Lisbon, capital of Portugal, " + 
		  "to the municipality of Almada on the left bank of the Tagus river. It was inaugurated on August 6, 1966 " +
		  "and a train platform was added in 1999.\", " +
		  "\"scope\" : \"public\" , "+
		  "\"last-modified\" : 1224043200000 ,"+
		  "\"created\" : 1224043200000 , "+
		  "\"created-by\" :  { \"user-name\" : \"rsimon\" } , " +
		  "\"fragment\" : \"" +
			"<svg:svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" width=\\\"640px\\\" height=\\\"480px\\\" viewbox=\\\"0px 0px 640px 480px\\\"> " +
			  "<svg:defs xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"> " +
			    "<svg:symbol xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" id=\\\"Polygon\\\"> " +
				"<svg:polygon xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" " +
				"points=\\\"0,24 45,22 45,32 49,32 49,23 190,20 285,19 193,0 119,17 48,5\\\" stroke=\\\"rgb(229,0,0)\\\" " +
				"stroke-width=\\\"2\\\" fill=\\\"none\\\"> " +
				"</svg:polygon> " +
				"</svg:symbol> " +
			  "</svg:defs>" +
			"</svg:svg>" +
		  "\" , "+
		  "\"media-type\" : \"image\" , "+
		  "\"object-uri\" : \"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/4\\/49\\/Hirschvogel_Map_Austria.jpg\"" +
		"}]";
	
	public static final String ANNOTATION_JSON_UPDATE =
		"{ \"parent-id\" : \"\" , " +
		  "\"root-id\" : \"\" , " +
		  "\"title\" : \"Ponte 25 de Abril\" , "+
		  "\"text\" : \"The 25 de Abril Bridge is a suspension bridge connecting the city of Lisbon, capital of Portugal, " + 
		  "to the municipality of Almada on the left bank of the Tagus river. It was inaugurated on August 6, 1966 " +
		  "and a train platform was added in 1999. It is often compared to the Golden Gate Bridge in San Francisco, USA, due to " +
		  "their similarities and same construction company. With a total length of 2.277 m, it is the 19th largest " +
		  "suspension bridge in the world. The upper platform carries six car lanes, the lower platform two train tracks. Until " +
		  "1974 the bridge was named Salazar Bridge.\", " +
		  "\"scope\" : \"public\" , "+
		  "\"last-modified\" : 1224043200000 ,"+
		  "\"created\" : 1224043200000 , "+
		  "\"created-by\" :  { \"user-name\" : \"rsimon\" } , " +
		  "\"fragment\" : \"" +
			"<svg:svg xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" width=\\\"640px\\\" height=\\\"480px\\\" viewbox=\\\"0px 0px 640px 480px\\\"> " +
			  "<svg:defs xmlns:svg=\\\"http://www.w3.org/2000/svg\\\"> " +
				"<svg:symbol xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" id=\\\"Polygon\\\"> " +
				"<svg:polygon xmlns:svg=\\\"http://www.w3.org/2000/svg\\\" " +
				"points=\\\"0,24 45,22 45,32 49,32 49,23 190,20 285,19 193,0 119,17 48,5\\\" stroke=\\\"rgb(229,0,0)\\\" " +
				"stroke-width=\\\"2\\\" fill=\\\"none\\\"> " +
				"</svg:polygon> " +
				"</svg:symbol> " +
			  "</svg:defs>" +
			"</svg:svg>" +
		  "\" , "+
		  "\"media-type\" : \"image\" , "+
		  "\"object-uri\" : \"http://dme.ait.ac.at/object/lissabon.jpg\", " +
		  "\"tags\" : [ " +
		    "{ \"alt-labels\" : [ { \"val\" : \"Lissabon\" , \"lang\" : \"de\" } , { \"val\" : \"Lisboa\" }  ] , \"label\" : \"Lisbon\" , \"type\" : \"place\" , \"relation\" : { \"namespace\" : \"http://geonames.org/geo#\" , \"property\" : \"spatiallyContains\" } , \"alt-descriptions\" : [ ] , \"lang\" : \"en\" , \"uri\" : \"http://www.geonames.org/2267057/\"} ," +
		    "{ \"alt-labels\" : [ ] , \"label\" : \"Portugal\" , \"type\" : \"place\" , \"relation\" : { \"namespace\" : \"http://geonames.org/geo#\" , \"property\" : \"spatiallyContains\" } , \"alt-descriptions\" : [ ] , \"lang\" : \"en\" , \"uri\" : \"http://www.geonames.org/2264397/\"} " +
		  "]" +
		"}";
	
	public static final String ROOT_JSON =
		"{ \"parent-id\" : \"\" , " +
		  "\"root-id\" : \"\" , " +
		  "\"title\" : \"Ponte 25 de Abril\" , "+
		  "\"text\" : \"Root annotation!\" ," +
		  "\"scope\" : \"public\" , "+
		  "\"last-modified\" : 1224043200000 ," +
		  "\"created\" : 1224043200000 , " +
		  "\"created-by\" :  { \"user-name\" : \"rsimon\" } , " +
		  "\"media-type\" : \"image\" , " +
		  "\"object-uri\" : \"http://dme.ait.ac.at/object/lissabon.jpg\"" +
		"}";
	
	private static final String REPLY_JSON =
		"{ \"parent-id\" : \"@parent@\" , " +
		  "\"root-id\" : \"@root@\" , " +
		  "\"title\" : \"RE: Ponte 25 de Abril\" , "+
		  "\"text\" : \"Interesting!\" ," +
		  "\"scope\" : \"public\" , "+
		  "\"last-modified\" : 1224043200000 ," +
		  "\"created\" : 1224043200000 , " +
		  "\"created-by\" :  { \"user-name\" : \"rsimon\" } , " +
		  "\"media-type\" : \"image\" , " +
		  "\"object-uri\" : \"http://dme.ait.ac.at/object/lissabon.jpg\"" +
		"}";
	
	public static String reply(String root, String parent) {
		return REPLY_JSON
			.replace("@root@", root)
			.replace("@parent@", parent);
	}

}
