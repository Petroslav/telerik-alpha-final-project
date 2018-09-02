package ExtensionService;

import com.alpha.marketplace.models.*;

import java.util.Date;

class ExtensionServiceSetUp {

    static Extension setUp(String name, String descr, User publisher, String version){
        Extension e = new Extension();
        e.setName(name);
        e.setDescription(descr);
        e.setPublisher(publisher);
        e.setVersion(version);
        return e;
    }

    static Extension setUpApproved(String name, String descr, User publisher, String version){
        Extension e = new Extension();
        e.setName(name);
        e.setDescription(descr);
        e.setPublisher(publisher);
        e.setVersion(version);
        e.setApproved(true);
        return e;
    }

    static Extension setUp(String name, String descr, User publisher, String version, int dls){
        Extension e = new Extension();
        e.setName(name);
        e.setDescription(descr);
        e.setPublisher(publisher);
        e.setVersion(version);
        e.setDownloads(dls);
        return e;
    }

    static Extension setUpApproved(String name, String descr, User publisher, String version, int dls){
        Extension e = new Extension();
        e.setName(name);
        e.setDescription(descr);
        e.setPublisher(publisher);
        e.setVersion(version);
        e.setApproved(true);
        e.setDownloads(dls);
        return e;
    }

    static Extension setUp(String name, String descr, User publisher, String version, boolean selected){
        Extension e = new Extension();
        e.setName(name);
        e.setDescription(descr);
        e.setPublisher(publisher);
        e.setVersion(version);
        e.setSelected(selected);
        return e;
    }

    static Extension setUpApproved(String name, String descr, User publisher, String version, boolean selected){
        Extension e = new Extension();
        e.setName(name);
        e.setDescription(descr);
        e.setPublisher(publisher);
        e.setVersion(version);
        e.setApproved(true);
        e.setSelected(selected);
        e.setSelectionDate(new Date());
        return e;
    }
}
