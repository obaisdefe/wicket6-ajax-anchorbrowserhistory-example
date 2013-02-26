package org.isdefe.wicket.anchorbrowserhistory.example;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.isdefe.wicket.anchorbrowserhistory.AnchorAjaxLink;
import org.isdefe.wicket.anchorbrowserhistory.HistoryAjaxBehaviour;

/**
 * Created by IntelliJ IDEA.
 * User: obesga
 * Date: 25-feb-2013
 * Time: 18:22:30
 * To change this template use File | Settings | File Templates.
 */
public class WebPageExample extends WebPage {

    private static final boolean LAUNCH_HISTORY_WITH_AJAX_CALLBACK = false;

    private static final boolean LAUNCH_HISTORY_WITH_BEHAVIOUR = false;

    private Label actualAnchor;


    public WebPageExample() {
        initPage();
    }

    public WebPageExample(IModel<?> model) {
        super(model);
        initPage();
    }

    public WebPageExample(PageParameters parameters) {
        super(parameters);
        initPage();
    }

    private void initPage() {

        add(new AnchorAjaxLink("anchor1"));
        add(new AnchorAjaxLink("anchor2"));
        add(new AnchorAjaxLink("anchor3"));
        add(new AnchorAjaxLink("anchor4"));
        add(new AnchorAjaxLink("anchor5"));

        Label actualAnchor = new Label("actualAnchor", new Model<String>(""));
        actualAnchor.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true).setEscapeModelStrings(false).setMarkupId("actualAnchor");
        add(actualAnchor);

        add(new HistoryAjaxBehaviour() {

            @Override
            public void onTokenChanged(AjaxRequestTarget ajaxRequestTarget, String token) {
                Label actualAnchor = (Label) WebPageExample.this.get("actualAnchor");
                actualAnchor.setDefaultModelObject(token != null && !token.isEmpty() ? token : " <i>TOKEN EMPTY</i>");

                //... if you want to send a wicket event, use it on abstract method
                //getComponent().send(getComponent().getPage(), Broadcast.BREADTH,new TokenChangedEvent(target, tokenParameterValue.toString()));
                // - - - -
                // ..or if you want to add data to the ajaxtargetrequest and use the same ajax event
                // RequestCycle.get().setMetaData(new MetaDataKey<String>(){},token);
                //
            }
        });

        if (LAUNCH_HISTORY_WITH_AJAX_CALLBACK) {
            // This is a simple ajax callback, launched on load to  have the ajaxrequesttarget to launch changetoken event
            add(new AbstractDefaultAjaxBehavior() {

                @Override
                public void renderHead(final Component component, final IHeaderResponse response) {
                    super.renderHead(component, response);
                    response.render(OnLoadHeaderItem.forScript(getCallbackScript().toString()));  // Sends callback to js
                }

                @Override
                protected void respond(AjaxRequestTarget ajaxRequestTarget) {
                     HistoryAjaxBehaviour.changeToken("plan_9_from_web_page",ajaxRequestTarget,true); // Changes token !!
                }
            });
        }
        if (LAUNCH_HISTORY_WITH_BEHAVIOUR) {
            // A simple behaviour that writes the javascript to launch the token
            add(new Behavior(){
                @Override
                public void renderHead(Component component, IHeaderResponse response) {
                    super.renderHead(component, response);
                    response.render(OnLoadHeaderItem.forScript(HistoryAjaxBehaviour.getScript("plan_9_from_web_page",true)));
                }
            });

        }
    }
}
