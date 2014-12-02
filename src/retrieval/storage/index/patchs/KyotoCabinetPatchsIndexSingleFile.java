/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package retrieval.storage.index.patchs;

import java.util.Map;
import kyotocabinet.Cursor;
import kyotocabinet.DB;
import org.apache.log4j.Logger;
import retrieval.server.globaldatabase.GlobalDatabase;
import retrieval.storage.exception.CloseIndexException;
import retrieval.storage.exception.ReadIndexException;

/**
 *
 * @author lrollus
 */
public class KyotoCabinetPatchsIndexSingleFile implements PicturePatchsIndex {

    /**
     * Store map
     */
    protected DB map;
    protected String prefix;
    /**
     * Logger
     */
    private static Logger logger = Logger.getLogger(KyotoCabinetPatchsIndexSingleFile.class);

    /**
     * Constructor to build a Patchs Map in Memory
     * @param read No effect (just memory, don't read anything)
     * @throws ReadIndexException Error during the Read
     */
    public KyotoCabinetPatchsIndexSingleFile(GlobalDatabase global,String idServer)
            throws ReadIndexException {
        logger.info("KyotoCabinetPatchsIndexSingleFile: start");
        this.map = (DB)global.getDatabasePatchs();
        this.prefix = idServer+"#";
        
    }
    
   /**
     * Delete all image ID key in index
     * @param picturesID Image ID to delete (just look the key)
     */
    public void delete(Map<Long, Integer> picturesID) {
        for (Map.Entry<Long, Integer> entry : picturesID.entrySet()) {
            System.out.println("map="+map);
            System.out.println("prefix="+prefix);
            System.out.println("entry.getKey()="+entry.getKey());
            map.remove(prefix+entry.getKey().toString());
        }
    }

    /**
     * Add a new image id and its N value (number of patch extracted to index)
     * @param imageID Image I
     * @param N NI (Number of patch extracted from I to index it)
     */
    public void put(Long imageID, Integer N) {
        map.set(prefix+imageID.toString(), N.toString());
    }

    /**
     * Get the NI value of image I
     * @param imageID I
     * @return Number of patch extracted from I to index it
     */
    public Integer get(Long imageID) {
        String numberOfPatch = map.get(prefix+imageID.toString());
        if (numberOfPatch == null) {
            return -1;            
        } else {
            return Integer.parseInt(numberOfPatch);
            
        }
    }

    /**
     * Print index
     */
    public void print() {
       // traverse records
        logger.info("PatchIndex");
        Cursor cur = map.cursor();
        cur.jump();
        String[] rec;
        while ((rec = cur.get_str(true)) != null) {

              logger.info(rec[0] + "=" + rec[1]);

        }
        cur.disable();
    }

    /**
     * Close index
     * @throws CloseIndexException Error during index close
     */
    public void close() throws CloseIndexException {
        map.close();
    
    }

    public void sync() {

    }    
}
