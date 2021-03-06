package net.sharkfw.knowledgeBase.inmemory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import net.sharkfw.knowledgeBase.*;

/**
 * InMemory Implementierung eines Kontextpunktes.
 * 
 * @author mfi
 */
public class InMemoContextPoint extends PropertyHolderDelegate implements ContextPoint {
  
    protected Vector<Information> information = new Vector<Information>();

    private ContextCoordinates coords;

    private ContextPointListener listener = null;

    public InMemoContextPoint(ContextCoordinates coordinates){
        coords = coordinates;
    }

    public InMemoContextPoint(SystemPropertyHolder persistentHolder){
        super(persistentHolder);
    }

    public Enumeration<Information> getInformation() {
        Vector informationEnum = (Vector) information.clone();
        return informationEnum.elements();
    }

    @Override
  public int getNumberInformation() {
    return information.size();
  }

  public ContextCoordinates getCoordinates() {
    return coords;
  }

    /**
     *
     * @param props
     * @param duplicatesAllowed
     * @return
     * @throws SharkKBException
     */
    public Information addInformation(Hashtable props, boolean duplicatesAllowed) throws SharkKBException {
    	Information info = new InMemoInformation();

    	/*
    	 * Should set UNIQUENAME with "" as property?
    	 */
    	if(props != null){
    		Enumeration keys = props.keys();
    		while(keys.hasMoreElements()){
    			String key = (String) keys.nextElement();
    			String value = (String) props.get(key);
    			info.setProperty(key, value);
    		}
    	}
    	this.addInformation(info);
    	return info;
    }

  /**
   * 
   * @param info
   */
    @Override
    public void addInformation(Information info){
    	boolean alreadyIn = false;

    	Enumeration infoEnum = this.information.elements();
    	while(infoEnum.hasMoreElements()) {
    		Information current = (Information) infoEnum.nextElement();
    		if(current.hashCode() == info.hashCode()) {
    			alreadyIn = true;
    		}
    	}

    	if(!alreadyIn) {
    		information.add(info);
    	}

    	/*
    	 * notify listener
    	 */
    	if(this.listener != null) {
    		this.listener.addedInformation(info, this);
    	}
    }

    @Override
  public void removeInformation(Information info) {
    information.remove(info);

    /*
     * Notify listener
     */
    if(this.listener != null) {
      this.listener.removedInformation(info, this);
    }
  }

    @Override
  public void setListener(ContextPointListener cpl) {
    this.listener = cpl;
  }

    @Override
  public void removeListener() {
    this.listener = null;
  }
    
    protected void putInformation(Information info) {
        this.information.add(info);    
    }

  // API rev methods
    @Override
  public Information addInformation() {
    Information newInfo = new InMemoInformation();
    this.putInformation(newInfo);
    return newInfo;
  }

    @Override
  public Information addInformation(InputStream is, long len) {
    Information info = this.addInformation();
    info.setContent(is, len);
    return info;
  }

    @Override
  public Information addInformation(byte[] content) {
    Information info = this.addInformation();
    info.setContent(content);
    return info;
  }

    @Override
  public Information addInformation(String content) {
    Information info = this.addInformation();
    info.setContentType("text/plain");
    info.setContent(content);
    return info;
  }

    @Override
  public Enumeration enumInformation() {
    return this.information.elements();
  }
    
    
    @Override
    public Iterator<Information> getInformation(String name) {
        ArrayList<Information> infoList = new ArrayList();
        
        Enumeration<Information> infoEnum = this.getInformation();
        if(infoEnum != null) {
            while(infoEnum.hasMoreElements()) {
                Information info = infoEnum.nextElement();
                String infoName = info.getName();
                
                if( (name == null && infoName == null)
                    ||
                    ( 
                        name != null 
                        && 
                        infoName != null
                        &&
                        name.equalsIgnoreCase(infoName)
                    )
                  )
                    {
                        infoList.add(info);
                    }
                }
            }
        
        return infoList.iterator();
    }

    @Override
  public ContextCoordinates getContextCoordinates() {
    return this.coords;
  }

    @Override
    public void setContextCoordinates(ContextCoordinates cc) {
        this.coords = cc;
        this.persist();
    }    
}
