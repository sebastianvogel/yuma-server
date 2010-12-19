package at.ait.dme.yuma.server.gui.search;

import java.util.HashMap;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;


public class Search extends WebPage {

	public static final String QUERY_PARAM = "q";
	
    public Search(final PageParameters parameters) {
        add(new SearchForm("form", new Query()));
    }
        
	private class SearchForm extends StatelessForm<Query> {

		private static final long serialVersionUID = -7070901543076806931L;

		public SearchForm(String id, Query q) {
			super(id, new CompoundPropertyModel<Query>(q));
			add(new TextField<String>("query"));
		}
		
		@Override
		protected void onSubmit() {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("q", getModelObject().toString());
            setResponsePage(new Results(new PageParameters(params)));
		}
    	
    }

    /**
     * Simple wrapper for the search form model.
     */
    class Query {
    	
    	private String query;
    	
    	public String getQuery() {
    		return query;
    	}
    	
    	public void setQuery(String query) {
    		this.query = query; 
    	}
    	
    	@Override
    	public String toString() {
    		return query; 
    	}
    	
    }
    
}
