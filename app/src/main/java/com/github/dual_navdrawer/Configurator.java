package com.github.dual_navdrawer;

import android.content.Context;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Configurator {
    private String fName;
    private ConfHandler conf;
    private Context cont;

    public Configurator(Context context) {
        cont = context;
    }

    public void setConfig(String fileName) {
        fName = fileName;
    }

    public void setConfigHandler(ConfHandler configHandler) {
        conf = configHandler;
    }

    public void startParse() {
        XMLReader XMLRead = new XMLReader();

        // Read XMLFile
        XMLRead.Initialize(cont, fName);

        Element iElmt, jElmt, kElmt, appElmt, childElmt, rootElmt;
        boolean shortcut;
        String moduleName;

        appElmt = XMLRead.getElement("includes"); // Includes element
        for (int i = 1; i < appElmt.getChildNodes().getLength(); i+=2) {
            Node iNode = appElmt.getChildNodes().item(i);
            iElmt = (Element) iNode; // Include element

            // Get and read the XML module
            XMLRead.Initialize(cont, iElmt.getAttribute("file"));

            rootElmt = XMLRead.getRootElmt(); // Module element
            moduleName = XMLRead.getRootAttr("name");

            if ("LeftDrawer".equals(moduleName)) {
                childElmt = XMLRead.getChildElmt(rootElmt, "left"); // Left element
                for (int j = 1; j < childElmt.getChildNodes().getLength(); j += 2) {
                    Node jNode = childElmt.getChildNodes().item(j);
                    jElmt = (Element) jNode; // Left item or Left list element

                    if (jElmt.getTagName().equalsIgnoreCase("item")) { // Left item
                        shortcut = (jElmt.getAttribute("shortcut").equalsIgnoreCase("TRUE"));
                        conf.onConfLeft(jElmt.getAttribute("label"), jElmt.getAttribute("icon"), shortcut);
                    } else // Left list
                        for (int k = 0; k < jElmt.getElementsByTagName("item").getLength(); k++) {
                            Node kNode = jElmt.getElementsByTagName("item").item(k);
                            kElmt = (Element) kNode; // Left item element

                            shortcut = (kElmt.getAttribute("shortcut").equalsIgnoreCase("TRUE"));
                            conf.onConfLeft(kElmt.getAttribute("label"), kElmt.getAttribute("icon"), shortcut);
                        }
                }
            } else if ("RightDrawer".equals(moduleName)) {
                childElmt = XMLRead.getChildElmt(rootElmt, "right"); // Right element
                for (int j = 1; j < childElmt.getChildNodes().getLength(); j+=2) {
                    Node jNode = childElmt.getChildNodes().item(j);
                    jElmt = (Element) jNode; // Right item element

                    shortcut = (jElmt.getAttribute("shortcut").equalsIgnoreCase("TRUE"));
                    conf.onConfRight(jElmt.getAttribute("label"),jElmt.getAttribute("icon"),shortcut);
                }
            }
        }
    }
}
