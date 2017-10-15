package com.ailk.biapp.ci.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLConfigReader extends ResourceBundle {
    protected Hashtable<Object, Object> hashcontents = null;
    protected int numberOfItems = 0;
    protected Vector<Object> vectOfItems;

    public XMLConfigReader(String xmlfile) {
        Document document = this.parseXmlFile(xmlfile, false);
        if(document == null) {
            this.hashcontents = new Hashtable();
        } else {
            NodeList listOfItems = document.getElementsByTagName("item");
            this.numberOfItems = listOfItems.getLength();
            this.vectOfItems = new Vector(this.numberOfItems);
            this.hashcontents = new Hashtable(this.numberOfItems);

            for(int i = 0; i < this.numberOfItems; ++i) {
                NodeList listOfSUBs = null;
                boolean numberOfSUBs = false;
                NamedNodeMap temp_list = listOfItems.item(i).getAttributes();
                Attr temp_attr = (Attr)temp_list.item(0);
                String temp_key = temp_attr.getValue();
                this.vectOfItems.add(temp_key);
                Node SUBs = listOfItems.item(i);
                listOfSUBs = SUBs.getChildNodes();
                int var17 = listOfSUBs.getLength();
                Hashtable hashsubcontents = new Hashtable(var17);

                for(int j = 0; j < var17; ++j) {
                    if(listOfSUBs.item(j).getNodeType() == 1) {
                        String temp_name = null;
                        String temp_value = null;
                        temp_name = listOfSUBs.item(j).getNodeName();

                        try {
                            temp_value = listOfSUBs.item(j).getFirstChild().getNodeValue();
                        } catch (Exception var16) {
                            temp_value = "";
                        }

                        if(temp_name != null && temp_value != null) {
                            hashsubcontents.put(temp_name, temp_value);
                        }
                    }
                }

                if(temp_key != null && hashsubcontents != null) {
                    this.hashcontents.put(temp_key, hashsubcontents);
                }
            }

        }
    }

    public Document parseXmlFile(String filename, boolean validating) {
        Document doc = null;
        DocumentBuilderFactory factory = null;

        try {
            factory = DocumentBuilderFactory.newInstance();
        } catch (FactoryConfigurationError var16) {
            var16.printStackTrace();
            return null;
        }

        factory.setValidating(validating);

        try {
            try {
                InputStream e = this.getClass().getResourceAsStream(filename);
                doc = factory.newDocumentBuilder().parse(e);
            } catch (Exception var13) {
                try {
                    doc = factory.newDocumentBuilder().parse(filename);
                } catch (IOException var12) {
                    try {
                        doc = factory.newDocumentBuilder().parse(new File(filename));
                    } catch (IOException var11) {
                        try {
                            String[] epath = System.getProperties().getProperty("java.class.path", ".").split(";");
                            String newpath = epath[0] + "/" + filename;
                            doc = factory.newDocumentBuilder().parse(new File(newpath));
                        } catch (IOException var10) {
                            System.err.println("IOException:" + var10);
                        }
                    }
                }
            }
        } catch (ParserConfigurationException var14) {
            System.err.println("[" + filename + "] ParserConfigurationException:" + var14);
        } catch (SAXException var15) {
            System.err.println("[" + filename + "] SAXException:" + var15);
        }

        return doc;
    }

    public final Object handleGetObject(String key) throws MissingResourceException {
        return this.hashcontents.get(key);
    }

    public int getNumberOfItems() {
        return this.numberOfItems;
    }

    public Enumeration getKeys() {
        return this.vectOfItems.elements();
    }

    public String getString(String key, String subkey, String def) {
        String param_value = "";

        try {
            Hashtable e = (Hashtable)this.getObject(key);
            param_value = (String)e.get(subkey);
            return param_value != null && param_value.length() > 0?param_value:def;
        } catch (Exception var6) {
            return def;
        }
    }

    public String getString(String key, String subkey) {
        return this.getString(key, subkey, "");
    }

    public int getInt(String key, String subkey, int def) {
        boolean i = false;
        String param_value = this.getString(key, subkey);

        int i1;
        try {
            i1 = Integer.parseInt(param_value);
        } catch (NumberFormatException var7) {
            i1 = def;
        }

        return i1;
    }

    public double getDouble(String key, String subkey, double def) {
        double i = 0.0D;
        String param_value = this.getString(key, subkey);

        try {
            i = Double.parseDouble(param_value);
        } catch (NumberFormatException var9) {
            i = def;
        }

        return i;
    }

    public String getKey(String subkey, String value) {
        Enumeration data_keys = this.getKeys();

        String current_key;
        do {
            if(!data_keys.hasMoreElements()) {
                return null;
            }

            current_key = (String)data_keys.nextElement();
        } while(this.getString(current_key, subkey).compareToIgnoreCase(value) != 0);

        return current_key;
    }
}
