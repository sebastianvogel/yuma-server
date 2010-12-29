package at.ait.dme.yuma.server.gui;

import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import at.ait.dme.yuma.server.gui.Navbar.NavbarItem;
import at.ait.dme.yuma.server.gui.search.Search;

/**
 * @author Rainer
 *
 */
public abstract class BaseTextPage extends WebPage {

	public BaseTextPage (final PageParameters parameters) {	}
	
	public void setTitle(String title) {
		add(new Label("title", title));		
	}
		
	public void setHeading(String heading) {
		add(new Label("heading", heading));
	}
	
	public void setNavbar(Navbar navbar) {
		add(new ListView<NavbarItem>("navbar", navbar.getItems()) {
			private static final long serialVersionUID = 9140947690636209796L;

			@SuppressWarnings({ "unchecked", "rawtypes" })
			protected void populateItem(ListItem<NavbarItem> item) {
				NavbarItem n = item.getModelObject();
				item.add(
					new BookmarkablePageLink("nav-item", n.getPageClass())
						.add(new Label("nav-label", n.getLabel())));
				
				if (n.isSelected())
					item.add(new SimpleAttributeModifier("class", "selected"));
		    }
		});
		
		add(new BookmarkablePageLink<String>("home", Search.class));
	}
	
}
