
package vhoster;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe che si occupa del backup dei file host
 * @author Simone
 */
public class Backup {
    
    private String pathHosts;
    private String pathApache;
    private String pathRootSite;
    private String fileHosts;
    private String fileApache;
    private String chrSet;
    private String fileBkHosts;
    private String fileBkApache;
    
    public Backup(){
        
        Host h = new Host();
        
        this.pathHosts = h.getPath('w');
        this.pathApache = h.getPath('a');
        this.pathRootSite = h.getPath('s');
        this.fileHosts = h.getFile('w');
        this.fileApache = h.getFile('a');
        this.chrSet = h.getCharset();
        this.fileBkHosts = "backup_" + this.fileHosts;
        this.fileBkApache = "backup_" + this.fileApache;
            
    }
    
    /**
     * Crea il backup dei due file di host
     */
    public void createbackup(){
        
        copyFile(this.pathHosts, this.pathApache, this.fileHosts, fileBkHosts, this.chrSet);
        copyFile(this.pathApache, this.pathApache, this.fileApache, fileBkApache, this.chrSet);
    
    }

    /**
     * Crea una copia di un file 
     * @param pathFrom percorso del file da copiare
     * @param pathTo percorso di destinazione del file copiato
     * @param fileHosts nome del file da copiare
     * @param fileBk nome del file di backup
     * @param chrSet charset definito
     */
    public void copyFile(String pathFrom, String pathTo, String fileHosts, String fileBk, String chrSet){
        
        Path absFrom = Paths.get(pathFrom);
        Path absTo = Paths.get(pathTo);
        Path originFile = Paths.get(fileHosts);
        Path backupFile = Paths.get(fileBk);
        Charset chr = Charset.forName(chrSet);       
        String text  = "";
        
        try{
            text = Host.init(absFrom.resolve(originFile),chr);
        } catch ( IOException e ){
            System.out.println( "Error in lettura " + e.getMessage() );
        }
            
        try{
            Host.writeFile(text, absTo.resolve(backupFile), chr);
        } catch ( IOException e ){
            System.out.println( "Errore in scrittura" + e.getMessage() );
        }
        
    }
    
    /**
     * Restore dei file di host all'ultima versione
     */
    public void restore(){
        
        Path absFrom = Paths.get(this.pathApache);
        Path backupFileHost = Paths.get(this.fileBkHosts);
        Path backupFileApache = Paths.get(this.fileBkApache);
        Path fileHost = absFrom.resolve(backupFileHost);
        Path fileApache = absFrom.resolve(backupFileApache);
        
        if( Files.exists(fileHost)){
            copyFile(this.pathApache, this.pathHosts, this.fileBkHosts, this.fileHosts, this.chrSet);
        } else {
            System.out.println("Non ci sono backup disponibili");
        }
        if( Files.exists(fileApache)){
            copyFile(this.pathApache, this.pathApache, this.fileBkApache, this.fileApache, this.chrSet);
        } else {
            System.out.println("Non ci sono backup disponibili");
        }
        
    }
    
}
