package at.ait.dme.yuma.server.gui;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;


public class LinkHeaderContributor extends HeaderContributor {

	private static final long serialVersionUID = -2587519445989482910L;

	public LinkHeaderContributor(IHeaderContributor headerContributor) {
		super(headerContributor);
	}

    public static final LinkHeaderContributor forRssLink(final String url) {

        return new LinkHeaderContributor(new IHeaderContributor() {

        	private static final long serialVersionUID = -3678334461356322549L;

			public void renderHead(IHeaderResponse response) {
                response.renderString(
                	"<link rel=\"alternate\" type=\"application/rss+xml\" href=\"" + url + "\" />"
                );
            }
        });
    }
} 