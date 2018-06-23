package com.github.dual_navdrawer;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLReader {
    private Document XmlDoc = null;

    public void Initialize(Context context, String fname) {
        try {
            InputStream XmlFile = context.getAssets().open(fname);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            XmlDoc = dBuilder.parse(XmlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Element getElement(String TagName) {
        NodeList nList = XmlDoc.getElementsByTagName(TagName);
        Node nNode = nList.item(0);
        return (Element) nNode;
    }

    public Element getChildElmt(Element Elmt, String TagName) {
        NodeList nList = Elmt.getElementsByTagName(TagName);
        Node nNode = nList.item(0);
        return (Element) nNode;
    }

    public Element getRootElmt() {
        return XmlDoc.getDocumentElement();
    }

    public String getRootAttr(String AttrName) {
        return XmlDoc.getDocumentElement().getAttribute(AttrName);
    }
}