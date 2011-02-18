package hudson.plugins.campfire;

import hudson.tasks.Publisher;
import hudson.tasks.BuildStepDescriptor;
import hudson.model.AbstractProject;

import org.kohsuke.stapler.StaplerRequest;
import net.sf.json.JSONObject;

public class DescriptorImpl extends BuildStepDescriptor<Publisher> {
    private boolean enabled = false;
    private String subdomain;
    private String token;
    private String room;
    private String hudsonUrl;
    private boolean ssl;
    private boolean smartNotify;
    private String memeTemplateType;
    private String memeTemplateID;
    private String memeGeneratorName;
    private String memeTemplateIDFailure;
    private String memeGeneratorNameFailure;

    public DescriptorImpl() {
        super(CampfireNotifier.class);
        load();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public String getToken() {
        return token;
    }

    public String getRoom() {
        return room;
    }

    public String getHudsonUrl() {
        return hudsonUrl;
    }

    public boolean getSsl() {
        return ssl;
    }
    
    public boolean getSmartNotify() {
        return smartNotify;
    }

    public String getMemeTemplateType() {
        return memeTemplateType;
    }

    public String getMemeTemplateID() {
        return memeTemplateID;
    }

    public String getMemeGeneratorName() {
        return memeGeneratorName;
    }

    public String getMemeTemplateIDFailure() {
        return memeTemplateIDFailure;
    }

    public String getMemeGeneratorNameFailure() {
        return memeGeneratorNameFailure;
    }

    public boolean isApplicable(Class<? extends AbstractProject> aClass) {
        return true;
    }

    /**
     * @see hudson.model.Descriptor#newInstance(org.kohsuke.stapler.StaplerRequest)
     */
    @Override
    public Publisher newInstance(StaplerRequest req, JSONObject formData) throws FormException {
        String projectRoom = req.getParameter("roomName");
        if ( projectRoom == null || projectRoom.trim().length() == 0 ) {
            projectRoom = room;
        }
        try {
            return new CampfireNotifier(subdomain, token, projectRoom, hudsonUrl, ssl,
                    smartNotify, memeTemplateType, memeTemplateID, memeGeneratorName,
                    memeTemplateIDFailure, memeGeneratorNameFailure);
        } catch (Exception e) {
            throw new FormException("Failed to initialize campfire notifier - check your campfire notifier configuration settings", e, "");
        }
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        subdomain = req.getParameter("campfireSubdomain");
        token = req.getParameter("campfireToken");
        room = req.getParameter("campfireRoom");
        hudsonUrl = req.getParameter("campfireHudsonUrl");
        memeTemplateType = req.getParameter("campfireMemeTemplateType");
        memeTemplateID = req.getParameter("campfireMemeTemplateID");
        memeGeneratorName = req.getParameter("campfireMemeGeneratorName");
        memeTemplateIDFailure = req.getParameter("campfireMemeTemplateIDFailure");
        memeGeneratorNameFailure = req.getParameter("campfireMemeGeneratorNameFailure");

        if ( hudsonUrl != null && !hudsonUrl.endsWith("/") ) {
            hudsonUrl = hudsonUrl + "/";
        }
        ssl = req.getParameter("campfireSsl") != null;
        smartNotify = req.getParameter("campfireSmartNotify") != null;
        try {
            CampfireNotifier campfireNotifier = new CampfireNotifier(subdomain, 
                    token, room, hudsonUrl, ssl, smartNotify, memeTemplateType,
                    memeTemplateID, memeGeneratorName, memeTemplateIDFailure,
                    memeGeneratorNameFailure);
        } catch (Exception e) {
            throw new FormException("Failed to initialize campfire notifier - check your global campfire notifier configuration settings", e, "");
        }
        save();
        return super.configure(req, json);
    }

    /**
     * @see hudson.model.Descriptor#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return "Campfire Notification";
    }

    /**
     * @see hudson.model.Descriptor#getHelpFile()
     */
    @Override
    public String getHelpFile() {
        return "/plugin/campfire/help.html";
    }
}
