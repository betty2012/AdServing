package net.mad.ads.manager.web.component.javascript;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class TestJS extends Behavior {
	private static final ResourceReference TEST_JS = new PackageResourceReference(TestJS.class,
		      "test.js");
	
	
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.renderJavaScriptReference(TEST_JS);
	}
}
