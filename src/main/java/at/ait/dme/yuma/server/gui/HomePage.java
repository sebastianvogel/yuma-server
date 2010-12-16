package at.ait.dme.yuma.server.gui;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public HomePage(final PageParameters parameters) {
        add(new BookmarkablePageLink("link-search", Search.class));
    }
    
}
